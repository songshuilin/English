package net.naucu.englishxianshi.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.lijunsai.httpInterface.implementation.HttpImplementation;
import com.lijunsai.httpInterface.request.NetworkRequestDeal;

import net.naucu.englishxianshi.R;
import net.naucu.englishxianshi.ui.base.BaseActivity;
import net.naucu.englishxianshi.util.NetTool;
import net.naucu.englishxianshi.util.SharedPreTool;

import org.xutils.common.Callback.CommonCallback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;

/**
 * 类名: WelcomeActivity.java 描述: 欢迎页 作者: youyou_pc 时间: 2015年11月16日 上午9:43:48
 */
public class WelcomeActivity extends BaseActivity {
    public static final String TAG = WelcomeActivity.class.getSimpleName();
    // Handler
    private static WelcomeHandler handler;

    // 账号
    private String account;
    // 密码
    private String password;

    public String szImei;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        init();
    }

    /**
     * 初始化用户信息
     * 并将信息保存至ShardPreference
     */
    private void init() {
        account = SharedPreTool.getSharedPreDateString(this, "loginPhonenumber", "");
        password = SharedPreTool.getSharedPreDateString(this, "loginArticleCode", "");
        TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        szImei = TelephonyMgr.getDeviceId();


        /**
         * 初始化handle
         *
         * */
        handler = new WelcomeHandler(this);
        /**
         * 判断当前网络状态
         * */
        if (NetTool.isNetworkConnected(this)) {
            if (NetTool.isWifiConnected(this)) {
                Toast.makeText(this, "正在使用Wi-Fi网络", Toast.LENGTH_SHORT).show();
            } else if (NetTool.isMobileConnected(this)) {
                Toast.makeText(this, "正在使用3G/4G网络", Toast.LENGTH_SHORT).show();
            }
            if(!account.equals("")&&!password.equals("")){
                handler.sendEmptyMessageDelayed(WelcomeHandler.WHAT_AUTO_LOGIN, WelcomeHandler.DELAY_TIME);
            }else{
                handler.sendEmptyMessage(WelcomeHandler.START_GUIDE);
            }

        } else {
            handler.sendEmptyMessageDelayed(WelcomeHandler.START_MAIN, WelcomeHandler.DELAY_TIME);
            Toast.makeText(this, "网络异常,请检查网络连接状态", Toast.LENGTH_SHORT).show();
        }


    }


    @Override
    protected void onResume() {
        if (handler == null) {
            handler = new WelcomeHandler(this);
            /**
             * 判断当前网络状态
             * */
            if (NetTool.isNetworkConnected(this)) {
                if (NetTool.isWifiConnected(this)) {
                    Toast.makeText(this, "正在使用Wi-Fi网络", Toast.LENGTH_SHORT).show();
                } else if (NetTool.isMobileConnected(this)) {
                    Toast.makeText(this, "正在使用3G/4G网络", Toast.LENGTH_SHORT).show();
                }
                if(!account.equals("")&&!password.equals("")){
                    handler.sendEmptyMessageDelayed(WelcomeHandler.WHAT_AUTO_LOGIN, WelcomeHandler.DELAY_TIME);
                }else{
                    handler.sendEmptyMessage(WelcomeHandler.START_GUIDE);
                }

            } else {
                handler.sendEmptyMessageDelayed(WelcomeHandler.START_MAIN, WelcomeHandler.DELAY_TIME);
                Toast.makeText(this, "当前网络不可用，请尝试重新连接", Toast.LENGTH_SHORT).show();
            }
        }
        super.onResume();
    }

    /**
     * Handler
     */
    private static class WelcomeHandler extends Handler {
        private final WeakReference<WelcomeActivity> reference;

        public WelcomeHandler(WelcomeActivity activity) {
            reference = new WeakReference<>(activity);
        }

        // 欢迎页图片
        public static final int WHAT_WELCOME = 11;
        // 登录
        public static final int WHAT_AUTO_LOGIN = 12;

        // 打开主页
        private static final int START_MAIN = 21;
        // 打开引导页
        private static final int START_GUIDE = 22;
        // 延迟时间
        private static final int DELAY_TIME = 2000;

        @Override
        public void handleMessage(Message msg) {
            if (reference.get() == null)
                return;
            switch (msg.what) {
                case WHAT_WELCOME:
                    x.http().get(new RequestParams(HttpImplementation.getWelcome()), welcomeCallback);
                    break;
                case WHAT_AUTO_LOGIN:
                    String jsonParams = "{'telephone':'" + reference.get().account + "','password':'"
                            + reference.get().password + "','serialnumber':" +reference.get().szImei + "}";

                    x.http().get(new RequestParams(HttpImplementation.getLogin() + "?info=" + jsonParams), loginCallback);
                    break;
                case START_MAIN:
                    startMain();
                    break;
                case START_GUIDE:
                    startGuide();
                    break;
            }
        }

        /**
         * 欢迎页数据回调
         */
        private SimpleCommonCallback<String> welcomeCallback = new SimpleCommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                handler.sendEmptyMessage(WHAT_AUTO_LOGIN);
            }

            @Override
            public void onError(Throwable throwable, boolean b) {

                handler.sendEmptyMessage(WHAT_AUTO_LOGIN);
            }
        };

        /**
         * 登录回调
         */
        private SimpleCommonCallback<String> loginCallback = new SimpleCommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if ((result == null || result.length() <= 0) || NetworkRequestDeal.isErrCode1(result)) {
                    sendEmptyMessageDelayed(START_GUIDE, DELAY_TIME);
                } else {
                    application.setLoginApplication(NetworkRequestDeal.getLogin(result));
                    sendEmptyMessageDelayed(START_MAIN, DELAY_TIME);
                }
            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                sendEmptyMessageDelayed(START_GUIDE, DELAY_TIME);
            }
        };

        /**
         * 打开主页
         */
        private void startMain() {
            reference.get().startActivity(new Intent(reference.get(), HomeActivity.class));
            reference.get().finish();
        }

        /**
         * 打开引导页
         */
        private void startGuide() {
            reference.get().startActivity(new Intent(reference.get(), WelcomeLoginActivity.class));
            reference.get().finish();
        }

    }

    /**
     * Simple Callback
     *
     * @param <T>
     */
    private static class SimpleCommonCallback<T> implements CommonCallback<T> {

        @Override
        public void onSuccess(T t) {

        }

        @Override
        public void onError(Throwable throwable, boolean b) {
//            JLog.e((throwable == null || throwable.getMessage() == null) ? "Throwable is null" : throwable.getMessage());
        }

        @Override
        public void onCancelled(CancelledException e) {
//            JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());
        }

        @Override
        public void onFinished() {

        }
    }
}
