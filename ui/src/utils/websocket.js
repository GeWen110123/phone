import storage from 'store'

import store from '@/store'
import router from '../router/index'
import {
  ACCESS_WEBSOCKETKEY
} from '@/store/mutation-types'
import notification from 'ant-design-vue/es/notification'


export function socket() {
  const env = process.env.NODE_ENV
  const wsUrl =
    env === 'development' ?
    'ws://192.168.150.147:8085/websocket/' + storage.get(ACCESS_WEBSOCKETKEY):
    `ws://${window.location.host}/websocket/${storage.get(ACCESS_WEBSOCKETKEY)}`
  //1.创建websocket客户端
  var timeConnect = 0;
  if (storage.get(ACCESS_WEBSOCKETKEY)) {
    webSocketInit(wsUrl);
  }
  let isReloginShow
  //socket初始化
  function webSocketInit(service) {
    var ws = new WebSocket(service);
    ws.onopen = function () {
      console.log("已连接ws");
    };
    ws.onmessage = function (e) {
      if (e.data == 'conn_error') {
        notification.open({
          message: '系统提示',
          description: '登录状态已过期，请重新登录',
          btn: h => {
            return h(
              'a-button', {
                props: {
                  type: 'primary',
                  size: 'small'
                },
                on: {
                  click: () => {
                    store.dispatch('Logout').then(() => {
                      isReloginShow = false
                      location.href = '/index'
                    })
                  }
                }
              },
              '确认'
            )
          },
          duration: 0,
          onClose: () => {
            isReloginShow = false
          }
        })

      } else {
        var result = eval('(' + e.data + ')')
        var msg = result
        console.log(msg);
        if (msg.type == '问题反馈') {
          popTitMessage('问题反馈通知', msg.msg, '立即查看', 1, msg.id)
        } else if (msg.type == '任务调度') {
          popTitMessage('任务调度新反馈提醒', msg.msg, '查看详情', 2, msg.id)
        } else if (msg.type == '专项调度') {
          popTitMessage('专项调度新反馈提醒', msg.msg, '查看详情', 3, msg.id)
        } else if (msg.type == '技术成果') {
          popTitMessage('技术成果更新提醒', msg.msg, '查看详情', 4, msg.id)
        } else if (msg.type == '线索流转') {
          popTitMessage('线索流转更新提醒', msg.msg, '查看详情', 5, msg)
        } else if (msg.type == '专刊报送') {
          popTitMessage('专刊报送更新提醒', msg.msg, '查看详情', 6, msg.id)
        } else if (msg.type == '文章报送') {
          popTitMessage('文章报送更新提醒', msg.msg, '查看详情', 7, msg.id)
        }
      }
    };

    ws.onclose = function () {
      console.log('服务器已经断开');
      reconnect(service);
    };
  }

  // 重连
  function reconnect(service) {
    // lockReconnect加锁，防止onclose、onerror两次重连
    timeConnect++;
    console.log("第" + timeConnect + "次重连");
    // 进行重连
    setTimeout(function () {
      webSocketInit(service);
    }, 1000);

  }

  function popTitMessage(title, titleContent, btnSize, type, id) {
    const key = `open${Date.now()}`;

    const changeMethods = {
      1: change1,
      2: change2,
      3: change3,
      4: change4,
      5: change5,
      6: change6,
      7: change7,
    };
    isReloginShow = true
    notification.open({
      message: title,
      description: titleContent,
      btn: (h) => {
        if (!id) {
          // 如果没有 id，不显示按钮
          return null;
        }
        return h(
          'a-button', {
            props: {
              type: 'primary',
              size: 'small',
            },
            on: {
              click: () => {
                // 通过 changeMethods[type] 动态获取对应的方法
                const changeMethod = changeMethods[type];

                // 只有在 changeMethod 存在时才调用
                if (changeMethod) {
                  changeMethod(id); // 调用对应的函数
                } else {
                  console.error(`No method found for type: ${type}`); // 可以记录日志
                }
                notification.close(key)
              },
            },
          },
          btnSize
        )
      },
      duration: 0,
      key,
      onClose: () => {
        isReloginShow = false
      },
    })
  }


  function change1(id) {
    router.replace({
      name: 'Feedback',
      query: {
        randomParam: Math.random()

      },
    })
  }



  function change3(id) {
    router.replace({
      path: '/special/detail',
      query: {
        id: id,
        randomParam: Math.random()

      },
    })
  }

  function change2(id) {
    router.replace({
      path: '/task/details',
      query: {
        id: id,
        randomParam: Math.random()

      },
    })
  }

  function change4(id) {
    router.replace({
      path: '/results/detail',
      query: {
        id: id,
        randomParam: Math.random()

      },
    })
  }

  function change5(item) {
    let {
      id,
      assId
    } = item;
    // 动态构建 query 参数对象
    const queryParams = {
      id
    };
    // 只在 traId 和 assId 有值时才添加到查询参数中
    if (assId) {
      queryParams.assId = assId;
    }
    queryParams.randomParam = Math.random()
    router.replace({
      path: '/clue/details',
      query: queryParams,

    })
  }

  function change6(id) {




    router.replace({
      path: '/wzSpecial/detail',
      query: {
        id: id,
        randomParam: Math.random()
      },
    })
  }

  function change7(id) {
    router.replace({
      path: '/Article/detail',
      query: {
        id: id,
        randomParam: Math.random()
      },
    })
  }




  // function change4(item, userId) {
  //   if (store.getters.user.roles[0].roleSort == 3) {
  //     router.push({
  //       path: '/noticeUser',
  //       query: {
  //         noticeId: item.noticeId,
  //         id: userId,
  //       }
  //     })
  //   } else {
  //     router.push({
  //       path: '/system/notice',
  //       query: {
  //         noticeId: item.noticeId,
  //         id: userId,
  //       }
  //     })
  //   }
  // }

  if (storage.get(ACCESS_WEBSOCKETKEY)) {
    // 心跳 * 回应
    // setInterval(function () {
    //   var websocket = new WebSocket(wsUrl);
    //   websocket.send('');

    // }, 1000 * 100)

  }

}