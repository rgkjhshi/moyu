package com.dodoyd.moyu.admin.service.impl;

import com.dodoyd.moyu.admin.dao.SysUserRoleDao;
import com.dodoyd.moyu.admin.domain.SysUserRole;
import com.dodoyd.moyu.admin.service.SysUserRoleService;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.List;

/**
 * SysUserRole服务实现类
 *
 * @author moyusi
 * @since 2024-01-05
 */
@Service
public class SysUserRoleServiceImpl implements SysUserRoleService {

    @Resource
    private SysUserRoleDao sysUserRoleDao;

    @Override
    public SysUserRole querySysUserRole(SysUserRole request) {
        SysUserRole query = new SysUserRole();
        if(request.getId() != null) {
            query.setId(request.getId());
        }
        if(request.getUserId() != null) {
            query.setUserId(request.getUserId());
        }
        if(request.getRoleId() != null) {
            query.setRoleId(request.getRoleId());
        }
        SysUserRole sysUserRole = sysUserRoleDao.selectOne(query);
        return sysUserRole;
    }

    @Override
    public List<SysUserRole> querySysUserRoleList(SysUserRole request) {
        SysUserRole query = new SysUserRole();
        if(request.getId() != null) {
            query.setId(request.getId());
        }
        if(request.getUserId() != null) {
            query.setUserId(request.getUserId());
        }
        if(request.getRoleId() != null) {
            query.setRoleId(request.getRoleId());
        }
        List<SysUserRole> list = sysUserRoleDao.selectList(query);
        return list;
    }

    @Override
    public int addSysUserRole(SysUserRole request) {
        SysUserRole add = new SysUserRole();
        if(request.getId() != null) {
            add.setId(request.getId());
        }
        if(request.getUserId() != null) {
            add.setUserId(request.getUserId());
        }
        if(request.getRoleId() != null) {
            add.setRoleId(request.getRoleId());
        }
        int row = sysUserRoleDao.insert(add);
        return row;
    }

    @Override
    public int updateSysUserRole(SysUserRole request) {
        SysUserRole update = new SysUserRole();
        if(request.getId() != null) {
            update.setId(request.getId());
        }
        if(request.getUserId() != null) {
            update.setUserId(request.getUserId());
        }
        if(request.getRoleId() != null) {
            update.setRoleId(request.getRoleId());
        }
        int row = sysUserRoleDao.updateById(update);
        return row;
    }

}
