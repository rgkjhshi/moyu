package com.moyu.system.sys.model.param;


import com.moyu.common.model.BasePageParam;
import lombok.Data;

import java.util.Set;

/**
 * 组织查询参数
 *
 * @author shisong
 * @since 2024-11-28
 */
@Data
public class SysOrgParam extends BasePageParam {
    //********** 额外字段 **********//
    /**
     * 待删除的code列表，通常会集联删除
     */
    private Set<String> codes;

    /**
     * 待删除的id列表，通常不会集联删除
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
     * 父节点编码
     */
    private String parentCode;

    /**
     * 名称
     */
    private String name;

    /**
     * 编码
     */
    private String code;

    /**
     * 组织机构类型(字典 1公司组织 2部门机构 3虚拟节点)
     *
     * @see com.moyu.system.sys.enums.OrgTypeEnum
     */
    private Integer orgType;

    /**
     * 组织层级(字典 1一级公司 2二级公司 3三级公司)
     */
    private Integer orgLevel;

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
