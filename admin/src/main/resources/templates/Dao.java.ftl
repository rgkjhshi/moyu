package ${packageName}.dao;

import ${packageName}.domain.${entity.className};
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * ${entity.className}数据访问对象
 *
 * @author ${author!}
 * @since ${.now?string["yyyy-MM-dd"]}
 */
@Mapper
public interface ${entity.className}Dao {

    /**
     * 添加记录
     *
     * @param ${entity.className?uncap_first} 数据库实体
     * @return 返回受影响的记录条数
     */
    @Insert({"<script>",
            "INSERT INTO ${entity.tableName}",
            "<trim prefix='(' suffix=')' suffixOverrides=','>",
            <#list columnList as column>
            "    <if test='${column.javaName} != null'>${column.columnName},</if>",
            </#list>
            "</trim>",
            "<trim prefix='VALUES (' suffix=')' suffixOverrides=','>",
            <#list columnList as column>
            "    <if test='${column.javaName} != null'>${r'#{'}${column.javaName}},</if>",
            </#list>
            "</trim>",
            "</script>"})
    @Options(useGeneratedKeys = true)
    int insert(${entity.className} ${entity.className?uncap_first});

    /**
     * 通过主键id删除
     *
     * @param id 主键id
     * @return 返回受影响的记录条数
     */
    @Delete("DELETE FROM ${entity.tableName} WHERE id = <#noparse>#{id}</#noparse>")
    int deleteById(Long id);

    /**
     * 通过主键列表删除
     *
     * @param idList 主键id列表
     * @return 返回受影响的记录条数
     */
    @Delete({"<script>",
            "DELETE FROM ${entity.tableName} WHERE id IN ",
            "<foreach collection='list' item='item' open='(' separator=',' close=')'>",
            "    <#noparse>#{item}</#noparse> ",
            "</foreach>",
            "</script>"})
    int deleteByIdList(List<Long> idList);

    /**
     * 通过主键id更新
     *
     * @param ${entity.className?uncap_first} 数据库实体
     * @return 返回受影响的记录条数
     */
    @Update({"<script>",
            "UPDATE ${entity.tableName}",
            "<set>",
            <#list columnList as column>
            "    <if test='${column.javaName} != null'>${column.columnName} = ${r'#{'}${column.javaName}},</if>",
            </#list>
            "</set>",
            "WHERE id = <#noparse>#{id}</#noparse>",
            "</script>"})
    int updateById(${entity.className} ${entity.className?uncap_first});

    /**
     * 查询所有
     *
     * @return 查询到的结果, 无结果将返回null
     */
    @Results(id = "baseResult", value = {
        <#list columnList as column>
            @Result(property = "${column.javaName}", column = "${column.columnName}"),
        </#list>
    })
    @Select("SELECT * FROM ${entity.tableName}")
    ${entity.className} selectAll();

    /**
     * 通过主键id查询
     *
     * @param id 主键id
     * @return 查询到的结果, 无结果将返回null
     */
    @ResultMap("baseResult")
    @Select("SELECT * FROM ${entity.tableName} WHERE id = <#noparse>#{id}</#noparse>")
    ${entity.className} selectById(Long id);

    /**
     * 查询一条记录, 自行控制条件保证返回一条记录
     *
     * @param ${entity.className?uncap_first} 实体的非空属性会做为查询条件
     * @return 查询到的结果, 无结果将返回null
     */
    @ResultMap("baseResult")
    @Select({"<script>",
            "SELECT * FROM ${entity.tableName}",
            "<where>",
            <#list columnList as column>
            "    <if test='${column.javaName} != null'>AND ${column.columnName} = ${r'#{'}${column.javaName}}</if>",
            </#list>
            "</where>",
            "</script>"})
    ${entity.className} selectOne(${entity.className} ${entity.className?uncap_first});

    /**
     * 查询多条记录, 自行控制条件保证返回多条记录
     *
     * @param ${entity.className?uncap_first} 实体的非空属性会作为查询条件
     * @return 查询到的结果, 无结果将返回空List
     */
    @ResultMap("baseResult")
    @Select({"<script>",
            "SELECT * FROM ${entity.tableName}",
            "<where>",
            <#list columnList as column>
            "    <if test='${column.javaName} != null'>AND ${column.columnName} = ${r'#{'}${column.javaName}}</if>",
            </#list>
            "</where>",
            "</script>"})
    List<${entity.className}> selectList(${entity.className} ${entity.className?uncap_first});

}
