package net.naucu.englishxianshi.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.app.myTool.ActionTitleBarWidget;
import com.app.myTool.ActionTitleBarWidget.ClickListener;

import net.naucu.englishxianshi.BaseApplication;
import net.naucu.englishxianshi.R;
import net.naucu.englishxianshi.ui.base.BaseActivity;
import net.naucu.englishxianshi.util.SharedPreTool;
import net.naucu.englishxianshi.widget.dialog.PromptDialog;
import net.naucu.englishxianshi.widget.dialog.PromptDialog.onPromptClickListener;
import net.naucu.englishxianshi.widget.view.CircleImageView;
import net.naucu.englishxianshi.widget.view.SwitchView;
import net.naucu.englishxianshi.widget.view.SwitchView.OnCheckedChangeListener;

import org.xutils.x;

import cn.jpush.android.api.JPushInterface;

/**
 * 类名: MoreSettingsActivity.java
 * 描述: TODO 更多设置
 * 作者: youyou_pc
 * 时间: 2015年11月19日 下午6:01:40
 */
@SuppressWarnings("unused")
public class MoreSettingsActivity extends BaseActivity implements OnClickListener {
    public static MoreSettingsActivity activity;

    private ActionTitleBarWidget titlebar;// 初始化标题控件
    public static CircleImageView im_user_head;// 用户头像
    private LinearLayout ll_usercenter;// 帮助与安全
    private SwitchView sv_trafficswitch;// 使用流量下载时提醒我
    private SwitchView sv_pushswitch;// 消息推送
    private RelativeLayout rl_readingset;// 正音朗读播放设定
    private RelativeLayout rl_about;// 关于我们
    private RelativeLayout rl_feedback;// 意见反馈
    private RelativeLayout rl_withdrawaccount;// 退出登录

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moresettings);
        activity = this;

        initview();
        initDate();
        initTitleBar();
        initEvent();
    }

    private void initview() {
        titlebar = (ActionTitleBarWidget) findViewById(R.id.titlebar);
        im_user_head = (CircleImageView) findViewById(R.id.im_user_head);
        ll_usercenter = (LinearLayout) findViewById(R.id.ll_usercenter);
        sv_trafficswitch = (SwitchView) findViewById(R.id.sv_trafficswitch);
        sv_pushswitch = (SwitchView) findViewById(R.id.sv_pushswitch);
        rl_readingset = (RelativeLayout) findViewById(R.id.rl_readingset);
        rl_about = (RelativeLayout) findViewById(R.id.rl_about);
        rl_feedback = (RelativeLayout) findViewById(R.id.rl_feedback);
        rl_withdrawaccount = (RelativeLayout) findViewById(R.id.rl_withdrawaccount);

    }

    /**
     * 初始化TitleBar
     */
    private void initTitleBar() {
        titlebar.setTitleBackgroundResource(R.drawable.menubutton_select, 0);
        titlebar.setLeftGravity(Gravity.CENTER);
        titlebar.setLeftIco(R.drawable.back);
        titlebar.setTitleBarBackgroundColor(Color.WHITE);
        titlebar.setCenterText(getString(R.string.tx_MoreSettings), 17, Color.BLACK);
        titlebar.setCenterGravity(Gravity.CENTER);
    }

    private void initEvent() {
        titlebar.OnTitleBarClickListener(clickListener);
        ll_usercenter.setOnClickListener(this);
        rl_about.setOnClickListener(this);
        rl_feedback.setOnClickListener(this);
        rl_withdrawaccount.setOnClickListener(this);
        rl_readingset.setOnClickListener(this);
        sv_trafficswitch.setOnCheckedChangeListener(changeListener);
        sv_pushswitch.setOnCheckedChangeListener(changeListener);
    }

    private void initDate() {
        sv_trafficswitch.setOn(SharedPreTool.getSharedPreDateBoolean(MoreSettingsActivity.this, "TrafficsShared", true));
        sv_pushswitch.setOn(SharedPreTool.getSharedPreDateBoolean(MoreSettingsActivity.this, "PushShared", true));
        if (application.getLoginApplication() != null) {
            if (application.getLoginApplication().getPicUrl() != null) {
                x.image().bind(im_user_head, application.getLoginApplication().getPicUrl(), BaseApplication.getImageOption());
            }
        }
    }

    private OnCheckedChangeListener changeListener = new OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(SwitchView mSwitch, final boolean isOn) {
            switch (mSwitch.getId()) {
                case R.id.sv_trafficswitch:
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            SharedPreTool.setSharedPreDateBoolean(MoreSettingsActivity.this, "TrafficsShared", isOn);
                        }
                    }).start();

                    break;
                case R.id.sv_pushswitch:
                    Log.i("TAG", "pushswitch = 消息推送开启完成");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (isOn) {
                                JPushInterface.resumePush(getApplicationContext());
                            } else {
                                JPushInterface.stopPush(getApplicationContext());
                            }
                            SharedPreTool.setSharedPreDateBoolean(MoreSettingsActivity.this, "PushShared", isOn);
                        }
                    }).start();

                    break;
            }
        }
    };
    private ClickListener clickListener = new ClickListener() {

        @Override
        public void onright(View v) {
        }

        @Override
        public void onleft(View v) {
            finish();
        }

        @Override
        public void oncenter(View arg0) {
            // TODO Auto-generated method stub

        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_usercenter:
                startActivity(new Intent(this, PersonalCenterActivity.class));
                break;
            case R.id.rl_about:
                startActivity(new Intent(this, AboutActivity.class));
                break;
            case R.id.rl_feedback:
                startActivity(new Intent(this, FeedBackActivity.class));
                break;
            case R.id.rl_readingset:
                startActivity(new Intent(this, ReadingsetActivity.class));
                break;
            case R.id.rl_withdrawaccount:
                final PromptDialog dialog = new PromptDialog(this);
                dialog.setContent(getString(R.string.exit_user));
                dialog.show();
                dialog.setClickListener(new onPromptClickListener() {

                    @Override
                    public void onDetermine(View v) {
                        dialog.dismiss();
                        SharedPreTool.setSharedPreDateString(MoreSettingsActivity.this, "loginResult", null);
                        SharedPreTool.setSharedPreDateString(MoreSettingsActivity.this, "loginPhonenumber", null);
                        SharedPreTool.setSharedPreDateString(MoreSettingsActivity.this, "loginArticleCode", null);
                        application.setLoginApplication(null);
                        startActivity(new Intent(MoreSettingsActivity.this, WelcomeLoginActivity.class));
                        finish();
                        if (HomeActivity.activity != null) {
                            HomeActivity.activity.finish();
                        }
                    }

                    @Override
                    public void onCancel(View v) {
                        dialog.dismiss();
                    }
                });
                break;
        }
    }
}
