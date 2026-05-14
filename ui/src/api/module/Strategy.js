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
 * 获取模板列表
 * 
 * @param {Object} query 查询参数对象
 * @returns {Promise} 返回请求结果Promise
 */
export function template(query) {
  return request({
    url: '/module/template/list',
    method: 'get',
    params: query
  })
}

/**
 * 更新模板数据
 * 
 * @param {Object} data - 需要更新的模板数据对象
 * @returns {Promise} 返回请求的Promise对象
 */
export function updateTemplate(data) {
  return request({
    url: '/module/template',
    method: 'put',
    data: data
  })
}




/**
 * 获取所有设备分组信息
 * 
 * 调用后端接口获取系统中所有的设备分组数据
 * 
 * @returns {Promise} 返回包含所有设备分组信息的Promise对象
 */
export function groupAll() {
  return request({
    url: '/module/deviceGroup/groupAll',
    method: 'get',
  })
}





/**
 * 添加设备组
 * 
 * @param {Object} data - 设备组数据对象
 * @returns {Promise} 请求Promise对象
 */
export function addGroup(data) {
  return request({
    url: '/module/deviceGroup',
    method: 'post',
    data: data
  })
}

export function task(query) {
  return request({
    url: '/module/taskTemplate/list',
    method: 'get',
    params: query
  })
}

/**
 * 更新任务模板
 * 
 * @param {Object} data - 任务模板数据对象
 * @returns {Promise} 请求Promise对象
 */
export function updateTask(data) {
  return request({
    url: '/module/taskTemplate',
    method: 'put',
    data: data
  })
}
/**
 * 获取任务模板数据
 * 
 * @param {Object} query - 查询参数对象
 * @returns {Promise} 返回请求的Promise对象
 */
export function goToTask(query) {
  return request({
    url: '/module/taskTemplate/getToTask',
    method: 'get',
    params: query
  })
}

/**
 * 添加任务
 * 
 * @param {Object} data - 任务数据对象
 * @returns {Promise} 请求Promise对象
 */
export function addTask(data) {
  return request({
    url: '/module/taskTemplate',
    method: 'post',
    data: data
  })
}


