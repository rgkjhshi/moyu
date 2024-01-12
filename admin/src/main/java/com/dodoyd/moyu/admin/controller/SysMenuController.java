package com.dodoyd.moyu.admin.controller;

import com.dodoyd.moyu.admin.domain.SysMenu;
import com.dodoyd.moyu.admin.model.request.SysMenuRequest;
import com.dodoyd.moyu.admin.service.SysMenuService;
import com.dodoyd.moyu.common.annotation.Log;
import com.dodoyd.moyu.common.model.BaseResponse;
import com.dodoyd.moyu.common.model.PageResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * SysMenuController控制器
 *
 * @author moyusi
 * @since 2024-01-12
 */
@RestController
@RequestMapping("/api/sysMenu")
public class SysMenuController {

    @Resource
    private SysMenuService sysMenuService;

    /**
     * 查询SysMenu列表
     */
    @Log(response = false)
    @PostMapping(value = "/list")
    public BaseResponse<PageResult<SysMenu>> queryList(@RequestBody SysMenuRequest request) {
        PageResult<SysMenu> page = sysMenuService.querySysMenuList(request);
        return BaseResponse.getSuccessResponse(page);
    }

    /**
     * 查询SysMenu详细信息
     */
    @Log(response = false)
    @GetMapping(value = "/get")
    public BaseResponse<SysMenu> getInfo(Long id) {
        SysMenu info = sysMenuService.querySysMenuById(id);
        return BaseResponse.getSuccessResponse(info);
    }

    /**
     * 查询SysMenu详细信息
     */
    @Log(response = false)
    @PostMapping(value = "/query")
    public BaseResponse<SysMenu> queryInfo(@RequestBody SysMenuRequest request) {
        SysMenu info = sysMenuService.querySysMenu(request);
        return BaseResponse.getSuccessResponse(info);
    }

    /**
     * 新增SysMenu
     */
    @Log
    @PostMapping(value = "/add")
    public BaseResponse<?> add(@RequestBody SysMenuRequest request) {
        sysMenuService.addSysMenu(request);
        return BaseResponse.getSuccessResponse();
    }

    /**
     * 修改SysMenu
     */
    @Log
    @PostMapping(value = "/edit")
    public BaseResponse<?> edit(@RequestBody SysMenuRequest request) {
        sysMenuService.updateSysMenu(request);
        return BaseResponse.getSuccessResponse();
    }

    /**
     * 删除SysMenu
     */
    @Log
    @PostMapping(value = "/delete")
    public BaseResponse<?> delete(Long id) {
        sysMenuService.deleteSysMenu(id);
        return BaseResponse.getSuccessResponse();
    }

    /**
     * 批量删除SysMenu
     * 请求参数Key为idList, value为逗号分割的字符串，如:idList=1,2,3
     */
    @Log
    @PostMapping(value = "/batchDelete")
    public BaseResponse<?> batchDelete(@RequestParam("idList") List<Long> idList) {
        sysMenuService.batchDeleteSysMenu(idList);
        return BaseResponse.getSuccessResponse();
    }
}
