<template>
	<!-- 上方模块选择 -->
	<a-card size="small">
		<a-space>
			<a-radio-group v-model:value="moduleId" button-style="solid">
				<a-radio-button v-for="module in moduleList" :key="module.code" :value="module.code" @click="moduleClick(module)">
					<component :is="module.icon" /> {{ module.name }}
				</a-radio-button>
			</a-radio-group>
		</a-space>
	</a-card>
	<!-- 内容区域 -->
	<a-card size="small">
		<STable
			ref="tableRef"
			:columns="columns"
			:data="loadData"
			:alert="options.alert.show"
			bordered
			:row-key="(node) => node.code"
			:show-pagination="false"
			:tool-config="toolConfig"
			:row-selection="options.rowSelection"
			:scroll="{ x: 'max-content' }"
		>
			<template #operator>
				<a-space>
					<a-button type="primary" :icon="h(PlusOutlined)" @click="addFormRef.onOpen(module, 2, module.code)">新增菜单</a-button>
					<BatchDeleteButton icon="DeleteOutlined" :selectedRowKeys="selectedRowKeys" @batchDelete="batchDeleteMenu" />
				</a-space>
			</template>
			<template #bodyCell="{ column, record : node }">
				<template v-if="column.dataIndex === 'resourceType'">
					<a-tag v-if="node.resourceType === 1" color="orange">模块</a-tag>
					<a-tag v-if="node.resourceType === 2" color="cyan">目录</a-tag>
					<a-tag v-if="node.resourceType === 3" color="blue">菜单</a-tag>
					<a-tag v-if="node.resourceType === 4" color="purple">按钮</a-tag>
					<a-tag v-if="node.resourceType === 5" color="green">链接</a-tag>
				</template>
				<template v-if="column.dataIndex === 'path'">
					<a-tag v-if="node.path" :bordered="false">{{ node.path }}</a-tag>
				</template>
				<template v-if="column.dataIndex === 'component'">
					<a-tag v-if="node.path" :bordered="false">{{ node.component }}</a-tag>
				</template>
				<template v-if="column.dataIndex === 'permission'">
					<a-tag v-if="node.permission" :bordered="false">{{ node.permission }}</a-tag>
				</template>
				<template v-if="column.dataIndex === 'icon'">
					<span v-if="node.icon && node.icon !== '#'" >
						<component :is="node.icon"/>
					</span>
					<span v-else />
				</template>
				<template v-if="column.dataIndex === 'visible'">
					<span v-if="node.resourceType !== 4" >
						<a-tag v-if="node.visible === 1" color="green">可见</a-tag>
						<a-tag v-else>不可见</a-tag>
					</span>
					<span v-else ></span>
				</template>
				<template v-if="column.dataIndex === 'action'">
					<a-space>
						<a-tooltip v-if="node.resourceType === 2" title="添加菜单">
							<a style="color:#53C61D;" @click="addFormRef.onOpen(module, 3, node.code)"><PlusSquareOutlined /></a>
              <a-divider type="vertical" />
						</a-tooltip>
						<a-tooltip v-else-if="node.resourceType === 3" title="添加按钮">
							<a style="color:#53C61D;" @click="addFormRef.onOpen(module, 4, node.code)"><PlusSquareOutlined /></a>
              <a-divider type="vertical" />
						</a-tooltip>
						<a-tooltip title="编辑">
							<a @click="editFormRef.onOpen(node, module)"><FormOutlined /></a>
						</a-tooltip>
						<a-divider type="vertical" />
						<a-tooltip title="删除">
							<a-popconfirm title="确定要删除吗？" @confirm="deleteMenu(node)">
								<a style="color:#FF4D4F;"><DeleteOutlined/></a>
							</a-popconfirm>
						</a-tooltip>
					</a-space>
				</template>
			</template>
		</STable>
	</a-card>
	<AddForm ref="addFormRef" @successful="handleSuccess" />
	<EditForm ref="editFormRef" @successful="handleSuccess" />
</template>

<script setup>
	import resourceApi from '@/api/sys/resourceApi.js'

	import { h } from 'vue'
	import { PlusOutlined } from '@ant-design/icons-vue'
	import AddForm from './addForm.vue'
	import EditForm from './editForm.vue'
	import { useMenuStore } from '@/store/menu'
	import { message } from "ant-design-vue";
  import BatchDeleteButton from '@/components/BatchDeleteButton/index.vue'
  import STable from "@/components/STable/index.vue"

	const queryForm = ref({})
	const tableRef = ref()
	const addFormRef = ref()
	const editFormRef = ref()
  const moduleId = ref()
  const module = ref()
	const moduleList = ref([])
	const toolConfig = { refresh: true, height: true, columnSetting: false, striped: false }
	const columns = [
		{
			title: '显示名称',
			dataIndex: 'name',
		},
		{
			title: '类型',
			dataIndex: 'resourceType',
			align: 'center',
			width: 80
		},
		{
			title: '图标',
			dataIndex: 'icon',
			align: 'center',
			width: 80
		},
		{
			title: '路由地址',
			dataIndex: 'path',
			ellipsis: true,
			width: 150
		},
		{
			title: '组件',
			dataIndex: 'component',
			ellipsis: true,
			width: 150
		},
		{
			title: '权限',
			dataIndex: 'permission',
			ellipsis: true,
			width: 150
		},
		{
			title: '是否可见',
			dataIndex: 'visible',
			align: 'center',
			width: 100
		},
		{
			title: '排序',
			dataIndex: 'sortNum',
			sorter: true,
			align: 'center',
			width: 80
		},
		{
			title: '操作',
			dataIndex: 'action',
			align: 'center',
			width: 150
		}
	]
	let selectedRowKeys = ref([])
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
	const loadData = async (parameter) => {
		if (!moduleId.value) {
			// 若无moduleId, 则查询module列表第一个module的code作为默认moduleId
			const res = await resourceApi.moduleList()
      moduleList.value = res.data
      module.value = res.data.length > 0 ? res.data[0] : null
      moduleId.value = module.value.code
      queryForm.value.module = moduleId.value
      const treeRes = await resourceApi.menuTree(Object.assign(parameter, queryForm.value))
      return treeRes.data ? treeRes.data : []
		} else {
			// menuTree获取到的data中的id和parentId均为code
      const treeRes = await resourceApi.menuTree(Object.assign(parameter, queryForm.value))
      return treeRes.data ? treeRes.data : []
		}
	}
	// 切换应用标签查询菜单列表
	const moduleClick = (value) => {
    module.value = value
		queryForm.value.module = module.value.code
		tableRef.value.refresh(true)
	}
	// 单个删除
	const deleteMenu = (node) => {
		let data = { codes: [node.code] }
		resourceApi.deleteMenuTree(data).then((res) => {
			message.success(res.message)
			tableRef.value.refresh(true)
			refreshCacheMenu()
		})
	}
	// 批量删除
	const batchDeleteMenu = (params) => {
		let data = { codes: selectedRowKeys.value }
		resourceApi.deleteMenuTree(data).then((res) => {
			message.success(res.message)
			tableRef.value.clearRefreshSelected()
			refreshCacheMenu()
		})
	}
	// 成功回调
	const handleSuccess = () => {
		tableRef.value.refresh(true)
		refreshCacheMenu()
	}
	// 刷新缓存的菜单
	const refreshCacheMenu = () => {
		const menuStore = useMenuStore()
		menuStore.refreshModuleMenu()
	}
</script>

<style scoped>
.ant-form-item {
	margin-bottom: 0 !important;
}
</style>
