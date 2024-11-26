package com.moyu.system.core.base.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 数据库表通用基础字段实体，需要此通用字段的实体可继承此类
 *
 * @author shisong
 * @since 2024-11-26
 */
@Data
public class BaseEntity implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

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
