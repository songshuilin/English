package net.naucu.englishxianshi.db.dao;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * 类名: Movie.java 描述: TODO 电影数据库 作者: youyou_pc 时间: 2015年12月30日 下午3:16:07
 */
@Table(name = "movie")
public class Movie {
    @Column(name = "Id", isId = true)
    private int id;// 唯一识别
    @Column(name = "FILMNAME")
    private String FilmName;// 影片名称
    @Column(name = "FILMCOVERS")
    private String FilmCovers;// 影片封面图片
    @Column(name = "SCORE")
    private String Score;// 评分
    @Column(name = "AUTHOR")
    private String Author;// 作者
    @Column(name = "NATIONALITY")
    private String Nationality;// 国籍
    @Column(name = "FILMGENRE")
    private String FilmGenre;// 片种
    @Column(name = "YINGKUIDENTIFIER")
    private int YingKuIdentifier; // 加入影库识别符
    @Column(name = "DOWNLOADID")
    private int DownloadId;// 影片下载Id
    @Column(name = "INTRODUCTION")
    private String introduction;// 简介
    @Column(name = "LASTPOSITION")
    private int lastPosition;// 上次观看位置
    @Column(name = "LOCALVIDEOPATH")
    private String localVideoPath;// 本地电影路径
    @Column(name = "LOCALSRTPATH")
    private String localSrtPath;// 本地字幕路径
    @Column(name = "LOCALMP3PATH")
    private String localMp3Path;// 本地mp3路径
    @Column(name = "ISTV")
    private boolean isTV;// 是否为美剧


    private boolean Logos;//多项删除操作标识


    public String getLocalMP3Path() {
        return localMp3Path;
    }

    public void setLocalMP3Path(String localMp3Path) {
        this.localMp3Path = localMp3Path;
    }

    public boolean isTV() {
        return isTV;
    }

    public void setTV(boolean isTV) {
        this.isTV = isTV;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isLogos() {
        return Logos;
    }

    public void setLogos(boolean logos) {
        Logos = logos;
    }


    public String getFilmName() {
        return FilmName;
    }

    public void setFilmName(String filmName) {
        FilmName = filmName;
    }

    public String getFilmCovers() {
        return FilmCovers;
    }

    public void setFilmCovers(String filmCovers) {
        FilmCovers = filmCovers;
    }

    public String getScore() {
        return Score;
    }

    public void setScore(String score) {
        Score = score;
    }

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        Author = author;
    }

    public String getNationality() {
        return Nationality;
    }

    public void setNationality(String nationality) {
        Nationality = nationality;
    }

    public String getFilmGenre() {
        return FilmGenre;
    }

    public void setFilmGenre(String filmGenre) {
        FilmGenre = filmGenre;
    }

    public int getYingKuIdentifier() {
        return YingKuIdentifier;
    }

    public void setYingKuIdentifier(int yingKuIdentifier) {
        YingKuIdentifier = yingKuIdentifier;
    }

    public int getDownloadId() {
        return DownloadId;
    }

    public void setDownloadId(int downloadId) {
        DownloadId = downloadId;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public int getLastPosition() {
        return lastPosition;
    }

    public void setLastPosition(int lastPosition) {
        this.lastPosition = lastPosition;
    }

    public String getLocalVideoPath() {
        return localVideoPath;
    }

    public void setLocalVideoPath(String localVideoPath) {
        this.localVideoPath = localVideoPath;
    }

    public String getLocalSrtPath() {
        return localSrtPath;
    }

    public void setLocalSrtPath(String localSrtPath) {
        this.localSrtPath = localSrtPath;
    }

    public Movie() {
        super();
        // TODO Auto-generated constructor stub
    }

    @Override
    public String toString() {
        return "Movie [id=" + id + ", FilmName=" + FilmName + ", FilmCovers=" + FilmCovers + ", Score=" + Score
                + ", Author=" + Author + ", Nationality=" + Nationality + ", FilmGenre=" + FilmGenre
                + ", YingKuIdentifier=" + YingKuIdentifier + ", DownloadId=" + DownloadId + ", introduction="
                + introduction + ", lastPosition=" + lastPosition + ", localVideoPath=" + localVideoPath
                + ", localSrtPath=" + localSrtPath + ", localMp3Path=" + localMp3Path + ", isTV=" + isTV + ", Logos="
                + Logos + "]";
    }


}
