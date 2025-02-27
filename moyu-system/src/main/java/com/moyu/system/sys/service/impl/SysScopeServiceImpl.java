package com.moyu.system.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moyu.common.model.PageResult;
import com.moyu.system.sys.mapper.SysScopeMapper;
import com.moyu.system.sys.model.entity.SysScope;
import com.moyu.system.sys.model.entity.SysUser;
import com.moyu.system.sys.model.param.SysScopeParam;
import com.moyu.system.sys.service.SysScopeService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @author shisong
 * @description 针对表【sys_scope(数据权限分组表)】的数据库操作Service实现
 * @createDate 2025-02-27 10:19:59
 */
@Service
public class SysScopeServiceImpl extends ServiceImpl<SysScopeMapper, SysScope> implements SysScopeService {

    @Override
    public PageResult<SysScope> pageList(SysScopeParam groupParam) {
        return null;
    }

    @Override
    public SysScope detail(SysScopeParam groupParam) {
        return null;
    }

    @Override
    public void add(SysScopeParam groupParam) {

    }

    @Override
    public void deleteByIds(SysScopeParam groupParam) {

    }

    @Override
    public void edit(SysScopeParam groupParam) {

    }

    @Override
    public List<SysUser> scopeUserList(SysScopeParam groupParam) {
        return Collections.emptyList();
    }

    @Override
    public void scopeAddUser(SysScopeParam groupParam) {

    }

    @Override
    public void scopeDeleteUser(SysScopeParam groupParam) {

    }
}




