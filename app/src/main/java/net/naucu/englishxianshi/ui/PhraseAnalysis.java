package net.naucu.englishxianshi.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.app.myTool.ActionTitleBarWidget;
import com.app.myTool.ActionTitleBarWidget.ClickListener;
import com.jiongbull.jlog.JLog;

import net.naucu.englishxianshi.R;
import net.naucu.englishxianshi.fragment.AdvancedGrammarFragment;
import net.naucu.englishxianshi.fragment.AdvancedGrammarFragment2;
import net.naucu.englishxianshi.fragment.GrammarMasaFragment;
import net.naucu.englishxianshi.fragment.TraditionalGrammarFragment1;
import net.naucu.englishxianshi.ui.base.BaseActivity;
import net.naucu.englishxianshi.util.MapString;
import net.naucu.englishxianshi.util.StreamTools;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class PhraseAnalysis extends BaseActivity implements OnClickListener {
    private ActionTitleBarWidget titlebar;// 初始化标题控件
    private TextView tv_yufact, tv_yufajj, tv_yufamasa;
    private String key;// NP PRP等内容
    private String key0;// "result":{"sbar":"d2"} "sbar"
    private String key1;// "result":{"sbar":"d2"} "d2"
    private String duanyu;// 短句子,带【2-3】
    private String shortduanyu;// 短句子，不带【2-3】
    private String sentence;// 完整句子
    private TraditionalGrammarFragment1 traditionalGrammarFragment = null;
    private GrammarMasaFragment grammarMasaFragment = null;
    private FragmentManager fm = getSupportFragmentManager();
    private FragmentTransaction fmt = null;
    private TextView tv;
    private JSONObject jsonObject;
    private Boolean checkmasa, checkct, checkjj;
    private AdvancedGrammarFragment advancedGrammarFragment;
    public Intent i;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_yufajiexi);
        tv = (TextView) findViewById(R.id.tv_cixing);
        checkmasa = false;
        checkct = true;
        checkjj = false;
        initView();
        initevent();
        i = getIntent();
        key = i.getStringExtra("key");
        duanyu = i.getStringExtra("duanyu");
        shortduanyu = duanyu.replaceAll("\\[\\d+-\\d+\\]", "").replaceAll("\\[\\d+\\]", "");
        sentence = i.getStringExtra("sentence");
        if (key != null) {
            getData(sentence, duanyu, key);
        } else {
            tv.setText(Html.fromHtml("已准备为你讲解  " + "<a href=\"bar\">" + MapString.map.get(duanyu) + "</a>" + " 的用法及辨析"));
        }

        grammarMasaFragment = new GrammarMasaFragment(bianSeCallBack);
        advancedGrammarFragment = new AdvancedGrammarFragment(bianSeCallBack);
        traditionalGrammarFragment = new TraditionalGrammarFragment1(bianSeCallBack, duanyu);

        showFragment(grammarMasaFragment, grammarMasaFragment.getTag());
        showFragment(advancedGrammarFragment, advancedGrammarFragment.getTag());
        showFragment(traditionalGrammarFragment, traditionalGrammarFragment.getTag());

    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    String s = (String) msg.obj;// {"msg":"200","result":{"sbar":"d2"},"success":true}
                    jsonObject = JSON.parseObject(s);
                    Boolean flag = false;
                    String xswb;
                    if (jsonObject.get("msg").equals("200")) {
                        jsonObject = jsonObject.getJSONObject("result");
                        Set<String> set = jsonObject.keySet();
                        if (jsonObject.toString().equals("{}")) {
                            if ((String) MapString.map.get(key) == null) {
                                xswb = "暂时未有相关内容";
                                traditionalGrammarFragment.goneWebView();
                            } else {
                                xswb = "<p style=\"text-align:center\">" + shortduanyu + "</p><span>在本句中作为" + "<a href=\"" + key
                                        + "\">" + (String) MapString.map.get(key) + "</a> " + ",已准备好为你讲解相关语法知识</span>";
                                tv.setText(Html.fromHtml(xswb));

                                tv_yufact.setTextColor(0xff1BE2E9);
                                String needKey = "";
                                if (key.equals("PRN")) {
                                    needKey = "PRN1";
                                } else {
                                    needKey = key;
                                }
                                showFragment(traditionalGrammarFragment, traditionalGrammarFragment.getTag());
                                traditionalGrammarFragment.setWeb(needKey);
                            }
                        } else if (set.size() > 0) {
                            Iterator<String> it = set.iterator();
                            while (it.hasNext()) {
                                key0 = it.next();// 左侧数据
                            }
                            key1 = jsonObject.getString(key0);// 右侧数据
                            ArrayList<String> alist = new ArrayList<>();
                            String sss[];
                            try {
                                sss = getAssets().list("");
                                for (int i = 0; i < sss.length; i++) {
                                    alist.add(sss[i]);
                                }
                            } catch (Exception e) {
                                JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());
                            }
                            if (alist.contains(key1 + ".html")) {
                                if ((String) MapString.map.get(key) == null) {
                                    flag = true;
                                    xswb = "<p style=\"text-align:center\">" + shortduanyu + "</p><span>在本句中" + "</a>作为 <a href=\"" + key1 + "\">"
                                            + MapString.map.get(key1) + "</a>" + ",已准备好为你讲解相关语法知识</span>";
                                } else {
                                    flag = true;
                                    xswb = "<p style=\"text-align:center\">" + shortduanyu + "</p><span>在本句中" + "<a href=\"" + key
                                            + "\">" + (String) MapString.map.get(key) + "</a> 作为 <a href=\"" + key1 + "\">"
                                            + MapString.map.get(key1) + "</a>" + ",已准备好为你讲解相关语法知识</span>";
                                }
                            } else {
                                flag = false;
                                xswb = "<p style=\"text-align:center\">" + shortduanyu + "</p><span>在本句中作为" + "<a href=\""
                                        + key + "\">" + (String) MapString.map.get(key) + "</a>,已准备好为你讲解相关语法知识</span>";
                            }

                            tv.setText(Html.fromHtml(xswb));

                            tv.setMovementMethod(LinkMovementMethod.getInstance());
                            CharSequence text = tv.getText();
                            if (text instanceof Spannable) {
                                int end = text.length();
                                Spannable sp = (Spannable) tv.getText();
                                URLSpan[] urls = sp.getSpans(0, end, URLSpan.class);
                                SpannableStringBuilder style = new SpannableStringBuilder(text);
                                style.clearSpans();// should clear old spans
                                for (URLSpan url : urls) {
                                    MyURLSpan myURLSpan = new MyURLSpan(url.getURL());
                                    style.setSpan(myURLSpan, sp.getSpanStart(url), sp.getSpanEnd(url),
                                            Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                                }
                                tv.setText(style);
                            }

                            tv_yufact.setTextColor(0xff1BE2E9);
                            String needKey = "";
                            if (flag) {
                                if (key.equals("PRN")) {
                                    needKey = "PRN1";
                                } else {
                                    needKey = key;
                                }
                            } else {
                                if (key1.equals("PRN")) {
                                    needKey = "PRN1";
                                } else {
                                    needKey = key1;
                                }
                            }
                            showFragment(traditionalGrammarFragment, traditionalGrammarFragment.getTag());
                            traditionalGrammarFragment.setWeb(needKey);
                        } else {
                            xswb = "暂时未有相关内容";
                            traditionalGrammarFragment.goneWebView();
                        }

                    }

                    break;

                default:
                    break;
            }
            return false;
        }
    });

    private class MyURLSpan extends ClickableSpan {
        private String mUrl;

        MyURLSpan(String url) {
            mUrl = url;
        }

        @Override
        public void onClick(View widget) {
            if (checkmasa) {
                tv_yufamasa.setTextColor(0xff1BE2E9);
                tv_yufact.setTextColor(0xffffffff);
                tv_yufajj.setTextColor(0xffffffff);
                showFragment(grammarMasaFragment, grammarMasaFragment.getTag());
            } else if (checkct) {
                tv_yufamasa.setTextColor(0xffffffff);
                tv_yufact.setTextColor(0xff1BE2E9);
                tv_yufajj.setTextColor(0xffffffff);
                showFragment(traditionalGrammarFragment, traditionalGrammarFragment.getTag());
                traditionalGrammarFragment.setWeb(mUrl);
            } else {
                tv_yufamasa.setTextColor(0xffffffff);
                tv_yufact.setTextColor(0xffffffff);
                tv_yufajj.setTextColor(0xff1BE2E9);
                while (fm.getBackStackEntryCount() > 0) {
                    fm.popBackStackImmediate();
                }
                AdvancedGrammarFragment2 advancedGrammarFragment = null;
                advancedGrammarFragment = AdvancedGrammarFragment2.getInstance(mUrl);
                fmt = fm.beginTransaction();
                fmt.replace(R.id.yufact, advancedGrammarFragment);
                fmt.commit();
            }

        }
    }

    URL surl;

    public void getData(String sentence, String duanyu, String key) {
        try {
            String sendSentence = sentence;
            sendSentence = URLEncoder.encode(sendSentence, "UTF-8");
            String sendDuanyu = duanyu;
            sendDuanyu = URLEncoder.encode(sendDuanyu, "UTF-8");
//            String phone = SharedPreTool.getSharedPreDateString(this, "loginPhonenumber", null);
//            if (phone == null) {
//                tv.setText("暂时未有相关内容");
//                return;
//            }
            String url = "http://101.201.212.118/parser/getRelation?words=" + sendSentence + "&phrase=" + sendDuanyu + "&type="
                    + key + "&phone=15000000000" + "&sign=1111" + "&source=abndero";

            surl = new URL(url);
        } catch (Exception e) {
            JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());
        }
        new Thread() {
            public void run() {
                HttpURLConnection con;
                try {
                    if (surl != null) {
                        con = (HttpURLConnection) surl.openConnection();
                        con.setReadTimeout(5000);
                        con.setRequestMethod("GET");
                        if (con.getResponseCode() == 200) {
                            InputStream is = con.getInputStream();
                            String s = StreamTools.streamToString(is);
                            handler.obtainMessage(1, s).sendToTarget();
                        }
                    }
                } catch (IOException e) {
                    JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());
                }
            }

        }.start();
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
        // titlebar.setCenterGravity(Gravity.CENTER);
        // titlebar.setCenterIco(R.drawable.collection);
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
                tv_yufamasa.setText("MASA语法");
                tv_yufajj.setText("语法进阶");
                tv_yufamasa.setTextColor(0xff1BE2E9);
                tv_yufact.setTextColor(0xffffffff);
                tv_yufajj.setTextColor(0xffffffff);
                checkmasa = true;
                checkct = false;
                checkjj = false;
                if (grammarMasaFragment.isWeb()) {
                    grammarMasaFragment.setBack();
                }
                showFragment(grammarMasaFragment, grammarMasaFragment.getTag());
                break;
            case R.id.tv_yufact:
                tv_yufamasa.setText("MASA语法");
                tv_yufajj.setText("语法进阶");
                tv_yufamasa.setTextColor(0xffffffff);
                tv_yufact.setTextColor(0xff1BE2E9);
                tv_yufajj.setTextColor(0xffffffff);
                checkmasa = false;
                checkct = true;
                checkjj = false;
                showFragment(traditionalGrammarFragment, traditionalGrammarFragment.getTag());
                break;
            case R.id.tv_yufajj:
                tv_yufajj.setText("语法进阶");
                tv_yufamasa.setText("MASA语法");
                tv_yufamasa.setTextColor(0xffffffff);
                tv_yufact.setTextColor(0xffffffff);
                tv_yufajj.setTextColor(0xff1BE2E9);
                checkmasa = false;
                checkct = false;
                checkjj = true;
                if (advancedGrammarFragment.isWeb()) {
                    advancedGrammarFragment.setBack();
                }
                if (key != null) {
                    if (MapString.map.get(key) != null) {
                        showFragment(advancedGrammarFragment, advancedGrammarFragment.getTag());
                        advancedGrammarFragment.setKey(MapString.map.get(key));
                    }
                } else {
                    if (duanyu != null) {
                        if (MapString.map.get(duanyu) != null) {
                            showFragment(advancedGrammarFragment, advancedGrammarFragment.getTag());
                            advancedGrammarFragment.setKey(MapString.map.get(duanyu));
                        }
                    }
                }
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
            ShareSDK.initSDK(PhraseAnalysis.this);
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
            oks.show(PhraseAnalysis.this);
        }

        @Override
        public void onleft(View v) {
            finish();
        }

        @Override
        public void oncenter(View arg0) {// 收藏

        }
    };

    public Fragment nowFragment;

    public void showFragment(Fragment toFragment, String tag) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        boolean isadd = toFragment.isAdded();
        if (nowFragment != null) {
            transaction.hide(nowFragment);
        }
        if (!isadd) {
            transaction.add(R.id.yufact, toFragment);
        } else {
            transaction.show(toFragment);
        }
        transaction.commitAllowingStateLoss();
        nowFragment = toFragment;
    }

    MasaGrammarActivity.BianSeCallBack bianSeCallBack = new MasaGrammarActivity.BianSeCallBack() {
        @Override
        public void callback(String name) {
            if (name.equals(AdvancedGrammarFragment.class.getSimpleName())) {
                tv_yufajj.setText("〈 语法进阶");
            }
            if (name.equals(GrammarMasaFragment.class.getSimpleName())) {
                tv_yufamasa.setText("〈 MASA语法");
            }
        }
    };
}
