package com.moyu.system.sys.service.impl;


import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.util.ObjectUtil;
import com.moyu.system.auth.security.util.SecurityUtils;
import com.moyu.system.sys.model.entity.SysUser;
import com.moyu.system.sys.model.param.SysUserParam;
import com.moyu.system.sys.model.vo.UserInfo;
import com.moyu.system.sys.service.UserCenterService;
import com.moyu.system.sys.service.SysUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * @author shisong
 * @since 2025-01-07
 */
@Service
public class UserCenterServiceImpl implements UserCenterService {

    @Resource
    SysUserService sysUserService;


    @Override
    public UserInfo currentUserInfo() {
        // 当前登陆用户username
        String username = SecurityUtils.getLoginUser().getUsername();
        // 查询用户entity
        SysUser user = sysUserService.detail(SysUserParam.builder().account(username).build());
        // 构造用户信息视图对象
        UserInfo userInfo = UserInfo.builder().account(username)
                .name(user.getName()).nickName(user.getNickName()).avatar(user.getAvatar()).build();
        // 角色集合
        Set<String> roles = SecurityUtils.getRoles();
        userInfo.setRoles(roles);
        // 权限集合
        if (ObjectUtil.isNotEmpty(roles)) {
            // TODO Set<String> perms = permissionService.getRolePermsFormCache(roles);
            Set<String> perms = SecurityUtils.getPerms();
            userInfo.setPerms(perms);
        }
        return userInfo;
    }

    @Override
    public List<Tree<String>> userMenu(SysUserParam param) {
        return null;
    }
}
