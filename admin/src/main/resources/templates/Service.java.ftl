package ${packageName}.service;

import ${packageName}.domain.${entity.className};
import ${packageName}.model.request.${entity.className}Request;
import com.dodoyd.moyu.common.model.PageResult;

import java.util.List;

/**
 * ${entity.className}服务
 *
 * @author ${author!}
 * @since ${.now?string["yyyy-MM-dd"]}
 */
public interface ${entity.className}Service {

    /**
     * 通过主键查询${entity.className}
     *
     * @param ${entity.pkColumn.javaName} 主键
     * @return ${entity.className}
     */
    ${entity.className} query${entity.className}By${entity.pkColumn.javaName?cap_first}(${entity.pkColumn.javaType} ${entity.pkColumn.javaName});

    /**
     * 查询${entity.className}
     *
     * @param request 查询请求
     * @return ${entity.className}
     */
    ${entity.className} query${entity.className}(${entity.className}Request request);

    /**
     * 查询${entity.className}列表
     *
     * @param request 查询请求
     * @return ${entity.className}列表
     */
    PageResult<${entity.className}> query${entity.className}List(${entity.className}Request request);

    /**
     * 新增${entity.className}
     *
     * @param request 请求参数
     * @return 结果
     */
    int add${entity.className}(${entity.className}Request request);

    /**
     * 修改${entity.className}
     *
     * @param request 请求参数
     * @return 结果
     */
    int update${entity.className}(${entity.className}Request request);

    /**
     * 删除${entity.className}
     *
     * @param ${entity.pkColumn.javaName} 主键
     * @return 结果
     */
    int delete${entity.className}(${entity.pkColumn.javaType} ${entity.pkColumn.javaName});

    /**
     * 批量删除${entity.className}
     *
     * @param ${entity.pkColumn.javaName}List 主键列表
     * @return 结果
     */
    int batchDelete${entity.className}(List<${entity.pkColumn.javaType}> ${entity.pkColumn.javaName}List);

}

