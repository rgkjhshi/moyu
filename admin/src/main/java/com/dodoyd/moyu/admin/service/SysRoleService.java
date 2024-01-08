package com.dodoyd.moyu.admin.service;

import com.dodoyd.moyu.admin.domain.SysRole;

import java.util.List;

/**
 * SysRole服务
 *
 * @author moyusi
 * @since 2024-01-05
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
     * 查询SysRole
     *
     * @param request 查询请求
     * @return SysRole
     */
    SysRole querySysRole(SysRole request);

    /**
     * 查询SysRole列表
     *
     * @param request 查询请求
     * @return SysRole列表
     */
    List<SysRole> querySysRoleList(SysRole request);

    /**
     * 新增SysRole
     *
     * @param request 请求参数
     * @return 结果
     */
    int addSysRole(SysRole request);

    /**
     * 修改SysRole
     *
     * @param request 请求参数
     * @return 结果
     */
    int updateSysRole(SysRole request);

}

