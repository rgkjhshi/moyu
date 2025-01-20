package com.moyu.system.sys.controller;


import com.moyu.common.annotation.Log;
import com.moyu.common.model.BaseResponse;
import com.moyu.common.model.PageResult;
import com.moyu.system.sys.model.entity.SysGroup;
import com.moyu.system.sys.model.entity.SysRole;
import com.moyu.system.sys.model.entity.SysUser;
import com.moyu.system.sys.model.param.SysGroupParam;
import com.moyu.system.sys.service.RelationService;
import com.moyu.system.sys.service.SysGroupService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 分组(角色组、岗位)控制器
 *
 * @author shisong
 * @since 2024-12-20
 */
@Log(jsonLog = true)
@RestController
@RequestMapping("/api/sys/group")
public class SysGroupController {

    @Resource
    private SysGroupService sysGroupService;

    @Resource
    private RelationService relationService;

    /**
     * 分页获取角色列表
     */
    @PostMapping("/page")
    public BaseResponse<PageResult<SysGroup>> pageList(@RequestBody SysGroupParam sysGroupParam) {
        PageResult<SysGroup> page = sysGroupService.pageList(sysGroupParam);
        return BaseResponse.getSuccessResponse(page);
    }

    /**
     * 获取详情
     */
    @PostMapping("/detail")
    public BaseResponse<SysGroup> detail(@RequestBody SysGroupParam sysGroupParam) {
        return BaseResponse.getSuccessResponse(sysGroupService.detail(sysGroupParam));
    }

    /**
     * 添加
     */
    @PostMapping("/add")
    public BaseResponse<String> add(@RequestBody SysGroupParam sysGroupParam) {
        sysGroupService.add(sysGroupParam);
        return BaseResponse.getSuccessResponse();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    public BaseResponse<String> delete(@RequestBody SysGroupParam sysGroupParam) {
        sysGroupService.deleteByIds(sysGroupParam);
        return BaseResponse.getSuccessResponse();
    }

    /**
     * 编辑
     */
    @PostMapping("/edit")
    public BaseResponse<String> edit(@RequestBody SysGroupParam sysGroupParam) {
        sysGroupService.edit(sysGroupParam);
        return BaseResponse.getSuccessResponse();
    }

    /**
     * 查询指定岗位的角色列表
     */
    @PostMapping("/roleList")
    public BaseResponse<List<SysRole>> roleList(@RequestBody SysGroupParam sysGroupParam) {
        List<SysRole> list = relationService.groupRoleList(sysGroupParam);
        return BaseResponse.getSuccessResponse(list);
    }

    /**
     * 岗位内新增角色
     */
    @PostMapping("/addRole")
    public BaseResponse<?> addRole(@RequestBody SysGroupParam sysGroupParam) {
        relationService.groupAddRole(sysGroupParam);
        return BaseResponse.getSuccessResponse();
    }

    /**
     * 岗位内移除角色
     */
    @PostMapping("/deleteRole")
    public BaseResponse<?> deleteRole(@RequestBody SysGroupParam sysGroupParam) {
        relationService.groupDeleteRole(sysGroupParam);
        return BaseResponse.getSuccessResponse();
    }

    /**
     * 查询指定岗位的角色列表
     */
    @PostMapping("/userList")
    public BaseResponse<List<SysUser>> userList(@RequestBody SysGroupParam sysGroupParam) {
        List<SysUser> list = relationService.groupUserList(sysGroupParam);
        return BaseResponse.getSuccessResponse(list);
    }

    /**
     * 岗位内新增角色
     */
    @PostMapping("/addUser")
    public BaseResponse<?> addUser(@RequestBody SysGroupParam sysGroupParam) {
        relationService.groupAddUser(sysGroupParam);
        return BaseResponse.getSuccessResponse();
    }

    /**
     * 岗位内移除角色
     */
    @PostMapping("/deleteUser")
    public BaseResponse<?> deleteUser(@RequestBody SysGroupParam sysGroupParam) {
        relationService.groupDeleteUser(sysGroupParam);
        return BaseResponse.getSuccessResponse();
    }

}
