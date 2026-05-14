/**
 * 项目默认配置项
 * primaryColor - 默认主题色, 如果修改颜色不生效，请清理 localStorage
 * navTheme - sidebar theme ['dark', 'light'] 两种主题
 * colorWeak - 色盲模式
 * layout - 整体布局方式 ['sidemenu', 'topmenu'] 两种布局
 * fixedHeader - 固定 Header : boolean
 * fixSiderbar - 固定左侧菜单栏 ： boolean
 * contentWidth - 内容区布局： 流式 |  固定
 *
 * storageOptions: {} - Vue-ls 插件配置项 (localStorage/sessionStorage)
 *
 */




// export default {
//   navTheme: 'dark', // theme for nav menu
//   primaryColor: '#1890FF', // primary color of ant design
//   layout: 'topmenu', // nav menu position: sidemenu or topmenu
//   contentWidth: 'Fluid', // layout of content: Fluid or Fixed, only works when layout is topmenu
//   fixedHeader: true, // sticky header
//   fixSiderbar: false, // sticky siderbar
//   colorWeak: true,
//   multiTab: false,
//   tableSize: 'default',
//   tableBordered: true,
//   hideFooter: false,
//   title: '思遥一体化办公平台',
//   production: process.env.NODE_ENV === 'production' && process.env.VUE_APP_PREVIEW !== 'true'
// }

export default {
  navTheme: 'light', // theme for nav menu
  primaryColor: '#2F54EB', // primary color of ant design
  layout: 'topmenu', // nav menu position: sidemenu or topmenu
  contentWidth: 'Fixed', // layout of content: Fluid or Fixed, only works when layout is topmenu
  fixedHeader: true, // sticky header
  fixSiderbar: false, // sticky siderbar
  colorWeak: true,
  multiTab: true,
  tableSize: 'default',
  tableBordered: true,
  hideFooter: false,
  title: '手机监测平台',
  production: process.env.NODE_ENV === 'production' && process.env.VUE_APP_PREVIEW !== 'true'
}