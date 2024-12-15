package com.moyu.system.sys.controller;


import com.moyu.common.annotation.Log;
import com.moyu.common.model.BaseResponse;
import com.moyu.common.model.PageResult;
import com.moyu.system.sys.model.entity.SysRole;
import com.moyu.system.sys.model.param.SysRoleParam;
import com.moyu.system.sys.service.SysRoleService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 角色信息控制器
 *
 * @author shisong
 * @since 2024-12-15
 */
@Log(jsonLog = true)
@RestController
@RequestMapping("/api/sys/role")
public class SysRoleController {

    @Resource
    private SysRoleService sysRoleService;

    /**
     * 分页获取角色列表
     */
    @PostMapping("/page")
    public BaseResponse<PageResult<SysRole>> pageList(@RequestBody SysRoleParam sysRoleParam) {
        PageResult<SysRole> page = sysRoleService.pageList(sysRoleParam);
        return BaseResponse.getSuccessResponse(page);
    }

    /**
     * 获取详情
     */
    @PostMapping("/detail")
    public BaseResponse<SysRole> detail(@RequestBody SysRoleParam sysRoleParam) {
        return BaseResponse.getSuccessResponse(sysRoleService.detail(sysRoleParam));
    }

    /**
     * 添加
     */
    @PostMapping("/add")
    public BaseResponse<String> add(@RequestBody SysRoleParam sysRoleParam) {
        sysRoleService.add(sysRoleParam);
        return BaseResponse.getSuccessResponse();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    public BaseResponse<String> delete(@RequestBody SysRoleParam sysRoleParam) {
        sysRoleService.deleteByIds(sysRoleParam);
        return BaseResponse.getSuccessResponse();
    }

    /**
     * 编辑
     */
    @PostMapping("/edit")
    public BaseResponse<String> edit(@RequestBody SysRoleParam sysRoleParam) {
        sysRoleService.edit(sysRoleParam);
        return BaseResponse.getSuccessResponse();
    }

}
