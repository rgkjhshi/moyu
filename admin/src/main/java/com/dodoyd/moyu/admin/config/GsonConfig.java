package com.dodoyd.moyu.admin.config;

import com.google.common.base.Strings;
import com.google.gson.*;
import org.springframework.boot.autoconfigure.gson.GsonBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Type;

/**
 * @author shisong02
 * @since 2023-12-29
 */
@Configuration
public class GsonConfig {

    /**
     * 用于自定义GsonBuilder的配置
     */
    @Bean
    public GsonBuilderCustomizer gsonBuilderCustomizer() {
        return new GsonBuilderCustomizer() {
            @Override
            public void customize(GsonBuilder builder) {
                // 用于将JSON中的整数反序列化为Integer对象的自定义反序列化器。
                builder.registerTypeAdapter(Integer.class, new JsonDeserializer<Integer>() {
                    @Override
                    public Integer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                        // 字符串转换为Integer时，仅将空字符串转换为null
                        return Strings.isNullOrEmpty(json.getAsString()) ? null : json.getAsInt();
                    }
                });
            }
        };
    }
}
