package com.moyu.system.sys.model.param;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.moyu.common.model.BasePageParam;
import lombok.Data;

import java.util.Date;
import java.util.Set;

/**
 * 岗位信息表
 *
 * @TableName sys_pos
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysUserParam extends BasePageParam {
    //********** 额外字段 **********//
    /**
     * 待删除的id列表
     */
    private Set<Long> ids;

    /**
     * 搜索关键词
     */
    private String searchKey;

    //********** db中存在的字段 **********//
    /**
     * 主键id
     */
    private Long id;

    /**
     * 账号
     */
    private String account;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 姓名
     */
    private String name;

    /**
     * 性别(字典 0未知 1男 2女)
     */
    private Integer gender;

    /**
     * 生日
     */
    private Date birthday;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机
     */
    private String phone;

    /**
     * 身份证号
     */
    private String idNo;

    /**
     * 联系地址
     */
    private String address;

    /**
     * 员工编码
     */
    private String staffCode;

    /**
     * 直属组织编码
     */
    private String orgCode;

    /**
     * 所属组织链,逗号分隔
     */
    private String orgChain;

    /**
     * 状态（0正常 1停用）
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

}