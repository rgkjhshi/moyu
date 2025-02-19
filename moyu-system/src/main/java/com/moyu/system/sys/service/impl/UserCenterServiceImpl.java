package com.moyu.system.sys.service.impl;


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
import com.google.common.collect.Lists;
import com.moyu.common.security.util.SecurityUtils;
import com.moyu.system.sys.constant.SysConstants;
import com.moyu.system.sys.enums.MenuTypeEnum;
import com.moyu.system.sys.enums.OrgTypeEnum;
import com.moyu.system.sys.enums.StatusEnum;
import com.moyu.system.sys.model.entity.SysMenu;
import com.moyu.system.sys.model.entity.SysUser;
import com.moyu.system.sys.model.param.SysUserParam;
import com.moyu.system.sys.model.vo.UserInfo;
import com.moyu.system.sys.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author shisong
 * @since 2025-01-07
 */
@Slf4j
@Service
public class UserCenterServiceImpl implements UserCenterService {

    @Resource
    private SysUserService sysUserService;

    @Resource
    private SysMenuService sysMenuService;

    @Resource
    private SysOrgService sysOrgService;

    @Resource
    private SysGroupService sysGroupService;

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
        Set<String> permSet = sysRelationService.userPerm(account);

        // 查询所有可用的菜单(不含按钮)
        List<SysMenu> menuList = sysMenuService.list(new LambdaQueryWrapper<SysMenu>()
                // 不能已停用
                .ne(SysMenu::getStatus, StatusEnum.DISABLE.getCode())
                // 不能是按钮
                .ne(SysMenu::getMenuType, MenuTypeEnum.BUTTON.getCode())
                .eq(SysMenu::getDeleteFlag, 0)
                .orderByAsc(SysMenu::getSortNum)
        );
        // 用户有权限的菜单(不含按钮) + 所有模块、目录
        List<SysMenu> userMenuList = CollectionUtil.newArrayList();
        menuList.forEach(sysMenu -> {
            if (MenuTypeEnum.MODULE.getCode().equals(sysMenu.getMenuType())) {
                // path为空则设置为随机字符串
                if (ObjectUtil.isEmpty(sysMenu.getPath())) {
                    sysMenu.setPath(StrUtil.SLASH + RandomUtil.randomString(10));
                }
                userMenuList.add(sysMenu);
            } else if (MenuTypeEnum.DIR.getCode().equals(sysMenu.getMenuType())) {
                userMenuList.add(sysMenu);
            } else {
                // 菜单，有权限才添加
                if (permSet.contains(sysMenu.getCode())) {
                    userMenuList.add(sysMenu);
                }
            }
        });
        // 构建菜单路由树结构
        Tree<String> singleTree = buildMenuTree(userMenuList, SysConstants.ROOT_ID);
        // 移除空目录
        removeTreeNodes(singleTree, tree -> {
            Object menuType = ((Map<?, ?>) tree.get("meta")).get("type");
            return !tree.hasChild() && (MenuTypeEnum.MODULE.getCode().equals(menuType) || MenuTypeEnum.DIR.getCode().equals(menuType));
        });

