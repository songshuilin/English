package net.naucu.englishxianshi.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;

import com.app.myTool.ActionTitleBarWidget;
import com.app.myTool.ActionTitleBarWidget.ClickListener;

import net.naucu.englishxianshi.R;
import net.naucu.englishxianshi.ui.account.AccountActivity;
import net.naucu.englishxianshi.ui.base.BaseActivity;

/**
 * 类名: ModifyGenderActivity.java
 * 描述: TODO 安全中心
 * 作者: youyou_pc
 * 时间: 2015年11月26日
 * 上午10:32:20
 */
public class SecurityCenterActivity extends BaseActivity implements OnClickListener {
    public static SecurityCenterActivity activity;
    private ActionTitleBarWidget titlebar;// 初始化标题控件

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_securitycenter);
        activity = this;
        initView();
        initTitleBar();
        initEvent();
    }

    private void initEvent() {
        titlebar.OnTitleBarClickListener(clickListener);
    }

    private void initView() {
        titlebar = (ActionTitleBarWidget) findViewById(R.id.titlebar);
        findViewById(R.id.rl_loginpassword).setOnClickListener(this);
    }

    private void initTitleBar() {
        titlebar.setTitleBackgroundResource(R.drawable.menubutton_select, 0);
        titlebar.setLeftGravity(Gravity.CENTER);
        titlebar.setLeftIco(R.drawable.back);
        titlebar.setCenterGravity(Gravity.CENTER);
        titlebar.setTitleBarBackgroundColor(Color.WHITE);
        titlebar.setCenterText(getString(R.string.Securitycenter), 17, Color.BLACK);
    }

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
            case R.id.rl_loginpassword:
                Intent intentUpdatePass = new Intent(this, AccountActivity.class);
                intentUpdatePass.putExtra(AccountActivity.OPT_TYPE, 3);
                startActivity(intentUpdatePass);
                break;
        }

    }
}
