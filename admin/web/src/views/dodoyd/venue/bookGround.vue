<template>
  <div class="app-container">
    <!-- 上方选择框   -->
    <el-form ref="queryRequest" :model="queryRequest">
      <el-row :gutter="20">
        <el-col :offset="1" :span="4">
          <el-form-item label-width="80px" label="场地类型:">
            <el-select v-model="queryRequest.groundTypeFilter" v-loading.fullscreen.lock="fullscreenLoading" @change="handleQueryClick">
              <el-option v-for="item in groundFilterOptions" :key="item.key" :label="item.name" :value="item.key" />
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
        <el-col v-if="chooseList.length > 0" :span="2">
          <el-button type="primary" @click="handBookGroundClick">预订</el-button>
        </el-col>
        <el-col v-if="chooseList.length > 0" :span="2">
          <el-button type="primary" @click="handleBookCoachClick">约课</el-button>
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
    <el-row>
      <!-- 场地选择区域 -->
      <el-col :offset="1" :span="18">
        <div style="display: flex">
          <BrickGap :start-time="venueData.openStart" :end-time="venueData.openEnd" />
          <!-- 场地列表 -->
          <BrickGroup v-for="ground in groundList" :key="ground.groundId" :title="ground.groundName">
            <Brick
              v-for="item in ground.itemList"
              :key="item.index"
              :type="item.showType"
              @click="handleGroundClick(ground, item)"
            >
              <span v-if="item.hasBooked === 1">
                <!--  状态,0:未知,1:预订,2:上课,3:内订 -->
                <template v-if="item.useType === 2">上课</template>
                <template v-else>已订</template>
              </span>
              <span v-else-if="!(item.hasPassed === 1)">{{ '￥' + item.groundPrice }}</span>
            </Brick>
          </BrickGroup>
        </div>
      </el-col>
      <!-- 已选区域 -->
      <el-col v-if="groundList.length <= 10" :span="3">
        <el-card v-if="chooseList.length > 0" style="margin-top: 10px;">
          <div slot="header">
            <span>已选场次({{ chooseList.length }})</span>
          </div>
          <div style="display: flex; flex-direction: column; justify-content: center; align-items: center;">
            <div v-for="item in chooseList" :key="item.key" class="item-wrap">
              <div class="item-title">
                {{ item.groundClock + ':00 - ' + (item.groundClock + 1) + ':00' }}
              </div>
              <div class="item-content">
                {{ item.groundName }}
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <!-- 已选区域 -->
    <el-row v-if="groundList.length > 10">
      <el-col :offset="1" :span="18">
        <el-card v-if="chooseList.length > 0" style="margin-top: 10px;">
          <div slot="header">
            <span>已选场次({{ chooseList.length }})</span>
          </div>
          <div style="display: flex; flex-wrap: wrap;">
            <div v-for="item in chooseList" :key="item.key" class="item-wrap">
              <div class="item-title">
                {{ item.groundClock + ':00 - ' + (item.groundClock + 1) + ':00' }}
              </div>
              <div class="item-content">
                {{ item.groundName }}
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <!-- 预订弹窗 -->
    <el-dialog title="确认预订？" :visible.sync="bookDialogVisible" width="30%">
      <el-form :model="bookForm">
        <el-form-item v-if="bookForm.useType === 2" label="教练" label-width="50px">
          <el-select v-model="bookForm.coachUserId" placeholder="请选择教练" clearable>
            <el-option v-for="user in coachList" :key="user.userId" :label="user.nickname" :value="user.userId">
              <div class="avatar-user-box">
                <el-avatar :size="30" :src="user.avatar">{{ user.nickname }}</el-avatar>
                <span class="avatar-user">{{ user.nickname }}</span>
              </div>
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="备注" label-width="50px">
          <el-input v-model="bookForm.remark" placeholder="填写备注可更清晰的记录信息" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="bookDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitBookGround">确 定</el-button>
      </div>
    </el-dialog>
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
          <el-descriptions-item v-if="useRecordDetail.bookOrderId" label="预定单号">
            <router-link :to="{ path: '/order/orderList', query: { orderNo: useRecordDetail.bookOrderId } }">
              <el-tag type="info" effect="plain">{{ useRecordDetail.bookOrderId }}</el-tag>
            </router-link>

          </el-descriptions-item>
          <el-descriptions-item label="备注">{{ useRecordDetail.remark }}</el-descriptions-item>
        </el-descriptions>
      </el-card>
      <div slot="footer">
        <el-button v-if="useRecordDetail.bookOrderId" type="primary" :disabled="useRecordDetail.passed" @click="gotoRefund(useRecordDetail.bookOrderId)">去退款</el-button>
        <el-button v-else type="primary" :disabled="useRecordDetail.passed" @click="submitCancelBookGround(useRecordDetail)">取消预定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import BrickGap from '@/views/components/BrickGap'
import BrickGroup from '@/views/components/BrickGroup'
import Brick from '@/views/components/Brick'
import { parseTime } from '@/utils'
import { dateList, getVenueGroundTypeList, showGroundBook, bookGround, cancelBookGround, getVenueUserList, queryUseRecordDetail } from '@/api/dodoyd'

