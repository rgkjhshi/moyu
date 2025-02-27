package com.moyu.common.mybatis.handler;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.plugins.handler.DataPermissionHandler;
import com.moyu.common.mybatis.annotation.DataPermission;
import com.moyu.common.security.util.SecurityUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;

import java.lang.reflect.Method;
import java.util.Set;

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
                    && (method.getName().equals(methodName) || (method.getName() + "_mpCount").equals(methodName))) {
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
    public static Expression dataScopeFilter(String orgAlias, String orgColumn, String userAlias, String userColumn, Expression where) {
        // 设置数据权限的语句，可在通过拦截注解此处理，也可不通过注解直接在代码中处理
        String orgColumnName = StrUtil.isNotBlank(orgAlias) ? (orgAlias + StringPool.DOT + orgColumn) : orgColumn;
        // 数据权限范围
        Set<String> scopes = SecurityUtils.getScopes();
        if (ObjectUtil.isEmpty(scopes)) {
            return where;
        }
        String sqlStr = orgColumnName + " IN ('" + CollectionUtil.join(scopes, "', '") + "')";
        Expression appendExpression = CCJSqlParserUtil.parseCondExpression(sqlStr);
        if (where == null) {
            return appendExpression;
        }
        return new AndExpression(where, appendExpression);
    }

}
