package ${packageName}.domain;

import com.dodoyd.moyu.common.model.PageRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * ${entity.tableComment!}请求对象
 *
 * @author ${author!}
 * @since ${.now?string["yyyy-MM-dd"]}
 */
@Getter
@Setter
@ToString
public class ${entity.className}Request extends PageRequest {

<#if columnList??>
    <#list columnList as column>
        /**
         * ${column.comment!}
         */
        private ${column.javaType} ${column.javaName};

    </#list>
</#if>
}