export default {
  name: 'BookGround',
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
        // 场地过滤条件, 0:全部,1:网球,2:羽毛球,3:篮球,4:足球
        groundTypeFilter: 0,
        dateStr: parseTime(new Date(), '{y}{m}{d}')
      },
      // 预定信息
      bookForm: {
        // 状态,0:未知,1:预订,2:上课,3:内订
        useType: null,
        venueId: null,
        dateStr: '',
        groundBookList: null,
        coachUserId: null,
        remark: null
      },
      bookDialogVisible: false,
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
    this.getDayList()
    this.getGroundTypeList()
    this.getCoachList()
    this.getDataList()
  },
  methods: {
    getDataList() {
      // this.fullscreenLoading = true
      this.queryRequest.venueId = this.venueId
      showGroundBook(this.queryRequest).then(response => {
        if (response.code === 0) {
          this.venueData = response.data
          this.groundList = response.data.groundList
          this.chooseList = []
          // 遍历所有场地
          this.groundList.forEach(ground => {
            // 遍历场地中的场次
            ground.itemList.forEach(item => {
              this.handleShowType(item)
            })
          })
        }
        this.chooseList.splice(0)
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
    // 获取表格内的数据列表
    getCoachList() {
      getVenueUserList({ venueId: this.venueId, roleFilter: 3 }).then(response => {
        if (response.code === 0) {
          this.coachList = response.data
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
          groundId: ground.groundId,
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
        console.log('has passed')
      } else {
        // 没过也没订，只剩可订的情况
        if (item.choosed === 1) {
          // 原来选中，则为取消选中
          item.choosed = 0
          item.showType = item.oldShowType
        } else {
          // 原来未选中，则为选中
          if (this.chooseList.length >= 8) {
            this.$message({
              showClose: true,
              message: '一次最多可选8个场次',
              type: 'warning'
            })
            return
          }
          item.choosed = 1
          item.oldShowType = item.showType
          item.showType = 'active'
        }
        this.updateChooseList(ground, item)
        // 用.slice(0)重新设置一遍table的dataList，强制刷新，参考：https://www.cnblogs.com/wiliam/p/13892649.html
        ground.itemList = ground.itemList.slice(0)
      }
    },
    handleShowType(groundItem) {
      if (groundItem.hasBooked === 1) {
        groundItem.showType = 'used'
      } else if (groundItem.hasPassed === 0) {
        groundItem.showType = 'available'
      }
    },
    // 更新已选数据列表
    updateChooseList(ground, groundItem) {
      const chooseList = this.chooseList
      // 唯一key = 场地+场次
      const key = ground.groundId.toString() + groundItem.groundClock.toString()
      // 新选中
      if (groundItem.choosed === 1) {
        const item = {}
        item.groundId = ground.groundId
        item.groundName = ground.groundName
        item.groundClock = groundItem.groundClock
        // 设置唯一key用于比对
        item.key = key
        chooseList.push(item)
      } else {
        // 取消选中
        const index = this.findChooseListIndex(key)
        if (index >= 0) {
          chooseList.splice(index, 1)
        }
      }
      // 用.slice(0)重新设置一遍dataList，强制刷新，参考：https://www.cnblogs.com/wiliam/p/13892649.html
      this.chooseList = chooseList.slice(0)
    },
    // 在已选列表中查找制定key的元素，返回下标。未找到返回-1
    findChooseListIndex(key) {
      const chooseList = this.chooseList
      let index = -1
      for (let i = 0; i < chooseList.length; ++i) {
        if (chooseList[i].key === key) {
          // 找到则返回下标
          index = i
        }
      }
      return index
    },
    // 点击预订按钮
    handBookGroundClick() {
      // 状态,0:未知,1:预订,2:上课,3:内订
      this.bookForm.useType = 3
      this.bookForm.coachUserId = null
      this.bookDialogVisible = true
    },
    // 点击约课按钮
    handleBookCoachClick() {
      // 状态,0:未知,1:预订,2:上课,3:内订
      this.bookForm.useType = 2
      this.bookDialogVisible = true
    },
    // 提交预订请求
    submitBookGround() {
      if (!this.bookForm.coachUserId && this.bookForm.useType === 2) {
        this.$message({
          showClose: true,
          message: '请选择教练！',
          type: 'error'
        })
        return
      }
      this.bookForm.venueId = this.venueId
      this.bookForm.dateStr = this.queryRequest.dateStr
      this.bookForm.groundBookList = this.chooseList
      if (!this.bookForm.coachUserId) {
        this.bookForm.coachUserId = null
      }
      // bookForm.coachUserId 和 remark已绑定内容
      bookGround(this.bookForm).then(response => {
        if (response.code === 0) {
          this.$message({ type: 'success', message: '预订成功!' })
          this.getDataList()
          this.bookDialogVisible = false
          this.remark = null
        } else {
          this.$message({ type: 'error', message: response.message })
        }
      }).catch(err => {
        console.log(err)
      })
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

.item-wrap {
  width: 100px;
  height: 60px;
  overflow: hidden;
  font-size: 12px;
  border-radius: 5px;
  border: solid #4A9FF9;
  margin-right: 10px;
  margin-bottom: 10px;
}
.item-title {
  width: 100px;
  height: 30px;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #409EFF;
  color: #ffffff;
}
.item-content {
  width: 100px;
  height: 30px;
  display: flex;
  justify-content: center;
  align-items: center;
  color: #409EFF;
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
