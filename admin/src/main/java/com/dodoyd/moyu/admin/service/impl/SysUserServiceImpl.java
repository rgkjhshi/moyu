package com.dodoyd.moyu.admin.service.impl;

import com.dodoyd.moyu.admin.dao.SysUserDao;
import com.dodoyd.moyu.admin.domain.SysUser;
import com.dodoyd.moyu.admin.service.SysUserService;
import com.google.common.base.Strings;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * SysUser服务实现类
 *
 * @author moyusi
 * @since 2024-01-05
 */
@Service
public class SysUserServiceImpl implements SysUserService {

    @Resource
    private SysUserDao sysUserDao;

    @Override
    public SysUser querySysUserByUsername(String username) {
        SysUser query = new SysUser();
        query.setUsername(username);
        return sysUserDao.selectOne(query);
    }

    @Override
    public SysUser querySysUser(SysUser request) {
        SysUser query = new SysUser();
        if (request.getUserId() != null) {
            query.setUserId(request.getUserId());
        }
        if (!Strings.isNullOrEmpty(request.getUsername())) {
            query.setUsername(request.getUsername());
        }
        if (!Strings.isNullOrEmpty(request.getNickname())) {
            query.setNickname(request.getNickname());
        }
        if (request.getGender() != null) {
            query.setGender(request.getGender());
        }
        if (!Strings.isNullOrEmpty(request.getAvatar())) {
            query.setAvatar(request.getAvatar());
        }
        if (!Strings.isNullOrEmpty(request.getEmail())) {
            query.setEmail(request.getEmail());
        }
        if (!Strings.isNullOrEmpty(request.getMobile())) {
            query.setMobile(request.getMobile());
        }
        if (!Strings.isNullOrEmpty(request.getUserPwd())) {
            query.setUserPwd(request.getUserPwd());
        }
        if (!Strings.isNullOrEmpty(request.getLoginIp())) {
            query.setLoginIp(request.getLoginIp());
        }
        if (request.getLoginTime() != null) {
            query.setLoginTime(request.getLoginTime());
        }
        if (request.getStatus() != null) {
            query.setStatus(request.getStatus());
        }
        if (request.getDeleted() != null) {
            query.setDeleted(request.getDeleted());
        }
        if (!Strings.isNullOrEmpty(request.getCreateBy())) {
            query.setCreateBy(request.getCreateBy());
        }
        if (!Strings.isNullOrEmpty(request.getUpdateBy())) {
            query.setUpdateBy(request.getUpdateBy());
        }
        if (request.getCreateTime() != null) {
            query.setCreateTime(request.getCreateTime());
        }
        if (request.getUpdateTime() != null) {
            query.setUpdateTime(request.getUpdateTime());
        }
        if (!Strings.isNullOrEmpty(request.getRemark())) {
            query.setRemark(request.getRemark());
        }
        SysUser sysUser = sysUserDao.selectOne(query);
        return sysUser;
    }

    @Override
    public List<SysUser> querySysUserList(SysUser request) {
        SysUser query = new SysUser();
        if (request.getUserId() != null) {
            query.setUserId(request.getUserId());
        }
        if (!Strings.isNullOrEmpty(request.getUsername())) {
            query.setUsername(request.getUsername());
        }
        if (!Strings.isNullOrEmpty(request.getNickname())) {
            query.setNickname(request.getNickname());
        }
        if (request.getGender() != null) {
            query.setGender(request.getGender());
        }
        if (!Strings.isNullOrEmpty(request.getAvatar())) {
            query.setAvatar(request.getAvatar());
        }
        if (!Strings.isNullOrEmpty(request.getEmail())) {
            query.setEmail(request.getEmail());
        }
        if (!Strings.isNullOrEmpty(request.getMobile())) {
            query.setMobile(request.getMobile());
        }
        if (!Strings.isNullOrEmpty(request.getUserPwd())) {
            query.setUserPwd(request.getUserPwd());
        }
        if (!Strings.isNullOrEmpty(request.getLoginIp())) {
            query.setLoginIp(request.getLoginIp());
        }
        if (request.getLoginTime() != null) {
            query.setLoginTime(request.getLoginTime());
        }
        if (request.getStatus() != null) {
            query.setStatus(request.getStatus());
        }
        if (request.getDeleted() != null) {
            query.setDeleted(request.getDeleted());
        }
        if (!Strings.isNullOrEmpty(request.getCreateBy())) {
            query.setCreateBy(request.getCreateBy());
        }
        if (!Strings.isNullOrEmpty(request.getUpdateBy())) {
            query.setUpdateBy(request.getUpdateBy());
        }
        if (request.getCreateTime() != null) {
            query.setCreateTime(request.getCreateTime());
        }
        if (request.getUpdateTime() != null) {
            query.setUpdateTime(request.getUpdateTime());
        }
        if (!Strings.isNullOrEmpty(request.getRemark())) {
            query.setRemark(request.getRemark());
        }
        List<SysUser> list = sysUserDao.selectList(query);
        return list;
    }

