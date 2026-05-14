// eslint-disable-next-line
import {
  UserLayout
} from '@/layouts'

import Layout from '@/layouts'
/**
 * Note: 路由配置项
 *
 * hidden: true                     // 当设置 true 的时候该路由不会再侧边栏出现 如401，login等页面，或者如一些编辑页面/edit/1
 * alwaysShow: true                 // 当你一个路由下面的 children 声明的路由大于1个时，自动会变成嵌套的模式
 *                                  // 只有一个时，会将那个子路由当做根路由显示在侧边栏
 *                                  // 若你想不管路由下面的 children 声明的个数都显示你的根路由
 *                                  // 你可以设置 alwaysShow: true，这样它就会忽略之前定义的规则，一直显示根路由
 * redirect: noRedirect             // 当设置 noRedirect 的时候该路由在面包屑导航中不可被点击
 * name:'router-name'               // 设定路由的名字，一定要填写不然使用<keep-alive>时会出现各种问题,此名称与页面name一致
 * meta : {
    noCache: true                   // 如果设置为true，则不会被 <keep-alive> 缓存(默认 false)
    title: 'title'                  // 设置该路由在侧边栏和面包屑中展示的名字
    icon: 'svg-name'                // 设置该路由的图标
  }
 */
export const indexRouterMap = [

  {
    path: '/index',
    name: 'Index',
    component: 'Home',
    meta: {
      title: '首页',
      noCache: false, // 不再强制刷新
      icon: 'dashboard',
      hiddenHeaderContent: true
    }
  },




  {
    path: '/device',
    name: 'Device',
    component: "Layout",
    hidden: false,
    alwaysShow: true,
    redirect: "noRedirect",
    meta: {
      title: '设备管理',
      noCache: false, // 不再强制刷新
      icon: 'retweet',
    },
    children: [{
      path: 'Facility', // 子路由路径
      name: 'Facility',
      component: 'Facility', // 子页面：设备列表
      meta: {
        title: '设备列表',
        noCache: false, // 不再强制刷新
        icon: 'retweet',
      }
    },
    {
      path: 'device-group', // 子路由路径
      name: 'DeviceGroup',
      component: 'DeviceGroup', // 子页面：设备分组
      meta: {
        title: '设备分组',
        noCache: false, // 不再强制刷新
        icon: 'cluster',
      }
    }
    ]
  },



  {
    path: '/strategy',
    name: 'Strategy',
    component: "Layout",
    hidden: false,
    alwaysShow: true,
    redirect: "noRedirect",
    meta: {
      title: '策略管理',
      noCache: false, // 不再强制刷新
      icon: 'unordered-list',
    },
    children: [{
      path: 'allocation', // 子路由路径
      name: 'allocation',
      component: 'Allocation', // 子页面：设备列表
      meta: {
        title: '策略分配',
        noCache: false, // 不再强制刷新
        icon: 'control',
      }
    },
    {
      path: 'task', // 子路由路径
      name: 'Task',
      component: 'Task', // 子页面：设备分组
      meta: {
        title: '任务策略',
        noCache: false, // 不再强制刷新
        icon: 'database',
      }
    }
    ]
  },

  {
    path: '/video',
    name: 'video',
    component: 'Video',
    meta: {
      title: '抖音视频',
      noCache: true,
      icon: 'video-camera',
    }
  },


  {
    path: '/video/detailPage',
    name: 'detailPage',
    component: 'DetailPage',
    meta: {
      title: '抖音详情',
      noCache: true
    },
    hidden: true
  },

  // {
  //   path: '/user-search',
  //   name: 'UserSearch',
  //   component: 'UserSearch',
  //   meta: {
  //     title: '用户搜索',
  //     noCache: true,
  //     icon: 'search',
  //   }
  // },


  {
    path: '/system',
    name: 'system',
    component: 'SystemUser',
    meta: {
      title: '用户管理',
      noCache: true,
      icon: 'user',
      role: ['admin'],
    }
  },

  // {
  //   path: '/clue/add',
  //   name: 'clueAdd',
  //   component: 'clueAdd',
  //   hidden: true,
  //   meta: {
  //     title: '线索创建',
  //     noCache: true,
  //   }
  // },




  {
    path: '/account/center',
    name: 'Center',
    component: 'AccountCenter',
    meta: {
      title: '个人中心',
      noCache: true
    },
    hidden: true
  },



  {
    path: '/project/information',
    name: 'Center',
    component: 'Information',
    meta: {
      title: '手机监测平台',
      noCache: true
    },
    hidden: true
  },

  {
    path: '/log',
    name: 'log',
    alwaysShow: true,
    component: 'Layout',
    meta: {
      title: '日志管理',
      noCache: false,
      hidden: false, // 父路由不隐藏
      icon: 'solution',
      hiddenHeaderContent: true
    },
    children: [{
      path: 'login-log', // 子路由路径
      name: 'loginLog',
      component: 'loginLog', // 子页面：设备列表
      meta: {
        title: '登录日志',
        noCache: false,
        icon: 'login',
      }
    },
    {
      path: 'device-log', // 子路由路径
      name: 'deviceLog',
      component: 'deviceLog', // 子页面：设备分组
      meta: {
        title: '系统日志',
        noCache: false,
        icon: 'menu-unfold',
      }
    }
    ]
  },

  {
    path: '/account/settings',
    name: 'Settings',
    component: 'AccountSettings',
    meta: {
      title: '个人设置',
      noCache: true
    },
    hidden: true
  },
  {
    path: '/monitor/job/log',
    name: 'JobLog',
    component: 'JobLog',
    meta: {
      title: '调度日志',
      noCache: true
    },
    permissions: ['monitor:job:list'],
    hidden: true
  },
  {
    path: '/system/role/authUser',
    name: 'AuthUser',
    component: 'AuthUser',
    meta: {
      title: '分配用户',
      noCache: true
    },
    permissions: ['system:role:edit'],
    hidden: true
  },
  {
    path: '/system/notice/form',
    name: 'NoticeForm',
    component: 'NoticeForm',
    meta: {
      title: '公告编辑',
      noCache: true
    },
    permissions: ['system:notice:edit'],
    hidden: true
  },
  {
    path: '/gen/edit',
    name: 'GenEdit',
    component: 'GenEdit',
    meta: {
      title: '修改生成配置',
      noCache: true
    },
    permissions: ['tool:gen:edit'],
    hidden: true
  }
]

/**
 * 基础路由
 * @type { *[] }
 */
export const constantRouterMap = [{
  path: '/user',
  component: UserLayout,
  redirect: '/user/login',
  hidden: true,
  children: [{
    path: 'login',
    name: 'login',
    component: () => import( /* webpackChunkName: "user" */ '@/views/user/Login')
  },
  {
    path: 'register',
    name: 'register',
    component: () => import( /* webpackChunkName: "user" */ '@/views/user/Register')
  },
  ]
},




]