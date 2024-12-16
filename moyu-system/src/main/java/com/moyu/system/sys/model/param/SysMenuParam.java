package com.moyu.system.sys.model.param;


import com.moyu.common.model.BasePageParam;
import lombok.Data;

import java.util.Set;

/**
 * 菜单查询参数
 *
 * @author shisong
 * @since 2024-11-28
 */
@Data
public class SysMenuParam extends BasePageParam {
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
     * 角色code
     */
    private String roleCode;

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
     * 父编码
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
     * 菜单类型（字典 1模块 2目录 3菜单 4按钮 5外链）
     *
     * @see com.moyu.system.sys.enums.MenuTypeEnum
     */
    private Integer menuType;

    /**
     * 路由地址
     */
    private String path;

    /**
     * 组件地址
     */
    private String component;

    /**
     * 图标
     */
    private String icon;

    /**
     * 权限标识
     */
    private String permission;

    /**
     * 是否可见（0不可见 1可见）
     */
    private Integer visible;

    /**
     * 归属模块
     */
    private String module;

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
