package com.moyu.system.sys.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.moyu.system.sys.enums.RelationTypeEnum;
import com.moyu.system.sys.mapper.SysRelationMapper;
import com.moyu.system.sys.model.entity.SysRelation;
import com.moyu.system.sys.model.entity.SysRole;
import com.moyu.system.sys.model.param.SysRelationParam;
import com.moyu.system.sys.model.param.SysRoleParam;
import com.moyu.system.sys.model.vo.RelationVO;
import com.moyu.system.sys.service.SysRelationService;
import com.moyu.system.sys.service.SysRoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

/**
 * @author shisong
 * @description 针对表【sys_relation(用户角色权限关系表)】的数据库操作Service实现
 * @createDate 2024-12-16 21:15:35
 */
@Service
public class SysRelationServiceImpl extends ServiceImpl<SysRelationMapper, SysRelation> implements SysRelationService {

    @Resource
    private SysRoleService sysRoleService;

    @Override
    public Set<String> userRole(String account) {
        // 用户所属分组
        Set<String> groupSet = new HashSet<>();
        // 查询用户归属的所有分组
        list(new LambdaQueryWrapper<SysRelation>()
                // 关系类型
                .eq(SysRelation::getRelationType, RelationTypeEnum.GROUP_HAS_USER.getCode())
                // 查询group
                .select(SysRelation::getObjectId)
                // 指定用户
                .eq(SysRelation::getTargetId, account)
        ).forEach(e -> groupSet.add(e.getObjectId()));
        // 角色集
        Set<String> roleSet = new HashSet<>();
        if (ObjectUtil.isNotEmpty(groupSet)) {
            // 查询分组的所有角色
            list(new LambdaQueryWrapper<SysRelation>()
                    // 关系类型
                    .eq(SysRelation::getRelationType, RelationTypeEnum.GROUP_HAS_ROLE.getCode())
                    // 查询role
                    .select(SysRelation::getTargetId)
                    // 指定group
                    .in(SysRelation::getObjectId, groupSet)
            ).forEach(e -> roleSet.add(e.getTargetId()));
        }
        return roleSet;
    }

    @Override
    public Set<String> userMenu(String account) {
        // 用户的角色集
        Set<String> roleSet = userRole(account);
        // 权限集
        Set<String> menuSet = new HashSet<>();
        if (ObjectUtil.isNotEmpty(roleSet)) {
            // 查询分组的所有角色
            list(new LambdaQueryWrapper<SysRelation>()
                    // 关系类型
                    .eq(SysRelation::getRelationType, RelationTypeEnum.ROLE_HAS_MENU.getCode())
                    // 查询menu
                    .select(SysRelation::getTargetId)
                    // 指定role
                    .in(SysRelation::getObjectId, roleSet)
            ).forEach(e -> menuSet.add(e.getTargetId()));
        }
        return menuSet;
    }

    @Override
    public List<RelationVO> groupRoleList(SysRelationParam param) {
        // 所有的关系(code->RelationVO)
        LinkedHashMap<String, RelationVO> map = new LinkedHashMap<>();
        list(new LambdaQueryWrapper<SysRelation>()
                // 指定关系类型
                .eq(SysRelation::getRelationType, RelationTypeEnum.GROUP_HAS_ROLE.getCode())
                // 指定objectId
                .eq(SysRelation::getObjectId, param.getObjectId()))
                .forEach(e -> map.put(e.getTargetId(), RelationVO.builder().code(e.getTargetId())
                        .createTime(e.getCreateTime()).createUser(e.getCreateUser()).build()));
        // 查询角色
        List<SysRole> roleList = sysRoleService.list(SysRoleParam.builder().searchKey(param.getSearchKey()).codeSet(map.keySet()).build());
        roleList.forEach(role -> {
            map.get(role.getCode()).setName(role.getName());
        });

        return Lists.newArrayList(map.values().iterator());
    }

}




