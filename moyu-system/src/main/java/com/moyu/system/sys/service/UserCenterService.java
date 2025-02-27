package com.moyu.system.sys.service;

import cn.hutool.core.lang.tree.Tree;
import com.moyu.system.sys.model.vo.UserInfo;

import java.util.List;

/**
 * 用户中心服务类，当前用户服务类
 *
 * @author shisong
 */
public interface UserCenterService {

    /**
     * 获取登陆用户详情
     */
    UserInfo currentUserInfo(String username);

    /**
     * 获取用户菜单树
     */
    List<Tree<String>> userMenu(String account);

    /**
     * 获取用户菜单树
     */
    List<Tree<String>> userOrgTree(String account);

}
