<template>
  <!-- 上方模块选择 -->
	<a-card size="small">
		<a-form ref="queryFormRef" :model="queryFormData">
			<a-row :gutter="24">
        <a-col :span="6">
          <a-form-item name="module" label="所属模块">
            <a-select v-model:value="moduleId" @change="onModuleChange" placeholder="请选择模块">
              <a-select-option v-for="item in moduleList" :key="item.code" :value="item.code">{{item.name}}</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
				<a-col :span="6">
					<a-form-item name="searchKey" label="名称关键词">
						<a-input v-model:value="queryFormData.searchKey" placeholder="请输入关键词" allowClear />
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
        :scroll="{ x: 'max-content' }"
    >
      <template #operator>
        <a-space>
          <a-button type="primary" :icon="h(PlusOutlined)" @click="addFormRef.onOpen(module)">新增按钮</a-button>
          <BatchDeleteButton icon="DeleteOutlined" :selectedRowKeys="selectedRowKeys" @batchDelete="deleteBatchButton" />
        </a-space>
      </template>
      <template #bodyCell="{ column, record }">
        <template v-if="column.dataIndex === 'resourceType'">
          <a-tag v-if="record.resourceType === 1" color="orange">模块</a-tag>
          <a-tag v-if="record.resourceType === 2" color="cyan">目录</a-tag>
          <a-tag v-if="record.resourceType === 3" color="blue">菜单</a-tag>
          <a-tag v-if="record.resourceType === 4" color="purple">按钮</a-tag>
          <a-tag v-if="record.resourceType === 5" color="green">链接</a-tag>
        </template>
        <template v-if="column.dataIndex === 'path'">
          <a-tag v-if="record.path" :bordered="false">{{ record.path }}</a-tag>
        </template>
        <template v-if="column.dataIndex === 'permission'">
          <a-tag v-if="record.permission" :bordered="false">{{ record.permission }}</a-tag>
        </template>
        <template v-if="column.dataIndex === 'action'">
          <a-space>
            <a-tooltip title="编辑">
              <a @click="editFormRef.onOpen(record, module)"><FormOutlined /></a>
            </a-tooltip>
            <a-divider type="vertical" />
            <a-tooltip title="删除">
              <a-popconfirm title="确定要删除吗？" @confirm="deleteButton(record)">
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
	import AddForm from "./addForm.vue";
	import EditForm from "./editForm.vue";
	import { message } from "ant-design-vue";
  import BatchDeleteButton from "@/components/BatchDeleteButton/index.vue"
  import STable from "@/components/STable/index.vue"

	// resourceType=4表示按钮
	const queryFormData = ref({ resourceType: 4 })
	const addFormRef = ref()
	const editFormRef = ref()
	const queryFormRef = ref()
  const moduleId = ref()
  const module = ref()
  const moduleList = ref([])
	const tableRef = ref()
	const toolConfig = { refresh: true, height: true, columnSetting: false, striped: false }
  const columns = [
    {
      title: '显示名称',
      dataIndex: 'name',
      resizable: true,
      width: 180
    },
    {
      title: '类型',
      dataIndex: 'resourceType',
      align: 'center',
      width: 80
    },
    {
      title: '接口地址',
      dataIndex: 'path',
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
      title: '排序',
      dataIndex: 'sortNum',
      sorter: true,
      align: 'center',
      width: 80
    },
    {
      title: '变更时间',
      dataIndex: 'updateTime',
      align: 'center',
      width: 160
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
      const moduleRes = await resourceApi.moduleList()
      moduleList.value = moduleRes.data
      module.value = moduleRes.data.length > 0 ? moduleRes.data[0] : null
      moduleId.value = module.value.code
      queryFormData.value.module = moduleId.value
      return resourceApi.resourcePage(Object.assign(parameter, queryFormData.value)).then((res) => {
        return res.data
      })
    } else {
      return resourceApi.resourcePage(Object.assign(parameter, queryFormData.value)).then((res) => {
        return res.data
      })
    }
  }

  // 模块选择发生变更
  const onModuleChange = (value) => {
    queryFormData.value.module = value
    module.value = moduleList.value.find((e) => e.code === value)
    tableRef.value.refresh(true)
  }

	// 重置
	const reset = () => {
		queryFormRef.value.resetFields()
		tableRef.value.refresh(true)
	}
	// 删除
	const deleteButton = (record) => {
		let data = { ids: [record.id] }
		resourceApi.deleteResource(data).then((res) => {
			message.success(res.message)
			tableRef.value.refresh(true)
		})
	}
	// 批量删除
	const deleteBatchButton = (params) => {
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
