package net.naucu.englishxianshi.util;

import android.content.Context;

/**
 * 类名: ErrorTool.java
 * 描述: TODO 错误码返回页面
 * 作者: youyou_pc
 * 时间: 2015年12月11日  上午11:34:03
 */
public class ErrorTool {
    public static void onError(final Context context, boolean isOnCallback) {
        if (context == null) return;
        if (isOnCallback) {
            ToastTool.showToastLong(context, "服务器错误!");
        } else {
            ToastTool.showToastLong(context, "网络错误,请检查网络!");
        }
    }


}