    @Override
    public int addSysUser(SysUser request) {
        SysUser add = new SysUser();
        if (request.getUserId() != null) {
            add.setUserId(request.getUserId());
        }
        if (!Strings.isNullOrEmpty(request.getUsername())) {
            add.setUsername(request.getUsername());
        }
        if (!Strings.isNullOrEmpty(request.getNickname())) {
            add.setNickname(request.getNickname());
        }
        if (request.getGender() != null) {
            add.setGender(request.getGender());
        }
        if (!Strings.isNullOrEmpty(request.getAvatar())) {
            add.setAvatar(request.getAvatar());
        }
        if (!Strings.isNullOrEmpty(request.getEmail())) {
            add.setEmail(request.getEmail());
        }
        if (!Strings.isNullOrEmpty(request.getMobile())) {
            add.setMobile(request.getMobile());
        }
        if (!Strings.isNullOrEmpty(request.getUserPwd())) {
            add.setUserPwd(request.getUserPwd());
        }
        if (!Strings.isNullOrEmpty(request.getLoginIp())) {
            add.setLoginIp(request.getLoginIp());
        }
        if (request.getLoginTime() != null) {
            add.setLoginTime(request.getLoginTime());
        }
        if (request.getStatus() != null) {
            add.setStatus(request.getStatus());
        }
        if (request.getDeleted() != null) {
            add.setDeleted(request.getDeleted());
        }
        if (!Strings.isNullOrEmpty(request.getCreateBy())) {
            add.setCreateBy(request.getCreateBy());
        }
        if (!Strings.isNullOrEmpty(request.getUpdateBy())) {
            add.setUpdateBy(request.getUpdateBy());
        }
        if (request.getCreateTime() != null) {
            add.setCreateTime(request.getCreateTime());
        }
        if (request.getUpdateTime() != null) {
            add.setUpdateTime(request.getUpdateTime());
        }
        if (!Strings.isNullOrEmpty(request.getRemark())) {
            add.setRemark(request.getRemark());
        }
        int row = sysUserDao.insert(add);
        return row;
    }

    @Override
    public int updateSysUser(SysUser request) {
        SysUser update = new SysUser();
        if (request.getUserId() != null) {
            update.setUserId(request.getUserId());
        }
        if (!Strings.isNullOrEmpty(request.getUsername())) {
            update.setUsername(request.getUsername());
        }
        if (!Strings.isNullOrEmpty(request.getNickname())) {
            update.setNickname(request.getNickname());
        }
        if (request.getGender() != null) {
            update.setGender(request.getGender());
        }
        if (!Strings.isNullOrEmpty(request.getAvatar())) {
            update.setAvatar(request.getAvatar());
        }
        if (!Strings.isNullOrEmpty(request.getEmail())) {
            update.setEmail(request.getEmail());
        }
        if (!Strings.isNullOrEmpty(request.getMobile())) {
            update.setMobile(request.getMobile());
        }
        if (!Strings.isNullOrEmpty(request.getUserPwd())) {
            update.setUserPwd(request.getUserPwd());
        }
        if (!Strings.isNullOrEmpty(request.getLoginIp())) {
            update.setLoginIp(request.getLoginIp());
        }
        if (request.getLoginTime() != null) {
            update.setLoginTime(request.getLoginTime());
        }
        if (request.getStatus() != null) {
            update.setStatus(request.getStatus());
        }
        if (request.getDeleted() != null) {
            update.setDeleted(request.getDeleted());
        }
        if (!Strings.isNullOrEmpty(request.getCreateBy())) {
            update.setCreateBy(request.getCreateBy());
        }
        if (!Strings.isNullOrEmpty(request.getUpdateBy())) {
            update.setUpdateBy(request.getUpdateBy());
        }
        if (request.getCreateTime() != null) {
            update.setCreateTime(request.getCreateTime());
        }
        if (request.getUpdateTime() != null) {
            update.setUpdateTime(request.getUpdateTime());
        }
        if (!Strings.isNullOrEmpty(request.getRemark())) {
            update.setRemark(request.getRemark());
        }
        int row = sysUserDao.updateByUserId(update);
        return row;
    }

    @Override
    public int deleteSysUser(Long userId) {
        int row = sysUserDao.deleteByUserId(userId);
        return row;
    }

    @Override
    public int batchDeleteSysUser(List<Long> userIdList) {
        int row = sysUserDao.deleteByUserIdList(userIdList);
        return row;
    }
}
