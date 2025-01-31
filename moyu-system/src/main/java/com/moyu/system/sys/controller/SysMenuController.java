package com.moyu.system.sys.controller;


import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.util.ObjectUtil;
import com.moyu.common.annotation.Log;
import com.moyu.common.model.BaseResponse;
import com.moyu.common.model.PageResult;
import com.moyu.system.sys.model.entity.SysMenu;
import com.moyu.system.sys.model.param.SysMenuParam;
import com.moyu.system.sys.service.SysMenuService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 资源菜单权限控制器
 *
 * @author shisong
 * @since 2024-12-11
 */
@Log(jsonLog = true)
@RestController
@RequestMapping("/api/sys/menu")
public class SysMenuController {


    @Resource
    private SysMenuService sysMenuService;

    /**
     * 菜单列表
     */
    @PostMapping("/list")
    public BaseResponse<List<SysMenu>> list(@RequestBody SysMenuParam sysMenuParam) {
        List<SysMenu> list = sysMenuService.list(sysMenuParam);
        return BaseResponse.getSuccessResponse(list);
    }

    /**
     * 分页菜单列表
     */
    @PostMapping("/page")
    public BaseResponse<PageResult<SysMenu>> pageList(@RequestBody SysMenuParam sysMenuParam) {
        PageResult<SysMenu> list = sysMenuService.pageList(sysMenuParam);
        return BaseResponse.getSuccessResponse(list);
    }

    /**
     * 获取菜单树
     */
    @Log(jsonLog = true, response = false)
    @PostMapping("/tree")
    public BaseResponse<List<Tree<String>>> tree(@RequestBody SysMenuParam sysMenuParam) {
        List<Tree<String>> treeList = sysMenuService.tree(sysMenuParam);
        return BaseResponse.getSuccessResponse(treeList);
    }

    /**
     * 获取菜单详情
     */
    @PostMapping("/detail")
    public BaseResponse<SysMenu> detail(@RequestBody SysMenuParam menuParam) {
        Assert.isTrue(!ObjectUtil.isAllEmpty(menuParam.getId(), menuParam.getCode()), "id和code不能同时为空");
        return BaseResponse.getSuccessResponse(sysMenuService.detail(menuParam));
    }

    /**
     * 添加菜单
     */
    @PostMapping("/add")
    public BaseResponse<String> add(@RequestBody SysMenuParam sysMenuParam) {
        sysMenuService.add(sysMenuParam);
        return BaseResponse.getSuccessResponse();
    }

    /**
     * 删除菜单
     */
    @PostMapping("/delete")
    public BaseResponse<String> delete(@RequestBody SysMenuParam sysMenuParam) {
        sysMenuService.deleteByIds(sysMenuParam);
        return BaseResponse.getSuccessResponse();
    }

    /**
     * 删除菜单树,会集联删除
     */
    @PostMapping("/deleteTree")
    public BaseResponse<String> deleteTree(@RequestBody SysMenuParam sysMenuParam) {
        sysMenuService.deleteTree(sysMenuParam);
        return BaseResponse.getSuccessResponse();
    }

    /**
     * 编辑菜单
     */
    @PostMapping("/edit")
    public BaseResponse<String> edit(@RequestBody SysMenuParam menuParam) {
        Assert.isTrue(!ObjectUtil.isAllEmpty(menuParam.getId(), menuParam.getCode()), "id和code不能同时为空");
        sysMenuService.edit(menuParam);
        return BaseResponse.getSuccessResponse();
    }

    /**
     * 获取菜单树选择器
     */
    @PostMapping("/treeSelector")
    public BaseResponse<List<Tree<String>>> menuTreeSelector(@RequestBody SysMenuParam sysMenuParam) {
        return BaseResponse.getSuccessResponse(sysMenuService.menuTreeSelector(sysMenuParam));
    }

}
