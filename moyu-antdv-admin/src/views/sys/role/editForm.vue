<template>
	<a-drawer
		:open="visible"
		title="编辑角色"
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
			<a-form-item label="角色名称" name="name" :rules="[required('请输入角色名称')]">
				<a-input v-model:value="formData.name" placeholder="请输入角色名称" allow-clear show-count :maxlength="20" />
			</a-form-item>
      <a-form-item label="唯一编码" name="code" tooltip="不可更改！不填将会自动生成">
        <a-input v-model:value="formData.code" placeholder="唯一编码，不填将自动生成，创建后不可更改" disabled />
      </a-form-item>
			<!-- 使用状态 -->
			<a-form-item label="使用状态" name="status" :rules="[required('请选择使用状态')]">
				<a-radio-group v-model:value="formData.status" option-type="button" button-style="solid" :options="statusOptions"/>
			</a-form-item>
			<a-form-item label="排序" name="sortNum">
				<a-input-number v-model:value="formData.sortNum" :max="100" />
			</a-form-item>
			<a-form-item label="备注" name="remark" >
				<a-input v-model:value="formData.remark" placeholder="备注信息" show-count :maxlength="100" />
			</a-form-item>
			<a-form-item label="扩展信息" name="extJson">
				<a-textarea v-model:value="formData.extJson" placeholder="Json格式" :rows="4" />
			</a-form-item>
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
	import roleApi from '@/api/sys/roleApi'

	import { required } from '@/utils/formRules'
	import { message } from "ant-design-vue";

	// 定义emit事件
	const emit = defineEmits({ successful: null })
	// 默认是关闭状态
	const visible = ref(false)
	const formRef = ref()
	// 表单数据
	const formData = ref({})
	const submitLoading = ref(false)
	// 使用状态options（0正常 1停用）
	const statusOptions = [
		{ label: "正常", value: 0 },
		{ label: "已停用", value: 1 }
	]
	// 打开抽屉
	const onOpen = async (record) => {
		// 获取模块信息
		const res = await roleApi.roleDetail({ id: record.id })
    formData.value = res.data
    // 数据就绪之后显示
    visible.value = true
	}
	// 关闭抽屉
	const onClose = () => {
		visible.value = false
	}
	// 验证并提交数据
	const onSubmit = () => {
		formRef.value.validate().then(() => {
			submitLoading.value = true
			roleApi.editRole(formData.value).then((res) => {
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
