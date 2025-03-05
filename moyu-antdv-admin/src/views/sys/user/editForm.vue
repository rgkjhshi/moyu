<template>
	<a-drawer
		:open="visible"
		title="编辑用户"
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
					<a-col :span="8">
						<a-form-item label="登陆账号：" name="account" :rules="[required('请输入账号')]">
							<a-input v-model:value="formData.account" placeholder="请输入账号" disabled />
						</a-form-item>
					</a-col>
					<a-col :span="8">
						<a-form-item label="姓名：" name="name" :rules="[required('请输入姓名')]">
							<a-input v-model:value="formData.name" placeholder="请输入姓名" allow-clear />
						</a-form-item>
					</a-col>
					<a-col :span="8">
						<a-form-item label="员工编号：" name="staffCode">
							<a-input v-model:value="formData.staffCode" placeholder="请输入员工编号" disabled />
						</a-form-item>
					</a-col>
					<a-col :span="8">
						<a-form-item label="直属组织：" name="orgCode" :rules="[required('请选择直属组织')]">
              <OrgTreeSelect :tree-data="treeData" :defaultValue="formData.orgCode" @onChange="parentChange"/>
						</a-form-item>
					</a-col>
					<a-col :span="8">
						<a-form-item label="性别：" name="gender" :rules="[required('请选择性别')]">
							<a-radio-group v-model:value="formData.gender">
								<a-radio :value='0'>未知</a-radio>
								<a-radio :value='1'>男</a-radio>
								<a-radio :value='2'>女</a-radio>
							</a-radio-group>
						</a-form-item>
					</a-col>
					<a-col :span="8">
						<a-form-item label="出生日期：" name="birthday">
							<a-date-picker v-model:value="formData.birthday" value-format="YYYY-MM-DD" />
						</a-form-item>
					</a-col>
					<a-col :span="8">
						<a-form-item label="身份证号：" name="idNo">
							<a-input v-model:value="formData.idNo" placeholder="请输入身份证号" allow-clear show-count :maxlength="18" />
						</a-form-item>
					</a-col>
					<a-col :span="8">
						<a-form-item label="手机号：" name="phone">
							<a-input v-model:value="formData.phone" placeholder="请输入手机" allow-clear show-count :maxlength="11" />
						</a-form-item>
					</a-col>
					<a-col :span="8">
						<a-form-item label="邮箱：" name="email">
							<a-input v-model:value="formData.email" placeholder="请输入邮箱" allow-clear />
						</a-form-item>
					</a-col>
					<a-col :span="8">
						<a-form-item label="昵称：" name="nickName">
							<a-input v-model:value="formData.nickName" placeholder="请输入昵称" allow-clear />
						</a-form-item>
					</a-col>
					<a-col :span="8">
						<a-form-item label="入职日期：" name="entryDate">
							<a-date-picker v-model:value="formData.entryDate" value-format="YYYY-MM-DD" />
						</a-form-item>
					</a-col>
					<!-- 账号状态 -->
					<a-col :span="8">
						<a-form-item label="账号状态:" name="status" :rules="[required('请选择账号状态')]">
							<a-radio-group v-model:value="formData.status" option-type="button" button-style="solid" :options="statusOptions" />
						</a-form-item>
					</a-col>
				</a-row>
			</a-card>
			<a-card title="更多信息">
				<a-row :gutter="16">
					<a-col :span="8">
						<a-form-item label="毕业学校：" name="college">
							<a-input v-model:value="formData.college" placeholder="请输入毕业学校" allow-clear />
						</a-form-item>
					</a-col>
					<a-col :span="8">
						<a-form-item label="学历：" name="education">
							<a-input v-model:value="formData.education" placeholder="请输入学历" allow-clear />
						</a-form-item>
					</a-col>
					<a-col :span="8">
						<a-form-item label="政治面貌：" name="politicalOutlook">
							<a-input v-model:value="formData.politicalOutlook" placeholder="请输入政治面貌" allow-clear />
						</a-form-item>
					</a-col>
				</a-row>
				<a-row :gutter="16">
					<a-col :span="8">
						<a-form-item label="家庭电话：" name="homeTel">
							<a-input v-model:value="formData.homeTel" placeholder="请输入家庭电话" allow-clear />
						</a-form-item>
					</a-col>
					<a-col :span="8">
						<a-form-item label="家庭住址：" name="address">
							<a-textarea
								v-model:value="formData.address"
								placeholder="请输入家庭住址"
								:auto-size="{ minRows: 2, maxRows: 5 }"
								allow-clear
							/>
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
import userApi from '@/api/sys/userApi'

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
	orgCode: '',
	gender: 0,
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
const onOpen = async (record, tree) => {
	visible.value = true
	// 获取用户信息
  const res = await userApi.userDetail({ account: record.account })
  formData.value = res.data
  // 组织树赋值并展开顶级节点
  treeData.value = tree
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
	formData.value.orgCode = value
}

// 验证并提交数据
const onSubmit = () => {
	formRef.value.validate().then(() => {
		submitLoading.value = true
		userApi.editUser(formData.value).then((res) => {
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
