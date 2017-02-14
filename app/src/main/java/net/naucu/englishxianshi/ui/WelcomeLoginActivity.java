package net.naucu.englishxianshi.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

import net.naucu.englishxianshi.R;
import net.naucu.englishxianshi.ui.account.AccountActivity;
import net.naucu.englishxianshi.ui.base.BaseActivity;

import cn.jpush.android.api.JPushInterface;

/**
 * 类名: WelcomeLoginActivity.java
 * 描述: 登录欢迎页
 * 作者: youyou_pc
 * 时间: 2015年11月19日  下午2:45:55
 */
public class WelcomeLoginActivity extends BaseActivity implements OnClickListener {
    private Button bt_login;
    private Button bt_registered;
    public static WelcomeLoginActivity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_welcomelogin);
        initView();
        initEvent();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        bt_login = (Button) findViewById(R.id.bt_login);
        bt_registered = (Button) findViewById(R.id.bt_registered);
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        bt_login.setOnClickListener(this);
        bt_registered.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_login:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.bt_registered:
                Intent intent = new Intent(this, AccountActivity.class);
                intent.putExtra(AccountActivity.OPT_TYPE, 1);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }
}
