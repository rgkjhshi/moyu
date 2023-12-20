package com.dodoyd.moyu.admin.dao;

import com.dodoyd.moyu.admin.model.vo.TableInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

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
    @Results(id = "baseResult", value = {
            @Result(property = "tableName", column = "table_name"),
            @Result(property = "tableComment", column = "table_comment"),
            @Result(property = "createTime", column = "create_time"),
            @Result(property = "updateTime", column = "update_time"),
    })
    @Select({"<script>",
            "SELECT table_name, table_comment, create_time FROM information_schema.tables",
            "WHERE table_schema = (SELECT DATABASE())",
            "</script>"})
    List<TableInfo> selectAllDbTableList();
}
