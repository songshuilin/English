package com.lijunsai.httpInterface.tool;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationTool {
    /**
     * 验证手机号
     *
     * @param telephone
     * @return
     */
    public static boolean ValidationTelephone(String telephone) {
        Pattern p = Pattern.compile("((13[\\d])|(147)|(15[\\d])|(18[\\d])|(17([6-8]|0)))\\d{8}");
        Matcher m = p.matcher(telephone);
        return m.matches();
    }
}
