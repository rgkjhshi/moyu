import service from '@/utils/request'

/**
 * 角色相关接口
 */
export default {
	// 查询角色列表
	roleList(data) {
		return service.postJson('/api/sys/role/list', data)
	},
	// 分页查询角色列表
	rolePage(data) {
		return service.postJson('/api/sys/role/page', data)
	},
	// 获取角色详情
	roleDetail(data) {
		return service.postJson('/api/sys/role/detail', data)
	},
	// 新增角色
	addRole(data) {
		return service.postJson('/api/sys/role/add', data)
	},
	// 删除角色，通过ids删除
	deleteRole(data) {
		return service.postJson('/api/sys/role/delete', data)
	},
	// 编辑角色
	editRole(data) {
		return service.postJson('/api/sys/role/edit', data)
	},
	// 获取角色菜单树，用于给角色授权时选择
	menuTreeForGrant(data) {
		return service.postJson('/api/sys/role/menuTreeForGrant', data)
	},
	// 给角色授权菜单
	roleGrantMenu(data) {
		return service.postJson('/api/sys/role/grantMenu', data)
	},
	// 角色用户列表
	roleUserList(data) {
		return service.postJson('/api/sys/role/userList', data)
	},
	// 用户直接授权角色
	userGrantRole(data) {
		return service.postJson('/api/sys/role/userGrantRole', data)
	},
	// 用户撤销已授权角色
	userRevokeRole(data) {
		return service.postJson('/api/sys/role/userRevokeRole', data)
	},
}
