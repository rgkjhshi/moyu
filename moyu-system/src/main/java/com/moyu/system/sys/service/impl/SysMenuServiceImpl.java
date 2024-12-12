package com.moyu.system.sys.service.impl;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.lang.tree.parser.NodeParser;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moyu.common.enums.ExceptionEnum;
import com.moyu.common.exception.BaseException;
import com.moyu.common.model.PageResult;
import com.moyu.system.sys.mapper.SysMenuMapper;
import com.moyu.system.sys.model.entity.SysMenu;
import com.moyu.system.sys.model.param.SysMenuParam;
import com.moyu.system.sys.service.SysMenuService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;
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
        // 查询所有组织结构
        List<SysMenu> menuList = this.list(new LambdaQueryWrapper<SysMenu>()
                // 关键词搜索
                .like(StrUtil.isNotBlank(menuParam.getSearchKey()), SysMenu::getName, menuParam.getSearchKey())
                // 指定模块
                .eq(ObjectUtil.isNotEmpty(menuParam.getModule()), SysMenu::getModule, menuParam.getModule())
                .eq(SysMenu::getDeleteFlag, 0)
                .orderByAsc(SysMenu::getSortNum)
        );
        // 自定义转换器
        NodeParser<SysMenu, String> nodeParser = (menu, tree) -> {
            tree.setId(menu.getCode());
            tree.setName(menu.getName());
            tree.setParentId(menu.getParentCode());
            tree.setWeight(menu.getSortNum());
            // 扩展属性
            tree.put("menuType", menu.getMenuType());
            tree.put("path", menu.getPath());
            tree.put("component", menu.getComponent());
            tree.put("icon", menu.getIcon());
            tree.put("permission", menu.getPermission());
            tree.put("visible", menu.getVisible());
            tree.put("link", menu.getLink());
            tree.put("module", menu.getModule());
            tree.put("status", menu.getStatus());
            tree.put("remark", menu.getRemark());
            tree.put("createTime", menu.getCreateTime());
            tree.put("updateTime", menu.getUpdateTime());
            tree.put("createUser", menu.getCreateUser());
            tree.put("updateUser", menu.getUpdateUser());
        };
        // 构建树
        return TreeUtil.build(menuList, "0", TreeNodeConfig.DEFAULT_CONFIG, nodeParser);
    }

    @Override
    public List<SysMenu> list(SysMenuParam menuParam) {
        QueryWrapper<SysMenu> queryWrapper = new QueryWrapper<SysMenu>().checkSqlInjection();
        // 查询条件
        queryWrapper.lambda()
                // 查询部分字段
//                .select(SysMenu::getCode, SysMenu::getName, SysMenu::getSortNum)
                // 关键词搜索
                .like(StrUtil.isNotBlank(menuParam.getSearchKey()), SysMenu::getName, menuParam.getSearchKey())
                // 指定菜单类型
                .eq(ObjectUtil.isNotEmpty(menuParam.getMenuType()), SysMenu::getMenuType, menuParam.getMenuType())
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
                .eq(SysMenu::getDeleteFlag, 0)
                .orderByAsc(SysMenu::getSortNum);

        // 分页查询
        Page<SysMenu> page = new Page<>(menuParam.getPageNum(), menuParam.getPageSize());
        Page<SysMenu> menuPage = this.page(page, queryWrapper);
        return new PageResult<>(menuPage.getTotal(), menuPage.getRecords());
    }

    @Override
    public SysMenu detail(SysMenuParam menuParam) {
        SysMenu sysMenu = this.getById(Long.valueOf(menuParam.getId()));
        return sysMenu;
    }

    @Override
    public SysMenu add(SysMenuParam menuParam) {
        // 判断父节点(目录、菜单、按钮、外链的父节点都存在，只有模块的父节点不存在)
        if (!"0".equals(menuParam.getParentCode())) {
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
        menu.setId(null);
        // 唯一code IdUtil.objectId()24位
        menu.setCode(RandomUtil.randomString(10));
        this.save(menu);
        return null;
    }

    @Override
    public SysMenu delete(SysMenuParam menuParam) {
        // 要集联删除，子节点也要全部删除
        QueryWrapper<SysMenu> queryWrapper = new QueryWrapper<SysMenu>().checkSqlInjection();
        // 查询所有的菜单(包括目录、按钮等)
        queryWrapper.lambda()
                // 查询部分字段
                .select(SysMenu::getId, SysMenu::getCode, SysMenu::getParentCode)
                // 指定模块
                .eq(ObjectUtil.isNotEmpty(menuParam.getModule()), SysMenu::getModule, menuParam.getModule())
                .eq(SysMenu::getDeleteFlag, 0);
        // 所有的菜单
        List<SysMenu> menuList = this.list(queryWrapper);
        // 待删除的id集合
        Set<Long> idSet = menuParam.getIds().stream().map(Long::valueOf).collect(Collectors.toSet());
        // 待删除节点的code集合
        Set<String> codeSet = menuList.stream()
                .filter(menu -> idSet.contains(menu.getId()))
                .map(SysMenu::getCode)
                .collect(Collectors.toSet());
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
        return null;
    }

    @Override
    public SysMenu edit(SysMenuParam menuParam) {
        return null;
    }

    /**
     * SysMenuParam -> SysMenu
     */
    private SysMenu buildSysMenu(SysMenuParam menuParam) {
        if (menuParam == null) {
            return null;
        }
        SysMenu sysMenu = new SysMenu();
        if (menuParam.getId() != null) {
            sysMenu.setId(Long.valueOf(menuParam.getId()));
        }
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
}




