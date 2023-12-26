package ${packageName}.service.impl;

import ${packageName}.domain.${entity.className};
import ${packageName}.dao.${entity.className}Dao;
import ${packageName}.service.${entity.className}Service;

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
    private ${entity.className}Service ${entity.className?uncap_first}Service;

    @Override
    public ${entity.className} query${entity.className}(${entity.className} request) {
        ${entity.className} query = new ${entity.className}();
        <#list columnList as column>
        if(request.get${column.javaName?cap_first}() != null) {
            query.set${column.javaName?cap_first}(request.get${column.javaName?cap_first}());
        }
        </#list>
        ${entity.className} ${entity.className?uncap_first} = ${entity.className}Dao.selectOne(query);
        return ${entity.className?uncap_first};
    }

    @Override
    public List<${entity.className}> query${entity.className}List(${entity.className} request) {
        ${entity.className} query = new ${entity.className}();
        <#list columnList as column>
        if(request.get${column.javaName?cap_first}() != null) {
            query.set${column.javaName?cap_first}(request.get${column.javaName?cap_first}());
        }
        </#list>
        List<${entity.className}> list = ${entity.className}Dao.selectList(query);
        return list;
    }

    @Override
    public int add${entity.className}(${entity.className} request) {
        ${entity.className} add =  = new ${entity.className}();
        <#list columnList as column>
        if(request.get${column.javaName?cap_first}() != null) {
            add.set${column.javaName?cap_first}(request.get${column.javaName?cap_first}());
        }
        </#list>
        int row = ${entity.className}Dao.insert(${entity.className?uncap_first});
        return row;
    }

    @Override
    public int update${entity.className}(${entity.className} request) {
        ${entity.className} update = new ${entity.className}();
        <#list columnList as column>
        if(request.get${column.javaName?cap_first}() != null) {
            update.set${column.javaName?cap_first}(request.get${column.javaName?cap_first}());
        }
        </#list>
        int row = ${entity.className}Dao.updateById(update);
        return row;
    }

}
