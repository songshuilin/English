package net.naucu.englishxianshi.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.app.myTool.ActionTitleBarWidget;
import com.app.myTool.ActionTitleBarWidget.ClickListener;

import net.naucu.englishxianshi.R;
import net.naucu.englishxianshi.ui.base.BaseActivity;
import net.naucu.englishxianshi.util.AppTool;

/**
 * 类名: AboutActivity.java
 * 描述: 关于我们
 * 作者: youyou_pc
 * 时间: 2015年11月25日  下午4:50:45
 */
public class AboutActivity extends BaseActivity {
    private ActionTitleBarWidget titlebar;//初始化标题控
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initView();
        initTitleBar();
        initEvent();
    }

    private void initEvent() {
        titlebar.OnTitleBarClickListener(clickListener);
    }

    private void initView() {
        titlebar = (ActionTitleBarWidget) findViewById(R.id.titlebar);
        TextView tv_versionnumber = (TextView) findViewById(R.id.tv_versionnumber);
        String version = tv_versionnumber.getText().toString().replace("$version$", AppTool.getVersion(this));
        tv_versionnumber.setText(version);
    }

    /**
     * 初始化TitleBar
     */
    private void initTitleBar() {
        titlebar.setTitleBackgroundResource(R.drawable.menubutton_select, 0);
        titlebar.setLeftGravity(Gravity.CENTER);
        titlebar.setLeftIco(R.drawable.back);
        titlebar.setCenterGravity(Gravity.CENTER);
        titlebar.setCenterText(getString(R.string.tx_about), 17, Color.BLACK);
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

        }
    };
}
