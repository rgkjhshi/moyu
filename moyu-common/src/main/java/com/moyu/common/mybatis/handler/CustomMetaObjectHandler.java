package com.moyu.common.mybatis.handler;


import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ReflectionException;

import java.util.Date;

/**
 * mybatis-plus 自动填充BaseEntity中的公共字段
 *
 * @author shisong
 * @see com.moyu.common.mybatis.entity.BaseEntity
 * @since 2024-12-23
 */
@Slf4j
public class CustomMetaObjectHandler implements MetaObjectHandler {

    /**
     * 删除标志
     */
    private static final String DELETE_FLAG = "deleteFlag";
    /**
     * 创建时间
     */
    private static final String CREATE_TIME = "createTime";
    /**
     * 创建人
     */
    private static final String CREATE_USER = "createUser";
    /**
     * 更新时间
     */
    private static final String UPDATE_TIME = "updateTime";
    /**
     * 更新人
     */
    private static final String UPDATE_USER = "updateUser";

    @Override
    public void insertFill(MetaObject metaObject) {
        // setFieldValByName方法会判断db中是否有对应的字段,无需判断 (metaObject.getOriginalObject() instanceof BaseEntity)
        try {
            // 为空则设置deleteFlag为0
            if (metaObject.getValue(DELETE_FLAG) == null) {
                setFieldValByName(DELETE_FLAG, 0, metaObject);
            }
            // 为空则设置createTime
            if (metaObject.getValue(CREATE_TIME) == null) {
                setFieldValByName(CREATE_TIME, new Date(), metaObject);
            }
            // 为空则设置createUser
            if (metaObject.getValue(CREATE_USER) == null) {
                setFieldValByName(CREATE_USER, this.getUserId(), metaObject);
            }
            // 为空则设置updateTime
            if (metaObject.getValue(UPDATE_TIME) == null) {
                setFieldValByName(UPDATE_TIME, new Date(), metaObject);
            }
            // 为空则设置updateUser
            if (metaObject.getValue(UPDATE_USER) == null) {
                setFieldValByName(UPDATE_USER, this.getUserId(), metaObject);
            }
        } catch (ReflectionException e) {
            log.warn("CustomMetaObjectHandler自动填充字段失败，可不做处理");
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        try {
            // 为空则设置updateTime
            if (metaObject.getValue(UPDATE_TIME) == null) {
                setFieldValByName(UPDATE_TIME, new Date(), metaObject);
            }
            // 为空则设置updateUser
            if (metaObject.getValue(UPDATE_USER) == null) {
                setFieldValByName(UPDATE_USER, this.getUserId(), metaObject);
            }
        } catch (ReflectionException e) {
            log.warn("CustomMetaObjectHandler.updateFill自动填充字段失败，可不做处理");
        }
    }

    /**
     * 获取用户id TODO
     */
    private String getUserId() {
        return null;
    }
}
