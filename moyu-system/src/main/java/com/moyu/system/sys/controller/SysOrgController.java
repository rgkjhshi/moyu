package com.moyu.system.sys.controller;


import cn.hutool.core.lang.tree.Tree;
import com.moyu.common.annotation.Log;
import com.moyu.common.model.BaseResponse;
import com.moyu.common.model.PageResult;
import com.moyu.common.web.model.Option;
import com.moyu.system.sys.model.entity.SysOrg;
import com.moyu.system.sys.model.param.SysOrgParam;
import com.moyu.system.sys.service.SysOrgService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 组织机构控制器
 *
 * @author shisong
 * @since 2024-11-28
 */
@Log(jsonLog = true)
@RestController
@RequestMapping("/api/sys/org")
public class SysOrgController {

    @Resource
    private SysOrgService sysOrgService;

    /**
     * 分页获取组织列表
     */
    @PostMapping("/page")
    public BaseResponse<PageResult<SysOrg>> pageList(@RequestBody SysOrgParam sysOrgParam) {
        PageResult<SysOrg> page = sysOrgService.pageList(sysOrgParam);
        return BaseResponse.getSuccessResponse(page);
    }

    /**
     * 获取组织树
     */
    @Log(jsonLog = true, response = false)
    @GetMapping("/tree")
    public BaseResponse<List<Tree<String>>> tree() {
        List<Tree<String>> list = sysOrgService.tree();
        return BaseResponse.getSuccessResponse(list);
    }

    /**
     * 获取组织树下拉选项
     */
    @Log(jsonLog = true, response = false)
    @GetMapping("/treeList")
    public BaseResponse<List<Option<?>>> treeList() {
        List<Option<?>> list = sysOrgService.listTreeOptions();
        return BaseResponse.getSuccessResponse(list);
    }

    /**
     * 获取详情
     */
    @PostMapping("/detail")
    public BaseResponse<SysOrg> detail(@RequestBody SysOrgParam sysOrgParam) {
        return BaseResponse.getSuccessResponse(sysOrgService.detail(sysOrgParam));
    }

    /**
     * 添加
     */
    @PostMapping("/add")
    public BaseResponse<String> add(@RequestBody SysOrgParam sysOrgParam) {
        sysOrgService.add(sysOrgParam);
        return BaseResponse.getSuccessResponse();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    public BaseResponse<String> delete(@RequestBody SysOrgParam sysOrgParam) {
        sysOrgService.deleteByIds(sysOrgParam);
        return BaseResponse.getSuccessResponse();
    }

    /**
     * 删除树,会集联删除
     */
    @PostMapping("/deleteTree")
    public BaseResponse<String> deleteTree(@RequestBody SysOrgParam sysOrgParam) {
        sysOrgService.deleteByCodes(sysOrgParam);
        return BaseResponse.getSuccessResponse();
    }

    /**
     * 编辑
     */
    @PostMapping("/edit")
    public BaseResponse<String> edit(@RequestBody SysOrgParam sysOrgParam) {
        sysOrgService.edit(sysOrgParam);
        return BaseResponse.getSuccessResponse();
    }

}
