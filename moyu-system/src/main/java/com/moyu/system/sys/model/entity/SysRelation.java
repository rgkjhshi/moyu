package com.moyu.system.sys.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.Date;

/**
 * 用户角色权限关系表
 *
 * @TableName sys_relation
 */
@Data
@TableName(value = "sys_relation")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysRelation {
    /**
     * 主键id
     * 注意Long值传递给前端精度丢失问题（JS最大精度整数是Math.pow(2,53)）
     */
    @TableId(value = "id", type = IdType.AUTO)
    @JsonSerialize(using = ToStringSerializer.class)
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
     *
     * @see com.moyu.system.sys.enums.RelationTypeEnum
     */
    private Integer relationType;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建用户
     */
    private String createUser;

}