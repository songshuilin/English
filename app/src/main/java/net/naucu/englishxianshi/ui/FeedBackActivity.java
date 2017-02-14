package net.naucu.englishxianshi.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.myTool.ActionTitleBarWidget;
import com.app.myTool.ActionTitleBarWidget.ClickListener;

import net.naucu.englishxianshi.R;
import net.naucu.englishxianshi.ui.base.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 类名: FeedBackActivity.java
 * 描述: TODO 意见反馈
 * 作者: youyou_pc
 * 时间: 2015年11月25日  下午5:02:59
 */
public class FeedBackActivity extends BaseActivity implements View.OnClickListener, FeedBackCallback {
    private ActionTitleBarWidget titlebar;//初始化标题控
    public Button btn_submit_feedback;
    public EditText edit_feedback;
    public Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        initView();
        initTitleBar();
        initEvent();
    }

    private void initEvent() {
        titlebar.OnTitleBarClickListener(clickListener);
        btn_submit_feedback.setOnClickListener(this);
    }

    private void initView() {
        btn_submit_feedback = (Button) findViewById(R.id.btn_submit_feedback);
        edit_feedback = (EditText) findViewById(R.id.edit_feedback);
        titlebar = (ActionTitleBarWidget) findViewById(R.id.titlebar);
    }

    /**
     * 初始化TitleBar
     */
    private void initTitleBar() {
        titlebar.setTitleBackgroundResource(R.drawable.menubutton_select, 0);
        titlebar.setLeftGravity(Gravity.CENTER);
        titlebar.setLeftIco(R.drawable.back);
        titlebar.setCenterGravity(Gravity.CENTER);
        titlebar.setCenterText(getString(R.string.tx_feedback), 17, Color.BLACK);
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_submit_feedback:
                if (edit_feedback.getText().toString().length() == 0 || edit_feedback.getText().toString().equals("") || edit_feedback.getText().toString() == "") {
                    toast = Toast.makeText(this, "请输入反馈信息!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {
                    HttpSubmitFeedBack httpSubmitFeedBack = new HttpSubmitFeedBack(edit_feedback.getText().toString(), this);
                    new Thread(httpSubmitFeedBack).start();

                }
                break;
        }
    }

    @Override
    public void feedBackCallback(String json) throws JSONException {
        parseJson(json);
    }

    private void parseJson(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        Looper.prepare();
        if (jsonObject.get("errCode").toString().equals("10000") || jsonObject.get("errCode'").toString() == "10000") {
            toast = Toast.makeText(this, "提交成功!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            finish();//成功后关闭Activity
        } else {
            toast = Toast.makeText(this, "提交失败!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
        Looper.loop();
    }
}
