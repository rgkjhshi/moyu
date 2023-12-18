<template>
  <div class="app-container">
    <!-- 上方选择框   -->
    <el-form ref="queryRequest" :model="queryRequest">
      <el-row :gutter="20">
        <el-col :offset="1" :span="4">
          <el-form-item label-width="80px" label="场地类型:">
            <el-select v-model="queryRequest.groundTypeFilter" @change="handleQueryClick">
              <el-option v-for="item in groundFilterOptions" :key="item.key" :label="item.name" :value="item.key" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="4">
          <el-form-item label="教练" label-width="50px">
            <el-select v-model="queryRequest.coachUserId" placeholder="请选择教练" clearable @change="handleQueryClick">
              <el-option v-for="user in coachList" :key="user.userId" :label="user.nickname" :value="user.userId">
                <div class="avatar-user-box">
                  <el-avatar :size="30" :src="user.avatar">{{ user.nickname }}</el-avatar>
                  <span class="avatar-user">{{ user.nickname }}</span>
                </div>
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label-width="80px" label="日期:">
            <el-date-picker
              v-model="queryRequest.dateStr"
              value-format="yyyyMMdd"
              type="date"
              placeholder="选择日期"
              :clearable="false"
              :picker-options="pickerOptions"
              @change="handleQueryClick"
            />
          </el-form-item>
        </el-col>
        <el-col :span="3">
          <el-button type="primary" icon="el-icon-search" @click="handleQueryClick">查询</el-button>
        </el-col>
      </el-row>
    </el-form>
    <!-- 日期预览tab区 -->
    <el-row>
      <el-col :offset="1" :span="20">
        <el-tabs v-model="queryRequest.dateStr" type="card" @tab-click="handleTabClick">
          <el-tab-pane v-for="(item, index) in dayList" :key="index" :name="item.dateStr">
            <div slot="label">
              <span class="tab">{{ item.weekday }}</span>
              <span class="tab">{{ item.showDate }}</span>
            </div>
          </el-tab-pane>
        </el-tabs>
      </el-col>
    </el-row>
    <!-- 场地选择区域 -->
    <el-row>
      <el-col :offset="1" :span="18">
        <div style="display: flex">
          <BrickGap :start-time="venueData.openStart" :end-time="venueData.openEnd" />
          <!-- 教练课程表 -->
          <BrickGroup v-for="ground in groundList" :key="ground.groundId" :title="ground.coachNickname">
            <Brick
              v-for="item in ground.itemList"
              :key="item.index"
              :type="item.showType"
              @click="handleGroundClick(ground, item)"
            >
              <span v-if="item.hasBooked === 1">{{ item.groundName }}</span>
            </Brick>
          </BrickGroup>
        </div>
      </el-col>
    </el-row>
    <!-- 预订信息 -->
    <el-dialog title="预订信息" :visible.sync="detailDialogVisible" width="30%" center>
      <el-card>
        <el-descriptions :column="1" border label-class-name="LS">
          <el-descriptions-item label="预订人">{{ useRecordDetail.bookUserName }}</el-descriptions-item>
          <el-descriptions-item label="预订人手机">
            <el-tooltip :content="useRecordDetail.bookUserMobile" placement="top" effect="light">
              <span>{{ useRecordDetail.bookUserMaskMobile }}</span>
            </el-tooltip>
          </el-descriptions-item>
          <el-descriptions-item v-if="useRecordDetail.useType === 2" label="场地用途">上课</el-descriptions-item>
          <el-descriptions-item v-if="useRecordDetail.useType === 2" label="教练">{{ useRecordDetail.useUserName }}</el-descriptions-item>
          <el-descriptions-item label="场地名称">{{ useRecordDetail.groundName }}</el-descriptions-item>
          <el-descriptions-item label="场次时间">{{ useRecordDetail.startTime | parseTime('{y}-{m}-{d} {h}:{i}') }}</el-descriptions-item>
          <el-descriptions-item label="预定时间">{{ useRecordDetail.addTime }}</el-descriptions-item>
          <el-descriptions-item v-if="useRecordDetail.bookOrderId" label="预定单号">{{ useRecordDetail.bookOrderId }}</el-descriptions-item>
          <el-descriptions-item label="备注">{{ useRecordDetail.remark }}</el-descriptions-item>
        </el-descriptions>
      </el-card>
      <div slot="footer">
        <el-button v-if="useRecordDetail.bookOrderId" type="primary" :disabled="useRecordDetail.passed" @click="gotoRefund(useRecordDetail.bookOrderId)">去退款</el-button>
        <el-button v-else type="primary" :disabled="useRecordDetail.passed" @click="submitCancelBookGround(useRecordDetail)">取消课程</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import BrickGap from '@/views/components/BrickGap'
import BrickGroup from '@/views/components/BrickGroup'
import Brick from '@/views/components/Brick'
import { parseTime } from '@/utils'
import { dateList, getVenueGroundTypeList, showCoachBook, cancelBookGround, getVenueUserList, queryUseRecordDetail } from '@/api/dodoyd'

