package net.naucu.englishxianshi.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 类名: SharedPreTool.java
 * 描述: 轻量级存取
 * 作者: youyou_pc
 * 时间: 2015年12月3日  上午10:32:22
 */
public class SharedPreTool {
    public final static String NAME = "englishxianshi_share";
    public final static String TEXT_SIZE = "textSize";
    public final static String IS_BEGIN_STOP = "isBeginStop";
    public final static String IS_END_STOP = "isEndStop";
    public final static String IS_TIME_LIMIT = "isTimeLimit";
    public final static String MYRECORD = "myRecord";
    // 保存数据String
    public static void setSharedPreDateString(Context context, String key, String text) {
        SharedPreferences sp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        sp.edit().putString(key, text).commit();
    }

    // 取出数据String
    public static String getSharedPreDateString(Context context, String key, String defValue) {
        SharedPreferences sp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);

        return sp.getString(key, defValue);
    }

    // 保存数据Boolean
    public static void setSharedPreDateBoolean(Context context, String key, boolean is) {
        SharedPreferences sp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, is).commit();
    }

    // 取出数据Boolean
    public static boolean getSharedPreDateBoolean(Context context, String key, boolean defValue) {
        SharedPreferences sp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);

        return sp.getBoolean(key, defValue);
    }

    // 保存数据Int
    public static void setSharedPreDateInt(Context context, String key, int number) {
        SharedPreferences sp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        sp.edit().putInt(key, number).commit();
    }

    // 取出数据Int
    public static int getSharedPreDateInt(Context context, String key, int defValue) {
        SharedPreferences sp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);

        return sp.getInt(key, defValue);
    }
}
