package com.dodoyd.moyu.admin.service.impl;

import com.dodoyd.moyu.admin.dao.SysRoleDao;
import com.dodoyd.moyu.admin.domain.SysRole;
import com.dodoyd.moyu.admin.service.SysRoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * SysRole服务实现类
 *
 * @author moyusi
 * @since 2024-01-05
 */
@Service
public class SysRoleServiceImpl implements SysRoleService {

    @Resource
    private SysRoleDao sysRoleDao;

    @Override
    public SysRole querySysRole(SysRole request) {
        SysRole query = new SysRole();
        if (request.getId() != null) {
            query.setId(request.getId());
        }
        if (request.getRoleName() != null) {
            query.setRoleName(request.getRoleName());
        }
        if (request.getRoleKey() != null) {
            query.setRoleKey(request.getRoleKey());
        }
        if (request.getSortOrder() != null) {
            query.setSortOrder(request.getSortOrder());
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
        SysRole sysRole = sysRoleDao.selectOne(query);
        return sysRole;
    }

    @Override
    public List<SysRole> querySysRoleList(SysRole request) {
        SysRole query = new SysRole();
        if (request.getId() != null) {
            query.setId(request.getId());
        }
        if (request.getRoleName() != null) {
            query.setRoleName(request.getRoleName());
        }
        if (request.getRoleKey() != null) {
            query.setRoleKey(request.getRoleKey());
        }
        if (request.getSortOrder() != null) {
            query.setSortOrder(request.getSortOrder());
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
        List<SysRole> list = sysRoleDao.selectList(query);
        return list;
    }

    @Override
    public int addSysRole(SysRole request) {
        SysRole add = new SysRole();
        if (request.getId() != null) {
            add.setId(request.getId());
        }
        if (request.getRoleName() != null) {
            add.setRoleName(request.getRoleName());
        }
        if (request.getRoleKey() != null) {
            add.setRoleKey(request.getRoleKey());
        }
        if (request.getSortOrder() != null) {
            add.setSortOrder(request.getSortOrder());
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
        int row = sysRoleDao.insert(add);
        return row;
    }

    @Override
    public int updateSysRole(SysRole request) {
        SysRole update = new SysRole();
        if (request.getId() != null) {
            update.setId(request.getId());
        }
        if (request.getRoleName() != null) {
            update.setRoleName(request.getRoleName());
        }
        if (request.getRoleKey() != null) {
            update.setRoleKey(request.getRoleKey());
        }
        if (request.getSortOrder() != null) {
            update.setSortOrder(request.getSortOrder());
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
        int row = sysRoleDao.updateById(update);
        return row;
    }

}
