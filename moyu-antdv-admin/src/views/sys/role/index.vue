<template>
	<a-card size="small">
		<a-form ref="searchFormRef" :model="searchFormData">
			<a-row :gutter="24">
				<a-col :span="8">
					<a-form-item name="searchKey" label="名称关键词">
						<a-input v-model:value="searchFormData.searchKey" placeholder="请输入关键词" allowClear />
					</a-form-item>
				</a-col>
				<a-col :span="6">
					<a-form-item label="使用状态" name="status">
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
			:data="loadData"
			:alert="options.alert.show"
			bordered
			:row-key="(record) => record.id"
			:row-selection="options.rowSelection"
			:tool-config="toolConfig"
		>
			<template #operator>
				<a-space>
					<a-button type="primary" :icon="h(PlusOutlined)" @click="addFormRef.onOpen(module)">新增角色</a-button>
					<BatchDeleteButton icon="DeleteOutlined" :selectedRowKeys="selectedRowKeys" @batchDelete="deleteBatchRole" />
				</a-space>
			</template>
			<template #bodyCell="{ column, record }">
				<template v-if="column.dataIndex === 'code'">
					<a-tag v-if="record.code" :bordered="false">{{ record.code }}</a-tag>
				</template>
				<template v-if="column.dataIndex === 'status'">
					<a-tag v-if="record.status === 0" color="green">正常</a-tag>
					<a-tag v-else>已停用</a-tag>
				</template>
				<template v-if="column.dataIndex === 'action'">
					<a-space>
            <a-tooltip title="功能授权">
              <a style="color:#1980FF;" @click="grantMenuFormRef.onOpen(record)"><DeploymentUnitOutlined /></a>
            </a-tooltip>
            <a-divider type="vertical" />
            <a-tooltip title="用户授权">
              <a style="color:#53C61D;" @click="roleUserRef.onOpen(record)"><UserAddOutlined /></a>
            </a-tooltip>
						<a-divider type="vertical" />
						<a-tooltip title="编辑">
							<a @click="editFormRef.onOpen(record)"><FormOutlined /></a>
						</a-tooltip>
						<a-divider type="vertical" />
						<a-tooltip title="删除">
							<a-popconfirm title="确定要删除吗？" @confirm="deleteRole(record)">
								<a style="color:#FF4D4F;"><DeleteOutlined/></a>
							</a-popconfirm>
						</a-tooltip>
					</a-space>
				</template>
			</template>
		</STable>
	</a-card>
	<grant-menu-form ref="grantMenuFormRef" @successful="tableRef.refresh()" />
	<AddForm ref="addFormRef" @successful="tableRef.refresh()" />
	<EditForm ref="editFormRef" @successful="tableRef.refresh()" />
  <RoleUser ref="roleUserRef" />
</template>

<script setup>
	import roleApi from '@/api/sys/roleApi'

	import { h } from "vue"
	import { PlusOutlined, RedoOutlined, SearchOutlined } from "@ant-design/icons-vue"
	import AddForm from "./addForm.vue";
	import EditForm from "./editForm.vue";
	import GrantMenuForm from "./grantMenuForm.vue";
	import { message } from "ant-design-vue";
  import BatchDeleteButton from "@/components/BatchDeleteButton/index.vue"
  import STable from "@/components/STable/index.vue"
  import RoleUser from "./roleUser.vue";

	const columns = [
		{
			title: '角色名称',
			dataIndex: 'name',
			resizable: true,
			width: 150
		},
		{
			title: '唯一编码',
			dataIndex: 'code',
			resizable: true,
			width: 200
		},
		{
			title: '排序',
			dataIndex: 'sortNum',
			sorter: true,
			align: 'center',
			width: 100
		},
		{
			title: '状态',
			dataIndex: 'status',
			align: 'center',
			width: 100
		},
		{
			title: '创建时间',
			dataIndex: 'createTime',
			align: 'center',
			width: 160
		},
		{
			title: '更新时间',
			dataIndex: 'updateTime',
			align: 'center',
			width: 160
		},
		{
			title: '操作',
			dataIndex: 'action',
			align: 'center',
			resizable: true,
			width: 150
		}
	]
	const selectedRowKeys = ref([])
	// 使用状态options（0正常 1停用）
	const statusOptions = [
		{ label: "正常", value: 0 },
		{ label: "已停用", value: 1 }
	]
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
	// 定义tableDOM
	const tableRef = ref()
	const formRef = ref()
	const addFormRef = ref()
	const editFormRef = ref()
  const module = ref()
	const toolConfig = { refresh: true, height: true, columnSetting: false, striped: false }
	const grantMenuFormRef = ref()
  const roleUserRef = ref()
	const searchFormRef = ref()
	const searchFormData = ref({})

	// 表格查询 返回 Promise 对象
	const loadData = (parameter) => {
		let param = Object.assign(parameter, searchFormData.value)
		return roleApi.rolePage(param).then((res) => {
			return res.data
		})
	}
	// 重置
	const reset = () => {
		searchFormRef.value.resetFields()
		tableRef.value.refresh(true)
	}
	// 删除
	const deleteRole = (record) => {
		let data = { ids: [record.id] }
		roleApi.deleteRole(data).then((res) => {
			message.success(res.message)
			tableRef.value.refresh(true)
		})
	}
	// 批量删除
	const deleteBatchRole = (params) => {
		let data = { ids: selectedRowKeys.value }
		roleApi.deleteRole(data).then((res) => {
			message.success(res.message)
			tableRef.value.clearRefreshSelected()
		})
	}
</script>

<style scoped>
	.ant-form-item {
		margin-bottom: 0 !important;
	}
</style>
