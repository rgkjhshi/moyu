package com.moyu.system.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moyu.common.model.PageResult;
import com.moyu.system.sys.model.entity.SysScope;
import com.moyu.system.sys.model.entity.SysUser;
import com.moyu.system.sys.model.param.SysScopeParam;

import java.util.List;

/**
 * @author shisong
 * @description 针对表【sys_scope(数据权限分组表)】的数据库操作Service
 * @createDate 2025-02-27 10:19:59
 */
public interface SysScopeService extends IService<SysScope> {

    /**
     * 分页获取记录列表
     */
    PageResult<SysScope> pageList(SysScopeParam groupParam);

    /**
     * 获取记录详情
     */
    SysScope detail(SysScopeParam groupParam);

    /**
     * 添加记录
     */
    void add(SysScopeParam groupParam);

    /**
     * 通过ids删除记录
     */
    void deleteByIds(SysScopeParam groupParam);

    /**
     * 修改记录
     */
    void edit(SysScopeParam groupParam);

    ////// scope 通过 relation 管理的数据

    /**
     * scope内用户列表
     */
    List<SysUser> scopeUserList(SysScopeParam groupParam);

    /**
     * scope新增用户
     */
    void scopeAddUser(SysScopeParam groupParam);

    /**
     * scope删除用户
     */
    void scopeDeleteUser(SysScopeParam groupParam);

}
