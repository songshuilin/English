package net.naucu.englishxianshi.ui;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Y on 2016/11/29.
 */
public class HttpSubmitFeedBack implements Runnable {
    public static final int HTTP_200 = 200;
    public static String type;
    public static FeedBackCallback feedBackCallback;
    public static String json;

    public HttpSubmitFeedBack(String type , FeedBackCallback feedBackCallback) {
        this.type = type;
        this.feedBackCallback = feedBackCallback;
    }

    public void requestByGet() throws Exception {
        String path = "https://naucu.com:8443/englishStudy/feedback?info={'message':'"+type+"'}";
        URL url = new URL(path);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setConnectTimeout(5 * 1000);
        urlConnection.connect();
        if (urlConnection.getResponseCode() == HTTP_200) {
            byte[] jsonData = readStream(urlConnection.getInputStream());
            json = new String(jsonData);
            feedBackCallback.feedBackCallback(json);
        } else {
            Log.i("请求失败!", "...");
        }
        // 关闭连接
        urlConnection.disconnect();
    }
    private static byte[] readStream(InputStream inputStream) throws Exception {
        byte[] buffer = new byte[1024];
        int len = -1;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            baos.write(buffer, 0, len);
        }
        byte[] data = baos.toByteArray();
        inputStream.close();
        baos.close();
        return data;
    }

    @Override
    public void run() {
        try {
            requestByGet();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
