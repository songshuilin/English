package net.naucu.englishxianshi.ui.account;

import net.naucu.englishxianshi.ui.base.BasePresenter;
import net.naucu.englishxianshi.ui.base.BaseView;

public interface AccountContract {

    interface View extends BaseView {

        /**
         * 手机号
         *
         * @return
         */
        String getPhone();

        /**
         * 验证码
         *
         * @return
         */
        String getSmsCode();

        /**
         * 新密码
         *
         * @return
         */
        String getNewPassword();

        /**
         * 确认密码
         *
         * @return
         */
        String getCompletePassword();

        void callbackSmsCode(int smsCode);

        void callbackNextSuccess();

        void callbackCompleteSuccess();

    }

    interface Presenter extends BasePresenter {

        /**
         *
         */
        void clearSmsCode();

        /**
         * 点击获取验证码
         */
        void clickObtainSmsmCode();

        /**
         * 点击下一步
         *
         * @param
         */
        void nextClick();

        /**
         * 点击完成
         *
         * @param
         */
        void completeClick();

    }
}
