package net.naucu.englishxianshi.util;

import com.jiongbull.jlog.JLog;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SignUtils {
    /**
     * @param
     */
    //final static private String SECRET = "d638d14285973d20b2f854ec9dcef7bf";
    public static String join(List<String> r, String d) {
        if (r.size() == 0) return "";
        StringBuilder sb = new StringBuilder();
        int i;
        for (i = 0; i < r.size() - 1; i++)
            sb.append(r.get(i) + d);
        return sb.toString() + r.get(i);
    }

    public static String sign(Map<String, String> parameter) {

        Object[] keys = parameter.keySet().toArray();

        Arrays.sort(keys);

        ArrayList<String> signArr = new ArrayList<String>();

        for (int i = 0; i < keys.length; i++) {
            String key = (String) keys[i];
            String p = key + "=" + parameter.get(key);
            signArr.add(p);
        }

        String signStr = SignUtils.join(signArr, "") + "android";
        String retValue = "";

        try {
            retValue = MD5.getMessageDigest(signStr.getBytes("UTF-8")).toLowerCase();
        } catch (UnsupportedEncodingException e) {
            JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());
        }

        return retValue;
    }

}
