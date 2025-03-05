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
					<a-form-item name="status" label="使用状态">
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
			:tool-config="toolConfig"
			:row-selection="options.rowSelection"
		>
			<template #operator>
				<a-space>
					<a-button type="primary" :icon="h(PlusOutlined)" @click="addFormRef.onOpen()">新增模块</a-button>
					<BatchDeleteButton icon="DeleteOutlined" :selectedRowKeys="selectedRowKeys" @batchDelete="deleteBatchModule" />
				</a-space>
			</template>
			<template #bodyCell="{ column, record }">
				<template v-if="column.dataIndex === 'icon'">
					<span v-if="record.icon && record.icon !== '#'" >
						<component :is="record.icon"/>
					</span>
					<span v-else />
				</template>
				<template v-if="column.dataIndex === 'code'">
					<a-tag v-if="record.code" :bordered="false">{{ record.code }}</a-tag>
				</template>
        <template v-if="column.dataIndex === 'path'">
          <a-tag v-if="record.path" :bordered="false">{{ record.path }}</a-tag>
        </template>
				<template v-if="column.dataIndex === 'link'">
					<a-tag v-if="record.link" :bordered="false">{{ record.link }}</a-tag>
				</template>
				<template v-if="column.dataIndex === 'status'">
					<a-tag v-if="record.status === 0" color="green">正常</a-tag>
					<a-tag v-else>已停用</a-tag>
				</template>
				<template v-if="column.dataIndex === 'action'">
					<a-space>
						<a-tooltip title="编辑">
							<a @click="editFormRef.onOpen(record)"><FormOutlined /></a>
						</a-tooltip>
						<a-divider type="vertical" />
						<a-tooltip title="删除">
							<a-popconfirm title="确定要删除吗？" @confirm="deleteModule(record)">
								<a style="color:#FF4D4F;"><DeleteOutlined/></a>
							</a-popconfirm>
						</a-tooltip>
					</a-space>
				</template>
			</template>
		</STable>
	</a-card>
	<AddForm ref="addFormRef" @successful="tableRef.refresh(true)" />
	<EditForm ref="editFormRef" @successful="tableRef.refresh(true)" />
</template>

<script setup>
	import resourceApi from '@/api/sys/resourceApi.js'
	import { h } from "vue";
	import { PlusOutlined, RedoOutlined, SearchOutlined } from "@ant-design/icons-vue";
	import AddForm from "@/views/sys/resource/module/addForm.vue";
	import EditForm from "@/views/sys/resource/module/editForm.vue";
	import { message } from "ant-design-vue";
  import BatchDeleteButton from "@/components/BatchDeleteButton/index.vue"
  import STable from "@/components/STable/index.vue"

	// resourceType=1标识模块
	const searchFormData = ref({ resourceType: 1 })
	const addFormRef = ref()
	const editFormRef = ref()
	const searchFormRef = ref()
	const tableRef = ref()
	const toolConfig = { refresh: true, height: true, columnSetting: false, striped: false }
	const columns = [
		{
			title: '图标',
			dataIndex: 'icon',
			align: 'center',
			width: 50
		},
		{
			title: '模块名称',
			dataIndex: 'name',
			width: 100
		},
		{
			title: '模块编码',
			dataIndex: 'code',
			width: 100
		},
    {
      title: '路径地址',
      dataIndex: 'path',
      width: 100
    },
		{
			title: '模块主页',
			dataIndex: 'link',
			width: 200
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
			width: 100
		},
		{
			title: '操作',
			dataIndex: 'action',
			align: 'center',
			width: 100
		}
	]
	let selectedRowKeys = ref([])
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
				selectedRowKeys = ref([])
			}
		},
		rowSelection: {
			onChange: (selectedRowKey, selectedRows) => {
				selectedRowKeys.value = selectedRowKey
			}
		}
	}
	const loadData = (parameter) => {
		return resourceApi.resourcePage(Object.assign(parameter, searchFormData.value)).then((res) => {
			return res.data
		})
	}
	// 重置
	const reset = () => {
		searchFormRef.value.resetFields()
		tableRef.value.refresh(true)
	}
	// 删除
	const deleteModule = (record) => {
		let data = { ids: [record.id] }
		resourceApi.deleteResource(data).then((res) => {
			message.success(res.message)
			tableRef.value.refresh(true)
		})
	}
	// 批量删除
	const deleteBatchModule = (params) => {
		let data = { ids: selectedRowKeys.value }
		resourceApi.deleteResource(data).then((res) => {
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
