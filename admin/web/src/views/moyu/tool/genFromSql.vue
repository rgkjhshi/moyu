<template>
  <div class="app-container">
    <!-- 从SQL生成 -->
    <el-dialog title="从SQL生成" :visible.sync="visible" append-to-body>
      <span>支持多个建表语句，预览时只会预览第一个</span>
      <el-input v-model="sql" type="textarea" placeholder="请输入建表语句" :autosize="{ minRows: 20}" />
      <div slot="footer">
        <el-button type="success" plain size="small" icon="el-icon-view" @click="handlePreview()">预览</el-button>
        <el-button type="primary" plain size="small" icon="el-icon-download" @click="handleDownLoad()">下载</el-button>
        <el-button @click="visible = false">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 预览界面 -->
    <el-dialog title="代码预览" :visible.sync="preview.open" width="80%" top="5vh" fullscreen append-to-body>
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
import { listDbTable, previewCodeBySql } from '@/api/tool/gen'

export default {
  name: 'GenFromSql',
  directives: {
    clipboard
  },
  data() {
    return {
      visible: false,
      sql: '',
      // 预览参数
      preview: {
        open: false,
        data: {},
        activeName: 'Domain.java'
      }
    }
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
    // 展示弹窗
    show() {
      this.visible = true
    },
    /** 预览按钮 */
    handlePreview() {
      if (!this.sql) {
        this.$message({ type: 'error', message: '建表语句不能为空' })
        return
      }
      previewCodeBySql({ sql: this.sql }).then(response => {
        this.preview.data = response.data
        this.preview.open = true
        this.preview.activeName = 'Domain.java'
      })
    },
    /** 复制代码成功 */
    clipboardSuccess() {
      this.$message({ type: 'success', message: '复制成功' })
    },
    /** 生成代码操作 */
    handleDownLoad(row) {
      const tableNameList = row.tableName || '' + this.tableNameList
      if (tableNameList === '') {
        this.$message({ type: 'error', message: '请选择要生成的表' })
        return
      }
      console.log(tableNameList)
      // this.$download.zip("/tool/gen/genCode?tableList=" + tableNames, "moyu.zip");
    }
  }
}
</script>
