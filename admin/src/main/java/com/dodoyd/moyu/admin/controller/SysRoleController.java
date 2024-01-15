package com.dodoyd.moyu.admin.controller;

import com.dodoyd.moyu.admin.domain.SysRole;
import com.dodoyd.moyu.admin.model.request.SysRoleRequest;
import com.dodoyd.moyu.admin.service.SysRoleService;
import com.dodoyd.moyu.common.annotation.Log;
import com.dodoyd.moyu.common.model.BaseResponse;
import com.dodoyd.moyu.common.model.PageResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * SysRoleController控制器
 *
 * @author moyusi
 * @since 2024-01-15
 */
@RestController
@RequestMapping("/api/sysRole")
public class SysRoleController {

    @Resource
    private SysRoleService sysRoleService;

    /**
     * 查询SysRole列表
     */
    @Log(response = false)
    @PostMapping(value = "/list")
    public BaseResponse<PageResult<SysRole>> queryList(@RequestBody SysRoleRequest request) {
        PageResult<SysRole> page = sysRoleService.querySysRoleList(request);
        return BaseResponse.getSuccessResponse(page);
    }

    /**
     * 查询SysRole详细信息
     */
    @Log(response = false)
    @GetMapping(value = "/get")
    public BaseResponse<SysRole> getInfo(Long id) {
        SysRole info = sysRoleService.querySysRoleById(id);
        return BaseResponse.getSuccessResponse(info);
    }

    /**
     * 查询SysRole详细信息
     */
    @Log(response = false)
    @PostMapping(value = "/query")
    public BaseResponse<SysRole> queryInfo(@RequestBody SysRoleRequest request) {
        SysRole info = sysRoleService.querySysRole(request);
        return BaseResponse.getSuccessResponse(info);
    }

    /**
     * 新增SysRole
     */
    @Log
    @PostMapping(value = "/add")
    public BaseResponse<?> add(@RequestBody SysRoleRequest request) {
        sysRoleService.addSysRole(request);
        return BaseResponse.getSuccessResponse();
    }

    /**
     * 修改SysRole
     */
    @Log
    @PostMapping(value = "/edit")
    public BaseResponse<?> edit(@RequestBody SysRoleRequest request) {
        sysRoleService.updateSysRole(request);
        return BaseResponse.getSuccessResponse();
    }

    /**
     * 删除SysRole
     */
    @Log
    @PostMapping(value = "/delete")
    public BaseResponse<?> delete(Long id) {
        sysRoleService.deleteSysRole(id);
        return BaseResponse.getSuccessResponse();
    }

    /**
     * 批量删除SysRole
     * 请求参数Key为idList, value为逗号分割的字符串，如:idList=1,2,3
     */
    @Log
    @PostMapping(value = "/batchDelete")
    public BaseResponse<?> batchDelete(@RequestParam("idList") List<Long> idList) {
        sysRoleService.batchDeleteSysRole(idList);
        return BaseResponse.getSuccessResponse();
    }
}
