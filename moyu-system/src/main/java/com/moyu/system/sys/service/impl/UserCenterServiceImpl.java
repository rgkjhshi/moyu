package com.moyu.system.sys.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.lang.tree.parser.DefaultNodeParser;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.moyu.system.auth.security.util.SecurityUtils;
import com.moyu.system.sys.constant.SysConstants;
import com.moyu.system.sys.enums.MenuTypeEnum;
import com.moyu.system.sys.enums.StatusEnum;
import com.moyu.system.sys.model.entity.SysMenu;
import com.moyu.system.sys.model.entity.SysUser;
import com.moyu.system.sys.model.param.SysUserParam;
import com.moyu.system.sys.model.vo.UserInfo;
import com.moyu.system.sys.service.SysMenuService;
import com.moyu.system.sys.service.SysRelationService;
import com.moyu.system.sys.service.SysUserService;
import com.moyu.system.sys.service.UserCenterService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author shisong
 * @since 2025-01-07
 */
@Service
public class UserCenterServiceImpl implements UserCenterService {

    @Resource
    private SysUserService sysUserService;

    @Resource
    private SysMenuService sysMenuService;

    @Resource
    private SysRelationService sysRelationService;

    @Override
    public UserInfo currentUserInfo() {
        // 当前登陆用户username
        String username = SecurityUtils.getLoginUser().getUsername();
        // 查询用户entity
        SysUser user = sysUserService.detail(SysUserParam.builder().account(username).build());
        // 构造用户信息视图对象
        UserInfo userInfo = UserInfo.builder().account(username)
                .name(user.getName()).nickName(user.getNickName()).avatar(user.getAvatar()).build();
        // 角色集合
        Set<String> roles = SecurityUtils.getRoles();
        userInfo.setRoles(roles);
        // 权限集合
        if (ObjectUtil.isNotEmpty(roles)) {
            // TODO Set<String> perms = permissionService.getRolePermsFormCache(roles);
            Set<String> perms = SecurityUtils.getPerms();
            userInfo.setPerms(perms);
        }
        return userInfo;
    }

    @Override
    public List<Tree<String>> userMenu(String account) {
        // 用户有权限的菜单code集合(含按钮)
        Set<String> userMenuSet = sysRelationService.userMenu(account);

        // 查询所有可用的菜单(不含按钮)
        List<SysMenu> menuList = sysMenuService.list(new LambdaQueryWrapper<SysMenu>()
                // 不能已停用
                .ne(SysMenu::getStatus, StatusEnum.DISABLE.getCode())
                // 不能是按钮
                .ne(SysMenu::getMenuType, MenuTypeEnum.BUTTON.getCode())
                .eq(SysMenu::getDeleteFlag, 0)
        );
        // 用户有权限的菜单(不含按钮) + 所有模块、目录
        List<SysMenu> userMenuList = CollectionUtil.newArrayList();
        menuList.forEach(sysMenu -> {
            if (MenuTypeEnum.MODULE.getCode().equals(sysMenu.getMenuType())) {
                sysMenu.setPath(StrUtil.SLASH + RandomUtil.randomString(10));
                userMenuList.add(sysMenu);
            } else if (MenuTypeEnum.DIR.getCode().equals(sysMenu.getMenuType())) {
                userMenuList.add(sysMenu);
            } else {
                if (userMenuSet.contains(sysMenu.getCode())) {
                    userMenuList.add(sysMenu);
                }
            }
        });
        // 构建菜单路由树结构
        return buildMenuTree(userMenuList, SysConstants.ROOT_ID);
    }

    /**
     * 构建菜单路由树结构(code, parentCode, children, weight, extra)
     *
     * @param menuList menu的非空字段构会放到树节点中
     * @param rootId   指定的根节点(从树中查找此rootId)
     * @return 返回以rootId为根的树，可能是子树或多棵树
     */
    private List<Tree<String>> buildMenuTree(List<SysMenu> menuList, String rootId) {
        // 配置TreeNode使用指定的字段名
        TreeNodeConfig nodeConfig = new TreeNodeConfig();
        nodeConfig.setIdKey("code");
        nodeConfig.setParentIdKey("parentCode");
        // 结构转换
        List<TreeNode<String>> treeNodeList = menuList.stream()
                .map(menu -> {
                    TreeNode<String> node = new TreeNode<>(menu.getCode(), menu.getParentCode(), menu.getName(), menu.getSortNum());
                    Map<String, Object> extra = BeanUtil.beanToMap(menu, false, true);
                    extra.put("path", menu.getPath());
                    extra.put("component", menu.getComponent());
                    if (MenuTypeEnum.LINK.getCode().equals(menu.getMenuType())) {
                        extra.put("redirect", menu.getLink());
                    }
                    Map<String, Object> metaMap = new HashMap<>();
                    metaMap.put("icon", menu.getIcon());
                    metaMap.put("title", menu.getName());
                    metaMap.put("type", menu.getMenuType());
                    // 如果设置了不可见，那么设置hidden
                    if (ObjectUtil.equal(menu.getVisible(), 0)) {
                        metaMap.put("hidden", true);
                    }
                    extra.put("meta", metaMap);
                    node.setExtra(extra);
                    return node;
                }).collect(Collectors.toList());
        // 构建树
        return TreeUtil.build(treeNodeList, rootId, nodeConfig, new DefaultNodeParser<>());
    }
}
