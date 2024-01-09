package com.dodoyd.moyu.admin.service;

import com.dodoyd.moyu.admin.domain.SysUser;

import java.util.List;

/**
 * SysUser服务
 *
 * @author moyusi
 * @since 2024-01-05
 */
public interface SysUserService {

    /**
     * 查询SysUser
     *
     * @param username 用户username
     * @return SysRole
     */
    SysUser querySysUserByUsername(String username);

    /**
     * 查询SysUser
     *
     * @param request 查询请求
     * @return SysUser
     */
    SysUser querySysUser(SysUser request);

    /**
     * 查询SysUser列表
     *
     * @param request 查询请求
     * @return SysUser列表
     */
    List<SysUser> querySysUserList(SysUser request);

    /**
     * 新增SysUser
     *
     * @param request 请求参数
     * @return 结果
     */
    int addSysUser(SysUser request);

    /**
     * 修改SysUser
     *
     * @param request 请求参数
     * @return 结果
     */
    int updateSysUser(SysUser request);

    /**
     * 删除SysUser
     *
     * @param userId 主键
     * @return 结果
     */
    int deleteSysUser(Long userId);

    /**
     * 批量删除SysUser
     *
     * @param userIdList 主键列表
     * @return 结果
     */
    int batchDeleteSysUser(List<Long> userIdList);
}

