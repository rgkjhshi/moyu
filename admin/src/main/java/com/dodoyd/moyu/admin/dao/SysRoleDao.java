package com.dodoyd.moyu.admin.dao;

import com.dodoyd.moyu.admin.domain.SysRole;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * SysRole数据访问对象
 *
 * @author moyusi
 * @since 2024-01-05
 */
@Mapper
public interface SysRoleDao {

    /**
     * 添加记录
     *
     * @param sysRole 数据库实体
     * @return 返回受影响的记录条数
     */
    @Insert({"<script>",
            "INSERT INTO sys_role",
            "<trim prefix='(' suffix=')' suffixOverrides=','>",
            "    <if test='id != null'>id,</if>",
            "    <if test='roleName != null'>role_name,</if>",
            "    <if test='roleKey != null'>role_key,</if>",
            "    <if test='sortOrder != null'>sort_order,</if>",
            "    <if test='status != null'>status,</if>",
            "    <if test='deleted != null'>deleted,</if>",
            "    <if test='createBy != null'>create_by,</if>",
            "    <if test='updateBy != null'>update_by,</if>",
            "    <if test='createTime != null'>create_time,</if>",
            "    <if test='updateTime != null'>update_time,</if>",
            "    <if test='remark != null'>remark,</if>",
            "</trim>",
            "<trim prefix='VALUES (' suffix=')' suffixOverrides=','>",
            "    <if test='id != null'>#{id},</if>",
            "    <if test='roleName != null'>#{roleName},</if>",
            "    <if test='roleKey != null'>#{roleKey},</if>",
            "    <if test='sortOrder != null'>#{sortOrder},</if>",
            "    <if test='status != null'>#{status},</if>",
            "    <if test='deleted != null'>#{deleted},</if>",
            "    <if test='createBy != null'>#{createBy},</if>",
            "    <if test='updateBy != null'>#{updateBy},</if>",
            "    <if test='createTime != null'>#{createTime},</if>",
            "    <if test='updateTime != null'>#{updateTime},</if>",
            "    <if test='remark != null'>#{remark},</if>",
            "</trim>",
            "</script>"})
    @Options(useGeneratedKeys = true)
    int insert(SysRole sysRole);

    /**
     * 通过主键id删除
     *
     * @param id 主键id
     * @return 返回受影响的记录条数
     */
    @Delete("DELETE FROM sys_role WHERE id = #{id}")
    int deleteById(Long id);

    /**
     * 通过主键id列表删除
     *
     * @param id 主键id
     * @return 返回受影响的记录条数
     */
    @Delete({"<script>",
            "DELETE FROM sys_role WHERE id IN ",
            "<foreach collection='list' item='item' open='(' separator=',' close=')'>",
            "    #{item} ",
            "</foreach>",
            "</script>"})
    int deleteByIdList(List<Long> idList);

    /**
     * 通过主键id更新
     *
     * @param sysRole 数据库实体
     * @return 返回受影响的记录条数
     */
    @Update({"<script>",
            "UPDATE sys_role",
            "<set>",
            "    <if test='id != null'>id = #{id},</if>",
            "    <if test='roleName != null'>role_name = #{roleName},</if>",
            "    <if test='roleKey != null'>role_key = #{roleKey},</if>",
            "    <if test='sortOrder != null'>sort_order = #{sortOrder},</if>",
            "    <if test='status != null'>status = #{status},</if>",
            "    <if test='deleted != null'>deleted = #{deleted},</if>",
            "    <if test='createBy != null'>create_by = #{createBy},</if>",
            "    <if test='updateBy != null'>update_by = #{updateBy},</if>",
            "    <if test='createTime != null'>create_time = #{createTime},</if>",
            "    <if test='updateTime != null'>update_time = #{updateTime},</if>",
            "    <if test='remark != null'>remark = #{remark},</if>",
            "</set>",
            "WHERE id = #{id}",
            "</script>"})
    int updateById(SysRole sysRole);

    /**
     * 查询所有
     *
     * @param id 主键id
     * @return 查询到的结果, 无结果将返回null
     */
    @Results(id = "baseResult", value = {
        @Result(property = "id", column = "id"),
        @Result(property = "roleName", column = "role_name"),
        @Result(property = "roleKey", column = "role_key"),
        @Result(property = "sortOrder", column = "sort_order"),
        @Result(property = "status", column = "status"),
        @Result(property = "deleted", column = "deleted"),
        @Result(property = "createBy", column = "create_by"),
        @Result(property = "updateBy", column = "update_by"),
        @Result(property = "createTime", column = "create_time"),
        @Result(property = "updateTime", column = "update_time"),
        @Result(property = "remark", column = "remark"),
    })
    @Select("SELECT * FROM sys_role")
    SysRole selectAll();

    /**
     * 通过主键id查询
     *
     * @param id 主键id
     * @return 查询到的结果, 无结果将返回null
     */
    @ResultMap("baseResult")
    @Select("SELECT * FROM sys_role WHERE id = #{id}")
    SysRole selectById(Long id);

    /**
     * 查询一条记录, 自行控制条件保证返回一条记录
     *
     * @param sysRole 实体的非空属性会做为查询条件
     * @return 查询到的结果, 无结果将返回null
     */
    @ResultMap("baseResult")
    @Select({"<script>",
            "SELECT * FROM sys_role",
            "<where>",
            "    <if test='id != null'>AND id = #{id},</if>",
            "    <if test='roleName != null'>AND role_name = #{roleName},</if>",
            "    <if test='roleKey != null'>AND role_key = #{roleKey},</if>",
            "    <if test='sortOrder != null'>AND sort_order = #{sortOrder},</if>",
            "    <if test='status != null'>AND status = #{status},</if>",
            "    <if test='deleted != null'>AND deleted = #{deleted},</if>",
            "    <if test='createBy != null'>AND create_by = #{createBy},</if>",
            "    <if test='updateBy != null'>AND update_by = #{updateBy},</if>",
            "    <if test='createTime != null'>AND create_time = #{createTime},</if>",
            "    <if test='updateTime != null'>AND update_time = #{updateTime},</if>",
            "    <if test='remark != null'>AND remark = #{remark},</if>",
            "</where>",
            "</script>"})
    SysRole selectOne(SysRole sysRole);

    /**
     * 查询多条记录, 自行控制条件保证返回多条记录
     *
     * @param sysRole 实体的非空属性会作为查询条件
     * @return 查询到的结果, 无结果将返回空List
     */
    @ResultMap("baseResult")
    @Select({"<script>",
            "SELECT * FROM sys_role",
            "<where>",
            "    <if test='id != null'>AND id = #{id},</if>",
            "    <if test='roleName != null'>AND role_name = #{roleName},</if>",
            "    <if test='roleKey != null'>AND role_key = #{roleKey},</if>",
            "    <if test='sortOrder != null'>AND sort_order = #{sortOrder},</if>",
            "    <if test='status != null'>AND status = #{status},</if>",
            "    <if test='deleted != null'>AND deleted = #{deleted},</if>",
            "    <if test='createBy != null'>AND create_by = #{createBy},</if>",
            "    <if test='updateBy != null'>AND update_by = #{updateBy},</if>",
            "    <if test='createTime != null'>AND create_time = #{createTime},</if>",
            "    <if test='updateTime != null'>AND update_time = #{updateTime},</if>",
            "    <if test='remark != null'>AND remark = #{remark},</if>",
            "</where>",
            "</script>"})
    List<SysRole> selectList(SysRole sysRole);

}
