package net.naucu.englishxianshi.bean;

import java.io.Serializable;

public class VideoBean implements Serializable {
    private static final long serialVersionUID = 7982555956186577466L;

    private String videoName;
    private String videoPictureUrl;
    private String videoPath;
    private String srtPath;
    private String mp3Path;

    private int downloadId;// 影片下载Id

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getVideoPictureUrl() {
        return videoPictureUrl;
    }

    public void setVideoPictureUrl(String videoPictureUrl) {
        this.videoPictureUrl = videoPictureUrl;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public String getSrtPath() {
        return srtPath;
    }

    public void setSrtPath(String srtPath) {
        this.srtPath = srtPath;
    }

    public int getDownloadId() {
        return downloadId;
    }

    public void setDownloadId(int downloadId) {
        this.downloadId = downloadId;
    }

    public String getMp3Path() {
        return mp3Path;
    }

    public void setMp3Path(String mp3Path) {
        this.mp3Path = mp3Path;
    }

    @Override
    public String toString() {
        return "VideoBean [videoName=" + videoName + ", videoPictureUrl=" + videoPictureUrl + ", videoPath=" + videoPath
                + ", srtPath=" + srtPath + ", mp3Path=" + mp3Path + ", downloadId=" + downloadId + "]";
    }

}
