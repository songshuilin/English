package net.naucu.englishxianshi.bean;

/**
 * Created by Y on 2017/1/4.
 */

public class HelpVideoBean {

    private int id;
    private String videourl;
    private String videoTitle;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVideourl() {
        return videourl;
    }

    public void setVideourl(String videourl) {
        this.videourl = videourl;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    @Override
    public String toString() {
        return "HelpVideoBean{" +
                "id=" + id +
                ", videourl='" + videourl + '\'' +
                ", videoTitle='" + videoTitle + '\'' +
                '}';
    }
}
