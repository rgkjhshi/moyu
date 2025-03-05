<template>
	<a-drawer
		:open="visible"
		title="编辑菜单"
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
						<a-form-item label="显示名称" name="name" :rules="[required('请输入菜单名称')]">
							<a-input v-model:value="formData.name" placeholder="请输入显示名称" allow-clear />
						</a-form-item>
					</a-col>
					<a-col :span="12">
						<a-form-item label="唯一编码" name="code" tooltip="不可更改！不填将会自动生成">
							<a-input v-model:value="formData.code" placeholder="唯一编码，不填将自动生成，创建后不可更改" disabled />
						</a-form-item>
					</a-col>
					<a-col :span="12">
						<a-form-item label="上级菜单" name="parentCode" :rules="[required('请选择上级菜单')]">
              <OrgTreeSelect :tree-data="treeData" :defaultValue="formData.parentCode" @onChange="parentChange"/>
						</a-form-item>
					</a-col>
					<a-col :span="12">
						<a-form-item label="菜单类型" name="resourceType" :rules="[required('请选择菜单类型')]">
							<a-radio-group v-model:value="formData.resourceType" button-style="solid">
								<!-- 1模块 2目录 3菜单 4按钮 5外链 -->
								<a-radio-button :value="2">目录</a-radio-button>
								<a-radio-button :value="3">菜单</a-radio-button>
								<a-radio-button :value="5">外链</a-radio-button>
								<a-radio-button :value="4">按钮</a-radio-button>
							</a-radio-group>
						</a-form-item>
					</a-col>
					<a-col :span="12">
						<a-form-item label="排序" name="sortNum" :rules="[required('请填写排序顺序')]">
							<a-input-number v-model:value="formData.sortNum" :max="100" style="width: 100%"/>
						</a-form-item>
					</a-col>
				</a-row>
			</a-card>
			<a-card title="资源信息">
				<!-- 路由、组件、权限、图标、可见、排序 -->
				<a-row :gutter="24">
					<!-- 目录、菜单:路由地址 -->
					<a-col :span="12" v-if="formData.resourceType === 2 || formData.resourceType === 3">
						<a-form-item label="路由地址" name="path" tooltip="菜单路由必须以反斜杠'/'开头" :rules="[required('请输入路由地址')]">
							<a-input v-model:value="formData.path" placeholder="请输入路由地址" allow-clear />
						</a-form-item>
					</a-col>
					<!-- 外链:链接地址 -->
					<a-col :span="12" v-else-if="formData.resourceType === 5">
						<a-form-item label="链接地址" name="path" tooltip="链接必须以http(s)开头" :rules="[required('请输入链接地址')]">
							<a-input v-model:value="formData.path" placeholder="请输入链接地址" allow-clear />
						</a-form-item>
					</a-col>
          <!-- 目录:组件地址 -->
          <a-col :span="12" v-if="formData.resourceType === 2">
            <a-form-item label="目录组件" name="component" tooltip="最外层目录选Layout">
              <a-select v-model:value="formData.component" allowClear>
                <a-select-option value="Layout">Layout</a-select-option>
                <a-select-option value="Empty">Empty</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
					<!-- 菜单:组件地址 -->
					<a-col :span="12" v-if="formData.resourceType === 3">
						<a-form-item label="组件地址" name="component" tooltip="前端组件(不带.vue)" :rules="[required('请输入组件地址')]">
							<a-input v-model:value="formData.component" addon-before="src/views/" placeholder="请输入组件地址" allow-clear/>
						</a-form-item>
					</a-col>
				</a-row>
        <!-- 按钮:接口地址、权限标识 -->
        <a-row :gutter="24">
          <!-- 按钮:接口地址 -->
          <a-col :span="12" v-if="formData.resourceType === 4">
            <a-form-item label="接口地址" name="path" tooltip="非必填，以反斜杠'/'开头">
              <a-input v-model:value="formData.path" placeholder="请输入接口地址" allow-clear />
            </a-form-item>
          </a-col>
          <!-- 按钮:权限标识 -->
          <a-col :span="12" v-if="formData.resourceType === 4">
            <a-form-item label="权限标识" name="permission" tooltip="权限标识应与后端接口保持一致且用':'分割，如'sys:user:add'" :rules="[required('请输入权限标识')]">
              <a-input v-model:value="formData.permission" placeholder="请输入权限标识" allow-clear/>
            </a-form-item>
          </a-col>
        </a-row>
				<a-row :gutter="24">
					<!-- 目录、菜单、外链:是否可见 -->
					<a-col :span="12" v-if="formData.resourceType === 2 || formData.resourceType === 3 || formData.resourceType === 5">
						<a-form-item label="是否可见" name="visible" :rules="[required('请选择是否可见')]">
							<a-radio-group v-model:value="formData.visible" option-type="button" button-style="solid" :options="visibleOptions"/>
						</a-form-item>
					</a-col>
					<!-- 目录、菜单、外链:图标 -->
					<a-col :span="12" v-if="formData.resourceType === 2 || formData.resourceType === 3 || formData.resourceType === 5">
						<a-form-item label="图标" name="icon">
							<a-input v-model:value="formData.icon" class="wdcalc-70" placeholder="请选择图标" allow-clear disabled />
							<a-button type="primary" @click="iconSelector.showIconModal(formData.icon)">选择</a-button>
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
		<Icon-selector ref="iconSelector" @iconCallBack="iconCallBack" />
	</a-drawer>
</template>

<script setup>
	import { required } from '@/utils/formRules'
	import resourceApi from '@/api/sys/resourceApi.js'
	import IconSelector from '@/components/Selector/iconSelector.vue'
	import { useSettingsStore } from "@/store";
	import { message } from "ant-design-vue";
  import OrgTreeSelect from "@/views/sys/components/orgTreeSelect.vue";

	const settingsStore = useSettingsStore()

	// 默认是关闭状态
	const visible = ref(false)
	const emit = defineEmits({ successful: null })
	const formRef = ref()
	const treeData = ref([])
	const iconSelector = ref()
	// 表单数据，这里有默认值
	const formData = ref({})
	const submitLoading = ref(false)

	const drawerWidth = computed(() => {
		return settingsStore.menuCollapsed ? `calc(100% - 80px)` : `calc(100% - 210px)`
	})

	// 打开抽屉
	const onOpen = async (node, module) => {
		// 获取菜单信息
    const res = await resourceApi.resourceDetail({ code: node.code })
    formData.value = res.data
		// 获取菜单树并加入顶级节点
    const moduleRes = await resourceApi.menuTreeSelector({ module: module.code })
    treeData.value = [{
      code: module.code,
      name: module.name,
      children: moduleRes.data
    }]
    // 数据就绪之后显示
    visible.value = true
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
	// 是否可见options
	const visibleOptions = [
		{ label: "可见", value: 1 },
		{ label: "不可见", value: 0 }
	]
	// 验证并提交数据
	const onSubmit = () => {
		formRef.value.validate().then(() => {
			const param = buildParam(formData.value)
			submitLoading.value = true
			resourceApi.editResource(param).then((res) => {
				message.success(res.message)
				emit('successful')
				onClose()
			}).finally(() => {
				submitLoading.value = false
			})
		}).catch(() => {
		})
	}
	const buildParam = (data) => {
		// 如果用户输入的组件带反斜线，则去掉
		if (data.component && data.component.slice(0, 1) === '/') {
			data.component = data.component.slice(1)
		}
		return data
	}
	// 调用这个函数将子组件的一些数据和方法暴露出去
	defineExpose({
		onOpen
	})
</script>
