package net.naucu.englishxianshi.ui.account;

import android.text.TextUtils;

import com.lijunsai.httpInterface.tool.ValidationTool;

import net.naucu.englishxianshi.ui.account.AccountContract.View;
import net.naucu.englishxianshi.ui.account.data.IUserInfoDao;
import net.naucu.englishxianshi.ui.account.data.ResponseListener;
import net.naucu.englishxianshi.ui.account.data.impl.IUserInfoDaoImpl;

public class AccountPresenter implements AccountContract.Presenter {

    private AccountContract.View view;
    private int optType;
    // 验证码
    private int smsCode;
    private IUserInfoDao iUserInfoDao;
    public String szImei;

    public AccountPresenter(View view, int optType, String szImei) {
        this.view = view;
        this.optType = optType;
        //
        this.szImei = szImei;
        iUserInfoDao = new IUserInfoDaoImpl();
    }

    @Override
    public void start() {

    }

    @Override
    public void clickObtainSmsmCode() {
        if (TextUtils.isEmpty(view.getPhone())) {
            view.showError("请输入手机号");
            return;
        }
        if (!ValidationTool.ValidationTelephone(view.getPhone())) {
            view.showError("请输入正确的手机号码");
            return;
        }
        switch (optType) {
            case 1:
                iUserInfoDao.registerSmsCode(view.getPhone(), smsCodeResponseListener);
                break;
            case 2:
                iUserInfoDao.updatePasswordSmsmCode(view.getPhone(), smsCodeResponseListener);
                break;
            case 3:
                iUserInfoDao.updatePasswordSmsmCode(view.getPhone(), smsCodeResponseListener);
                break;
        }
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
    public void nextClick() {

        if (TextUtils.isEmpty(view.getPhone())) {
            view.showError("请输入手机号");
            return;
        }
        if (!ValidationTool.ValidationTelephone(view.getPhone())) {
            view.showError("请输入正确的手机号码");
            return;
        }
        if (TextUtils.isEmpty(view.getSmsCode())) {
            view.showError("请输入验证码");
            return;
        }
        if (!view.getSmsCode().equals(String.valueOf(smsCode))) {
            view.showError("验证码错误");
            return;
        }
        view.callbackNextSuccess();

    }

    @Override
    public void completeClick() {
        /**
         * 非注册模式下
         * */
        if (optType == 2 || optType == 3) {
            if (TextUtils.isEmpty(view.getNewPassword())) {
                view.showError("请输入密匙");
                return;
            }
            if (TextUtils.isEmpty(view.getCompletePassword())) {
                view.showError("请输入确认密匙");
                return;
            }
            if (!view.getNewPassword().equals(view.getCompletePassword())) {
                view.showError("两次密匙输入不一致");
                return;
            }
            switch (optType) {
                case 2:
                    iUserInfoDao.updatePassword(view.getPhone(), view.getNewPassword(), smsCode, responseListener);
                    break;
                case 3:
                    iUserInfoDao.updatePassword(view.getPhone(), view.getNewPassword(), smsCode, responseListener);
                    break;
            }
        } else {
            if (TextUtils.isEmpty(view.getNewPassword())) {
                view.showError("请输入密钥");
            }
            switch (optType) {
                case 1:
                default:
                    iUserInfoDao.registerUser(view.getPhone(), view.getNewPassword(), Integer.parseInt(view.getSmsCode()), szImei,
                            responseListener);
                    break;

            }
        }
    }

    private ResponseListener<Integer> responseListener = new ResponseListener<Integer>() {

        @Override
        public void success(Integer result) {
            view.callbackCompleteSuccess();
        }

        @Override
        public void faliure(int code, String msg) {
            view.showError(msg);
        }
    };

    @Override
    public void clearSmsCode() {
        smsCode = 0;
    }

}
