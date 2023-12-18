<template>
  <div class="app-container">

    <!-- 上方选择框   -->
    <el-form ref="queryRequest" :model="queryRequest">
      <el-row :gutter="20">
        <el-col :span="4">
          <el-form-item label-width="80px" label="权限类型:">
            <el-select v-model="queryRequest.roleFilter" value="0" @change="handleClickQuery">
              <el-option v-for="item in roleFilterOptions" :key="item.key" :label="item.name" :value="item.key" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="2">
          <el-button type="primary" icon="el-icon-search" @click="handleClickQuery">
            查询
          </el-button>
        </el-col>
      </el-row>
    </el-form>
    <!-- 我的申请 列表数据 -->
    <el-table v-loading="listLoading" :data="dataList" border fit highlight-current-row>
      <el-table-column prop="venueName" label="场馆名称" width="300px" align="center" />
      <el-table-column label="权限类型" width="200px" align="center">
        <template v-slot="{row}">
          <!--  角色,0:未分配,1:店长,2:店员,3:教练 -->
          <el-tag>{{ row.userRole | roleTypeFilter }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="remark" label="备注" width="200px" align="center" />
      <el-table-column prop="addTime" label="添加时间" width="200px" align="center" />
      <el-table-column label="操作" align="center" min-width="160">
        <template v-slot="scope">
          <el-button size="small" @click="handleDelete(scope)"> 删除 </el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>

import { deleteUserRole, getMyRoleList } from '@/api/dodoyd'

export default {
  name: 'MyRoleList',
  directives: {
  },
  filters: {
    // 角色,0:未分配,1:店长,2:店员,3:教练
    roleTypeFilter(roleType) {
      const roleTypeMap = {
        0: '未分配',
        1: '店长',
        2: '店员',
        3: '教练'
      }
      return roleTypeMap[roleType]
    }
  },
  data() {
    return {
      listLoading: false,
      dataList: null,
      queryRequest: {
        // 权限过滤条件, 0:全部,1:店长,2:店员,3:教练
        roleFilter: 0
      },
      // 0:全部,1:店长,2:店员,3:教练
      roleFilterOptions: [
        { key: 0, name: '全部' },
        { key: 2, name: '店员' },
        { key: 3, name: '教练' }
      ]
    }
  },
  created() {
    this.getDataList()
  },
  methods: {
    // 获取表格内的数据列表
    getDataList() {
      this.listLoading = true
      getMyRoleList(this.queryRequest).then(response => {
        if (response.code === 0) {
          this.dataList = response.data
        }
        this.listLoading = false
      })
    },
    handleClickQuery() {
      this.getDataList()
    },
    handleDelete({ $index, row }) {
      this.$confirm('删除后不可恢复', '确定要删除吗？', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async() => {
        return deleteUserRole({ id: row.id, venueId: row.venueId })
      }).then(response => {
        if (response.code === 0) {
          this.$message({ type: 'success', message: '删除成功!' })
          this.dataList.splice($index, 1)
        } else {
          this.$message({ type: 'error', message: response.message })
        }
      }).catch(err => { console.log(err) })
    }
  }
}
</script>
