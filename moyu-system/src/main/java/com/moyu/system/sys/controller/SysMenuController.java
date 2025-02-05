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
import org.springframework.security.access.prepost.PreAuthorize;
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
    public BaseResponse<List<SysMenu>> list(@RequestBody SysMenuParam menuParam) {
        List<SysMenu> list = sysMenuService.list(menuParam);
        return BaseResponse.getSuccessResponse(list);
    }

    /**
     * 分页菜单列表
     */
    @PostMapping("/page")
    public BaseResponse<PageResult<SysMenu>> pageList(@RequestBody SysMenuParam menuParam) {
        Assert.isTrue(ObjectUtil.isAllNotEmpty(menuParam.getPageNum(), menuParam.getPageSize()), "分页参数pageNum,pageSize都不能为空");
        PageResult<SysMenu> list = sysMenuService.pageList(menuParam);
        return BaseResponse.getSuccessResponse(list);
    }

    /**
     * 获取菜单树(可指定module、status)
     */
    @Log(jsonLog = true, response = false)
    @PostMapping("/tree")
    public BaseResponse<List<Tree<String>>> tree(@RequestBody SysMenuParam menuParam) {
        List<Tree<String>> treeList = sysMenuService.tree(menuParam);
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
    @PreAuthorize("hasAuthority('sys:menu:add')")
    @PostMapping("/add")
    public BaseResponse<String> add(@RequestBody SysMenuParam menuParam) {
        sysMenuService.add(menuParam);
        return BaseResponse.getSuccessResponse();
    }

    /**
     * 删除菜单
     */
    @PreAuthorize("hasAuthority('sys:menu:delete')")
    @PostMapping("/delete")
    public BaseResponse<String> delete(@RequestBody SysMenuParam menuParam) {
        Assert.notEmpty(menuParam.getIds(), "删除列表ids不能为空");
        sysMenuService.deleteByIds(menuParam);
        return BaseResponse.getSuccessResponse();
    }

    /**
     * 删除菜单树,会集联删除
     */
    @PostMapping("/deleteTree")
    public BaseResponse<String> deleteTree(@RequestBody SysMenuParam menuParam) {
        Assert.notEmpty(menuParam.getCodes(), "删除列表codes不能为空");
        sysMenuService.deleteTree(menuParam);
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
    public BaseResponse<List<Tree<String>>> menuTreeSelector(@RequestBody SysMenuParam menuParam) {
        return BaseResponse.getSuccessResponse(sysMenuService.menuTreeSelector(menuParam));
    }

}
