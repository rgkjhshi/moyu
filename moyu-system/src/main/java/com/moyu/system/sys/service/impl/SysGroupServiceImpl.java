package com.moyu.system.sys.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Strings;
import com.moyu.common.enums.ExceptionEnum;
import com.moyu.common.exception.BaseException;
import com.moyu.common.model.PageResult;
import com.moyu.common.security.util.SecurityUtils;
import com.moyu.system.sys.constant.SysConstants;
import com.moyu.system.sys.enums.RelationTypeEnum;
import com.moyu.system.sys.mapper.SysGroupMapper;
import com.moyu.system.sys.model.entity.SysGroup;
import com.moyu.system.sys.model.entity.SysRelation;
import com.moyu.system.sys.model.entity.SysRole;
import com.moyu.system.sys.model.entity.SysUser;
import com.moyu.system.sys.model.param.SysGroupParam;
import com.moyu.system.sys.model.param.SysRelationParam;
import com.moyu.system.sys.model.param.SysRoleParam;
import com.moyu.system.sys.model.param.SysUserParam;
import com.moyu.system.sys.service.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author shisong
 * @description 针对表【sys_pos(岗位信息表)】的数据库操作Service实现
 * @createDate 2024-12-20 14:29:15
 */
@Service
public class SysGroupServiceImpl extends ServiceImpl<SysGroupMapper, SysGroup> implements SysGroupService {

    @Resource
    private SysOrgService sysOrgService;

    @Resource
    private SysRelationService sysRelationService;

    @Resource
    private SysRoleService sysRoleService;

    @Resource
    private SysUserService sysUserService;

    @Override
    public List<SysGroup> list(SysGroupParam groupParam) {
        QueryWrapper<SysGroup> queryWrapper = new QueryWrapper<SysGroup>().checkSqlInjection();
        // 查询条件
        queryWrapper.lambda()
                // 关键词搜索
                .like(StrUtil.isNotBlank(groupParam.getSearchKey()), SysGroup::getName, groupParam.getSearchKey())
                // 指定类型
                .eq(ObjectUtil.isNotEmpty(groupParam.getGroupType()), SysGroup::getGroupType, groupParam.getGroupType())
                // 指定状态
                .eq(ObjectUtil.isNotEmpty(groupParam.getStatus()), SysGroup::getStatus, groupParam.getStatus())
                .eq(SysGroup::getDeleteFlag, 0)
                .orderByAsc(SysGroup::getSortNum);
        // 查询
        List<SysGroup> groupList = this.list(queryWrapper);
        return groupList;
    }

    @Override
    public PageResult<SysGroup> pageList(SysGroupParam groupParam) {
        // 数据权限范围
        Set<String> scopeSet = new HashSet<>();
        // 非ROOT则限制
        if (!SecurityUtils.isRoot()) {
            scopeSet = SecurityUtils.getScopes();
        }
        // 查询条件
        LambdaQueryWrapper<SysGroup> queryWrapper = Wrappers.lambdaQuery(SysGroup.class)
                // 关键词搜索
                .like(StrUtil.isNotBlank(groupParam.getSearchKey()), SysGroup::getName, groupParam.getSearchKey())
                // 指定orgCode
                .eq(StrUtil.isNotBlank(groupParam.getOrgCode()), SysGroup::getOrgCode, groupParam.getOrgCode())
                // 指定状态
                .eq(ObjectUtil.isNotEmpty(groupParam.getStatus()), SysGroup::getStatus, groupParam.getStatus())
                // 数据权限(非空才有效)
                .in(ObjectUtil.isNotEmpty(scopeSet), SysGroup::getOrgCode, scopeSet)
                .eq(SysGroup::getDeleteFlag, 0)
                .orderByAsc(SysGroup::getSortNum);
        // 分页查询
        Page<SysGroup> page = new Page<>(groupParam.getPageNum(), groupParam.getPageSize());
        Page<SysGroup> groupPage = this.page(page, queryWrapper);
        return new PageResult<>(groupPage.getTotal(), groupPage.getRecords());
    }

