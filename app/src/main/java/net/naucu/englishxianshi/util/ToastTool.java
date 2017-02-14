package net.naucu.englishxianshi.util;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.myTool.tool.DensityTool;

import net.naucu.englishxianshi.R;

/**
 * 类名: ToastTool.java
 * 描述: TODO 通知提示工具类
 * 作者: youyou_pc
 * 时间: 2015年11月14日  下午3:26:36
 */
public class ToastTool {
    private static Toast toast;

    /**
     * 自定义时间
     *
     * @param context
     * @param msg
     */
    public static void showToastLong(final Context context, String msg) {
        if (context == null) return;
        if (toast != null) {
            toast.cancel();
        }

        toast = new Toast(context);
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);
        layout.setBackgroundResource(R.drawable.loadingdialog_bg);
        layout.setPadding(DensityTool.dip2px(context, 10), DensityTool.dip2px(context, 10), DensityTool.dip2px(context, 10), DensityTool.dip2px(context, 10));
        TextView temp = new TextView(context);
        temp.setTextColor(Color.WHITE);
        temp.setTextSize(24);
        temp.setText(msg);
        layout.addView(temp, 0);
        layout.setLayoutParams(new LinearLayout.LayoutParams(DensityTool.dip2px(context, 150), DensityTool.dip2px(context, 150)));
        toast.setView(layout);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }
}
