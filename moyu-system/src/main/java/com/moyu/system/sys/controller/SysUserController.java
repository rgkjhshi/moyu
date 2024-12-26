package com.moyu.system.sys.controller;


import com.moyu.common.annotation.Log;
import com.moyu.common.model.BaseResponse;
import com.moyu.common.model.PageResult;
import com.moyu.system.sys.model.entity.SysPost;
import com.moyu.system.sys.model.entity.SysUser;
import com.moyu.system.sys.model.param.SysPostParam;
import com.moyu.system.sys.model.param.SysUserParam;
import com.moyu.system.sys.service.SysPostService;
import com.moyu.system.sys.service.SysUserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 用户控制器
 *
 * @author shisong
 * @since 2024-12-20
 */
@Log(jsonLog = true)
@RestController
@RequestMapping("/api/sys/user")
public class SysUserController {

    @Resource
    private SysPostService sysPostService;

    @Resource
    private SysUserService sysUserService;

    /**
     * 分页获取角色列表
     */
    @PostMapping("/page")
    public BaseResponse<PageResult<SysUser>> pageList(@RequestBody SysUserParam sysUserParam) {
        PageResult<SysUser> page = sysUserService.pageList(sysUserParam);
        return BaseResponse.getSuccessResponse(page);
    }

    /**
     * 获取详情
     */
    @PostMapping("/detail")
    public BaseResponse<SysUser> detail(@RequestBody SysUserParam sysUserParam) {
        return BaseResponse.getSuccessResponse(sysUserService.detail(sysUserParam));
    }

    /**
     * 添加
     */
    @PostMapping("/add")
    public BaseResponse<String> add(@RequestBody SysUserParam sysUserParam) {
        sysUserService.add(sysUserParam);
        return BaseResponse.getSuccessResponse();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    public BaseResponse<String> delete(@RequestBody SysUserParam sysUserParam) {
        sysUserService.deleteByIds(sysUserParam);
        return BaseResponse.getSuccessResponse();
    }

    /**
     * 编辑
     */
    @PostMapping("/edit")
    public BaseResponse<?> edit(@RequestBody SysUserParam sysUserParam) {
        sysUserService.edit(sysUserParam);
        return BaseResponse.getSuccessResponse();
    }

    /**
     * 重置用户密码
     **/
    @PostMapping("/resetPwd")
    public BaseResponse<?> resetPassword(@RequestBody SysUserParam sysUserParam) {
        sysUserService.resetPassword(sysUserParam);
        return BaseResponse.getSuccessResponse();
    }

}
