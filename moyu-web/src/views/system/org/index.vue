<template>
  <div class="app-container">
    <el-row :gutter="4">
      <!-- 部门树 -->
      <el-col :span="4">
        <el-card>
          <!-- 部门树上面的搜索框 -->
<!--          <el-input v-model="deptName" placeholder="部门名称" clearable size="small" prefix-icon="el-icon-search" style="margin-bottom:10px;" />-->
          <el-tree ref="orgTreeRef" :data="orgTreeData" :props="{ children: 'children', label: 'label', disabled: '' }" :expand-on-click-node="false" :filter-node-method="filterNode" node-key="id" default-expand-all highlight-current @node-click="handleNodeClick" />
        </el-card>
      </el-col>
      <!-- 数据 -->
      <el-col :span="20">
        <el-card>
          <!-- 上方选择框   -->
          <el-form ref="queryFormRef" :model="queryRequest" :inline="true" size="small">
            <el-form-item label="关键字" prop="keywords">
              <el-input v-model="queryRequest.keywords" placeholder="部门名称" @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
              <el-button icon="el-icon-refresh" @click="resetQuery"> 重置 </el-button>
            </el-form-item>
          </el-form>
        </el-card>
        <el-card>
          <!-- 选中数据操作   -->
          <el-row :gutter="10" style="margin-bottom: 10px">
            <el-col :span="1.5">
              <el-button type="success" plain icon="el-icon-plus" size="mini" @click="handleAdd">新增</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button type="primary" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleEdit">修改</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multi" @click="handleDelete">删除</el-button>
            </el-col>
          </el-row>
          <!-- 表格数据 -->
          <el-table v-loading="dataLoading" :data="dataList" size="mini" border :header-cell-style="{background:'#f5f7fa',color:'#606266'}" @selection-change="handleSelectionChange">
            <el-table-column type="selection" align="center" width="55" />
            <el-table-column label="序号" type="index" width="60px" align="center" />
            <el-table-column prop="name" label="组织名称" width="200px" show-overflow-tooltip align="center" />
            <el-table-column prop="category" label="类别" width="200px" show-overflow-tooltip align="center" />
            <el-table-column prop="orgLevel" label="层级" width="200px" show-overflow-tooltip align="center" />
            <el-table-column prop="sortNum" label="排序" width="200px" show-overflow-tooltip align="center" />
            <el-table-column prop="status" label="状态" width="200px" show-overflow-tooltip align="center" />
            <el-table-column prop="createTime" label="创建时间" width="200px" show-overflow-tooltip align="center" />
            <el-table-column prop="updateTime" label="更新时间" width="200px" show-overflow-tooltip align="center" />
            <el-table-column prop="remark" label="备注" width="200px" show-overflow-tooltip align="center" />
            <el-table-column label="操作" fixed="right" align="center" min-width="200">
              <template v-slot="{row}">
                <el-button type="success" plain size="small" icon="el-icon-edit" @click="handleEdit(row)">修改</el-button>
                <el-button type="danger" plain size="small" icon="el-icon-delete" @click="handleDelete(row)">删除</el-button>
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
            @size-change="getDataList"
            @current-change="getDataList"
          />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>

import { addSysUser, editSysUser, deleteSysUser } from '@/api/system/sysUser'
import { getOrgTreeOptions, listSysOrg } from '@/api/system/sysOrg'

export default {
  name: 'SysOrg',
  components: { },
  directives: {
  },
  data() {
    return {
      dataLoading: false,
      // 部门名称，用于过滤
      deptName: undefined,
      // 组织机构树
      orgTreeData: undefined,
      dataList: [],
      // 选中数组
      idList: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multi: true,
      total: 0,
      queryRequest: {
        // 页码
        pageNum: 1,
        // 页面大小
        pageSize: 10,
        // 父节点
        pid: undefined,
        // 关键词
        keywords: ''
      }
    }
  },
  watch: {
    // 根据名称筛选部门树
    deptName(val) {
      this.$refs.orgTreeRef.filter(val)
    }
  },
  created() {
    this.getDataList()
  },
  mounted() {
    this.getOrgTreeData()
  },
  methods: {
    // 获取表格内的数据列表
    getDataList() {
      this.dataLoading = false
      // 查询数据
      listSysOrg(this.queryRequest).then(response => {
        if (response.code === 0) {
          this.total = response.data.total
          this.dataList = response.data.rows
        }
        this.dataLoading = false
      }).catch(err => {
        console.log(err)
        this.dataLoading = false
      })
    },
    /** 查询按钮操作 */
    handleQuery() {
      this.getDataList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.$refs['queryFormRef'].resetFields()
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.idList = selection.map(item => item.userId)
      this.single = !(selection.length === 1)
      this.multi = !(selection.length > 0)
    },
    /** 新增按钮操作 */
    handleAdd() {
      addSysUser().then(response => {
      }).catch(err => {
        console.log(err)
      })
    },
    /** 修改按钮操作 */
    handleEdit(row) {
      editSysUser(row).then(response => {
      }).catch(err => {
        console.log(err)
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.userId || '' + this.idList
      this.$confirm('是否确认删除userId为"' + ids + '"的数据?', {
        type: 'warning'
      }).then(async() => {
        return deleteSysUser({ userIdList: ids })
      }).then(response => {
        if (response.code === 0) {
          this.$message({ showClose: true, message: response.message, type: 'success' })
        }
      }).catch(err => {
        console.log(err)
      })
    },

    /** 查询部门下拉树结构 */
    getOrgTreeData() {
      getOrgTreeOptions().then(response => {
        console.log(response)
        this.orgTreeData = response.data
      }).catch(err => {
        console.log(err)
      })
    },

    /** 部门筛选 */
    filterNode(value, data) {
      if (!value) {
        return true
      }
      return data.label.indexOf(value) !== -1
    },

    /** 点击部门树节点*/
    handleNodeClick(data) {
      this.queryRequest.pid = data.value
      this.handleQuery()
      // console.log(data)
    }

  }
}
</script>
