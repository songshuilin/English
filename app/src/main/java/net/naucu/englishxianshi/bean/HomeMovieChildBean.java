package net.naucu.englishxianshi.bean;

/**
 * 类名: HomeMovieChildBean.java
 * 描述: TODO 电影子级视频类别实体类
 * 作者: youyou_pc
 * <p/>
 * 时间: 2015年11月17日  下午3:54:35
 */
public class HomeMovieChildBean {
    private String MoviePicturesUrl;//电影图片
    private String MovieName;//电影名称
    private int MovieLevel;//电影等级
    private String MovieIntroduction;//电影简介
    private String MoviePlayUrl;//电影下载地址

    public String getMoviePicturesUrl() {
        return MoviePicturesUrl;
    }

    public void setMoviePicturesUrl(String moviePicturesUrl) {
        MoviePicturesUrl = moviePicturesUrl;
    }

    public String getMovieName() {
        return MovieName;
    }

    public void setMovieName(String movieName) {
        MovieName = movieName;
    }

    public int getMovieLevel() {
        return MovieLevel;
    }

    public void setMovieLevel(int movieLevel) {
        MovieLevel = movieLevel;
    }

    public String getMovieIntroduction() {
        return MovieIntroduction;
    }

    public void setMovieIntroduction(String movieIntroduction) {
        MovieIntroduction = movieIntroduction;
    }

    public String getMoviePlayUrl() {
        return MoviePlayUrl;
    }

    public void setMoviePlayUrl(String moviePlayUrl) {
        MoviePlayUrl = moviePlayUrl;
    }

    @Override
    public String toString() {
        return "HomeMovieChildBean [MoviePicturesUrl=" + MoviePicturesUrl + ", MovieName=" + MovieName + ", MovieLevel="
                + MovieLevel + ", MovieIntroduction=" + MovieIntroduction + ", MoviePlayUrl=" + MoviePlayUrl + "]";
    }

    public HomeMovieChildBean(String moviePicturesUrl, String movieName, int movieLevel, String movieIntroduction,
                              String moviePlayUrl) {
        super();
        MoviePicturesUrl = moviePicturesUrl;
        MovieName = movieName;
        MovieLevel = movieLevel;
        MovieIntroduction = movieIntroduction;
        MoviePlayUrl = moviePlayUrl;
    }

    public HomeMovieChildBean() {
        super();
        // TODO Auto-generated constructor stub
    }

}
