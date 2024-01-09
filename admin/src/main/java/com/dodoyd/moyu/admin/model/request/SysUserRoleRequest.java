package com.dodoyd.moyu.admin.model.request;

import com.dodoyd.moyu.common.model.PageRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 用户和角色关联表请求对象
 *
 * @author moyusi
 * @since 2024-01-09
 */
@Getter
@Setter
@ToString
public class SysUserRoleRequest extends PageRequest {

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
