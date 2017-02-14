package net.naucu.englishxianshi.ui.account.updatephone;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import net.naucu.englishxianshi.R;
import net.naucu.englishxianshi.ui.HomeActivity;
import net.naucu.englishxianshi.ui.MoreSettingsActivity;
import net.naucu.englishxianshi.ui.PersonalCenterActivity;
import net.naucu.englishxianshi.ui.SecurityCenterActivity;
import net.naucu.englishxianshi.ui.WelcomeLoginActivity;
import net.naucu.englishxianshi.ui.base.BaseActivity;
import net.naucu.englishxianshi.util.SharedPreTool;
import net.naucu.englishxianshi.util.ToastTool;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * 修改手机号
 *
 * @author Yi
 */
@ContentView(R.layout.activity_update_phone)
public class UpdatePhoneActivity extends BaseActivity implements UpdatePhoneContract.View {
    public static final String TAG = UpdatePhoneActivity.class.getSimpleName();
    public UpdatePhoneContract.Presenter presenter;

    // 验证码输入框
    @ViewInject(R.id.update_phone_smscode_edit)
    private EditText smsCodeEdit;
    // 获取验证码按钮
    @ViewInject(R.id.update_phone_obtain_smscode_edit)
    private Button obtainSmsCodeBtn;
    // 手机号输入框
    @ViewInject(R.id.update_phone_edit)
    private EditText phoneEdit;

    // 倒计时
    private TimeCount timeCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        timeCount = new TimeCount(60000, 1000);

        findViewById(R.id.bar_back).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        obtainSmsCodeBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                presenter.obtainSmsCode();
            }
        });

        findViewById(R.id.update_phone_comple_btn).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                presenter.completeUpdate();
                Intent intent = new Intent();
                intent.setClass(UpdatePhoneActivity.this, UpdatePhonePassword.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (presenter == null) {
            presenter = new UpdatePhonePresenter(this);
        }
        presenter.start();
    }


    @Override
    public String getMyPhone() {
        return application.getLoginApplication().getTelephone();
    }

    @Override
    public String getSmsCode() {
        return smsCodeEdit.getText().toString().trim();
    }

    @Override
    public String getNewPhone() {
        return phoneEdit.getText().toString().trim();
    }

    @Override
    public void callbackSmsCode(int smsCode) {
        obtainSmsCodeBtn.setClickable(false);
        obtainSmsCodeBtn.setTextColor(Color.parseColor("#989898"));
        obtainSmsCodeBtn.setBackgroundResource(R.drawable.login_registered_press);
        timeCount.start();
    }

    @Override
    public void callbackUpdateSuccess() {
        ToastTool.showToastLong(this, "修改成功,请重新登录");
        Intent intent = new Intent(this, WelcomeLoginActivity.class);
        startActivity(intent);
        SharedPreTool.setSharedPreDateString(this, "loginResult", null);
        SharedPreTool.setSharedPreDateString(this, "loginPhonenumber", null);
        SharedPreTool.setSharedPreDateString(this, "loginArticleCode", null);
        application.setLoginApplication(null);
        if (SecurityCenterActivity.activity != null) {
            SecurityCenterActivity.activity.finish();
        }
        if (PersonalCenterActivity.activity != null) {
            PersonalCenterActivity.activity.finish();
        }
        if (MoreSettingsActivity.activity != null) {
            MoreSettingsActivity.activity.finish();
        }
        if (HomeActivity.activity != null) {
            HomeActivity.activity.finish();
        }
        finish();
    }

    @Override
    public void showError(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
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
            obtainSmsCodeBtn.setText("(" + (millisUntilFinished / 1000) + ")s重新获取");
        }

        @Override
        public void onFinish() {
            obtainSmsCodeBtn.setClickable(true);
            obtainSmsCodeBtn.setText("重新获取");
            obtainSmsCodeBtn.setBackgroundResource(R.drawable.login_registered_liftup);
            obtainSmsCodeBtn.setTextColor(Color.parseColor("#ffffff"));
        }
    }

}
