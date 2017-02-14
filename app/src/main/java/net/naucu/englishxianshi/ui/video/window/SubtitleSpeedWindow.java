package net.naucu.englishxianshi.ui.video.window;

import android.annotation.SuppressLint;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import net.naucu.englishxianshi.R;
import net.naucu.englishxianshi.ui.video.PlaySubtitleActivity;
import net.naucu.englishxianshi.ui.video.window.event.OnSubtitleChangeFontSizeListener;
import net.naucu.englishxianshi.ui.video.window.event.OnWindowStateListener;
import net.naucu.englishxianshi.util.SharedPreTool;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * 字体大小
 *
 * @author Yi
 */
public class SubtitleSpeedWindow extends PopupWindow implements SubtitleSettingWindow.closePopwindowCallBack {

    // Activity
    private PlaySubtitleActivity subtitleActivity;
    // 屏幕尺寸对象
    private DisplayMetrics displayMetrics;
    // Window根View
    private View rootView;
    // 语速慢
    @ViewInject(R.id.rl_man_video)
    private RelativeLayout manLayout;
    @ViewInject(R.id.iv_man_video)
    private ImageView manImage;
    // 语速中
    @ViewInject(R.id.rl_zhong_video)
    private RelativeLayout zhongLayout;
    @ViewInject(R.id.iv_zhong_video)
    private ImageView zhongImage;
    // 语速快
    @ViewInject(R.id.rl_kuai_video)
    private RelativeLayout kuaiLayout;
    @ViewInject(R.id.iv_kuai_video)
    private ImageView kuaiImage;

    // Window状态监听器
    private OnWindowStateListener onWindowStateListener;
    // 字体大小改变监听器
    private OnSubtitleChangeFontSizeListener onSubtitleChangeFontSizeListener;

    @SuppressWarnings("deprecation")
    public SubtitleSpeedWindow(PlaySubtitleActivity subtitleActivity) {
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
        setWidth(LayoutParams.MATCH_PARENT);
        setHeight(LayoutParams.WRAP_CONTENT);
    }

    /**
     * 初始化View
     */
    @SuppressLint("InflateParams")
    private void initView() {
        rootView = LayoutInflater.from(subtitleActivity).inflate(R.layout.pop_speed, null);
        setContentView(rootView);
        x.view().inject(this, rootView);
        switch (SharedPreTool.getSharedPreDateInt(subtitleActivity, "EadLanguageShared", 50)) {
            case 0:
                manImage.setVisibility(View.VISIBLE);
                zhongImage.setVisibility(View.GONE);
                kuaiImage.setVisibility(View.GONE);
                break;
            case 50:
                manImage.setVisibility(View.GONE);
                zhongImage.setVisibility(View.VISIBLE);
                kuaiImage.setVisibility(View.GONE);
                break;
            case 100:
                manImage.setVisibility(View.GONE);
                zhongImage.setVisibility(View.GONE);
                kuaiImage.setVisibility(View.VISIBLE);
                break;
        }
        manLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreTool.setSharedPreDateInt(subtitleActivity, "EadLanguageShared", 0);
                manImage.setVisibility(View.VISIBLE);
                zhongImage.setVisibility(View.GONE);
                kuaiImage.setVisibility(View.GONE);
                dismiss();
            }
        });
        zhongLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreTool.setSharedPreDateInt(subtitleActivity, "EadLanguageShared", 50);
                manImage.setVisibility(View.GONE);
                zhongImage.setVisibility(View.VISIBLE);
                kuaiImage.setVisibility(View.GONE);
                dismiss();
            }
        });
        kuaiLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreTool.setSharedPreDateInt(subtitleActivity, "EadLanguageShared", 100);
                manImage.setVisibility(View.GONE);
                zhongImage.setVisibility(View.GONE);
                kuaiImage.setVisibility(View.VISIBLE);
                dismiss();
            }
        });
    }

    /**
     * 打开Window
     */
    public void showWindow() {
        if (onWindowStateListener != null) {
            onWindowStateListener.windowOpen();
        }
        showAtLocation(subtitleActivity.findViewById(R.id.iv_moreset_video), Gravity.BOTTOM, 0, (int) (200 * displayMetrics.density));
    }

    @Override
    public void close() {
        this.dismiss();
    }
}
