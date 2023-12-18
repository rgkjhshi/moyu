<template>
  <div class="app-container">

    <!-- 上方选择框   -->
    <el-form ref="queryRequest" :model="queryRequest">
      <el-row :gutter="20">
        <el-col :span="4">
          <el-form-item label-width="80px" label="配置类型:">
            <el-select v-model="queryRequest.configTypeFilter" @change="handleClickQuery">
              <el-option v-for="item in configFilterOptions" :key="item.key" :label="item.name" :value="item.key" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label-width="80px" label="配置编号:">
            <el-input v-model="queryRequest.configNo" placeholder="配置编号" clearable />
          </el-form-item>
        </el-col>
        <el-col :span="2">
          <el-button type="primary" icon="el-icon-search" @click="handleClickQuery">
            查询
          </el-button>
        </el-col>
        <el-col v-if="isAdmin" :span="4">
          <router-link :to="{ path: '/sysAdmin/addRuleConfig' }">
            <el-button type="primary" icon="el-icon-plus"> 添加配置 </el-button>
          </router-link>
        </el-col>
      </el-row>
    </el-form>
    <!-- 我的申请 列表数据 -->
    <el-table v-loading="listLoading" :data="dataList" border fit highlight-current-row>
      <el-table-column label="序号" type="index" width="60px" align="center" />
      <el-table-column prop="configNo" label="配置编号" width="200px" align="center" />
      <el-table-column prop="configName" label="配置名称" width="220px" align="center" />
      <el-table-column label="配置类型" width="160px" align="center">
        <template v-slot="{row}">
          <!--  配置类型,0:未知,1:场地配置 -->
          <el-tag>{{ row.configType | configTypeFilter }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="配置内容" width="400px">
        <template v-slot="{row}">
          <!--  jsonViewer参考资料：http://www.bryh.cn/a/340749.html https://github.com/chenfengjw163/vue-json-viewer/blob/master/README_CN.md -->
          <json-viewer :value="JSON.parse(row.configRule)" boxed copyable :show-array-index="false" @copied="onCopied">
            <template slot="copy">
              <i class="el-icon-document-copy" title="复制" />
            </template>
          </json-viewer>
        </template>
      </el-table-column>

      <el-table-column label="操作" align="center" min-width="160">
        <template v-slot="{row}">
          <router-link :to="{ path: '/sysAdmin/editRuleConfig', query: { configNo: row.configNo } }">
            <el-button size="small"> 修改 </el-button>
          </router-link>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>

import { queryRuleConfigList } from '@/api/dodoyd'
// 单页面引入 JsonViewer
import JsonViewer from 'vue-json-viewer'

export default {
  name: 'RuleConfigListList',
  components: { JsonViewer },

  filters: {
    // 配置类型,0:未知,1:场地配置
    configTypeFilter(groundType) {
      const groundTypeMap = {
        0: '未知',
        1: '场地配置'
      }
      return groundTypeMap[groundType]
    }
  },
  data() {
    return {
      listLoading: false,
      dataList: [],
      queryRequest: {
        // 场地过滤条件, 0:全部,1:场地配置
        configTypeFilter: 0,
        venueId: null,
        configNo: null
      },
      // 0:全部,1:场地配置
      configFilterOptions: [
        { key: 0, name: '全部' },
        { key: 1, name: '场地配置' }
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
    this.queryRequest.configNo = this.$route.query.configNo
    this.getDataList()
  },
  methods: {
    // 获取表格内的数据列表
    getDataList() {
      this.listLoading = true
      this.queryRequest.venueId = this.venueId
      queryRuleConfigList(this.queryRequest).then(response => {
        if (response.code === 0) {
          this.dataList = response.data
        }
        this.listLoading = false
      })
    },
    handleClickQuery() {
      this.getDataList()
    },
    onCopied() {
      this.$message({ type: 'success', message: '复制成功!' })
    }
  }
}
</script>
