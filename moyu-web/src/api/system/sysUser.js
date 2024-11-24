import request from '@/utils/request'

// 查询SysUser列表
export function listSysUser(data) {
  return request.postJson('/api/sysUser/list', data)
}

// 查询SysUser详细信息
export function getSysUser(data) {
  return request({
    url: '/api/sysUser/get',
    method: 'get',
    params: data
  })
}

// 查询SysUser详细信息
export function querySysUser(data) {
  return request({
    url: '/api/sysUser/query',
    method: 'post',
    // data是json，params是查询参数
    data: data
  })
}

// 新增SysUser
export function addSysUser(data) {
  return request.postJson('/api/sysUser/add', data)
}

// 修改SysUser
export function editSysUser(data) {
  return request.postJson('/api/sysUser/edit', data)
}

// 批量删除SysUser
export function deleteSysUser(data) {
  return request.postForm('/api/sysUser/batchDelete', data)
}
