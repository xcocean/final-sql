package top.lingkang.finalsql.sql;

import java.io.Serializable;

/**
 * @author lingkang
 * Created by 2022/5/22
 */
public class PageInfo implements Serializable {
    private Long total;
    private Integer page;
    private Integer size;

    public PageInfo() {
    }

    public PageInfo(Integer page, Integer size) {
        this.page = page;
        this.size = size;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "PageInfo{" +
                "total=" + total +
                ", page=" + page +
                ", size=" + size +
                '}';
    }
}
