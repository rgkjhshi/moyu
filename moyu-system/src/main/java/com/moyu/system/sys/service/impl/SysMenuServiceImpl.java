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
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Strings;
import com.moyu.common.enums.ExceptionEnum;
import com.moyu.common.exception.BaseException;
import com.moyu.common.model.PageResult;
import com.moyu.system.sys.constant.SysConstants;
import com.moyu.system.sys.enums.ResourceTypeEnum;
import com.moyu.system.sys.enums.RelationTypeEnum;
import com.moyu.system.sys.enums.StatusEnum;
import com.moyu.system.sys.mapper.SysMenuMapper;
import com.moyu.system.sys.model.entity.SysResource;
import com.moyu.system.sys.model.entity.SysRelation;
import com.moyu.system.sys.model.param.SysMenuParam;
import com.moyu.system.sys.service.SysMenuService;
import com.moyu.system.sys.service.SysRelationService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author shisong
 * @description 针对表【sys_menu(菜单权限表)】的数据库操作Service实现
 * @createDate 2024-12-10 21:05:13
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysResource> implements SysMenuService {

    @Resource
    private SysRelationService sysRelationService;

    @Override
    public List<Tree<String>> tree(SysMenuParam menuParam) {
        // 查询条件(可指定module、status)
        SysMenuParam query = SysMenuParam.builder().module(menuParam.getModule()).status(menuParam.getStatus()).build();
        // 查询所有菜单
        List<SysResource> menuList = this.list(query);
        // 构建树中包含记录的所有字段
        String rootId = ObjectUtil.isEmpty(menuParam.getModule()) ? SysConstants.ROOT_NODE_ID : menuParam.getModule();
        return buildTree(menuList, rootId);
    }

    @Override
    public List<SysResource> list(SysMenuParam menuParam) {
        // 查询条件
        LambdaQueryWrapper<SysResource> queryWrapper = Wrappers.lambdaQuery(SysResource.class)
                // 关键词搜索
                .like(StrUtil.isNotBlank(menuParam.getSearchKey()), SysResource::getName, menuParam.getSearchKey())
                // 指定菜单类型
                .eq(ObjectUtil.isNotEmpty(menuParam.getMenuType()), SysResource::getMenuType, menuParam.getMenuType())
                // 指定模块
                .eq(ObjectUtil.isNotEmpty(menuParam.getModule()), SysResource::getModule, menuParam.getModule())
                // 指定状态
                .eq(ObjectUtil.isNotEmpty(menuParam.getStatus()), SysResource::getStatus, menuParam.getStatus())
                .eq(SysResource::getDeleteFlag, 0)
                .orderByAsc(SysResource::getSortNum);
        // 查询
        List<SysResource> menuList = this.list(queryWrapper);
        return menuList;
    }

    @Override
    public PageResult<SysResource> pageList(SysMenuParam menuParam) {
        QueryWrapper<SysResource> queryWrapper = new QueryWrapper<SysResource>().checkSqlInjection();
        // 查询条件
        queryWrapper.lambda()
                // 查询部分字段
//                .select(SysMenu::getCode, SysMenu::getName, SysMenu::getSortNum)
                // 关键词搜索
                .like(StrUtil.isNotBlank(menuParam.getSearchKey()), SysResource::getName, menuParam.getSearchKey())
                // 指定菜单类型
                .eq(ObjectUtil.isNotEmpty(menuParam.getMenuType()), SysResource::getMenuType, menuParam.getMenuType())
                // 指定模块
                .eq(ObjectUtil.isNotEmpty(menuParam.getModule()), SysResource::getModule, menuParam.getModule())
                // 指定状态
                .eq(ObjectUtil.isNotEmpty(menuParam.getStatus()), SysResource::getStatus, menuParam.getStatus())
                .eq(SysResource::getDeleteFlag, 0)
                .orderByAsc(SysResource::getSortNum);

        // 分页查询
        Page<SysResource> page = new Page<>(menuParam.getPageNum(), menuParam.getPageSize());
        Page<SysResource> menuPage = this.page(page, queryWrapper);
        return new PageResult<>(menuPage.getTotal(), menuPage.getRecords());
    }

    @Override
    public SysResource detail(SysMenuParam menuParam) {
        LambdaQueryWrapper<SysResource> queryWrapper = new QueryWrapper<SysResource>().checkSqlInjection().lambda()
                .eq(ObjectUtil.isNotEmpty(menuParam.getId()), SysResource::getId, menuParam.getId())
                .eq(ObjectUtil.isNotEmpty(menuParam.getCode()), SysResource::getCode, menuParam.getCode());
        // id、code均为唯一标识
        SysResource sysResource = this.getOne(queryWrapper);
        if (sysResource == null) {
            throw new BaseException(ExceptionEnum.INVALID_PARAMETER, "未查到指定数据");
        }
        return sysResource;
    }

    @Override
    public void add(SysMenuParam menuParam) {
        // 若指定了唯一编码code，则必须全局唯一
        if (!Strings.isNullOrEmpty(menuParam.getCode())) {
            // 查询指定code
            SysResource menu = this.getOne(new LambdaQueryWrapper<SysResource>()
                    .eq(SysResource::getCode, menuParam.getCode())
                    .eq(SysResource::getDeleteFlag, 0));
            if (menu != null) {
                throw new BaseException(ExceptionEnum.INVALID_PARAMETER, "唯一编码重复，请更换或留空自动生成");
            }
        }
        // 非root节点的parent必须存在(module为root节点)
        if (!Objects.equals(ResourceTypeEnum.MODULE.getCode(), menuParam.getMenuType())) {
            Assert.notEmpty(menuParam.getParentCode(), "上级菜单parentCode不能为空");
            // 查询所选父节点
            SysResource parentMenu = this.getOne(new LambdaQueryWrapper<SysResource>()
                    .eq(SysResource::getCode, menuParam.getParentCode())
                    .eq(SysResource::getDeleteFlag, 0));
            if (parentMenu == null) {
                throw new BaseException(ExceptionEnum.INVALID_PARAMETER, "指定的父节点不存在");
            }
            // 若上级菜单指定了module, 则子节点也必须一致
            if (parentMenu.getModule() != null && !parentMenu.getModule().equals(menuParam.getModule())) {
                throw new BaseException(ExceptionEnum.INVALID_PARAMETER, "与上级菜单module不一致");
            }
        }
        // 转换
        SysResource menu = buildSysMenu(menuParam);
        fillSysMenu(menu);
        menu.setId(null);
        // 若未指定唯一编码code，则自动生成
        if (Strings.isNullOrEmpty(menuParam.getCode())) {
            // 唯一code RandomUtil.randomString(10)、IdUtil.objectId()24位
            menu.setCode(IdUtil.objectId());
        }
        this.save(menu);
    }

    @Override
    public void deleteByIds(SysMenuParam menuParam) {
        // 待删除的id集合
        Set<Long> idSet = menuParam.getIds();
        // 逻辑删除
        UpdateWrapper<SysResource> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("id", idSet).set("delete_flag", 1);
        this.update(updateWrapper);
        // 资源删除时,对应的role_has_menu也要删除
        clearRoleMenu(idSet);
    }

    @Override
    public void deleteTree(SysMenuParam menuParam) {
        // 要集联删除，子节点也要全部删除
        QueryWrapper<SysResource> queryWrapper = new QueryWrapper<SysResource>().checkSqlInjection();
        // 查询所有的菜单(包括目录、按钮等)
        queryWrapper.lambda()
                // 查询部分字段
                .select(SysResource::getId, SysResource::getCode, SysResource::getParentCode)
                // 指定模块(有模块的情况下要过滤)
                .eq(ObjectUtil.isNotEmpty(menuParam.getModule()), SysResource::getModule, menuParam.getModule())
                .eq(SysResource::getDeleteFlag, 0);
        // 所有的菜单
        List<SysResource> menuList = this.list(queryWrapper);
        // 待删除节点的code集合
        Set<String> codeSet = menuParam.getCodes();

        // 待删除的id集合(先把指定节点加入集合)
        Set<Long> idSet = menuList.stream()
                .filter(menu -> codeSet.contains(menu.getCode()))
                .map(SysResource::getId)
                .collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(idSet)) {
            throw new BaseException(ExceptionEnum.INVALID_PARAMETER, "删除失败,未查到指定数据");
        }
        // 循环查找子节点,并加入到待删除集合
        while (!CollectionUtils.isEmpty(codeSet)) {
            Set<String> childrenSet = new HashSet<>();
            menuList.forEach(menu -> {
                if (codeSet.contains(menu.getParentCode())) {
                    childrenSet.add(menu.getCode());
                    idSet.add(menu.getId());
                }
            });
            // 子节点将变为新的父节点
            codeSet.clear();
            codeSet.addAll(childrenSet);
        }
        // 逻辑删除
        UpdateWrapper<SysResource> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("id", idSet).set("delete_flag", 1);
        this.update(updateWrapper);
        // 资源删除时,对应的role_has_menu也要删除
        clearRoleMenu(idSet);
    }

    @Override
    public void edit(SysMenuParam menuParam) {
        SysResource oldMenu = this.detail(menuParam);
        // 转换
        SysResource updateMenu = buildSysMenu(menuParam);
        fillSysMenu(updateMenu);
        updateMenu.setId(oldMenu.getId());
        this.updateById(updateMenu);
    }

    @Override
    public List<Tree<String>> menuTreeSelector(SysMenuParam menuParam) {
        // 查询所有菜单
        List<SysResource> menuList = this.list(new LambdaQueryWrapper<SysResource>()
                // 查询部分字段
                .select(SysResource::getCode, SysResource::getParentCode, SysResource::getName, SysResource::getSortNum, SysResource::getId)
                // 指定模块
                .eq(ObjectUtil.isNotEmpty(menuParam.getModule()), SysResource::getModule, menuParam.getModule())
                // 不能已停用
                .ne(SysResource::getStatus, StatusEnum.DISABLE.getCode())
                // 不能是按钮
                .ne(SysResource::getMenuType, ResourceTypeEnum.BUTTON.getCode())
                .eq(SysResource::getDeleteFlag, 0)
                .orderByAsc(SysResource::getSortNum)
        );
        // 构建的树中仅包含部分字段
        String rootId = ObjectUtil.isEmpty(menuParam.getModule()) ? SysConstants.ROOT_NODE_ID : menuParam.getModule();
        return buildTree(menuList, rootId);
    }

    /**
     * SysMenuParam -> SysMenu
     */
    private SysResource buildSysMenu(SysMenuParam menuParam) {
        if (menuParam == null) {
            return null;
        }
        SysResource sysResource = new SysResource();
        sysResource.setId(menuParam.getId());
        sysResource.setParentCode(menuParam.getParentCode());
        sysResource.setName(menuParam.getName());
        sysResource.setCode(menuParam.getCode());
        sysResource.setMenuType(menuParam.getMenuType());
        sysResource.setPath(menuParam.getPath());
        sysResource.setComponent(menuParam.getComponent());
        sysResource.setIcon(menuParam.getIcon());
        sysResource.setPermission(menuParam.getPermission());
        sysResource.setVisible(menuParam.getVisible());
        sysResource.setModule(menuParam.getModule());
        sysResource.setSortNum(menuParam.getSortNum());
        sysResource.setStatus(menuParam.getStatus());
        sysResource.setExtJson(menuParam.getExtJson());
        sysResource.setRemark(menuParam.getRemark());
        return sysResource;
    }

    /**
     * 根据menu的类型为某些字段填充默认值
     */
    private void fillSysMenu(SysResource menu) {
        Assert.notNull(menu, "菜单menu不能为空");
        // 菜单类型（字典 1模块 2目录 3菜单 4按钮 5外链）
        if (Objects.equals(ResourceTypeEnum.MODULE.getCode(), menu.getMenuType())) {
            // 模块的路径、组件、权限为空
            menu.setPath(menu.getPath());
            menu.setComponent("");
            menu.setPermission("");
        } else {
            // 非模块必须指定parentCode及module
            Assert.notEmpty(menu.getParentCode(), "上级菜单parentCode不能为空");
            Assert.notEmpty(menu.getModule(), "归属模块module不能为空");
        }
        if (Objects.equals(ResourceTypeEnum.DIR.getCode(), menu.getMenuType())) {
            // 目录的组件、权限为空
            Assert.notEmpty(menu.getPath(), "路由地址path不能为空");
            menu.setComponent(menu.getComponent());
            menu.setPermission("");
        } else if (Objects.equals(ResourceTypeEnum.MENU.getCode(), menu.getMenuType())) {
            Assert.notEmpty(menu.getPath(), "路由地址path不能为空");
            Assert.notEmpty(menu.getPath(), "组件component不能为空");
            // 菜单的权限为空
            menu.setPermission("");
        } else if (Objects.equals(ResourceTypeEnum.BUTTON.getCode(), menu.getMenuType())) {
            // 按钮的路径、组件为空，忽略可见性
            Assert.notEmpty(menu.getPermission(), "权限标识permission不能为空");
            menu.setPath("");
            menu.setComponent("");
        } else if (Objects.equals(ResourceTypeEnum.LINK.getCode(), menu.getMenuType())) {
            Assert.notEmpty(menu.getPath(), "路由地址path不能为空");
            Assert.isTrue(menu.getPath().startsWith("http"), "链接必须以http(s)开头");
            // 链接的组件、权限为空，忽略可见性
            menu.setComponent("");
            menu.setPermission("");
        }
    }

    /**
     * 构建树结构(code, parentCode, children, weight, extra)
     *
     * @param menuList menu的非空字段构会放到树节点中
     * @param rootId   指定的根节点(从树中查找此rootId)
     * @return 返回以rootId为根的树，可能是子树或多棵树
     */
    private List<Tree<String>> buildTree(List<SysResource> menuList, String rootId) {
        // 配置TreeNode使用指定的字段名
        TreeNodeConfig nodeConfig = new TreeNodeConfig();
        nodeConfig.setIdKey("code");
        nodeConfig.setParentIdKey("parentCode");
        // 结构转换
        List<TreeNode<String>> treeNodeList = menuList.stream()
                .map(menu -> {
                    TreeNode<String> node = new TreeNode<>(menu.getCode(), menu.getParentCode(), menu.getName(), menu.getSortNum());
                    node.setExtra(BeanUtil.beanToMap(menu, false, true));
                    return node;
                }).collect(Collectors.toList());
        // 构建树
        return TreeUtil.build(treeNodeList, rootId, nodeConfig, new DefaultNodeParser<>());
    }

    /**
     * 清除关系表中role_has_menu的指定的menu id的关系
     *
     * @param menuIds 指定的menu id集合
     */
    private void clearRoleMenu(Set<Long> menuIds) {
        if (ObjectUtil.isEmpty(menuIds)) {
            return;
        }
        // 查询出来menu对应的code
        Set<String> codeSet = new HashSet<>();
        this.list(Wrappers.lambdaQuery(SysResource.class).select(SysResource::getCode).in(SysResource::getId, menuIds))
                .forEach(e -> codeSet.add(e.getCode()));
        // 删除指定menuCode 的 ROLE_HAS_MENU
        sysRelationService.remove(Wrappers.lambdaQuery(SysRelation.class)
                .eq(SysRelation::getRelationType, RelationTypeEnum.ROLE_HAS_MENU)
                .in(SysRelation::getTargetId, codeSet));
    }
}




