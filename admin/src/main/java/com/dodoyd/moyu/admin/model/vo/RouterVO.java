package com.dodoyd.moyu.admin.model.vo;

import java.util.List;

/**
 * 与前端对应的路由对象
 *
 * @author shisong02
 * @link <a href="https://panjiachen.github.io/vue-element-admin-site/zh/guide/essentials/router-and-nav.html">路由属性说明</a>
 * @since 2024-01-12
 */
public class RouterVO {

    /**
     * 路由名字
     */
    private String name;

    /**
     * 路由地址
     */
    private String path;

    /**
     * 组件地址
     */
    private String component;

    /**
     * 是否隐藏路由，当设置 true 的时候该路由不会再侧边栏出现
     */
    private boolean hidden;

    /**
     * 重定向地址，当设置 noRedirect 的时候该路由在面包屑导航中不可被点击
     */
    private String redirect;

    /**
     * 路由参数：如 {"id": 1, "name": "ry"}
     */
    private String query;

    /**
     * 当你一个路由下面的 children 声明的路由大于1个时，自动会变成嵌套的模式。为true时会一直显示根路由
     */
    private Boolean alwaysShow;

    /**
     * 路由属性元素
     */
    private MetaVO meta;

    /**
     * 子路由节点
     */
    private List<RouterVO> children;

}
