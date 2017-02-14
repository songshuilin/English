package net.naucu.englishxianshi.pay;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.myTool.ActionTitleBarWidget;
import com.app.myTool.ActionTitleBarWidget.ClickListener;

import net.naucu.englishxianshi.R;
import net.naucu.englishxianshi.BaseApplication;
import net.naucu.englishxianshi.ui.base.BaseActivity;
import net.naucu.englishxianshi.widget.dialog.PromptDialog;
import net.naucu.englishxianshi.widget.dialog.PromptDialog.OnDismissListener;
import net.naucu.englishxianshi.widget.dialog.PromptDialog.onPromptClickListener;
import net.naucu.englishxianshi.util.ToastTool;

public class PayActivity extends BaseActivity implements PayContract.View, OnClickListener {
    public static final String TAG = PayActivity.class.getSimpleName();
    public static final String ACTIVE_CODE = "ACTIVE_CODE";
    public PayContract.Presenter presenter;
    private TextView tvMoney;
    //支付宝
    private RelativeLayout ll_Paytreasure;
    //微信
    private RelativeLayout ll_WeChat;
    // 初始化标题控件
    private ActionTitleBarWidget titlebar;
    //邀请码
    private String activeCode;

    private PromptDialog promptDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        activeCode = getIntent().getStringExtra(ACTIVE_CODE);

        //初始化View
        initView();
    }

    private void initView() {
        tvMoney = (TextView) findViewById(R.id.tv_money);
        ll_Paytreasure = (RelativeLayout) findViewById(R.id.ll_Paytreasure);
        ll_Paytreasure.setOnClickListener(this);
        ll_WeChat = (RelativeLayout) findViewById(R.id.ll_WeChat);
        ll_WeChat.setOnClickListener(this);

        titlebar = (ActionTitleBarWidget) findViewById(R.id.titlebar);
        titlebar.setTitleBackgroundResource(R.drawable.menubutton_select, 0);
        titlebar.setLeftGravity(Gravity.CENTER);
        titlebar.setLeftIco(R.drawable.back);
        titlebar.setCenterText(getString(R.string.tx_unlock_features), 17, Color.BLACK);
        titlebar.setTitleBarBackgroundColor(Color.WHITE);
        titlebar.setCenterGravity(Gravity.CENTER);
        titlebar.OnTitleBarClickListener(new ClickListener() {

            @Override
            public void onright(View arg0) {
            }

            @Override
            public void onleft(View arg0) {
                finish();

            }

            @Override
            public void oncenter(View arg0) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_Paytreasure:
                presenter.payTreasure();
                break;
            case R.id.ll_WeChat:
                presenter.weChat();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        if (presenter == null) {
            presenter = new PayPresenter(this);
        }
        presenter.start();
        super.onResume();
    }


    @Override
    public BaseApplication getApp() {
        return application;
    }


    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public String getActiveCode() {
        return activeCode;
    }

    @Override
    public void callbakcMoney(int money) {
        tvMoney.setText("￥" + money + ".00");
    }

    @Override
    public void showError(String msg) {
        ToastTool.showToastLong(this, msg);
    }

    @Override
    public void callbackPaySuccess() {
        if (promptDialog == null) {
            promptDialog = new PromptDialog(this);
            promptDialog.setContent("支付成功");
            promptDialog.setClickListener(new onPromptClickListener() {

                @Override
                public void onDetermine(View v) {
                    promptDialog.dismiss();
                }

                @Override
                public void onCancel(View v) {
                    promptDialog.dismiss();
                }
            });
            promptDialog.setOnDismissListener(new OnDismissListener() {
                @Override
                public void onDismiss(Dialog dialog) {
                    finish();
                }
            });
        }
        promptDialog.show();
    }
}
