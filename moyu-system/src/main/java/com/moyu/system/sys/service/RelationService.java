package com.moyu.system.sys.service;

import com.moyu.system.sys.model.param.SysRelationParam;
import com.moyu.system.sys.model.vo.RelationVO;

import java.util.List;

/**
 * 关联对象的服务类(关联对象是指存在映射关系的实体)
 */
public interface RelationService {

    /**
     * 获取记录列表
     */
    List<RelationVO> groupRoleList(SysRelationParam param);

}