export default {
  name: 'CoachBookList',
  components: { Brick, BrickGap, BrickGroup },

  data() {
    return {
      fullscreenLoading: false,
      dayList: [],
      coachList: [],
      venueData: {},
      groundList: [],
      chooseList: [],
      queryRequest: {
        venueId: null,
        coachUserId: null,
        // 场地过滤条件, 0:全部,1:网球,2:羽毛球,3:篮球,4:足球
        groundTypeFilter: 0,
        dateStr: parseTime(new Date(), '{y}{m}{d}')
      },
      detailDialogVisible: false,
      // 预定记录
      useRecordDetail: {},
      // 0:全部,1:网球,2:羽毛球,3:篮球,4:足球
      groundFilterOptions: [
        { key: 0, name: '全部' },
        { key: 1, name: '网球' },
        { key: 2, name: '羽毛球' },
        { key: 3, name: '篮球' },
        { key: 4, name: '足球' }
      ],
      pickerOptions: {
        // 周起始日,1 到 7
        firstDayOfWeek: 1,
        disabledDate: (time) => {
          const now = new Date()
          // 时间跨度
          const range = 30 * 24 * 3600 * 1000
          // true：不可以选择该日期；false：可以选择该日期。
          return time.getTime() > (now.getTime() + range)
        },
        shortcuts: [{
          text: '明天',
          onClick(picker) {
            const date = new Date()
            date.setTime(date.getTime() + 3600 * 1000 * 24)
            picker.$emit('pick', date)
          }
        }, {
          text: '今天',
          onClick(picker) {
            picker.$emit('pick', new Date())
          }
        }, {
          text: '昨天',
          onClick(picker) {
            const date = new Date()
            date.setTime(date.getTime() - 3600 * 1000 * 24)
            picker.$emit('pick', date)
          }
        }]
      }
    }
  },
  computed: {
    venueId() {
      let venueId = this.$store.getters.venueId
      if (!venueId) {
        venueId = localStorage.getItem('venueId')
      }
      return venueId
    }
  },
  created() {
    this.getGroundTypeList()
    this.getDayList()
    this.getCoachList()
    this.getDataList()
  },
  methods: {
    getDataList() {
      // this.fullscreenLoading = true
      this.queryRequest.venueId = this.venueId
      // 删除所有的null属性和空串
      for (var key in this.queryRequest) {
        if (this.queryRequest[key] == null || this.queryRequest[key] === '') {
          delete this.queryRequest[key]
        }
      }
      showCoachBook(this.queryRequest).then(response => {
        if (response.code === 0) {
          this.venueData = response.data
          this.groundList = response.data.coachList
          // 遍历所有场地
          this.groundList.forEach(ground => {
            // 遍历场地中的场次
            ground.itemList.forEach(item => {
              this.handleShowType(item)
            })
          })
        }
        this.fullscreenLoading = false
      }).catch(e => {
        this.fullscreenLoading = false
      })
    },
    // 获取日期bab内容
    getDayList() {
      dateList().then(response => {
        if (response.code === 0) {
          this.dayList = response.data
        }
      })
    },
    // 获取表格内的数据列表
    getCoachList() {
      getVenueUserList({ venueId: this.venueId, roleFilter: 3 }).then(response => {
        if (response.code === 0) {
          this.coachList = response.data
        }
      })
    },
    // 查询场馆场地类型列表
    getGroundTypeList() {
      getVenueGroundTypeList(this.venueId).then(response => {
        if (response.code === 0) {
          const groundTypeOptions = [{ key: 0, name: '全部' }]
          const groundTypeList = response.data
          if (groundTypeList.indexOf(1) > -1) {
            groundTypeOptions.push({ key: 1, name: '网球' })
          }
          if (groundTypeList.indexOf(2) > -1) {
            groundTypeOptions.push({ key: 1, name: '羽毛球' })
          }
          if (groundTypeList.indexOf(3) > -1) {
            groundTypeOptions.push({ key: 1, name: '篮球' })
          }
          if (groundTypeList.indexOf(4) > -1) {
            groundTypeOptions.push({ key: 1, name: '足球' })
          }
          this.groundFilterOptions = groundTypeOptions
        }
      })
    },
    handleQueryClick() {
      this.getDataList()
    },
    // 切换Tab
    handleTabClick(tab, event) {
      this.getDataList()
    },
    handleGroundClick(ground, item) {
      if (item.hasBooked === 1) {
        queryUseRecordDetail({
          groundId: item.groundId,
          startTime: item.startTime
        }).then(response => {
          if (response.code === 0) {
            this.useRecordDetail = response.data
            if (item.hasPassed === 1) {
              this.useRecordDetail.passed = true
            }
            this.detailDialogVisible = true
          }
        })
        console.log('已经预订了')
      } else if (item.hasPassed === 1) {
        console.log('已经过去了')
      } else if (item.hasAvailableGround === 0) {
        console.log('没有空场地了')
      } else {
        console.log('教练空闲')
      }
    },
    handleShowType(coachItem) {
      if (coachItem.hasBooked === 1) {
        coachItem.showType = 'used'
      } else if (coachItem.hasPassed === 0 && coachItem.hasAvailableGround === 1) {
        coachItem.showType = 'available'
      }
    },
    // 提交取消预订请求
    submitCancelBookGround(useRecordDetail) {
      cancelBookGround({
        id: useRecordDetail.id,
        groundId: useRecordDetail.groundId
      }).then(response => {
        if (response.code === 0) {
          this.$message({ type: 'success', message: '取消成功!' })
          this.getDataList()
          this.detailDialogVisible = false
        } else {
          this.$message({ type: 'error', message: response.message })
        }
      }).catch(err => {
        console.log(err)
      })
    },
    // 去退款
    gotoRefund(orderNo) {
      this.$router.push({ path: '/order/orderList', query: { orderNo: orderNo }})
    }
  }
}
</script>

<style>
/* tab内容的样式 */
.tab {
  width: 60px;
  height: 30px;
  display: flex;
  justify-content: center;
  align-items: center;
}

/* tab的高度自适应 */
.el-tabs__item {
  height: 100%;
}

.avatar-user-box {
  display: flex;
}
.avatar-user {
  padding-left: 20px;
  height: 30px;
  line-height: 30px;
  font-size: 16px;
  color: rgba(0, 0, 0, 0.6);
}
</style>
