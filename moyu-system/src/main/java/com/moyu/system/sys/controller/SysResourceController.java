package com.moyu.system.sys.controller;


import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.util.ObjectUtil;
import com.moyu.common.annotation.Log;
import com.moyu.common.model.BaseResponse;
import com.moyu.common.model.PageResult;
import com.moyu.system.sys.model.entity.SysResource;
import com.moyu.system.sys.model.param.SysResourceParam;
import com.moyu.system.sys.service.SysResourceService;
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
@RequestMapping("/api/sys/resource")
public class SysResourceController {


    @Resource
    private SysResourceService sysResourceService;

    /**
     * 资源列表
     */
    @PostMapping("/list")
    public BaseResponse<List<SysResource>> list(@RequestBody SysResourceParam menuParam) {
        List<SysResource> list = sysResourceService.list(menuParam);
        return BaseResponse.getSuccessResponse(list);
    }

    /**
     * 资源分页列表
     */
    @PostMapping("/page")
    public BaseResponse<PageResult<SysResource>> pageList(@RequestBody SysResourceParam menuParam) {
        Assert.isTrue(ObjectUtil.isAllNotEmpty(menuParam.getPageNum(), menuParam.getPageSize()), "分页参数pageNum,pageSize都不能为空");
        PageResult<SysResource> list = sysResourceService.pageList(menuParam);
        return BaseResponse.getSuccessResponse(list);
    }

    /**
     * 获取资源树(可指定module、status)
     */
    @Log(jsonLog = true, response = false)
    @PostMapping("/tree")
    public BaseResponse<List<Tree<String>>> tree(@RequestBody SysResourceParam menuParam) {
        List<Tree<String>> treeList = sysResourceService.tree(menuParam);
        return BaseResponse.getSuccessResponse(treeList);
    }

    /**
     * 获取资源详情
     */
    @PostMapping("/detail")
    public BaseResponse<SysResource> detail(@RequestBody SysResourceParam menuParam) {
        Assert.isTrue(!ObjectUtil.isAllEmpty(menuParam.getId(), menuParam.getCode()), "id和code不能同时为空");
        return BaseResponse.getSuccessResponse(sysResourceService.detail(menuParam));
    }

    /**
     * 添加资源
     */
    @PreAuthorize("hasAuthority('sys:menu:add')")
    @PostMapping("/add")
    public BaseResponse<String> add(@RequestBody SysResourceParam menuParam) {
        sysResourceService.add(menuParam);
        return BaseResponse.getSuccessResponse();
    }

    /**
     * 删除资源
     */
    @PreAuthorize("hasAuthority('sys:menu:delete')")
    @PostMapping("/delete")
    public BaseResponse<String> delete(@RequestBody SysResourceParam menuParam) {
        Assert.notEmpty(menuParam.getIds(), "删除列表ids不能为空");
        sysResourceService.deleteByIds(menuParam);
        return BaseResponse.getSuccessResponse();
    }

    /**
     * 删除资源树,会集联删除
     */
    @PostMapping("/deleteTree")
    public BaseResponse<String> deleteTree(@RequestBody SysResourceParam menuParam) {
        Assert.notEmpty(menuParam.getCodes(), "删除列表codes不能为空");
        sysResourceService.deleteTree(menuParam);
        return BaseResponse.getSuccessResponse();
    }

    /**
     * 编辑资源
     */
    @PostMapping("/edit")
    public BaseResponse<String> edit(@RequestBody SysResourceParam menuParam) {
        Assert.isTrue(!ObjectUtil.isAllEmpty(menuParam.getId(), menuParam.getCode()), "id和code不能同时为空");
        sysResourceService.edit(menuParam);
        return BaseResponse.getSuccessResponse();
    }

    /**
     * 获取菜单树选择器
     */
    @PostMapping("/menuTreeSelector")
    public BaseResponse<List<Tree<String>>> menuTreeSelector(@RequestBody SysResourceParam menuParam) {
        return BaseResponse.getSuccessResponse(sysResourceService.menuTreeSelector(menuParam));
    }

}
