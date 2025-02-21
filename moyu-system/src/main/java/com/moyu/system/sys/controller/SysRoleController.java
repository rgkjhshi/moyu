package com.moyu.system.sys.controller;


import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.util.ObjectUtil;
import com.moyu.common.annotation.Log;
import com.moyu.common.model.BaseResponse;
import com.moyu.common.model.PageResult;
import com.moyu.system.sys.model.entity.SysRole;
import com.moyu.system.sys.model.entity.SysUser;
import com.moyu.system.sys.model.param.SysGroupParam;
import com.moyu.system.sys.model.param.SysRoleParam;
import com.moyu.system.sys.service.RelationService;
import com.moyu.system.sys.service.SysRoleService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

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

    @Resource
    private RelationService relationService;

    /**
     * 获取角色列表
     */
    @PostMapping("/list")
    public BaseResponse<List<SysRole>> list(@RequestBody SysRoleParam roleParam) {
        List<SysRole> list = sysRoleService.list(roleParam);
        return BaseResponse.getSuccessResponse(list);
    }

    /**
     * 分页获取角色列表
     */
    @PostMapping("/page")
    public BaseResponse<PageResult<SysRole>> pageList(@RequestBody SysRoleParam roleParam) {
        Assert.isTrue(ObjectUtil.isAllNotEmpty(roleParam.getPageNum(), roleParam.getPageSize()), "分页参数pageNum,pageSize都不能为空");
        PageResult<SysRole> page = sysRoleService.pageList(roleParam);
        return BaseResponse.getSuccessResponse(page);
    }

    /**
     * 获取详情
     */
    @PostMapping("/detail")
    public BaseResponse<SysRole> detail(@RequestBody SysRoleParam roleParam) {
        Assert.isTrue(!ObjectUtil.isAllEmpty(roleParam.getId(), roleParam.getCode()), "id和code不能同时为空");
        return BaseResponse.getSuccessResponse(sysRoleService.detail(roleParam));
    }

    /**
     * 添加
     */
    @PostMapping("/add")
    public BaseResponse<String> add(@Validated @RequestBody SysRoleParam roleParam) {
        sysRoleService.add(roleParam);
        return BaseResponse.getSuccessResponse();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    public BaseResponse<String> delete(@RequestBody SysRoleParam roleParam) {
        Assert.notEmpty(roleParam.getIds(), "删除列表ids不能为空");
        sysRoleService.deleteByIds(roleParam);
        return BaseResponse.getSuccessResponse();
    }

    /**
     * 编辑
     */
    @PostMapping("/edit")
    public BaseResponse<String> edit(@Validated @RequestBody SysRoleParam roleParam) {
        Assert.isTrue(!ObjectUtil.isAllEmpty(roleParam.getId(), roleParam.getCode()), "id和code不能同时为空");
        sysRoleService.edit(roleParam);
        return BaseResponse.getSuccessResponse();
    }

    /**
     * 获取菜单树，用于给角色授权时选择(treeNode不包含button)
     */
    @PostMapping("/menuTreeForGrant")
    public BaseResponse<List<Tree<String>>> menuTreeForGrant(@RequestBody SysRoleParam roleParam) {
        Assert.notEmpty(roleParam.getCode(), "角色code不能为空");
        return BaseResponse.getSuccessResponse(sysRoleService.treeForGrant(roleParam));
    }

    /**
     * 给角色授权菜单
     */
    @PostMapping("/grantMenu")
    public BaseResponse<List<Tree<String>>> grantMenu(@RequestBody SysRoleParam roleParam) {
        Assert.notEmpty(roleParam.getCode(), "角色code不能为空");
        Assert.notEmpty(roleParam.getModule(), "模块module不能为空");
        Assert.notEmpty(roleParam.getGrantMenuList(), "授权列表grantMenuList不能为空");
        sysRoleService.grantMenu(roleParam);
        return BaseResponse.getSuccessResponse();
    }

    /**
     * 查询拥有指定角色的所有用户(仅直接通过 用户-角色 关系指定的用户，即全局角色用户)
     */
    @PostMapping("/userList")
    public BaseResponse<List<SysUser>> userList(@RequestBody SysRoleParam roleParam) {
        Assert.notEmpty(roleParam.getCode(), "分组code不能为空");
        List<SysUser> list = relationService.roleUserList(roleParam);
        return BaseResponse.getSuccessResponse(list);
    }

    /**
     * 授权用户角色
     */
    @PostMapping("/userGrantRole")
    public BaseResponse<?> userGrantRole(@RequestBody SysRoleParam roleParam) {
        Assert.notEmpty(roleParam.getCode(), "角色code不能为空");
        Assert.notEmpty(roleParam.getCodeSet(), "指定集合codeSet不能为空");
        sysRoleService.userGrantRole(roleParam);
        return BaseResponse.getSuccessResponse();
    }

    /**
     * 撤销用户已授权的角色
     */
    @PostMapping("/userRevokeRole")
    public BaseResponse<?> userRevokeRole(@RequestBody SysRoleParam roleParam) {
        Assert.notEmpty(roleParam.getCode(), "角色code不能为空");
        Assert.notEmpty(roleParam.getCodeSet(), "指定集合codeSet不能为空");
        sysRoleService.userRevokeRole(roleParam);
        return BaseResponse.getSuccessResponse();
    }

}
