package com.moyu.system.sys.controller;


import com.moyu.common.annotation.Log;
import com.moyu.common.model.BaseResponse;
import com.moyu.system.sys.model.entity.SysMenu;
import com.moyu.system.sys.model.param.SysMenuParam;
import com.moyu.system.sys.service.SysMenuService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 资源菜单权限控制器
 *
 * @author shisong
 * @since 2024-12-11
 */
@Log(jsonLog = true)
@RestController
@RequestMapping("/api/sys/menu")
public class SysMenuController {


    @Resource
    private SysMenuService sysMenuService;

    /**
     * 菜单列表
     */
    @PostMapping("/list")
    public BaseResponse<List<SysMenu>> list(@RequestBody SysMenuParam sysMenuParam) {
        List<SysMenu> list = sysMenuService.list(sysMenuParam);
        return BaseResponse.getSuccessResponse(list);
    }

}
