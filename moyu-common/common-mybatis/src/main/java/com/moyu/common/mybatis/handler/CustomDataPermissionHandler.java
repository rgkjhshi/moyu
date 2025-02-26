package com.moyu.common.mybatis.handler;


import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.plugins.handler.DataPermissionHandler;
import com.moyu.common.mybatis.annotation.DataPermission;
import com.moyu.common.security.util.SecurityUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;

import java.lang.reflect.Method;

/**
 * 自定义数据权限处理器
 *
 * @author shisong
 * @since 2025-02-25
 */
@Slf4j
public class CustomDataPermissionHandler implements DataPermissionHandler {

    @Override
    @SneakyThrows
    public Expression getSqlSegment(Expression where, String mappedStatementId) {
        // mappedStatementId值类似于：com.moyu.system.sys.mapper.SysOrgMapper.selectList
        Class<?> clazz = Class.forName(mappedStatementId.substring(0, mappedStatementId.lastIndexOf(StringPool.DOT)));
        // 只有Mapper中的方法才会被拦截到
        String methodName = mappedStatementId.substring(mappedStatementId.lastIndexOf(StringPool.DOT) + 1);
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            DataPermission annotation = method.getAnnotation(DataPermission.class);
            // 没有注解，不进行数据权限过滤
            if (annotation == null) {
                return where;
            }
            // root超级管理员不受数据权限控制
            if (SecurityUtils.isRoot()) {
                return where;
            }
            if (ObjectUtils.isNotEmpty(annotation)
                    && (method.getName().equals(methodName) || (method.getName() + "_COUNT").equals(methodName))) {
                return dataScopeFilter(annotation.orgAlias(), annotation.orgColumnName(), annotation.userAlias(), annotation.userColumnName(), where);
            }
        }
        return where;
    }


    /**
     * 构建过滤条件
     *
     * @param where 当前查询条件
     * @return 构建后查询条件
     */
    @SneakyThrows
    public static Expression dataScopeFilter(String orgAlias, String orgColumnName, String userAlias, String userColumnName, Expression where) {
        // 设置数据权限的语句，可在通过拦截注解此处理，也可不通过注解直接在代码中处理
        return where;
    }

}
