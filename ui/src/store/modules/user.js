import storage from 'store'
import {
  login,
  getInfo,
  logout
} from '@/api/login'
import moment from 'moment'
import {
  socket
} from '@/utils/websocket'
import {
  listNotice
} from '@/api/system/notice'
import {
  ACCESS_TOKEN,
  ACCESS_WEBSOCKETKEY
} from '@/store/mutation-types'

const user = {
  state: {
    token: '',
    name: '',
    welcome: '',
    avatar: '',
    roles: [],
    user: {},
    titCount: 0,
    info: {}
  },

  mutations: {
    SET_USER: (state, user) => {
      state.user = user
    },
    SET_TITCOUNT: (state, titCount) => {
      state.titCount = titCount
    },



    SET_TOKEN: (state, token) => {
      state.token = token
    },
    SET_NAME: (state, name) => {
      state.name = name
    },
    SET_AVATAR: (state, avatar) => {
      state.avatar = avatar
    },
    SET_ROLES: (state, roles) => {
      state.roles = roles
    },
    SET_INFO: (state, info) => {
      state.info = info
    },
    SET_PERMISSIONS: (state, permissions) => {
      state.permissions = permissions
    }
  },

  actions: {
    // 登录
    Login({
      commit
    }, userInfo) {
      return new Promise((resolve, reject) => {
        login(userInfo).then(res => {

            if (res.loginNew) {
              resolve(res)
            } else {
              storage.set(ACCESS_TOKEN, res.token, 7 * 24 * 60 * 60 * 1000)
              storage.set(ACCESS_WEBSOCKETKEY, res.webSocketKey)
              commit('SET_TOKEN', res.token)
              resolve()
            }
          })
          .catch(error => {
            reject(error)
          })
      })
    },

    // 获取用户信息
    GetInfo({
      commit,
      state
    }) {
      return new Promise((resolve, reject) => {
        getInfo(state.token).then(res => {

            const user = res.user
            const avatar = user.avatar === '' ? require('@/assets/images/profile.jpeg') : process.env.VUE_APP_BASE_API + user.avatar
            if (res.roles && res.roles.length > 0) { // 验证返回的roles是否是一个非空数组
              commit('SET_ROLES', res.roles)
              commit('SET_PERMISSIONS', res.permissions)
            } else {
              commit('SET_ROLES', ['ROLE_DEFAULT'])
            }
            commit('SET_NAME', user.nickName)
            commit('SET_USER', user)
            commit('SET_AVATAR', avatar)


            // let canvas = document.createElement('canvas')
            // const spacing = 300 // 水印间的间距
            // canvas.width = window.innerWidth * 2 // Canvas的宽度为视口宽度的两倍
            // canvas.height = window.innerHeight * 2 // Canvas的高度为视口高度的两倍

            // let ctx = canvas.getContext('2d')
            // ctx.font = '16px Arial' // 字体大小
            // ctx.fillStyle = 'rgba(0, 0, 0, 0.2)' // 字体颜色和透明度
            // ctx.rotate(-Math.PI / 8) // 将文字旋转45度

            // // 在Canvas上重复绘制倾斜的文字
            // for (let x = -canvas.width; x < canvas.width; x += spacing) {
            //   for (let y = -canvas.height; y < canvas.height; y += spacing) {
            //     ctx.fillText(user.userName + '  ' + user.loginIp + '  ' + moment().format('YYYY-MM-DD'), x, y)
            //   }
            // }

            // // 创建一个新的div元素用于显示水印
            // const watermarkDiv = document.createElement('div')
            // watermarkDiv.style.position = 'fixed'
            // watermarkDiv.style.top = '0'
            // watermarkDiv.style.left = '0'
            // watermarkDiv.style.width = '100%'
            // watermarkDiv.style.height = '100%'
            // watermarkDiv.style.backgroundImage = `url(${canvas.toDataURL()})`
            // watermarkDiv.style.backgroundRepeat = 'repeat'
            // watermarkDiv.style.pointerEvents = 'none'
            // watermarkDiv.style.zIndex = '9999' // 设置高z-index值
            // document.body.appendChild(watermarkDiv)


            resolve(res)
          })
          .catch(error => {
            reject(error)
          })
      })
    },

    // 登出
    Logout({
      commit,
      state
    }) {
      return new Promise((resolve, reject) => {
        logout(state.token).then(() => {
          resolve()
        }).catch(error => {
          reject(error)
        }).finally(() => {
          commit('SET_TOKEN', '')
          commit('SET_ROLES', [])
          commit('SET_PERMISSIONS', [])
          commit('SET_USER', {})
          storage.remove(ACCESS_TOKEN)
          storage.remove(ACCESS_WEBSOCKETKEY)
        })
      })
    }

  }
}

export default user