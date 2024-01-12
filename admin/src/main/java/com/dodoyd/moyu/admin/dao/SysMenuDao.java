package com.dodoyd.moyu.admin.dao;

import com.dodoyd.moyu.admin.domain.SysMenu;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * SysMenu数据访问对象
 *
 * @author moyusi
 * @since 2024-01-12
 */
@Mapper
public interface SysMenuDao {

    /**
     * 添加记录
     *
     * @param sysMenu 数据库实体
     * @return 返回受影响的记录条数
     */
    @Insert({"<script>",
            "INSERT INTO sys_menu",
            "<trim prefix='(' suffix=')' suffixOverrides=','>",
            "    <if test='id != null'>id,</if>",
            "    <if test='menuName != null'>menu_name,</if>",
            "    <if test='parentId != null'>parent_id,</if>",
            "    <if test='sortOrder != null'>sort_order,</if>",
            "    <if test='menuType != null'>menu_type,</if>",
            "    <if test='path != null'>path,</if>",
            "    <if test='component != null'>component,</if>",
            "    <if test='perms != null'>perms,</if>",
            "    <if test='icon != null'>icon,</if>",
            "    <if test='hidden != null'>hidden,</if>",
            "    <if test='link != null'>link,</if>",
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
            "    <if test='menuName != null'>#{menuName},</if>",
            "    <if test='parentId != null'>#{parentId},</if>",
            "    <if test='sortOrder != null'>#{sortOrder},</if>",
            "    <if test='menuType != null'>#{menuType},</if>",
            "    <if test='path != null'>#{path},</if>",
            "    <if test='component != null'>#{component},</if>",
            "    <if test='perms != null'>#{perms},</if>",
            "    <if test='icon != null'>#{icon},</if>",
            "    <if test='hidden != null'>#{hidden},</if>",
            "    <if test='link != null'>#{link},</if>",
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
    int insert(SysMenu sysMenu);

    /**
     * 通过主键删除
     *
     * @param id 主键
     * @return 返回受影响的记录条数
     */
    @Delete("DELETE FROM sys_menu WHERE id = #{id}")
    int deleteById(Long id);

    /**
     * 通过主键列表批量删除
     *
     * @param idList 主键列表
     * @return 返回受影响的记录条数
     */
    @Delete({"<script>",
            "DELETE FROM sys_menu WHERE id IN ",
            "<foreach collection='list' item='item' open='(' separator=',' close=')'>",
            "    #{item} ",
            "</foreach>",
            "</script>"})
    int deleteByIdList(List<Long> idList);

    /**
     * 通过主键更新
     *
     * @param sysMenu 数据库实体
     * @return 返回受影响的记录条数
     */
    @Update({"<script>",
            "UPDATE sys_menu",
            "<set>",
            "    <if test='id != null'>id = #{id},</if>",
            "    <if test='menuName != null'>menu_name = #{menuName},</if>",
            "    <if test='parentId != null'>parent_id = #{parentId},</if>",
            "    <if test='sortOrder != null'>sort_order = #{sortOrder},</if>",
            "    <if test='menuType != null'>menu_type = #{menuType},</if>",
            "    <if test='path != null'>path = #{path},</if>",
            "    <if test='component != null'>component = #{component},</if>",
            "    <if test='perms != null'>perms = #{perms},</if>",
            "    <if test='icon != null'>icon = #{icon},</if>",
            "    <if test='hidden != null'>hidden = #{hidden},</if>",
            "    <if test='link != null'>link = #{link},</if>",
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
    int updateById(SysMenu sysMenu);

    /**
     * 查询所有
     *
     * @return 查询到的结果, 无结果将返回null
     */
    @Results(id = "baseResult", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "menuName", column = "menu_name"),
            @Result(property = "parentId", column = "parent_id"),
            @Result(property = "sortOrder", column = "sort_order"),
            @Result(property = "menuType", column = "menu_type"),
            @Result(property = "path", column = "path"),
            @Result(property = "component", column = "component"),
            @Result(property = "perms", column = "perms"),
            @Result(property = "icon", column = "icon"),
            @Result(property = "hidden", column = "hidden"),
            @Result(property = "link", column = "link"),
            @Result(property = "status", column = "status"),
            @Result(property = "deleted", column = "deleted"),
            @Result(property = "createBy", column = "create_by"),
            @Result(property = "updateBy", column = "update_by"),
            @Result(property = "createTime", column = "create_time"),
            @Result(property = "updateTime", column = "update_time"),
            @Result(property = "remark", column = "remark"),
    })
    @Select("SELECT * FROM sys_menu")
    SysMenu selectAll();

    /**
     * 通过主键查询
     *
     * @param id 主键
     * @return 查询到的结果, 无结果将返回null
     */
    @ResultMap("baseResult")
    @Select("SELECT * FROM sys_menu WHERE id = #{id}")
    SysMenu selectById(Long id);

    /**
     * 通过主键列表查询
     *
     * @param idList 主键列表
     * @return 查询到的结果, 无结果将返回空List
     */
    @ResultMap("baseResult")
    @Select({"<script>",
            "SELECT * FROM sys_menu WHERE id IN ",
            "<foreach collection='list' item='item' open='(' separator=',' close=')'>",
            "    #{item} ",
            "</foreach>",
            "</script>"})
    List<SysMenu> selectByIdList(List<Long> idList);

    /**
     * 查询一条记录, 自行控制条件保证返回一条记录
     *
     * @param sysMenu 实体的非空属性会做为查询条件
     * @return 查询到的结果, 无结果将返回null
     */
    @ResultMap("baseResult")
    @Select({"<script>",
            "SELECT * FROM sys_menu",
            "<where>",
            "    <if test='id != null'>AND id = #{id}</if>",
            "    <if test='menuName != null'>AND menu_name = #{menuName}</if>",
            "    <if test='parentId != null'>AND parent_id = #{parentId}</if>",
            "    <if test='sortOrder != null'>AND sort_order = #{sortOrder}</if>",
            "    <if test='menuType != null'>AND menu_type = #{menuType}</if>",
            "    <if test='path != null'>AND path = #{path}</if>",
            "    <if test='component != null'>AND component = #{component}</if>",
            "    <if test='perms != null'>AND perms = #{perms}</if>",
            "    <if test='icon != null'>AND icon = #{icon}</if>",
            "    <if test='hidden != null'>AND hidden = #{hidden}</if>",
            "    <if test='link != null'>AND link = #{link}</if>",
            "    <if test='status != null'>AND status = #{status}</if>",
            "    <if test='deleted != null'>AND deleted = #{deleted}</if>",
            "    <if test='createBy != null'>AND create_by = #{createBy}</if>",
            "    <if test='updateBy != null'>AND update_by = #{updateBy}</if>",
            "    <if test='createTime != null'>AND create_time = #{createTime}</if>",
            "    <if test='updateTime != null'>AND update_time = #{updateTime}</if>",
            "    <if test='remark != null'>AND remark = #{remark}</if>",
            "</where>",
            "</script>"})
    SysMenu selectOne(SysMenu sysMenu);

    /**
     * 查询多条记录, 自行控制条件保证返回多条记录
     *
     * @param sysMenu 实体的非空属性会作为查询条件
     * @return 查询到的结果, 无结果将返回空List
     */
    @ResultMap("baseResult")
    @Select({"<script>",
            "SELECT * FROM sys_menu",
            "<where>",
            "    <if test='id != null'>AND id = #{id}</if>",
            "    <if test='menuName != null'>AND menu_name = #{menuName}</if>",
            "    <if test='parentId != null'>AND parent_id = #{parentId}</if>",
            "    <if test='sortOrder != null'>AND sort_order = #{sortOrder}</if>",
            "    <if test='menuType != null'>AND menu_type = #{menuType}</if>",
            "    <if test='path != null'>AND path = #{path}</if>",
            "    <if test='component != null'>AND component = #{component}</if>",
            "    <if test='perms != null'>AND perms = #{perms}</if>",
            "    <if test='icon != null'>AND icon = #{icon}</if>",
            "    <if test='hidden != null'>AND hidden = #{hidden}</if>",
            "    <if test='link != null'>AND link = #{link}</if>",
            "    <if test='status != null'>AND status = #{status}</if>",
            "    <if test='deleted != null'>AND deleted = #{deleted}</if>",
            "    <if test='createBy != null'>AND create_by = #{createBy}</if>",
            "    <if test='updateBy != null'>AND update_by = #{updateBy}</if>",
            "    <if test='createTime != null'>AND create_time = #{createTime}</if>",
            "    <if test='updateTime != null'>AND update_time = #{updateTime}</if>",
            "    <if test='remark != null'>AND remark = #{remark}</if>",
            "</where>",
            "ORDER BY parent_id, sort_order",
            "</script>"})
    List<SysMenu> selectList(SysMenu sysMenu);

    /**
     * 查询所有有效的菜单列表(目录+菜单)
     *
     * @return 查询到的结果, 无结果将返回空List
     */
    @ResultMap("baseResult")
    @Select({"<script>",
            "SELECT * FROM sys_menu WHERE status = 0 AND deleted = 0 AND menu_type IN ('D', 'M', 'L')",
            "ORDER BY parent_id, sort_order",
            "</script>"})
    List<SysMenu> selectValidMenuList();

}
