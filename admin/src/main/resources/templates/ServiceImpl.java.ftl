package ${packageName}.service.impl;

import ${packageName}.dao.${entity.className}Dao;
import ${packageName}.domain.${entity.className};
import ${packageName}.model.request.${entity.className}Request;
import ${packageName}.service.${entity.className}Service;
import com.dodoyd.moyu.common.model.PageResult;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.base.Strings;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * ${entity.className}服务实现类
 *
 * @author ${author!}
 * @since ${.now?string["yyyy-MM-dd"]}
 */
@Service
public class ${entity.className}ServiceImpl implements ${entity.className}Service {

    @Resource
    private ${entity.className}Dao ${entity.className?uncap_first}Dao;

    @Override
    public ${entity.className} query${entity.className}By${entity.pkColumn.javaName?cap_first}(${entity.pkColumn.javaType} ${entity.pkColumn.javaName}) {
        return ${entity.className?uncap_first}Dao.selectBy${entity.pkColumn.javaName?cap_first}(${entity.pkColumn.javaName});
    }

    @Override
    public ${entity.className} query${entity.className}(${entity.className}Request request) {
        ${entity.className} query = new ${entity.className}();
        <#list columnList as column>
            <#if (column.javaType!"") == "String">
        if (!Strings.isNullOrEmpty(request.get${column.javaName?cap_first}())) {
            <#else >
        if (request.get${column.javaName?cap_first}() != null) {
            </#if>
            query.set${column.javaName?cap_first}(request.get${column.javaName?cap_first}());
        }
        </#list>
        ${entity.className} ${entity.className?uncap_first} = ${entity.className?uncap_first}Dao.selectOne(query);
        return ${entity.className?uncap_first};
    }

    @Override
    public PageResult<${entity.className}> query${entity.className}List(${entity.className}Request request) {
        ${entity.className} query = new ${entity.className}();
        <#list columnList as column>
            <#if (column.javaType!"") == "String">
        if (!Strings.isNullOrEmpty(request.get${column.javaName?cap_first}())) {
            <#else >
        if (request.get${column.javaName?cap_first}() != null) {
            </#if>
            query.set${column.javaName?cap_first}(request.get${column.javaName?cap_first}());
        }
        </#list>
        PageHelper.startPage(request.getPageNum(), request.getPageSize());
        Page<${entity.className}> page = (Page<${entity.className}>) ${entity.className?uncap_first}Dao.selectList(query);
        // 分页结果
        PageResult<${entity.className}> pageResult = new PageResult<>();
        pageResult.setPageNum(page.getPageNum());
        pageResult.setTotal(page.getTotal());
        pageResult.setPageData(page.getResult());
        return pageResult;
    }

    @Override
    public int add${entity.className}(${entity.className}Request request) {
        ${entity.className} add = new ${entity.className}();
        <#list columnList as column>
            <#if (column.javaType!"") == "String">
        if (!Strings.isNullOrEmpty(request.get${column.javaName?cap_first}())) {
            <#else >
        if (request.get${column.javaName?cap_first}() != null) {
            </#if>
            add.set${column.javaName?cap_first}(request.get${column.javaName?cap_first}());
        }
        </#list>
        int row = ${entity.className?uncap_first}Dao.insert(add);
        return row;
    }

    @Override
    public int update${entity.className}(${entity.className}Request request) {
        ${entity.className} update = new ${entity.className}();
        <#list columnList as column>
            <#if (column.javaType!"") == "String">
        if (!Strings.isNullOrEmpty(request.get${column.javaName?cap_first}())) {
            <#else >
        if (request.get${column.javaName?cap_first}() != null) {
            </#if>
            update.set${column.javaName?cap_first}(request.get${column.javaName?cap_first}());
        }
        </#list>
        int row = ${entity.className?uncap_first}Dao.updateBy${entity.pkColumn.javaName?cap_first}(update);
        return row;
    }

    @Override
    public int delete${entity.className}(${entity.pkColumn.javaType} ${entity.pkColumn.javaName}) {
        int row = ${entity.className?uncap_first}Dao.deleteBy${entity.pkColumn.javaName?cap_first}(${entity.pkColumn.javaName});
        return row;
    }

    @Override
    public int batchDelete${entity.className}(List<${entity.pkColumn.javaType}> ${entity.pkColumn.javaName}List) {
        int row = ${entity.className?uncap_first}Dao.deleteBy${entity.pkColumn.javaName?cap_first}List(${entity.pkColumn.javaName}List);
        return row;
    }
}
