package com.lijunsai.httpInterface.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 类名: ALLbookBean.java
 * 描述: TODO 获取电子书
 * 作者: youyou_pc
 * 时间: 2015年12月15日  上午10:04:06
 */
public class ALLbookBean implements Serializable {

    private String twoCategory;
    private List<ALLbookDetailsBean> getbookDetailsBeans;

    public String getTwoCategory() {
        return twoCategory;
    }

    public void setTwoCategory(String twoCategory) {
        this.twoCategory = twoCategory;
    }

    public List<ALLbookDetailsBean> getGetbookDetailsBeans() {
        return getbookDetailsBeans;
    }

    public void setGetbookDetailsBeans(List<ALLbookDetailsBean> getbookDetailsBeans) {
        this.getbookDetailsBeans = getbookDetailsBeans;
    }

    public ALLbookBean() {
        super();
    }

    public ALLbookBean(String twoCategory, List<ALLbookDetailsBean> getbookDetailsBeans) {
        super();
        this.twoCategory = twoCategory;
        this.getbookDetailsBeans = getbookDetailsBeans;
    }


}
