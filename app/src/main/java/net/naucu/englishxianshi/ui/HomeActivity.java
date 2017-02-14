package net.naucu.englishxianshi.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jiongbull.jlog.JLog;
import com.nineoldandroids.view.ViewHelper;

import net.naucu.englishxianshi.BaseApplication;
import net.naucu.englishxianshi.R;
import net.naucu.englishxianshi.db.DataBaseHelper;
import net.naucu.englishxianshi.ui.DictionaryTranslationActivity.TranslateType;
import net.naucu.englishxianshi.ui.base.BaseActivity;
import net.naucu.englishxianshi.util.NetTool;
import net.naucu.englishxianshi.util.SharedPreTool;
import net.naucu.englishxianshi.widget.dialog.PromptDialog;
import net.naucu.englishxianshi.widget.dialog.PromptDialog.onPromptClickListener;
import net.naucu.englishxianshi.widget.view.FragmentView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.IOException;

import static net.naucu.englishxianshi.R.drawable.home_bg;


/**
 * 类名: HomeActivity.java 描述: 首页 作者: youyou_pc 时间: 2015年11月16日 上午10:00:19
 */

public class HomeActivity extends BaseActivity implements OnClickListener {
    public static HomeActivity activity;
    public static DrawerLayout slidingMenu;// 侧滑菜单
    private RadioButton showBooks;// 电子书
    private RadioButton showMovie;// 电影
    private static RelativeLayout rl_leftlayout;
    private LinearLayout fl_userheadview;// 侧滑头像布局View
    private Button rb_DownloadManager;// 下载管理
    private Button rb_MoreSettings;// 更多设置
    // private Button rb_Localdownload;// 本地下载
    private Button rb_Myrecording;// 我的录音
    private LinearLayout rb_masayufa;
    private Button rb_Learningnotes;// 学习笔记
    private Button rb_Mybookshelf;// 我的书架
    private Button rb_MyYingKu;// 我的影库
    private Button rb_Readandreread;// 朗读与复读
    private Button rb_Electronicdictionary;// 电子词典
    private Button rb_Grammartranslation;// 语法翻译
    private Button br_translation;// 翻译
    private Button bt_softwarepay;// 支付
    public static ImageView im_user_head;// 头像
    public static TextView user_Nickname;
    private BaseApplication application;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home);
        textView = (TextView) findViewById(R.id.huadong_yinying);

        initView();
        initEvent();
        DataBaseHelper dbh = new DataBaseHelper(this);
        try {
            dbh.createDataBase();
        } catch (IOException e) {
            JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());
        }
        initData();

        HomeMovieActivity.needShow = true;
    }

    /**
     * 初始化View
     */
    private void initView() {
        slidingMenu = (DrawerLayout) findViewById(R.id.slidingMenu);
        slidingMenu.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, GravityCompat.END);
        showBooks = (RadioButton) findViewById(R.id.showBooks);
        showMovie = (RadioButton) findViewById(R.id.showMovie);
        rl_leftlayout = (RelativeLayout) findViewById(R.id.rl_leftlayout);
        fl_userheadview = (LinearLayout) findViewById(R.id.fl_userheadview);
        rb_DownloadManager = (Button) findViewById(R.id.rb_DownloadManager);
        rb_MoreSettings = (Button) findViewById(R.id.rb_MoreSettings);
        rb_Myrecording = (Button) findViewById(R.id.rb_Myrecording);
        rb_masayufa = (LinearLayout) findViewById(R.id.rb_masayufa);
        rb_Learningnotes = (Button) findViewById(R.id.rb_Learningnotes);
        rb_Mybookshelf = (Button) findViewById(R.id.rb_Mybookshelf);
        rb_MyYingKu = (Button) findViewById(R.id.rb_MyYingKu);
        rb_Readandreread = (Button) findViewById(R.id.rb_Readandreread);
        rb_Electronicdictionary = (Button) findViewById(R.id.rb_Electronicdictionary);
        rb_Grammartranslation = (Button) findViewById(R.id.rb_Grammartranslation);
        br_translation = (Button) findViewById(R.id.br_translation);
        im_user_head = (ImageView) findViewById(R.id.im_user_head);
        user_Nickname = (TextView) findViewById(R.id.user_Nickname);
        bt_softwarepay = (Button) findViewById(R.id.bt_softwarepay);
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        slidingMenu.addDrawerListener(drawerListener);
        showBooks.setOnClickListener(this);
        showMovie.setOnClickListener(this);
        fl_userheadview.setOnClickListener(this);
        rb_DownloadManager.setOnClickListener(this);
        rb_MoreSettings.setOnClickListener(this);
        rb_Myrecording.setOnClickListener(this);
        rb_masayufa.setOnClickListener(this);
        rb_Learningnotes.setOnClickListener(this);
        rb_Mybookshelf.setOnClickListener(this);
        rb_MyYingKu.setOnClickListener(this);
        rb_Readandreread.setOnClickListener(this);
        rb_Electronicdictionary.setOnClickListener(this);
        rb_Grammartranslation.setOnClickListener(this);
        br_translation.setOnClickListener(this);
        bt_softwarepay.setOnClickListener(this);
    }

    /**
     * 侧滑菜单
     */
    private DrawerListener drawerListener = new DrawerListener() {

        @Override
        public void onDrawerStateChanged(int newState) {

        }

        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {
            if (slideOffset > 0) {
                textView.setVisibility(View.VISIBLE);
                int v = (int) (255 * slideOffset);
                String s = "";
                if (v < 0x85) {
                    if (v < 0x10) {
                        s = Integer.toHexString(v);
                        Log.i("sdggdgds", "#0" + s + "000000");
                        textView.setBackgroundColor(Color.parseColor("#0" + s + "000000"));
                    } else {
                        s = Integer.toHexString(v);
                        Log.i("sdggdgds", "#" + s + "000000");
                        textView.setBackgroundColor(Color.parseColor("#" + s + "000000"));
                    }
                }
            } else {
                textView.setVisibility(View.GONE);
            }
            View mContent = slidingMenu.getChildAt(0);
            View mMenu = drawerView;
            float scale = 1 - slideOffset;
            if (drawerView.getTag().equals("LEFT")) {
                ViewHelper.setAlpha(mMenu, 0.6f + 0.4f * (1 - scale));
                ViewHelper.setTranslationX(mContent, mMenu.getMeasuredWidth() * (1 - scale));
                mContent.invalidate();
            }
        }

        @Override
        public void onDrawerOpened(View drawerView) {
        }

        @Override
        public void onDrawerClosed(View drawerView) {
            slidingMenu.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, GravityCompat.END);
        }
    };

    /**
     * 初始化数据
     */

    private void initData() {
        application = (BaseApplication) getApplication();
        slidingMenu.setScrimColor(0x00000000);
        showMovie.setChecked(true);
        islayout(1);
        if (application.getLoginApplication() != null) {
            if (!TextUtils.isEmpty(application.getLoginApplication().getTelephone())) {
                /**
                 * 随机生成小贴士
                 *
                 * */
                RequestParams params = new RequestParams("https://naucu.com:8443/englishStudy/randomTips");
                x.http().get(params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String s) {
                        try {
                            JSONObject object = new JSONObject(s);
                            String msg = object.getString("msg");
                            user_Nickname.setText("小贴士：" + msg);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable throwable, boolean b) {

                    }

                    @Override
                    public void onCancelled(CancelledException e) {

                    }

                    @Override
                    public void onFinished() {

                    }
                });
            } else {
                user_Nickname.setText(getString(R.string.noNickname));
            }
            if (application.getLoginApplication().getPicUrl() != null) {
                x.image().bind(im_user_head, application.getLoginApplication().getPicUrl(),
                        BaseApplication.getImageOption());
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    /**
     * 点击事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.showMovie:
                if (NetTool.isNetworkConnected(this)) {
                    if (application.getLoginApplication() == null) {
                        startActivity(new Intent(this, LoginActivity.class));
                    } else {
                        islayout(1);
                    }
                } else {
                    islayout(1);
                    Toast.makeText(this, "网络异常,请检查网络连接状态", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.showBooks:
                if (NetTool.isNetworkConnected(this)) {
                    if (application.getLoginApplication() == null) {
                        startActivity(new Intent(this, LoginActivity.class));
                    } else {
                        islayout(2);
                    }
                } else {
                    islayout(2);
                    Toast.makeText(this, "网络异常,请检查网络连接状态", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.fl_userheadview:
                if (NetTool.isNetworkConnected(this)) {
                    if (application.getLoginApplication() != null) {
                        startActivity(new Intent(this, PersonalCenterActivity.class));
                    } else {
                        startActivity(new Intent(this, LoginActivity.class));
                        finish();
                    }
                } else {
                    Toast.makeText(this, "网络异常,请检查网络连接状态", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.rb_MoreSettings:
                if (NetTool.isNetworkConnected(this)) {
                    if (application.getLoginApplication() != null) {
                        startActivity(new Intent(this, MoreSettingsActivity.class));
                    } else {
                        startActivity(new Intent(this, LoginActivity.class));
                        finish();
                    }
                } else {
                    Toast.makeText(this, "网络异常,请检查网络连接状态", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.rb_DownloadManager:
                if (NetTool.isNetworkConnected(this)) {
                    if (application.getLoginApplication() != null) {
                        startActivity(new Intent(this, DownloadActivity.class));
                    } else {
                        startActivity(new Intent(this, LoginActivity.class));
                        finish();
                    }
                } else {
                    Toast.makeText(this, "网络异常,请检查网络连接状态", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.rb_Myrecording:
                if (NetTool.isNetworkConnected(this)) {
                    if (application.getLoginApplication() != null) {
                        startActivity(new Intent(this, MyrecordingActivity.class));
                    } else {
                        startActivity(new Intent(this, LoginActivity.class));
                        finish();
                    }
                } else {
                    Toast.makeText(this, "网络异常,请检查网络连接状态", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.rb_masayufa:
                if (NetTool.isNetworkConnected(this)) {
                    if (application.getLoginApplication() != null) {
                        startActivity(new Intent(this, MasaGrammarActivity.class));
                    } else {
                        startActivity(new Intent(this, LoginActivity.class));
                        finish();
                    }
                } else {
                    Toast.makeText(this, "网络异常,请检查网络连接状态", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.rb_Learningnotes:
                if (NetTool.isNetworkConnected(this)) {
                    if (application.getLoginApplication() != null) {
                        startActivity(new Intent(this, LearningnotesActivity.class));
                    } else {
                        startActivity(new Intent(this, LoginActivity.class));
                        finish();
                    }
                } else {
                    Toast.makeText(this, "网络异常,请检查网络连接状态", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.rb_Mybookshelf:
                startActivity(new Intent(this, MyBooksActivity.class));
                break;
            case R.id.rb_MyYingKu:
                startActivity(new Intent(this, MyMoreActivity.class));
                break;
            case R.id.rb_Readandreread:
                if (NetTool.isNetworkConnected(this)) {
                    if (application.getLoginApplication() != null) {
                        DictionaryTranslationActivity.startActivity(this, TranslateType.sentence, null, 0);
                    } else {
                        startActivity(new Intent(this, LoginActivity.class));
                        finish();
                    }
                } else {
                    Toast.makeText(this, "网络异常,请检查网络连接状态", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.rb_Electronicdictionary:
                /**
                 * 从侧滑直接跳转的话
                 * 电子词典的选词模式直接设置为自动选词
                 * 因此直接从侧滑跳转时电子词典没有提示按钮
                                                               *
                 * */
                SharedPreTool.setSharedPreDateBoolean(HomeActivity.this, "autoInput", true);

                if (NetTool.isNetworkConnected(this)) {
                    if (application.getLoginApplication() != null) {
                        DictionaryTranslationActivity.startActivity(this, TranslateType.word, null, 0);
                    } else {
                        startActivity(new Intent(this, LoginActivity.class));
                        finish();
                    }
                } else {
                    Toast.makeText(this, "网络异常,请检查网络连接状态", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.rb_Grammartranslation:
                if (NetTool.isNetworkConnected(this)) {
                    if (application.getLoginApplication() != null) {
                        DictionaryTranslationActivity.startActivity(this, TranslateType.analyse, null, 0);
                    } else {
                        startActivity(new Intent(this, LoginActivity.class));
                        finish();
                    }
                } else {
                    Toast.makeText(this, "网络异常,请检查网络连接状态", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.br_translation:
                if (NetTool.isNetworkConnected(this)) {
                    if (application.getLoginApplication() != null) {
                        DictionaryTranslationActivity.startActivity(this, TranslateType.sentence, null, 0);
                    } else {
                        startActivity(new Intent(this, LoginActivity.class));
                        finish();
                    }
                } else {
                    Toast.makeText(this, "网络异常,请检查网络连接状态", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.bt_softwarepay:
                if (NetTool.isNetworkConnected(this)) {
                    if (application.getLoginApplication() != null) {
                        startActivity(new Intent(this, PayGuideActivity.class));
                    } else {
                        startActivity(new Intent(this, LoginActivity.class));
                        finish();
                    }
                } else {
                    Toast.makeText(this, "网络异常,请检查网络连接状态", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * 开启关闭侧滑菜单
     */
    public static void isshow() {
        if (slidingMenu.isDrawerOpen(rl_leftlayout)) {
            slidingMenu.closeDrawer(rl_leftlayout);
        } else {
            slidingMenu.openDrawer(rl_leftlayout);
        }
    }

    public void islayout(int islayout) {
        View view1 = findViewById(R.id.page_v1);
        View view2 = findViewById(R.id.page_v2);
        view1.setVisibility(View.INVISIBLE);
        view2.setVisibility(View.INVISIBLE);

        switch (islayout) {
            case 1:
                view1.setVisibility(View.VISIBLE);
                FragmentView.replacess(getSupportFragmentManager(), new HomeMovieActivity(), R.id.fl_Home);
                break;
            case 2:
                view2.setVisibility(View.VISIBLE);
                FragmentView.replacess(getSupportFragmentManager(), new HomeBooksActivity(), R.id.fl_Home);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (slidingMenu.isDrawerOpen(rl_leftlayout)) {
                slidingMenu.closeDrawer(rl_leftlayout);
            } else {

                final PromptDialog dialog = new PromptDialog(this);
                dialog.setContent(getString(R.string.tx_exit));
                dialog.show();
                dialog.setClickListener(new onPromptClickListener() {

                    @Override
                    public void onDetermine(View v) {
                        dialog.dismiss();
                        HomeActivity.this.finish();
                        System.exit(0);
                    }

                    @Override
                    public void onCancel(View v) {
                        dialog.dismiss();
                    }
                });
            }

        }
        return false;
    }
}
