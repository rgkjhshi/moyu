package com.moyu.common.mybatis.enums;


import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

/**
 * 数据权限枚举
 *
 * @author shisong
 * @since 2024-12-25
 */
@Getter
public enum DataScopeEnum {

    /**
     * 所有数据
     */
    ALL(0, "所有数据"),

    /**
     * 仅自己
     */
    SELF(1, "本人数据"),

    /**
     * 仅所属组织
     */
    ORG(2, "所属组织"),

    /**
     * 所属组织及以下
     */
    ORG_CHILD(3, "所属组织及以下"),

    /**
     * 自定义组织
     */
    ORG_DEFINE(4, "自定义组织");

    //  Mybatis-Plus 提供注解表示插入数据库时插入该值
    @EnumValue
    private final Integer code;

    private final String desc;

    DataScopeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 根据枚举的code值获取枚举对象
     */
    public static DataScopeEnum getByCode(Integer code) {
        return Arrays.stream(DataScopeEnum.values()).filter(e -> Objects.equals(e.getCode(), code)).findFirst().orElse(null);
    }
}
