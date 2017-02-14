package net.naucu.englishxianshi.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Toast;

import com.jiongbull.jlog.JLog;

import net.naucu.englishxianshi.R;

public class GrammarMasaxqFragment extends Fragment {
    public static final String TAG = GrammarMasaxqFragment.class.getSimpleName();
    private View view;
    private String key;
    private WebView webview;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_yufamasaxq, container, false);
        key = (String) getArguments().get("key");
        webview = (WebView) view.findViewById(R.id.web_masaxqyufa);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setBackgroundColor(0);
        webview.setOverScrollMode(View.OVER_SCROLL_NEVER);
        webview.getSettings().setDefaultFontSize(18);

        showWeb(key);
        return view;
    }

    public static GrammarMasaxqFragment getInstance(String key) {
        GrammarMasaxqFragment f = new GrammarMasaxqFragment();
        Bundle args = new Bundle();
        args.putString("key", key);
        f.setArguments(args);

        return f;
    }

    public void setKey(String key) {
        this.key = key;
        showWeb(key);
    }

    private void showWeb(String key) {
        try {
            if (webview == null) {
                webview = (WebView) view.findViewById(R.id.web_masaxqyufa);
                webview.getSettings().setJavaScriptEnabled(true);
                webview.setBackgroundColor(0);
                webview.setOverScrollMode(View.OVER_SCROLL_NEVER);
                webview.getSettings().setDefaultFontSize(18);
            }
            webview.loadUrl("file:///android_asset/" + key + ".html");
        } catch (Exception e) {
            JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());
            Toast.makeText(getActivity(), "文件为找到", Toast.LENGTH_LONG).show();
        }
    }


}
