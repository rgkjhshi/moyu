package com.moyu.system.sys.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.lang.tree.parser.DefaultNodeParser;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Strings;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.moyu.common.enums.ExceptionEnum;
import com.moyu.common.exception.BaseException;
import com.moyu.common.model.PageResult;
import com.moyu.system.sys.constant.SysConstants;
import com.moyu.system.sys.enums.MenuTypeEnum;
import com.moyu.system.sys.enums.RelationTypeEnum;
import com.moyu.system.sys.enums.StatusEnum;
import com.moyu.system.sys.mapper.SysRoleMapper;
import com.moyu.system.sys.model.entity.SysMenu;
import com.moyu.system.sys.model.entity.SysRelation;
import com.moyu.system.sys.model.entity.SysRole;
import com.moyu.system.sys.model.entity.SysUser;
import com.moyu.system.sys.model.param.SysMenuParam;
import com.moyu.system.sys.model.param.SysRelationParam;
import com.moyu.system.sys.model.param.SysRoleParam;
import com.moyu.system.sys.model.param.SysUserParam;
import com.moyu.system.sys.service.SysMenuService;
import com.moyu.system.sys.service.SysRelationService;
import com.moyu.system.sys.service.SysRoleService;
import com.moyu.system.sys.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author shisong
 * @description 针对表【sys_role(角色信息表)】的数据库操作Service实现
 * @createDate 2024-12-15 20:49:43
 */
