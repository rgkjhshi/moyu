package com.moyu.system.sys.controller;


import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.util.ObjectUtil;
import com.moyu.common.annotation.Log;
import com.moyu.common.model.BaseResponse;
import com.moyu.common.model.PageResult;
import com.moyu.system.sys.model.entity.SysOrg;
import com.moyu.system.sys.model.param.SysOrgParam;
import com.moyu.system.sys.service.SysOrgService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
@RequestMapping("/api/sys/org")
public class SysOrgController {

    @Resource
    private SysOrgService sysOrgService;

    /**
     * 分页获取组织列表
     */
    @PostMapping("/page")
    public BaseResponse<PageResult<SysOrg>> pageList(@RequestBody SysOrgParam orgParam) {
        Assert.isTrue(ObjectUtil.isAllNotEmpty(orgParam.getPageNum(), orgParam.getPageSize()), "分页参数pageNum,pageSize都不能为空");
        PageResult<SysOrg> page = sysOrgService.pageList(orgParam);
        return BaseResponse.getSuccessResponse(page);
    }

    /**
     * 获取组织树
     */
    @Log(jsonLog = true, response = false)
    @PostMapping("/tree")
    public BaseResponse<List<Tree<String>>> tree() {
        List<Tree<String>> list = sysOrgService.tree();
        return BaseResponse.getSuccessResponse(list);
    }

    /**
     * 获取详情
     */
    @PostMapping("/detail")
    public BaseResponse<SysOrg> detail(@RequestBody SysOrgParam orgParam) {
        Assert.isTrue(!ObjectUtil.isAllEmpty(orgParam.getId(), orgParam.getCode()), "id和code不能同时为空");
        return BaseResponse.getSuccessResponse(sysOrgService.detail(orgParam));
    }

    /**
     * 添加
     */
    @PostMapping("/add")
    public BaseResponse<String> add(@Validated @RequestBody SysOrgParam orgParam) {
        sysOrgService.add(orgParam);
        return BaseResponse.getSuccessResponse();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    public BaseResponse<String> delete(@RequestBody SysOrgParam orgParam) {
        Assert.notEmpty(orgParam.getIds(), "删除列表ids不能为空");
        sysOrgService.deleteByIds(orgParam);
        return BaseResponse.getSuccessResponse();
    }

    /**
     * 删除树,会集联删除
     */
    @PostMapping("/deleteTree")
    public BaseResponse<String> deleteTree(@RequestBody SysOrgParam orgParam) {
        Assert.notEmpty(orgParam.getCodes(), "删除列表codes不能为空");
        sysOrgService.deleteTree(orgParam);
        return BaseResponse.getSuccessResponse();
    }

    /**
     * 编辑
     */
    @PostMapping("/edit")
    public BaseResponse<String> edit(@Validated @RequestBody SysOrgParam orgParam) {
        Assert.isTrue(!ObjectUtil.isAllEmpty(orgParam.getId(), orgParam.getCode()), "id和code不能同时为空");
        sysOrgService.edit(orgParam);
        return BaseResponse.getSuccessResponse();
    }

}
