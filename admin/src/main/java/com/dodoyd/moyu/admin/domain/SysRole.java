package com.dodoyd.moyu.admin.domain;

import java.util.Date;

import lombok.Data;

/**
 * 角色信息表实体模型
 *
 * @author moyusi
 * @since 2024-01-05
 */
@Data
public class SysRole {

        /**
         * 主键id
         */
        private Long id;

        /**
         * 角色名称
         */
        private String roleName;

        /**
         * 角色权限字符串
         */
        private String roleKey;

        /**
         * 显示顺序
         */
        private Integer sortOrder;

        /**
         * 角色状态,0:正常,1:停用
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
