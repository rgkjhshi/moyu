package com.moyu.system.sys.model.param;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.*;

import java.util.Date;
import java.util.Set;

/**
 * 关系映射查询参数
 */
@Getter
@Setter
@ToString
@Builder
public class SysRelationParam {
    //********** 额外字段 **********//
    /**
     * 待删除的id列表
     */
    private Set<Long> ids;
    /**
     * objectId的集合
     */
    private Set<String> objectSet;
    /**
     * targetId的集合
     */
    private Set<String> targetSet;
    /**
     * 名称关键词
     */
    private String searchKey;

    //********** db中存在的字段 **********//
    /**
     * 主键id
     */
    private Long id;

    /**
     * 对象ID
     */
    private String objectId;

    /**
     * 目标ID
     */
    private String targetId;

    /**
     * 关系类型(字典 1:group_has_user,2:group_has_role,3:role_has_menu)
     * @see com.moyu.system.sys.enums.RelationTypeEnum
     */
    private Integer relationType;

}