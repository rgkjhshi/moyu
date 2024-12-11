package com.moyu.system.sys.service.impl;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moyu.system.sys.mapper.SysMenuMapper;
import com.moyu.system.sys.model.entity.SysMenu;
import com.moyu.system.sys.model.param.SysMenuParam;
import com.moyu.system.sys.service.SysMenuService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @author shisong
 * @description 针对表【sys_menu(菜单权限表)】的数据库操作Service实现
 * @createDate 2024-12-10 21:05:13
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Override
    public List<Tree<String>> tree() {
        return Collections.emptyList();
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
}




