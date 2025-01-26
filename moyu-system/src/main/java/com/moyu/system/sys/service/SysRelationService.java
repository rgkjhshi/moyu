package com.moyu.system.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moyu.system.sys.model.entity.SysRelation;
import com.moyu.system.sys.model.param.SysRelationParam;

import java.util.List;
import java.util.Set;

/**
 * @author shisong
 * @description 针对表【sys_relation(用户角色权限关系表)】的数据库操作Service
 * @createDate 2024-12-16 21:15:35
 */
public interface SysRelationService extends IService<SysRelation> {

    /**
     * 获取记录列表
     */
    List<SysRelation> list(SysRelationParam param);

    /**
     * 通过(分组-用户、分组-角色、角色-权限)关系查询 用户->角色
     */
    Set<String> userRole(String account);

    /**
     * 通过(用户->分组、分组->角色、角色->权限)关系查询 用户->权限
     */
    Set<String> userPerm(String account);

}
