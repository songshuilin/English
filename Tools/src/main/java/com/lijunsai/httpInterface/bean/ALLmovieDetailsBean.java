package com.lijunsai.httpInterface.bean;

import java.io.Serializable;

public class ALLmovieDetailsBean implements Serializable {

    private String createTime;
    private String firstCategory;
    private int id;
    private String level;
    private String movieAbout;
    private String movieAuthor;
    private String movieImageUrl;
    private String movieMadeBy;
    private String movieName;
    private String moviepingfen;
    private String twoCategory;
    private boolean isTV;
    private int type =0;
    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }


    public boolean isTV() {
        return isTV;
    }

    public void setTV(boolean isTV) {
        this.isTV = isTV;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getTwoCategory() {
        return twoCategory;
    }

    public void setTwoCategory(String twoCategory) {
        this.twoCategory = twoCategory;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getMovieAbout() {
        return movieAbout;
    }

    public void setMovieAbout(String movieAbout) {
        this.movieAbout = movieAbout;
    }

    public String getMovieAuthor() {
        return movieAuthor;
    }

    public void setMovieAuthor(String movieAuthor) {
        this.movieAuthor = movieAuthor;
    }

    public String getMovieImageUrl() {
        return movieImageUrl.replaceAll("\\\\", "/");
    }

    public void setMovieImageUrl(String movieImageUrl) {
        this.movieImageUrl = movieImageUrl;
    }

    public String getMovieMadeBy() {
        return movieMadeBy;
    }

    public void setMovieMadeBy(String movieMadeBy) {
        this.movieMadeBy = movieMadeBy;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getMoviepingfen() {
        return moviepingfen;
    }

    public void setMoviepingfen(String moviepingfen) {
        this.moviepingfen = moviepingfen;
    }

    public ALLmovieDetailsBean() {
        super();
    }

    @Override
    public String toString() {
        return "ALLmovieDetailsBean [createTime=" + createTime + ", firstCategory=" + firstCategory + ", id=" + id
                + ", level=" + level + ", movieAbout=" + movieAbout + ", movieAuthor=" + movieAuthor
                + ", movieImageUrl=" + movieImageUrl + ", movieMadeBy=" + movieMadeBy + ", movieName=" + movieName
                + ", moviepingfen=" + moviepingfen + ", twoCategory=" + twoCategory + ", isTV=" + isTV + "]";
    }

}
