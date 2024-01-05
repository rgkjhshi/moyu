package com.dodoyd.moyu.admin.service;

import com.dodoyd.moyu.admin.domain.SysUserRole;

import java.util.List;

/**
 * SysUserRole服务
 *
 * @author moyusi
 * @since 2024-01-05
 */
public interface SysUserRoleService {

    /**
     * 查询SysUserRole
     *
     * @param request 查询请求
     * @return SysUserRole
     */
    SysUserRole querySysUserRole(SysUserRole request);

    /**
     * 查询SysUserRole列表
     *
     * @param request 查询请求
     * @return SysUserRole列表
     */
    List<SysUserRole> querySysUserRoleList(SysUserRole request);

    /**
     * 新增SysUserRole
     *
     * @param request 请求参数
     * @return 结果
     */
    int addSysUserRole(SysUserRole request);

    /**
     * 修改SysUserRole
     *
     * @param request 请求参数
     * @return 结果
     */
    int updateSysUserRole(SysUserRole request);

}

