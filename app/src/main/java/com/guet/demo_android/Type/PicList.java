package com.guet.demo_android.Type;

import java.util.List;

public class PicList {
    List<ShareDetail> records;
    Integer total;
    Integer size;
    Integer current;
    public List<ShareDetail> getRecords() {
        return records;
    }

    public void setRecords(List<ShareDetail> records) {
        this.records = records;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        this.current = current;
    }

    @Override
    public String toString() {
        return "PicList{" +
                "records=" + records +
                ", total=" + total +
                ", size=" + size +
                ", current=" + current +
                '}';
    }
}
