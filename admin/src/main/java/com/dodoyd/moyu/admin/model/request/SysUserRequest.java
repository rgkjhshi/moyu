package com.dodoyd.moyu.admin.model.request;

import com.dodoyd.moyu.common.model.PageRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 用户信息表请求对象
 *
 * @author moyusi
 * @since 2024-01-09
 */
@Getter
@Setter
@ToString
public class SysUserRequest extends PageRequest {

        /**
         * 用户唯一ID
         */
        private Long userId;

        /**
         * 用户账号
         */
        private String username;

        /**
         * 用户昵称
         */
        private String nickname;

        /**
         * 性别,0:未知,1:男,2:女
         */
        private Integer gender;

        /**
         * 头像地址
         */
        private String avatar;

        /**
         * 用户邮箱
         */
        private String email;

        /**
         * 手机号码
         */
        private String mobile;

        /**
         * 登录口令密文
         */
        private String userPwd;

        /**
         * 最后登录IP
         */
        private String loginIp;

        /**
         * 最后登录时间
         */
        private Date loginTime;

        /**
         * 帐号状态,0:正常,1:停用
         */
        private Integer status;

        /**
         * 删除标志,0:未删除,1:已删除
         */
        private Integer deleted;

        /**
         * 创建者
         */
        private String createBy;

        /**
         * 更新者
         */
        private String updateBy;

        /**
         * 创建时间
         */
        private Date createTime;

        /**
         * 更新时间
         */
        private Date updateTime;

        /**
         * 备注
         */
        private String remark;

}
