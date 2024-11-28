package com.moyu.common.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 * @author shisong02
 * @since 2023-12-28
 */
public class PageResult<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 总数量
     */
    private Long total;

    /**
     * 当前页内数据
     */
    private List<T> rows;

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    /**
     * 返回一个无数据的空结果页
     */
    public static <T> PageResult<T> emptyPage() {
        return new PageResult<>(0L, new ArrayList<>());
    }

    /**
     * 构造方法
     */
    public PageResult(Long total, List<T> rows) {
        this.total = total;
        this.rows = rows;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", PageResult.class.getSimpleName() + "[", "]")
                .add("total=" + total)
                .add("rows=" + rows)
                .toString();
    }
}
