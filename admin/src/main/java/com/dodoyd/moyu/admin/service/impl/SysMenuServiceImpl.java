package com.dodoyd.moyu.admin.service.impl;

import com.dodoyd.moyu.admin.constant.Constants;
import com.dodoyd.moyu.admin.dao.SysMenuDao;
import com.dodoyd.moyu.admin.domain.SysMenu;
import com.dodoyd.moyu.admin.model.LoginUser;
import com.dodoyd.moyu.admin.model.request.SysMenuRequest;
import com.dodoyd.moyu.admin.model.vo.MetaVO;
import com.dodoyd.moyu.admin.model.vo.RouterVO;
import com.dodoyd.moyu.admin.service.LoginService;
import com.dodoyd.moyu.admin.service.SysMenuService;
import com.dodoyd.moyu.common.model.PageResult;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.base.Strings;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @Resource
    private LoginService loginService;

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

    @Override
    public List<RouterVO> queryMenuTree() {
        // 查询所有的菜单树
        List<SysMenu> list = sysMenuDao.selectValidMenuList();
        return buildMenuTree(list, 0L);
    }

    /**
     * 过滤菜单列表，根据父菜单ID筛选
     */
    private List<SysMenu> filterMenuList(List<SysMenu> list, Long parentId) {
        return list.stream().filter(sysMenu -> sysMenu.getParentId().equals(parentId)).collect(Collectors.toList());
    }

    /**
     * 构造菜单树(一层一层递归构造)
     */
    private List<RouterVO> buildMenuTree(List<SysMenu> list, Long parentId) {
        List<SysMenu> children = filterMenuList(list, parentId);
        // 无节点则返回
        if (CollectionUtils.isEmpty(children)) {
            return null;
        }
        List<RouterVO> routerList = new ArrayList<>();
        // 遍历本层所有节点,创建router, 并递归构建子层
        for (SysMenu menu : children) {
            RouterVO router = buildRouterVO(menu);
            // 递归构建菜单树的子层
            router.setChildren(buildMenuTree(list, menu.getId()));
            // 如果是目录且有子节点, 则设置显示
            if (Constants.MenuType.DIR.equals(menu.getMenuType()) && !CollectionUtils.isEmpty(router.getChildren())) {
                router.setAlwaysShow(true);
                router.setRedirect("noRedirect");
            }
            // 获取当前用户的权限
            List<String> userRoles = loginService.getRoles();
            // 如果所需权限不为空，则与当前用户权限取交集
            if (!CollectionUtils.isEmpty(router.getMeta().getRoles())) {
                List<String> retainList = new ArrayList<>(userRoles);
                retainList.retainAll(router.getMeta().getRoles());
                // 如果交集不为空，则将菜单添加到菜单树中。
                if (!CollectionUtils.isEmpty(retainList)) {
                    routerList.add(router);
                }
            } else {
                // 所需权限为空，直接将菜单添加到菜单树中。
                routerList.add(router);
            }
        }
        return routerList;
    }

    /**
     * 构建RouterVO对象(不包含children)
     */
    private RouterVO buildRouterVO(SysMenu menu) {
        RouterVO router = new RouterVO();
        router.setName(getRouteName(menu));
        router.setPath(getRouterPath(menu));
        router.setComponent(getComponent(menu));
        router.setHidden(menu.getHidden().equals(1));

        MetaVO meta = new MetaVO();
        meta.setTitle(menu.getMenuName());
        meta.setIcon(menu.getIcon());
        meta.setNoCache(true);
        // 资源所需权限
        List<String> roles = sysMenuDao.selectMenuRole(menu.getId());
        meta.setRoles(roles);
        // 链接需要跳转地址
        if (Constants.MenuType.LINK.equals(menu.getMenuType())) {
            meta.setLink(menu.getPath());
        }
        router.setMeta(meta);
        return router;
    }

    /**
     * 获取路由名称
     */
    public String getRouteName(SysMenu menu) {
        String routerName = null;
        // 如果不是链接
        if (!Constants.MenuType.LINK.equals(menu.getMenuType())) {
            routerName = StringUtils.capitalize(menu.getPath());
        }
        return routerName;
    }

    /**
     * 获取路由地址
     */
    public String getRouterPath(SysMenu menu) {
        String path = menu.getPath();
        // 如果不是链接
        if (!Constants.MenuType.LINK.equals(menu.getMenuType())) {
            path = "/" + path;
        }
        return path;
    }

    /**
     * 获取组件信息
     */
    public String getComponent(SysMenu menu) {
        String component = "";
        switch (menu.getMenuType()) {
            case Constants.MenuType.DIR:
                component = "Layout";
                break;
            case Constants.MenuType.MENU:
                component = menu.getComponent();
                break;
            case Constants.MenuType.LINK:
                component = null;
                break;
        }
        return component;
    }

}
