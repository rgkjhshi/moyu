package com.dodoyd.moyu.admin.dao;

import com.dodoyd.moyu.admin.model.vo.ColumnInfo;
import com.dodoyd.moyu.admin.model.vo.TableInfo;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 生成表格数据访问接口
 *
 * @author shisong02
 * @since 2023-12-19
 */
@Mapper
public interface GenCodeDao {

    /**
     * 查询数据库中的定义的表
     */
    @Results(id = "tableResult", value = {
            @Result(property = "tableName", column = "table_name"),
            @Result(property = "tableComment", column = "table_comment"),
            @Result(property = "createTime", column = "create_time"),
            @Result(property = "updateTime", column = "update_time"),
    })
    @Select({"<script>",
            "SELECT table_name, table_comment, create_time, update_time FROM information_schema.tables",
            "WHERE table_schema = (SELECT DATABASE())",
            "</script>"})
    List<TableInfo> selectAllTableList();

    /**
     * 查询指定表名的表信息
     */
    @ResultMap("tableResult")
    @Select({"<script>",
            "SELECT table_name, table_comment, create_time, update_time FROM information_schema.tables",
            "<where>",
            "    table_schema = (SELECT DATABASE())",
            "    AND table_name IN ",
            "    <foreach collection='list' item='item' separator=',' open='(' close=')'>",
            "        #{item} ",
            "    </foreach>",
            "</where>",
            "</script>"})
    List<TableInfo> selectTableListByNameList(List<String> nameList);

    /**
     * 查询指定表名的表信息
     */
    @ResultMap("tableResult")
    @Select({"<script>",
            "SELECT table_name, table_comment, create_time, update_time FROM information_schema.tables",
            "WHERE table_schema = (SELECT DATABASE()) AND table_name = #{tableName}",
            "</script>"})
    TableInfo selectTableByName(String tableName);

    /**
     * 查询指定表名的列信息
     */
    @Results(id = "columnResult", value = {
            @Result(property = "columnName", column = "column_name"),
            @Result(property = "jdbcType", column = "data_type"),
            @Result(property = "comment", column = "column_comment"),
    })
    @Select({"<script>",
            "SELECT column_name, data_type, column_comment  FROM information_schema.columns",
            "WHERE table_schema = (SELECT DATABASE()) AND table_name = #{tableName}",
            "ORDER BY ordinal_position",
            "</script>"})
    List<ColumnInfo> selectColumnListByTableName(String tableName);

    /**
     * 查询主键列(可能存在联合主键的情形，因此结果为list)
     */
    @ResultMap("columnResult")
    @Select({"<script>",
            "SELECT column_name, data_type, column_comment  FROM information_schema.columns",
            "WHERE table_schema = (SELECT DATABASE()) AND table_name = #{tableName} AND column_key = 'PRI'",
            "</script>"})
    List<ColumnInfo> selectPkColumnListByTableName(String tableName);
}
