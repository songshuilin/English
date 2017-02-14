package com.lijunsai.httpInterface;

public class HttpInterface {

    static {
        System.loadLibrary("http-url");
    }

    public native String getOnlineHttpUrl();

    public native String getOfflineHttpUrl();

    public native String modifyPhonenumber();

    public native String getLogin();

    public native String getRegisterGetCode();

    public native String getRegister();

    public native String modifyPassword();

    public native String getchangePassword();

    public native String addHeadPortrait();

    public native String addNickname();

    public native String addGender();

    public native String getUser();

    public native String getALLbook();

    public native String getDirectoryAccess();

    public native String getALLbookSecondary();

    public native String getWelcome();

    public native String getQueryBookby();

    public native String getQueryBookbyLevel();

    public native String getQueryDownload();

    public native String getMovie();

    public native String getMovieBytwoCategory();

    public native String getqueryMoviesbysearch();

    public native String getMoviesSubtitles();

    public native String getPaytreasure();

    public native String getPayreturn();

    public native String selectPrice();

    public native String getweiXinPay();

    public native String getweiXinPayReturn();

    public native String getTVShow();

    public native String getBaiduTranslation();

    public native String getTvShowTelevBy();

    public native String getTvShowTelevFlie();
}
