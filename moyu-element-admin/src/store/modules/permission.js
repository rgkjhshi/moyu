import { asyncRoutes, constantRoutes } from '@/router'

/**
 * 用 meta.role 决定当前用户是否有权限
 * @param roles
 * @param route
 */
function hasPermission(roles, route) {
  if (route.meta && route.meta.roles) {
    // roles.some => Array.some 相当于是只要有一个满足就为true
    // 判断用户的权限于当前路由访问所需要的权限是否有一个满足
    return roles.some(role => route.meta.roles.includes(role))
  } else {
    // 默认是可访问的
    return true
  }
}

/**
 * Filter asynchronous routing tables by recursion
 * 递归过滤异步路由表，返回符合用户角色权限的路由表
 * @param routes asyncRoutes
 * @param roles
 */
export function filterAsyncRoutes(routes, roles) {
  const res = []

  routes.forEach(route => {
    const tmp = { ...route }
    // 判断当前路由是否可以访问
    if (hasPermission(roles, tmp)) {
      if (tmp.children) {
        tmp.children = filterAsyncRoutes(tmp.children, roles)
      }
      // 将可访问路由放入数组中
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
    // 添加的路由
    state.addRoutes = routes
    // 将vuex中的路由进行更新
    state.routes = constantRoutes.concat(routes)
  }
}

const actions = {
  // 生成路由
  generateRoutes({ commit }, roles) {
    return new Promise(resolve => {
      let accessedRoutes
      if (roles.includes('admin')) {
        accessedRoutes = asyncRoutes || []
      } else {
        // 利用 filterAsyncRoutes 过滤出可访问的路由
        accessedRoutes = filterAsyncRoutes(asyncRoutes, roles)
      }
      // 保存可访问的路由到store中
      commit('SET_ROUTES', accessedRoutes)
      // 返回可访问路由
      resolve(accessedRoutes)
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
