<template>
  <div class="app-container">

    <!-- 上方选择框   -->
    <el-form ref="queryRequest" :model="queryRequest">
      <el-row :gutter="20">
        <el-col :span="4">
          <el-form-item label-width="100px" label="会员卡类型:">
            <el-select v-model="queryRequest.cardType" @change="handleClickQuery">
              <!-- 会员卡类型，0:全部,1:储值卡,2:折扣卡,3:计次卡,4:课时卡 -->
              <el-option label="全部" :value="null" />
              <el-option label="储值卡" :value="1" />
              <el-option label="折扣卡" :value="2" />
              <el-option label="计次卡" :value="3" />
              <el-option label="课时卡" :value="4" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label-width="60px" label="卡号:">
            <el-input v-model="queryRequest.cardNo" placeholder="会员卡号" clearable />
          </el-form-item>
        </el-col>
        <el-col :span="4">
          <el-form-item label-width="60px" label="手机号:">
            <el-input v-model="queryRequest.mobile" placeholder="手机号或尾号" maxlength="11" clearable />
          </el-form-item>
        </el-col>
        <el-col :span="2">
          <el-button type="primary" icon="el-icon-search" @click="handleClickQuery"> 查询 </el-button>
        </el-col>
      </el-row>
    </el-form>
    <!-- 列表数据 -->
    <el-table v-loading="listLoading" :data="dataList" border fit highlight-current-row>
      <el-table-column label="序号" type="index" width="60px" align="center" />
      <el-table-column label="会员" width="160px" align="center">
        <template v-slot="{row}">
          <div v-if="row.userInfo.userId" class="user-box">
            <el-avatar :src="row.userInfo.avatar">{{ row.userInfo.nickname }}</el-avatar>
            <div class="user-title">{{ row.userInfo.nickname }}</div>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="联系方式" width="120px" align="center">
        <template v-slot="{row}">
          <el-tooltip :content="row.userInfo.mobile" placement="top" effect="light">
            <el-tag type="info">{{ row.userInfo.maskMobile }}</el-tag>
          </el-tooltip>
        </template>
      </el-table-column>
      <el-table-column label="会员卡" width="320px" align="center">
        <template v-slot="{row}">
          <!-- 不同类型的会员卡 -->
          <el-card
            :class="[ 'box-card', row.cardType===1?'bg-gradual-orange':row.cardType===2?'bg-gradual-red':row.cardType===3?'bg-gradual-pink':'bg-gradual-blue']"
            shadow="hover"
          >
            <div class="card-title">
              <span>{{ row.cardName }}</span>
            </div>
            <div class="card-info">
              <span v-if="row.cardType === 1">储值卡 | 剩余 {{ row.cardValue }}</span>
              <span v-if="row.cardType === 2">折扣卡 | 剩余 {{ row.cardValue }}</span>
              <span v-if="row.cardType === 3">计次卡 | 剩余 {{ row.cardValue }} 次</span>
              <span v-if="row.cardType === 4">课时卡 | 剩余 {{ row.cardValue }} 课时</span>
            </div>
            <div class="card-info">
              <span>有效期至: {{ row.expireTime | parseTime('{y}-{m}-{d}') }}</span>
            </div>
            <div v-if="row.cardType === 2" class="card-info">
              <span>{{ row.discount/10 }}折</span>
            </div>
            <div class="card-info">
              <span v-if="row.goldenUseLimit === 0">仅限非黄时段</span>
              <span v-if="row.goldenUseLimit === 1">全时段通用</span>
            </div>
          </el-card>
        </template>
      </el-table-column>
      <el-table-column prop="cardNo" label="卡号" width="200px" align="center" />
      <el-table-column label="类型" width="100px" align="center">
        <template v-slot="{row}">
          <!-- 会员卡类型，0:全部,1:储值卡,2:折扣卡,3:计次卡,4:课时卡 -->
          <el-tag v-if="row.cardType === 1">储值卡</el-tag>
          <el-tag v-if="row.cardType === 2">折扣卡</el-tag>
          <el-tag v-if="row.cardType === 3">计次卡</el-tag>
          <el-tag v-if="row.cardType === 4">课时卡</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="cardValue" label="余额" width="100px" align="center" />
      <el-table-column label="生效时间" width="120px" align="center">
        <template v-slot="{row}">
          <span>{{ row.validTime | parseTime('{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="到期时间" width="120px" align="center">
        <template v-slot="{row}">
          <span>{{ row.expireTime | parseTime('{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" min-width="250">
        <template v-slot="{row}">
          <el-button type="primary" plain size="small" @click="handleWithhold(row)"> 代扣 </el-button>
          <el-button type="primary" plain size="small" @click="handleExpandDate(row)"> 延期 </el-button>
          <el-button type="primary" plain size="small" @click="handleConsumeLog(row)"> 消费记录 </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页组件 -->
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

import { deleteVenueCard, getVipCardList } from '@/api/dodoyd'

export default {
  name: 'VipList',
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
      total: 0,
      cardNo: null,
      mobile: null,
      dataList: [],
      queryRequest: {
        // 场地过滤条件, 0:全部,1:储值卡,2:折扣卡,3:计次卡,4:课时卡
        cardType: null,
        venueId: null,
        cardNo: null,
        mobile: null,
        // 页码
        pageNum: 1,
        // 页面大小
        pageSize: 10
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
    },
    isAdmin() {
      const roles = this.$store.getters.roles
      return roles && roles.length > 0 && roles.indexOf('admin') > -1
    }
  },
  created() {
    this.queryRequest.cardNo = this.$route.query.cardNo
    this.queryRequest.mobile = this.$route.query.mobile
    this.getDataList()
  },
  methods: {
    // 获取表格内的数据列表
    getDataList() {
      this.listLoading = true
      this.queryRequest.venueId = this.venueId
      getVipCardList(this.queryRequest).then(response => {
        if (response.code === 0) {
          this.total = response.data.total
          this.dataList = response.data.pageData
        }
        this.listLoading = false
      }).catch(err => {
        console.log(err)
        this.listLoading = false
      })
    },
    handleClickQuery() {
      this.getDataList()
    },
    // 会员卡代扣
    handleWithhold(row) {
      this.$router.push({ path: '/vip/withhold', query: { cardNo: row.cardNo }})
    },
    // 会员卡延期
    handleExpandDate(row) {
      this.$router.push({ path: '/vip/expandDate', query: { cardNo: row.cardNo }})
    },
    // 会员卡消费记录
    handleConsumeLog(row) {
      this.$router.push({ path: '/vip/consumeLog', query: { cardNo: row.cardNo }})
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
    handleDelete(row) {
      this.$confirm('删除后不可恢复', '确定要删除吗？', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async() => {
        return deleteVenueCard({ templateId: row.templateId, venueId: this.venueId })
      }).then(response => {
        if (response.code === 0) {
          this.$message({ type: 'success', message: '删除成功!' })
          this.getDataList()
        } else {
          this.$message({ type: 'error', message: response.message })
        }
      }).catch(err => { console.log(err) })
    }
  }
}
</script>

<style lang="scss" scoped>

.box-card {
  width: 300px;
  text-align: left;
  line-height: 23px;
}
.card-title {
  font-size: 16px;
  font-weight: bold;
  padding-bottom: 10px;
}
.card-info {
}
.user-box {
  //display: flex;
}
.user-title {
  padding-left: 10px;
  height: 40px;
  line-height: 40px;
  font-size: 16px;
}
.bg-gradual-red {
  background-image: linear-gradient(45deg, #f43f3b, #ec008c);
  color: #ffffff;
}

.bg-gradual-orange {
  background-image: linear-gradient(45deg, #ff9700, #ed1c24);
  color: #ffffff;
}

.bg-gradual-green {
  background-image: linear-gradient(45deg, #39b54a, #8dc63f);
  color: #ffffff;
}

.bg-gradual-purple {
  background-image: linear-gradient(45deg, #9000ff, #5e00ff);
  color: #ffffff;
}

.bg-gradual-pink {
  background-image: linear-gradient(45deg, #ec008c, #6739b6);
  color: #ffffff;
}

.bg-gradual-blue {
  background-image: linear-gradient(45deg, #0081ff, #1cbbb4);
  color: #ffffff;
}

</style>
