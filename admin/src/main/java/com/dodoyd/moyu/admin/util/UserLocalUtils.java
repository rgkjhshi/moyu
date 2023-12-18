package com.dodoyd.moyu.admin.util;

/**
 * @author shisong02
 * @since 2023-05-04
 */
public class UserLocalUtils {
    private UserLocalUtils() {
    }

    /**
     * 线程局部变量用于存放userId
     */
    private static final ThreadLocal<Long> LOCAL = new ThreadLocal<>();

    /**
     * 设置userId
     */
    public static void setUserId(Long userId) {
        LOCAL.set(userId);
    }

    /**
     * 获取userId
     */
    public static Long getUserId() {
        return LOCAL.get();
    }
}
