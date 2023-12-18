<template>
  <div class="app-container">

    <!-- 上方选择框   -->
    <el-form ref="queryRequest" :model="queryRequest">
      <el-row :gutter="20">
        <el-col :span="4">
          <el-form-item label-width="80px" label="角色类型:">
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
        <el-col v-if="isAdmin" :span="4">
          <router-link :to="{ path: '/staff/addStaff' }">
            <el-button type="primary" icon="el-icon-plus"> 添加人员 </el-button>
          </router-link>
        </el-col>
      </el-row>
    </el-form>
    <!-- 我的申请 列表数据 -->
    <el-table v-loading="listLoading" :data="dataList" border fit highlight-current-row>
      <el-table-column label="序号" type="index" width="60px" align="center" />
      <el-table-column label="头像" width="100px" align="center">
        <template v-slot="{row}">
          <el-avatar :src="row.avatar">{{ row.nickname }}</el-avatar>
        </template>
      </el-table-column>
      <el-table-column prop="nickname" label="昵称" width="200px" align="center" />
      <el-table-column label="身份角色" width="120px" align="center">
        <template v-slot="{row}">
          <!--  角色,0:未分配,1:店长,2:店员,3:教练 -->
          <el-tag>{{ row.userRole | roleTypeFilter }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="maskMobile" label="手机号" width="200px" align="center" />
      <el-table-column prop="remark" label="备注" width="200px" align="center" />
      <el-table-column prop="addTime" label="添加时间" width="200px" align="center" />
      <el-table-column label="操作" align="center" min-width="160">
        <template v-slot="{row}">
          <el-button v-if="isAdmin || isShopUser" size="small" @click="handleDelete(row)"> 删除 </el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>

import { deleteUserRole, getMyVenueRoleList, getVenueUserList } from '@/api/dodoyd'

export default {
  name: 'StaffList',
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
      venueRoleList: null,
      dataList: null,
      queryRequest: {
        // 权限过滤条件, 0:全部,1:店长,2:店员,3:教练
        roleFilter: 0,
        venueId: null
      },
      // 0:全部,1:店长,2:店员,3:教练
      roleFilterOptions: [
        { key: 0, name: '全部' },
        { key: 2, name: '店员' },
        { key: 3, name: '教练' }
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
    isShopUser() {
      return this.venueRoleList && this.venueRoleList.length > 0 && this.venueRoleList.indexOf(2) > -1
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
      getVenueUserList(this.queryRequest).then(response => {
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
    handleDelete(row) {
      this.$confirm('删除后不可恢复', '确定要删除吗？', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async() => {
        return deleteUserRole({ id: row.id, venueId: row.venueId })
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
