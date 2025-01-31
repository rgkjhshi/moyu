package com.moyu.system.sys.model.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.moyu.common.model.BasePageParam;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.Set;

/**
 * 用户信息参数
 */
@Getter
@Setter
@ToString
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysUserParam extends BasePageParam {
    //********** 额外字段 **********//
    /**
     * 待删除的id列表
     */
    private Set<Long> ids;
    /**
     * 指定的account集合
     */
    private Set<String> codeSet;
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
    @NotBlank(message = "account 用户账号不能为空")
    private String account;

    /**
     * 密码
     */
    private String password;

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
    @NotBlank(message = "name 用户姓名不能为空")
    private String name;

    /**
     * 性别(字典 0未知 1男 2女)
     */
    @NotEmpty(message = "gender 用户性别不能为空")
    private Integer gender;

    /**
     * 生日
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
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
     * 员工入职日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date entryDate;

    /**
     * 直属组织编码
     */
    @NotBlank(message = "orgCode 用户直属组织不能为空")
    private String orgCode;

    /**
     * 状态（0正常 1停用）
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

}