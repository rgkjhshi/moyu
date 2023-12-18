<template>
  <div class="app-container">

    <!-- 上方选择框   -->
    <el-form ref="queryRequest" :model="queryRequest">
      <el-row :gutter="20">
        <el-col :span="4">
          <el-form-item label-width="80px" label="场地类型:">
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
          <router-link :to="{ path: '/venue/addGround' }">
            <el-button type="primary" icon="el-icon-plus"> 添加场地 </el-button>
          </router-link>
        </el-col>
      </el-row>
    </el-form>
    <!-- 我的申请 列表数据 -->
    <el-table v-loading="listLoading" :data="dataList" border fit highlight-current-row>
      <el-table-column label="序号" type="index" width="60px" align="center" />
      <el-table-column prop="venueId" label="场馆ID" width="200px" align="center" />
      <el-table-column prop="venueName" label="场馆名称" width="300px" align="center" />
      <el-table-column label="场馆状态" width="200px" align="center">
        <template v-slot="{row}">
          <!--  场馆状态,0:未上线,1:正常营业,2:暂停营业 -->
          <el-tag v-if="row.venueStatus === 0" type="info" effect="plain">未上线</el-tag>
          <el-tag v-else-if="row.venueStatus === 1" type="success">正常营业</el-tag>
          <el-tag v-else-if="row.venueStatus === 2" type="info">暂停营业</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="营业时间" align="center" width="160">
        <template v-slot="{row}">
          {{ row.openStart + ":00 - " + row.openEnd + ":00" }}
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>

import { queryVenueList } from '@/api/dodoyd'

export default {
  name: 'VenueList',
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
      dataList: [
        {
          'id': 1,
          'venueId': '20001',
          'venueName': '温菲尔德国家馆旗舰中心',
          'venueStatus': 1,
          'openStart': 8,
          'openEnd': 23,
          'address': '北京市朝阳区天辰西路国家体育馆南侧'
        }
      ],
      queryRequest: {
        // 场地过滤条件, 0:全部,1:网球,2:羽毛球,3:篮球,4:足球
        groundTypeFilter: 0,
        venueId: null
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
    isAdmin() {
      const roles = this.$store.getters.roles
      return roles && roles.length > 0 && roles.indexOf('admin') > -1
    }
  },
  created() {
    this.getDataList()
  },
  methods: {
    // 获取表格内的数据列表
    getDataList() {
      this.listLoading = true
      queryVenueList().then(response => {
        if (response.code === 0) {
          this.dataList = response.data
        }
        this.listLoading = false
      }).catch(err => {
        console.log(err)
      })
    },
    handleClickQuery() {
      this.getDataList()
    }
  }
}
</script>
