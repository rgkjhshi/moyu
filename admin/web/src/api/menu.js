import request from '@/utils/request'

// 获取路由
export function getRouters() {
  return request.get('/api/getRouters')
}
