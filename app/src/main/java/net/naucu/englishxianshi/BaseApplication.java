package net.naucu.englishxianshi;

import android.app.Application;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.multidex.MultiDex;

import com.iflytek.cloud.Setting;
import com.iflytek.cloud.SpeechUtility;
import com.jiongbull.jlog.JLog;
import com.lijunsai.httpInterface.bean.LoginBean;
import com.lijunsai.httpInterface.implementation.HttpImplementation;
import com.lijunsai.httpInterface.tool.CardUtils;
import com.zxy.recovery.core.Recovery;

import org.xutils.common.util.MD5;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import cn.jpush.android.api.JPushInterface;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * 类名: BaseApplication.java 描述: 全局数据持久 作者: youyou_pc 时间: 2015年11月16日 上午9:50:47
 */
public class BaseApplication extends Application {
    private static ImageOptions imageOptions;
    private LoginBean loginApplication;
    private static String downUrl = "";



    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/simyou.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
//        Recovery.getInstance()
//                .debug(true)
//                .recoverInBackground(true)
//                .silent(true, Recovery.SilentMode.RESTART)
//                .init(this);

        MultiDex.install(this);
        WifiManager wm = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        StringBuffer divice = new StringBuffer();
        divice.append(wm.getConnectionInfo().getMacAddress());
        divice.append(Build.BOARD);
        divice.append(Build.DEVICE);
        divice.append(Build.FINGERPRINT);
        MD5.md5(divice.toString());
        //全局异常监听
        // CrashHandler.getInstance().init(getApplicationContext());
        //初始化Xutils
        x.Ext.init(this);
        //x.Ext.setDebug(true);
        //初始化极光推送
        JPushInterface.init(this);
        JPushInterface.setDebugMode(true);
        //初始化 Jlog
        JLog.init(this).setDebug(true);

        SpeechUtility.createUtility(this, "appid=" + getString(R.string.iflytek_id));
        HttpImplementation.InItHttpUrl(this, "Online");// Online Offline
        Setting.setShowLog(true);
        CardUtils.initBasePath(this);
    }

    public static ImageOptions getImageOption() {
        if (imageOptions == null) {
            imageOptions = new ImageOptions
                    .Builder()
                    .setLoadingDrawableId(R.mipmap.ic_launcher)
                    .setFailureDrawableId(R.mipmap.ic_launcher)
                    .setIgnoreGif(true)
                    .build();
        }
        return imageOptions;
    }

    public LoginBean getLoginApplication() {
        return loginApplication;
    }

    public void setLoginApplication(LoginBean loginApplication) {
        this.loginApplication = loginApplication;
    }

    public static String getDownUrl() {
        return downUrl;
    }

    public static void setDownUrl(String downUrl) {
        BaseApplication.downUrl = downUrl;
    }


}
