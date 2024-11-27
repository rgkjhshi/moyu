import request from '@/utils/request'

// 获取组织机构树形选项
export function getOrgTreeOptions() {
  return request({
    url: '/api/sysOrg/treeOptions',
    method: 'get'
  })
}
