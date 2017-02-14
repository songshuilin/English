package net.naucu.englishxianshi.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Toast;

import com.jiongbull.jlog.JLog;

import net.naucu.englishxianshi.R;

public class AdvancedGrammarxqFragment extends Fragment {
    public static final String TAG = AdvancedGrammarxqFragment.class.getSimpleName();
    private Context mContext;

    private String result;
    private View view;
    private WebView webview;

    @Override
    public void onAttach(Context context) {
        this.mContext = context;
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_jjxq, container, false);

        result = getArguments().getString("result");
        String style = "<style type=\"text/css\"> p {color: white;word-wrap:break-word;}</style>";
        result = result + style;
        webview = (WebView) view.findViewById(R.id.web_jjxq);
        webview.setBackgroundColor(0);
        webview.setOverScrollMode(View.OVER_SCROLL_NEVER);
        webview.getSettings().setDefaultFontSize(18);

        showWeb(result);
        return view;
    }

    public static AdvancedGrammarxqFragment getInstance(String result) {
        AdvancedGrammarxqFragment f = new AdvancedGrammarxqFragment();
        Bundle args = new Bundle();
        args.putString("result", result);
        f.setArguments(args);
        return f;
    }

    public void setKey(String key) {
        this.result = key;
        showWeb(key);
    }

    private void showWeb(String key) {
        try {
            if (webview == null) {
                webview = (WebView) view.findViewById(R.id.web_jjxq);
                webview.setBackgroundColor(0);
                webview.setOverScrollMode(View.OVER_SCROLL_NEVER);
                webview.getSettings().setDefaultFontSize(18);
            }
            webview.loadDataWithBaseURL("", result, "text/html", "UTF-8", "");
        } catch (Exception e) {
            JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());
            Toast.makeText(mContext, "文件未找到", Toast.LENGTH_LONG).show();
        }
    }

}
