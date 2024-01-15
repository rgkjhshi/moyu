import { asyncRoutes, constantRoutes } from '@/router'
import { getRouters } from '@/api/menu'
import Layout from '@/layout/index'

/**
 * Use meta.role to determine if the current user has permission
 * @param roles
 * @param route
 */
function hasPermission(roles, route) {
  if (route.meta && route.meta.roles) {
    return roles.some(role => route.meta.roles.includes(role))
  } else {
    return true
  }
}

/**
 * 加载指定的vue组件
 */
export const loadComponent = (view) => {
  if (process.env.NODE_ENV === 'development') {
    return (resolve) => require([`@/views/moyu/${view}`], resolve)
  } else {
    // 使用 import 实现生产环境的路由懒加载
    return (resolve) => require([`@/views/moyu/${view}`], resolve)
    // return () => import(`@/views/moyu/${view}`)
  }
}

/**
 * Filter asynchronous routing tables by recursion
 * @param routes asyncRoutes
 * @param roles
 */
export function filterAsyncRoutes(routes, roles) {
  const res = []

  routes.forEach(route => {
    const tmp = { ...route }
    if (hasPermission(roles, tmp)) {
      if (tmp.children) {
        tmp.children = filterAsyncRoutes(tmp.children, roles)
      }
      // 后端返回的菜单需要处理为组件
      if (tmp.component === 'Layout') {
        tmp.component = Layout
      } else {
        tmp.component = loadComponent(tmp.component)
      }
      res.push(tmp)
    }
  })

  return res
}

const state = {
  routes: [],
  addRoutes: []
}

const mutations = {
  SET_ROUTES: (state, routes) => {
    state.addRoutes = routes
    state.routes = constantRoutes.concat(routes)
  }
}

const actions = {
  generateRoutes({ commit }, roles) {
    // 原来的前端配置菜单
    // return new Promise(resolve => {
    //   let accessedRoutes
    //   if (roles.includes('admin')) {
    //     accessedRoutes = asyncRoutes || []
    //   } else {
    //     accessedRoutes = filterAsyncRoutes(asyncRoutes, roles)
    //   }
    //   commit('SET_ROUTES', accessedRoutes)
    //   resolve(accessedRoutes)
    // })
    // 后端返回菜单
    return new Promise((resolve, reject) => {
      getRouters().then(response => {
        if (response.code !== 0 || !response.data) {
          reject('获取菜单失败!')
        }
        // 后端菜单(getRouters)+前端菜单(router/index)
        let accessedRoutes = response.data.concat(asyncRoutes)
        // 权限过滤
        accessedRoutes = filterAsyncRoutes(accessedRoutes, roles)
        commit('SET_ROUTES', accessedRoutes)
        resolve(accessedRoutes)
      }).catch(error => {
        reject(error)
      })
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
