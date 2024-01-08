package com.dodoyd.moyu.admin.service.impl;

import com.dodoyd.moyu.admin.dao.SysUserDao;
import com.dodoyd.moyu.admin.domain.SysUser;
import com.dodoyd.moyu.admin.service.SysUserService;
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
        if (request.getUsername() != null) {
            query.setUsername(request.getUsername());
        }
        if (request.getNickname() != null) {
            query.setNickname(request.getNickname());
        }
        if (request.getGender() != null) {
            query.setGender(request.getGender());
        }
        if (request.getAvatar() != null) {
            query.setAvatar(request.getAvatar());
        }
        if (request.getEmail() != null) {
            query.setEmail(request.getEmail());
        }
        if (request.getMobile() != null) {
            query.setMobile(request.getMobile());
        }
        if (request.getUserPwd() != null) {
            query.setUserPwd(request.getUserPwd());
        }
        if (request.getLoginIp() != null) {
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
        if (request.getCreateBy() != null) {
            query.setCreateBy(request.getCreateBy());
        }
        if (request.getUpdateBy() != null) {
            query.setUpdateBy(request.getUpdateBy());
        }
        if (request.getCreateTime() != null) {
            query.setCreateTime(request.getCreateTime());
        }
        if (request.getUpdateTime() != null) {
            query.setUpdateTime(request.getUpdateTime());
        }
        if (request.getRemark() != null) {
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
        if (request.getUsername() != null) {
            query.setUsername(request.getUsername());
        }
        if (request.getNickname() != null) {
            query.setNickname(request.getNickname());
        }
        if (request.getGender() != null) {
            query.setGender(request.getGender());
        }
        if (request.getAvatar() != null) {
            query.setAvatar(request.getAvatar());
        }
        if (request.getEmail() != null) {
            query.setEmail(request.getEmail());
        }
        if (request.getMobile() != null) {
            query.setMobile(request.getMobile());
        }
        if (request.getUserPwd() != null) {
            query.setUserPwd(request.getUserPwd());
        }
        if (request.getLoginIp() != null) {
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
        if (request.getCreateBy() != null) {
            query.setCreateBy(request.getCreateBy());
        }
        if (request.getUpdateBy() != null) {
            query.setUpdateBy(request.getUpdateBy());
        }
        if (request.getCreateTime() != null) {
            query.setCreateTime(request.getCreateTime());
        }
        if (request.getUpdateTime() != null) {
            query.setUpdateTime(request.getUpdateTime());
        }
        if (request.getRemark() != null) {
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
        if (request.getUsername() != null) {
            add.setUsername(request.getUsername());
        }
        if (request.getNickname() != null) {
            add.setNickname(request.getNickname());
        }
        if (request.getGender() != null) {
            add.setGender(request.getGender());
        }
        if (request.getAvatar() != null) {
            add.setAvatar(request.getAvatar());
        }
        if (request.getEmail() != null) {
            add.setEmail(request.getEmail());
        }
        if (request.getMobile() != null) {
            add.setMobile(request.getMobile());
        }
        if (request.getUserPwd() != null) {
            add.setUserPwd(request.getUserPwd());
        }
        if (request.getLoginIp() != null) {
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
        if (request.getCreateBy() != null) {
            add.setCreateBy(request.getCreateBy());
        }
        if (request.getUpdateBy() != null) {
            add.setUpdateBy(request.getUpdateBy());
        }
        if (request.getCreateTime() != null) {
            add.setCreateTime(request.getCreateTime());
        }
        if (request.getUpdateTime() != null) {
            add.setUpdateTime(request.getUpdateTime());
        }
        if (request.getRemark() != null) {
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
        if (request.getUsername() != null) {
            update.setUsername(request.getUsername());
        }
        if (request.getNickname() != null) {
            update.setNickname(request.getNickname());
        }
        if (request.getGender() != null) {
            update.setGender(request.getGender());
        }
        if (request.getAvatar() != null) {
            update.setAvatar(request.getAvatar());
        }
        if (request.getEmail() != null) {
            update.setEmail(request.getEmail());
        }
        if (request.getMobile() != null) {
            update.setMobile(request.getMobile());
        }
        if (request.getUserPwd() != null) {
            update.setUserPwd(request.getUserPwd());
        }
        if (request.getLoginIp() != null) {
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
        if (request.getCreateBy() != null) {
            update.setCreateBy(request.getCreateBy());
        }
        if (request.getUpdateBy() != null) {
            update.setUpdateBy(request.getUpdateBy());
        }
        if (request.getCreateTime() != null) {
            update.setCreateTime(request.getCreateTime());
        }
        if (request.getUpdateTime() != null) {
            update.setUpdateTime(request.getUpdateTime());
        }
        if (request.getRemark() != null) {
            update.setRemark(request.getRemark());
        }
        int row = sysUserDao.updateById(update);
        return row;
    }

}
