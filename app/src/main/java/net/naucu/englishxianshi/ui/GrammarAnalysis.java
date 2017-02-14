package net.naucu.englishxianshi.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import com.app.myTool.ActionTitleBarWidget;
import com.app.myTool.ActionTitleBarWidget.ClickListener;

import net.naucu.englishxianshi.R;
import net.naucu.englishxianshi.fragment.AdvancedGrammarFragment2;
import net.naucu.englishxianshi.fragment.GrammarMasaFragment;
import net.naucu.englishxianshi.fragment.TraditionalGrammarFragment;
import net.naucu.englishxianshi.ui.base.BaseActivity;
import net.naucu.englishxianshi.util.MapString;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class GrammarAnalysis extends BaseActivity implements OnClickListener {
    public static final String TAG = GrammarAnalysis.class.getSimpleName();

    private ActionTitleBarWidget titlebar;// 初始化标题控件
    private TextView tv_yufact, tv_yufajj, tv_yufamasa, tv;
    private String key;
    private TraditionalGrammarFragment traditionalGrammarFragment = null;
    private GrammarMasaFragment grammarMasaFragment = null;
    private FragmentManager fm = getSupportFragmentManager();
    private FragmentTransaction fmt = null;
    private String describe;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_yufajiexi);
        tv = (TextView) findViewById(R.id.tv_cixing);
        initView();
        initevent();

        Intent i = getIntent();
        key = i.getStringExtra("key");
        String jushi = MapString.map.get(key);
        if (jushi != null) {

            describe = "已准备为你讲解  " + "<a href=\"bar\">" + jushi + "</a>" + " 的用法及辨析";
            System.out.println(describe);
            tv.setText(Html.fromHtml(describe));

            tv.setMovementMethod(LinkMovementMethod.getInstance());
            CharSequence text = tv.getText();
            if (text instanceof Spannable) {
                int end = text.length();
                Spannable sp = (Spannable) tv.getText();
                URLSpan[] urls = sp.getSpans(0, end, URLSpan.class);
                SpannableStringBuilder style = new SpannableStringBuilder(text);
                style.clearSpans();
                for (URLSpan url : urls) {
                    MyURLSpan myURLSpan = new MyURLSpan(url.getURL());
                    style.setSpan(myURLSpan, sp.getSpanStart(url), sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                }
                tv.setText(style);
            }

            tv_yufact.setTextColor(0xff1BE2E9);
            traditionalGrammarFragment = new TraditionalGrammarFragment(null);
            Bundle b = new Bundle();
            b.putString("key", key);
            traditionalGrammarFragment.setArguments(b);
            fmt = fm.beginTransaction();
            fmt.replace(R.id.yufact, traditionalGrammarFragment);
            fmt.commit();
        } else {
            describe = "暂时未有相关内容";
            tv.setText(describe);
        }
    }

    private class MyURLSpan extends ClickableSpan {
        private String mUrl;

        MyURLSpan(String url) {
            mUrl = url;
        }

        @Override
        public void onClick(View widget) {
            System.out.println(mUrl);
        }
    }

    private void initView() {
        tv_yufact = (TextView) findViewById(R.id.tv_yufact);
        tv_yufajj = (TextView) findViewById(R.id.tv_yufajj);
        tv_yufamasa = (TextView) findViewById(R.id.tv_yufamasa);

        titlebar = (ActionTitleBarWidget) findViewById(R.id.titlebar);
        titlebar.setTitleBackgroundResource(R.drawable.menubutton_select, R.drawable.menubutton_select);
        titlebar.setLeftGravity(Gravity.CENTER);
        titlebar.setLeftIco(R.drawable.backs);
        titlebar.setRightGravity(Gravity.CENTER);
        titlebar.setRightIco(R.drawable.share);
//		titlebar.setCenterGravity(Gravity.CENTER);
//		titlebar.setCenterIco(R.drawable.collection);
    }

    private void initevent() {
        titlebar.OnTitleBarClickListener(clickListener);
        tv_yufact.setOnClickListener(this);
        tv_yufajj.setOnClickListener(this);
        tv_yufamasa.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_yufamasa:
                tv_yufamasa.setTextColor(0xff1BE2E9);
                tv_yufact.setTextColor(0xffffffff);
                tv_yufajj.setTextColor(0xffffffff);
                while (fm.getBackStackEntryCount() > 0) {
                    fm.popBackStackImmediate();
                }
                grammarMasaFragment = new GrammarMasaFragment(null);
                fmt = fm.beginTransaction();
                fmt.replace(R.id.yufact, grammarMasaFragment);
                fmt.commit();
                break;
            case R.id.tv_yufact:
                tv_yufamasa.setTextColor(0xffffffff);
                tv_yufact.setTextColor(0xff1BE2E9);
                tv_yufajj.setTextColor(0xffffffff);
                while (fm.getBackStackEntryCount() > 0) {
                    fm.popBackStackImmediate();
                }
                traditionalGrammarFragment = TraditionalGrammarFragment.getInstance(key, this);
                fmt = fm.beginTransaction();
                fmt.replace(R.id.yufact, traditionalGrammarFragment);
                fmt.commit();
                break;
            case R.id.tv_yufajj:
                tv_yufamasa.setTextColor(0xffffffff);
                tv_yufact.setTextColor(0xffffffff);
                tv_yufajj.setTextColor(0xff1BE2E9);
                while (fm.getBackStackEntryCount() > 0) {
                    fm.popBackStackImmediate();
                }
                AdvancedGrammarFragment2 yfjj = AdvancedGrammarFragment2.getInstance(key);
                fmt = fm.beginTransaction();
                fmt.replace(R.id.yufact, yfjj);
                fmt.commit();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private ClickListener clickListener = new ClickListener() {

        @Override
        public void onright(View v) {
            ShareSDK.initSDK(GrammarAnalysis.this);
            OnekeyShare oks = new OnekeyShare();
            //关闭sso授权
            oks.disableSSOWhenAuthorize();

            // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
            oks.setTitle("标题");
            // titleUrl是标题的网络链接，QQ和QQ空间等使用
            oks.setTitleUrl("http://sharesdk.cn");
            // text是分享文本，所有平台都需要这个字段
            oks.setText("我是分享文本");
            // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
            //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
            // url仅在微信（包括好友和朋友圈）中使用
            oks.setUrl("http://sharesdk.cn");
            // comment是我对这条分享的评论，仅在人人网和QQ空间使用
            oks.setComment("我是测试评论文本");
            // site是分享此内容的网站名称，仅在QQ空间使用
            oks.setSite(getString(R.string.app_name));
            // siteUrl是分享此内容的网站地址，仅在QQ空间使用
            oks.setSiteUrl("http://sharesdk.cn");

            // 启动分享GUI
            oks.show(GrammarAnalysis.this);
        }

        @Override
        public void onleft(View v) {
            finish();
        }

        @Override
        public void oncenter(View arg0) {// 收藏

        }
    };

}
