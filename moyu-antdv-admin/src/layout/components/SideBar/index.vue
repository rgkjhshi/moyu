<template>
	<!-- 左侧侧边栏 -->
	<a-layout-sider
		:collapsed="menuCollapsed"
		:trigger="null"
		collapsible
		:theme="sideTheme"
		width="210"
	>
		<!-- 左侧菜单上方标题LOGO区域 -->
		<header class="header-logo">
			<div class="header-left">
				<div class="logo-bar">
					<img class="logo" :src="defaultSettings.logo" />
					<span>{{ defaultSettings.title }}</span>
				</div>
			</div>
		</header>
		<!-- sideBarMenu -->
		<a-menu
			v-bind:openKeys="openKeys"
			v-bind:selectedKeys="selectedKeys"
			:theme="sideTheme"
			mode="inline"
			@select="onSelect"
			@openChange="onOpenChange"
		>
			<MenuItem v-for="route in menuList" :key="route.path" :item="route" />
		</a-menu>
	</a-layout-sider>
</template>

<script setup>
import { useMenuStore, useSettingsStore } from '@/store'
import defaultSettings from '@/config/settings';

import { useRoute, useRouter } from "vue-router";
import MenuItem from "@/layout/components/SideBar/MenuItem.vue";

const settingsStore = useSettingsStore()
const menuStore = useMenuStore()
const route = useRoute()
const router = useRouter()

const openKeys = ref([])
const selectedKeys = ref([])

const menuList = computed(() => {
	// 这里使用的是所有路由(静态+动态), 若使用menuStore.menuList.value.children则只有动态菜单
	return menuStore.routes
})

const menuCollapsed = computed(() => {
	return settingsStore.menuCollapsed
})

const sideUniqueOpen = computed(() => {
	return settingsStore.sideUniqueOpen
})

const theme = computed(() => {
	return settingsStore.theme
})
const sideTheme = computed(() => {
	return theme.value
})

onMounted(() => {
	let matched = route.matched
	let pathList = []
	matched.forEach((item) => {
		pathList.push(item.path)
	})
	if (pathList.length) {
		// path中最后一个为菜单，应当选中此菜单，同时移除菜单后的路径列表即为目录，应打开
		selectedKeys.value = pathList.splice(pathList.length - 1, 1)
		openKeys.value = pathList
	}
})

// 当菜单被选中时 { item, key, selectedKeys }
const onSelect = (obj) => {
	// console.log(obj)
	// 数组最后一个元素即最新选中的，也可直接使用obj.key
	const path = obj.keyPath[obj.keyPath.length - 1]
	// 跳转到新path
	router.push({ path })
	// 设置选中
	selectedKeys.value = obj.selectedKeys
}

// 菜单展开/关闭的回调
const onOpenChange = (keys) => {
	if (sideUniqueOpen.value) {
		// 新打开的key，关闭时为 undefined。oldKeys中不存在的为新打开，都存在则为关闭
		const openKey = keys.find(key => openKeys.value.indexOf(key) === -1);
		if (openKey) {
			// console.log('新打开' + openKey)
			// 新打开 openKey 按照目录树获取上级
			openKeys.value = getParentKeys(menuList.value, openKey)
		} else {
			// 关闭
			openKeys.value = keys
		}
	} else {
		openKeys.value = keys
	}
}
// 获取上级keys
const getParentKeys = (menuList, key) => {
	// 递归查找
	return traverse(menuList, key)
}
// 递归查找菜单树的节点，并返回路径
const traverse = (array, key) => {
	// 递归父级key
	for (const element of array) {
		if (element.path === key) {
			return [element.path]
		}
		if (element.children) {
			const sub = traverse(element.children, key)
			if (sub) {
				return [element.path].concat(sub)
			}
		}
	}
}

</script>

<style scoped>

</style>
