package net.naucu.englishxianshi.ui.account.updatephone;

import android.text.TextUtils;

import com.lijunsai.httpInterface.tool.ValidationTool;

import net.naucu.englishxianshi.ui.account.data.IUserInfoDao;
import net.naucu.englishxianshi.ui.account.data.ResponseListener;
import net.naucu.englishxianshi.ui.account.data.impl.IUserInfoDaoImpl;
import net.naucu.englishxianshi.ui.account.updatephone.UpdatePhoneContract.View;

public class UpdatePhonePresenter implements UpdatePhoneContract.Presenter {

    private UpdatePhoneContract.View view;
    private IUserInfoDao iUserInfoDao;
    //
    private int smsCode;

    public UpdatePhonePresenter(View view) {
        this.view = view;
        //
        iUserInfoDao = new IUserInfoDaoImpl();
    }

    @Override
    public void start() {

    }

    @Override
    public void obtainSmsCode() {
        iUserInfoDao.updatePasswordSmsmCode(view.getMyPhone(), smsCodeResponseListener);
    }

    private ResponseListener<Integer> smsCodeResponseListener = new ResponseListener<Integer>() {

        @Override
        public void success(Integer result) {
            smsCode = result;
            view.callbackSmsCode(smsCode);
        }

        @Override
        public void faliure(int code, String msg) {
            view.showError(msg);
        }
    };

    @Override
    public void completeUpdate() {
        if (TextUtils.isEmpty(view.getSmsCode())) {
            view.showError("验证码不能为空");
            return;
        }
        if (!view.getSmsCode().equals(String.valueOf(smsCode))) {
            view.showError("验证码输入错误");
            return;
        }
        if (TextUtils.isEmpty(view.getNewPhone())) {
            view.showError("新手机号不能为空");
            return;
        }
        if (!ValidationTool.ValidationTelephone(view.getNewPhone())) {
            view.showError("手机号格式不正确");
            return;
        }
        iUserInfoDao.updatePhone(view.getMyPhone(), view.getNewPhone(), updateResponseListener);
    }

    private ResponseListener<Integer> updateResponseListener = new ResponseListener<Integer>() {

        @Override
        public void success(Integer result) {
            view.callbackUpdateSuccess();
        }

        @Override
        public void faliure(int code, String msg) {
            view.showError(msg);
        }
    };

}
