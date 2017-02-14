package net.naucu.englishxianshi.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import com.app.myTool.ActionTitleBarWidget;
import com.app.myTool.ActionTitleBarWidget.ClickListener;

import net.naucu.englishxianshi.R;
import net.naucu.englishxianshi.ui.base.BaseActivity;

/**
 * 类名: PersonalCenterActivity.java
 * 描述: 个人中心
 * 作者: youyou_pc
 * 时间: 2015年11月19日  上午10:23:18
 */

public class PersonalCenterActivity extends BaseActivity implements OnClickListener {
    public static final String TAG = PersonalCenterActivity.class.getSimpleName();
    public static PersonalCenterActivity activity;
    private ActionTitleBarWidget titlebar;// 初始化标题控件
    private RelativeLayout rl_modifygender;
    private RelativeLayout rl_Securitycenter;
    private RelativeLayout rl_nickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_personalcenter);
        activity = this;
        initView();
        initTitleBar();
        initEvent();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        titlebar = (ActionTitleBarWidget) findViewById(R.id.titlebar);
        rl_modifygender = (RelativeLayout) findViewById(R.id.rl_modifygender);
        rl_Securitycenter = (RelativeLayout) findViewById(R.id.rl_Securitycenter);
        rl_nickname = (RelativeLayout) findViewById(R.id.rl_nickname);

    }

    /**
     * 初始化TitleBar
     */
    private void initTitleBar() {
        titlebar.setTitleBackgroundResource(R.drawable.menubutton_select, 0);
        titlebar.setLeftGravity(Gravity.CENTER);
        titlebar.setLeftIco(R.drawable.back);
        titlebar.setCenterText("帮助与安全", 17, Color.BLACK);
        titlebar.setTitleBarBackgroundColor(Color.WHITE);
        titlebar.setCenterGravity(Gravity.CENTER);
    }

    private void initEvent() {
        titlebar.OnTitleBarClickListener(clickListener);
        rl_modifygender.setOnClickListener(this);
        rl_Securitycenter.setOnClickListener(this);
        rl_nickname.setOnClickListener(this);
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
            case R.id.rl_modifygender:
                startActivity(new Intent(this, ModifyGenderActivity.class));
                break;
            case R.id.rl_Securitycenter:
                startActivity(new Intent(this, SecurityCenterActivity.class));
                break;
            case  R.id.rl_nickname:
                startActivity(new Intent(this, ModifyNicknameActivity.class));
                break;
        }

    }
}
