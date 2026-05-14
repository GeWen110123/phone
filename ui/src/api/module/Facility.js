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
 * 获取用户列表
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
 * 获取设备日志列表
 * 
 * @param {Object} query - 查询参数对象
 * @param {string} [query.deviceId] - 设备ID
 * @param {string} [query.logType] - 日志类型
 * @param {string} [query.startTime] - 开始时间
 * @param {string} [query.endTime] - 结束时间
 * @param {number} [query.pageNum] - 页码
 * @param {number} [query.pageSize] - 每页大小
 * @returns {Promise} Promise对象，包含设备日志列表数据
 */
export function deviceLog(query) {
  return request({
    url: '/module/DeviceLog/list',
    method: 'get',
    params: query
  })
}
/**
 * 获取设备应用列表
 * 
 * @param {Object} query - 查询参数对象
 * @returns {Promise} 返回请求结果的Promise对象
 */
export function deviceApp(query) {
  return request({
    url: '/module/DeviceApp/list',
    method: 'get',
    params: query
  })
}



/**
 * 更新设备信息
 * 
 * @param {Object} data - 设备数据对象，包含需要更新的设备信息
 * @returns {Promise} 返回请求的Promise对象
 */
export function updateDevice(data) {
  return request({
    url: '/module/device/edit',
    method: 'put',
    data: data
  })
}











/**
 * 获取设备组列表
 * 
 * @returns {Promise} 包含设备组列表的Promise对象
 */
export function getDeviceGroup() {
  return request({
    url: '/module/deviceGroup/list',
    method: 'get'
  })
}

