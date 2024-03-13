package ${packageName}.domain;

import lombok.Data;

import java.util.Date;

/**
 * ${entity.tableComment!}实体模型
 *
 * @author ${author!}
 * @since ${.now?string["yyyy-MM-dd"]}
 */
@Data
public class ${entity.className} {

<#if columnList??>
    <#list columnList as column>
    /**
     * ${column.comment!}
     */
    private ${column.javaType} ${column.javaName};

    </#list>
</#if>
}
