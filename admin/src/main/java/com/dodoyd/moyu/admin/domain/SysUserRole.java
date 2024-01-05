package com.dodoyd.moyu.admin.domain;

import java.util.Date;

import lombok.Data;

/**
 * 用户和角色关联表实体模型
 *
 * @author moyusi
 * @since 2024-01-05
 */
@Data
public class SysUserRole {

        /**
         * 主键id
         */
        private Long id;

        /**
         * 用户ID
         */
        private Long userId;

        /**
         * 角色ID
         */
        private Long roleId;

}
