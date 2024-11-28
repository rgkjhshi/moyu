import request from '@/utils/request'

// 获取组织树下拉选项
export function getOrgTreeOptions() {
  return request({
    url: '/api/system/org/tree',
    method: 'get'
  })
}

// 分页获取组织列表
export function listSysOrg(data) {
  return request.postJson('/api/system/org/list', data)
}

