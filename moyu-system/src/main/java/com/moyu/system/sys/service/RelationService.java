package com.moyu.system.sys.service;

import com.moyu.system.sys.model.entity.SysRole;
import com.moyu.system.sys.model.entity.SysUser;
import com.moyu.system.sys.model.param.SysGroupParam;

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

}
