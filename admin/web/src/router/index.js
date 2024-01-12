import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/**
 * 注意: 子菜单只会在route的子节点>=1时出现
 * 详见: https://panjiachen.github.io/vue-element-admin-site/zh/guide/essentials/router-and-nav.html
 *
 * hidden: true                   // 当设置 true 的时候该路由不会在侧边栏出现 如401，login等页面。(默认 false)
 * alwaysShow: true               为 true 时，一直显示根路由
 *                                如果不设置alwaysShow, 当路由下面的 children 声明的路由大于1个时，自动会变成嵌套的模式
 *                                只有一个时，会将那个子路由当做根路由显示在侧边栏。
 * redirect: noRedirect           当设置 noRedirect 的时候该路由在面包屑导航中不可被点击
 * name:'router-name'             设定路由的名字，一定要填写，不然使用<keep-alive>时会出现各种问题
 * meta : {
    roles: ['admin','editor']     设置该路由进入的权限，支持多个权限叠加
    title: 'title'                设置该路由在侧边栏和面包屑中展示的名字
    icon: 'svg-name'/'el-icon-x'  设置该路由的图标，支持 svg-class，也支持 el-icon-x element-ui 的 icon
    noCache: true                 如果设置为true，则不会被 <keep-alive> 缓存(默认 false)
    affix: true                   如果设置为true，它则会固定在tags-view中(默认 false)
    breadcrumb: false             如果设置为false，则不会在breadcrumb面包屑中显示(默认 true)
    activeMenu: '/example/list'   当路由设置了该属性，则会高亮相对应的侧边栏。
  }
 */

/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */
export const constantRoutes = [
  {
    path: '/redirect',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '/redirect/:path(.*)',
        component: () => import('@/views/redirect/index')
      }
    ]
  },
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },
  {
    path: '/404',
    component: () => import('@/views/error-page/404'),
    hidden: true
  },
  {
    path: '/',
    component: Layout,
    redirect: '/home',
    children: [
      {
        path: 'home',
        component: () => import('@/views/dodoyd/home/index'),
        name: 'Home',
        meta: { title: '首页', icon: 'international', affix: true }
      }
    ]
  },
  {
    path: '/account',
    component: Layout,
    redirect: '/account/index',
    hidden: true,
    children: [
      {
        path: 'index',
        component: () => import('@/views/dodoyd/account/index'),
        name: 'AccountInfo',
        meta: { title: '账号设置', icon: 'user', noCache: true }
      }
    ]
  }
]

/**
 * asyncRoutes
 * the routes that need to be dynamically loaded based on user roles
 */
