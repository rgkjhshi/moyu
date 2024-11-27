package com.moyu.common.web.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * 下拉选项对象
 *
 * @author shisong
 * @since 2024-11-27
 */
@Data
public class Option<T> {

    /**
     * 选项的值
     */
    private T value;

    /**
     * 选项的标签
     */
    private String label;

    /**
     * 子选项列表
     */
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    private List<Option<T>> children;
}