@Slf4j
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Resource
    private TransactionTemplate transactionTemplate;

    @Resource
    private SysRelationService sysRelationService;

    @Resource
    private SysMenuService sysMenuService;

    @Resource
    private SysUserService sysUserService;

    @Override
    public List<SysRole> list(SysRoleParam roleParam) {
        QueryWrapper<SysRole> queryWrapper = new QueryWrapper<SysRole>().checkSqlInjection();
        // 查询条件
        queryWrapper.lambda()
                // 关键词搜索
                .like(StrUtil.isNotBlank(roleParam.getSearchKey()), SysRole::getName, roleParam.getSearchKey())
                // 指定code集合
                .in(ObjectUtil.isNotEmpty(roleParam.getCodeSet()), SysRole::getCode, roleParam.getCodeSet())
                // 指定模块
                .eq(ObjectUtil.isNotEmpty(roleParam.getModule()), SysRole::getModule, roleParam.getModule())
                // 指定状态
                .eq(ObjectUtil.isNotEmpty(roleParam.getStatus()), SysRole::getStatus, roleParam.getStatus())
                .eq(SysRole::getDeleteFlag, 0)
                .orderByAsc(SysRole::getSortNum);
        // 查询
        List<SysRole> roleList = this.list(queryWrapper);
        return roleList;
    }

    @Override
    public PageResult<SysRole> pageList(SysRoleParam roleParam) {
        QueryWrapper<SysRole> queryWrapper = new QueryWrapper<SysRole>().checkSqlInjection();
        // 查询条件
        queryWrapper.lambda()
                // 关键词搜索
                .like(StrUtil.isNotBlank(roleParam.getSearchKey()), SysRole::getName, roleParam.getSearchKey())
                // 指定模块
                .eq(ObjectUtil.isNotEmpty(roleParam.getModule()), SysRole::getModule, roleParam.getModule())
                // 指定状态
                .eq(ObjectUtil.isNotEmpty(roleParam.getStatus()), SysRole::getStatus, roleParam.getStatus())
                .eq(SysRole::getDeleteFlag, 0)
                .orderByAsc(SysRole::getSortNum);
        // 分页查询
        Page<SysRole> page = new Page<>(roleParam.getPageNum(), roleParam.getPageSize());
        Page<SysRole> rolePage = this.page(page, queryWrapper);
        return new PageResult<>(rolePage.getTotal(), rolePage.getRecords());
    }

    @Override
    public SysRole detail(SysRoleParam roleParam) {
        LambdaQueryWrapper<SysRole> queryWrapper = new QueryWrapper<SysRole>().checkSqlInjection().lambda()
                .eq(ObjectUtil.isNotEmpty(roleParam.getId()), SysRole::getId, roleParam.getId())
                .eq(ObjectUtil.isNotEmpty(roleParam.getCode()), SysRole::getCode, roleParam.getCode());
        // id、code均为唯一标识
        SysRole sysRole = this.getOne(queryWrapper);
        if (sysRole == null) {
            throw new BaseException(ExceptionEnum.INVALID_PARAMETER, "未查到指定数据");
        }
        return sysRole;
    }

    @Override
    public void add(SysRoleParam roleParam) {
        // 若指定了唯一编码code，则必须全局唯一
        if (!Strings.isNullOrEmpty(roleParam.getCode())) {
            // 查询指定code
            SysRole role = this.getOne(new LambdaQueryWrapper<SysRole>()
                    .eq(SysRole::getCode, roleParam.getCode())
                    .eq(SysRole::getDeleteFlag, 0));
            if (role != null) {
                throw new BaseException(ExceptionEnum.INVALID_PARAMETER, "唯一编码重复，请更换或留空自动生成");
            }
        }
        // 属性复制
        SysRole role = BeanUtil.copyProperties(roleParam, SysRole.class);
        role.setId(null);
        // 若未指定唯一编码code，则自动生成
        if (Strings.isNullOrEmpty(role.getCode())) {
            // 唯一code RandomUtil.randomString(10)、IdUtil.objectId()24位
            role.setCode(IdUtil.objectId());
        }
        this.save(role);
    }

    @Override
    public void deleteByIds(SysRoleParam roleParam) {
        // 待删除的id集合
        Set<Long> idSet = roleParam.getIds();
        // 逻辑删除
        UpdateWrapper<SysRole> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("id", idSet).set("delete_flag", 1);
        this.update(updateWrapper);
    }

    @Override
    public void edit(SysRoleParam roleParam) {
        SysRole oldRole = this.detail(roleParam);
        // 属性复制
        SysRole updateOrg = BeanUtil.copyProperties(roleParam, SysRole.class);
        updateOrg.setId(oldRole.getId());
        this.updateById(updateOrg);
    }

    @Override
    public List<Tree<String>> treeForGrant(SysRoleParam roleParam) {
        // 模块编码
        SysMenuParam query = SysMenuParam.builder().module(roleParam.getModule()).status(StatusEnum.ENABLE.getCode()).build();
        // 查询所有菜单
        List<SysMenu> menuList = sysMenuService.list(query);

        // 所有的role-menu关系(menu.code->menu)
        Map<String, SysRelation> rmMap = new HashMap<>();
        sysRelationService.list(new LambdaQueryWrapper<SysRelation>()
                        // 指定关系类型
                        .eq(SysRelation::getRelationType, RelationTypeEnum.ROLE_HAS_MENU.getCode())
                        // 指定哪个role
                        .eq(SysRelation::getObjectId, roleParam.getCode()))
                .forEach(e -> rmMap.put(e.getTargetId(), e));

        // 过滤出button，转为 parentCode->button 格式的的 multimap
        Multimap<String, SysMenu> allButtonMap = ArrayListMultimap.create();
        Multimap<String, String> grantButtonMap = HashMultimap.create();
        menuList.stream().filter(e -> MenuTypeEnum.BUTTON.getCode().equals(e.getMenuType()))
                .forEach(e -> {
                    allButtonMap.put(e.getParentCode(), e);
                    if (rmMap.containsKey(e.getCode())) {
                        grantButtonMap.put(e.getParentCode(), e.getCode());
                    }
                });

        // 过滤出menu转为treeNode
        List<TreeNode<String>> nodeList = new ArrayList<>();
        menuList.stream()
                .filter(e -> !MenuTypeEnum.BUTTON.getCode().equals(e.getMenuType()))
                .forEach(e -> {
                    TreeNode<String> node = new TreeNode<>(e.getCode(), e.getParentCode(), e.getName(), e.getSortNum());
                    Map<String, Object> extMap = new HashMap<>();
                    if (MenuTypeEnum.MODULE.getCode().equals(e.getMenuType())) {
                        // 模块只放图标
                        extMap.put("icon", e.getIcon());
                    } else {
                        extMap.put("menuType", e.getMenuType());
                        // rm关系中存在，表示有权限
                        extMap.put("checked", rmMap.containsKey(e.getCode()));
                        // 将把包含的按钮加进来
                        extMap.put("allButtonList", allButtonMap.get(e.getCode()));
                        extMap.put("grantButtonList", grantButtonMap.get(e.getCode()));
                    }
                    node.setExtra(extMap);
                    nodeList.add(node);
                });

        // 配置TreeNode使用指定的字段名
        TreeNodeConfig nodeConfig = new TreeNodeConfig();
        nodeConfig.setIdKey("code");
        nodeConfig.setParentIdKey("parentCode");
        // 指定rootId
        String rootId = ObjectUtil.isEmpty(roleParam.getModule()) ? SysConstants.ROOT_NODE_ID : roleParam.getModule();
        // 构建树
        return TreeUtil.build(nodeList, rootId, nodeConfig, new DefaultNodeParser<>());
    }

    @Override
    public void grantMenu(SysRoleParam roleParam) {
        // 查询指定模块的可授权内容(菜单、按钮、链接)
        QueryWrapper<SysMenu> queryWrapper = new QueryWrapper<SysMenu>().checkSqlInjection();
        queryWrapper.lambda().select(SysMenu::getCode)
                // 指定模块
                .eq(SysMenu::getModule, roleParam.getModule())
                // 指定菜单类型
                .in(SysMenu::getMenuType, MenuTypeEnum.MENU.getCode(), MenuTypeEnum.BUTTON.getCode(), MenuTypeEnum.LINK.getCode())
                .eq(SysMenu::getDeleteFlag, 0);
        List<SysMenu> menuList = sysMenuService.list(queryWrapper);
        // 本模块的所有权限
        List<String> allMenuCode = menuList.stream().map(SysMenu::getCode).collect(Collectors.toList());
        // 如果本模块无任何可用资源，则不用授权
        if (ObjectUtil.isEmpty(allMenuCode)) {
            return;
        }
        // 本次授权内容
        Set<String> grantMenuSet = roleParam.getGrantMenuList();
        // 本次授权内容中，仅保留可授权部分(目录不可授权)
        grantMenuSet.retainAll(allMenuCode);

        // 删除旧权限和添加新权限放在一个事务中，有异常会自动回滚(使用模板事物精确控制粒度)
        transactionTemplate.execute((transactionStatus) -> {
            // TransactionCallbackWithoutResult 有异常则会自动回滚

            // 清空角色在本模块的所有权限
            QueryWrapper<SysRelation> wrapper = new QueryWrapper<SysRelation>().checkSqlInjection();
            wrapper.lambda().eq(SysRelation::getObjectId, roleParam.getCode()).in(SysRelation::getTargetId, allMenuCode);
            sysRelationService.remove(wrapper);
            // 非空则新加权限
            if (ObjectUtil.isNotEmpty(grantMenuSet)) {
                List<SysRelation> addList = new ArrayList<>();
                grantMenuSet.forEach(code -> {
                    SysRelation relation = new SysRelation();
                    relation.setObjectId(roleParam.getCode());
                    relation.setTargetId(code);
                    relation.setRelationType(RelationTypeEnum.ROLE_HAS_MENU.getCode());
                    addList.add(relation);
                });
                sysRelationService.saveBatch(addList);
            }
            return null;
        });
    }

    @Override
    public void userGrantRole(SysRoleParam roleParam) {
        Assert.notEmpty(roleParam.getCode(), "角色code不能为空");
        // 待授权的用户集合
        Set<String> userSet = roleParam.getCodeSet();
        if (ObjectUtil.isEmpty(userSet)) {
            return;
        }
        // 查询该角色已拥有的用户
        Set<String> oldUserSet = new HashSet<>();
        sysRelationService.list(new LambdaQueryWrapper<SysRelation>()
                // 只查询user的code
                .select(SysRelation::getTargetId)
                // 关系类型
                .eq(SysRelation::getRelationType, RelationTypeEnum.ROLE_HAS_USER.getCode())
                // 指定role
                .eq(SysRelation::getObjectId, roleParam.getCode())
        ).forEach(e -> oldUserSet.add(e.getTargetId()));

        // 去除已有角色的用户
        userSet.removeAll(oldUserSet);
        // 无需新添加则返回
        if (ObjectUtil.isEmpty(userSet)) {
            return;
        }
        // 添加 ROLE_HAS_USER 关系
        List<SysRelation> addList = new ArrayList<>();
        userSet.forEach(code -> {
            SysRelation entity = new SysRelation();
            entity.setObjectId(roleParam.getCode());
            entity.setTargetId(code);
            entity.setRelationType(RelationTypeEnum.ROLE_HAS_USER.getCode());
            addList.add(entity);
        });
        sysRelationService.saveBatch(addList);
    }

    @Override
    public void userRevokeRole(SysRoleParam roleParam) {
        Assert.notEmpty(roleParam.getCode(), "角色coe不能为空");
        // 待撤销授权的用户集合
        Set<String> userSet = roleParam.getCodeSet();
        if (ObjectUtil.isEmpty(userSet)) {
            return;
        }
        // 要删除的ids
        Set<Long> ids = new HashSet<>();
        // 查询指定userSet中已存在的于指定role的user，加入ids待删
        sysRelationService.list(SysRelationParam.builder().objectId(roleParam.getCode()).targetSet(userSet)
                .relationType(RelationTypeEnum.ROLE_HAS_USER.getCode()).build()
        ).forEach(e -> ids.add(e.getId()));
        // 删除
        if (ObjectUtil.isNotEmpty(ids)) {
            sysRelationService.removeByIds(ids);
        }
    }

    @Override
    public List<SysUser> roleUserList(SysRoleParam roleParam) {
        // 查询指定role的所有user
        Set<String> userSet = sysRelationService.roleUser(roleParam.getCode());
        if (ObjectUtil.isEmpty(userSet)) {
            return new ArrayList<>();
        }
        // 查询用户(可指定搜索词)
        List<SysUser> userList = sysUserService.list(SysUserParam.builder()
                .searchKey(roleParam.getSearchKey())
                .orgCode(roleParam.getOrgCode())
                .codeSet(userSet).build());
        return userList;
    }
}




