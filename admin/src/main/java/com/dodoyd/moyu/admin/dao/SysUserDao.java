package com.dodoyd.moyu.admin.dao;

import com.dodoyd.moyu.admin.domain.SysUser;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * SysUser数据访问对象
 *
 * @author moyusi
 * @since 2024-01-09
 */
@Mapper
public interface SysUserDao {

    /**
     * 添加记录
     *
     * @param sysUser 数据库实体
     * @return 返回受影响的记录条数
     */
    @Insert({"<script>",
            "INSERT INTO sys_user",
            "<trim prefix='(' suffix=')' suffixOverrides=','>",
            "    <if test='userId != null'>user_id,</if>",
            "    <if test='username != null'>username,</if>",
            "    <if test='nickname != null'>nickname,</if>",
            "    <if test='gender != null'>gender,</if>",
            "    <if test='avatar != null'>avatar,</if>",
            "    <if test='email != null'>email,</if>",
            "    <if test='mobile != null'>mobile,</if>",
            "    <if test='userPwd != null'>user_pwd,</if>",
            "    <if test='loginIp != null'>login_ip,</if>",
            "    <if test='loginTime != null'>login_time,</if>",
            "    <if test='status != null'>status,</if>",
            "    <if test='deleted != null'>deleted,</if>",
            "    <if test='createBy != null'>create_by,</if>",
            "    <if test='updateBy != null'>update_by,</if>",
            "    <if test='createTime != null'>create_time,</if>",
            "    <if test='updateTime != null'>update_time,</if>",
            "    <if test='remark != null'>remark,</if>",
            "</trim>",
            "<trim prefix='VALUES (' suffix=')' suffixOverrides=','>",
            "    <if test='userId != null'>#{userId},</if>",
            "    <if test='username != null'>#{username},</if>",
            "    <if test='nickname != null'>#{nickname},</if>",
            "    <if test='gender != null'>#{gender},</if>",
            "    <if test='avatar != null'>#{avatar},</if>",
            "    <if test='email != null'>#{email},</if>",
            "    <if test='mobile != null'>#{mobile},</if>",
            "    <if test='userPwd != null'>#{userPwd},</if>",
            "    <if test='loginIp != null'>#{loginIp},</if>",
            "    <if test='loginTime != null'>#{loginTime},</if>",
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
    int insert(SysUser sysUser);

    /**
     * 通过主键删除
     *
     * @param userId 主键
     * @return 返回受影响的记录条数
     */
    @Delete("DELETE FROM sys_user WHERE user_id = #{userId}")
    int deleteByUserId(Long userId);

    /**
     * 通过主键列表批量删除
     *
     * @param userIdList 主键列表
     * @return 返回受影响的记录条数
     */
    @Delete({"<script>",
            "DELETE FROM sys_user WHERE user_id IN ",
            "<foreach collection='list' item='item' open='(' separator=',' close=')'>",
            "    #{item} ",
            "</foreach>",
            "</script>"})
    int deleteByUserIdList(List<Long> userIdList);

    /**
     * 通过主键更新
     *
     * @param sysUser 数据库实体
     * @return 返回受影响的记录条数
     */
    @Update({"<script>",
            "UPDATE sys_user",
            "<set>",
            "    <if test='userId != null'>user_id = #{userId},</if>",
            "    <if test='username != null'>username = #{username},</if>",
            "    <if test='nickname != null'>nickname = #{nickname},</if>",
            "    <if test='gender != null'>gender = #{gender},</if>",
            "    <if test='avatar != null'>avatar = #{avatar},</if>",
            "    <if test='email != null'>email = #{email},</if>",
            "    <if test='mobile != null'>mobile = #{mobile},</if>",
            "    <if test='userPwd != null'>user_pwd = #{userPwd},</if>",
            "    <if test='loginIp != null'>login_ip = #{loginIp},</if>",
            "    <if test='loginTime != null'>login_time = #{loginTime},</if>",
            "    <if test='status != null'>status = #{status},</if>",
            "    <if test='deleted != null'>deleted = #{deleted},</if>",
            "    <if test='createBy != null'>create_by = #{createBy},</if>",
            "    <if test='updateBy != null'>update_by = #{updateBy},</if>",
            "    <if test='createTime != null'>create_time = #{createTime},</if>",
            "    <if test='updateTime != null'>update_time = #{updateTime},</if>",
            "    <if test='remark != null'>remark = #{remark},</if>",
            "</set>",
            "WHERE user_id = #{userId}",
            "</script>"})
    int updateByUserId(SysUser sysUser);

    /**
     * 查询所有
     *
     * @return 查询到的结果, 无结果将返回null
     */
    @Results(id = "baseResult", value = {
            @Result(property = "userId", column = "user_id"),
            @Result(property = "username", column = "username"),
            @Result(property = "nickname", column = "nickname"),
            @Result(property = "gender", column = "gender"),
            @Result(property = "avatar", column = "avatar"),
            @Result(property = "email", column = "email"),
            @Result(property = "mobile", column = "mobile"),
            @Result(property = "userPwd", column = "user_pwd"),
            @Result(property = "loginIp", column = "login_ip"),
            @Result(property = "loginTime", column = "login_time"),
            @Result(property = "status", column = "status"),
            @Result(property = "deleted", column = "deleted"),
            @Result(property = "createBy", column = "create_by"),
            @Result(property = "updateBy", column = "update_by"),
            @Result(property = "createTime", column = "create_time"),
            @Result(property = "updateTime", column = "update_time"),
            @Result(property = "remark", column = "remark"),
    })
    @Select("SELECT * FROM sys_user")
    SysUser selectAll();

    /**
     * 通过主键查询
     *
     * @param userId 主键
     * @return 查询到的结果, 无结果将返回null
     */
    @ResultMap("baseResult")
    @Select("SELECT * FROM sys_user WHERE user_id = #{userId}")
    SysUser selectByUserId(Long userId);

    /**
     * 通过主键列表查询
     *
     * @param userIdList 主键列表
     * @return 查询到的结果, 无结果将返回空List
     */
    @ResultMap("baseResult")
    @Select({"<script>",
            "SELECT * FROM sys_user WHERE user_id IN ",
            "<foreach collection='list' item='item' open='(' separator=',' close=')'>",
            "    #{item} ",
            "</foreach>",
            "</script>"})
    List<SysUser> selectByUserIdList(List<Long> userIdList);

    /**
     * 查询一条记录, 自行控制条件保证返回一条记录
     *
     * @param sysUser 实体的非空属性会做为查询条件
     * @return 查询到的结果, 无结果将返回null
     */
    @ResultMap("baseResult")
    @Select({"<script>",
            "SELECT * FROM sys_user",
            "<where>",
            "    <if test='userId != null'>AND user_id = #{userId}</if>",
            "    <if test='username != null'>AND username = #{username}</if>",
            "    <if test='nickname != null'>AND nickname = #{nickname}</if>",
            "    <if test='gender != null'>AND gender = #{gender}</if>",
            "    <if test='avatar != null'>AND avatar = #{avatar}</if>",
            "    <if test='email != null'>AND email = #{email}</if>",
            "    <if test='mobile != null'>AND mobile = #{mobile}</if>",
            "    <if test='userPwd != null'>AND user_pwd = #{userPwd}</if>",
            "    <if test='loginIp != null'>AND login_ip = #{loginIp}</if>",
            "    <if test='loginTime != null'>AND login_time = #{loginTime}</if>",
            "    <if test='status != null'>AND status = #{status}</if>",
            "    <if test='deleted != null'>AND deleted = #{deleted}</if>",
            "    <if test='createBy != null'>AND create_by = #{createBy}</if>",
            "    <if test='updateBy != null'>AND update_by = #{updateBy}</if>",
            "    <if test='createTime != null'>AND create_time = #{createTime}</if>",
            "    <if test='updateTime != null'>AND update_time = #{updateTime}</if>",
            "    <if test='remark != null'>AND remark = #{remark}</if>",
            "</where>",
            "</script>"})
    SysUser selectOne(SysUser sysUser);

    /**
     * 查询多条记录, 自行控制条件保证返回多条记录
     *
     * @param sysUser 实体的非空属性会作为查询条件
     * @return 查询到的结果, 无结果将返回空List
     */
    @ResultMap("baseResult")
    @Select({"<script>",
            "SELECT * FROM sys_user",
            "<where>",
            "    <if test='userId != null'>AND user_id = #{userId}</if>",
            "    <if test='username != null'>AND username = #{username}</if>",
            "    <if test='nickname != null'>AND nickname = #{nickname}</if>",
            "    <if test='gender != null'>AND gender = #{gender}</if>",
            "    <if test='avatar != null'>AND avatar = #{avatar}</if>",
            "    <if test='email != null'>AND email = #{email}</if>",
            "    <if test='mobile != null'>AND mobile = #{mobile}</if>",
            "    <if test='userPwd != null'>AND user_pwd = #{userPwd}</if>",
            "    <if test='loginIp != null'>AND login_ip = #{loginIp}</if>",
            "    <if test='loginTime != null'>AND login_time = #{loginTime}</if>",
            "    <if test='status != null'>AND status = #{status}</if>",
            "    <if test='deleted != null'>AND deleted = #{deleted}</if>",
            "    <if test='createBy != null'>AND create_by = #{createBy}</if>",
            "    <if test='updateBy != null'>AND update_by = #{updateBy}</if>",
            "    <if test='createTime != null'>AND create_time = #{createTime}</if>",
            "    <if test='updateTime != null'>AND update_time = #{updateTime}</if>",
            "    <if test='remark != null'>AND remark = #{remark}</if>",
            "</where>",
            "</script>"})
    List<SysUser> selectList(SysUser sysUser);

}
