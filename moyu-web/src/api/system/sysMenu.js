import request from '@/utils/request'

// 查询SysMenu列表
export function listSysMenu(data) {
  return request.postJson('/api/sysMenu/list', data)
}

// 查询SysMenu详细信息
export function getSysMenu(data) {
  return request({
    url: '/api/sysMenu/get',
    method: 'get',
    params: data
  })
}

// 查询SysMenu详细信息
export function querySysMenu(data) {
  return request({
    url: '/api/sysMenu/query',
    method: 'post',
    // data是json，params是查询参数
    data: data
  })
}

// 新增SysMenu
export function addSysMenu(data) {
  return request.postJson('/api/sysMenu/add', data)
}

// 修改SysMenu
export function editSysMenu(data) {
  return request.postJson('/api/sysMenu/edit', data)
}

// 删除SysMenu
export function deleteSysMenu(data) {
  return request.postForm('/api/sysMenu/batchDelete', data)
}
