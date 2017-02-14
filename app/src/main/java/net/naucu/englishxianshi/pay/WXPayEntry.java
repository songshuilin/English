package net.naucu.englishxianshi.pay;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;

import com.app.myTool.tool.Util;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import net.naucu.englishxianshi.widget.dialog.LoadingDialog;
import net.naucu.englishxianshi.util.MD5;
import net.naucu.englishxianshi.util.ToastTool;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;

import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class WXPayEntry {
    public static final String TAG = WXPayEntry.class.getSimpleName();
    private Context context;
    private PayReq req;
    private IWXAPI msgApi;
    private GetPrepayIdTask getPrepayId;
    private Map<String, String> resultunifiedorder;
    private StringBuffer sb;
    private String ordername;
    private String money;
    private LoadingDialog loadDialog;

    public WXPayEntry(Context context, String ordername, String money) {
        this.context = context;
        this.ordername = ordername;
        this.money = money;
        init();
    }

    private void init() {
        msgApi = WXAPIFactory.createWXAPI(context, null);
        if (msgApi.isWXAppInstalled()) {
            loadDialog = new LoadingDialog(context);
            loadDialog.setContent("\u8df3\u8f6c\u652f\u4ed8\u4e2d...");
            loadDialog.show();
            req = new PayReq();
            sb = new StringBuffer();
            msgApi.registerApp(Constants.APP_ID);
            GetPrepayIdTask getPrepayId = new GetPrepayIdTask();
            getPrepayId.execute();
        } else {
            ToastTool.showToastLong(context, "\u5c1a\u672a\u5b89\u88c5\u5fae\u4fe1");
        }
    }

    private class GetPrepayIdTask extends AsyncTask<Void, Void, Map<String, String>> {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(Map<String, String> result) {
            resultunifiedorder = result;
            genPayReq();
//			String packageSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
            sendPayReq();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected Map<String, String> doInBackground(Void... params) {

            String url = String.format("https://api.mch.weixin.qq.com/pay/unifiedorder");
            String entity = genProductArgs();
            byte[] buf = Util.httpPost(url, entity);
            String content = new String(buf);
            Map<String, String> xml = decodeXml(content);
            return xml;
        }
    }

    private String genProductArgs() {
        StringBuffer xml = new StringBuffer();

        try {
            String nonceStr = genNonceStr();
            xml.append("</xml>");
            List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
            packageParams
                    .add(new BasicNameValuePair("appid", Constants.APP_ID));
            packageParams.add(new BasicNameValuePair("body", ordername));
            packageParams
                    .add(new BasicNameValuePair("mch_id", Constants.MCH_ID));
            packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
            packageParams.add(new BasicNameValuePair("notify_url", "http://weixin.qq.com"));
            packageParams.add(new BasicNameValuePair("out_trade_no", genOutTradNo()));
            packageParams.add(new BasicNameValuePair("spbill_create_ip", "127.0.0.1"));
            packageParams.add(new BasicNameValuePair("total_fee", money));
            packageParams.add(new BasicNameValuePair("trade_type", "APP"));
            String sign = genPackageSign(packageParams);
            packageParams.add(new BasicNameValuePair("sign", sign));

            String xmlstring = toXml(packageParams);

            return new String(xmlstring.toString().getBytes(), "ISO8859-1");

        } catch (Exception e) {
            return null;
        }
    }

    private String genPackageSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(Constants.API_KEY);
        String packageSign = MD5.getMessageDigest(sb.toString().getBytes())
                .toUpperCase();
        return packageSign;
    }

    private String genAppSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(Constants.API_KEY);
        this.sb.append("sign str\n" + sb.toString() + "\n\n");
        String appSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
        return appSign;
    }

    private String toXml(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();
        sb.append("<xml>");
        for (int i = 0; i < params.size(); i++) {
            sb.append("<" + params.get(i).getName() + ">");

            sb.append(params.get(i).getValue());
            sb.append("</" + params.get(i).getName() + ">");
        }
        sb.append("</xml>");

        Log.e("orion", sb.toString());
        return sb.toString();
    }

    private String genNonceStr() {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000))
                .getBytes());
    }

    private long genTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }

    private String genOutTradNo() {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }

    public Map<String, String> decodeXml(String content) {

        try {
            Map<String, String> xml = new HashMap<String, String>();
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(content));
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                String nodeName = parser.getName();
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if ("xml".equals(nodeName) == false) {
                            xml.put(nodeName, parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                event = parser.next();
            }

            return xml;
        } catch (Exception e) {
            Log.e("orion", e.toString());
        }
        return null;

    }

    private void sendPayReq() {
        msgApi.registerApp(Constants.APP_ID);
        msgApi.sendReq(req);
        loadDialog.dismiss();
    }

    private void genPayReq() {
        req.appId = Constants.APP_ID;
        req.partnerId = Constants.MCH_ID;
        req.prepayId = resultunifiedorder.get("prepay_id");
        req.packageValue = "Sign=WXPay";
        req.nonceStr = genNonceStr();
        req.timeStamp = String.valueOf(genTimeStamp());
        List<NameValuePair> signParams = new LinkedList<NameValuePair>();
        signParams.add(new BasicNameValuePair("appid", req.appId));
        signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
        signParams.add(new BasicNameValuePair("package", req.packageValue));
        signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
        signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
        signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));
        req.sign = genAppSign(signParams);
        sb.append("sign\n" + req.sign + "\n\n");
    }

}
