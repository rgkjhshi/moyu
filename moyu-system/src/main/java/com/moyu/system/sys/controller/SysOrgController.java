package com.moyu.system.sys.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.moyu.common.annotation.Log;
import com.moyu.common.model.BaseResponse;
import com.moyu.common.model.PageResult;
import com.moyu.common.web.model.Option;
import com.moyu.system.sys.model.entity.SysOrg;
import com.moyu.system.sys.model.param.SysOrgParam;
import com.moyu.system.sys.service.SysOrgService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/api/system/org")
public class SysOrgController {

    @Resource
    private SysOrgService sysOrgService;

    /**
     * 获取组织树下拉选项
     */
    @GetMapping("/tree")
    public BaseResponse<List<Option<?>>> treeList() {
        List<Option<?>> list = sysOrgService.listTreeOptions();
        return BaseResponse.getSuccessResponse(list);
    }

    /**
     * 分页获取组织列表
     */
    @PostMapping("/list")
    public BaseResponse<PageResult<SysOrg>> pageList(SysOrgParam sysOrgParam) {
        PageResult<SysOrg> page = sysOrgService.pageList(sysOrgParam);
        return BaseResponse.getSuccessResponse(page);
    }

}
