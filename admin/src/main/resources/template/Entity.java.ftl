package ${packageName}.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * ${modelName}
 *
 * @author: YZ.YANG
 * @date: ${.now?string["yyyy-MM-dd HH:mm:ss"]}
 */
@Data
public class ${modelName}{

<#if columns??>
    <#list columns as column>
        <#if column.type='VARCHAR'||column.type='TEXT'||column.type='CHAR'>
            /**
             * ${column.remark}
             */
            private String ${column.propertyName?uncap_first};
        </#if>
        <#if column.type='INT'>
            /**
             * ${column.remark}
             */
            private Integer ${column.propertyName?uncap_first};
        </#if>
        <#if column.type='DATETIME'>
            /**
             * ${column.remark}
             */
            private Date ${column.propertyName?uncap_first};
        </#if>
        <#if column.type='BIGINT'>
            /**
             * ${column.remark}
             */
            private Long ${column.propertyName?uncap_first};
        </#if>
        <#if column.type='DOUBLE'>
            /**
             * ${column.remark}
             */
            private Double ${column.propertyName?uncap_first};
        </#if>
        <#if column.type='BIT'>
            /**
             * ${column.remark}
             */
            private Boolean ${column.propertyName?uncap_first};
        </#if>
    </#list>
</#if>
}
