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
      <el-table-column prop="groundId" label="场地ID" width="160px" align="center" />
      <el-table-column prop="groundName" label="场地名称" width="220px" align="center" />
      <el-table-column label="场地类型" width="120px" align="center">
        <template v-slot="{row}">
          <!--  场地类型, 0:全部,1:网球,2:羽毛球,3:篮球,4:足球 -->
          <el-tag>{{ row.groundType | groundTypeFilter }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="groundPrice" label="价格" width="120px" align="center" />
      <el-table-column prop="configNo" label="价格配置" width="240px" align="center">
        <template v-slot="{row}">
          <router-link :to="{ path: '/sysAdmin/ruleConfigList', query: { configNo: row.configNo } }">
            <el-tag v-if="row.configNo" type="info" effect="plain">{{ row.configNo }}</el-tag>
          </router-link>
        </template>
      </el-table-column>
      <el-table-column label="状态" width="160px" align="center">
        <template v-slot="{row}">
          <!--  状态,0:未生效,1:已生效 -->
          <el-tag v-if="row.groundStatus === 0" type="info" effect="plain">未上线</el-tag>
          <el-tag v-else-if="row.groundStatus === 1" type="success">正常使用</el-tag>
          <el-tag v-else-if="row.groundStatus === 2" type="info">暂不可用</el-tag>
        </template>
      </el-table-column>
      <!--      <el-table-column label="说明" min-width="200px">-->
      <!--        <template v-slot="{row}">-->
      <!--          &lt;!&ndash;  状态,0:未生效,1:已生效 &ndash;&gt;-->
      <!--          <span v-if="row.groundStatus === 0"></span>-->
      <!--          <span v-else-if="row.groundStatus === 1">可正常预定</span>-->
      <!--          <span v-else-if="row.groundStatus === 2">不对外开放预订</span>-->
      <!--        </template>-->
      <!--      </el-table-column>-->
      <el-table-column v-if="isAdmin" label="操作" align="center" min-width="160">
        <template v-slot="{row}">
          <router-link v-if="isAdmin" :to="{ path: '/venue/editGround', query: { groundId: row.groundId } }">
            <el-button size="small"> 修改 </el-button>
          </router-link>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>

import { queryGroundList } from '@/api/dodoyd'

export default {
  name: 'GroundList',
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
      dataList: [],
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
    this.getDataList()
  },
  methods: {
    // 获取表格内的数据列表
    getDataList() {
      this.listLoading = true
      this.queryRequest.venueId = this.venueId
      queryGroundList(this.queryRequest).then(response => {
        if (response.code === 0) {
          this.dataList = response.data
        }
        this.listLoading = false
      })
    },
    handleClickQuery() {
      this.getDataList()
    }
  }
}
</script>
