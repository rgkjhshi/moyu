<template>
  <div class="app-container">
    <el-card class="tip">
      <span class="tip-content">
        仅显示最近的20条消费记录
      </span>
    </el-card>
    <!-- 列表数据 -->
    <el-table v-loading="listLoading" :data="dataList" border fit highlight-current-row>
      <el-table-column label="序号" type="index" width="60px" align="center" />
      <el-table-column prop="consumeDesc" label="消费内容" width="200px" align="center" />
      <el-table-column prop="consumeValue" label="消费数额" width="100px" align="center">
        <template v-slot="{row}">
          <span>-{{ row.consumeValue }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="addTime" label="消费时间" width="200px" align="center" />
      <el-table-column prop="orderNo" label="关联订单" width="260px" align="center">
        <template v-slot="{row}">
          <router-link :to="{ path: '/order/orderList', query: { orderNo: row.orderNo } }">
            <el-tag v-if="row.orderNo" type="info" effect="plain">{{ row.orderNo }}</el-tag>
          </router-link>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>

import { getCardConsumeLog } from '@/api/dodoyd'

export default {
  name: 'VipConsumeLog',
  directives: {
  },
  data() {
    return {
      listLoading: false,
      dataList: [],
      queryRequest: {
        cardNo: null
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
    this.queryRequest.cardNo = this.$route.query.cardNo
    this.getDataList()
  },
  methods: {
    // 获取表格内的数据列表
    getDataList() {
      this.listLoading = true
      getCardConsumeLog(this.queryRequest).then(response => {
        if (response.code === 0) {
          this.dataList = response.data
        }
        this.listLoading = false
      }).catch(err => {
        console.log(err)
        this.listLoading = false
      })
    },
    handleClickQuery() {
      this.getDataList()
    }
  }
}
</script>

<style lang="scss" scoped>
.tip {
  background-color: #ecf8ff;
  border-radius: 4px;
  border-left: 5px solid #50bfff;
}
.tip-error {
  background-color: #fff6f7;
  border-radius: 4px;
  border-left: 5px solid #fe6c6f;
}
.tip-content {
  font-size: 14px;
  color: #5e6d82;
  line-height: 1.5em;
}
</style>
