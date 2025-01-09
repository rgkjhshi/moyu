package com.moyu.system.sys.model.vo;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 关系视图对象
 *
 * @author shisong
 * @since 2025-01-07
 */
@Getter
@Setter
@ToString
@Builder
public class RelationVO {

    /**
     * 唯一编码，对应objectId
     */
    private String code;

    /**
     * 显示名称
     */
    private String name;

    /**
     * 关系创建时间
     */
    private Date createTime;

    /**
     * 创建用户
     */
    private String createUser;
}
