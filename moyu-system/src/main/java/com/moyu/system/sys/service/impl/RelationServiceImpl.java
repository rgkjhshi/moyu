package com.moyu.system.sys.service.impl;


import com.google.common.collect.Lists;
import com.moyu.system.sys.enums.RelationTypeEnum;
import com.moyu.system.sys.model.entity.SysRelation;
import com.moyu.system.sys.model.entity.SysRole;
import com.moyu.system.sys.model.param.SysRelationParam;
import com.moyu.system.sys.model.param.SysRoleParam;
import com.moyu.system.sys.model.vo.RelationVO;
import com.moyu.system.sys.service.RelationService;
import com.moyu.system.sys.service.SysRelationService;
import com.moyu.system.sys.service.SysRoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.List;

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


    @Override
    public List<RelationVO> groupRoleList(SysRelationParam param) {
        // 查询指定group的所有relation
        List<SysRelation> list = sysRelationService.list(SysRelationParam.builder()
                .relationType(RelationTypeEnum.GROUP_HAS_ROLE.getCode()).objectId(param.getObjectId()).build());
        // 关联对象map(code->RelationVO)
        LinkedHashMap<String, RelationVO> map = new LinkedHashMap<>();
        list.forEach(e -> map.put(e.getTargetId(), RelationVO.builder().code(e.getTargetId())
                .createTime(e.getCreateTime()).createUser(e.getCreateUser()).build()));
        // 查询角色
        List<SysRole> roleList = sysRoleService.list(SysRoleParam.builder().searchKey(param.getSearchKey()).codeSet(map.keySet()).build());
        roleList.forEach(role -> {
            map.get(role.getCode()).setName(role.getName());
        });

        return Lists.newArrayList(map.values().iterator());
    }
}
