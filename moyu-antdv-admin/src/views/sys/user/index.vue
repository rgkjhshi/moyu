<template>
	<a-row :gutter="8">
		<!-- 左侧组织树 -->
		<a-col :span="5">
      <OrgTree ref="treeRef" @onSelect="treeSelect"></OrgTree>
		</a-col>
		<!-- 右侧内容 -->
		<a-col :span="19">
			<a-card size="small">
				<a-form ref="searchFormRef" :model="searchFormData">
					<a-row :gutter="24">
						<a-col :span="8">
							<a-form-item name="searchKey" label="关键词">
								<a-input v-model:value="searchFormData.searchKey" placeholder="请输入姓名或关键词" allowClear />
							</a-form-item>
						</a-col>
						<a-col :span="6">
							<a-form-item label="用户状态" name="status">
								<a-select v-model:value="searchFormData.status" placeholder="请选择状态" :options="statusOptions" allowClear />
							</a-form-item>
						</a-col>
						<a-col :span="8">
							<a-space>
								<a-button type="primary" :icon="h(SearchOutlined)" @click="tableRef.refresh(true)">查询</a-button>
								<a-button :icon="h(RedoOutlined)" @click="reset">重置</a-button>
							</a-space>
						</a-col>
					</a-row>
				</a-form>
			</a-card>
			<a-card size="small">
				<STable
					ref="tableRef"
					:columns="columns"
					:data="loadTableData"
					:scroll="{ x: 'max-content' }"
					bordered
					:alert="options.alert.show"
					:row-key="(record) => record.id"
					:row-selection="options.rowSelection"
					:tool-config="toolConfig"
				>
					<template #operator>
						<a-space>
							<a-button type="primary" :icon="h(PlusOutlined)" @click="addFormRef.onOpen(searchFormData.orgCode, treeRef.treeData)">新增用户</a-button>
							<BatchDeleteButton icon="DeleteOutlined" :selectedRowKeys="selectedRowKeys" @batchDelete="batchDeleteUser" />
						</a-space>
					</template>
					<template #bodyCell="{ column, record }">
						<template v-if="column.dataIndex === 'gender'">
							<a-tag v-if="record.gender === 1" color="blue">男</a-tag>
							<a-tag v-else-if="record.gender === 2" color="pink">女</a-tag>
							<a-tag v-else>未知</a-tag>
						</template>
						<template v-if="column.dataIndex === 'status'">
							<a-tag v-if="record.status === 0" color="green">正常</a-tag>
							<a-tag v-else>已停用</a-tag>
						</template>
						<template v-if="column.dataIndex === 'action'">
							<a-space>
								<a-tooltip title="编辑">
									<a @click="editFormRef.onOpen(record, treeRef.treeData)"><FormOutlined /></a>
								</a-tooltip>
								<a-divider type="vertical" />
								<a-tooltip title="删除">
									<a-popconfirm title="确定要删除吗？" @confirm="deleteUser(record)">
										<a style="color:#FF4D4F;"><DeleteOutlined/></a>
									</a-popconfirm>
								</a-tooltip>
								<a-divider type="vertical" />
								<a-tooltip title="重置密码">
								  <a-popconfirm title="确定要重置吗？" @confirm="resetPassword(record)">
									<a style="color:darkorange;"><LockOutlined/></a>
								  </a-popconfirm>
								</a-tooltip>
							</a-space>
						</template>
					</template>
				</STable>
			</a-card>
		</a-col>
	</a-row>
	<EditForm ref="editFormRef" @successful="tableRef.refresh()" />
	<AddForm ref="addFormRef" @successful="tableRef.refresh()" />
</template>

<script setup>
	import userApi from '@/api/sys/userApi'

	import { h } from "vue";
	import { message, Empty } from 'ant-design-vue'
	import { SearchOutlined, RedoOutlined, PlusOutlined } from "@ant-design/icons-vue";
	import AddForm from './addForm.vue'
	import EditForm from "./editForm.vue"
	import OrgTree from "../components/orgTree.vue";
	import BatchDeleteButton from "@/components/BatchDeleteButton/index.vue"
  import STable from "@/components/STable/index.vue"

	const columns = [
		{
			title: '账号',
			dataIndex: 'account',
			resizable: true,
			width: 200,
			ellipsis: true
		},
		{
			title: '姓名',
			dataIndex: 'name',
			resizable: true,
			width: 150
		},
		{
			title: '性别',
			dataIndex: 'gender',
			align: 'center',
			width: 80
		},
		{
			title: '手机',
			dataIndex: 'phone',
			align: 'center',
			width: 150
		},
		{
			title: '组织机构',
			dataIndex: 'orgName',
			resizable: true,
			width: 200,
			ellipsis: true
		},
		{
			title: '状态',
			dataIndex: 'status',
			align: 'center',
			width: 80
		},
		{
			title: '操作',
			dataIndex: 'action',
			align: 'center',
			resizable: true,
			width: 200
		}
	]
	const selectedRowKeys = ref([])
	// 列表选择配置
	const options = {
		alert: {
			show: false,
			clear: () => {
				selectedRowKeys.value = ref([])
			}
		},
		rowSelection: {
			onChange: (selectedRowKey, selectedRows) => {
				selectedRowKeys.value = selectedRowKey
			}
		}
	}
	// 使用状态options（0正常 1停用）
	const statusOptions = [
		{ label: "正常", value: 0 },
		{ label: "已停用", value: 1 }
	]
	// 定义tableDOM
	const tableRef = ref()
	const toolConfig = { refresh: true, height: true, columnSetting: false, striped: false }
	const addFormRef = ref()
	const editFormRef = ref()
	const searchFormRef = ref()
	const searchFormData = ref({})
  // 定义treeRef
  const treeRef = ref()

	const loading = ref(false)

	// 表格查询 返回 Promise 对象
	const loadTableData = (parameter) => {
		return userApi.userPage(Object.assign(parameter, searchFormData.value)).then((res) => {
			return res.data
		})
	}
	// 重置
	const reset = () => {
		searchFormRef.value.resetFields()
		tableRef.value.refresh(true)
	}
	// 点击树查询
	const treeSelect = (selectedKeys) => {
		if (selectedKeys.length > 0) {
			searchFormData.value.orgCode = selectedKeys.toString()
		} else {
			delete searchFormData.value.orgCode
		}
		tableRef.value.refresh(true)
	}
	// 删除用户
	const deleteUser = (record) => {
		let data = { ids: [record.id] }
		userApi.deleteUser(data).then((res) => {
			message.success(res.message)
			tableRef.value.refresh(true)
		})
	}
	// 批量删除
	const batchDeleteUser = (params) => {
		let data = { ids: selectedRowKeys.value }
		userApi.deleteUser(data).then((res) => {
			message.success(res.message)
			tableRef.value.clearRefreshSelected()
		})
	}
	// 批量导出
	const exportBatchUser = (params) => {

	}

	// 重置用户密码
	const resetPassword = (record) => {
		let data = { ids: [record.id] }
		userApi.resetPassword(data).then((res) => {
			message.success(res.message)
		})
	}
</script>

<style scoped>
	.ant-form-item {
		margin-bottom: 0 !important;
	}
</style>
