import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/api/user/login',
    method: 'post',
    data
  })
}

export function getInfo(token) {
  return request({
    url: '/api/user/info',
    method: 'get',
    params: { token }
  })
}

export function logout() {
  return request({
    url: '/api/user/logout',
    method: 'post'
  })
}

// 查询用户账号信息
export function getUserInfo(data) {
  return request({
    url: '/api/user/getUserInfo',
    method: 'post',
    data: data
  })
}

// 更改用户信息
export function updateUserInfo(data) {
  return request({
    url: '/api/user/updateInfo',
    method: 'post',
    data: data
  })
}

// 修改密码口令
export function changePassword(data) {
  return request({
    url: '/api/user/changePwd',
    method: 'post',
    data: data
  })
}
