package net.naucu.englishxianshi.ui.account;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiongbull.jlog.JLog;
import com.lijunsai.httpInterface.bean.LoginBean;
import com.lijunsai.httpInterface.implementation.HttpImplementation;
import com.lijunsai.httpInterface.request.NetworkRequestDeal;

import net.naucu.englishxianshi.R;
import net.naucu.englishxianshi.ui.HomeActivity;
import net.naucu.englishxianshi.ui.LoginActivity;
import net.naucu.englishxianshi.ui.WelcomeLoginActivity;
import net.naucu.englishxianshi.ui.base.BaseActivity;
import net.naucu.englishxianshi.util.ErrorTool;
import net.naucu.englishxianshi.util.SharedPreTool;
import net.naucu.englishxianshi.util.ToastTool;
import net.naucu.englishxianshi.widget.dialog.LoadingDialog;

import org.json.JSONException;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.activity_account)
public class AccountActivity extends BaseActivity implements AccountContract.View, OnClickListener {
    public static final String TAG = AccountActivity.class.getSimpleName();
    public static final String OPT_TYPE = "OPT_TYPE";
    // 操作类型 1注册 2忘记密匙 3修改密匙
    private int optType = 1;
    //
    public AccountContract.Presenter presenter;

    // Bar Layout
    @ViewInject(R.id.account_bar_layout)
    private RelativeLayout barLayout;
    // 返回按钮
    @ViewInject(R.id.bar_back)
    private ImageButton backBtn;
    // 标题
    @ViewInject(R.id.bar_title)
    private TextView titleText;

    // 手机号Layout
    @ViewInject(R.id.account_phone_layout)
    private LinearLayout phoneLayout;
    // 手机号输入框
    @ViewInject(R.id.account_phone_edit)
    private EditText phoneEdit;
    // 验证码输入框
    @ViewInject(R.id.account_smscode_edit)
    private EditText smscodeEdit;
    // 获取验证码按钮
    @ViewInject(R.id.account_obtain_smscode_btn)
    private Button obtainSmscodeBtn;
    // 下一步按钮
    @ViewInject(R.id.account_next_btn)
    private Button nextBtn;

    // 密码Layout
    @ViewInject(R.id.account_password_layout)
    private LinearLayout passwordLayout;
    // 新密码输入框
    @ViewInject(R.id.account_new_passowrd_edit)
    private EditText newPasswordEdit;
    // 确认密码
    @ViewInject(R.id.account_comfirm_passowrd_edit)
    private EditText comfirmPasswordEdit;
    // 完成按钮
    @ViewInject(R.id.account_complete_btn)
    private Button completeBtn;
    //
    private TimeCount timeCount;
    public String szImei;