        return singleTree.getChildren();
    }

    @Override
    public List<Tree<String>> userOrgTree(String account) {
        // 获取全部树
        Tree<String> tree = sysOrgService.singleTree(SysConstants.ROOT_ID);
//        // 获取用户所在分组
//        List<SysRelation> list = sysRelationService.list(SysRelationParam.builder()
//                .relationType(RelationTypeEnum.GROUP_HAS_USER.getCode()).targetId(account).build());
//        Set<String> groupSet = list.stream().map(SysRelation::getObjectId).collect(Collectors.toSet());
//        // 收集用户分组归属的org
//        Set<String> orgSet = new HashSet<>();
//        // 不为空则查询group所属的org
//        if (ObjectUtil.isNotEmpty(groupSet)) {
//            sysGroupService.list(SysGroupParam.builder().codeSet(groupSet).build()).forEach(group -> orgSet.add(group.getOrgCode()));
//        }
        // 查询用户信息
        SysUser user = sysUserService.detail(SysUserParam.builder().account(account).build());
        // 获取用户所属的最近一级公司组织code
        String orgCode = getOrgNode(tree, user);
        // 获取用户有权限的所有公司
        // 获取公司对应的tree
        return Lists.newArrayList(tree.getNode(orgCode));
    }

    /**
     * 构建菜单路由树结构(code, parentCode, children, weight, extra)
     *
     * @param menuList menu的非空字段构会放到树节点中
     * @param rootId   指定的根节点(从树中查找此rootId)
     * @return 返回以rootId为根的树，可能是子树或多棵树
     */
    private Tree<String> buildMenuTree(List<SysMenu> menuList, String rootId) {
        // 配置TreeNode使用指定的字段名
        TreeNodeConfig nodeConfig = new TreeNodeConfig();
        nodeConfig.setIdKey("code");
        nodeConfig.setParentIdKey("parentCode");
        // 结构转换
        List<TreeNode<String>> treeNodeList = menuList.stream()
                .map(menu -> {
                    TreeNode<String> node = new TreeNode<>(menu.getCode(), menu.getParentCode(), menu.getName(), menu.getSortNum());
                    // path、name、component、redirect、hidden
                    Map<String, Object> extra = new HashMap<>();//BeanUtil.beanToMap(menu, false, true);
                    extra.put("path", menu.getPath());
                    extra.put("component", menu.getComponent());
                    if (MenuTypeEnum.DIR.getCode().equals(menu.getMenuType())) {
                        extra.put("redirect", menu.getLink());
                    } else if (MenuTypeEnum.MODULE.getCode().equals(menu.getMenuType())) {
                        extra.put("redirect", menu.getLink());
                    }
                    Map<String, Object> meta = new HashMap<>();
                    meta.put("icon", menu.getIcon());
                    meta.put("title", menu.getName());
                    meta.put("type", menu.getMenuType());
                    // 如果设置了不可见，那么设置hidden
                    if (ObjectUtil.equal(menu.getVisible(), 0)) {
                        meta.put("hidden", true);
                    }
                    // 如果是超链接，设置url
                    if (MenuTypeEnum.LINK.getCode().equals(menu.getMenuType())) {
                        meta.put("url", menu.getPath());
                    }
                    extra.put("meta", meta);
                    node.setExtra(extra);
                    return node;
                }).collect(Collectors.toList());
        // 构建树
        return TreeUtil.buildSingle(treeNodeList, rootId, nodeConfig, new DefaultNodeParser<>());
    }

    // 按指定条件移除节点

    /**
     * 按指定条件移除节点
     */
    private static void removeTreeNodes(Tree<String> singleTree, Predicate<Tree<String>> condition) {
        if (ObjectUtil.isEmpty(singleTree.getChildren())) {
            return;
        }
        List<Tree<String>> removeList = new ArrayList<>();
        for (Tree<String> child : singleTree.getChildren()) {
            if (condition.test(child)) {
                removeList.add(child);
            } else {
                removeTreeNodes(child, condition);
            }
        }
        singleTree.getChildren().removeAll(removeList);
    }

    /**
     * 获取给定code的最近一级公司组织
     */
    private String getOrgNode(Tree<String> tree, SysUser user) {
        // 通过用户的orgChain获取用户的组织链接
        List<String> orgChainList = TreeUtil.getParentsId(tree.getNode(user.getOrgCode()), true);
        // 从前往后遍历，因组织链有顺序，所以遍历顺序不能变
        String orgCode = orgChainList.stream()
                .filter(code -> ObjectUtil.equal(OrgTypeEnum.ORG.getCode(), tree.getNode(code).get("orgType")))
                .findFirst().orElse(user.getOrgCode());
        return orgCode;
    }
}
