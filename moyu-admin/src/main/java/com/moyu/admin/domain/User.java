package com.moyu.admin.domain;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author shisong
 * @since 2024-11-25
 */
@Data
@TableName("`user`")
public class User {
    private Long id;
    private String name;
    private Integer age;
    private String email;
}
