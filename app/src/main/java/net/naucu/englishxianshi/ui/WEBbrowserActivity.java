package net.naucu.englishxianshi.ui;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.app.myTool.ActionTitleBarWidget;
import com.app.myTool.ActionTitleBarWidget.ClickListener;

import net.naucu.englishxianshi.R;
import net.naucu.englishxianshi.ui.base.BaseActivity;

/**
 * 类名: WEBbrowserActivity.java
 * 描述: Web浏览器
 * 作者: youyou_pc
 * 时间: 2015年12月16日  上午11:18:22
 */
public class WEBbrowserActivity extends BaseActivity {
    private WebView WEbbrowser;
    private ActionTitleBarWidget titlebar;//初始化标题控件

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webbrowser);
        initview();
        initTitleBar();
    }

    /**
     * 初始化TitleBar
     */
    private void initTitleBar() {
        titlebar.setTitleBackgroundResource(R.drawable.menubutton_select, 0);
        titlebar.setLeftGravity(Gravity.CENTER);
        titlebar.setLeftIco(R.drawable.back);
        titlebar.setCenterGravity(Gravity.CENTER);
    }

    private void initview() {
        WEbbrowser = (WebView) findViewById(R.id.web_browser);
        titlebar = (ActionTitleBarWidget) findViewById(R.id.titlebar);
        titlebar.OnTitleBarClickListener(clickListener);
        setdate();
        setWebView();
    }

    private ClickListener clickListener = new ClickListener() {

        @Override
        public void onright(View v) {

        }

        @Override
        public void onleft(View v) {
            if (WEbbrowser.canGoBack()) {
                WEbbrowser.goBack();
            } else {
                finish();
            }
        }

        @Override
        public void oncenter(View arg0) {

        }
    };

    @SuppressLint("SetJavaScriptEnabled")
    private void setWebView() {
        WebSettings webSettings = WEbbrowser.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setLoadWithOverviewMode(true);
        WEbbrowser.setWebViewClient(new WebViewClient() {
            @Override
            public void onFormResubmission(WebView view, Message dontResend, Message resend) {
                super.onFormResubmission(view, dontResend, resend);
            }

        });
        WEbbrowser.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                titlebar.setCenterText(title, 17, Color.BLACK);
            }
        });
        WEbbrowser.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                //用javascript隐藏系统定义的404页面信息
                String data = "Page NO FOUND！";
                view.loadUrl("javascript:document.body.innerHTML=\"" + data + "\"");
            }

        });
    }

    private void setdate() {
        String httpurl = getIntent().getStringExtra("url");
        if (!TextUtils.isEmpty(httpurl)) {
            WEbbrowser.loadUrl(httpurl);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (WEbbrowser.canGoBack()) {
                WEbbrowser.goBack();//返回上一页面
                return true;
            } else {
                finish();
            }
        }
        return false;
    }
}
