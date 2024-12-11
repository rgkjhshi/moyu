package com.moyu.system.sys.model.param;


import com.moyu.common.model.BasePageParam;
import lombok.Data;

/**
 * 菜单查询参数
 *
 * @author shisong
 * @since 2024-11-28
 */
@Data
public class SysMenuParam extends BasePageParam {
    /**
     * 父节点id
     */
    private String parentId;

    /**
     * 名称关键词
     */
    private String searchKey;

    /**
     * 菜单类型（字典 1模块 2目录 3菜单 4按钮 5外链）
     *
     * @see com.moyu.system.sys.enums.MenuTypeEnum
     */
    private Integer menuType;

}
