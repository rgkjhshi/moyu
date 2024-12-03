import request from '@/utils/request'

// 查询SysRole列表
export function listSysRole(data) {
  return request.postJson('/api/sysRole/list', data)
}

// 查询SysRole详细信息
export function getSysRole(data) {
  return request({
    url: '/api/sysRole/get',
    method: 'get',
    params: data
  })
}

// 查询SysRole详细信息
export function querySysRole(data) {
  return request({
    url: '/api/sysRole/query',
    method: 'post',
    // data是json，params是查询参数
    data: data
  })
}

// 新增SysRole
export function addSysRole(data) {
  return request.postJson('/api/sysRole/add', data)
}

// 修改SysRole
export function editSysRole(data) {
  return request.postJson('/api/sysRole/edit', data)
}

// 删除SysRole
export function deleteSysRole(data) {
  return request.postForm('/api/sysRole/batchDelete', data)
}
