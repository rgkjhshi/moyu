package com.moyu.system.sys.controller;


import com.moyu.common.annotation.Log;
import com.moyu.common.model.BaseResponse;
import com.moyu.common.model.PageResult;
import com.moyu.system.sys.model.entity.SysPost;
import com.moyu.system.sys.model.entity.SysRelation;
import com.moyu.system.sys.model.param.SysPostParam;
import com.moyu.system.sys.model.param.SysRelationParam;
import com.moyu.system.sys.model.vo.RelationVO;
import com.moyu.system.sys.service.SysPostService;
import com.moyu.system.sys.service.SysRelationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 关系映射控制器(角色组-角色、用户组-用户等)
 *
 * @author shisong
 * @since 2024-12-20
 */
@Log(jsonLog = true)
@RestController
@RequestMapping("/api/sys/relation")
public class SysRelationController {

    @Resource
    private SysPostService sysPostService;

    @Resource
    private SysRelationService sysRelationService;

    /**
     * 组内的角色列表
     */
    @PostMapping("/groupRoleList")
    public BaseResponse<List<RelationVO>> list(@RequestBody SysRelationParam param) {
        List<RelationVO> list = sysRelationService.groupRoleList(param);
        return BaseResponse.getSuccessResponse(list);
    }

    /**
     * 分页获取角色列表
     */
    @PostMapping("/page")
    public BaseResponse<PageResult<SysPost>> pageList(@RequestBody SysPostParam sysPostParam) {
        PageResult<SysPost> page = sysPostService.pageList(sysPostParam);
        return BaseResponse.getSuccessResponse(page);
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


}
