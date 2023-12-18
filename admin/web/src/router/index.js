import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['admin','editor']    control the page roles (you can set multiple roles)
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'/'el-icon-x' the icon show in the sidebar
    noCache: true                if set true, the page will no be cached(default is false)
    affix: true                  if set true, the tag will affix in the tags-view
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
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
    path: '/venue',
    component: Layout,
    redirect: '/venue/index',
    alwaysShow: true,
    meta: {
      title: '场地管理',
      icon: 'component',
      roles: ['admin', 'user']
    },
    children: [
      {
        path: 'venueInfo',
        component: () => import('@/views/dodoyd/venue/venueInfo'),
        name: 'VenueInfo',
        meta: { title: '场馆信息', icon: 'example', noCache: true }
      },
      {
        path: 'bookGround',
        component: () => import('@/views/dodoyd/venue/bookGround'),
        name: 'BookGround',
        meta: { title: '预订场地', icon: 'guide', noCache: true }
      },
      {
        path: 'groundUseRecord',
        component: () => import('@/views/dodoyd/venue/groundUseRecord'),
        name: 'GroundUseRecord',
        meta: { title: '场地使用记录', icon: 'list', noCache: true }
      },
      {
        path: 'editVenue',
        component: () => import('@/views/dodoyd/venue/editVenue'),
        name: 'EditVenue',
        meta: { title: '修改场馆信息', icon: 'edit', roles: ['admin'], noCache: true },
        hidden: true
      },
      {
        path: 'addGround',
        component: () => import('@/views/dodoyd/venue/addGround'),
        name: 'AddGround',
        meta: { title: '新建场地', icon: 'lock', roles: ['admin'], noCache: true },
        hidden: true
      },
      {
        path: 'editGround',
        component: () => import('@/views/dodoyd/venue/editGround'),
        name: 'EditGround',
        meta: { title: '修改场地信息', icon: 'edit', roles: ['admin'], noCache: true },
        hidden: true
      }
    ]
  },
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
    path: '/coach',
    component: Layout,
    redirect: '/coach/coachList',
    alwaysShow: true,
    meta: {
      title: '课程管理',
      icon: 'component',
      roles: ['admin', 'user']
    },
    children: [
      {
        path: 'coachList',
        component: () => import('@/views/dodoyd/coach/coachList'),
        name: 'CoachList',
        meta: { title: '教练列表', icon: 'list', noCache: true }
      },
      {
        path: 'coachBookList',
        component: () => import('@/views/dodoyd/coach/coachBookList'),
        name: 'CoachBookList',
        meta: { title: '教练课程表', icon: 'table', noCache: true }
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
    path: '/staff',
    component: Layout,
    redirect: '/staff/staffList',
    alwaysShow: true,
    meta: {
      title: '人员管理',
      icon: 'lock',
      roles: ['admin']
    },
    children: [
      {
        path: 'addStaff',
        component: () => import('@/views/dodoyd/staff/addStaff'),
        name: 'AddStaff',
        meta: { title: '添加成员', icon: 'user', roles: ['admin'], noCache: true }
      },
      {
        path: 'staffList',
        component: () => import('@/views/dodoyd/staff/staffList'),
        name: 'StaffList',
        meta: { title: '人员列表', icon: 'list', roles: ['admin'], noCache: true }
      },
      {
        path: 'roleList',
        component: () => import('@/views/dodoyd/my/myRoleList'),
        name: 'MyRoleList',
        meta: { title: '我的权限', icon: 'user', roles: ['admin'], noCache: true }
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
