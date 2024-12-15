package com.moyu.system.sys.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.moyu.common.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色信息表
 *
 * @TableName sys_role
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sys_role")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysRole extends BaseEntity {
    /**
     * 角色ID
     * 注意Long值传递给前端精度丢失问题（JS最大精度整数是Math.pow(2,53)）
     */
    @TableId(value = "id", type = IdType.AUTO)
//    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 编码
     */
    @TableField(value = "code")
    private String code;

    /**
     * 归属模块
     */
    @TableField(value = "module")
    private String module;

    /**
     * 数据范围（0全部数据权限 1自定数据权限 2本部门数据权限 3本部门及以下数据权限）
     */
    @TableField(value = "data_scope")
    private Integer dataScope;

    /**
     * 排序顺序
     */
    @TableField(value = "sort_num")
    private Integer sortNum;

    /**
     * 使用状态（0正常 1停用）
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 删除标志（0未删除  1已删除）
     */
    @TableField(value = "delete_flag")
    private Integer deleteFlag;

    /**
     * 扩展信息
     */
    @TableField(value = "ext_json")
    private String extJson;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

}