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
        <el-col :span="2">
          <el-button type="primary" icon="el-icon-search" @click="handleClickQuery"> 查询 </el-button>
        </el-col>
        <el-col v-if="isAdmin" :span="4">
          <router-link :to="{ path: '/sysAdmin/addVenueCard' }">
            <el-button type="primary" icon="el-icon-plus"> 添加会员卡 </el-button>
          </router-link>
        </el-col>
      </el-row>
    </el-form>
    <!-- 我的申请 列表数据 -->
    <el-table v-loading="listLoading" :data="dataList" border fit highlight-current-row>
      <el-table-column label="序号" type="index" width="60px" align="center" />
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
              <span v-if="row.cardType === 1">储值卡 | 面值 {{ row.valueCount }}</span>
              <span v-if="row.cardType === 2">折扣卡 | 面值 {{ row.valueCount }}</span>
              <span v-if="row.cardType === 3">计次卡 | 共 {{ row.valueCount }} 次</span>
              <span v-if="row.cardType === 4">课时卡 | 共 {{ row.valueCount }} 课时</span>
            </div>
            <div class="card-info">
              <span>有效期 {{ row.validDaysLimit }} 天</span>
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
      <el-table-column label="类型" width="100px" align="center">
        <template v-slot="{row}">
          <!-- 会员卡类型，0:全部,1:储值卡,2:折扣卡,3:计次卡,4:课时卡 -->
          <el-tag v-if="row.cardType === 1">储值卡</el-tag>
          <el-tag v-if="row.cardType === 2">折扣卡</el-tag>
          <el-tag v-if="row.cardType === 3">计次卡</el-tag>
          <el-tag v-if="row.cardType === 4">课时卡</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="amount" label="价格" width="100px" align="center" />
      <el-table-column label="购买须知" min-width="300px">
        <template v-slot="{row}">
          <div v-for="item in row.descList" :key="item">{{ item }}</div>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" min-width="200">
        <template v-slot="{row}">
          <el-button v-if="isShopUser" type="primary" plain size="small" @click="handleAddVip(row)"> 办理会员 </el-button>
          <el-button v-if="isAdmin" type="primary" plain size="small" @click="handleModify(row)"> 修改 </el-button>
          <el-button v-if="isAdmin" type="primary" plain size="small" @click="handleDelete(row)"> 删除 </el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>

import { getVenueCardList, deleteVenueCard, getMyVenueRoleList } from '@/api/dodoyd'

export default {
  name: 'VenueCardList',
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
      venueRoleList: null,
      dataList: [],
      queryRequest: {
        // 场地过滤条件, 0:全部,1:储值卡,2:折扣卡,3:计次卡,4:课时卡
        cardType: null,
        venueId: null
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
    isShopUser() {
      // return this.venueRoleList && this.venueRoleList.length > 0 && this.venueRoleList.indexOf(1) > -1
      return this.venueRoleList && this.venueRoleList.length > 0
    },
    isAdmin() {
      const roles = this.$store.getters.roles
      return roles && roles.length > 0 && roles.indexOf('admin') > -1
    }
  },
  created() {
    this.getMyVenueRoleList()
    this.getDataList()
  },
  methods: {
    // 获取表格内的数据列表
    getDataList() {
      this.listLoading = true
      this.queryRequest.venueId = this.venueId
      getVenueCardList(this.queryRequest).then(response => {
        if (response.code === 0) {
          this.dataList = response.data
        }
        this.listLoading = false
      })
    },
    // 获取当前用户的venueRole
    getMyVenueRoleList() {
      getMyVenueRoleList({ venueId: this.venueId }).then(response => {
        if (response.code === 0) {
          this.venueRoleList = response.data
        }
      })
    },
    handleClickQuery() {
      this.getDataList()
    },
    handleAddVip(row) {
      this.$router.push({ path: '/vip/addVipCard', query: { templateId: row.templateId }})
    },
    handleModify(row) {
      this.$router.push({ path: '/sysAdmin/editVenueCard', query: { templateId: row.templateId }})
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
