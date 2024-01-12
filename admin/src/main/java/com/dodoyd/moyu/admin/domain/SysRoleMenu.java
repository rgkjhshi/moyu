package com.dodoyd.moyu.admin.domain;

import java.util.Date;

import lombok.Data;

/**
 * 角色和菜单关联表实体模型
 *
 * @author moyusi
 * @since 2024-01-12
 */
@Data
public class SysRoleMenu {

        /**
         * 主键id
         */
        private Long id;

        /**
         * 角色ID
         */
        private Long roleId;

        /**
         * 菜单ID
         */
        private Long menuId;

}
