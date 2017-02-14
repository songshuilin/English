package net.naucu.englishxianshi.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.app.myTool.ActionTitleBarWidget;
import com.app.myTool.ActionTitleBarWidget.ClickListener;

import net.naucu.englishxianshi.R;
import net.naucu.englishxianshi.fragment.AdvancedGrammarFragment;
import net.naucu.englishxianshi.fragment.GrammarMasaFragment;
import net.naucu.englishxianshi.fragment.TraditionalGrammarFragment1;
import net.naucu.englishxianshi.fragment.WordFragment;
import net.naucu.englishxianshi.ui.base.BaseActivity;

public class MasaGrammarActivity extends BaseActivity implements OnClickListener {

    private String text;

    private ActionTitleBarWidget titlebar;
    private EditText edInputword;

    // MASA语法
    private TextView tvMasaYufa;
    private GrammarMasaFragment grammarMasaFragment;
    // 传统语法
    private TextView tvChuantongYufa;
    private TraditionalGrammarFragment1 traditionalGrammarFragment;
    // 语法进阶
    private TextView tvYufaJinjie;
    private AdvancedGrammarFragment advancedGrammarFragment;
    // 单词辨析
    private TextView tvDanciBianxi;
    private WordFragment wordFragment;

    //当前显示fragment
    private Fragment nowFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_masa_grammar);

        // 初始化View
        initView();
    }

    private void initView() {
        titlebar = (ActionTitleBarWidget) findViewById(R.id.titlebar);
        titlebar.setTitleBackgroundResource(R.drawable.menubutton_select, R.drawable.menubutton_select);
        titlebar.setLeftGravity(Gravity.CENTER);
        titlebar.setLeftIco(R.drawable.backs);
        titlebar.setCenterText(getString(R.string.tx_masayufa), 17, Color.WHITE);
        titlebar.setCenterGravity(Gravity.CENTER);

        tvMasaYufa = (TextView) findViewById(R.id.tv_masa_yufa);
        tvMasaYufa.setOnClickListener(this);
        tvChuantongYufa = (TextView) findViewById(R.id.tv_chuantong_yufa);
        tvChuantongYufa.setOnClickListener(this);
        tvYufaJinjie = (TextView) findViewById(R.id.tv_yufa_jinjie);
        tvYufaJinjie.setOnClickListener(this);
        tvDanciBianxi = (TextView) findViewById(R.id.tv_danci_bianxi);
        tvDanciBianxi.setOnClickListener(this);
        tvMasaYufa.setTextColor(0xff1BE2E9);
        tvDanciBianxi.setTextColor(0xffffffff);
        tvYufaJinjie.setTextColor(0xffffffff);
        tvChuantongYufa.setTextColor(0xffffffff);
        titlebar.OnTitleBarClickListener(new ClickListener() {

            @Override
            public void onright(View paramView) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onleft(View paramView) {
                finish();
            }

            @Override
            public void oncenter(View paramView) {
                // TODO Auto-generated method stub

            }
        });

        edInputword = (EditText) findViewById(R.id.ed_inputword);


        grammarMasaFragment = new GrammarMasaFragment(bianSeCallBack);
        traditionalGrammarFragment = new TraditionalGrammarFragment1(bianSeCallBack);
        advancedGrammarFragment = new AdvancedGrammarFragment(bianSeCallBack);
        wordFragment = WordFragment.getInstance(edInputword.getText().toString(), this, bianSeCallBack);

        showFragment(advancedGrammarFragment, advancedGrammarFragment.getTag());
        showFragment(wordFragment, wordFragment.getTag());
        showFragment(traditionalGrammarFragment, grammarMasaFragment.getTag());
        showFragment(grammarMasaFragment, grammarMasaFragment.getTag());
    }

    @Override
    public void onClick(View v) {
        tvMasaYufa.setText("MASA语法");
        tvDanciBianxi.setText("单词辩词");
        tvYufaJinjie.setText("语法进阶");
        tvChuantongYufa.setText("传统语法");
        switch (v.getId()) {
            case R.id.tv_masa_yufa:
                tvMasaYufa.setTextColor(0xff1BE2E9);
                tvDanciBianxi.setTextColor(0xffffffff);
                tvYufaJinjie.setTextColor(0xffffffff);
                tvChuantongYufa.setTextColor(0xffffffff);
                if (grammarMasaFragment.isWeb()) {
                    grammarMasaFragment.setBack();

                }
                showFragment(grammarMasaFragment, GrammarMasaFragment.TAG);
                break;
            case R.id.tv_chuantong_yufa:
                tvMasaYufa.setTextColor(0xffffffff);
                tvDanciBianxi.setTextColor(0xffffffff);
                tvYufaJinjie.setTextColor(0xffffffff);
                tvChuantongYufa.setTextColor(0xff1BE2E9);
                if (traditionalGrammarFragment.isWeb()) {
                    traditionalGrammarFragment.setBack();
                }
                showFragment(traditionalGrammarFragment, TraditionalGrammarFragment1.TAG);
                traditionalGrammarFragment.setKey(edInputword.getText().toString());
                break;
            case R.id.tv_yufa_jinjie:
                tvMasaYufa.setTextColor(0xffffffff);
                tvDanciBianxi.setTextColor(0xffffffff);
                tvYufaJinjie.setTextColor(0xff1BE2E9);
                tvChuantongYufa.setTextColor(0xffffffff);
                if (advancedGrammarFragment.isWeb()) {
                    advancedGrammarFragment.setBack();
                }
                showFragment(advancedGrammarFragment, AdvancedGrammarFragment.TAG);
                advancedGrammarFragment.setKey(edInputword.getText().toString());
                break;
            case R.id.tv_danci_bianxi:
                tvMasaYufa.setTextColor(0xffffffff);
                tvDanciBianxi.setTextColor(0xff1BE2E9);
                tvYufaJinjie.setTextColor(0xffffffff);
                tvChuantongYufa.setTextColor(0xffffffff);
                if (wordFragment.isWeb()) {
                    wordFragment.setBack();
                }
                showFragment(wordFragment, WordFragment.TAG);
                wordFragment.analyse(edInputword.getText().toString());
                break;
        }
    }

    @Override
    protected void onResume() {
        showFragment(grammarMasaFragment, GrammarMasaFragment.TAG);
        super.onResume();
    }

    public void showFragment(Fragment toFragment, String tag) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        boolean isadd = toFragment.isAdded();
        if (nowFragment != null) {
            transaction.hide(nowFragment);
        }
        if (!isadd) {
            transaction.add(R.id.ll_fragment, toFragment);
        } else {
            transaction.show(toFragment);
        }
        transaction.commit();
        nowFragment = toFragment;
    }

    public interface BianSeCallBack {
        void callback(String name);
    }

    /**
     * // MASA语法
     * private TextView tvMasaYufa;
     * private GrammarMasaFragment grammarMasaFragment;
     * // 传统语法
     * private TextView tvChuantongYufa;
     * private TraditionalGrammarFragment1 traditionalGrammarFragment;
     * // 语法进阶
     * private TextView tvYufaJinjie;
     * private AdvancedGrammarFragment advancedGrammarFragment;
     * // 单词辨析
     * private TextView tvDanciBianxi;
     * private WordFragment wordFragment;
     */


    BianSeCallBack bianSeCallBack = new BianSeCallBack() {
        @Override
        public void callback(String name) {
            if (name.equals(GrammarMasaFragment.class.getSimpleName())) {
                tvMasaYufa.setText("〈 MASA语法");
                tvDanciBianxi.setText("单词辩词");
                tvYufaJinjie.setText("语法进阶");
                tvChuantongYufa.setText("传统语法");
            }
            if (name.equals(TraditionalGrammarFragment1.class.getSimpleName())) {
                tvMasaYufa.setText("MASA语法");
                tvDanciBianxi.setText("单词辩词");
                tvYufaJinjie.setText("语法进阶");
                tvChuantongYufa.setText("〈 传统语法");
            }
            if (name.equals(AdvancedGrammarFragment.class.getSimpleName())) {
                tvMasaYufa.setText("MASA语法");
                tvDanciBianxi.setText("单词辩词");
                tvYufaJinjie.setText("〈 语法进阶");
                tvChuantongYufa.setText("传统语法");
            }
            if (name.equals(WordFragment.class.getSimpleName())) {
                tvMasaYufa.setText("MASA语法");
                tvDanciBianxi.setText("〈 单词辩词");
                tvYufaJinjie.setText("语法进阶");
                tvChuantongYufa.setText("传统语法");
            }
        }
    };
}
