<template>
	<a-drawer
		:open="visible"
		title="新增模块"
		:width="550"
		:closable="false"
		:footerStyle="{'display': 'flex', 'justify-content': 'flex-end' }"
		:destroy-on-close="true"
		@close="onClose"
	>
		<template #extra>
			<a-button type="primary" size="small" @click="onClose"><CloseOutlined /></a-button>
		</template>
    <a-form ref="formRef" :model="formData" layout="vertical">
      <a-form-item label="显示名称" name="name" :rules="[required('请输入按钮名称')]">
        <a-input v-model:value="formData.name" placeholder="请输入显示名称" allow-clear />
      </a-form-item>
      <a-form-item label="唯一编码" name="code" tooltip="不可更改！不填将会自动生成">
        <a-input v-model:value="formData.code" placeholder="唯一编码，不填将自动生成，创建后不可更改" allow-clear />
      </a-form-item>
      <a-form-item label="上级菜单" name="parentCode" :rules="[required('请选择上级菜单')]">
        <OrgTreeSelect :tree-data="treeData" :defaultValue="moduleId" @onChange="parentChange"/>
      </a-form-item>
      <!-- 按钮:接口地址 -->
      <a-form-item label="接口地址" name="path" tooltip="非必填，以反斜杠'/'开头">
        <a-input v-model:value="formData.path" placeholder="请输入接口地址" allow-clear />
      </a-form-item>
      <!-- 按钮:权限标识 -->
      <a-form-item label="权限标识" name="permission" tooltip="权限标识应与后端接口保持一致且用':'分割，如'sys:user:add'" :rules="[required('请输入权限标识')]">
        <a-input v-model:value="formData.permission" placeholder="请输入权限标识" allow-clear/>
      </a-form-item>
      <a-form-item label="排序" name="sortNum" :rules="[required('请填写排序顺序')]">
        <a-input-number v-model:value="formData.sortNum" :max="100" class="wd"/>
      </a-form-item>
    </a-form>
		<template #footer>
			<a-space>
				<a-button @click="onClose">关闭</a-button>
				<a-button type="primary" @click="onSubmit">保存</a-button>
			</a-space>
		</template>
	</a-drawer>
</template>

<script setup>
	import { required } from '@/utils/formRules'
	import resourceApi from '@/api/sys/resourceApi.js'
  import OrgTreeSelect from "@/views/sys/components/orgTreeSelect.vue";
	import { message } from "ant-design-vue";
	// 默认是关闭状态
	const visible = ref(false)
	const emit = defineEmits({ successful: null })
	const formRef = ref()
  const treeData = ref([])
  const moduleId = ref()
	// 表单数据，这里有默认值
	const formData = ref({
    resourceType: 4,
		sortNum: 9,
		status: 0
	})

  // 打开抽屉
  const onOpen = async (module) => {
    moduleId.value = module.code
    // 获取菜单树并加入顶级节点
    const moduleRes = await resourceApi.menuTreeSelector({ module: module.code })
    treeData.value = [{
      code: module.code,
      name: module?.name,
      children: moduleRes.data
    }]
    // 数据就绪之后显示
    visible.value = true
  }
  // 选择上级加载模块的选择框
  const parentChange = (value) => {
    formData.value.parentCode = value
  }
	// 关闭抽屉
	const onClose = () => {
		formRef.value.resetFields()
		visible.value = false
	}

	// 验证并提交数据
	const onSubmit = () => {
		formRef.value.validate().then(() => {
			resourceApi.addResource(formData.value).then((res) => {
				message.success(res.message)
				emit('successful')
				onClose()
			})
		}).catch(() => {
		})
	}

	// 调用这个函数将子组件的一些数据和方法暴露出去
	defineExpose({
		onOpen
	})
</script>
