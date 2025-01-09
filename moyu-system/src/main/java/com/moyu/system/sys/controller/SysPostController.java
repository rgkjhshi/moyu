package com.moyu.system.sys.controller;


import com.moyu.common.annotation.Log;
import com.moyu.common.model.BaseResponse;
import com.moyu.common.model.PageResult;
import com.moyu.system.sys.model.entity.SysPost;
import com.moyu.system.sys.model.param.SysPostParam;
import com.moyu.system.sys.model.vo.RelationVO;
import com.moyu.system.sys.service.RelationService;
import com.moyu.system.sys.service.SysPostService;
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
@RequestMapping("/api/sys/post")
public class SysPostController {

    @Resource
    private SysPostService sysPostService;

    @Resource
    private RelationService relationService;

    /**
     * 分页获取角色列表
     */
    @PostMapping("/page")
    public BaseResponse<PageResult<SysPost>> pageList(@RequestBody SysPostParam sysPostParam) {
        PageResult<SysPost> page = sysPostService.pageList(sysPostParam);
        return BaseResponse.getSuccessResponse(page);
    }

    /**
     * 获取详情
     */
    @PostMapping("/detail")
    public BaseResponse<SysPost> detail(@RequestBody SysPostParam sysPostParam) {
        return BaseResponse.getSuccessResponse(sysPostService.detail(sysPostParam));
    }

    /**
     * 添加
     */
    @PostMapping("/add")
    public BaseResponse<String> add(@RequestBody SysPostParam sysPostParam) {
        sysPostService.add(sysPostParam);
        return BaseResponse.getSuccessResponse();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    public BaseResponse<String> delete(@RequestBody SysPostParam sysPostParam) {
        sysPostService.deleteByIds(sysPostParam);
        return BaseResponse.getSuccessResponse();
    }

    /**
     * 编辑
     */
    @PostMapping("/edit")
    public BaseResponse<String> edit(@RequestBody SysPostParam sysPostParam) {
        sysPostService.edit(sysPostParam);
        return BaseResponse.getSuccessResponse();
    }

    /**
     * 查询指定岗位的角色列表
     */
    @PostMapping("/roleList")
    public BaseResponse<List<RelationVO>> roleList(@RequestBody SysPostParam sysPostParam) {
        List<RelationVO> list = relationService.groupRoleList(sysPostParam);
        return BaseResponse.getSuccessResponse(list);
    }

    /**
     * 岗位内新增角色
     */
    @PostMapping("/addRole")
    public BaseResponse<?> addRole(@RequestBody SysPostParam sysPostParam) {
        relationService.groupAddRole(sysPostParam);
        return BaseResponse.getSuccessResponse();
    }

}
