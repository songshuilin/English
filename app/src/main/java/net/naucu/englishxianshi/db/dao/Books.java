package net.naucu.englishxianshi.db.dao;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "books")
public class Books {
    @Column(name = "Id", isId = true)
    private int id;//检索id
    @Column(name = "BOOKSNAME")
    private String booksName;//电子书名称
    @Column(name = "AUTHOR")
    private String booksAuthor;//作者
    @Column(name = "PIC")
    private String pic;//封面
    @Column(name = "UID")
    private int uid;//用户id
    @Column(name = "BOOKSLOCALURL")
    private String booksLocalUrl;//本地地址
    @Column(name = "DOWNLOADSTATUS")
    private String downloadStatus;//下载状态
    @Column(name = "BOOKSCASESTATUS")
    private String bookcaseStatus;//书架状态
    @Column(name = "DOWNLOADID")
    private String downloadId;//下载时的ID
    @Column(name = "PAYMENTSTATUS")
    private String PaymentStatus;//支付状态
    @Column(name = "INTRODUCTION")//简介
    private String introduction;
    @Column(name = "SCORE")//评分
    private double score;
    @Column(name = "LASTPOSITION")//上次退出时的位置
    private int lastPosition;

    private Boolean logos = false;//标示


    public int getLastPosition() {
        return lastPosition;
    }

    public void setLastPosition(int lastPosition) {
        this.lastPosition = lastPosition;
    }

    public double getScore() {
        return score;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public Boolean getLogos() {
        return logos;
    }

    public void setLogos(Boolean logos) {
        this.logos = logos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBooksName() {
        return booksName;
    }

    public void setBooksName(String booksName) {
        this.booksName = booksName;
    }

    public String getBooksAuthor() {
        return booksAuthor;
    }

    public void setBooksAuthor(String booksAuthor) {
        this.booksAuthor = booksAuthor;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getBooksLocalUrl() {
        return booksLocalUrl;
    }

    public void setBooksLocalUrl(String booksLocalUrl) {
        this.booksLocalUrl = booksLocalUrl;
    }

    public String getDownloadStatus() {
        return downloadStatus;
    }

    public void setDownloadStatus(String downloadStatus) {
        this.downloadStatus = downloadStatus;
    }

    public String getBookcaseStatus() {
        return bookcaseStatus;
    }

    public void setBookcaseStatus(String bookcaseStatus) {
        this.bookcaseStatus = bookcaseStatus;
    }

    public String getDownloadId() {
        return downloadId;
    }

    public void setDownloadId(String downloadId) {
        this.downloadId = downloadId;
    }

    public String getPaymentStatus() {
        return PaymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        PaymentStatus = paymentStatus;
    }


    @Override
    public String toString() {
        return "Books [id=" + id + ", booksName=" + booksName + ", booksAuthor=" + booksAuthor + ", pic=" + pic
                + ", uid=" + uid + ", booksNetUrl=" + ", booksLocalUrl=" + booksLocalUrl
                + ", downloadStatus=" + downloadStatus + ", bookcaseStatus=" + bookcaseStatus + ", downloadId="
                + downloadId + ", PaymentStatus=" + PaymentStatus + ", booksDetailsId=" + ", logos="
                + logos + "]";
    }

    public Books() {
        super();
    }

}
