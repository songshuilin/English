package net.naucu.englishxianshi.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.app.myTool.ActionTitleBarWidget;
import com.app.myTool.ActionTitleBarWidget.ClickListener;

import net.naucu.englishxianshi.R;
import net.naucu.englishxianshi.adapter.MyrecordingAdapter;
import net.naucu.englishxianshi.bean.MyRecordBean;
import net.naucu.englishxianshi.db.Manager;
import net.naucu.englishxianshi.ui.base.BaseActivity;

import java.util.List;

/**
 * 类名: MyrecordingActivity.java 描述: TODO 我的配音 作者: youyou_pc 时间: 2015年11月20日
 * 下午1:25:22
 */
public class MyrecordingActivity extends BaseActivity {
    private ActionTitleBarWidget titlebar;// 初始化标题控件
    private ListView lv_recording;
    private MyrecordingAdapter adapter;
    private List<MyRecordBean> recordlist;
    private LinearLayout _bottom;
    private TextView _bottomdelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myrecording);
        initView();
        initTitleBar();
        initData();
        initEvent();

    }

    private void initView() {
        titlebar = (ActionTitleBarWidget) findViewById(R.id.titlebar);
        lv_recording = (ListView) findViewById(R.id.lv_recording);
        _bottom = (LinearLayout) findViewById(R.id.bottom_choice);
        _bottomdelete = (TextView) findViewById(R.id.tv_counts);
    }

    /**
     * 初始化TitleBar
     */
    private void initTitleBar() {
        titlebar.setTitleBackgroundResource(R.drawable.menubutton_select, R.drawable.menubutton_select);
        titlebar.setLeftGravity(Gravity.CENTER);
        titlebar.setLeftIco(R.drawable.back);
        titlebar.setRightGravity(Gravity.CENTER);
        titlebar.setRightText(getString(R.string.tx_editor), 17, Color.BLACK);
        titlebar.setCenterText(getString(R.string.tx_Myrecording), 17, Color.BLACK);
        titlebar.setCenterGravity(Gravity.CENTER);
        titlebar.setTitleBarBackgroundColor(Color.WHITE);
    }

    private void initData() {
        if (application.getLoginApplication() == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

//        Log.i("TAG", "EDGEDG_RECORD 02 = " + Manager.Records2MyRecordBeans(Manager.sleectRecords()));
        recordlist = Manager.Records2MyRecordBeans(Manager.sleectRecords());
        Log.i("TAG", "EDGEDG_RECORD 01 = " + Manager.sleectRecords());
        Log.i("TAG", "EDGEDG_RECORD 02 = " + Manager.Records2MyRecordBeans(Manager.sleectRecords()));
        if (recordlist != null) {
            adapter = new MyrecordingAdapter(this, recordlist, hander,
                    application.getLoginApplication().getState() != null
                            && application.getLoginApplication().getState().equals("0"));
            lv_recording.setAdapter(adapter);
        }
    }

    private void initEvent() {
        titlebar.OnTitleBarClickListener(clickListener);
        lv_recording.setOnItemClickListener(onItemClickListener);
        _bottom.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                titlebar.RightViewRemove();
                titlebar.setRightText(getString(R.string.tx_editor), 17, Color.BLACK);
                if (adapter != null) {
                    for (int i : adapter.getSelects()) {
                        Manager.DeleteRecord(recordlist.get(i).getId());
                    }
                }
                adapter.showDelete(false);
                initData();
                _bottom.setVisibility(View.GONE);
            }
        });
    }

    private Handler hander = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            _bottomdelete.setText(adapter.getSelects().size() + "");
        }
    };
    private OnItemClickListener onItemClickListener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> adapterview, View view, int i, long l) {
            if (recordlist != null) {
                // VideoPlayActivity.beginRollPlayWhenStart(MyrecordingActivity.this,
                // recordlist.get(i));
            }
        }
    };
    private ClickListener clickListener = new ClickListener() {

        @Override
        public void onright(View v) {
            if (recordlist == null || recordlist.size() <= 0) {
                return;
            }
            if (_bottom.getVisibility() == View.VISIBLE) {
                titlebar.RightViewRemove();
                titlebar.setRightText(getString(R.string.tx_editor), 17, Color.BLACK);
                adapter.showDelete(false);
                _bottom.setVisibility(View.GONE);
            } else {
                titlebar.RightViewRemove();
                titlebar.setRightText(getString(R.string.tx_cancel), 17, Color.BLACK);
                adapter.showDelete(true);
                _bottom.setVisibility(View.VISIBLE);
            }
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
