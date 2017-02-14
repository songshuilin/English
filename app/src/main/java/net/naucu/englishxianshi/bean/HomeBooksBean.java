package net.naucu.englishxianshi.bean;

import java.util.List;

/**
 * 类名: HomeBooksBean.java
 * 描述: TODO 首页电子书 实体
 * <p/>
 * 作者: youyou_pc
 * 时间: 2015年11月17日  上午11:22:50
 */
public class HomeBooksBean {
    private String televisionType;//影视类型
    private List<HomeBooksChildBean> booksChildBeans;//书详细内容

    public String getTelevisionType() {
        return televisionType;
    }

    public void setTelevisionType(String televisionType) {
        this.televisionType = televisionType;
    }

    public List<HomeBooksChildBean> getBooksChildBeans() {
        return booksChildBeans;
    }

    public void setBooksChildBeans(List<HomeBooksChildBean> booksChildBeans) {
        this.booksChildBeans = booksChildBeans;
    }

    @Override
    public String toString() {
        return "HomeBooksBean [televisionType=" + televisionType + ", booksChildBeans=" + booksChildBeans + "]";
    }

    public HomeBooksBean() {
        super();
    }

    public HomeBooksBean(String televisionType, List<HomeBooksChildBean> booksChildBeans) {
        super();
        this.televisionType = televisionType;
        this.booksChildBeans = booksChildBeans;
    }
}

