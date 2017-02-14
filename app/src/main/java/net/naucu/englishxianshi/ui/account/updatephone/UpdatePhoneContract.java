package net.naucu.englishxianshi.ui.account.updatephone;

import net.naucu.englishxianshi.ui.base.BasePresenter;
import net.naucu.englishxianshi.ui.base.BaseView;

public interface UpdatePhoneContract {

    interface View extends BaseView {

        String getMyPhone();

        String getSmsCode();

        String getNewPhone();

        void callbackSmsCode(int smsCode);

        void callbackUpdateSuccess();
    }

    interface Presenter extends BasePresenter {

        void obtainSmsCode();

        void completeUpdate();
    }

}
