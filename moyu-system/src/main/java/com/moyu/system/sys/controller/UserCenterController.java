package com.moyu.system.sys.controller;


import cn.hutool.core.lang.tree.Tree;
import com.moyu.common.annotation.Log;
import com.moyu.common.model.BaseResponse;
import com.moyu.system.auth.security.util.SecurityUtils;
import com.moyu.system.sys.model.param.SysUserParam;
import com.moyu.system.sys.model.vo.UserInfo;
import com.moyu.system.sys.service.UserCenterService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户中心控制器，当前登陆用户控制器
 *
 * @author shisong
 * @since 2024-12-20
 */
@Log(jsonLog = true)
@RestController
@RequestMapping("/api/sys/userCenter")
public class UserCenterController {

    @Resource
    private UserCenterService userCenterService;

    /**
     * 获取当前登陆用户信息
     */
    @PostMapping("/me")
    public BaseResponse<UserInfo> currentUserInfo() {
        // 当前登陆用户username
        String username = SecurityUtils.getLoginUser().getUsername();
        return BaseResponse.getSuccessResponse(userCenterService.currentUserInfo());
    }

    /**
     * 获取当前登陆用户的菜单
     */
    @PostMapping("/userMenu")
    public BaseResponse<List<Tree<String>>> currentUserMenu() {
        // 当前登陆用户username
        String username = SecurityUtils.getLoginUser().getUsername();
        SysUserParam param = SysUserParam.builder().account(username).build();
        return BaseResponse.getSuccessResponse(userCenterService.userMenu(param));
    }

}
