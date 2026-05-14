import request from '@/utils/request'

export function listWzinfo(query) {
  return request({
    url: '/module/wzinfo/list',
    method: 'get',
    params: query
  })
}

export function listByReportDept(query) {
  return request({
    url: '/module/wzinfo/listByReportDept',
    method: 'get',
    params: query
  })
}



// 查询文章信息详细
export function getWzinfo(id) {
  return request({
    url: '/module/wzinfo/' + id,
    method: 'get'
  })
}


export function fetchChildren(query) {
  return request({
    url: '/module/WzTypeTag/tagsById',
    method: 'get',
    params: query
  })
}





export function wzTypeAll() {
  return request({
    url: '/module/wzinfo/wzTypeAll',
    method: 'get',
  })
}

export function listOtherDept() {
  return request({
    url: '/module/wzinfo/listOtherDept',
    method: 'get',
  })
}

export function wzListDeptByAll() {
  return request({
    url: '/module/wzinfo/listDeptByAll',
    method: 'get',
  })
}


export function listDeptByParentId() {
  return request({
    url: '/module/wzinfo/listDeptByParentId',
    method: 'get',
  })
}




export function updateEditorial(data) {
  return request({
    url: '/module/wzinfo/editorialReport',
    method: 'put',
    data: data
  })
}

export function updateRating(data) {
  return request({
    url: '/module/wzinfo/rating',
    method: 'put',
    data: data
  })
}










// 新增文章信息
export function addWzinfo(data) {
  return request({
    url: '/module/wzinfo',
    method: 'post',
    data: data
  })
}

// 修改文章信息
export function updateWzinfo(data) {
  return request({
    url: '/module/wzinfo',
    method: 'put',
    data: data
  })
}

export function updateEvaluation(data) {
  return request({
    url: '/module/wzinfo/evaluation',
    method: 'put',
    data: data
  })
}
export function updateReport(data) {
  return request({
    url: '/module/wzinfo/report',
    method: 'put',
    data: data
  })
}
export function updateWithdraw(data) {
  return request({
    url: '/module/wzinfo/withdraw',
    method: 'put',
    data: data
  })
}
export function updateReject(data) {
  return request({
    url: '/module/wzinfo/reject',
    method: 'put',
    data: data
  })
}

export function updateIgnore(data) {
  return request({
    url: '/module/wzinfo/ignore',
    method: 'put',
    data: data
  })
}
export function updateCancelAdopt(data) {
  return request({
    url: '/module/wzinfo/cancelAdopt ',
    method: 'put',
    data: data
  })
}
export function updateCancelLocalPj(data) {
  return request({
    url: '/module/wzinfo/cancelLocalPj',
    method: 'put',
    data: data
  })
}
export function updateCancelReport(data) {
  return request({
    url: '/module/wzinfo/cancelReport',
    method: 'put',
    data: data
  })
}

export function updateCancelRating(data) {
  return request({
    url: '/module/wzinfo/cancelRating',
    method: 'put',
    data: data
  })
}



export function updateAdopt(data) {
  return request({
    url: '/module/wzinfo/adopt',
    method: 'put',
    data: data
  })
}
export function updateLocalPj(data) {
  return request({
    url: '/module/wzinfo/localPj',
    method: 'put',
    data: data
  })
}



export function updateAdopt1(data) {
  return request({
    url: '/module/wzinfo/adopt1',
    method: 'put',
    data: data
  })
}









// 删除文章信息
export function delWzinfo(id) {
  return request({
    url: '/module/wzinfo/' + id,
    method: 'delete'
  })
}