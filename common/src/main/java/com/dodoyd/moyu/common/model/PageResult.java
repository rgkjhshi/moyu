package com.dodoyd.moyu.common.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shisong02
 * @since 2023-12-28
 */
public class PageResult<T> {
    /**
     * 总数量
     */
    private Long total;
    /**
     * 当前页码
     */
    private Integer pageNum;
    /**
     * 当前页内数据
     */
    private List<T> pageData;

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public List<T> getPageData() {
        return pageData;
    }

    public void setPageData(List<T> pageData) {
        this.pageData = pageData;
    }

    /**
     * 返回一个无数据的空结果页
     */
    public static <T> PageResult<T> emptyPage() {
        PageResult<T> pageResult = new PageResult<>();
        pageResult.setTotal(0L);
        pageResult.setPageNum(1);
        pageResult.setPageData(new ArrayList<>());
        return pageResult;
    }
}
