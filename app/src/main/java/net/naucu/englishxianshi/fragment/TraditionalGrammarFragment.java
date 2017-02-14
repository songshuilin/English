package net.naucu.englishxianshi.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import net.naucu.englishxianshi.R;
import net.naucu.englishxianshi.ui.MasaGrammarActivity;

public class TraditionalGrammarFragment extends Fragment {
    public static final String TAG = TraditionalGrammarFragment.class.getSimpleName();

    private View view;
    private String key;
    private WebView webview;
    public static Context context;

    private MasaGrammarActivity.BianSeCallBack bianSeCallBack;

    public TraditionalGrammarFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_yufact, container, false);
        key = getArguments().getString("key");
        webview = (WebView) view.findViewById(R.id.web_ctyufa);
        webview.setBackgroundColor(0);
        webview.setOverScrollMode(View.OVER_SCROLL_NEVER);
        WebSettings webSettings = webview.getSettings();
        //webSettings.setJavaScriptEnabled(true);
        webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setDefaultFontSize(18);

        showWeb("");
        return view;
    }

    public static TraditionalGrammarFragment getInstance(String key, Context context) {
        TraditionalGrammarFragment f = new TraditionalGrammarFragment(null);
        TraditionalGrammarFragment.context = context;
        Bundle args = new Bundle();
        args.putString("key", key);
        f.setArguments(args);
        return f;
    }


    public TraditionalGrammarFragment(MasaGrammarActivity.BianSeCallBack bianSeCallBack) {
        this.bianSeCallBack = bianSeCallBack;
    }

    public void setKey(String key) {
        this.key = key;
        showWeb(key);
    }

    private void showWeb(final String key) {
        if (key == null || key.length() <= 0) {
            Toast.makeText(getActivity(), "请输入语法名词", Toast.LENGTH_LONG).show();
            return;
        }
//        try {
        if (key.equals("PRN")) {
            webview.loadUrl("file:///android_asset/PRN1.html");
        } else {

            webview.loadUrl("file:///android_asset/" + key + ".html");
        }
//        } catch (Exception e) {
//
//            JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());
//            Toast.makeText(getActivity(), "文件为找到", Toast.LENGTH_LONG).show();
//        }

        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }
        });
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }

            //当网页面加载失败时，会调用 这个方法，所以我们在这个方法中处理
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(getContext(), "未找到相关内容", Toast.LENGTH_SHORT).show();
                view.setVisibility(View.GONE);

            }
        });


    }

}
