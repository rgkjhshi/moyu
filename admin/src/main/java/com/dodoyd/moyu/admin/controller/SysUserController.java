package com.dodoyd.moyu.admin.controller;

import com.dodoyd.moyu.admin.domain.SysUser;
import com.dodoyd.moyu.admin.model.request.SysUserRequest;
import com.dodoyd.moyu.admin.service.SysUserService;
import com.dodoyd.moyu.common.annotation.Log;
import com.dodoyd.moyu.common.model.BaseResponse;
import com.dodoyd.moyu.common.model.PageResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * SysUserController控制器
 *
 * @author moyusi
 * @since 2024-01-09
 */
@RestController
@RequestMapping("/api/sysUser")
public class SysUserController {

    @Resource
    private SysUserService sysUserService;

    /**
     * 查询SysUser列表
     */
    @Log(response = false)
    @PostMapping(value = "/list")
    public BaseResponse<PageResult<SysUser>> queryList(@RequestBody SysUserRequest request) {
        PageResult<SysUser> page = sysUserService.querySysUserList(request);
        return BaseResponse.getSuccessResponse(page);
    }

    /**
     * 查询SysUser详细信息
     */
    @Log(response = false)
    @GetMapping(value = "/get")
    public BaseResponse<SysUser> getInfo(Long userId) {
        SysUser info = sysUserService.querySysUserByUserId(userId);
        return BaseResponse.getSuccessResponse(info);
    }

    /**
     * 查询SysUser详细信息
     */
    @Log(response = false)
    @PostMapping(value = "/query")
    public BaseResponse<SysUser> queryInfo(@RequestBody SysUserRequest request) {
        SysUser info = sysUserService.querySysUser(request);
        return BaseResponse.getSuccessResponse(info);
    }

    /**
     * 新增SysUser
     */
    @Log
    @PostMapping(value = "/add")
    public BaseResponse<?> add(@RequestBody SysUserRequest request) {
        sysUserService.addSysUser(request);
        return BaseResponse.getSuccessResponse();
    }

    /**
     * 修改SysUser
     */
    @Log
    @PostMapping(value = "/edit")
    public BaseResponse<?> edit(@RequestBody SysUserRequest request) {
        sysUserService.updateSysUser(request);
        return BaseResponse.getSuccessResponse();
    }

    /**
     * 删除SysUser
     */
    @Log
    @PostMapping(value = "/delete")
    public BaseResponse<?> delete(Long userId) {
        sysUserService.deleteSysUser(userId);
        return BaseResponse.getSuccessResponse();
    }

    /**
     * 批量删除SysUser
     * 请求参数Key为userIdList, value为逗号分割的字符串，如:idList=1,2,3
     */
    @Log
    @PostMapping(value = "/batchDelete")
    public BaseResponse<?> batchDelete(@RequestParam("userIdList") List<Long> userIdList) {
        sysUserService.batchDeleteSysUser(userIdList);
        return BaseResponse.getSuccessResponse();
    }
}
