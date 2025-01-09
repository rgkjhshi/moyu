package com.moyu.system.sys.service;

import com.moyu.system.sys.model.entity.SysRole;
import com.moyu.system.sys.model.entity.SysUser;
import com.moyu.system.sys.model.param.SysPostParam;

import java.util.List;

/**
 * 关联对象的服务类(关联对象是指存在映射关系的实体)
 */
public interface RelationService {

    /**
     * group内角色列表
     */
    List<SysRole> groupRoleList(SysPostParam postParam);

    /**
     * group内用户列表
     */
    List<SysUser> groupUserList(SysPostParam postParam);

    /**
     * group新增角色
     */
    void groupAddRole(SysPostParam postParam);

    /**
     * group删除角色
     */
    void groupDeleteRole(SysPostParam postParam);

    /**
     * group新增用户
     */
    void groupAddUser(SysPostParam postParam);

    /**
     * group删除用户
     */
    void groupDeleteUser(SysPostParam postParam);

}
