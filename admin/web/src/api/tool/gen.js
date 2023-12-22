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

// 预览生成代码
export function previewCode(data) {
  return request({
    url: '/api/tool/gen/preview',
    method: 'post',
    // data是json，params是表单
    params: data
  })
}

// 生成代码（自定义路径）
export function genCode(data) {
  return request({
    url: '/api/tool/gen/genCode',
    method: 'post',
    // data是json，params是表单
    params: data
  })
}

