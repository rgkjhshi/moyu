<template>
  <div class="app-container">
    <!-- 上方选择框   -->
    <el-form ref="queryRequest" :model="queryRequest">
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item label-width="80px" label="日期范围:">
            <el-date-picker
              v-model="defaultDate"
              value-format="yyyyMMdd"
              type="daterange"
              unlink-panels
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              :picker-options="pickerOptions"
            />
          </el-form-item>
        </el-col>
        <el-col :span="4">
          <el-form-item label-width="80px" label="场地类型:">
            <el-select v-model="queryRequest.groundTypeFilter" @change="handleClickQuery">
              <el-option v-for="item in groundFilterOptions" :key="item.key" :label="item.name" :value="item.key" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="4">
          <el-form-item label-width="60px" label="用途:">
            <el-select v-model="queryRequest.useTypeFilter" @change="handleClickQuery">
              <!-- 预订用途，0:未知,1:预订,2:上课,3:内订 -->
              <el-option label="全部" :value="null" />
              <el-option label="用户预订" :value="1" />
              <el-option label="教练上课" :value="2" />
              <el-option label="内部预订" :value="3" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-button type="primary" icon="el-icon-search" @click="handleClickQuery">
            查询
          </el-button>
          <el-button v-if="dataList.length>0" :loading="downloadLoading" style="margin:0 0 20px 20px;" type="primary" icon="el-icon-document" @click="handleDownload">
            导出表格
          </el-button>
        </el-col>
      </el-row>
    </el-form>
    <!-- 我的申请 列表数据 -->
    <el-table v-loading="listLoading" :data="dataList" border fit highlight-current-row>
      <el-table-column label="序号" type="index" width="60px" align="center" />
      <el-table-column prop="groundName" label="场地名称" width="180px" align="center" sortable />
      <el-table-column label="场地类型" width="100px" align="center">
        <template v-slot="{row}">
          <!--  场地类型, 0:全部,1:网球,2:羽毛球,3:篮球,4:足球 -->
          <el-tag>{{ row.groundType | groundTypeFilter }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="startTime" label="场次时间" width="140px" align="center">
        <template v-slot="{row}">
          <span>{{ row.startTime | parseTime('{y}-{m}-{d} {h}:{i}') }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="bookUserName" label="预订人" width="120px" align="center" />
      <el-table-column prop="useType" label="用途" width="100px" align="center">
        <template v-slot="{row}">
          <!--  状态,0:未知,1:预订,2:上课,3:内订 -->
          <el-tag v-if="row.useType === 2">上课</el-tag>
          <el-tag v-else>预订</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="bookOrderId" label="订单" width="240px" align="center">
        <template v-slot="{row}">
          <router-link :to="{ path: '/order/orderList', query: { orderNo: row.bookOrderId } }">
            <el-tag v-if="row.bookOrderId" type="info" effect="plain">{{ row.bookOrderId }}</el-tag>
          </router-link>
        </template>
      </el-table-column>
      <el-table-column prop="useUserName" label="教练" width="120px" align="center" />
      <el-table-column prop="remark" label="备注" width="220px" align="center" />
      <el-table-column prop="addTime" label="预订时间" width="200px" align="center" />
      <!--      <el-table-column label="操作" align="center" min-width="160">-->
      <!--        <template v-slot="{row}">-->
      <!--          <router-link v-if="isAdmin || isShopKeeper || isClerk" :to="{ path: '/venue/editGround', query: { groundId: row.groundId } }">-->
      <!--            <el-button size="small"> 修改 </el-button>-->
      <!--          </router-link>-->
      <!--        </template>-->
      <!--      </el-table-column>-->
    </el-table>
    <el-pagination
      style="padding-top: 10px; text-align: right;"
      background
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
      :current-page.sync="queryRequest.pageNum"
      :page-size.sync="queryRequest.pageSize"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
  </div>
</template>

<script>

import { parseTime } from '@/utils'
import { getVenueGroundTypeList, queryGroundUseRecord } from '@/api/dodoyd'

export default {
  name: 'GroundUseRecord',
  directives: {
  },
  filters: {
    // 场地类型,0:全部,1:网球,2:羽毛球,3:篮球,4:足球
    groundTypeFilter(groundType) {
      const groundTypeMap = {
        0: '全部',
        1: '网球',
        2: '羽毛球',
        3: '篮球',
        4: '足球'
      }
      return groundTypeMap[groundType]
    }
  },
  data() {
    return {
      listLoading: false,
      downloadLoading: false,
      dataList: [],
      defaultDate: [parseTime(new Date(), '{y}{m}{d}'), parseTime(new Date(), '{y}{m}{d}')],
      total: 0,
      queryRequest: {
        // 场地过滤条件, 0:全部,1:网球,2:羽毛球,3:篮球,4:足球
        groundTypeFilter: 0,
        venueId: null,
        useTypeFilter: null,
        startDate: null,
        endDate: null,
        // 页码
        pageNum: 1,
        // 页面大小
        pageSize: 10
      },
      // 0:全部,1:网球,2:羽毛球,3:篮球,4:足球
      groundFilterOptions: [
        { key: 0, name: '全部' },
        { key: 1, name: '网球' },
        { key: 2, name: '羽毛球' },
        { key: 3, name: '篮球' },
        { key: 4, name: '足球' }
      ],
      minDate: '',
      maxDate: '',
      pickerOptions: {
        // 周起始日,1 到 7
        firstDayOfWeek: 1,
        onPick: ({ maxDate, minDate }) => {
          this.minDate = minDate
          this.maxDate = maxDate
          if (maxDate) {
            // 选择了最大日期后，清除已选日期，解除限制。
            this.minDate = ''
          }
        },
        disabledDate: (time) => {
          // 是否禁用日期选择
          if (this.minDate) {
            // 时间跨度
            const range = 60 * 24 * 3600 * 1000
            // 大于或者小于本月的日期被禁用
            return time.getTime() < (this.minDate.getTime() - range) || time.getTime() > (this.minDate.getTime() + range)
          } else {
            // true：不可以选择该日期；false：可以选择该日期。
            return false
          }
        },
        shortcuts: [{
          text: '今天',
          onClick(picker) {
            const now = new Date()
            picker.$emit('pick', [now, now])
          }
        }, {
          text: '最近三天',
          onClick(picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 3)
            picker.$emit('pick', [start, end])
          }
        }, {
          text: '最近一周',
          onClick(picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
            picker.$emit('pick', [start, end])
          }
        }, {
          text: '最近30天',
          onClick(picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
            picker.$emit('pick', [start, end])
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
    this.getDataList()
  },
  methods: {
    // 获取表格内的数据列表
    getDataList() {
      this.listLoading = true
      this.queryRequest.venueId = this.venueId
      this.queryRequest.startDate = this.defaultDate[0]
      this.queryRequest.endDate = this.defaultDate[1]
      queryGroundUseRecord(this.queryRequest).then(response => {
        if (response.code === 0) {
          this.total = response.data.total
          this.dataList = response.data.pageData
        }
        this.listLoading = false
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
    handleClickQuery() {
      this.getDataList()
    },
    handleSizeChange(value) {
      // 已经通过.sync实现了双向绑定，否则这里要主动修改值 this.queryRequest.pageSize = value
      this.getDataList()
      // console.log('页大小：' + this.queryRequest.pageSize)
    },
    handleCurrentChange(value) {
      // 已经通过.sync实现了双向绑定，否则这里要主动修改值 this.queryRequest.pageNum = value
      this.getDataList()
      // console.log(this.queryRequest.pageNum)
    },
    handleDownload() {
      // 文档：https://panjiachen.github.io/vue-element-admin-site/feature/component/excel.html#excel-export
      this.downloadLoading = true
      import('@/vendor/Export2Excel').then(excel => {
        const tHeader = ['场地名称', '场地类型', '场次时间', '预订人', '用途', '订单', '教练', '备注', '预订时间']
        const filterVal = ['groundName', 'groundType', 'startTime', 'bookUserName', 'useType', 'bookOrderId', 'useUserName', 'remark', 'addTime']
        const list = this.dataList
        const data = this.formatJson(filterVal, list)
        excel.export_json_to_excel({
          header: tHeader,
          data,
          filename: '场地使用记录'
        })
        this.downloadLoading = false
      })
    },
    formatJson(filterVal, jsonData) {
      return jsonData.map(v => filterVal.map(j => {
        if (j === 'groundType') {
          return this.$options.filters['groundTypeFilter'](v[j])
        } else if (j === 'startTime') {
          return parseTime(v[j], '{y}-{m}-{d} {h}:{i}')
        } else if (j === 'useType') {
          if (v[j] === 1) {
            return '用户预订'
          } else if (v[j] === 2) {
            return '上课'
          } else if (v[j] === 3) {
            return '内部预订'
          } else {
            return ''
          }
        } else {
          return v[j]
        }
      }))
    }
  }
}
</script>
