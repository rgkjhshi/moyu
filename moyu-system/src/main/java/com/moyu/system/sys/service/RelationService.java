package com.moyu.system.sys.service;

import com.moyu.system.sys.model.entity.SysRole;
import com.moyu.system.sys.model.entity.SysUser;
import com.moyu.system.sys.model.param.SysGroupParam;
import com.moyu.system.sys.model.param.SysRoleParam;

import java.util.List;

/**
 * 关联对象的服务类(关联对象是指存在映射关系的实体)
 */
public interface RelationService {

    /**
     * group内角色列表
     */
    List<SysRole> groupRoleList(SysGroupParam groupParam);

    /**
     * group内用户列表
     */
    List<SysUser> groupUserList(SysGroupParam groupParam);

    /**
     * group新增角色
     */
    void groupAddRole(SysGroupParam groupParam);

    /**
     * group删除角色
     */
    void groupDeleteRole(SysGroupParam groupParam);

    /**
     * group新增用户
     */
    void groupAddUser(SysGroupParam groupParam);

    /**
     * group删除用户
     */
    void groupDeleteUser(SysGroupParam groupParam);

    /**
     * 拥有某角色的用户列表，仅包含用户-角色关系直接指定的用户，即全局角色
     */
    List<SysUser> roleUserList(SysRoleParam roleParam);

}
