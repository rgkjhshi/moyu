package com.dodoyd.moyu.admin.model.request;

import com.dodoyd.moyu.common.model.PageRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 角色信息表请求对象
 *
 * @author moyusi
 * @since 2024-01-09
 */
@Getter
@Setter
@ToString
public class SysRoleRequest extends PageRequest {

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
