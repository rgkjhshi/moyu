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
					:data="loadTableData"
          :scroll="{ x: 'max-content' }"
					bordered
					:row-key="(record) => record.id"
					:tool-config="toolConfig"
					:row-selection="options.rowSelection"
				>
					<template #operator class="table-operator">
						<a-space>
							<a-button type="primary" :icon="h(PlusOutlined)" @click="addFormRef.onOpen(searchFormData.orgCode, treeRef.treeData)">新增</a-button>
							<BatchDeleteButton icon="DeleteOutlined" :selectedRowKeys="selectedRowKeys" @batchDelete="batchDeleteGroup" />
						</a-space>
					</template>
					<template #bodyCell="{ column, record }">
						<template v-if="column.dataIndex === 'code'">
							<a-tag v-if="record.code" :bordered="false">{{ record.code }}</a-tag>
						</template>
            <template v-if="column.dataIndex === 'dataScope'">
              <!-- 数据范围(字典 0无限制 1本人数据 2本机构 3本机构及以下 4自定义) -->
              <a-tag v-if="record.dataScope === 1" color="orange">本人数据</a-tag>
              <a-tag v-if="record.dataScope === 2" color="cyan">本机构</a-tag>
              <a-tag v-if="record.dataScope === 3" color="blue">本机构及以下</a-tag>
              <a-tag v-if="record.dataScope === 4" color="purple">自定义</a-tag>
            </template>
						<template v-if="column.dataIndex === 'status'">
							<a-tag v-if="record.status === 0" color="green">正常</a-tag>
							<a-tag v-else>已停用</a-tag>
						</template>
						<template v-if="column.dataIndex === 'action'">
							<a-space>
								<a-tooltip title="分配角色">
									<a style="color:#1980FF;" @click="groupRoleRef.onOpen(record)"><DeploymentUnitOutlined /></a>
								</a-tooltip>
								<a-divider type="vertical" />
								<a-tooltip title="分配用户">
									<a style="color:#53C61D;" @click="groupUserRef.onOpen(record, treeRef.treeData)"><UsergroupAddOutlined /></a>
								</a-tooltip>
								<a-divider type="vertical" />
								<a-tooltip title="编辑">
									<a @click="editFormRef.onOpen(record, treeRef.treeData)"><FormOutlined /></a>
								</a-tooltip>
								<a-divider type="vertical" />
								<a-tooltip title="删除">
									<a-popconfirm title="确定要删除吗？" @confirm="deleteGroup(record)">
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
	<EditForm ref="editFormRef" @successful="tableRef.refresh()" />
	<AddForm ref="addFormRef" @successful="tableRef.refresh()" />
	<GroupRole ref="groupRoleRef" @successful="handleSuccess()" />
	<GroupUser ref="groupUserRef" @successful="handleSuccess()" />
</template>

<script setup>
	import groupApi from '@/api/sys/groupApi'
	import { onMounted, h } from "vue";
	import { message } from 'ant-design-vue'
	import { PlusOutlined, RedoOutlined, SearchOutlined } from "@ant-design/icons-vue";
	import AddForm from './addForm.vue'
	import EditForm from './editForm.vue'
	import GroupRole from './groupRole.vue'
	import GroupUser from './groupUser.vue'
	import OrgTree from "../components/orgTree.vue"
	import BatchDeleteButton from "@/components/BatchDeleteButton/index.vue"
  import STable from "@/components/STable/index.vue"

	const columns = [
		{
			title: '分组名称',
			dataIndex: 'name',
			resizable: true,
			width: 200
		},
		{
			title: '组织机构',
			dataIndex: 'orgName',
			resizable: true,
			width: 200,
			ellipsis: true
		},
    {
      title: '数据范围',
      dataIndex: 'dataScope',
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
			title: '创建时间',
			dataIndex: 'createTime',
			align: 'center',
			width: 160
		},
		{
			title: '操作',
			dataIndex: 'action',
			align: 'center',
			width: 200,
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
	const groupUserRef = ref()
	const groupRoleRef = ref()
	const searchFormRef = ref()
	const searchFormData = ref({})

	// 定义treeRef
	const treeRef = ref()

	// 表格查询 返回 Promise 对象
	const loadTableData = (parameter) => {
		return groupApi.groupPage(Object.assign(parameter, searchFormData.value)).then((res) => {
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
	// 单个删除
	const deleteGroup = (record) => {
		let data = { ids: [record.id] }
		groupApi.deleteGroup(data).then((res) => {
			message.success(res.message)
			tableRef.value.refresh(true)
		})
	}
	// 批量删除
	const batchDeleteGroup = (params) => {
		let data = { ids: selectedRowKeys.value }
		groupApi.deleteGroup(data).then((res) => {
			message.success(res.message)
			tableRef.value.clearRefreshSelected()
		})
	}
	// 成功回调
	const handleSuccess = () => {
		loadTableData()
	}
</script>

<style scoped>
	.ant-form-item {
		margin-bottom: 0 !important;
	}
</style>
