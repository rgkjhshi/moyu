package com.moyu.system.sys.model.param;


import com.moyu.common.model.BasePageParam;
import lombok.Data;

/**
 * 组织查询参数
 *
 * @author shisong
 * @since 2024-11-28
 */
@Data
public class SysOrgParam extends BasePageParam {
    /**
     * 父id
     */
    private String pid;

    /**
     * 名称关键词
     */
    private String keywords;
}
