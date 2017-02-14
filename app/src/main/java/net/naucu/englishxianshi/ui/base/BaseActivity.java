package net.naucu.englishxianshi.ui.base;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import net.naucu.englishxianshi.BaseApplication;
import net.naucu.englishxianshi.NetBroadcastReceiver;
import net.naucu.englishxianshi.R;

import cn.jpush.android.api.JPushInterface;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * 类名: BaseActivity.java
 * 描述: 子Activity的父类
 * 作者: youyou_pc
 * 时间: 2015年11月16日  上午9:26:37
 */
public class BaseActivity extends FragmentActivity implements NetBroadcastReceiver.NetEvevt{
    public static BaseApplication application;
    public static NetBroadcastReceiver.NetEvevt evevt;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        application = (BaseApplication) getApplication();
        evevt = this;


    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    public void isnodate(boolean b) {
        LinearLayout ll_aredata = (LinearLayout) findViewById(R.id.aredate);
        LinearLayout ll_nodata = (LinearLayout) findViewById(R.id.nodata);
        if (b) {
            ll_aredata.setVisibility(View.VISIBLE);
            ll_nodata.setVisibility(View.GONE);
        } else {
            ll_nodata.setVisibility(View.VISIBLE);
            ll_aredata.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (isHideInput(view, ev)) {
                HideSoftInput(view.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    // 判定是否需要隐藏
    private boolean isHideInput(View v, MotionEvent ev) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (ev.getX() > left && ev.getX() < right && ev.getY() > top
                    && ev.getY() < bottom) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    // 隐藏软键盘
    private void HideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(token,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onNetChange(int netMobile) {
        AlertDialog.Builder builder;
        switch (netMobile){
            case -1:
                Toast.makeText(this, "网络已断开，请重新加载", Toast.LENGTH_LONG).show();
                break;
            case 0:
                builder = new AlertDialog.Builder(this);
                builder.setTitle("提示"); //设置标题
                builder.setMessage("已切换至3G/4G网络，可能消耗流量，是否继续？"); //设置内容
                builder.setNegativeButton("留在本页", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("继续", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setNeutralButton("忽略", new DialogInterface.OnClickListener() {//设置忽略按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.create().show();

                break;
            case 1:
                Toast.makeText(this, "已切换至Wi-Fi网络", Toast.LENGTH_LONG).show();
                break;
        }

    }
}
