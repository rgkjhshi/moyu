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

import java.io.Serializable;

/**
 * 岗位信息表
 *
 * @TableName sys_post
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sys_post")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysPost extends BaseEntity implements Serializable {
    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
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
     * 岗位类型(字典 1特有 2通用 3自建)
     */
    @TableField(value = "post_type")
    private Integer postType;

    /**
     * 直属组织
     */
    @TableField(value = "org_code")
    private String orgCode;

    /**
     * 所属组织,逗号分隔
     */
    @TableField(value = "orgs")
    private String orgs;

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