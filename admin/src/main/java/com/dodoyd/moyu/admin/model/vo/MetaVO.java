package com.dodoyd.moyu.admin.model.vo;

import lombok.Data;

import java.util.List;

/**
 * 路由属性对象
 *
 * @author shisong02
 * @since 2024-01-12
 */
@Data
public class MetaVO {

    /**
     * 设置该路由在侧边栏和面包屑中展示的名字
     */
    private String title;

    /**
     * 设置该路由的图标，对应路径src/assets/icons/svg
     */
    private String icon;

    /**
     * 设置为true，则不会被 <keep-alive>缓存
     */
    private boolean noCache;

    /**
     * 设置该路由进入的权限，支持多个权限叠加
     */
    private List<String> roles;

    /**
     * 内链地址（http(s)://开头）
     */
    private String link;

}
