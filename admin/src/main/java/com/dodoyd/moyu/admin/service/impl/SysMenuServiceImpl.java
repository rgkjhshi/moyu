package com.dodoyd.moyu.admin.service.impl;

import com.dodoyd.moyu.admin.dao.SysMenuDao;
import com.dodoyd.moyu.admin.domain.SysMenu;
import com.dodoyd.moyu.admin.model.request.SysMenuRequest;
import com.dodoyd.moyu.admin.service.SysMenuService;
import com.dodoyd.moyu.common.model.PageResult;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.base.Strings;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * SysMenu服务实现类
 *
 * @author moyusi
 * @since 2024-01-12
 */
@Service
public class SysMenuServiceImpl implements SysMenuService {

    @Resource
    private SysMenuDao sysMenuDao;

    @Override
    public SysMenu querySysMenuById(Long id) {
        return sysMenuDao.selectById(id);
    }

    @Override
    public SysMenu querySysMenu(SysMenuRequest request) {
        SysMenu query = new SysMenu();
        if (request.getId() != null) {
            query.setId(request.getId());
        }
        if (!Strings.isNullOrEmpty(request.getMenuName())) {
            query.setMenuName(request.getMenuName());
        }
        if (request.getParentId() != null) {
            query.setParentId(request.getParentId());
        }
        if (request.getSortOrder() != null) {
            query.setSortOrder(request.getSortOrder());
        }
        if (!Strings.isNullOrEmpty(request.getMenuType())) {
            query.setMenuType(request.getMenuType());
        }
        if (!Strings.isNullOrEmpty(request.getPath())) {
            query.setPath(request.getPath());
        }
        if (!Strings.isNullOrEmpty(request.getComponent())) {
            query.setComponent(request.getComponent());
        }
        if (!Strings.isNullOrEmpty(request.getPerms())) {
            query.setPerms(request.getPerms());
        }
        if (!Strings.isNullOrEmpty(request.getIcon())) {
            query.setIcon(request.getIcon());
        }
        if (request.getHidden() != null) {
            query.setHidden(request.getHidden());
        }
        if (request.getLink() != null) {
            query.setLink(request.getLink());
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
        SysMenu sysMenu = sysMenuDao.selectOne(query);
        return sysMenu;
    }

    @Override
    public PageResult<SysMenu> querySysMenuList(SysMenuRequest request) {
        SysMenu query = new SysMenu();
        if (request.getId() != null) {
            query.setId(request.getId());
        }
        if (!Strings.isNullOrEmpty(request.getMenuName())) {
            query.setMenuName(request.getMenuName());
        }
        if (request.getParentId() != null) {
            query.setParentId(request.getParentId());
        }
        if (request.getSortOrder() != null) {
            query.setSortOrder(request.getSortOrder());
        }
        if (!Strings.isNullOrEmpty(request.getMenuType())) {
            query.setMenuType(request.getMenuType());
        }
        if (!Strings.isNullOrEmpty(request.getPath())) {
            query.setPath(request.getPath());
        }
        if (!Strings.isNullOrEmpty(request.getComponent())) {
            query.setComponent(request.getComponent());
        }
        if (!Strings.isNullOrEmpty(request.getPerms())) {
            query.setPerms(request.getPerms());
        }
        if (!Strings.isNullOrEmpty(request.getIcon())) {
            query.setIcon(request.getIcon());
        }
        if (request.getHidden() != null) {
            query.setHidden(request.getHidden());
        }
        if (request.getLink() != null) {
            query.setLink(request.getLink());
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
        PageHelper.startPage(request.getPageNum(), request.getPageSize());
        Page<SysMenu> page = (Page<SysMenu>) sysMenuDao.selectList(query);
        // 分页结果
        PageResult<SysMenu> pageResult = new PageResult<>();
        pageResult.setPageNum(page.getPageNum());
        pageResult.setTotal(page.getTotal());
        pageResult.setPageData(page.getResult());
        return pageResult;
    }

    @Override
    public int addSysMenu(SysMenuRequest request) {
        SysMenu add = new SysMenu();
        if (request.getId() != null) {
            add.setId(request.getId());
        }
        if (!Strings.isNullOrEmpty(request.getMenuName())) {
            add.setMenuName(request.getMenuName());
        }
        if (request.getParentId() != null) {
            add.setParentId(request.getParentId());
        }
        if (request.getSortOrder() != null) {
            add.setSortOrder(request.getSortOrder());
        }
        if (!Strings.isNullOrEmpty(request.getMenuType())) {
            add.setMenuType(request.getMenuType());
        }
        if (!Strings.isNullOrEmpty(request.getPath())) {
            add.setPath(request.getPath());
        }
        if (!Strings.isNullOrEmpty(request.getComponent())) {
            add.setComponent(request.getComponent());
        }
        if (!Strings.isNullOrEmpty(request.getPerms())) {
            add.setPerms(request.getPerms());
        }
        if (!Strings.isNullOrEmpty(request.getIcon())) {
            add.setIcon(request.getIcon());
        }
        if (request.getHidden() != null) {
            add.setHidden(request.getHidden());
        }
        if (request.getLink() != null) {
            add.setLink(request.getLink());
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
        int row = sysMenuDao.insert(add);
        return row;
    }

    @Override
    public int updateSysMenu(SysMenuRequest request) {
        SysMenu update = new SysMenu();
        if (request.getId() != null) {
            update.setId(request.getId());
        }
        if (!Strings.isNullOrEmpty(request.getMenuName())) {
            update.setMenuName(request.getMenuName());
        }
        if (request.getParentId() != null) {
            update.setParentId(request.getParentId());
        }
        if (request.getSortOrder() != null) {
            update.setSortOrder(request.getSortOrder());
        }
        if (!Strings.isNullOrEmpty(request.getMenuType())) {
            update.setMenuType(request.getMenuType());
        }
        if (!Strings.isNullOrEmpty(request.getPath())) {
            update.setPath(request.getPath());
        }
        if (!Strings.isNullOrEmpty(request.getComponent())) {
            update.setComponent(request.getComponent());
        }
        if (!Strings.isNullOrEmpty(request.getPerms())) {
            update.setPerms(request.getPerms());
        }
        if (!Strings.isNullOrEmpty(request.getIcon())) {
            update.setIcon(request.getIcon());
        }
        if (request.getHidden() != null) {
            update.setHidden(request.getHidden());
        }
        if (request.getLink() != null) {
            update.setLink(request.getLink());
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
        int row = sysMenuDao.updateById(update);
        return row;
    }

    @Override
    public int deleteSysMenu(Long id) {
        int row = sysMenuDao.deleteById(id);
        return row;
    }

    @Override
    public int batchDeleteSysMenu(List<Long> idList) {
        int row = sysMenuDao.deleteByIdList(idList);
        return row;
    }
}
