package com.dodoyd.moyu.admin.service;

import com.dodoyd.moyu.admin.domain.SysMenu;
import com.dodoyd.moyu.admin.model.request.SysMenuRequest;
import com.dodoyd.moyu.common.model.PageResult;

import java.util.List;

/**
 * SysMenu服务
 *
 * @author moyusi
 * @since 2024-01-12
 */
public interface SysMenuService {

    /**
     * 通过主键查询SysMenu
     *
     * @param id 主键
     * @return SysMenu
     */
    SysMenu querySysMenuById(Long id);

    /**
     * 查询SysMenu
     *
     * @param request 查询请求
     * @return SysMenu
     */
    SysMenu querySysMenu(SysMenuRequest request);

    /**
     * 查询SysMenu列表
     *
     * @param request 查询请求
     * @return SysMenu列表
     */
    PageResult<SysMenu> querySysMenuList(SysMenuRequest request);

    /**
     * 新增SysMenu
     *
     * @param request 请求参数
     * @return 结果
     */
    int addSysMenu(SysMenuRequest request);

    /**
     * 修改SysMenu
     *
     * @param request 请求参数
     * @return 结果
     */
    int updateSysMenu(SysMenuRequest request);

    /**
     * 删除SysMenu
     *
     * @param id 主键
     * @return 结果
     */
    int deleteSysMenu(Long id);

    /**
     * 批量删除SysMenu
     *
     * @param idList 主键列表
     * @return 结果
     */
    int batchDeleteSysMenu(List<Long> idList);

}

