package com.dodoyd.moyu.admin.dao;

import com.dodoyd.moyu.admin.domain.SysUserRole;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * SysUserRole数据访问对象
 *
 * @author moyusi
 * @since 2024-01-09
 */
@Mapper
public interface SysUserRoleDao {

    /**
     * 添加记录
     *
     * @param sysUserRole 数据库实体
     * @return 返回受影响的记录条数
     */
    @Insert({"<script>",
            "INSERT INTO sys_user_role",
            "<trim prefix='(' suffix=')' suffixOverrides=','>",
            "    <if test='id != null'>id,</if>",
            "    <if test='userId != null'>user_id,</if>",
            "    <if test='roleId != null'>role_id,</if>",
            "</trim>",
            "<trim prefix='VALUES (' suffix=')' suffixOverrides=','>",
            "    <if test='id != null'>#{id},</if>",
            "    <if test='userId != null'>#{userId},</if>",
            "    <if test='roleId != null'>#{roleId},</if>",
            "</trim>",
            "</script>"})
    @Options(useGeneratedKeys = true)
    int insert(SysUserRole sysUserRole);

    /**
     * 通过主键删除
     *
     * @param id 主键
     * @return 返回受影响的记录条数
     */
    @Delete("DELETE FROM sys_user_role WHERE id = #{id}")
    int deleteById(Long id);

    /**
     * 通过主键列表批量删除
     *
     * @param idList 主键列表
     * @return 返回受影响的记录条数
     */
    @Delete({"<script>",
            "DELETE FROM sys_user_role WHERE id IN ",
            "<foreach collection='list' item='item' open='(' separator=',' close=')'>",
            "    #{item} ",
            "</foreach>",
            "</script>"})
    int deleteByIdList(List<Long> idList);

    /**
     * 通过主键更新
     *
     * @param sysUserRole 数据库实体
     * @return 返回受影响的记录条数
     */
    @Update({"<script>",
            "UPDATE sys_user_role",
            "<set>",
            "    <if test='id != null'>id = #{id},</if>",
            "    <if test='userId != null'>user_id = #{userId},</if>",
            "    <if test='roleId != null'>role_id = #{roleId},</if>",
            "</set>",
            "WHERE id = #{id}",
            "</script>"})
    int updateById(SysUserRole sysUserRole);

    /**
     * 查询所有
     *
     * @return 查询到的结果, 无结果将返回null
     */
    @Results(id = "baseResult", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "userId", column = "user_id"),
            @Result(property = "roleId", column = "role_id"),
    })
    @Select("SELECT * FROM sys_user_role")
    SysUserRole selectAll();

    /**
     * 通过主键查询
     *
     * @param id 主键
     * @return 查询到的结果, 无结果将返回null
     */
    @ResultMap("baseResult")
    @Select("SELECT * FROM sys_user_role WHERE id = #{id}")
    SysUserRole selectById(Long id);

    /**
     * 通过主键列表查询
     *
     * @param idList 主键列表
     * @return 查询到的结果, 无结果将返回空List
     */
    @ResultMap("baseResult")
    @Select({"<script>",
            "SELECT * FROM sys_user_role WHERE id IN ",
            "<foreach collection='list' item='item' open='(' separator=',' close=')'>",
            "    #{item} ",
            "</foreach>",
            "</script>"})
    List<SysUserRole> selectByIdList(List<Long> idList);

    /**
     * 查询一条记录, 自行控制条件保证返回一条记录
     *
     * @param sysUserRole 实体的非空属性会做为查询条件
     * @return 查询到的结果, 无结果将返回null
     */
    @ResultMap("baseResult")
    @Select({"<script>",
            "SELECT * FROM sys_user_role",
            "<where>",
            "    <if test='id != null'>AND id = #{id}</if>",
            "    <if test='userId != null'>AND user_id = #{userId}</if>",
            "    <if test='roleId != null'>AND role_id = #{roleId}</if>",
            "</where>",
            "</script>"})
    SysUserRole selectOne(SysUserRole sysUserRole);

    /**
     * 查询多条记录, 自行控制条件保证返回多条记录
     *
     * @param sysUserRole 实体的非空属性会作为查询条件
     * @return 查询到的结果, 无结果将返回空List
     */
    @ResultMap("baseResult")
    @Select({"<script>",
            "SELECT * FROM sys_user_role",
            "<where>",
            "    <if test='id != null'>AND id = #{id}</if>",
            "    <if test='userId != null'>AND user_id = #{userId}</if>",
            "    <if test='roleId != null'>AND role_id = #{roleId}</if>",
            "</where>",
            "</script>"})
    List<SysUserRole> selectList(SysUserRole sysUserRole);

}
