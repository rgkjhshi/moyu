package com.moyu.system.sys.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.moyu.common.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 组织机构表
 *
 * @TableName sys_org
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sys_org")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysOrg extends BaseEntity {
    /**
     * 主键id
     * 注意Long值传递给前端精度丢失问题（JS最大精度整数是Math.pow(2,53)）
     */
    @TableId(value = "id", type = IdType.AUTO)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 父编码
     */
    @TableField(value = "parent_code")
    private String parentCode;

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
     * 组织机构类型(字典 1公司组织 2部门机构 3虚拟节点)
     *
     * @see com.moyu.system.sys.enums.OrgTypeEnum
     */
    @TableField(value = "org_type")
    private Integer orgType;

    /**
     * 组织层级(字典 1一级公司 2二级公司 3三级公司)
     */
    @TableField(value = "org_level")
    private Integer orgLevel;

    /**
     * 排序顺序
     */
    @TableField(value = "sort_num")
    private Integer sortNum;

    /**
     * 状态（0正常 1停用）
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