package com.guet.demo_android;

public class User {
    String id;
    String userName;
    String avatar;
    String introduce;
    Integer sex;

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public void id(String id) {
        this.id = id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getSex() {
        return sex;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getId() {
        return id;
    }

    public String getIntroduce() {
        return introduce;
    }

    public String getUserName() {
        return userName;
    }
}
