package com.lijunsai.httpInterface.bean;

import java.io.Serializable;

/**
 * 类名: ALLbookBean.java
 * 描述: TODO 获取电子书
 * 作者: youyou_pc
 * 时间: 2015年12月15日  上午10:04:06
 */
public class ALLbookDetailsBean implements Serializable {

    private String bookAbout;
    private String bookAuthor;
    private String bookImage;
    private String bookName;
    private double bookpingfen;
    private String createTime;
    private String firstCategory;
    private String id;
    private String price;
    private String twoCategory;
    private boolean isNovel;


    public boolean isNovel() {
        return isNovel;
    }

    public void setNovel(boolean isNovel) {
        this.isNovel = isNovel;
    }

    public String getBookAbout() {
        return bookAbout;
    }

    public void setBookAbout(String bookAbout) {
        this.bookAbout = bookAbout;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public String getBookImage() {
        return bookImage.replaceAll("\\\\", "/");
    }

    public void setBookImage(String bookImage) {
        this.bookImage = bookImage;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public double getBookpingfen() {
        return bookpingfen;
    }

    public void setBookpingfen(double bookpingfen) {
        this.bookpingfen = bookpingfen;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getFirstCategory() {
        return firstCategory;
    }

    public void setFirstCategory(String firstCategory) {
        this.firstCategory = firstCategory;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "ALLbookDetailsBean [bookAbout=" + bookAbout + ", bookAuthor=" + bookAuthor +
                ", bookImage=" + bookImage + ", bookName=" + bookName + ", bookpingfen=" + bookpingfen
                + ", createTime=" + createTime + ", firstCategory=" + firstCategory + ", id=" + id +
                ", price=" + price + ", twoCategory=" + twoCategory + ", isNovel=" + isNovel + "]";
    }

    public ALLbookDetailsBean(String bookAbout, String bookAuthor, String bookImage, String bookName,
                              int bookpingfen, String createTime, String firstCategory, String id, String price) {
        super();
        this.bookAbout = bookAbout;
        this.bookAuthor = bookAuthor;
        this.bookImage = bookImage;
        this.bookName = bookName;
        this.bookpingfen = bookpingfen;
        this.createTime = createTime;
        this.firstCategory = firstCategory;
        this.id = id;
        this.price = price;
    }

    public ALLbookDetailsBean() {
        super();
    }

    public String getTwoCategory() {
        return twoCategory;
    }

    public void setTwoCategory(String twoCategory) {
        this.twoCategory = twoCategory;
    }

}
