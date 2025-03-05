<template>
	<a-drawer
		:open="visible"
		title="新增岗位"
		:width="drawerWidth"
		:closable="false"
		:footerStyle="{display: 'flex', justifyContent: 'flex-end'}"
		:destroy-on-close="true"
		@close="onClose"
	>
		<template #extra>
			<a-button type="primary" size="small" @click="onClose"><CloseOutlined /></a-button>
		</template>
		<a-form ref="formRef" :model="formData">
			<a-card title="基本信息">
				<a-row :gutter="24">
					<a-col :span="12">
						<a-form-item label="岗位名称：" name="name" :rules="[required('请输入名称')]">
							<a-input v-model:value="formData.name" placeholder="请输入名称" allow-clear />
						</a-form-item>
					</a-col>
          <!-- 使用状态 -->
          <a-col :span="12">
            <a-form-item label="使用状态:" name="status" :rules="[required('请选择使用状态')]">
              <a-radio-group v-model:value="formData.status" option-type="button" button-style="solid" :options="statusOptions" />
            </a-form-item>
          </a-col>
					<a-col :span="12">
						<a-form-item label="直属组织：" name="orgCode" :rules="[required('请选择直属组织')]">
              <OrgTreeSelect :tree-data="treeData" :defaultValue="formData.orgCode" @onChange="parentChange"/>
						</a-form-item>
					</a-col>
          <a-col :span="12">
            <a-form-item label="数据范围：" name="dataScope" :rules="[required('请选择数据范围')]">
              <a-radio-group v-model:value="formData.dataScope" button-style="solid">
                <!-- 数据范围(字典 1本人 2本机构 3本机构及以下 4自定义) -->
                <a-radio-button :value="1">仅本人</a-radio-button>
                <a-radio-button :value="2">仅本机构</a-radio-button>
                <a-radio-button :value="3">本机构及以下</a-radio-button>
                <a-radio-button :value="4">自定义</a-radio-button>
              </a-radio-group>
            </a-form-item>
          </a-col>
					<a-col :span="12">
						<a-form-item label="排序:" name="sortNum" :rules="[required('请填写排序值')]">
							<a-input-number class="wd" v-model:value="formData.sortNum" :max="100" />
						</a-form-item>
					</a-col>
          <a-col :span="12" v-if="formData.dataScope === 4">
            <a-form-item label="自定义范围：" name="scopeList" tooltip="自定义数据范围时必填">
              <OrgTreeSelect :tree-data="treeData" multiSelect @onChange="scopeChange"/>
            </a-form-item>
          </a-col>
				</a-row>
			</a-card>
		</a-form>
		<template #footer>
			<a-space>
				<a-button @click="onClose">关闭</a-button>
				<a-button type="primary" :loading="submitLoading" @click="onSubmit">保存</a-button>
			</a-space>
		</template>
	</a-drawer>
</template>

<script setup>
	import groupApi from '@/api/sys/groupApi'

	import { required } from '@/utils/formRules'
	import { useSettingsStore } from "@/store";
	import { message } from "ant-design-vue";
  import OrgTreeSelect from "@/views/sys/components/orgTreeSelect.vue";

	const settingsStore = useSettingsStore()

	// 默认是关闭状态
	const visible = ref(false)
	const emit = defineEmits({ successful: null })
	const formRef = ref()
	const treeData = ref([])
	// 表单数据，这里有默认值
	const formData = ref({
		dataScope: 2,
		sortNum: 99,
		visible: 1,
		status: 0
	})
  const scopeList = ref([])
	const submitLoading = ref(false)
	// 使用状态options（0正常 1停用）
	const statusOptions = [
		{ label: "正常", value: 0 },
		{ label: "已停用", value: 1 }
	]
	// 抽屉宽度
	const drawerWidth = computed(() => {
		return settingsStore.menuCollapsed ? `calc(100% - 80px)` : `calc(100% - 210px)`
	})

	// 打开抽屉
	const onOpen = (orgCode, tree) => {
		visible.value = true
    formData.value.orgCode = orgCode
    // 组织树赋值并展开顶级节点
    treeData.value = tree
	}
	// 关闭抽屉
	const onClose = () => {
		formRef.value.resetFields()
		visible.value = false
	}
	// 选择上级加载模块的选择框
	const parentChange = (value) => {
		formData.value.orgCode = value
	}
  // 自定义数据范围变更
  const scopeChange = (value) => {
    scopeList.value = value
  }
	// 验证并提交数据
	const onSubmit = () => {
		formRef.value.validate().then(() => {
			submitLoading.value = true
      if (scopeList.value) {
        formData.value.scopeSet = scopeList.value.join(',');
      }
			groupApi.addGroup(formData.value).then((res) => {
				message.success(res.message)
				emit('successful')
				onClose()
			}).finally(() => {
				submitLoading.value = false
			})
		}).catch(() => {
		})
	}
	// 调用这个函数将子组件的一些数据和方法暴露出去
	defineExpose({
		onOpen
	})
</script>
