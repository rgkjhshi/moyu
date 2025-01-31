package com.moyu.system.sys.controller;


import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import com.moyu.common.annotation.Log;
import com.moyu.common.model.BaseResponse;
import com.moyu.common.model.PageResult;
import com.moyu.system.sys.model.entity.SysGroup;
import com.moyu.system.sys.model.entity.SysRole;
import com.moyu.system.sys.model.entity.SysUser;
import com.moyu.system.sys.model.param.SysGroupParam;
import com.moyu.system.sys.service.RelationService;
import com.moyu.system.sys.service.SysGroupService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 分组(角色组、分组)控制器
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
    public BaseResponse<PageResult<SysGroup>> pageList(@RequestBody SysGroupParam groupParam) {
        Assert.isTrue(ObjectUtil.isAllNotEmpty(groupParam.getPageNum(), groupParam.getPageSize()), "分页参数pageNum,pageSize都不能为空");
        PageResult<SysGroup> page = sysGroupService.pageList(groupParam);
        return BaseResponse.getSuccessResponse(page);
    }

    /**
     * 获取详情
     */
    @PostMapping("/detail")
    public BaseResponse<SysGroup> detail(@RequestBody SysGroupParam groupParam) {
        Assert.isTrue(!ObjectUtil.isAllEmpty(groupParam.getId(), groupParam.getCode()), "id和code不能同时为空");
        return BaseResponse.getSuccessResponse(sysGroupService.detail(groupParam));
    }

    /**
     * 添加
     */
    @PostMapping("/add")
    public BaseResponse<String> add(@Validated @RequestBody SysGroupParam groupParam) {
        sysGroupService.add(groupParam);
        return BaseResponse.getSuccessResponse();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    public BaseResponse<String> delete(@RequestBody SysGroupParam groupParam) {
        Assert.notEmpty(groupParam.getIds(), "删除列表ids不能为空");
        sysGroupService.deleteByIds(groupParam);
        return BaseResponse.getSuccessResponse();
    }

    /**
     * 编辑
     */
    @PostMapping("/edit")
    public BaseResponse<String> edit(@Validated @RequestBody SysGroupParam groupParam) {
        Assert.isTrue(!ObjectUtil.isAllEmpty(groupParam.getId(), groupParam.getCode()), "id和code不能同时为空");
        sysGroupService.edit(groupParam);
        return BaseResponse.getSuccessResponse();
    }

    /**
     * 查询指定分组的角色列表
     */
    @PostMapping("/roleList")
    public BaseResponse<List<SysRole>> roleList(@RequestBody SysGroupParam groupParam) {
        Assert.notEmpty(groupParam.getCode(), "分组code不能为空");
        List<SysRole> list = relationService.groupRoleList(groupParam);
        return BaseResponse.getSuccessResponse(list);
    }

    /**
     * 分组内新增角色
     */
    @PostMapping("/addRole")
    public BaseResponse<?> addRole(@RequestBody SysGroupParam groupParam) {
        Assert.notEmpty(groupParam.getCode(), "分组code不能为空");
        Assert.notEmpty(groupParam.getCodeSet(), "指定集合codeSet不能为空");
        relationService.groupAddRole(groupParam);
        return BaseResponse.getSuccessResponse();
    }

    /**
     * 分组内移除角色
     */
    @PostMapping("/deleteRole")
    public BaseResponse<?> deleteRole(@RequestBody SysGroupParam groupParam) {
        Assert.notEmpty(groupParam.getCode(), "分组code不能为空");
        Assert.notEmpty(groupParam.getCodeSet(), "指定集合codeSet不能为空");
        relationService.groupDeleteRole(groupParam);
        return BaseResponse.getSuccessResponse();
    }

    /**
     * 查询指定分组的角色列表
     */
    @PostMapping("/userList")
    public BaseResponse<List<SysUser>> userList(@RequestBody SysGroupParam groupParam) {
        Assert.notEmpty(groupParam.getCode(), "分组code不能为空");
        List<SysUser> list = relationService.groupUserList(groupParam);
        return BaseResponse.getSuccessResponse(list);
    }

    /**
     * 分组内新增角色
     */
    @PostMapping("/addUser")
    public BaseResponse<?> addUser(@RequestBody SysGroupParam groupParam) {
        Assert.notEmpty(groupParam.getCode(), "分组code不能为空");
        Assert.notEmpty(groupParam.getCodeSet(), "指定集合codeSet不能为空");
        relationService.groupAddUser(groupParam);
        return BaseResponse.getSuccessResponse();
    }

    /**
     * 分组内移除角色
     */
    @PostMapping("/deleteUser")
    public BaseResponse<?> deleteUser(@RequestBody SysGroupParam groupParam) {
        Assert.notEmpty(groupParam.getCode(), "分组code不能为空");
        Assert.notEmpty(groupParam.getCodeSet(), "指定集合codeSet不能为空");
        relationService.groupDeleteUser(groupParam);
        return BaseResponse.getSuccessResponse();
    }

}
