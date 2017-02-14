package com.lijunsai.httpInterface.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 类名: ALLmovieBean.java
 * 描述: TODO 电影
 * 作者: youyou_pc
 * 时间: 2015年12月21日  下午2:16:31
 */
public class ALLmovieBean implements Serializable {

    private String twoCategory;
    private List<ALLmovieDetailsBean> getalLmovieDetailsBeans;

    public String getTwoCategory() {
        return twoCategory;
    }

    public void setTwoCategory(String twoCategory) {
        this.twoCategory = twoCategory;
    }

    public List<ALLmovieDetailsBean> getGetalLmovieDetailsBeans() {
        return getalLmovieDetailsBeans;
    }

    public void setGetalLmovieDetailsBeans(List<ALLmovieDetailsBean> getalLmovieDetailsBeans) {
        this.getalLmovieDetailsBeans = getalLmovieDetailsBeans;
    }

    public ALLmovieBean(String twoCategory, List<ALLmovieDetailsBean> getalLmovieDetailsBeans) {
        super();
        this.twoCategory = twoCategory;
        this.getalLmovieDetailsBeans = getalLmovieDetailsBeans;
    }

    public ALLmovieBean() {
        super();
        // TODO Auto-generated constructor stub
    }

}
