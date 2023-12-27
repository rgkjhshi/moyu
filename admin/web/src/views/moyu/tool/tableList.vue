<template>
  <div class="app-container">
    <!-- 上方选择框   -->
    <el-form ref="queryRequest" :model="queryRequest" :inline="true" size="small">
      <el-form-item label-width="60px" label="表名:">
        <el-input v-model="queryRequest.orderNo" placeholder="请输入表名" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label-width="60px" label="表描述:">
        <el-input v-model="queryRequest.orderNo" placeholder="请输入表描述" clearable />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="small" @click="handleQuery">查询</el-button>
        <el-button icon="el-icon-refresh" size="small" @click="handleQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <!-- 选中数据操作   -->
    <el-row :gutter="10" style="margin-bottom: 10px">
      <el-col :span="1.5">
        <el-button v-hasPermi="['tool:gen:code']" type="primary" plain icon="el-icon-download" size="mini" @click="handleGenTable">生成</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button v-hasPermi="['tool:gen:import']" type="info" plain icon="el-icon-upload" size="mini" @click="openImportTable">导入</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button v-hasPermi="['tool:gen:edit']" type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleEditTable">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button v-hasPermi="['tool:gen:remove']" type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete">删除</el-button>
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
          <el-button type="primary" plain size="small" icon="el-icon-view" @click="handlePreview(row)">预览</el-button>
          <el-button type="success" plain size="small" icon="el-icon-download" @click="handleGenTable(row)">生成代码</el-button>
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
    <el-dialog :title="preview.title" :visible.sync="preview.open" width="80%" top="5vh" fullscreen append-to-body>
      <el-tabs v-model="preview.activeName">
        <el-tab-pane v-for="(code, key) in preview.data" :key="key" :label="key" :name="key">
          <el-link v-clipboard:copy="code" v-clipboard:success="clipboardSuccess" icon="el-icon-document-copy" :underline="false" style="float:right">复制
          </el-link>
          <pre v-highlight><code class="">{{ code }}</code></pre>
        </el-tab-pane>
      </el-tabs>
    </el-dialog>
  </div>
</template>

<script>

import clipboard from '@/directive/clipboard/index.js'
import { listDbTable, previewCode } from '@/api/tool/gen'

export default {
  name: 'TableList',
  directives: {
    clipboard
  },
  data() {
    return {
      listLoading: false,
      downloadLoading: false,
      dataList: [],
      total: 0,
      queryRequest: {
        startDate: null,
        endDate: null,
        // 页码
        pageNum: 1,
        // 页面大小
        pageSize: 10
      },
      // 预览参数
      preview: {
        open: false,
        title: '代码预览',
        data: {},
        activeName: 'Domain.java'
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
          this.total = 1
          this.dataList = response.data
        }
        this.listLoading = false
      }).catch(err => {
        console.log(err)
        this.listLoading = false
      })
    },
    handleQuery() {
      this.getDataList()
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
      previewCode({ tableName: row.tableName }).then(response => {
        this.preview.data = response.data
        this.preview.open = true
        this.preview.activeName = 'Domain.java'
      })
    },
    /** 复制代码成功 */
    clipboardSuccess() {
      this.$message({ type: 'success', message: '复制成功' })
    }
  }
}
</script>