    @Override
    public SysGroup detail(SysGroupParam groupParam) {
        LambdaQueryWrapper<SysGroup> queryWrapper = new QueryWrapper<SysGroup>().checkSqlInjection().lambda()
                .eq(ObjectUtil.isNotEmpty(groupParam.getId()), SysGroup::getId, groupParam.getId())
                .eq(ObjectUtil.isNotEmpty(groupParam.getCode()), SysGroup::getCode, groupParam.getCode());
        // id、code均为唯一标识
        SysGroup sysGroup = this.getOne(queryWrapper);
        if (sysGroup == null) {
            throw new BaseException(ExceptionEnum.INVALID_PARAMETER, "未查到指定数据");
        }
        return sysGroup;
    }

    @Override
    public void add(SysGroupParam groupParam) {
        // 属性复制
        SysGroup group = BeanUtil.copyProperties(groupParam, SysGroup.class);
        group.setId(null);
        // 若未指定唯一编码code，则自动生成
        if (Strings.isNullOrEmpty(group.getCode())) {
            // 唯一code RandomUtil.randomString(10)、IdUtil.objectId()24位、IdUtil.getSnowflakeNextId()19位
            group.setCode(SysConstants.GROUP_PREFIX + IdUtil.getSnowflakeNextId());
        }
        // 若指定了直属组织，则设置所属组织
        if (ObjectUtil.isNotEmpty(group.getOrgCode())) {
            // 获取组织结构树
            Tree<String> rootTree = sysOrgService.singleTree();
            Tree<String> orgNode = rootTree.getNode(group.getOrgCode());
            // 设置直属机构名称
            group.setOrgName(orgNode.getName().toString());
        }
        this.save(group);
    }

    @Override
    public void deleteByIds(SysGroupParam groupParam) {
        // 待删除的id集合
        Set<Long> idSet = groupParam.getIds();
        // 逻辑删除
        UpdateWrapper<SysGroup> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("id", idSet).set("delete_flag", 1);
        this.update(updateWrapper);
    }

    @Override
    public void edit(SysGroupParam groupParam) {
        SysGroup oldGroup = this.detail(groupParam);
        // 属性复制
        SysGroup updateOrg = BeanUtil.copyProperties(groupParam, SysGroup.class);
        updateOrg.setId(oldGroup.getId());
        // 若新指定了直属组织，则设置组织名
        if (ObjectUtil.notEqual(oldGroup.getOrgCode(), updateOrg.getOrgCode()) && ObjectUtil.isNotEmpty(updateOrg.getOrgCode())) {
            // 获取组织结构树
            Tree<String> rootTree = sysOrgService.singleTree();
            Tree<String> orgNode = rootTree.getNode(updateOrg.getOrgCode());
            // 设置直属机构名称
            updateOrg.setOrgName(orgNode.getName().toString());
        }
        this.updateById(updateOrg);
    }

    @Override
    public List<SysRole> groupRoleList(SysGroupParam groupParam) {
        // 查询指定group的所有role
        List<SysRelation> list = sysRelationService.list(SysRelationParam.builder()
                .relationType(RelationTypeEnum.GROUP_HAS_ROLE.getCode()).objectId(groupParam.getCode()).build());
        if (ObjectUtil.isEmpty(list)) {
            return new ArrayList<>();
        }
        // roleSet
        Set<String> roleSet = list.stream().map(SysRelation::getTargetId).collect(Collectors.toSet());
        // 查询角色(可指定搜索词)
        List<SysRole> roleList = sysRoleService.list(SysRoleParam.builder().searchKey(groupParam.getSearchKey()).codeSet(roleSet).build());
        return roleList;
    }

    @Override
    public List<SysUser> groupUserList(SysGroupParam groupParam) {
        // 查询指定group的所有user
        List<SysRelation> list = sysRelationService.list(SysRelationParam.builder()
                .relationType(RelationTypeEnum.GROUP_HAS_USER.getCode()).objectId(groupParam.getCode()).build());
        if (ObjectUtil.isEmpty(list)) {
            return new ArrayList<>();
        }
        // userSet
        Set<String> userSet = list.stream().map(SysRelation::getTargetId).collect(Collectors.toSet());
        // 查询用户(可指定搜索词)
        List<SysUser> userList = sysUserService.list(SysUserParam.builder()
                .searchKey(groupParam.getSearchKey())
                .orgCode(groupParam.getOrgCode())
                .codeSet(userSet).build());
        return userList;
    }

