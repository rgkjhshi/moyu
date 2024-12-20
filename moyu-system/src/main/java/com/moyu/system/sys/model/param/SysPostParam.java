package com.moyu.system.sys.model.param;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.moyu.common.model.BasePageParam;
import lombok.Data;

import java.util.Set;

/**
 * 岗位信息表
 *
 * @TableName sys_pos
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysPostParam extends BasePageParam {
    //********** 额外字段 **********//
    /**
     * 待删除的id列表
     */
    private Set<Long> ids;

    /**
     * 名称关键词
     */
    private String searchKey;

    //********** db中存在的字段 **********//
    /**
     * 主键id
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 编码
     */
    private String code;

    /**
     * 岗位类型(字典 1特有 2通用 3自建)
     */
    private Integer posType;

    /**
     * 直属组织
     */
    private String orgCode;

    /**
     * 所属组织,逗号分隔
     */
    private String orgs;

    /**
     * 排序顺序
     */
    private Integer sortNum;

    /**
     * 状态（0正常 1停用）
     */
    private Integer status;

    /**
     * 删除标志（0未删除  1已删除）
     */
    private Integer deleteFlag;

    /**
     * 扩展信息
     */
    private String extJson;

    /**
     * 备注
     */
    private String remark;

}