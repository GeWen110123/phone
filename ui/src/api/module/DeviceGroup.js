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

/**
 * 获取设备组用户列表
 * 
 * @returns {Promise} 返回包含用户列表的Promise对象
 */
export function listUser() {
  return request({
    url: '/module/deviceGroup/listUser',
    method: 'get',
  })
}

/**
 * 获取设备分组列表
 * 
 * @returns {Promise} 返回包含设备分组列表的Promise对象
 */
export function getDeviceGroup() {
  return request({
    url: '/module/deviceGroup/list',
    method: 'get'
  })
}


/**
 * 添加设备分组
 * 
 * @param {Object} data - 设备分组数据
 * @returns {Promise} 返回请求结果的Promise对象
 */
export function addGroup(data) {
  return request({
    url: '/module/deviceGroup',
    method: 'post',
    data: data
  })
}


/**
 * 更新设备分组信息
 * 
 * @param {Object} data - 设备分组更新数据
 * @returns {Promise} 返回请求结果的Promise对象
 */
export function updateDeviceGroup(data) {
  return request({
    url: '/module/deviceGroup',
    method: 'put',
    data: data
  })
}