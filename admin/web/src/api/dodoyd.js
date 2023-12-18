import request from '@/utils/request'

// 查询场馆列表(仅查用户相关的)
export function queryVenueList(data) {
  return request({
    url: '/merchant/api/venue/list',
    method: 'post',
    data: data
  })
}

// 查询场馆信息
export function getVenueInfo(venueId) {
  return request({
    url: '/merchant/api/venue/info',
    method: 'get',
    params: { venueId }
  })
}

// 查询场馆信息
export function getVenueGroundTypeList(venueId) {
  return request({
    url: '/merchant/api/venue/groundTypeList',
    method: 'get',
    params: { venueId }
  })
}

// 查询我的权限(包括所有场馆)
export function getMyRoleList(data) {
  return request({
    url: '/merchant/api/venue/myRoleList',
    method: 'post',
    data: data
  })
}

// 查询我的场馆内权限(指定venueId)
export function getMyVenueRoleList(data) {
  return request({
    url: '/merchant/api/venue/myVenueRoleList',
    method: 'post',
    data: data
  })
}

// 查询场馆用户列表
export function getVenueUserList(data) {
  return request({
    url: '/merchant/api/venue/venueUserList',
    method: 'post',
    data: data
  })
}

// 删除场馆
export function deleteUserRole(data) {
  return request({
    url: '/merchant/api/venue/deleteUserRole',
    method: 'post',
    data: data
  })
}

// 添加场馆用户
export function addVenueUser(data) {
  return request({
    url: '/merchant/api/venue/addVenueUser',
    method: 'post',
    data: data
  })
}

// 添加场馆
export function addVenue(data) {
  return request({
    url: '/merchant/api/venue/addVenue',
    method: 'post',
    data: data
  })
}

// 修改场馆
export function updateVenue(data) {
  return request({
    url: '/merchant/api/venue/updateVenue',
    method: 'post',
    data: data
  })
}

// 更新场馆开关配置
export function updateVenueConfig(data) {
  return request({
    url: '/merchant/api/venue/updateVenueConfig',
    method: 'post',
    data: data
  })
}

// 查询预览日期列表
export function dateList() {
  return request({
    url: '/merchant/api/venue/dateList',
    method: 'get'
  })
}

// 查询场地列表
export function queryGroundList(data) {
  return request({
    url: '/merchant/api/ground/list',
    method: 'post',
    data: data
  })
}

// 查询场馆信息
export function getGroundInfo(data) {
  return request({
    url: '/merchant/api/ground/info',
    method: 'post',
    data: data
  })
}

// 添加场地
export function addGround(data) {
  return request({
    url: '/merchant/api/ground/addGround',
    method: 'post',
    data: data
  })
}

// 修改场地
export function updateGround(data) {
  return request({
    url: '/merchant/api/ground/updateGround',
    method: 'post',
    data: data
  })
}

// 查询单个场地使用记录
export function queryUseRecordDetail(data) {
  return request({
    url: '/merchant/api/ground/useRecordDetail',
    method: 'post',
    data: data
  })
}

// 查询场地使用列表
export function queryGroundUseRecord(data) {
  return request({
    url: '/merchant/api/ground/useRecord',
    method: 'post',
    data: data
  })
}

// 场地预订概览
export function showGroundBook(data) {
  return request({
    url: '/merchant/api/ground/showBookList',
    method: 'post',
    data: data
  })
}

// 教练日程表概览
export function showCoachBook(data) {
  return request({
    url: '/merchant/api/ground/showCoachBookList',
    method: 'post',
    data: data
  })
}

// 预订场地
export function bookGround(data) {
  return request({
    url: '/merchant/api/ground/bookGround',
    method: 'post',
    data: data
  })
}

// 取消预订
export function cancelBookGround(data) {
  return request({
    url: '/merchant/api/ground/cancelBookGround',
    method: 'post',
    data: data
  })
}

// 查询场馆订单列表
export function queryVenueOrderList(data) {
  return request({
    url: '/merchant/api/order/getVenueOrderList',
    method: 'post',
    data: data
  })
}

// 查询最新的订单信息
export function queryLastedOrderInfo(data) {
  return request({
    url: '/merchant/api/order/getLatestOrderInfo',
    method: 'post',
    data: data
  })
}

// 申请退款
export function applyRefund(data) {
  return request({
    url: '/merchant/api/order/applyRefund',
    method: 'post',
    data: data
  })
}

// 查询场馆的会员卡
export function getVenueCardInfo(data) {
  return request({
    url: '/merchant/api/card/getVenueCardInfo',
    method: 'post',
    data: data
  })
}

// 查询场馆的会员卡
export function getVenueCardList(data) {
  return request({
    url: '/merchant/api/card/getVenueCardList',
    method: 'post',
    data: data
  })
}

// 添加场馆会员卡
export function addVenueCard(data) {
  return request({
    url: '/merchant/api/card/addVenueCard',
    method: 'post',
    data: data
  })
}

// 修改场馆会员卡部分信息
export function updateVenueCard(data) {
  return request({
    url: '/merchant/api/card/updateVenueCard',
    method: 'post',
    data: data
  })
}

// 删除场馆会员卡
export function deleteVenueCard(data) {
  return request({
    url: '/merchant/api/card/deleteVenueCard',
    method: 'post',
    data: data
  })
}

// 查询会员卡
export function getVipCardInfo(data) {
  return request({
    url: '/merchant/api/vip/getVipCardInfo',
    method: 'post',
    data: data
  })
}

// 查询会员卡列表
export function getVipCardList(data) {
  return request({
    url: '/merchant/api/vip/getVipCardList',
    method: 'post',
    data: data
  })
}

// 办理会员卡
export function addVipCard(data) {
  return request({
    url: '/merchant/api/vip/addVipCard',
    method: 'post',
    data: data
  })
}

// 会员卡代扣
export function vipCardWithhold(data) {
  return request({
    url: '/merchant/api/vip/vipCardWithhold',
    method: 'post',
    data: data
  })
}

// 会员卡延期
export function vipCardExpandDate(data) {
  return request({
    url: '/merchant/api/vip/vipCardExpandDate',
    method: 'post',
    data: data
  })
}

// 会员卡延期
export function getCardConsumeLog(data) {
  return request({
    url: '/merchant/api/vip/getCardConsumeLog',
    method: 'post',
    params: data
  })
}

// 查询规则配置列表
export function queryRuleConfigList(data) {
  return request({
    url: '/merchant/api/venue/queryRuleConfigList',
    method: 'post',
    data: data
  })
}

// 查询规则配置信息
export function queryRuleConfigInfo(data) {
  return request({
    url: '/merchant/api/venue/queryRuleConfigInfo',
    method: 'post',
    data: data
  })
}

// 添加规则配置
export function addRuleConfig(data) {
  return request({
    url: '/merchant/api/venue/addRuleConfig',
    method: 'post',
    data: data
  })
}

// 修改规则配置
export function updateRuleConfig(data) {
  return request({
    url: '/merchant/api/venue/updateRuleConfig',
    method: 'post',
    data: data
  })
}

