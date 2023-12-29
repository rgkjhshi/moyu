import request, { download } from '@/utils/request'

// 查询数据库表
export function listDbTable(data) {
  return request({
    url: '/api/tool/gen/list',
    method: 'post',
    // data是json，params是查询参数
    params: data
  })
}

// 预览生成代码
export function previewCode(data) {
  return request({
    url: '/api/tool/gen/preview',
    method: 'post',
    // data是json，params是查询参数
    data: data
  })
}

// 通过解析SQL预览生成代码
export function previewCodeBySql(data) {
  return request({
    url: '/api/tool/gen/previewBySql',
    method: 'post',
    data: data
  })
}

// 下载代码
export function downloadCode(data) {
  return download({
    url: '/api/tool/gen/download',
    method: 'post',
    data: data
  })
}

// 通过SQL生成代码并下载
export function downloadCodeBySql(data) {
  return download({
    url: '/api/tool/gen/downloadBySql',
    method: 'post',
    data: data
  })
}

