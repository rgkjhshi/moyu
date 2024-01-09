<template>
  <div class="app-container">
    <!-- 上方选择框   -->
    <el-form ref="queryForm" :model="queryRequest" :inline="true" size="small" label-width="80px">
      <el-form-item label="表名:" prop="tableName">
        <el-input v-model="queryRequest.tableName" placeholder="请输入表名" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="表描述:" prop="tableName">
        <el-input v-model="queryRequest.tableName" placeholder="请输入表描述" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="small" @click="handleQuery">查询</el-button>
        <el-button icon="el-icon-refresh" size="small" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <!-- 选中数据操作   -->
    <el-row :gutter="10" style="margin-bottom: 10px">
      <el-col :span="2">
        <el-button type="success" plain icon="el-icon-upload" size="mini" @click="openGenFromSql">从SQL生成</el-button>
      </el-col>
      <el-col :span="2">
        <el-button type="primary" plain icon="el-icon-download" size="mini" @click="handleDownLoad">批量生成</el-button>
      </el-col>
    </el-row>
    <!-- 表格数据 -->
    <el-table v-loading="listLoading" :data="dataList" border :header-cell-style="{background:'#f5f7fa',color:'#606266'}" @selection-change="handleSelectionChange">
      <el-table-column type="selection" align="center" width="55" />
      <el-table-column label="序号" type="index" width="60px" align="center" />
      <el-table-column prop="tableName" label="表名称" width="200px" show-overflow-tooltip align="center" />
      <el-table-column prop="tableComment" label="表描述" width="200px" show-overflow-tooltip align="center" />
      <el-table-column prop="createTime" label="创建时间" width="200px" show-overflow-tooltip align="center" />
      <el-table-column prop="updateTime" label="更新时间" width="200px" show-overflow-tooltip align="center" />
      <el-table-column label="操作" align="center" min-width="160">
        <template v-slot="{row}">
          <el-button type="success" plain size="small" icon="el-icon-view" @click="handlePreview(row)">预览</el-button>
          <el-button type="primary" plain size="small" icon="el-icon-download" @click="handleDownLoad(row)">生成</el-button>
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
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
    <!-- 预览界面 -->
    <el-dialog title="代码预览" :visible.sync="preview.open" width="80%" top="5vh" fullscreen append-to-body>
      <el-tabs v-model="preview.activeName">
        <el-tab-pane v-for="(code, key) in preview.data" :key="key" :label="key" :name="key">
          <el-link v-clipboard:copy="code" v-clipboard:success="clipboardSuccess" icon="el-icon-document-copy" :underline="false" style="float:right">复制
          </el-link>
          <pre><code class="" v-html="highlightedCode(code, key)" /></pre>
        </el-tab-pane>
      </el-tabs>
    </el-dialog>
    <!-- 从SQL生成窗口 -->
    <gen-from-sql ref="genFromSql" />
  </div>
</template>

<script>

import clipboard from '@/directive/clipboard/index.js'
import GenFromSql from '@/views/moyu/tool/genFromSql'
import hljs from 'highlight.js' // 导入代码高亮文件
import 'highlight.js/styles/github.css' // 代码高亮风格，选择更多风格需导入 node_modules/hightlight.js/styles/ 目录下其它css文件

import { listDbTable, previewCode, downloadCode } from '@/api/tool/gen'

export default {
  name: 'GenCode',
  components: { GenFromSql },
  directives: {
    clipboard
  },
  data() {
    return {
      listLoading: false,
      downloadLoading: false,
      // 选中表数组
      tableNameList: [],
      dataList: [],
      total: 0,
      queryRequest: {
        // 页码
        pageNum: 1,
        // 页面大小
        pageSize: 10,
        tableName: null
      },
      // 预览参数
      preview: {
        open: false,
        data: {},
        activeName: 'Domain.java'
      }
    }
  },
  created() {
    if (this.$route.query.orderNo) {
      this.queryRequest.orderNo = this.$route.query.orderNo
    }
    this.getDataList()
  },
  methods: {
    // 获取表格内的数据列表
    getDataList() {
      this.listLoading = true
      // 查询数据
      listDbTable(this.queryRequest).then(response => {
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
    /** 预览按钮 */
    handlePreview(row) {
      const tableName = row.tableName || this.tableNameList[0]
      previewCode({ tableName: tableName }).then(response => {
        this.preview.data = response.data
        this.preview.open = true
        this.preview.activeName = 'Domain.java'
      }).catch(err => {
        console.log(err)
      })
    },
    /** 高亮显示 */
    highlightedCode(code, key) {
      var language = key.substring(key.indexOf('.') + 1, key.length)
      const result = hljs.highlight(code, { language: language, ignoreIllegals: true })
      return result.value
    },
    /** 复制代码成功 */
    clipboardSuccess() {
      this.$message({ type: 'success', message: '复制成功' })
    },
    /** 多选框选中数据 */
    handleSelectionChange(selection) {
      this.tableNameList = selection.map(item => item.tableName)
    },
    /** 生成代码操作 */
    handleDownLoad(row) {
      const tableNames = row.tableName || '' + this.tableNameList
      if (tableNames === '') {
        this.$message({ type: 'error', message: '请选择要生成的表' })
        return
      }
      downloadCode({ tableNames: tableNames }).then(response => {
      }).catch(err => {
        console.log(err)
      })
    },
    /** 打开从SQL生成的弹窗 */
    openGenFromSql() {
      this.$refs.genFromSql.show()
    }
  }
}
</script>
