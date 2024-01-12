package com.dodoyd.moyu.admin.model.request;

import com.dodoyd.moyu.common.model.PageRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * 菜单权限表请求对象
 *
 * @author moyusi
 * @since 2024-01-12
 */
@Getter
@Setter
@ToString
public class SysMenuRequest extends PageRequest {

        /**
         * 主键id
         */
        private Long id;

        /**
         * 菜单名称
         */
        private String menuName;

        /**
         * 父菜单ID
         */
        private Long parentId;

        /**
         * 显示顺序
         */
        private Integer sortOrder;

        /**
         * 资源类型,D:目录,M:菜单,B:按钮
         */
        private String menuType;

        /**
         * 路径地址
         */
        private String path;

        /**
         * 组件路径
         */
        private String component;

        /**
         * 权限标识
         */
        private String perms;

        /**
         * 菜单图标
         */
        private String icon;

        /**
         * 是否隐藏,0:显示,1:隐藏
         */
        private Integer hidden;

        /**
         * 是否外链,0:非外链,1:外链
         */
        private Integer link;

        /**
         * 菜单状态,0:正常,1:停用
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
