package cn.gtgs.base.playpro.http;

import java.util.List;

/**
 * Created by  on 2017/5/4.
 */

public class BaseList<T> {
    public int page;
    public int totalCount;
    public int totalPage;
    public List<T> dataList;

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    @Override
    public String toString() {
        return "BaseList{" +
                "dataList=" + dataList +
                ", page=" + page +
                ", totalCount=" + totalCount +
                ", totalPage=" + totalPage +
                '}';
    }
}
