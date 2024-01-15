package com.dodoyd.moyu.admin.service;

import com.dodoyd.moyu.admin.domain.SysRole;
import com.dodoyd.moyu.admin.model.request.SysRoleRequest;
import com.dodoyd.moyu.common.model.PageResult;

import java.util.List;

/**
 * SysRole服务
 *
 * @author moyusi
 * @since 2024-01-15
 */
public interface SysRoleService {

    /**
     * 查询用户权限列表
     *
     * @param userId 用户ID
     * @return SysRole列表
     */
    List<SysRole> queryUserRoleList(Long userId);

    /**
     * 通过主键查询SysRole
     *
     * @param id 主键
     * @return SysRole
     */
    SysRole querySysRoleById(Long id);

    /**
     * 查询SysRole
     *
     * @param request 查询请求
     * @return SysRole
     */
    SysRole querySysRole(SysRoleRequest request);

    /**
     * 查询SysRole列表
     *
     * @param request 查询请求
     * @return SysRole列表
     */
    PageResult<SysRole> querySysRoleList(SysRoleRequest request);

    /**
     * 新增SysRole
     *
     * @param request 请求参数
     * @return 结果
     */
    int addSysRole(SysRoleRequest request);

    /**
     * 修改SysRole
     *
     * @param request 请求参数
     * @return 结果
     */
    int updateSysRole(SysRoleRequest request);

    /**
     * 删除SysRole
     *
     * @param id 主键
     * @return 结果
     */
    int deleteSysRole(Long id);

    /**
     * 批量删除SysRole
     *
     * @param idList 主键列表
     * @return 结果
     */
    int batchDeleteSysRole(List<Long> idList);

}

