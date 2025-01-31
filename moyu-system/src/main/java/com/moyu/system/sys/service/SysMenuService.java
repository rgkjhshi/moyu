package com.moyu.system.sys.service;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.extension.service.IService;
import com.moyu.common.model.PageResult;
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
     * 菜单树,包含按钮(借助hutool的树结构)
     *
     * @param menuParam 查询条件(可指定module、status)
     * @return 菜单树List集合
     */
    List<Tree<String>> tree(SysMenuParam menuParam);

    /**
     * 获取菜单列表
     */
    List<SysMenu> list(SysMenuParam menuParam);

    /**
     * 分页获取菜单列表
     */
    PageResult<SysMenu> pageList(SysMenuParam menuParam);

    /**
     * 获取菜单详情
     */
    SysMenu detail(SysMenuParam menuParam);

    /**
     * 添加菜单
     */
    void add(SysMenuParam menuParam);

    /**
     * 通过ids删除，且不会集联删除
     */
    void deleteByIds(SysMenuParam menuParam);

    /**
     * 通过codes删除，会集联删除树的所有节点
     */
    void deleteTree(SysMenuParam menuParam);

    /**
     * 修改菜单
     */
    void edit(SysMenuParam menuParam);

    /**
     * 获取菜单树选择器(字段少，不包含按钮)
     *
     * @param menuParam 可指定module
     */
    List<Tree<String>> menuTreeSelector(SysMenuParam menuParam);

}
