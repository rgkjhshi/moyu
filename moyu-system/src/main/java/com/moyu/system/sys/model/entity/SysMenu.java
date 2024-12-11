package com.moyu.system.sys.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.moyu.common.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 菜单权限表
 *
 * @TableName sys_menu
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sys_menu")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysMenu extends BaseEntity {
    /**
     * 主键id
     * 注意Long值传递给前端精度丢失问题（JS最大精度整数是Math.pow(2,53)）
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 父菜单ID
     */
    @TableField(value = "pid")
    private Long pid;

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
     * 菜单类型（字典 1模块 2目录 3菜单 4按钮 5外链）
     *
     * @see com.moyu.system.sys.enums.MenuTypeEnum
     */
    @TableField(value = "menu_type")
    private Integer menuType;

    /**
     * 路由地址
     */
    @TableField(value = "path")
    private String path;

    /**
     * 组件地址
     */
    @TableField(value = "component")
    private String component;

    /**
     * 图标
     */
    @TableField(value = "icon")
    private String icon;

    /**
     * 权限标识
     */
    @TableField(value = "permission")
    private String permission;

    /**
     * 是否可见（0不可见 1可见）
     */
    @TableField(value = "visible")
    private Integer visible;

    /**
     * 链接地址
     */
    @TableField(value = "link")
    private String link;

    /**
     * 归属模块
     */
    @TableField(value = "module")
    private String module;

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

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 创建用户
     */
    @TableField(value = "create_user")
    private String createUser;

    /**
     * 修改时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 修改用户
     */
    @TableField(value = "update_user")
    private String updateUser;
}