    private LoadingDialog load;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        // 获取操作类型
        optType = getIntent().getIntExtra(OPT_TYPE, 0);
        // 初始化View
        initView();
        presenter = new AccountPresenter(this, optType, szImei);
    }

    /**
     * 初始化View
     */
    private void initView() {
        load = new LoadingDialog(this);
        TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        szImei = TelephonyMgr.getDeviceId();
        switch (optType) {
            case 2:
                titleText.setText("忘记密匙");
                break;
            case 3:
                titleText.setText("修 改密匙");
                break;
            default:
                titleText.setText("注册");
                break;
        }
        backBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (phoneLayout.getVisibility() == View.GONE) {
                    phoneLayout.setVisibility(View.VISIBLE);
                    newPasswordEdit.setText("");
                    comfirmPasswordEdit.setText("");
                    presenter.clearSmsCode();
                } else {
                    finish();
                }
            }
        });
        //
        obtainSmscodeBtn.setOnClickListener(this);
        nextBtn.setOnClickListener(this);
        completeBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.account_obtain_smscode_btn:
                presenter.clickObtainSmsmCode();
                break;
            case R.id.account_next_btn:
                presenter.nextClick();

                break;
            case R.id.account_complete_btn:
                presenter.completeClick();

                if (optType == 2 || optType == 3) {
                    ToastTool.showToastLong(this, "修改成功");
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
//                SharedPreTool.setSharedPreDateString(this, "loginResult", null);
//                SharedPreTool.setSharedPreDateString(this, "loginPhonenumber", null);
//                SharedPreTool.setSharedPreDateString(this, "loginArticleCode", null);
//                application.setLoginApplication(null);
//                if (SecurityCenterActivity.activity != null) {
//                    SecurityCenterActivity.activity.finish();
//                }
//                if (PersonalCenterActivity.activity != null) {
//                    PersonalCenterActivity.activity.finish();
//                }
//                if (MoreSettingsActivity.activity != null) {
//                    MoreSettingsActivity.activity.finish();
//                }
//                if (HomeActivity.activity != null) {
//                    HomeActivity.activity.finish();
//                }
                    finish();
                }else{

                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (timeCount == null) {
            timeCount = new TimeCount(60000, 1000);
        }
        if (presenter == null) {
            presenter = new AccountPresenter(this, optType, szImei);
        }
        presenter.start();
    }

    @Override
    public void showError(String msg) {
        ToastTool.showToastLong(this, msg);
    }

    @Override
    public String getPhone() {
        return phoneEdit.getText().toString().trim();
    }

    @Override
    public String getSmsCode() {
        return smscodeEdit.getText().toString().trim();
    }

    @Override
    public String getNewPassword() {
        return newPasswordEdit.getText().toString().trim();
    }

    @Override
    public String getCompletePassword() {
        return comfirmPasswordEdit.getText().toString().trim();
    }

    @Override
    public void callbackSmsCode(int smsCode) {
        obtainSmscodeBtn.setEnabled(false);
        obtainSmscodeBtn.setTextColor(Color.parseColor("#989898"));
        obtainSmscodeBtn.setBackgroundResource(R.drawable.login_registered_press);
        timeCount.start();
    }

    @Override
    public void callbackNextSuccess() {
        phoneLayout.setVisibility(View.GONE);
        passwordLayout.setVisibility(View.VISIBLE);
        if (optType != 2 && optType != 3) {
            comfirmPasswordEdit.setVisibility(View.GONE);
            newPasswordEdit.setHint("请输入密匙");
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) newPasswordEdit.getLayoutParams();
            newPasswordEdit.setInputType(InputType.TYPE_CLASS_TEXT);
            lp.setMargins(10, 50, 10, 0);

            newPasswordEdit.setLayoutParams(lp);
        }
    }

    @Override
    public void callbackCompleteSuccess() {
        switch (optType) {
            case 1:
                submitLogin();
                ToastTool.showToastLong(this, "注册成功");
                finish();
                break;
            case 2:

                submitLogin();
                ToastTool.showToastLong(this, "重置密匙成功");
                finish();
                break;
            case 3:
//                Intent intent = new Intent(this, WelcomeLoginActivity.class);
//                startActivity(intent);
//                SharedPreTool.setSharedPreDateString(this, "loginResult", null);
//                SharedPreTool.setSharedPreDateString(this, "loginPhonenumber", null);
//                SharedPreTool.setSharedPreDateString(this, "loginArticleCode", null);
//                application.setLoginApplication(null);
//                if (SecurityCenterActivity.activity != null) {
//                    SecurityCenterActivity.activity.finish();
//                }
//                if (PersonalCenterActivity.activity != null) {
//                    PersonalCenterActivity.activity.finish();
//                }
//                if (MoreSettingsActivity.activity != null) {
//                    MoreSettingsActivity.activity.finish();
//                }
//                if (HomeActivity.activity != null) {
//                    HomeActivity.activity.finish();
//                }
                submitLogin();
                ToastTool.showToastLong(this, "修改成功");
                finish();
                break;
        }

    }

    class TimeCount extends CountDownTimer {

        /**
         * @param millisInFuture    The number of millis in the future from the call to
         *                          {@link #start()} until the countdown is done and
         *                          {@link #onFinish()} is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            obtainSmscodeBtn.setText("(" + (millisUntilFinished / 1000) + ")s重新获取");
        }

        @Override
        public void onFinish() {
            obtainSmscodeBtn.setEnabled(true);
            obtainSmscodeBtn.setText("重新获取");
            obtainSmscodeBtn.setBackgroundResource(R.drawable.login_registered_liftup);
            obtainSmscodeBtn.setTextColor(Color.parseColor("#ffffff"));
        }
    }
    private String tx_username;
    private String tx_userpassword;
    private void getText() {
        tx_username = phoneEdit.getText().toString().trim();
        tx_userpassword = newPasswordEdit.getText().toString().trim();
    }
    private void submitLogin() {
        load.setContent("登录中");
        load.show();
        getText();
        RequestParams params = new RequestParams(HttpImplementation.getLogin());
        params.addBodyParameter("info", "{'telephone':'" + tx_username + "','password':'" + tx_userpassword + "','serialnumber':"+szImei+"}");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onCancelled(CancelledException e) {
                load.dismiss();
                JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());
            }

            @Override
            public void onError(Throwable throwable, boolean isOnCallback) {
                load.dismiss();
                ErrorTool.onError(AccountActivity.this, isOnCallback);
                JLog.e((throwable == null || throwable.getMessage() == null) ? "Throwable is null" : throwable.getMessage());
            }

            @Override
            public void onFinished() {
                load.dismiss();
            }

            @Override
            public void onSuccess(String result) {
                try {
                    if (NetworkRequestDeal.isErrCode(result)) {
                        switch (NetworkRequestDeal.getErrCode(result)) {
                            case 1001:
                                ToastTool.showToastLong(AccountActivity.this, "手机号或密匙错误!");
                                break;
                            case 1002:
                                ToastTool.showToastLong(AccountActivity.this, "用户不存在!");
                                break;
                            case 1003:
                                ToastTool.showToastLong(AccountActivity.this, "手机号或密匙不能为空!");
                                break;
                            case 1004:
                                ToastTool.showToastLong(AccountActivity.this, "服务器错误!");
                                break;
                            case 1005:
                                ToastTool.showToastLong(AccountActivity.this, "手机号码格式不正确!");
                                break;
                            case 1007:
                                ToastTool.showToastLong(AccountActivity.this, "手机序列号不符合!");
                                break;
                            case 1000:
                                LoginBean loginBean = NetworkRequestDeal.getLogin(result);
                                if (loginBean != null) {
                                    SharedPreTool.setSharedPreDateString(AccountActivity.this, "loginPhonenumber", tx_username);
                                    SharedPreTool.setSharedPreDateString(AccountActivity.this, "loginArticleCode", tx_userpassword);
                                    ToastTool.showToastLong(AccountActivity.this, "登录成功!");
                                    application.setLoginApplication(loginBean);
                                    startActivity(new Intent(AccountActivity.this, HomeActivity.class));
                                    finish();
                                    if (WelcomeLoginActivity.activity != null) {
                                        WelcomeLoginActivity.activity.finish();
                                    }
                                }
                                break;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                load.dismiss();
            }
        });
    }


}
