import { defineStore } from 'pinia'
import { useSearchStore } from '@/store/search'
import userCenterApi from '@/api/sys/userCenterApi'
import routesData from '@/router/systemRouter'
import { RouteRecordRaw } from "vue-router"
import Layout from '@/layout/index.vue'

// findPwd和login路由组件已静态加载，此处不在进行异步加载
const modules = import.meta.glob([
	'/src/views/**/**.vue',
	'!/src/views/auth/findPwd/**.vue',
	'!/src/views/auth/login/**.vue'
])
// 空布局。多层目录嵌套时需要使用
const Empty = () => import("@/layout/empty.vue")

// 过滤异步路由
const filterAsyncRoutes = (menus) => {
	const asyncRoutes: RouteRecordRaw[] = [];
	// 遍历所有菜单
	menus.forEach((menu) => {
		// menu转路由对象
		const route: RouteRecordRaw = {
			path: menu.path,
			name: menu.path,
			meta: menu.meta ? menu.meta : {},
			redirect: menu.redirect,
			children: menu.children ? filterAsyncRoutes(menu.children) : null,
			component: loadComponent(menu)
		}
		asyncRoutes.push(route);
	});
	return asyncRoutes;
}

// 加载组件
const loadComponent = (menu) => {
	// 菜单类型（字典 1模块 2目录 3菜单 4按钮 5外链）
	let item = Empty;
	const component = menu.component
	if (component?.toString() === "Layout") {
		item = Layout
	} else {
		item = modules[`/src/views/${component}.vue`] || modules[`/src/views/${component}/index.vue`] || Empty
	}
	return item
}

export const useMenuStore = defineStore('menuStore', () => {
	// state
	// 所有模块的menu
	const menuList = ref([])
	// 当前的所有路由(包括静态配置的和动态生成的)
	const routes = ref<RouteRecordRaw[]>([]);

	// 在 Setup Stores 中，您需要创建自己的 $reset() 方法 https://pinia.vuejs.org/zh/core-concepts/state.html
	function $reset() {
		menuList.value = []
		routes.value = []
	}

	// actions
	function setRoutes(newRoutes) {
		routes.value = routesData.concat(newRoutes);
	}

	/**
	 * 初始化模块及菜单(优先本地，无则api获取)
	 */
	const initModuleMenu = async () => {
		// 优先获取本地数据
		const menu = localStorage.getItem('MENU')
		let localMenu = JSON.parse(menu)
		if (!localMenu) {
			// 若本地无数据则api获取，然后保存到本地
			const res = await userCenterApi.loginUserMenu()
			localMenu = res.data
			localStorage.setItem('MENU', JSON.stringify(res.data))
		}
		menuList.value = localMenu
	};

	/**
	 * 从api更新模块及菜单
	 */
	const refreshModuleMenu = async () => {
		const res = await userCenterApi.loginUserMenu()
		localStorage.setItem('MENU', JSON.stringify(res.data))
		menuList.value = res.data
	};

	/**
	 * 生成异步(动态)路由
	 */
	const generateRoutes = async () => {
		// 生成动态路由
		const asyncRoutes = filterAsyncRoutes(menuList.value);
		// 设置routes
		setRoutes(asyncRoutes)
		// 初始化面包屑
		initBreadcrumb(routes.value)
		// 初始化搜索
		const searchStore = useSearchStore()
		searchStore.init(routes.value)
		return asyncRoutes;
	};

	/**
	 * 根据当前的routes生成菜单的面包屑
	 */
	const initBreadcrumb = (routes: RouteRecordRaw[], breadcrumb: RouteRecordRaw[] = []) => {
		routes.forEach((route) => {
			// 生成当前route的breadcrumb
			const temp: RouteRecordRaw[] = [...breadcrumb]
			temp.push(route)
			// 设置在route的meta中
			route.meta.breadcrumb = temp
			// 若有子路由则递归设置子路由
			if (route.children) {
				initBreadcrumb(route.children, temp)
			}
		})
	}

	return {
		menuList,
		routes,
		$reset,
		initModuleMenu,
		refreshModuleMenu,
		generateRoutes,
	}
})
