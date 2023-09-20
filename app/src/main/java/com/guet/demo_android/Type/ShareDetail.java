package com.guet.demo_android.Type;

import java.util.List;

public class ShareDetail {
    String id;
    String pUserId;
    String imageCode;
    String title;
    String content;
    String createTime;
    List<String> imageUrlList;
    String likeId;
    int likeNum;
    Boolean hasLike;
    String collectId;
    Integer collectNum;
    Boolean hasCollect;
    Boolean hasFocus;
    String username;

    public String getAvatar(String avatar) {
        return this.avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    String avatar;
    // 新增字段表示记录是否展开图片
    private boolean isExpanded = false;
    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getpUserId() {
        return pUserId;
    }

    public void setpUserId(String pUserId) {
        this.pUserId = pUserId;
    }

    public String getImageCode() {
        return imageCode;
    }

    public void setImageCode(String imageCode) {
        this.imageCode = imageCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public List<String> getImageUrlList() {
        return imageUrlList;
    }

    public void setImageUrlList(List<String> imageUrlList) {
        this.imageUrlList = imageUrlList;
    }

    public String getLikeId() {
        return likeId;
    }

    public void setLikeId(String likeId) {
        this.likeId = likeId;
    }

    public int getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(Integer likeNum) {
        this.likeNum = likeNum;
    }

    public Boolean getHasLike() {
        return hasLike;
    }

    public void setHasLike(Boolean hasLike) {
        this.hasLike = hasLike;
    }

    public String getCollectId() {
        return collectId;
    }

    public void setCollectId(String collectId) {
        this.collectId = collectId;
    }

    public Integer getCollectNum() {
        return collectNum;
    }

    public void setCollectNum(Integer collectNum) {
        this.collectNum = collectNum;
    }

    public Boolean getHasCollect() {
        return hasCollect;
    }

    public void setHasCollect(Boolean hasCollect) {
        this.hasCollect = hasCollect;
    }

    public Boolean getHasFocus() {
        return hasFocus;
    }

    public void setHasFocus(Boolean hasFocus) {
        this.hasFocus = hasFocus;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "ShareDetail{" +
                "id='" + id + '\'' +
                ", pUserId='" + pUserId + '\'' +
                ", imageCode='" + imageCode + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", createTime='" + createTime + '\'' +
                ", imageUrlList=" + imageUrlList +
                ", likeId='" + likeId + '\'' +
                ", likeNum=" + likeNum +
                ", hasLike=" + hasLike +
                ", collectId='" + collectId + '\'' +
                ", collectNum=" + collectNum +
                ", hasCollect=" + hasCollect +
                ", hasFocus=" + hasFocus +
                ", username='" + username + '\'' +
                '}';
    }
}
