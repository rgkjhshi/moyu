package ${packageName}.domain;

import java.util.Date;

import lombok.Data;

/**
 * ${tableComment}实体模型
 *
 * @author shisong02
 * @since ${.now?string["yyyy-MM-dd"]}
 */
@Data
public class ${className} {

<#if columnList??>
    <#list columnList as column>

        /**
         * ${columnComment!}
         */
        private ${javaType} ${javaName};
    </#list>
</#if>
}
