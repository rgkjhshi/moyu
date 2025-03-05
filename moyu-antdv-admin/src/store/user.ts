import { defineStore } from 'pinia'
import userCenterApi from '@/api/sys/userCenterApi'
import { UserInfo } from "@/api/sys/types.ts";

export const useUserStore = defineStore('userStore', () => {
	// 定义state
	// 用户信息
	const userInfo = ref<UserInfo>({});

	// 在 Setup Stores 中，您需要创建自己的 $reset() 方法 https://pinia.vuejs.org/zh/core-concepts/state.html
	function $reset() {
		userInfo.value = {}
	}

	// 定义action
	// 初始化用户信息
	const initUserInfo = async () => {
		const res = await userCenterApi.loginUserInfo()
		if (!res.data.roles || res.data.roles.length <= 0) {
			// 这等价于 Promise.reject(new Error("Rejected value"))
			throw new Error("initUserInfo: 用户权限不能为空");
		}
		userInfo.value = res.data
		localStorage.setItem('USER_INFO', JSON.stringify(userInfo.value))
	}
	// 刷新登录用户信息
	const refreshUserLoginUserInfo = () => {
		userCenterApi.loginUserInfo().then((res) => {
			userInfo.value = res.data
			localStorage.setItem('USER_INFO', JSON.stringify(userInfo.value))
		})
	}
	return { userInfo, $reset, initUserInfo, refreshUserLoginUserInfo }
})