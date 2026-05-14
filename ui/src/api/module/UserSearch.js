import request from '@/utils/request'

/**
 * 获取设备列表
 * 
 * @param {Object} query - 查询参数对象
 * @returns {Promise} 返回请求的Promise对象
 */
export function getCrawl(query) {
  return request({
    url: '/module/account/crawl',
    method: 'get',
    params: query
  })
}

export function crawl(query) {
  return request({
    url: '/module/account/list',
    method: 'get',
    params: query
  })
}