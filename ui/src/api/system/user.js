import request from '@/utils/request'
import {
  parseStrEmpty
} from '@/utils/ruoyi'

// 查询用户列表
export function listUser(query) {
  return request({
    url: '/system/user/list',
    method: 'get',
    params: query
  })
}
// 查询市级统计评分列表
export function listCity(query) {
  return request({
    url: '/module/statistics_city/list',
    method: 'get',
    params: query
  })
}
// 查询统计列表
export function listStatistics(query) {
  return request({
    url: '/module/statistics/list',
    method: 'get',
    params: query
  })
}


// 查询用户详细
export function getUser(userId) {
  return request({
    url: '/system/user/' + parseStrEmpty(userId),
    method: 'get'
  })
}

// 新增用户
export function addUser(data) {
  return request({
    url: '/system/user',
    method: 'post',
    data: data
  })
}

// 修改用户
export function updateUser(data) {
  return request({
    url: '/system/user',
    method: 'put',
    data: data
  })
}





// 删除用户
export function delUser(userId) {
  return request({
    url: '/system/user/' + userId,
    method: 'delete'
  })
}

// 用户密码重置
export function resetUserPwd(userId, password) {
  const data = {
    userId,
    password
  }

  return request({
    url: '/system/user/resetPwd',
    method: 'put',
    data: data
  })


}





// 用户状态修改
export function changeUserStatus(userId, status) {
  const data = {
    userId,
    status
  }
  return request({
    url: '/system/user/changeStatus',
    method: 'put',
    data: data
  })
}

export function changeLoginStatus(userId, loginStatus) {
  const data = {
    userId,
    loginStatus
  }
  return request({
    url: '/system/user/changeLoginStatus',
    method: 'put',
    data: data
  })
}


// 查询用户个人信息
export function getUserProfile() {
  return request({
    url: '/system/user/profile',
    method: 'get'
  })
}

// 修改用户个人信息
export function updateUserProfile(data) {
  return request({
    url: '/system/user/profile',
    method: 'put',
    data: data
  })
}

// 用户密码重置
export function updateUserPwd(oldPassword, newPassword, token) {
  const data = {
    oldPassword,
    newPassword
  }
  if (token) {
    return request({
      url: '/system/user/profile/updatePwd',
      method: 'put',
      headers: {
        Authorization: "Bearer " + token,
      },
      params: data
    })
  } else {
    return request({
      url: '/system/user/profile/updatePwd',
      method: 'put',
      params: data
    })
  }


}


// 用户双因子口令验证并重置
export function vaildTcode(tcode) {
  const data = {
    tcode
  }
  return request({
    url: '/system/user/profile/tcodecheck',
    method: 'put',
    params: data
  })
}


//关闭双因子口令验证(用户自己)
export function resetUserTcode() {
  return request({
    url: '/system/user/profile/resetUserTcode',
    method: 'get'
  })
}


//获取用户双因子口令状态

export function tcodeStatus() {
  return request({
    url: '/system/user/profile/tcodestatus',
    method: 'get'
  })
}

// 用户头像上传
export function uploadAvatar(data) {
  return request({
    url: '/system/user/profile/avatar',
    method: 'post',
    data: data
  })
}

// 导入用户
export function importData(data) {
  return request({
    url: '/system/user/importData',
    method: 'post',
    data: data
  })
}

export function deptData(data) {
  return request({
    url: '/system/dept/importData',
    method: 'post',
    data: data
  })
}
// 查询普通用户角色
export function getListAllCommonUser() {
  return request({
    url: '/system/user/listAllCommonUser',
    method: 'get'
  })
}








// 查询授权角色
export function getAuthRole(userId) {
  return request({
    url: '/system/user/authRole/' + userId,
    method: 'get'
  })
}

// 保存授权角色
export function updateAuthRole(data) {
  return request({
    url: '/system/user/authRole',
    method: 'put',
    params: data
  })
}

// 查询部门下拉树结构
export function deptTreeSelect() {
  return request({
    url: '/system/user/deptTreeProject',
    method: 'get'
  })
}


// 查询排名统计列表
export function listStatistical(query) {
  return request({
    url: '/module/statistical/list',
    method: 'get',
    params: query
  })
}


// 查询首页排名统计列表
export function selectSumList(query) {
  return request({
    url: '/module/statistical/selectSumList',
    method: 'get',
    params: query
  })
}