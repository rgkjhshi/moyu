package com.moyu.system.sys.service.impl;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.lang.tree.parser.NodeParser;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moyu.common.model.PageResult;
import com.moyu.system.sys.mapper.SysMenuMapper;
import com.moyu.system.sys.model.entity.SysMenu;
import com.moyu.system.sys.model.param.SysMenuParam;
import com.moyu.system.sys.service.SysMenuService;
import org.springframework.stereotype.Service;

import java.util.List;

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
}




