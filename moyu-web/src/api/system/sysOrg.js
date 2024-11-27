import request from '@/utils/request'

// 查询组织机构树
export function getOrgTreeData(data) {
  return request.postJson('/api/sysOrg/treeData', data)
}
