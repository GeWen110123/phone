import request from '@/utils/request'

// 查询公告列表
export function listNotice(query) {
  return request({
    url: '/module/message/list',
    method: 'get',
    params: query
  })
}

// 
export function readId(id) {
  return request({
    url: `/module/message/read/${id}`,
    method: 'get',
  })
}



// 查询公告详细
export function getNotice(noticeId) {
  return request({
    url: '/system/notice/' + noticeId,
    method: 'get'
  })
}

// 新增公告
export function addNotice(data) {
  return request({
    url: '/system/notice',
    method: 'post',
    data: data
  })
}

// 修改公告
export function updateNotice(data) {
  return request({
    url: '/system/notice',
    method: 'put',
    data: data
  })
}

// 删除公告
export function delNotice(noticeId) {
  return request({
    url: '/system/notice/' + noticeId,
    method: 'delete'
  })
}