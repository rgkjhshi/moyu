<template>
  <div class="app-container">
    <!-- 上方选择框   -->
    <el-form ref="queryRequest" :model="queryRequest" :inline="true" size="small">
      <el-form-item label-width="60px" label="表名:">
        <el-input v-model="queryRequest.orderNo" placeholder="请输入表名" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label-width="60px" label="表描述:">
        <el-input v-model="queryRequest.orderNo" placeholder="请输入表描述" clearable />
      </el-form-item>
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
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="small" @click="handleQuery">查询</el-button>
        <el-button icon="el-icon-refresh" size="small" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <!-- 我的申请 列表数据 -->
    <el-table v-loading="listLoading" :data="dataList" border fit highlight-current-row>
      <el-table-column label="序号" type="index" width="60px" align="center" />
      <el-table-column prop="userNickname" label="下单用户" width="120px" align="center" />
      <el-table-column prop="orderNo" label="订单号" width="200px" align="center" />
      <el-table-column prop="orderTitle" label="订单标题" width="250px" align="center" />
      <el-table-column label="订单类型" width="100px" align="center">
        <template v-slot="{row}">
          <!--  订单类型，1:订场,2:约课,3:办卡 -->
          <span v-if="row.orderType === 1"> <el-tag>订场</el-tag> </span>
          <span v-else-if="row.orderType === 2"> <el-tag>约课</el-tag> </span>
          <span v-else-if="row.orderType === 3"> <el-tag>购卡</el-tag> </span>
        </template>
      </el-table-column>
      <el-table-column prop="orderTime" label="下单时间" width="160px" align="center" />
      <el-table-column label="订单金额" width="100px" align="center">
        <template v-slot="{row}">
          {{ row.totalAmount / 100 + "元" }}
        </template>
      </el-table-column>
      <el-table-column label="订单状态" width="120px" align="center">
        <template v-slot="{row}">
          <!--  状态,2:支付成功,4:已退款 -->
          <span v-if="row.payStatus === 1"> <el-tag>待支付</el-tag> </span>
          <span v-else-if="row.payStatus === 2"> <el-tag type="success">支付成功</el-tag> </span>
          <span v-else-if="row.payStatus === 4">
            <!--  退款状态，1:退款处理中,2:退款成功,3:退款失败,4:已关闭 -->
            <el-tag v-if="row.refundStatus === 2" type="info">已退款</el-tag>
            <el-tag v-else type="info">退款处理中</el-tag>
          </span>
          <span v-else-if="row.payStatus === 5"> <el-tag type="info">已取消</el-tag> </span>
        </template>
      </el-table-column>
      <el-table-column label="支付方式" width="100px" align="center">
        <template v-slot="{row}">
          <!--  支付方式，1:微信支付,2:会员卡支付 -->
          <span v-if="row.payType === 1"> <el-tag>微信支付</el-tag> </span>
          <span v-else-if="row.payType === 2"> <el-tag>会员卡</el-tag> </span>
        </template>
      </el-table-column>
      <el-table-column label="支付金额" width="100px" align="center">
        <template v-slot="{row}">
          <!--  支付方式，1:微信支付,2:会员卡支付 -->
          <span v-if="row.payType === 1"> {{ row.payAmount / 100 + "元" }} </span>
          <span v-else-if="row.payType === 2"> {{ row.payAmount > 100 ? (row.payAmount / 100) : row.payAmount }} </span>
        </template>
      </el-table-column>
      <el-table-column prop="showDate" label="场次日期" width="100px" align="center" />
      <el-table-column label="场次" width="200px" align="center">
        <template v-slot="{row}">
          <el-tag v-for="item in row.infoList" :key="item.key" type="info">{{ item }}</el-tag><br>
        </template>
      </el-table-column>

      <el-table-column prop="remark" label="备注" width="220px" align="center" />
      <el-table-column label="操作" align="center" min-width="160">
        <template v-slot="{row}">
          <el-button v-if="row.payStatus === 2" size="small" @click="handleRefund(row)"> 退款 </el-button>
          <el-button v-if="row.payStatus === 4 && row.refundStatus === 1" size="small" @click="handleRefresh(row)"> 更新状态 </el-button>
        </template>
      </el-table-column>
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
import { queryVenueOrderList, applyRefund, queryLastedOrderInfo } from '@/api/dodoyd'

export default {
  name: 'TableList',
  directives: {
  },
  filters: {
    // 订单类型,0:全部,1:订场,2:约课,3:购卡
    orderTypeFilter(orderType) {
      const orderTypeMap = {
        0: '全部',
        1: '订场',
        2: '约课',
        3: '购卡'
      }
      return orderTypeMap[orderType]
    }
  },
  data() {
    return {
      listLoading: false,
      downloadLoading: false,
      dataList: [],
      defaultDate: [parseTime(new Date(new Date().getTime() - 3600 * 1000 * 24 * 3), '{y}{m}{d}'), parseTime(new Date(), '{y}{m}{d}')],
      total: 0,
      queryRequest: {
        venueId: null,
        startDate: null,
        endDate: null,
        orderNo: null,
        orderTypeFilter: null,
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
    if (this.$route.query.orderNo) {
      this.queryRequest.orderNo = this.$route.query.orderNo
    }
    this.getDataList()
  },
  methods: {
    // 获取表格内的数据列表
    getDataList() {
      this.listLoading = true
      if (this.queryRequest.orderNo) {
        // 有订单号则单查
        queryLastedOrderInfo(this.queryRequest).then(response => {
          if (response.code === 0) {
            this.total = 1
            this.dataList = [response.data]
          }
          this.listLoading = false
        }).catch(err => {
          console.log(err)
          this.listLoading = false
        })
      } else {
        // 无订单号则查列表
        this.queryRequest.venueId = this.venueId
        this.queryRequest.startDate = this.defaultDate[0]
        this.queryRequest.endDate = this.defaultDate[1]
        queryVenueOrderList(this.queryRequest).then(response => {
          if (response.code === 0) {
            this.total = response.data.total
            this.dataList = response.data.pageData
          }
          this.listLoading = false
        }).catch(err => {
          console.log(err)
          this.listLoading = false
        })
      }
    },
    handleQuery() {
      this.getDataList()
    },
    handleRefund(row) {
      this.$confirm('退款后支付金额将完全退回给用户', '确定要退款吗？', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async() => {
        return applyRefund({ venueId: row.venueId, orderNo: row.orderNo })
      }).then(response => {
        if (response.code === 0) {
          this.$message({ type: 'success', message: '已提交退款!' })
          this.getDataList()
        } else {
          this.$message({ type: 'error', message: response.message })
        }
      }).catch(err => { console.log(err) })
    },
    handleRefresh(row) {
      this.listLoading = true
      queryLastedOrderInfo({ 'orderNo': row.orderNo }).then(response => {
        this.getDataList()
      }).catch(err => {
        console.log(err)
        this.listLoading = false
      })
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
    }
  }
}
</script>
