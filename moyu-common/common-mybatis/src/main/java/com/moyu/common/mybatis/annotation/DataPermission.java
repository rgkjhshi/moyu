package com.moyu.common.mybatis.annotation;


import java.lang.annotation.*;

/**
 * 数据权限注解
 * 数据权限 {@link com.baomidou.mybatisplus.extension.plugins.inner.DataPermissionInterceptor}
 *
 * @author shisong
 * @since 2025-02-25
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface DataPermission {

    /**
     * 用于控制组织机构的表名
     */
    String orgAlias() default "";

    /**
     * 用于控制组织机构的列名
     */
    String orgColumnName() default "org_code";

    /**
     * 用于控制用户的表名
     */
    String userAlias() default "";

    /**
     * 用于控制用户的列名
     */
    String userColumnName() default "create_by";

}
