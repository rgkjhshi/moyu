package com.moyu.system.sys.model.param;


import lombok.Data;

/**
 * 组织查询参数
 *
 * @author shisong
 * @since 2024-11-28
 */
@Data
public class SysOrgParam {

    /**
     * 当前页
     */
    private Integer current;

    /**
     * 每页条数
     */
    private Integer size;

    /**
     * 父id
     */
    private String pid;

    /**
     * 名称关键词
     */
    private String keywords;
}
