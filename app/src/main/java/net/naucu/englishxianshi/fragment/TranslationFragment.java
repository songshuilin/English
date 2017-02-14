package net.naucu.englishxianshi.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.naucu.englishxianshi.R;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * 类名: TranslationFragment.java
 * 描述: 翻译
 * 作者: youyou_pc
 * 时间: 2015年12月29日  下午5:27:15
 */
public class TranslationFragment extends Fragment {
    private View view;
    @ViewInject(R.id.tv_result)
    private TextView _result;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_translation, container, false);
        x.view().inject(this, view);
        return view;
    }

    public void setResult(String result) {
        _result.setText(result);
    }
}
