package net.naucu.englishxianshi.ui.account.data.impl;

import com.lijunsai.httpInterface.implementation.HttpImplementation;

import net.naucu.englishxianshi.ui.account.data.IUserInfoDao;
import net.naucu.englishxianshi.ui.account.data.ResponseListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

public class IUserInfoDaoImpl implements IUserInfoDao {
    public static final String TAG = IUserInfoDao.class.getSimpleName();

    @Override
    public void registerSmsCode(String telephone, final ResponseListener<Integer> responseListener) {
        String url = HttpImplementation.getHttpUrl() + "zcUser";
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("info", "{'telephone':'" + telephone + "'}");
        x.http().get(params, new CommonCallback<String>() {
            @Override
            public void onCancelled(CancelledException arg0) {
            }

            @Override
            public void onError(Throwable throwable, boolean arg1) {
                if (throwable instanceof HttpException) {
                    HttpException httpException = (HttpException) throwable;
                    responseListener.faliure(httpException.getCode(), "网络状况不佳，请稍后重试");
                } else {
                    responseListener.faliure(-1, "网络状况不佳，请稍后重试");
                }
            }

            @Override
            public void onFinished() {
            }

            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int errCode = jsonObject.optInt("errCode");
                    if (errCode == 10000) {
                        responseListener.success(jsonObject.optInt("msg"));
                    } else {
                        responseListener.faliure(errCode, jsonObject.optString("msg"));
                    }
                } catch (JSONException e) {
                    responseListener.faliure(-2, "网络状况不佳，请稍后重试");
                }
            }
        });
    }

    @Override
    public void registerUser(String telephone, String password, int smsCode, String szImei,
                             final ResponseListener<Integer> responseListener) {
        String url = HttpImplementation.getHttpUrl() + "userRegister";
        RequestParams params = new RequestParams(url);
        String jsonParams = "{'telephone':'" + telephone + "','password':'" + password + "','code':'" + smsCode + "','serialnumber':'" + szImei + "'}";
        params.addBodyParameter("info", jsonParams);
        x.http().get(params, new CommonCallback<String>() {
            @Override
            public void onCancelled(CancelledException arg0) {
            }

            @Override
            public void onError(Throwable throwable, boolean arg1) {
                if (throwable instanceof HttpException) {
                    HttpException httpException = (HttpException) throwable;
                    responseListener.faliure(httpException.getCode(), "网络状况不佳，请稍后重试");
                } else {
                    responseListener.faliure(-1, "网络状况不佳，请稍后重试");
                }
            }

            @Override
            public void onFinished() {
            }

            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int errCode = jsonObject.optInt("errCode");
                    if (errCode == 10003) {
                        responseListener.success(jsonObject.optInt("msg"));
                    } else {
                        responseListener.faliure(errCode, jsonObject.optString("msg"));
                    }
                } catch (JSONException e) {
                    responseListener.faliure(-2,"网络状况不佳，请稍后重试");
                }
            }
        });
    }

    @Override
    public void updatePasswordSmsmCode(String telephone, final ResponseListener<Integer> responseListener) {
        String url = HttpImplementation.getHttpUrl() + "wjPassword";
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("info", "{'telephone':" + telephone + "}");
        x.http().get(params, new CommonCallback<String>() {

            @Override
            public void onCancelled(CancelledException arg0) {
            }

            @Override
            public void onError(Throwable throwable, boolean arg1) {
                if (throwable instanceof HttpException) {
                    HttpException httpException = (HttpException) throwable;
                    responseListener.faliure(httpException.getCode(), "网络状况不佳，请稍后重试");
                } else {
                    responseListener.faliure(-1, "网络状况不佳，请稍后重试");
                }
            }

            @Override
            public void onFinished() {
            }

            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int errCode = jsonObject.optInt("errCode");
                    if (errCode == 10000) {
                        responseListener.success(jsonObject.optInt("msg"));
                    } else {
                        responseListener.faliure(errCode, jsonObject.optString("msg"));
                    }
                } catch (JSONException e) {
                    responseListener.faliure(-2, "网络状况不佳或，请稍后重试");//json请求失败
                }
            }
        });
    }

    @Override
    public void updatePassword(String telephone, String password, int smsCode,
                               final ResponseListener<Integer> responseListener) {
        String url = HttpImplementation.getHttpUrl() + "userchangePassword";
        RequestParams params = new RequestParams(url);
        String jsonParams = "{'telephone':'" + telephone + "','password':'" + password + "','code':'" + smsCode + "'}";
        params.addBodyParameter("info", jsonParams);
        x.http().get(params, new CommonCallback<String>() {
            @Override
            public void onCancelled(CancelledException arg0) {
            }

            @Override
            public void onError(Throwable throwable, boolean arg1) {
                if (throwable instanceof HttpException) {
                    HttpException httpException = (HttpException) throwable;
                    responseListener.faliure(httpException.getCode(),"系统繁忙，请稍后重试");
                } else {
                    responseListener.faliure(-1, "系统繁忙，请稍后重试");
                }
            }

            @Override
            public void onFinished() {
            }

            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int errCode = jsonObject.optInt("errCode");
                    if (errCode == 10000) {
                        responseListener.success(jsonObject.optInt("msg"));
                    } else {
                        responseListener.faliure(errCode, jsonObject.optString("msg"));
                    }
                } catch (JSONException e) {
                    responseListener.faliure(-2, "网络状况不佳，请稍后重试");
                }
            }
        });
    }

    @Override
    public void updatePhone(String myTelephone, String newTelephone, final ResponseListener<Integer> responseListener) {
        String url = HttpImplementation.getHttpUrl() + "updateUserTele";
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("info", "{'teleNew':'" + newTelephone + "','teleOld':'" + myTelephone + "'}");
        x.http().get(params, new CommonCallback<String>() {

            @Override
            public void onCancelled(CancelledException arg0) {
            }

            @Override
            public void onError(Throwable throwable, boolean arg1) {
                if (throwable instanceof HttpException) {
                    HttpException httpException = (HttpException) throwable;
                    responseListener.faliure(httpException.getCode(), "网络状况不佳，请稍后重试");
                } else {
                    responseListener.faliure(-1, "网络状况不佳，请稍后重试");
                }
            }

            @Override
            public void onFinished() {
            }

            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int errCode = jsonObject.optInt("errCode");
                    if (errCode == 10000) {
                        responseListener.success(jsonObject.optInt("msg"));
                    } else {
                        responseListener.faliure(errCode, jsonObject.optString("msg"));
                    }
                } catch (JSONException e) {
                    responseListener.faliure(-2, "网络状况不佳，请稍后重试");
                }
            }
        });
    }

}
