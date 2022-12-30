package com.john.springbootmall.dto;

public class OrderQueryParams {

    private Integer userId;
    private Integer limt;
    private Integer offset;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getLimt() {
        return limt;
    }

    public void setLimt(Integer limt) {
        this.limt = limt;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }
}
