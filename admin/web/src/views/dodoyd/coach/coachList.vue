<template>
  <div class="app-container">

    <!-- 上方选择框   -->
    <el-form ref="queryRequest" :model="queryRequest">
      <el-row :gutter="20">
        <el-col :span="4">
          <el-form-item label-width="80px" label="项目种类:">
            <el-select v-model="queryRequest.groundTypeFilter" @change="handleClickQuery">
              <el-option v-for="item in groundFilterOptions" :key="item.key" :label="item.name" :value="item.key" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="2">
          <el-button type="primary" icon="el-icon-search" @click="handleClickQuery">
            查询
          </el-button>
        </el-col>
        <el-col v-if="isAdmin" :span="4">
          <router-link :to="{ path: '/coach/addCoach' }">
            <el-button type="primary" icon="el-icon-plus"> 添加教练 </el-button>
          </router-link>
        </el-col>
      </el-row>
    </el-form>
    <!-- 我的申请 列表数据 -->
    <el-table v-loading="listLoading" :data="coachList" border fit highlight-current-row>
      <el-table-column label="序号" type="index" width="60px" align="center" />
      <el-table-column label="头像" width="100px" align="center">
        <template v-slot="{row}">
          <el-avatar :src="row.avatar">{{ row.nickname }}</el-avatar>
        </template>
      </el-table-column>
      <el-table-column prop="nickname" label="昵称" width="200px" align="center" />
      <el-table-column label="联系方式" width="120px" align="center">
        <template v-slot="{row}">
          <el-tooltip :content="row.mobile" placement="top" effect="light">
            <span>{{ row.maskMobile }}</span>
          </el-tooltip>
        </template>
      </el-table-column>
      <el-table-column label="项目种类" width="120px" align="center">
        <template v-slot="{row}">
          <el-tag>{{ row.groundType | groundTypeFilter }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="groundName" label="首选场地" width="120px" align="center" />
      <el-table-column prop="addTime" label="添加时间" width="200px" align="center" />
    </el-table>
  </div>
</template>

<script>

import { getVenueGroundTypeList, getVenueUserList } from '@/api/dodoyd'

export default {
  name: 'CoachList',
  directives: {
  },
  filters: {
    // 场地类型,0:全部,1:网球,2:羽毛球,3:篮球,4:足球
    groundTypeFilter(groundType) {
      const groundTypeMap = {
        0: '未知',
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
      coachList: null,
      queryRequest: {
        roleFilter: 3,
        groundTypeFilter: 0
      },
      // 0:全部,1:网球,2:羽毛球,3:篮球,4:足球
      groundFilterOptions: [
        { key: 0, name: '全部' },
        { key: 1, name: '网球' },
        { key: 2, name: '羽毛球' },
        { key: 3, name: '篮球' },
        { key: 4, name: '足球' }
      ]
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
    this.getGroundTypeList()
    this.getCoachList()
  },
  methods: {
    // 获取表格内的数据列表
    getCoachList() {
      this.queryRequest.venueId = this.venueId
      getVenueUserList(this.queryRequest).then(response => {
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
    handleClickQuery() {
      this.getCoachList()
    }
  }
}
</script>
