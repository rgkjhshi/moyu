package com.dodoyd.moyu.admin.constant;

import com.auth0.jwt.algorithms.Algorithm;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * @author shisong02
 * @since 2023-04-28
 */
public class Constants {

    /**
     * 序列化GSON
     */
    public static final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    /**
     * 时间格式
     */
    public static final DateTimeFormatter DATE_TIME_PATTERN = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
    /**
     * 日期格式
     */
    public static final DateTimeFormatter DATE_PATTERN = DateTimeFormat.forPattern("yyyy-MM-dd");
    /**
     * 日期格式
     */
    public static final DateTimeFormatter SHORT_DATE_PATTERN = DateTimeFormat.forPattern("yyyyMMdd");
    /**
     * 逗号分隔
     */
    public static final Splitter COMMA_SPLITTER = Splitter.on(",").omitEmptyStrings().trimResults();
    /**
     * 逗号连接器
     */
    public static final Joiner COMMA_JOINER = Joiner.on(",").skipNulls();

    /**
     * token相关常量
     */
    public static class Token {
        /**
         * jwt秘钥
         */
        public static final String SECRET = "something_secret";
        /**
         * JWT签名算法
         */
        public static final Algorithm SIGNATURE_ALGORITHM = Algorithm.HMAC256(SECRET);
        /**
         * JWT token有效期(100天)
         */
        public static final int TOKEN_VALID_TIME = 60 * 60 * 24 * 100;
    }

    /**
     * 通用错误码
     */
    public static class ErrorCode {
        /**
         * token非法
         */
        public static final Integer TOKEN_INVALID = 50008;
        /**
         * token已过期
         */
        public static final Integer TOKEN_EXPIRED = 50014;
    }

}