export const asyncRoutes = [
  {
    path: '/vip',
    component: Layout,
    redirect: '/vip/venueCardList',
    alwaysShow: true,
    meta: {
      title: '会员管理',
      icon: 'component',
      roles: ['admin', 'user']
    },
    children: [
      {
        path: 'venueCardList',
        component: () => import('@/views/dodoyd/card/venueCardList'),
        name: 'VenueCardList',
        meta: { title: '会员卡', icon: 'list', noCache: true }
      },
      {
        path: 'vipList',
        component: () => import('@/views/dodoyd/vip/vipList'),
        name: 'VipList',
        meta: { title: '会员列表', icon: 'list', noCache: true }
      },
      {
        path: 'addVipCard',
        component: () => import('@/views/dodoyd/vip/addVipCard'),
        name: 'AddVipCard',
        meta: { title: '办理会员', icon: 'user', noCache: true },
        hidden: true
      },
      {
        path: 'withhold',
        component: () => import('@/views/dodoyd/vip/vipCardWithhold'),
        name: 'VipCardWithhold',
        meta: { title: '会员卡代扣', icon: 'user', noCache: true },
        hidden: true
      },
      {
        path: 'expandDate',
        component: () => import('@/views/dodoyd/vip/vipCardExpandDate'),
        name: 'VipCardExpandDate',
        meta: { title: '会员卡延期', icon: 'user', noCache: true },
        hidden: true
      },
      {
        path: 'consumeLog',
        component: () => import('@/views/dodoyd/vip/vipConsumeLog'),
        name: 'VipConsumeLog',
        meta: { title: '消费记录', icon: 'user', noCache: true },
        hidden: true
      }
    ]
  },
  {
    path: '/order',
    component: Layout,
    redirect: '/order/orderList',
    alwaysShow: true,
    meta: {
      title: '订单管理',
      icon: 'component',
      roles: ['admin', 'user']
    },
    children: [
      {
        path: 'orderList',
        component: () => import('@/views/dodoyd/venue/orderList'),
        name: 'OrderList',
        meta: { title: '订单列表', icon: 'list', noCache: true }
      }
    ]
  },
  {
    path: '/system',
    component: Layout,
    redirect: '/user/list',
    alwaysShow: true,
    meta: {
      title: '系统管理',
      icon: 'component',
      roles: ['admin', 'user']
    },
    children: [
      {
        path: 'user',
        component: () => import('@/views/moyu/system/user/index'),
        name: 'User',
        meta: { title: '用户管理', icon: 'list', noCache: true }
      }
    ]
  },
  {
    path: '/tool',
    component: Layout,
    redirect: '/tool/gen',
    alwaysShow: true,
    meta: {
      title: '系统工具',
      icon: 'component',
      roles: ['admin', 'user']
    },
    children: [
      {
        path: 'gen',
        component: () => import('@/views/moyu/tool/index'),
        name: 'GenCode',
        meta: { title: '代码生成', icon: 'list', noCache: true }
      }
    ]
  },
  {
    path: '/sysAdmin',
    component: Layout,
    redirect: '/sysAdmin/index',
    alwaysShow: true,
    meta: {
      title: '系统管理',
      icon: 'lock',
      roles: ['admin']
    },
    children: [
      {
        path: 'addVenue',
        component: () => import('@/views/dodoyd/venue/addVenue'),
        name: 'AddVenue',
        meta: { title: '新增场馆', icon: 'lock', roles: ['admin'], noCache: true }
      },
      {
        path: 'addVenueCard',
        component: () => import('@/views/dodoyd/card/addVenueCard'),
        name: 'AddVenueCard',
        meta: { title: '制卡', icon: 'lock', roles: ['admin'], noCache: true }
      },
      {
        path: 'editVenueCard',
        component: () => import('@/views/dodoyd/card/editVenueCard'),
        name: 'EditVenueCard',
        meta: { title: '修改卡信息', icon: 'lock', roles: ['admin'], noCache: true },
        hidden: true
      },
      {
        path: 'venueList',
        component: () => import('@/views/dodoyd/venue/venueList'),
        name: 'VenueList',
        meta: { title: '场馆列表', icon: 'list', roles: ['admin'], noCache: true }
      },
      {
        path: 'groundList',
        component: () => import('@/views/dodoyd/venue/groundList'),
        name: 'GroundList',
        meta: { title: '场地列表', icon: 'list', roles: ['admin'], noCache: true }
      },
      {
        path: 'addRuleConfig',
        component: () => import('@/views/dodoyd/rule/addRuleConfig'),
        name: 'AddRuleConfig',
        meta: { title: '添加规则配置', icon: 'lock', roles: ['admin'], noCache: true }
      },
      {
        path: 'editRuleConfig',
        component: () => import('@/views/dodoyd/rule/editRuleConfig'),
        name: 'EditRuleConfig',
        meta: { title: '修改配置信息', icon: 'lock', roles: ['admin'], noCache: true },
        hidden: true
      },
      {
        path: 'ruleConfigList',
        component: () => import('@/views/dodoyd/rule/ruleConfigList'),
        name: 'RuleConfigList',
        meta: { title: '配置列表', icon: 'list', roles: ['admin'], noCache: true }
      }
    ]
  },

  // 404 page must be placed at the end !!!
  { path: '*', redirect: '/404', hidden: true }
]

const createRouter = () => new Router({
  mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
