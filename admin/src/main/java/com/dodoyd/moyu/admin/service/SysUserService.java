package com.dodoyd.moyu.admin.service;

import com.dodoyd.moyu.admin.domain.SysUser;
import com.dodoyd.moyu.admin.model.request.SysUserRequest;
import com.dodoyd.moyu.common.model.PageResult;

import java.util.List;

/**
 * SysUser服务
 *
 * @author moyusi
 * @since 2024-01-09
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
     * 通过主键查询SysUser
     *
     * @param userId 主键
     * @return SysUser
     */
    SysUser querySysUserByUserId(Long userId);

    /**
     * 查询SysUser
     *
     * @param request 查询请求
     * @return SysUser
     */
    SysUser querySysUser(SysUserRequest request);

    /**
     * 查询SysUser列表
     *
     * @param request 查询请求
     * @return SysUser列表
     */
    PageResult<SysUser> querySysUserList(SysUserRequest request);

    /**
     * 新增SysUser
     *
     * @param request 请求参数
     * @return 结果
     */
    int addSysUser(SysUserRequest request);

    /**
     * 修改SysUser
     *
     * @param request 请求参数
     * @return 结果
     */
    int updateSysUser(SysUserRequest request);

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

