package com.moyu.system.sys.service;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.extension.service.IService;
import com.moyu.common.model.PageResult;
import com.moyu.system.sys.model.entity.SysRole;
import com.moyu.system.sys.model.entity.SysUser;
import com.moyu.system.sys.model.param.SysRoleParam;

import java.util.List;

/**
 * @author shisong
 * @description 针对表【sys_role(角色信息表)】的数据库操作Service
 * @createDate 2024-12-15 20:49:43
 */
public interface SysRoleService extends IService<SysRole> {

    /**
     * 获取记录列表
     */
    List<SysRole> list(SysRoleParam roleParam);

    /**
     * 分页获取记录列表
     */
    PageResult<SysRole> pageList(SysRoleParam roleParam);

    /**
     * 获取记录详情
     */
    SysRole detail(SysRoleParam roleParam);

    /**
     * 添加记录
     */
    void add(SysRoleParam roleParam);

    /**
     * 通过ids删除记录
     */
    void deleteByIds(SysRoleParam roleParam);

    /**
     * 修改记录
     */
    void edit(SysRoleParam roleParam);

    /**
     * 获取菜单树，用于给角色授权时选择(treeNode不包含button)
     *
     * @param roleParam 角色code必须传
     */
    List<Tree<String>> treeForGrant(SysRoleParam roleParam);

    /**
     * 角色授权，ROLE_HAS_MENU
     *
     * @param roleParam 角色code，授权module必须传
     */
    void grantMenu(SysRoleParam roleParam);

    /**
     * 用户分配角色，USER_HAS_ROLE
     *
     * @param roleParam 角色code，用户集合 codeSet
     */
    void userGrantRole(SysRoleParam roleParam);

    /**
     * 用户撤销角色，USER_HAS_ROLE
     *
     * @param roleParam 角色code，用户集合 codeSet
     */
    void userRevokeRole(SysRoleParam roleParam);

    /**
     * 拥有某角色的用户列表，仅包含用户-角色关系直接指定的用户，即全局角色。USER_HAS_ROLE
     */
    List<SysUser> roleUserList(SysRoleParam roleParam);

}
