<template>
	<a-drawer
		:open="visible"
		title="新增组织机构"
		:width="drawerWidth"
		:closable="false"
		:footerStyle="{'display': 'flex', 'justify-content': 'flex-end' }"
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
						<a-form-item label="组织名称：" name="name" :rules="[required('请输入名称')]">
							<a-input v-model:value="formData.name" placeholder="请输入显示名称" allow-clear />
						</a-form-item>
					</a-col>
					<a-col :span="12">
						<a-form-item label="组织类型：" name="orgType" :rules="[required('请选择组织类型')]">
							<a-radio-group v-model:value="formData.orgType" button-style="solid">
								<!-- 组织机构类型(字典 1公司组织 2部门机构 3虚拟节点) -->
								<a-radio-button :value="1">公司组织</a-radio-button>
								<a-radio-button :value="2">部门机构</a-radio-button>
								<a-radio-button :value="3">虚拟节点</a-radio-button>
							</a-radio-group>
						</a-form-item>
					</a-col>
					<a-col :span="12">
						<a-form-item label="上级组织：" name="parentCode" :rules="[required('请选择上级组织')]">
              <OrgTreeSelect :tree-data="treeData" :defaultValue="formData.parentCode" @onChange="parentChange"/>
						</a-form-item>
					</a-col>
					<!-- 使用状态 -->
					<a-col :span="12">
						<a-form-item label="使用状态:" name="status" :rules="[required('请选择使用状态')]">
							<a-radio-group v-model:value="formData.status" option-type="button" button-style="solid" :options="statusOptions" />
						</a-form-item>
					</a-col>
					<a-col :span="12">
						<a-form-item label="排序:" name="sortNum" :rules="[required('请填写排序值')]">
							<a-input-number class="wd" v-model:value="formData.sortNum" :max="100" />
						</a-form-item>
					</a-col>
				</a-row>
			</a-card>
			<a-card title="资源信息">
				<a-row :gutter="24">
					<!-- 公司层级 -->
					<a-col :span="12" v-if="formData.orgType === 1">
						<a-form-item label="公司层级：" name="orgLevel" :rules="[required('请选择层级')]">
							<a-radio-group v-model:value="formData.orgLevel" button-style="solid">
								<a-radio-button :value="1">总部</a-radio-button>
								<a-radio-button :value="2">二级公司</a-radio-button>
								<a-radio-button :value="3">三级公司</a-radio-button>
							</a-radio-group>
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
	import orgApi from '@/api/sys/orgApi'

	import { required } from '@/utils/formRules'
	import { message } from "ant-design-vue"
	import { useSettingsStore } from "@/store"
  import OrgTreeSelect from "../components/orgTreeSelect.vue";

	const settingsStore = useSettingsStore()

	// 默认是关闭状态
	const visible = ref(false)
	const emit = defineEmits({ successful: null })
	const formRef = ref()
	const treeData = ref([])
	// 表单数据，这里有默认值
	const formData = ref({
		orgType: 1,
		orgLevel: 2,
		sortNum: 99,
		visible: 1,
		status: 0
	})
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
	const onOpen = (parentCode, tree) => {
		visible.value = true
		formData.value.parentCode = parentCode
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
		formData.value.parentCode = value
	}
	// 图标选择器回调
	const iconCallBack = (value) => {
		if (value) {
			formRef.value.clearValidate('icon')
		}
		formData.value.icon = value
	}

	// 验证并提交数据
	const onSubmit = () => {
		formRef.value.validate().then(() => {
			submitLoading.value = true
			orgApi.addOrg(formData.value).then((res) => {
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
