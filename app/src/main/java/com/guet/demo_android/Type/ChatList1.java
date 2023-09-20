package com.guet.demo_android.Type;

import java.util.List;

public class ChatList1 {
    private List<Chat1> records;
    private Integer total;
    private Integer size;
    private Integer current;

    public List<Chat1> getRecords() {
        return records;
    }

    public void setRecords(List<Chat1> records) {
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
}
