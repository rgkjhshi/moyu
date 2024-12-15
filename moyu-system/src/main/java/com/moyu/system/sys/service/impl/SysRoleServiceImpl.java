package com.moyu.system.sys.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moyu.common.enums.ExceptionEnum;
import com.moyu.common.exception.BaseException;
import com.moyu.common.model.PageResult;
import com.moyu.system.sys.mapper.SysRoleMapper;
import com.moyu.system.sys.model.entity.SysRole;
import com.moyu.system.sys.model.param.SysRoleParam;
import com.moyu.system.sys.service.SysRoleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * @author shisong
 * @description 针对表【sys_role(角色信息表)】的数据库操作Service实现
 * @createDate 2024-12-15 20:49:43
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Override
    public List<SysRole> list(SysRoleParam roleParam) {
        QueryWrapper<SysRole> queryWrapper = new QueryWrapper<SysRole>().checkSqlInjection();
        // 查询条件
        queryWrapper.lambda()
                // 查询部分字段
//                .select(SysMenu::getCode, SysMenu::getName, SysMenu::getSortNum)
                // 关键词搜索
                .like(StrUtil.isNotBlank(roleParam.getSearchKey()), SysRole::getName, roleParam.getSearchKey())
                // 指定模块
                .eq(ObjectUtil.isNotEmpty(roleParam.getModule()), SysRole::getModule, roleParam.getModule())
                // 指定状态
                .eq(ObjectUtil.isNotEmpty(roleParam.getStatus()), SysRole::getStatus, roleParam.getStatus())
                .eq(SysRole::getDeleteFlag, 0)
                .orderByAsc(SysRole::getSortNum);
        // 查询
        List<SysRole> roleList = this.list(queryWrapper);
        return roleList;
    }

    @Override
    public PageResult<SysRole> pageList(SysRoleParam roleParam) {
        QueryWrapper<SysRole> queryWrapper = new QueryWrapper<SysRole>().checkSqlInjection();
        // 查询条件
        queryWrapper.lambda()
                // 关键词搜索
                .like(StrUtil.isNotBlank(roleParam.getSearchKey()), SysRole::getName, roleParam.getSearchKey())
                // 指定模块
                .eq(ObjectUtil.isNotEmpty(roleParam.getModule()), SysRole::getModule, roleParam.getModule())
                // 指定状态
                .eq(ObjectUtil.isNotEmpty(roleParam.getStatus()), SysRole::getStatus, roleParam.getStatus())
                .eq(SysRole::getDeleteFlag, 0)
                .orderByAsc(SysRole::getSortNum);
        // 分页查询
        Page<SysRole> page = new Page<>(roleParam.getPageNum(), roleParam.getPageSize());
        Page<SysRole> rolePage = this.page(page, queryWrapper);
        return new PageResult<>(rolePage.getTotal(), rolePage.getRecords());
    }

    @Override
    public SysRole detail(SysRoleParam roleParam) {
        LambdaQueryWrapper<SysRole> queryWrapper = new QueryWrapper<SysRole>().checkSqlInjection().lambda()
                .eq(ObjectUtil.isNotEmpty(roleParam.getId()), SysRole::getId, roleParam.getId())
                .eq(ObjectUtil.isNotEmpty(roleParam.getCode()), SysRole::getCode, roleParam.getCode());
        // id、code均为唯一标识
        SysRole sysRole = this.getOne(queryWrapper);
        if (sysRole == null) {
            throw new BaseException(ExceptionEnum.INVALID_PARAMETER, "未查到指定数据");
        }
        return sysRole;
    }

    @Override
    public void add(SysRoleParam roleParam) {
        // 属性复制
        SysRole role = BeanUtil.copyProperties(roleParam, SysRole.class);
        role.setId(null);
        // 唯一code RandomUtil.randomString(10)、IdUtil.objectId()24位
        role.setCode(IdUtil.objectId());
        this.save(role);
    }

    @Override
    public void deleteByIds(SysRoleParam roleParam) {
        // 待删除的id集合
        Set<Long> idSet = roleParam.getIds();
        // 逻辑删除
        UpdateWrapper<SysRole> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("id", idSet).set("delete_flag", 1);
        this.update(updateWrapper);
    }

    @Override
    public void edit(SysRoleParam roleParam) {
        SysRole oldRole = this.detail(roleParam);
        // 属性复制
        SysRole updateOrg = BeanUtil.copyProperties(roleParam, SysRole.class);
        updateOrg.setId(oldRole.getId());
        this.updateById(updateOrg);
    }
}




