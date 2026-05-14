import request from '@/utils/request'

/**
 * 获取设备列表
 * 
 * @param {Object} query - 查询参数对象
 * @returns {Promise} 返回请求的Promise对象
 */
export function deviceList(query) {
  return request({
    url: '/module/device/list',
    method: 'get',
    params: query
  })
}


export function getAccount(query) {
  return request({
    url: '/module/video/getAccount',
    method: 'get',
    params: query
  })
}



export function videoList(query) {
  return request({
    url: '/module/video/list',
    method: 'get',
    params: query
  })
}
export function videoTask(query) {
  return request({
    url: '/module/video_task/list',
    method: 'get',
    params: query
  })
}

export function addTags(data) {
  return request({
    url: '/module/video_task',
    method: 'post',
    data: data
  })
}


export function VideoTags(query) {
  return request({
    url: '/module/VideoTags/list',
    method: 'get',
    params: query
  })
}


export function updateVideoTags(data) {
  return request({
    url: '/module/VideoTags',
    method: 'put',
    data: data
  })
}

