package net.naucu.englishxianshi.db.dao;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * 类名: Record.java 描述: TODO 录音记录 作者: fght 时间: 2016年1月4日 下午3:24:15
 */
@Table(name = "record")
public class Record {
    @Column(name = "Id", isId = true)
    private int id;// 唯一识别
    @Column(name = "DOWNLOADID")
    private int DownloadId;// 影片下载Id
    @Column(name = "ENDPOSITION")
    private int endPosition;// 结束位置
    @Column(name = "SENTENCESCORE")
    private String sentenceScore;// 每句得分
    @Column(name = "SCORE")
    private float score;// 平均得分
    @Column(name = "STARTPOSITION")
    private int startPosition;// 开始位置
    @Column(name = "RECORDPATHS")
    private String recordPaths;// 配音路径
    @Column(name = "SELECTLIST")
    private String selectList;// 选中语句
    @Column(name = "VIDEOLOCALPATH")
    private String videoLocalPath;// 影片本地路劲
    @Column(name = "VIDEONAME")
    private String videoName;// 影片本地路劲
    @Column(name = "MP3LOCALPATH")
    private String mp3LocalPath;// 本地路径
    @Column(name = "SRTLOCALPATH")
    private String srtLocalPath;// 字幕本地路劲
    @Column(name = "VIDEOPICTUREURL")
    private String videoPictureUrl;// 影片缩略图地址
    @Column(name = "BEGINTIME")
    private int beginTime;// 开始时间
    @Column(name = "ENDTIME")
    private int endTime;// 结束时间
    @Column(name = "INTRODUCTION")
    private String introduction;// 录音简介

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDownloadId() {
        return DownloadId;
    }

    public void setDownloadId(int downloadId) {
        DownloadId = downloadId;
    }

    public int getEndPosition() {
        return endPosition;
    }

    public void setEndPosition(int endPosition) {
        this.endPosition = endPosition;
    }

    public String getSentenceScore() {
        return sentenceScore;
    }

    public void setSentenceScore(String sentenceScore) {
        this.sentenceScore = sentenceScore;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public int getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(int startPosition) {
        this.startPosition = startPosition;
    }

    public String getRecordPaths() {
        return recordPaths;
    }

    public void setRecordPaths(String recordPaths) {
        this.recordPaths = recordPaths;
    }

    public String getSelectList() {
        return selectList;
    }

    public void setSelectList(String selectList) {
        this.selectList = selectList;
    }

    public String getVideoLocalPath() {
        return videoLocalPath;
    }

    public void setVideoLocalPath(String videoLocalPath) {
        this.videoLocalPath = videoLocalPath;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getMp3LocalPath() {
        return mp3LocalPath;
    }

    public void setMp3LocalPath(String mp3LocalPath) {
        this.mp3LocalPath = mp3LocalPath;
    }

    public String getSrtLocalPath() {
        return srtLocalPath;
    }

    public void setSrtLocalPath(String srtLocalPath) {
        this.srtLocalPath = srtLocalPath;
    }

    public String getVideoPictureUrl() {
        return videoPictureUrl;
    }

    public void setVideoPictureUrl(String videoPictureUrl) {
        this.videoPictureUrl = videoPictureUrl;
    }

    public int getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(int beginTime) {
        this.beginTime = beginTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    @Override
    public String toString() {
        return "Record [id=" + id + ", DownloadId=" + DownloadId + ", endPosition=" + endPosition + ", sentenceScore="
                + sentenceScore + ", score=" + score + ", startPosition=" + startPosition + ", recordPaths="
                + recordPaths + ", selectList=" + selectList + ", videoLocalPath=" + videoLocalPath + ", videoName="
                + videoName + ", mp3LocalPath=" + mp3LocalPath + ", srtLocalPath=" + srtLocalPath + ", videoPictureUrl="
                + videoPictureUrl + ", beginTime=" + beginTime + ", endTime=" + endTime + ", introduction="
                + introduction + "]";
    }

}
