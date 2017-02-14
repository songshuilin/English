package com.lijunsai.httpInterface.bean;

import java.io.Serializable;

/**
 * 类名: LoginBean.java
 * 描述: 登录
 * 作者: youyou_pc
 * 时间: 2015年12月10日  下午1:33:30
 */
public class LoginBean implements Serializable {

    private Integer uid;
    private String username;
    private String telephone;
    private String gender;//性别
    private String state;//是否支付
    private String payTime;//支付时间
    private String activeCode;//激活码
    private String picUrl;//头像

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getGender() {
        return gender == null || gender.equals("") ? "0" : gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getActiveCode() {
        return activeCode;
    }

    public void setActiveCode(String activeCode) {
        this.activeCode = activeCode;
    }

    public String getPicUrl() {
        return picUrl == null ? null : picUrl.replaceAll("\\\\", "/");
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public LoginBean() {
        super();
    }

    @Override
    public String toString() {
        return "LoginBean [uid=" + uid + ", username=" + username + ", telephone=" + telephone + ", gender=" + gender + ", state=" + state + ", payTime=" + payTime + ", activeCode=" + activeCode + ", picUrl=" + picUrl + "]";
    }

}