    @Override
    public void groupAddRole(SysGroupParam groupParam) {
        String objectId = groupParam.getCode();
        Set<String> targetSet = groupParam.getCodeSet();
        if (ObjectUtil.isEmpty(targetSet)) {
            return;
        }
        Set<String> oldSet = new HashSet<>();
        // 查询指定group包含的role，放入oldSet
        sysRelationService.list(SysRelationParam.builder().objectId(objectId).targetSet(targetSet)
                .relationType(RelationTypeEnum.GROUP_HAS_ROLE.getCode()).build()
        ).forEach(e -> oldSet.add(e.getTargetId()));
        // 从target中删除已经存在的
        targetSet.removeAll(oldSet);
        // 再次判断要新增的内容为空则返回
        if (ObjectUtil.isEmpty(targetSet)) {
            return;
        }
        List<SysRelation> addList = new ArrayList<>();
        targetSet.forEach(code -> {
            SysRelation entity = new SysRelation();
            entity.setObjectId(objectId);
            entity.setTargetId(code);
            entity.setRelationType(RelationTypeEnum.GROUP_HAS_ROLE.getCode());
            addList.add(entity);
        });
        sysRelationService.saveBatch(addList);
    }

    @Override
    public void groupDeleteRole(SysGroupParam groupParam) {
        if (ObjectUtil.isEmpty(groupParam.getCodeSet())) {
            return;
        }
        // 要删除的ids
        Set<Long> ids = new HashSet<>();
        // 查询指定group中存在的role，加入ids待删
        sysRelationService.list(SysRelationParam.builder().objectId(groupParam.getCode()).targetSet(groupParam.getCodeSet())
                .relationType(RelationTypeEnum.GROUP_HAS_ROLE.getCode()).build()
        ).forEach(e -> ids.add(e.getId()));
        // 删除
        if (ObjectUtil.isNotEmpty(ids)) {
            sysRelationService.removeByIds(ids);
        }
    }

    @Override
    public void groupAddUser(SysGroupParam groupParam) {
        String objectId = groupParam.getCode();
        Set<String> targetSet = groupParam.getCodeSet();
        if (ObjectUtil.isEmpty(targetSet)) {
            return;
        }
        // 已加入分组的用户
        Set<String> oldSet = new HashSet<>();
        Set<String> otherGroupUserSet = new HashSet<>();
        // 查询指定group包含的user，放入oldSet
        sysRelationService.list(Wrappers.lambdaQuery(SysRelation.class)
                .in(SysRelation::getTargetId, targetSet)
                .eq(SysRelation::getRelationType, RelationTypeEnum.GROUP_HAS_USER.getCode())
        ).forEach(e -> {
            if (objectId.equals(e.getObjectId())) {
                oldSet.add(e.getTargetId());
            } else {
                otherGroupUserSet.add(e.getTargetId());
            }
        });
        // 限制用户只允许加入一个分组
        if (ObjectUtil.isNotEmpty(otherGroupUserSet)) {
            String message = "下列用户已加入其他分组，不可重复添加:" + otherGroupUserSet;
            throw new BaseException(ExceptionEnum.INVALID_PARAMETER.getCode(), message);
        }
        // 从target中删除已经存在的
        targetSet.removeAll(oldSet);
        // 再次判断要新增的内容为空则返回
        if (ObjectUtil.isEmpty(targetSet)) {
            return;
        }
        List<SysRelation> addList = new ArrayList<>();
        targetSet.forEach(code -> {
            SysRelation entity = new SysRelation();
            entity.setObjectId(objectId);
            entity.setTargetId(code);
            entity.setRelationType(RelationTypeEnum.GROUP_HAS_USER.getCode());
            addList.add(entity);
        });
        sysRelationService.saveBatch(addList);
    }

    @Override
    public void groupDeleteUser(SysGroupParam groupParam) {
        if (ObjectUtil.isEmpty(groupParam.getCodeSet())) {
            return;
        }
        // 要删除的ids
        Set<Long> ids = new HashSet<>();
        // 查询指定group中存在的user，加入ids待删
        sysRelationService.list(SysRelationParam.builder().objectId(groupParam.getCode()).targetSet(groupParam.getCodeSet())
                .relationType(RelationTypeEnum.GROUP_HAS_USER.getCode()).build()
        ).forEach(e -> ids.add(e.getId()));
        // 删除
        if (ObjectUtil.isNotEmpty(ids)) {
            sysRelationService.removeByIds(ids);
        }
    }

}




