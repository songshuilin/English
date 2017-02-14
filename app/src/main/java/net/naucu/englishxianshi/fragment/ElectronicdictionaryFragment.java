package net.naucu.englishxianshi.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import net.naucu.englishxianshi.R;

/**
 * 类名: ElectronicdictionaryFragment.java 描述: 电子词典 作者: youyou_pc 时间: 2015年12月29日
 * 下午5:28:53
 */
public class ElectronicdictionaryFragment extends Fragment {
    final static String wordurl = "http://dict.cn/mini.php?q=";

    private WebView webView;

    private Button _tempview;


    @SuppressWarnings("deprecation")
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_electronicdictionary, container, false);
        _tempview = (Button) view.findViewById(R.id.bt_temp);
        webView = (WebView) view.findViewById(R.id.web_word);
        initWebView();
        return view;
    }

    private void initWebView() {


        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setPluginState(PluginState.ON);
        webView.setBackgroundColor(0);
        webView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        _tempview.setVisibility(View.INVISIBLE);
        // webView.setWebChromeClient(new WebChromeClient() {
        // public void onProgressChanged(WebView view, int progress) {
        // // Activity和Webview根据加载程度决定进度条的进度大小
        // // 当加载到100%的时候 进度条自动消失
        // }
        // });
    }

    public void translate(String word) {
        String startZm = null;
        int spaceIndex = 0;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if ((c >= 65 && c <= 90) || (c >= 97 && c <= 122)) {
                startZm = word.substring(i, word.length());
                break;
            }
        }
        for (int i = 0; i < startZm.length(); i++) {
            char c = startZm.charAt(i);
            if (c == 32) {
                spaceIndex += 1;
            }
        }
        if (spaceIndex > 1) {
            Toast.makeText(this.getContext(), "请选择要查询的单词", Toast.LENGTH_SHORT).show();
        } else {
            if (webView == null) {
                initWebView();
            }
            webView.setBackgroundColor(0xffffffff);
            webView.loadUrl(wordurl + startZm);
            _tempview.setVisibility(View.VISIBLE);
        }
    }
}
