package net.naucu.englishxianshi.pay;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.alipay.sdk.app.PayTask;
import com.jiongbull.jlog.JLog;
import com.lijunsai.httpInterface.implementation.HttpImplementation;

import net.naucu.englishxianshi.util.PayResult;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;

public class PayPresenter implements PayContract.Presenter {
    public static final String TAG = PayPresenter.class.getSimpleName();
    private PayContract.View view;

    private int money;
    // Handler
    public static PayHandler handler;

    public PayPresenter(PayContract.View view) {
        this.view = view;
        // 初始化 Handler
        handler = new PayHandler(this);
    }

    @Override
    public void start() {
        RequestParams params = new RequestParams(HttpImplementation.selectPrice());
        x.http().get(params, new CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonResult = new JSONObject(result);
                    if (!jsonResult.isNull("activeMoney")) {
                        money = jsonResult.optInt("activeMoney");
                    }
                    view.callbakcMoney(money);
                } catch (JSONException e) {
                    JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());
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

    @Override
    public void payTreasure() {
        RequestParams params = new RequestParams(HttpImplementation.getPaytreasure());
        params.addBodyParameter("info", "{'id':'" + view.getApp().getLoginApplication().getUid() + "'}");
        x.http().get(params, new CommonCallback<String>() {

            @Override
            public void onCancelled(CancelledException e) {
                JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());
            }

            @Override
            public void onError(Throwable throwable, boolean arg1) {
                view.showError("网络异常,请稍后再试");
                JLog.e((throwable == null || throwable.getMessage() == null) ? "Throwable is null" : throwable.getMessage());
            }

            @Override
            public void onFinished() {
                // TODO Auto-generated method stub

            }

            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonResult = new JSONObject(result);
                    if (jsonResult.isNull("payOrder")) {
                        view.showError("网络异常,请稍后再试");
                    } else {
                        final String payOrder = jsonResult.optString("payOrder");

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                PayTask payTask = new PayTask(view.getActivity());
                                // 开始支付
                                String payResult = payTask.pay(payOrder);
                                Log.e(TAG, payResult);
                                Message message = handler.obtainMessage();
                                message.what = PayHandler.WHAT_PAY_TREASURE;
                                message.obj = payResult;
                                handler.sendMessage(message);
                            }
                        }).start();
                    }
                } catch (JSONException e) {
                    view.showError("网络异常,请稍后再试");
                    JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());
                }

            }
        });
    }

    @Override
    public void weChat() {
        new WXPayEntry(view.getActivity(), "测试", String.valueOf(money * 10));
    }

    /**
     * Handler
     *
     * @author Y
     */
    public static class PayHandler extends Handler {
        private final WeakReference<PayPresenter> reference;

        public PayHandler(PayPresenter presenter) {
            reference = new WeakReference<>(presenter);
        }

        // 支付宝支付
        public static final int WHAT_PAY_TREASURE = 11;
        //微信支付成功
        public static final int WHAT_WEI_CHAT_SUCCESS = 12;
        //微信支付失败
        public static final int WHAT_WEI_CHAT_FAILURE = 13;

        @Override
        public void handleMessage(Message msg) {
            if (reference.get() == null)
                return;
            switch (msg.what) {
                case WHAT_PAY_TREASURE:
                    if (msg.obj == null) {
                        reference.get().view.showError("支付失败");
                    } else {
                        payTresureResult(String.valueOf(msg.obj));
                    }
                    break;
                case WHAT_WEI_CHAT_SUCCESS:
                    weChatResult();
                    break;
                case WHAT_WEI_CHAT_FAILURE:
                    reference.get().view.showError("支付失败");
                    break;
                default:
                    break;
            }
        }

        /**
         * 支付宝支付结果处理
         *
         * @param result
         */
        private void payTresureResult(String result) {
            PayResult payResult = new PayResult(result);

            if (!payResult.getResultStatus().equals("9000")) {
                reference.get().view.showError("支付失败");
                return;
            }
            RequestParams params = new RequestParams(HttpImplementation.getPayreturn());
            params.addBodyParameter("info",
                    "{"
                            + "'userid':'" + reference.get().view.getApp().getLoginApplication().getUid() + "',"
                            + "'resultStatus':'" + payResult.getResultStatus() + "',"
                            + "'activeCode':'" + reference.get().view.getActiveCode() + "',"
                            + "'result':'" + payResult.getResult() + "',"
                            + "'telephone':'" + reference.get().view.getApp().getLoginApplication().getTelephone() + "',"
                            + "'username':'" + reference.get().view.getApp().getLoginApplication().getUsername() + "'"
                            + "}");
            x.http().get(params, new CommonCallback<String>() {

                @Override
                public void onCancelled(CancelledException e) {
                    JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());
                }

                @Override
                public void onError(Throwable throwable, boolean arg1) {
                    reference.get().view.showError("支付失败");
                    JLog.e((throwable == null || throwable.getMessage() == null) ? "Throwable is null" : throwable.getMessage());
                }

                @Override
                public void onFinished() {
                }

                @Override
                public void onSuccess(String arg0) {
                    reference.get().view.callbackPaySuccess();
                }
            });
        }

        /**
         * 微信支付结果处理
         */
        private void weChatResult() {
            RequestParams params = new RequestParams(HttpImplementation.getweiXinPayReturn());
            params.addBodyParameter("info",
                    "{"
                            + "'id':'" + reference.get().view.getApp().getLoginApplication().getUid() + "',"
                            + "'activeCode':'" + reference.get().view.getActiveCode() + "',"
                            + "'username':'" + reference.get().view.getApp().getLoginApplication().getUsername() + "',"
                            + "'telephone':'" + reference.get().view.getApp().getLoginApplication().getTelephone() + "'"
                            + "}");
            x.http().get(params, new CommonCallback<String>() {

                @Override
                public void onCancelled(CancelledException e) {
                    JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());
                }

                @Override
                public void onError(Throwable throwable, boolean arg1) {
                    reference.get().view.showError("支付失败");
                    JLog.e((throwable == null || throwable.getMessage() == null) ? "Throwable is null" : throwable.getMessage());
                }

                @Override
                public void onFinished() {
                }

                @Override
                public void onSuccess(String arg0) {
                    reference.get().view.callbackPaySuccess();
                }
            });
        }
    }

}
