package com.moyu.system.sys.service.impl;


import cn.hutool.core.util.ObjectUtil;
import com.moyu.system.sys.enums.RelationTypeEnum;
import com.moyu.system.sys.model.entity.SysRelation;
import com.moyu.system.sys.model.entity.SysRole;
import com.moyu.system.sys.model.entity.SysUser;
import com.moyu.system.sys.model.param.SysGroupParam;
import com.moyu.system.sys.model.param.SysRelationParam;
import com.moyu.system.sys.model.param.SysRoleParam;
import com.moyu.system.sys.model.param.SysUserParam;
import com.moyu.system.sys.service.RelationService;
import com.moyu.system.sys.service.SysRelationService;
import com.moyu.system.sys.service.SysRoleService;
import com.moyu.system.sys.service.SysUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author shisong
 * @since 2025-01-09
 */
@Service
public class RelationServiceImpl implements RelationService {

    @Resource
    private SysRelationService sysRelationService;

    @Resource
    private SysRoleService sysRoleService;

    @Resource
    private SysUserService sysUserService;


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
        Set<String> roleSet = list.stream().map(SysRelation::getTargetId).collect(Collectors.toSet());
        // 查询角色(可指定搜索词)
        List<SysUser> userList = sysUserService.list(SysUserParam.builder()
                .searchKey(groupParam.getSearchKey())
                .orgCode(groupParam.getOrgCode())
                .codeSet(roleSet).build());
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
        Set<String> oldSet = new HashSet<>();
        // 查询指定group包含的user，放入oldSet
        sysRelationService.list(SysRelationParam.builder().objectId(objectId).targetSet(targetSet)
                .relationType(RelationTypeEnum.GROUP_HAS_USER.getCode()).build()
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
