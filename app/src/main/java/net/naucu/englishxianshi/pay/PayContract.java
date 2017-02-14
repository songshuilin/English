package net.naucu.englishxianshi.pay;

import android.app.Activity;

import net.naucu.englishxianshi.BaseApplication;
import net.naucu.englishxianshi.ui.base.BasePresenter;
import net.naucu.englishxianshi.ui.base.BaseView;

public interface PayContract {

    interface View extends BaseView {

        BaseApplication getApp();

        Activity getActivity();

        String getActiveCode();

        void callbakcMoney(int money);

        void callbackPaySuccess();
    }

    interface Presenter extends BasePresenter {

        void payTreasure();

        void weChat();
    }
}
