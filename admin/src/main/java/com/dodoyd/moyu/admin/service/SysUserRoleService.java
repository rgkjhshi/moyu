package com.dodoyd.moyu.admin.service;

import com.dodoyd.moyu.admin.domain.SysUserRole;
import com.dodoyd.moyu.admin.model.request.SysUserRoleRequest;

import java.util.List;

/**
 * SysUserRole服务
 *
 * @author moyusi
 * @since 2024-01-09
 */
public interface SysUserRoleService {

    /**
     * 通过主键查询SysUserRole
     *
     * @param id 主键
     * @return SysUserRole
     */
    SysUserRole querySysUserRoleById(Long id);

    /**
     * 查询SysUserRole
     *
     * @param request 查询请求
     * @return SysUserRole
     */
    SysUserRole querySysUserRole(SysUserRoleRequest request);

    /**
     * 查询SysUserRole列表
     *
     * @param request 查询请求
     * @return SysUserRole列表
     */
    List<SysUserRole> querySysUserRoleList(SysUserRoleRequest request);

    /**
     * 新增SysUserRole
     *
     * @param request 请求参数
     * @return 结果
     */
    int addSysUserRole(SysUserRoleRequest request);

    /**
     * 修改SysUserRole
     *
     * @param request 请求参数
     * @return 结果
     */
    int updateSysUserRole(SysUserRoleRequest request);

    /**
     * 删除SysUserRole
     *
     * @param id 主键
     * @return 结果
     */
    int deleteSysUserRole(Long id);

    /**
     * 批量删除SysUserRole
     *
     * @param idList 主键列表
     * @return 结果
     */
    int batchDeleteSysUserRole(List<Long> idList);

}

