package net.naucu.englishxianshi.ui.video.window;

import android.annotation.SuppressLint;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.PopupWindow;

import net.naucu.englishxianshi.R;
import net.naucu.englishxianshi.ui.video.PlayConfig;
import net.naucu.englishxianshi.ui.video.PlaySubtitleActivity;
import net.naucu.englishxianshi.ui.video.window.event.OnWindowStateListener;
import net.naucu.englishxianshi.util.SharedPreTool;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * 角色扮演设置
 *
 * @author Yi
 */
public class SettingRolePlayWindow extends PopupWindow {

    // Activity
    private PlaySubtitleActivity subtitleActivity;
    // 屏幕尺寸对象
    private DisplayMetrics displayMetrics;
    // Window根View
    private View rootView;
    // 录音开始画面暂停
    @ViewInject(R.id.cb_beginstop)
    private CheckBox beginStop;
    // 录音过程是否限制时间
    @ViewInject(R.id.cb_timelimit)
    private CheckBox timelimit;
    // 每句录音完毕是否暂停试听
    @ViewInject(R.id.cb_endstop)
    private CheckBox endstop;

    // Window状态监听器
    private OnWindowStateListener onWindowStateListener;

    @SuppressWarnings("deprecation")
    public SettingRolePlayWindow(PlaySubtitleActivity subtitleActivity) {
        this.subtitleActivity = subtitleActivity;
        // 初始化
        init();
        // 初始化View
        initView();
        //
        setFocusable(true);
        setBackgroundDrawable(new BitmapDrawable());
    }

    /**
     * 初始化
     */
    private void init() {
        // 初始化屏幕尺寸对象
        displayMetrics = new DisplayMetrics();
        subtitleActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        // 设置Window显示大小
        int width = displayMetrics.widthPixels / 3;
        setWidth(width * 2 + 100);
        setHeight((int) (displayMetrics.density * 130));
    }

    /**
     * 初始化数据
     */
    private void initData() {
        beginStop.setChecked(PlayConfig.isBeginPlayPause);
        timelimit.setChecked(PlayConfig.isRecordLimitTime);
        endstop.setChecked(PlayConfig.isCompletePausePlay);
    }

    /**
     * 初始化View
     */
    @SuppressLint("InflateParams")
    private void initView() {
        rootView = LayoutInflater.from(subtitleActivity).inflate(R.layout.pop_rollplayset, null);
        setContentView(rootView);
        x.view().inject(this, rootView);

        // 初始化数据
        initData();
        // 录音开始前画面是否暂停，
        beginStop.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PlayConfig.isBeginPlayPause = isChecked;
                SharedPreTool.setSharedPreDateBoolean(subtitleActivity, "isBeginStop", isChecked);
            }
        });
        // 录音过程中是否限制时间
        timelimit.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PlayConfig.isRecordLimitTime = isChecked;
                SharedPreTool.setSharedPreDateBoolean(subtitleActivity, "isTimeLimit", isChecked);
            }
        });
        // 每句录完后是否暂停试听
        endstop.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PlayConfig.isCompletePausePlay = isChecked;
                SharedPreTool.setSharedPreDateBoolean(subtitleActivity, "isEndStop", isChecked);
            }
        });
    }

    /**
     * 设置Window状态监听器
     *
     * @param onWindowStateListener
     */
    public void setOnWindowStateListener(OnWindowStateListener onWindowStateListener) {
        this.onWindowStateListener = onWindowStateListener;
    }

    /**
     * 打开Window
     */
    public void showWindow() {
        if (onWindowStateListener != null) {
            onWindowStateListener.windowOpen();
        }
        showAtLocation(subtitleActivity.findViewById(R.id.iv_rollPlay), Gravity.CENTER, 0,
                (int) (150 * displayMetrics.density));
    }

    /**
     * 关闭Window
     */
    public void closeWindow() {
        if (onWindowStateListener != null) {
            onWindowStateListener.windowExternalClose();
        }
    }
}
