import request from '@/utils/request'

// 查询数据库表
export function listDbTable(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request.post('/api/tool/gen/list', formData)
}

// 预览生成代码
export function previewCode(data) {
  return request.post('/api/tool/gen/preview', data)
}

// 通过解析SQL预览生成代码
export function previewCodeBySql(data) {
  return request({
    url: '/api/tool/gen/previewBySql',
    method: 'post',
    // data是json，params是查询参数
    data: data
  })
}

// 下载代码
export function downloadCode(data) {
  return request.download({
    url: '/api/tool/gen/download',
    method: 'post',
    // data是json，params是查询参数
    data: data
  })
}

// 通过SQL生成代码并下载
export function downloadCodeBySql(data) {
  return request.download({
    url: '/api/tool/gen/downloadBySql',
    method: 'post',
    data: data
  })
}

