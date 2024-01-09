package com.dodoyd.moyu.admin.service.impl;

import com.dodoyd.moyu.admin.dao.SysRoleDao;
import com.dodoyd.moyu.admin.dao.SysUserRoleDao;
import com.dodoyd.moyu.admin.domain.SysRole;
import com.dodoyd.moyu.admin.domain.SysUserRole;
import com.dodoyd.moyu.admin.model.request.SysRoleRequest;
import com.dodoyd.moyu.admin.service.SysRoleService;
import com.google.common.base.Strings;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * SysRole服务实现类
 *
 * @author moyusi
 * @since 2024-01-09
 */
@Service
public class SysRoleServiceImpl implements SysRoleService {

    @Resource
    private SysRoleDao sysRoleDao;

    @Resource
    private SysUserRoleDao sysUserRoleDao;

    @Override
    public List<SysRole> queryUserRoleList(Long userId) {
        SysUserRole queryUserRole = new SysUserRole();
        queryUserRole.setUserId(userId);
        List<SysUserRole> userRoleList = sysUserRoleDao.selectList(queryUserRole);
        if (CollectionUtils.isEmpty(userRoleList)) {
            return new ArrayList<>();
        }
        List<Long> idList = userRoleList.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
        return sysRoleDao.selectByIdList(idList);
    }

    @Override
    public SysRole querySysRoleById(Long id) {
        return sysRoleDao.selectById(id);
    }

    @Override
    public SysRole querySysRole(SysRoleRequest request) {
        SysRole query = new SysRole();
        if (request.getId() != null) {
            query.setId(request.getId());
        }
        if (!Strings.isNullOrEmpty(request.getRoleName())) {
            query.setRoleName(request.getRoleName());
        }
        if (!Strings.isNullOrEmpty(request.getRoleKey())) {
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
        SysRole sysRole = sysRoleDao.selectOne(query);
        return sysRole;
    }

    @Override
    public List<SysRole> querySysRoleList(SysRoleRequest request) {
        SysRole query = new SysRole();
        if (request.getId() != null) {
            query.setId(request.getId());
        }
        if (!Strings.isNullOrEmpty(request.getRoleName())) {
            query.setRoleName(request.getRoleName());
        }
        if (!Strings.isNullOrEmpty(request.getRoleKey())) {
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
        List<SysRole> list = sysRoleDao.selectList(query);
        return list;
    }

    @Override
    public int addSysRole(SysRoleRequest request) {
        SysRole add = new SysRole();
        if (request.getId() != null) {
            add.setId(request.getId());
        }
        if (!Strings.isNullOrEmpty(request.getRoleName())) {
            add.setRoleName(request.getRoleName());
        }
        if (!Strings.isNullOrEmpty(request.getRoleKey())) {
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
        int row = sysRoleDao.insert(add);
        return row;
    }

    @Override
    public int updateSysRole(SysRoleRequest request) {
        SysRole update = new SysRole();
        if (request.getId() != null) {
            update.setId(request.getId());
        }
        if (!Strings.isNullOrEmpty(request.getRoleName())) {
            update.setRoleName(request.getRoleName());
        }
        if (!Strings.isNullOrEmpty(request.getRoleKey())) {
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
        int row = sysRoleDao.updateById(update);
        return row;
    }

    @Override
    public int deleteSysRole(Long id) {
        int row = sysRoleDao.deleteById(id);
        return row;
    }

    @Override
    public int batchDeleteSysRole(List<Long> idList) {
        int row = sysRoleDao.deleteByIdList(idList);
        return row;
    }
}
