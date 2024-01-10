<template>
  <div class="app-container">
    <!-- 从SQL生成 -->
    <el-dialog title="从SQL生成" :visible.sync="visible" append-to-body>
      <span>支持多个建表语句，预览时只会预览第一个</span>
      <el-input v-model="sql" type="textarea" placeholder="请输入建表语句" :autosize="{ minRows: 20}" />
      <div slot="footer">
        <el-button type="success" plain size="small" icon="el-icon-view" @click="handlePreview()">预览</el-button>
        <el-button type="primary" plain size="small" icon="el-icon-download" @click="handleDownLoad()">生成</el-button>
        <el-button @click="visible = false">取 消</el-button>
      </div>
    </el-dialog>
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
  </div>
</template>

<script>

import clipboard from '@/directive/clipboard/index.js'
import { downloadCodeBySql, previewCodeBySql } from '@/api/tool/gen'
import hljs from 'highlight.js' // 导入代码高亮文件
import 'highlight.js/styles/github.css' // 代码高亮风格，选择更多风格需导入 node_modules/hightlight.js/styles/ 目录下其它css文件
hljs.registerLanguage('vue', require('highlight.js/lib/languages/xml'))

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
    /** 高亮显示 */
    highlightedCode(code, key) {
      var language = key.substring(key.indexOf('.') + 1, key.length)
      const result = hljs.highlight(language, code, true)
      // const result = hljs.highlight(code, { language: language, ignoreIllegals: true })
      return result.value
    },
    /** 生成代码操作 */
    handleDownLoad(row) {
      if (!this.sql) {
        this.$message({ type: 'error', message: '建表语句不能为空' })
        return
      }
      downloadCodeBySql({ sql: this.sql })
    }
  }
}
</script>
