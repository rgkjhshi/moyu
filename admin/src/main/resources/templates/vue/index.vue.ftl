<template>
  <div class="app-container">
    <!-- 上方选择框   -->
    <el-form ref="queryForm" :model="queryRequest" :inline="true" size="small" label-width="80px">
      <el-form-item label="${entity.pkColumn.comment}:" prop="${entity.pkColumn.javaName}">
        <el-input v-model="queryRequest.${entity.pkColumn.javaName}" placeholder="请输入${entity.pkColumn.comment}" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="small" @click="handleQuery">查询</el-button>
        <el-button icon="el-icon-refresh" size="small" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
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
    <el-table v-loading="dataLoading" :data="dataList" border :header-cell-style="{background:'#f5f7fa',color:'#606266'}" @selection-change="handleSelectionChange">
      <el-table-column type="selection" align="center" width="55" />
      <el-table-column label="序号" type="index" width="60px" align="center" />
<#list columnList as column>
      <el-table-column prop="${column.javaName}" label="${column.comment}" width="200px" show-overflow-tooltip align="center" />
</#list>
      <el-table-column label="操作" align="center" min-width="200">
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
  </div>
</template>

<script>

import { list${entity.className}, add${entity.className}, edit${entity.className}, delete${entity.className} } from '@/api/system/sysUser'

export default {
  name: '${entity.className}',
  components: { },
  directives: {
  },
  data() {
    return {
      dataLoading: false,
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
        tableName: null
      }
    }
  },
  created() {
    this.getDataList()
  },
  methods: {
    // 获取表格内的数据列表
    getDataList() {
      this.dataLoading = true
      // 查询数据
      list${entity.className}(this.queryRequest).then(response => {
        if (response.code === 0) {
          this.total = response.data.total
          this.dataList = response.data.pageData
        }
        this.dataLoading = false
      }).catch(err => {
        console.log(err)
        this.dataLoading = false
      })
    },
    /** 查询按钮操作 */
    handleQuery() {
      this.queryRequest.pageNum = 1
      this.getDataList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.$refs['queryForm'].resetFields()
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
      add${entity.className}().then(response => {
      }).catch(err => {
        console.log(err)
      })
    },
    /** 修改按钮操作 */
    handleEdit(row) {
      edit${entity.className}(row).then(response => {
      }).catch(err => {
        console.log(err)
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.${entity.pkColumn.javaName} || '' + this.idList
      this.$confirm('是否确认删除${entity.pkColumn.javaName}为"' + ids + '"的数据?', {
        type: 'warning'
      }).then(async() => {
        return delete${entity.className}({ ${entity.pkColumn.javaName}List: ids })
      }).then(response => {
        if (response.code === 0) {
          this.$message({ showClose: true, message: response.message, type: 'success' })
        }
      }).catch(err => {
        console.log(err)
      })
    }
  }
}
</script>
