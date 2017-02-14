package com.lijunsai.httpInterface.implementation;

import android.content.Context;

import com.lijunsai.httpInterface.HttpInterface;
import com.lijunsai.httpInterface.tool.SharedPreTool;

public class HttpImplementation {
    private static HttpInterface httpInterface = new HttpInterface();
    public static Context context;

    /**
     * 初始化网络
     */
    public static void InItHttpUrl(Context context, String state) {
        SharedPreTool.setSharedPreDateString(context, "httpState", state);
        HttpImplementation.context = context;
    }

    /**
     * 获取总地址
     */
    public static String getHttpUrl() {
        String state = SharedPreTool.getSharedPreDateString(context, "httpState", null);
        if (state.equals("Offline")) {
            return httpInterface.getOfflineHttpUrl();
        } else if (state.equals("Online")) {
            return httpInterface.getOnlineHttpUrl();
        }
        return "\u975e\u6cd5\u7834\u89e3";
    }

    /**
     * 登录地址
     */
    public static String getLogin() {
        return getHttpUrl() + httpInterface.getLogin();
    }

    /**
     * 获取注册验证码
     */
    public static String getRegisterGetCode() {
        return getHttpUrl() + httpInterface.getRegisterGetCode();
    }

    /**
     * 修改密码验证码
     */
    public static String getModifyPasswordUrl() {
        return getHttpUrl() + httpInterface.modifyPassword();
    }

    /**
     * 注册
     */
    public static String getRegister() {
        return getHttpUrl() + httpInterface.getRegister();
    }

    /**
     * 修改手机号验证码
     */
    public static String getModifyPhonenumberUrl() {
        return getHttpUrl() + httpInterface.modifyPhonenumber();
    }

    /**
     * 修改密码
     */
    public static String getchangePassword() {
        return getHttpUrl() + httpInterface.getchangePassword();
    }

    /**
     * 添加头像
     */
    public static String addHeadPortrait() {
        return getHttpUrl() + httpInterface.addHeadPortrait();
    }

    /**
     * 添加性别
     */
    public static String addGender() {
        return getHttpUrl() + httpInterface.addGender();
    }

    /**
     * 添加昵称
     */
    public static String addNickname() {
        return getHttpUrl() + httpInterface.addNickname();
    }

    /**
     * 获取用户信息
     */
    public static String getUser() {
        return getHttpUrl() + httpInterface.getUser();
    }

    /**
     * 获取首页推荐电子书
     */
    public static String getALLbook() {
        return getHttpUrl() + httpInterface.getALLbook();
    }

    /**
     * 获取分类
     */
    public static String getDirectoryAccess() {
        return getHttpUrl() + httpInterface.getDirectoryAccess();
    }

    /**
     * 获取电子书二级
     */
    public static String getALLbookSecondary() {
        return getHttpUrl() + httpInterface.getALLbookSecondary();
    }

    /**
     * 获取欢迎页
     */
    public static String getWelcome() {
        return getHttpUrl() + httpInterface.getWelcome();
    }

    /**
     * 查询电子书
     */
    public static String getQueryBookby() {
        return getHttpUrl() + httpInterface.getQueryBookby();
    }

    /**
     * 查询电子书等级
     */
    public static String getQueryBookbyLevel() {
        return getHttpUrl() + httpInterface.getQueryBookbyLevel();
    }

    /**
     * 查询电子书下载
     */
    public static String getQueryDownload() {
        return getHttpUrl() + httpInterface.getQueryDownload();
    }

    /**
     * 查询电影
     */
    public static String getMovie() {
        return getHttpUrl() + httpInterface.getMovie();
    }

    /**
     * 电影子类别
     */
    public static String getMovieBytwoCategory() {
        return getHttpUrl() + httpInterface.getMovieBytwoCategory();
    }

    /**
     * 搜索电影
     */
    public static String getqueryMoviesbysearch() {
        return getHttpUrl() + httpInterface.getqueryMoviesbysearch();
    }

    /**
     * 下载电影字幕
     */
    public static String getMoviesSubtitles() {
        return getHttpUrl() + httpInterface.getMoviesSubtitles();
    }

    /**
     * 支付宝
     */
    public static String getPaytreasure() {
        return getHttpUrl() + httpInterface.getPaytreasure();
    }

    /**
     * 支付宝支付状态回调
     */
    public static String getPayreturn() {
        return getHttpUrl() + httpInterface.getPayreturn();
    }

    /**
     * 支付金额
     *
     * @return
     */
    public static String selectPrice() {
        return getHttpUrl() + httpInterface.selectPrice();
    }

    /**
     * 微信
     */
    public static String getweiXinPay() {
        return getHttpUrl() + httpInterface.getweiXinPay();
    }

    /**
     * 微信状态回调
     */
    public static String getweiXinPayReturn() {
        return getHttpUrl() + httpInterface.getweiXinPayReturn();
    }


    /**
     * 获取美剧接口
     */
    public static String getTVShow() {
        return getHttpUrl() + httpInterface.getTVShow();
    }

    /**
     * 百度翻译
     */
    public static String getBaiduTranslation() {
        return getHttpUrl() + httpInterface.getBaiduTranslation();
    }

    /**
     * 美剧二级
     */
    public static String getTvShowTelevBy() {
        return getHttpUrl() + httpInterface.getTvShowTelevBy();
    }

    /**
     * 美剧二级下载
     */
    public static String getTvShowTelevFlie() {
        return getHttpUrl() + httpInterface.getTvShowTelevFlie();
    }
}
