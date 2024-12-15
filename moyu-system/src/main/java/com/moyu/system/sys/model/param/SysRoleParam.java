package com.moyu.system.sys.model.param;

import com.moyu.common.model.BasePageParam;
import lombok.Data;

import java.util.Set;

/**
 * 角色信息查询参数
 */
@Data
public class SysRoleParam extends BasePageParam {
    //********** 额外字段 **********//
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
     * 主键ID
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
     * 归属模块
     */
    private String module;

    /**
     * 数据范围（0全部数据权限 1自定数据权限 2本部门数据权限 3本部门及以下数据权限）
     */
    private Integer dataScope;

    /**
     * 排序顺序
     */
    private Integer sortNum;

    /**
     * 使用状态（0正常 1停用）
     */
    private Integer status;

    /**
     * 扩展信息
     */
    private String extJson;

    /**
     * 备注
     */
    private String remark;

}