package com.moyu.system.sys.service.impl;

import cn.hutool.core.bean.BeanUtil;
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
import com.moyu.common.enums.ExceptionEnum;
import com.moyu.common.exception.BaseException;
import com.moyu.common.model.PageResult;
import com.moyu.system.sys.enums.MenuTypeEnum;
import com.moyu.system.sys.enums.StatusEnum;
import com.moyu.system.sys.mapper.SysMenuMapper;
import com.moyu.system.sys.model.entity.SysMenu;
import com.moyu.system.sys.model.param.SysMenuParam;
import com.moyu.system.sys.service.SysMenuService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Override
    public List<Tree<String>> tree(SysMenuParam menuParam) {
        // 查询所有菜单
        List<SysMenu> menuList = this.list(new LambdaQueryWrapper<SysMenu>()
                // 指定模块
                .eq(ObjectUtil.isNotEmpty(menuParam.getModule()), SysMenu::getModule, menuParam.getModule())
                // 使用状态
                .eq(ObjectUtil.isNotEmpty(menuParam.getStatus()), SysMenu::getStatus, menuParam.getStatus())
                .eq(SysMenu::getDeleteFlag, 0)
                .orderByAsc(SysMenu::getSortNum)
        );
        // 构建树中包含记录的所有字段
        String rootId = ObjectUtil.isEmpty(menuParam.getModule()) ? "0" : menuParam.getModule();
        return buildTree(menuList, rootId);
    }

    @Override
    public List<SysMenu> list(SysMenuParam menuParam) {
        QueryWrapper<SysMenu> queryWrapper = new QueryWrapper<SysMenu>().checkSqlInjection();
        // 查询条件
        queryWrapper.lambda()
                // 关键词搜索
                .like(StrUtil.isNotBlank(menuParam.getSearchKey()), SysMenu::getName, menuParam.getSearchKey())
                // 指定菜单类型
                .eq(ObjectUtil.isNotEmpty(menuParam.getMenuType()), SysMenu::getMenuType, menuParam.getMenuType())
                // 指定模块
                .eq(ObjectUtil.isNotEmpty(menuParam.getModule()), SysMenu::getModule, menuParam.getModule())
                // 指定状态
                .eq(ObjectUtil.isNotEmpty(menuParam.getStatus()), SysMenu::getStatus, menuParam.getStatus())
                .eq(SysMenu::getDeleteFlag, 0)
                .orderByAsc(SysMenu::getSortNum);
        // 查询
        List<SysMenu> menuList = this.list(queryWrapper);
        return menuList;
    }

    @Override
    public PageResult<SysMenu> pageList(SysMenuParam menuParam) {
        QueryWrapper<SysMenu> queryWrapper = new QueryWrapper<SysMenu>().checkSqlInjection();
        // 查询条件
        queryWrapper.lambda()
                // 查询部分字段
//                .select(SysMenu::getCode, SysMenu::getName, SysMenu::getSortNum)
                // 关键词搜索
                .like(StrUtil.isNotBlank(menuParam.getSearchKey()), SysMenu::getName, menuParam.getSearchKey())
                // 指定菜单类型
                .eq(ObjectUtil.isNotEmpty(menuParam.getMenuType()), SysMenu::getMenuType, menuParam.getMenuType())
                // 指定模块
                .eq(ObjectUtil.isNotEmpty(menuParam.getModule()), SysMenu::getModule, menuParam.getModule())
                .eq(SysMenu::getDeleteFlag, 0)
                .orderByAsc(SysMenu::getSortNum);

        // 分页查询
        Page<SysMenu> page = new Page<>(menuParam.getPageNum(), menuParam.getPageSize());
        Page<SysMenu> menuPage = this.page(page, queryWrapper);
        return new PageResult<>(menuPage.getTotal(), menuPage.getRecords());
    }

    @Override
    public SysMenu detail(SysMenuParam menuParam) {
        LambdaQueryWrapper<SysMenu> queryWrapper = new QueryWrapper<SysMenu>().checkSqlInjection().lambda()
                .eq(ObjectUtil.isNotEmpty(menuParam.getId()), SysMenu::getId, menuParam.getId())
                .eq(ObjectUtil.isNotEmpty(menuParam.getCode()), SysMenu::getCode, menuParam.getCode());
        // id、code均为唯一标识
        SysMenu sysMenu = this.getOne(queryWrapper);
        if (sysMenu == null) {
            throw new BaseException(ExceptionEnum.INVALID_PARAMETER, "未查到指定数据");
        }
        return sysMenu;
    }

    @Override
    public void add(SysMenuParam menuParam) {
        // 若指定了唯一编码code，则必须全局唯一
        if (!Strings.isNullOrEmpty(menuParam.getCode())) {
            // 查询指定code
            SysMenu menu = this.getOne(new LambdaQueryWrapper<SysMenu>()
                    .eq(SysMenu::getCode, menuParam.getCode())
                    .eq(SysMenu::getDeleteFlag, 0));
            if (menu != null) {
                throw new BaseException(ExceptionEnum.INVALID_PARAMETER, "唯一编码重复，请更换或留空自动生成");
            }
        }
        // 非root节点的parent必须存在(module为root节点)
        if (!Objects.equals(MenuTypeEnum.MODULE.getCode(), menuParam.getMenuType())) {
            // 查询所选父节点
            SysMenu parentMenu = this.getOne(new LambdaQueryWrapper<SysMenu>()
                    .eq(SysMenu::getCode, menuParam.getParentCode())
                    .eq(SysMenu::getDeleteFlag, 0));
            if (parentMenu == null) {
                throw new BaseException(ExceptionEnum.INVALID_PARAMETER, "指定的父节点不存在");
            }
            // 若上级菜单指定了module, 则子节点也必须一致
            if (parentMenu.getModule() != null && !parentMenu.getModule().equals(menuParam.getModule())) {
                throw new BaseException(ExceptionEnum.INVALID_PARAMETER, "与上级菜单module不一致");
            }
        }
        // 不使用beanCopy是为了效率
        SysMenu menu = buildSysMenu(menuParam);
        fillEmptyByType(menu);
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
        UpdateWrapper<SysMenu> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("id", idSet).set("delete_flag", 1);
        this.update(updateWrapper);
    }

    @Override
    public void deleteTree(SysMenuParam menuParam) {
        // 要集联删除，子节点也要全部删除
        QueryWrapper<SysMenu> queryWrapper = new QueryWrapper<SysMenu>().checkSqlInjection();
        // 查询所有的菜单(包括目录、按钮等)
        queryWrapper.lambda()
                // 查询部分字段
                .select(SysMenu::getId, SysMenu::getCode, SysMenu::getParentCode)
                // 指定模块(有模块的情况下要过滤)
                .eq(ObjectUtil.isNotEmpty(menuParam.getModule()), SysMenu::getModule, menuParam.getModule())
                .eq(SysMenu::getDeleteFlag, 0);
        // 所有的菜单
        List<SysMenu> menuList = this.list(queryWrapper);
        // 待删除节点的code集合
        Set<String> codeSet = menuParam.getCodes();

        // 待删除的id集合(先把指定节点加入集合)
        Set<Long> idSet = menuList.stream()
                .filter(menu -> codeSet.contains(menu.getCode()))
                .map(SysMenu::getId)
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
        UpdateWrapper<SysMenu> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("id", idSet).set("delete_flag", 1);
        this.update(updateWrapper);
    }

    @Override
    public void edit(SysMenuParam menuParam) {
        SysMenu oldMenu = this.detail(menuParam);
        // 不使用beanCopy是为了效率
        SysMenu updateMenu = buildSysMenu(menuParam);
        fillEmptyByType(updateMenu);
        updateMenu.setId(oldMenu.getId());
        this.updateById(updateMenu);
    }

    @Override
    public List<Tree<String>> menuTreeSelector(SysMenuParam menuParam) {
        // 查询所有菜单
        List<SysMenu> menuList = this.list(new LambdaQueryWrapper<SysMenu>()
                // 查询部分字段
                .select(SysMenu::getCode, SysMenu::getParentCode, SysMenu::getName, SysMenu::getSortNum, SysMenu::getId)
                // 指定模块
                .eq(ObjectUtil.isNotEmpty(menuParam.getModule()), SysMenu::getModule, menuParam.getModule())
                // 不能已停用
                .ne(SysMenu::getStatus, StatusEnum.DISABLE.getCode())
                // 不能是按钮
                .ne(SysMenu::getMenuType, MenuTypeEnum.BUTTON.getCode())
                .eq(SysMenu::getDeleteFlag, 0)
                .orderByAsc(SysMenu::getSortNum)
        );
        // 构建的树中仅包含部分字段
        String rootId = ObjectUtil.isEmpty(menuParam.getModule()) ? "0" : menuParam.getModule();
        return buildTree(menuList, rootId);
    }

    /**
     * SysMenuParam -> SysMenu
     */
    private SysMenu buildSysMenu(SysMenuParam menuParam) {
        if (menuParam == null) {
            return null;
        }
        SysMenu sysMenu = new SysMenu();
        sysMenu.setId(menuParam.getId());
        sysMenu.setParentCode(menuParam.getParentCode());
        sysMenu.setName(menuParam.getName());
        sysMenu.setCode(menuParam.getCode());
        sysMenu.setMenuType(menuParam.getMenuType());
        sysMenu.setPath(menuParam.getPath());
        sysMenu.setComponent(menuParam.getComponent());
        sysMenu.setIcon(menuParam.getIcon());
        sysMenu.setPermission(menuParam.getPermission());
        sysMenu.setVisible(menuParam.getVisible());
        sysMenu.setModule(menuParam.getModule());
        sysMenu.setSortNum(menuParam.getSortNum());
        sysMenu.setStatus(menuParam.getStatus());
        sysMenu.setExtJson(menuParam.getExtJson());
        sysMenu.setRemark(menuParam.getRemark());
        return sysMenu;
    }

    /**
     * 根据menu的类型为某些字段填充空字符串
     */
    private void fillEmptyByType(SysMenu menu) {
        if (menu == null) {
            return;
        }
        // 菜单类型（字典 1模块 2目录 3菜单 4按钮 5外链）
        if (Objects.equals(MenuTypeEnum.MODULE.getCode(), menu.getMenuType())) {
            // 模块的路径、组件、权限为空
            menu.setPath("");
            menu.setComponent("");
            menu.setPermission("");
        } else if (Objects.equals(MenuTypeEnum.DIR.getCode(), menu.getMenuType())) {
            // 目录的组件、权限为空
            menu.setComponent("");
            menu.setPermission("");
        } else if (Objects.equals(MenuTypeEnum.MENU.getCode(), menu.getMenuType())) {
            // 菜单的权限为空
            menu.setPermission("");
        } else if (Objects.equals(MenuTypeEnum.BUTTON.getCode(), menu.getMenuType())) {
            // 按钮的路径、组件为空，忽略可见性
            menu.setPath("");
            menu.setComponent("");
        } else if (Objects.equals(MenuTypeEnum.LINK.getCode(), menu.getMenuType())) {
            // 链接的组件、权限为空，忽略可见性
            menu.setComponent("");
            menu.setPermission("");
        }
    }


    /**
     * 构建树结构(code, parentCode, children, weight, extra)
     */
    private List<Tree<String>> buildTree(List<SysMenu> menuList, String rootId) {
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
}




