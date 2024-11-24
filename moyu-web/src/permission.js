import router from './router'
import store from './store'
import {Message} from 'element-ui'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style
import {getToken} from '@/utils/auth' // get token from cookie
import getPageTitle from '@/utils/get-page-title'

NProgress.configure({showSpinner: false}) // NProgress Configuration

// 白名单
const whiteList = ['/login', '/register'] // no redirect whitelist

router.beforeEach(async (to, from, next) => {
  // start progress bar
  NProgress.start()

  // 设置页面title
  document.title = getPageTitle(to.meta.title)

  // 决定用户是否已登陆
  const hasToken = getToken()

  // 如果有 token 也就是已经登录的情况下
  if (hasToken) {
    if (to.path === '/login') {
      // 如果要前往的路径是 '/login' 则重定向到首页
      next({path: '/'})
      NProgress.done()
    } else {
      // 从store中取得用户的 roles, 也就是用户的权限。用户是否已经通过 getInfo 获取了权限角色
      const hasRoles = store.getters.roles && store.getters.roles.length > 0
      if (hasRoles) {
        next()
      } else {
        try {
          /**
           * 获取用户信息，会到src/store/modules/user.js中请求getInfo方法
           * 在这个地方获取信息并进行判断 从获取到的所有信息userInfo 中获取 角色值 或者进行判断
           * 要注意 roles 一定要是一个数组 这涉及到 之后的 .some() 方法
           */
          const {roles} = await store.dispatch('user/getInfo')

          // 基于角色生成可访问路由
          const accessRoutes = await store.dispatch('permission/generateRoutes', roles)

          // 动态添加可访问路由表
          router.addRoutes(accessRoutes)
          // hack方法 确保addRoutes已完成
          next({ ...to, replace: true })
        } catch (error) {
          // 如果出现异常  清空token
          await store.dispatch('user/resetToken')
          Message.error(error || 'Has Error')
          next(`/login?redirect=${to.path}`)
          NProgress.done()
        }
      }
    }
  } else {
    /* 无token */
    if (whiteList.indexOf(to.path) !== -1) {
      // 免登录白名单直接访问
      next()
    } else {
      // 其他无访问权限的页面重定向到登录页
      next(`/login?redirect=${to.path}`)
      NProgress.done()
    }
  }
})

router.afterEach(() => {
  // finish progress bar
  NProgress.done()
})
