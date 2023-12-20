<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${packageName}.mapper.${mapperName}">

    <resultMap id="BaseResultMap" type="${packageName}.model.${modelName}">
    <#list columns as column>
        <<#if column.primary??>id<#else>result</#if> column="${column.columnName}" property="${column.propertyName?uncap_first}" jdbcType="<#if column.type='INT'>INTEGER<#elseif column.type='DATETIME'>TIMESTAMP<#elseif column.type='TEXT'>VARCHAR<#else>${column.type}</#if>" />
    </#list>
    </resultMap>


    <select id="select${modelName}All" resultMap="BaseResultMap">
        select * from ${tableName};
    </select>


    <select id="select${modelName}ById" resultType="${packageName}.model.${modelName}">
        select * from ${tableName} where id = <#noparse>#{</#noparse>id<#noparse>}</#noparse>;
    </select>


    <insert id="insert${modelName}">
        insert into ${tableName} (<#list columns as column>${column.columnName},</#list>)
        values( <#list columns as column> <#noparse>#{</#noparse>${column.propertyName?uncap_first}<#noparse>}</#noparse>,</#list>);
    </insert>


    <update id="update${modelName}">
        update ${tableName} set
           <#list columns as column>
               ${column.columnName}=${column.propertyName?uncap_first},
           </#list>
         where id = <#noparse>#{</#noparse>id<#noparse>}</#noparse>;
    </update>


    <delete id="delete${modelName}ById">
        delete * from ${tableName} where id = <#noparse>#{</#noparse>id<#noparse>}</#noparse>;
    </delete>

</mapper>
