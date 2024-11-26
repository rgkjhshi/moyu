package com.moyu.system.modular.org.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.moyu.system.core.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 组织机构表
 *
 * @TableName sys_org
 */
@TableName(value = "sys_org")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysOrg extends BaseEntity {
    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 父id
     */
    @TableField(value = "pid")
    private Long pid;

    /**
     * 父名称
     */
    @TableField(value = "parent_name")
    private String parentName;

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
     * 组织机构类别(字典 0公司组织 1部门机构 2虚拟节点)
     */
    @TableField(value = "category")
    private Integer category;

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