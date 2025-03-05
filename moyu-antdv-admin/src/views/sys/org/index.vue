<template>
	<a-row :gutter="8">
		<!-- 左侧组织树 -->
		<a-col :span="5">
			<OrgTree ref="treeRef" @onSelect="treeSelect"></OrgTree>
		</a-col>
		<!-- 右侧内容 -->
		<a-col :span="19">
			<a-card size="small">
				<a-form ref="searchFormRef" :model="searchFormState">
					<a-row :gutter="24">
						<a-col :span="8">
							<a-form-item name="searchKey" label="名称关键词">
								<a-input v-model:value="searchFormState.searchKey" placeholder="请输入关键词" allowClear />
							</a-form-item>
						</a-col>
						<a-col :span="6">
							<a-form-item label="使用状态" name="status">
								<a-select v-model:value="searchFormState.status" placeholder="请选择状态" :options="statusOptions" allowClear />
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
					:expand-row-by-click="true"
					:alert="options.alert.show"
          :scroll="{ x: 'max-content' }"
					bordered
					:row-key="(record) => record.code"
					:tool-config="toolConfig"
					:row-selection="options.rowSelection"
				>
					<template #operator class="table-operator">
						<a-space>
							<a-button type="primary" :icon="h(PlusOutlined)" @click="addFormRef.onOpen(searchFormState.parentCode, treeRef.treeData)">新增</a-button>
							<BatchDeleteButton icon="DeleteOutlined" :selectedRowKeys="selectedRowKeys" @batchDelete="batchDeleteOrg" />
						</a-space>
					</template>
					<template #bodyCell="{ column, record }">
						<template v-if="column.dataIndex === 'code'">
							<a-tag v-if="record.code" :bordered="false">{{ record.code }}</a-tag>
						</template>
						<template v-if="column.dataIndex === 'orgType'">
							<a-tag v-if="record.orgType === 1" color="cyan">公司组织</a-tag>
							<a-tag v-if="record.orgType === 2" color="blue">部门机构</a-tag>
							<a-tag v-if="record.orgType === 3" color="purple">虚拟节点</a-tag>
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
									<a-popconfirm title="确定要删除吗？" @confirm="deleteOrg(record)">
										<a style="color:#FF4D4F;"><DeleteOutlined/></a>
									</a-popconfirm>
								</a-tooltip>
							</a-space>
						</template>
					</template>
				</STable>
			</a-card>
		</a-col>
	</a-row>
	<EditForm ref="editFormRef" @successful="handleSuccess" />
	<AddForm ref="addFormRef" @successful="handleSuccess" />
</template>

<script setup>
	import { onMounted, h } from "vue";
	import orgApi from '@/api/sys/orgApi'
	import { Empty, message } from 'ant-design-vue'
	import { PlusOutlined, RedoOutlined, SearchOutlined } from "@ant-design/icons-vue";
	import AddForm from './addForm.vue'
	import EditForm from './editForm.vue'
	import OrgTree from "../components/orgTree.vue"
  import BatchDeleteButton from "@/components/BatchDeleteButton/index.vue"
  import STable from "@/components/STable/index.vue"

	const columns = [
		{
			title: '组织名称',
			dataIndex: 'name',
			resizable: true,
			width: 200
		},
		{
			title: '组织编码',
			dataIndex: 'code',
			width: 100
		},
		{
			title: '组织类型',
			dataIndex: 'orgType',
			align: 'center',
			width: 80
		},
		{
			title: '组织层级',
			dataIndex: 'orgLevel',
			align: 'center',
			width: 80
		},
		{
			title: '排序',
			dataIndex: 'sortNum',
			sorter: true,
			align: 'center',
			width: 80
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
			width: 100
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
	const searchFormState = ref({})
	// 定义treeRef
	const treeRef = ref()

	// 表格查询 返回 Promise 对象
	const loadTableData = (parameter) => {
		return orgApi.orgPage(Object.assign(parameter, searchFormState.value)).then((res) => {
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
			searchFormState.value.parentCode = selectedKeys.toString()
		} else {
			delete searchFormState.value.parentCode
		}
		tableRef.value.refresh(true)
	}
	// 单个删除
	const deleteOrg = (record) => {
		let data = { codes: [record.code] }
		orgApi.deleteOrgTree(data).then((res) => {
			message.success(res.message)
			tableRef.value.refresh(true)
		})
	}
	// 批量删除
	const batchDeleteOrg = (params) => {
		let data = { codes: selectedRowKeys.value }
		orgApi.deleteOrgTree(data).then((res) => {
			message.success(res.message)
			tableRef.value.clearRefreshSelected()
		})
	}
	// 成功回调
	const handleSuccess = () => {
		treeRef.value.refresh()
		tableRef.value.refresh()
	}
</script>

<style scoped>
	.ant-form-item {
		margin-bottom: 0 !important;
	}
</style>
