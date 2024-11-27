<template>
  <div class="app-container">
    <el-row :gutter="4">
      <!-- 部门树 -->
      <el-col :span="4">
        <el-card>
          <el-input v-model="deptName" placeholder="部门名称" clearable size="small" prefix-icon="el-icon-search" style="margin-bottom:10px;" />

          <el-tree
            ref="orgTreeRef"
            :data="orgTreeData"
            :props="{ children: 'children', label: 'label', disabled: '' }"
            :expand-on-click-node="false"
            :filter-node-method="filterNode"
            node-key="id"
            default-expand-all
            highlight-current
            @node-click="handleNodeClick"
          />
        </el-card>
      </el-col>
      <!-- 数据 -->
      <el-col :span="20">
        <el-card>
          <!-- 上方选择框   -->
          <el-form ref="queryForm" :model="queryRequest" :inline="true" size="small" label-width="60px">
            <el-form-item label="用户ID:" prop="userId">
              <el-input v-model="queryRequest.userId" placeholder="请输入用户唯一ID" clearable @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" size="small" @click="handleQuery">查询</el-button>
              <el-button icon="el-icon-refresh" size="small" @click="resetQuery">重置</el-button>
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
          <el-table v-loading="dataLoading" :data="dataList" size="small" border :header-cell-style="{background:'#f5f7fa',color:'#606266'}" @selection-change="handleSelectionChange">
            <el-table-column type="selection" align="center" width="55" />
            <el-table-column label="序号" type="index" width="60px" align="center" />
            <el-table-column prop="userId" label="用户ID" width="200px" show-overflow-tooltip align="center" />
            <el-table-column prop="username" label="用户账号" width="200px" show-overflow-tooltip align="center" />
            <el-table-column prop="nickname" label="用户昵称" width="200px" show-overflow-tooltip align="center" />
            <el-table-column prop="gender" label="性别,0:未知,1:男,2:女" width="200px" show-overflow-tooltip align="center" />
            <el-table-column prop="email" label="用户邮箱" width="200px" show-overflow-tooltip align="center" />
            <el-table-column prop="mobile" label="手机号码" width="200px" show-overflow-tooltip align="center" />
            <el-table-column prop="loginIp" label="最后登录IP" width="200px" show-overflow-tooltip align="center" />
            <el-table-column prop="loginTime" label="最后登录时间" width="200px" show-overflow-tooltip align="center" />
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

import { listSysUser, addSysUser, editSysUser, deleteSysUser } from '@/api/system/sysUser'
import { getOrgTreeOptions } from '@/api/system/sysOrg'

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
      orgTreeData: [
        {
          'id': '1543842934270394368',
          'parentId': '0',
          'name': '大家科技有限公司',
          'children': [
            {
              'id': '1543842934270394369',
              'parentId': '1543842934270394368',
              'name': '领导班子'
            },
            {
              'id': '1543842934270394370',
              'parentId': '1543842934270394368',
              'name': '工会办公室'
            },
            {
              'id': '1543842934270394371',
              'parentId': '1543842934270394368',
              'name': '综合管理部'
            },
            {
              'id': '1543842934270394372',
              'parentId': '1543842934270394368',
              'name': '财务资产部'
            },
            {
              'id': '1543842934270394373',
              'parentId': '1543842934270394368',
              'name': '人力资源部'
            },
            {
              'id': '1543842934270394374',
              'parentId': '1543842934270394368',
              'name': '党群工作部'
            },
            {
              'id': '1543842934270394375',
              'parentId': '1543842934270394368',
              'name': '纪检监督部'
            },
            {
              'id': '1543842934270394376',
              'parentId': '1543842934270394368',
              'name': '生产技术部'
            },
            {
              'id': '1543842934270394377',
              'parentId': '1543842934270394368',
              'name': '计划营销部'
            },
            {
              'id': '1543842934270394378',
              'parentId': '1543842934270394368',
              'name': '后勤保卫部'
            },
            {
              'id': '1543842934270394379',
              'parentId': '1543842934270394368',
              'name': '西南分公司',
              'children': [
                {
                  'id': '1543842934270394380',
                  'parentId': '1543842934270394379',
                  'name': '综管部'
                },
                {
                  'id': '1543842934270394381',
                  'parentId': '1543842934270394379',
                  'name': '研发部'
                },
                {
                  'id': '1543842934270394382',
                  'parentId': '1543842934270394379',
                  'name': '销售部'
                },
                {
                  'id': '1543842934270394383',
                  'parentId': '1543842934270394379',
                  'name': '人事部'
                },
                {
                  'id': '1543842934270394384',
                  'parentId': '1543842934270394379',
                  'name': '采购部'
                },
                {
                  'id': '1543842934270394385',
                  'parentId': '1543842934270394379',
                  'name': '技术部'
                },
                {
                  'id': '1543842934270394386',
                  'parentId': '1543842934270394379',
                  'name': '质检部'
                }
              ]
            },
            {
              'id': '1543842934270394379',
              'parentId': '1543842934270394368',
              'name': '东南分公司',
              'children': [
                {
                  'id': '1543842934270394380',
                  'parentId': '1543842934270394379',
                  'name': '综管部'
                },
                {
                  'id': '1543842934270394381',
                  'parentId': '1543842934270394379',
                  'name': '研发部'
                },
                {
                  'id': '1543842934270394382',
                  'parentId': '1543842934270394379',
                  'name': '销售部'
                },
                {
                  'id': '1543842934270394383',
                  'parentId': '1543842934270394379',
                  'name': '人事部'
                },
                {
                  'id': '1543842934270394384',
                  'parentId': '1543842934270394379',
                  'name': '采购部'
                },
                {
                  'id': '1543842934270394385',
                  'parentId': '1543842934270394379',
                  'name': '技术部'
                },
                {
                  'id': '1543842934270394386',
                  'parentId': '1543842934270394379',
                  'name': '质检部'
                }
              ]
            }
          ]
        }
      ],
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
      listSysUser(this.queryRequest).then(response => {
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
      // this.queryParams.deptId = data.id
      // this.handleQuery()
      console.log(data.name)
    }

  }
}
</script>
