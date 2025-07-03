package com.nefu.project.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 数据列表 */
    private List<T> results;

    /** 总记录数 */
    // 改为 long
    private long total;

    /** 当前页码 */
    // 改为 long
    private long currentPage;

    /** 每页记录数 */
    // 改为 long
    private long pageSize;

    /** 总页数 */
    // 改为 long
    private long pages;

    public List<T> getResults() {
        return results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
        if (total == 0) {
            this.pages = 0;
            return;
        }
        // 计算总页数时注意类型
        this.pages = (total + pageSize - 1) / pageSize;
    }

    public long getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(long currentPage) {
        this.currentPage = currentPage;
    }

    public long getPageSize() {
        return pageSize;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
        // 计算总页数时注意类型
        this.pages = (total + pageSize - 1) / pageSize;
    }

    public long getPages() {
        return pages;
    }
}