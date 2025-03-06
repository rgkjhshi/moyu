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
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.moyu.common.security.model.LoginUser;
import com.moyu.common.security.util.SecurityUtils;
import com.moyu.system.sys.constant.SysConstants;
import com.moyu.system.sys.enums.OrgTypeEnum;
import com.moyu.system.sys.enums.ResourceTypeEnum;
import com.moyu.system.sys.enums.StatusEnum;
import com.moyu.system.sys.model.entity.SysGroup;
import com.moyu.system.sys.model.entity.SysResource;
import com.moyu.system.sys.model.entity.SysRole;
import com.moyu.system.sys.model.entity.SysUser;
import com.moyu.system.sys.model.param.SysRoleParam;
import com.moyu.system.sys.model.param.SysUserParam;
import com.moyu.system.sys.model.vo.GroupInfo;
import com.moyu.system.sys.model.vo.UserInfo;
import com.moyu.system.sys.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
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
    private SysResourceService sysResourceService;

    @Resource
    private SysOrgService sysOrgService;

    @Resource
    private SysRoleService sysRoleService;

    @Resource
    private SysGroupService sysGroupService;

    @Resource
    private SysRelationService sysRelationService;

    @Override
    public UserInfo currentUserInfo(String username) {
        // 查询用户entity
        SysUser user = sysUserService.detail(SysUserParam.builder().account(username).build());
        // 当前登陆用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        // 构造用户信息视图对象
        UserInfo userInfo = UserInfo.builder().account(username)
                .name(user.getName()).nickName(user.getNickName()).avatar(user.getAvatar())
                .perms(loginUser.getPerms()).roles(loginUser.getRoles())
                .orgCode(loginUser.getOrgCode()).groupCode(loginUser.getGroupCode())
                .dataScope(loginUser.getDataScope()).scopes(loginUser.getScopes())
                .build();
        // 岗位列表
        List<SysGroup> groupList = sysGroupService.userGroupList(username);
        if (ObjectUtil.isNotEmpty(groupList)) {
            Tree<String> orgTree = sysOrgService.singleTree();
            List<GroupInfo> groupInfoList = new ArrayList<>();
            groupList.forEach(e -> {
                List<CharSequence> nameList = TreeUtil.getParentsName(orgTree.getNode(e.getOrgCode()), true);
                // nameList进行反转
                Collections.reverse(nameList);
                String fullName = Joiner.on("-").skipNulls().join(nameList);
                GroupInfo groupInfo = GroupInfo.builder().code(e.getCode()).name(e.getName())
                        .orgCode(e.getOrgCode()).orgName(e.getOrgName()).orgFullName(fullName).build();
                groupInfoList.add(groupInfo);
            });
            userInfo.setGroupInfoList(groupInfoList);
        }
        return userInfo;
    }

    @Override
    public List<Tree<String>> userMenu(String username) {
        // 用户有权限的资源code集合(含按钮)
        Set<String> permSet = sysRelationService.userMenu(username);

        // 查询所有可用的菜单(不含按钮)
        List<SysResource> allMenuList = sysResourceService.list(new LambdaQueryWrapper<SysResource>()
                // 不能已停用
                .ne(SysResource::getStatus, StatusEnum.DISABLE.getCode())
                // 不能是按钮
                .ne(SysResource::getResourceType, ResourceTypeEnum.BUTTON.getCode())
                .eq(SysResource::getDeleteFlag, 0)
                .orderByAsc(SysResource::getSortNum)
        );
        // 用户有权限的菜单(不含按钮) + 所有模块、目录
        List<SysResource> userMenuList = CollectionUtil.newArrayList();
        allMenuList.forEach(sysMenu -> {
            if (ResourceTypeEnum.MODULE.getCode().equals(sysMenu.getResourceType())) {
                // path为空则设置为随机字符串
                if (ObjectUtil.isEmpty(sysMenu.getPath())) {
                    sysMenu.setPath(StrUtil.SLASH + RandomUtil.randomString(10));
                }
                userMenuList.add(sysMenu);
            } else if (ResourceTypeEnum.DIR.getCode().equals(sysMenu.getResourceType())) {
                userMenuList.add(sysMenu);
            } else {
                // 菜单，有权限才添加
                if (permSet.contains(sysMenu.getCode())) {
                    userMenuList.add(sysMenu);
                }
            }
        });
        // 构建菜单路由树结构
        Tree<String> singleTree = buildMenuTree(userMenuList, SysConstants.ROOT_NODE_ID);

        // 移除空目录(只要有符合条件的子节点就保留)
        singleTree.filter(tree -> {
            // id=0或parentId=0均不符合要求(排除根和模块)
            if (SysConstants.ROOT_NODE_ID.equals(tree.getId()) || SysConstants.ROOT_NODE_ID.equals(tree.getParentId())) {
                return false;
            }
            if (ObjectUtil.isNotEmpty(tree.get("meta"))) {
                Map<String, Object> meta = (Map<String, Object>) tree.get("meta");
                Integer menuType = (Integer) meta.get("menuType");
                // 不是目录
                boolean notDir = !ResourceTypeEnum.DIR.getCode().equals(menuType) && !ResourceTypeEnum.MODULE.getCode().equals(menuType);
                // 有权限的菜单叶子节点才符合要求
                return notDir && permSet.contains(tree.getId());
            } else {
                return false;
            }
        });
        return singleTree.getChildren();
    }

    @Override
    public List<Tree<String>> userOrgTree(String username) {
        if (SecurityUtils.isRoot()) {
            return sysOrgService.tree();
        }
        // 获取全部树
        Tree<String> rootTree = sysOrgService.singleTree();
        // 查询用户信息
        SysUser user = sysUserService.detail(SysUserParam.builder().account(username).build());
        // 获取用户所属的最近一级公司组织code
        String orgCode = getUserCompanyCode(rootTree, user.getOrgCode());
        // 用户直属公司orgTree
        Tree<String> orgTree = rootTree.getNode(orgCode);
        // 用户公司树列表
        return Lists.newArrayList(orgTree);
    }

    @Override
    public List<SysRole> userRoleList(String username, String searchKey) {
        // 查询用户所有的角色列表
        Set<String> codeSet = sysRoleService.userAllRoles(username);
        return sysRoleService.list(SysRoleParam.builder().codeSet(codeSet).searchKey(searchKey).build());
    }

    /**
     * 构建菜单路由树结构(code, parentCode, children, weight, extra)
     *
     * @param menuList menu的非空字段构会放到树节点中
     * @param rootId   指定的根节点(从树中查找此rootId)
     * @return 返回以rootId为根的树，可能是子树或多棵树
     */
    private Tree<String> buildMenuTree(List<SysResource> menuList, String rootId) {
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
                    if (ResourceTypeEnum.DIR.getCode().equals(menu.getResourceType())) {
                        extra.put("redirect", menu.getLink());
                    } else if (ResourceTypeEnum.MODULE.getCode().equals(menu.getResourceType())) {
                        extra.put("redirect", menu.getLink());
                    }
                    Map<String, Object> meta = new HashMap<>();
                    meta.put("icon", menu.getIcon());
                    meta.put("title", menu.getName());
                    meta.put("menuType", menu.getResourceType());
                    // 如果设置了不可见，那么设置hidden
                    if (ObjectUtil.equal(menu.getVisible(), 0)) {
                        meta.put("hidden", true);
                    }
                    // 如果是超链接，设置url
                    if (ResourceTypeEnum.LINK.getCode().equals(menu.getResourceType())) {
                        meta.put("url", menu.getPath());
                    }
                    extra.put("meta", meta);
                    node.setExtra(extra);
                    return node;
                }).collect(Collectors.toList());
        // 构建树
        return TreeUtil.buildSingle(treeNodeList, rootId, nodeConfig, new DefaultNodeParser<>());
    }

    /**
     * 获取指定部门所属公司的orgCode
     */
    private String getUserCompanyCode(Tree<String> tree, String deptCode) {
        // 通过用户的orgPath获取用户的组织链接
        List<String> orgPathList = TreeUtil.getParentsId(tree.getNode(deptCode), true);
        // 从前往后遍历，因组织链有顺序，所以遍历顺序不能变
        String orgCode = orgPathList.stream()
                .filter(code -> ObjectUtil.equal(OrgTypeEnum.COMPANY.getCode(), tree.getNode(code).get("orgType")))
                .findFirst().orElse(deptCode);
        return orgCode;
    }


}
