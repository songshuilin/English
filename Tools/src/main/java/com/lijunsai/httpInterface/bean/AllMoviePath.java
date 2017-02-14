package com.lijunsai.httpInterface.bean;

public class AllMoviePath {

    private String movieurl;
    private String moviestrurl;
    private String moviemp3url;
    private int number;

    public String getMoviemp3url() {
        return moviemp3url;
    }

    public void setMoviemp3url(String moviemp3url) {
        this.moviemp3url = moviemp3url;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getMovieurl() {
        return movieurl;
    }

    public void setMovieurl(String movieurl) {
        this.movieurl = movieurl;
    }

    public String getMoviestrurl() {
        return moviestrurl;
    }

    public void setMoviestrurl(String moviestrurl) {
        this.moviestrurl = moviestrurl;
    }

    public AllMoviePath(String movieurl, String moviestrurl, String moviemp3url) {
        super();
        this.movieurl = movieurl;
        this.moviestrurl = moviestrurl;
        this.moviemp3url = moviemp3url;
    }

    public AllMoviePath(String movieurl, String moviestrurl, String moviemp3url, int number) {
        super();
        this.movieurl = movieurl;
        this.moviestrurl = moviestrurl;
        this.moviemp3url = moviemp3url;
        this.number = number;
    }

    public AllMoviePath() {
        super();
    }

}
