package net.naucu.englishxianshi.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.app.myTool.ActionTitleBarWidget;
import com.app.myTool.ActionTitleBarWidget.ClickListener;
import com.jiongbull.jlog.JLog;
import com.lijunsai.httpInterface.implementation.HttpImplementation;

import net.naucu.englishxianshi.R;
import net.naucu.englishxianshi.ui.base.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * 类名: PayGuideActivity.java
 * 描述: TODO 支付引导
 * 作者: youyou_pc
 * 时间: 2015年12月31日  下午4:35:26
 */
public class PayGuideActivity extends BaseActivity implements OnClickListener {
    private ActionTitleBarWidget titlebar;//初始化标题控\

    //钱
    private TextView tvMoney;
    //邀请码
    private EditText _activecode;
    //立即支付
    private Button bt_pay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_payguide);

        initView();
        initTitleBar();
        initEvent();
    }

    private void initEvent() {
        titlebar.OnTitleBarClickListener(clickListener);
        bt_pay.setOnClickListener(this);
    }

    private void initView() {
        tvMoney = (TextView) findViewById(R.id.tv_money);
        titlebar = (ActionTitleBarWidget) findViewById(R.id.titlebar);
        bt_pay = (Button) findViewById(R.id.bt_pay);
        _activecode = (EditText) findViewById(R.id.et_activecode);

    }

    /**
     * 初始化TitleBar
     */
    private void initTitleBar() {
        titlebar.setTitleBackgroundResource(R.drawable.menubutton_select, 0);
        titlebar.setLeftGravity(Gravity.CENTER);
        titlebar.setLeftIco(R.drawable.back);
        titlebar.setCenterGravity(Gravity.CENTER);
        titlebar.setCenterText(getString(R.string.tx_unlock_features), 17, Color.BLACK);
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

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, net.naucu.englishxianshi.pay.PayActivity.class);
        intent.putExtra(net.naucu.englishxianshi.pay.PayActivity.ACTIVE_CODE, _activecode.getText().toString());
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadMoney();
    }

    private void loadMoney() {
        RequestParams params = new RequestParams(HttpImplementation.selectPrice());
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonResult = new JSONObject(result);
                    if (!jsonResult.isNull("activeMoney")) {
                        tvMoney.setText("￥" + jsonResult.optInt("activeMoney") + ".00");
                    } else {
                        tvMoney.setText("￥0.00");
                        bt_pay.setClickable(false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                JLog.e((throwable == null || throwable.getMessage() == null) ? "Throwable is null" : throwable.getMessage());
            }

            @Override
            public void onCancelled(CancelledException e) {
                JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());
            }

            @Override
            public void onFinished() {

            }
        });
    }

}
