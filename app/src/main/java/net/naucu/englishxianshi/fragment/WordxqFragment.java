package net.naucu.englishxianshi.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Toast;

import net.naucu.englishxianshi.R;

public class WordxqFragment extends Fragment {
    public static final String TAG = WordxqFragment.class.getSimpleName();
    private String result;
    private View view;
    private WebView webview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_jjxq, container, false);
        }
        result = getArguments().getString("result");
        String style = "<style type=\"text/css\"> p {color: white;word-wrap:break-word;}</style>";
        result = result + style;
        webview = (WebView) view.findViewById(R.id.web_jjxq);
        webview.setBackgroundColor(0);
        webview.setOverScrollMode(View.OVER_SCROLL_NEVER);
        webview.getSettings().setDefaultFontSize(18);
        try {
            webview.loadDataWithBaseURL("", result, "text/html", "UTF-8", "");
        } catch (Exception e) {
            Toast.makeText(getActivity(), "文件未找到", Toast.LENGTH_LONG).show();
        }
        return view;
    }

    public static WordxqFragment getInstance(String result) {
        WordxqFragment f = new WordxqFragment();
        Bundle args = new Bundle();
        args.putString("result", result);
        f.setArguments(args);
        return f;
    }
}
