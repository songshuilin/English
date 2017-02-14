package net.naucu.englishxianshi.ui;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.jiongbull.jlog.JLog;
import com.lijunsai.httpInterface.bean.LoginBean;
import com.lijunsai.httpInterface.implementation.HttpImplementation;
import com.lijunsai.httpInterface.request.NetworkRequestDeal;
import com.lijunsai.httpInterface.tool.ValidationTool;

import net.naucu.englishxianshi.R;
import net.naucu.englishxianshi.ui.account.AccountActivity;
import net.naucu.englishxianshi.ui.base.BaseActivity;
import net.naucu.englishxianshi.util.ErrorTool;
import net.naucu.englishxianshi.util.SharedPreTool;
import net.naucu.englishxianshi.util.ToastTool;
import net.naucu.englishxianshi.widget.dialog.LoadingDialog;

import org.json.JSONException;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * 类名: LoginActivity.java 描述: 登录 作者: youyou_pc 时间: 2015年11月16日 上午9:28:29
 */
public class LoginActivity extends BaseActivity implements OnClickListener {
    private EditText ed_username;
    private EditText ed_userpassword;
    private Button bt_login;
    private Button bt_RetrievePassword;
    private String tx_username;
    private String tx_userpassword;
    private LoadingDialog load;
    public ImageView bar_back;
    public String szImei;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        initView();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        /**
         * 序列号
         * */
        szImei = TelephonyMgr.getDeviceId();


        load = new LoadingDialog(this);
        ed_username = (EditText) findViewById(R.id.ed_username);
        if (SharedPreTool.getSharedPreDateString(LoginActivity.this, "loginPhonenumber", null) != null) {
            ed_username.setText(SharedPreTool.getSharedPreDateString(LoginActivity.this, "loginPhonenumber", null));
        }
        ed_userpassword = (EditText) findViewById(R.id.ed_userpassword);
        bt_login = (Button) findViewById(R.id.bt_login);
        bt_RetrievePassword = (Button) findViewById(R.id.bt_RetrievePassword);
        bar_back = (ImageView) findViewById(R.id.bar_back);
        bt_RetrievePassword.setOnClickListener(this);
        bt_login.setOnClickListener(this);
        bar_back.setOnClickListener(this);
        Log.i("TAG", "qe87as4d35as4dw8q7 = " + szImei);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_login:
                if (isText()) {
                    submitLogin();
                }
                break;
            case R.id.bt_RetrievePassword:
                Intent intent = new Intent(this, AccountActivity.class);
                intent.putExtra(AccountActivity.OPT_TYPE, 2);
                startActivity(intent);
                break;
            case R.id.bar_back:
                this.finish();
                break;
        }

    }

    private void getText() {
        tx_username = ed_username.getText().toString().trim();
        tx_userpassword = ed_userpassword.getText().toString().trim();
    }

    private boolean isText() {
        getText();
        if (!TextUtils.isEmpty(tx_username) && tx_username.length() != 0) {
            if (ValidationTool.ValidationTelephone(tx_username)) {
                if (!TextUtils.isEmpty(tx_userpassword) && tx_userpassword.length() != 0) {
                    return true;
                } else {
                    ToastTool.showToastLong(LoginActivity.this, "请输入密匙!");
                }
            } else {
                ToastTool.showToastLong(LoginActivity.this, "请输入正确的手机号码!");
            }
        } else {
            ToastTool.showToastLong(LoginActivity.this, "请输入手机号!");
        }
        return false;
    }

    private void submitLogin() {
        load.setContent("登录中");
        load.show();
        getText();
        RequestParams params = new RequestParams(HttpImplementation.getLogin());
        params.addBodyParameter("info", "{'telephone':'" + tx_username + "','password':'" + tx_userpassword + "','serialnumber':"+szImei+"}");
        x.http().get(params, new CommonCallback<String>() {
            @Override
            public void onCancelled(CancelledException e) {
                load.dismiss();
                JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());
            }

            @Override
            public void onError(Throwable throwable, boolean isOnCallback) {
                load.dismiss();
                ErrorTool.onError(LoginActivity.this, isOnCallback);
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
                                ToastTool.showToastLong(LoginActivity.this, "手机号或密匙错误!");
                                break;
                            case 1002:
                                ToastTool.showToastLong(LoginActivity.this, "用户不存在!");
                                break;
                            case 1003:
                                ToastTool.showToastLong(LoginActivity.this, "手机号或密匙不能为空!");
                                break;
                            case 1004:
                                ToastTool.showToastLong(LoginActivity.this, "服务器错误!");
                                break;
                            case 1005:
                                ToastTool.showToastLong(LoginActivity.this, "手机号码格式不正确!");
                                break;
                            case 1007:
                                ToastTool.showToastLong(LoginActivity.this, "手机序列号不符合!");
                                break;
                            case 1000:
                                LoginBean loginBean = NetworkRequestDeal.getLogin(result);
                                if (loginBean != null) {
                                    SharedPreTool.setSharedPreDateString(LoginActivity.this, "loginPhonenumber", tx_username);
                                    SharedPreTool.setSharedPreDateString(LoginActivity.this, "loginArticleCode", tx_userpassword);
                                    ToastTool.showToastLong(LoginActivity.this, "登录成功!");
                                    application.setLoginApplication(loginBean);
                                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
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
