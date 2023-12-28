package com.dodoyd.moyu.common.model;

import java.util.StringJoiner;

/**
 * 分页请求
 *
 * @author shisong02
 * @since 2022-10-24
 */
public class PageRequest {
    /**
     * 页码，第几页
     */
    private Integer pageNum;
    /**
     * 页大小
     */
    private Integer pageSize;

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", PageRequest.class.getSimpleName() + "[", "]")
                .add("pageNum=" + pageNum)
                .add("pageSize=" + pageSize)
                .toString();
    }
}
