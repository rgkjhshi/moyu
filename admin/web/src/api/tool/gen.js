import request from '@/utils/request'

// 查询数据库表
export function listDbTable(data) {
  return request({
    url: '/api/tool/gen/list',
    method: 'post',
    // data是json，params是表单
    data: data
  })
}

