package com.moyu.system.sys.service.impl;


import cn.hutool.core.util.ObjectUtil;
import com.google.common.collect.Lists;
import com.moyu.system.sys.enums.RelationTypeEnum;
import com.moyu.system.sys.model.entity.SysRelation;
import com.moyu.system.sys.model.entity.SysRole;
import com.moyu.system.sys.model.entity.SysUser;
import com.moyu.system.sys.model.param.SysPostParam;
import com.moyu.system.sys.model.param.SysRelationParam;
import com.moyu.system.sys.model.param.SysRoleParam;
import com.moyu.system.sys.model.param.SysUserParam;
import com.moyu.system.sys.model.vo.RelationVO;
import com.moyu.system.sys.service.RelationService;
import com.moyu.system.sys.service.SysRelationService;
import com.moyu.system.sys.service.SysRoleService;
import com.moyu.system.sys.service.SysUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.LinkedHashMap;
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
    public List<RelationVO> groupRoleList(SysPostParam postParam) {
        // 查询指定group的所有relation
        List<SysRelation> list = sysRelationService.list(SysRelationParam.builder()
                .relationType(RelationTypeEnum.GROUP_HAS_ROLE.getCode()).objectId(postParam.getCode()).build());
        // 关联对象map(code->RelationVO)
        LinkedHashMap<String, RelationVO> map = new LinkedHashMap<>();
        list.forEach(e -> map.put(e.getTargetId(), RelationVO.builder().code(e.getTargetId())
                .createTime(e.getCreateTime()).createUser(e.getCreateUser()).build()));
        // 查询角色(可指定搜索词)
        List<SysRole> roleList = sysRoleService.list(SysRoleParam.builder().searchKey(postParam.getSearchKey()).codeSet(map.keySet()).build());
        roleList.forEach(role -> {
            map.get(role.getCode()).setName(role.getName());
        });

        return Lists.newArrayList(map.values().iterator());
    }

    @Override
    public List<RelationVO> groupUserList(SysPostParam postParam) {
        // 查询指定group的所有user
        List<SysRelation> list = sysRelationService.list(SysRelationParam.builder()
                .relationType(RelationTypeEnum.GROUP_HAS_USER.getCode()).objectId(postParam.getCode()).build());
        // 关联对象map(account->RelationVO)
        LinkedHashMap<String, RelationVO> map = new LinkedHashMap<>();
        list.forEach(e -> map.put(e.getTargetId(), RelationVO.builder().code(e.getTargetId())
                .createTime(e.getCreateTime()).createUser(e.getCreateUser()).build()));
        // 查询角色(可指定搜索词)
        List<SysUser> userList = sysUserService.list(SysUserParam.builder()
                .searchKey(postParam.getSearchKey())
                .orgCode(postParam.getOrgCode())
                .codeSet(map.keySet()).build());
        userList.forEach(user -> {
            map.get(user.getAccount()).setName(user.getName());
        });

        return Lists.newArrayList(map.values().iterator());
    }

    @Override
    public void groupAddRole(SysPostParam postParam) {
        String objectId = postParam.getCode();
        Set<String> targetSet = postParam.getCodeSet();
        if (ObjectUtil.isEmpty(targetSet)) {
            return;
        }
        // 查询指定group的所有relation
        List<SysRelation> list = sysRelationService.list(SysRelationParam.builder()
                .objectId(objectId).targetSet(targetSet)
                .relationType(RelationTypeEnum.GROUP_HAS_ROLE.getCode()).build()
        );
        Set<String> oldSet = list.stream().map(SysRelation::getTargetId).collect(Collectors.toSet());
        // 要新增的targetId集合
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
    public void groupDeleteRole(SysPostParam postParam) {
        if (ObjectUtil.isEmpty(postParam.getCodeSet())) {
            return;
        }
        // 查询指定group的所有relation
        List<SysRelation> list = sysRelationService.list(SysRelationParam.builder()
                .objectId(postParam.getCode()).targetSet(postParam.getCodeSet())
                .relationType(RelationTypeEnum.GROUP_HAS_ROLE.getCode()).build()
        );
        Set<Long> ids = list.stream().map(SysRelation::getId).collect(Collectors.toSet());
        if (ObjectUtil.isNotEmpty(ids)) {
            sysRelationService.removeByIds(ids);
        }
    }
}
