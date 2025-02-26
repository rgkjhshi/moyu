package com.moyu.system.sys.enums;


import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

/**
 * 菜单类型，SysMenu实体中menuType字段对应的取值范围
 *
 * @author shisong
 * @since 2024-12-11
 */
@Getter
public enum MenuTypeEnum {

    /**
     * 菜单类型（字典 1模块 2目录 3菜单 4按钮 5外链）
     */
    INVALID(0, null),
    // 菜单
    MODULE(1, "模块"),
    DIR(2, "目录"),
    MENU(3, "菜单"),
    LINK(5, "外链"),
    // 操作
    BUTTON(4, "按钮");

    //  Mybatis-Plus 提供注解表示插入数据库时插入该值
    @EnumValue
    private final Integer code;

    private final String desc;

    MenuTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 根据枚举的code值获取枚举对象
     */
    public static MenuTypeEnum getByCode(Integer code) {
        return Arrays.stream(MenuTypeEnum.values()).filter(e -> Objects.equals(e.getCode(), code)).findFirst().orElse(INVALID);
    }
}
