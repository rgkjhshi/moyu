package com.moyu.system.sys.service;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.extension.service.IService;
import com.moyu.system.sys.model.entity.SysMenu;
import com.moyu.system.sys.model.param.SysMenuParam;

import java.util.List;

/**
 * @author shisong
 * @description 针对表【sys_menu(菜单权限表)】的数据库操作Service
 * @createDate 2024-12-10 21:05:13
 */
public interface SysMenuService extends IService<SysMenu> {

    /**
     * 菜单树(借助hutool的树结构)
     *
     * @return 菜单树List集合
     */
    List<Tree<String>> tree();

    /**
     * 获取菜单列表
     */
    List<SysMenu> list(SysMenuParam menuParam);
}
