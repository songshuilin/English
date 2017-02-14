package net.naucu.englishxianshi.ui.video.window;

import android.annotation.SuppressLint;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.lijunsai.httpInterface.tool.SharedPreTool;

import net.naucu.englishxianshi.R;
import net.naucu.englishxianshi.ui.video.PlayConfig;
import net.naucu.englishxianshi.ui.video.PlaySubtitleActivity;
import net.naucu.englishxianshi.ui.video.window.event.OnSubtitleChangeFontSizeListener;
import net.naucu.englishxianshi.ui.video.window.event.OnWindowStateListener;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * 字体大小
 *
 * @author Yi
 */
public class SubtitleFontSizeWindow extends PopupWindow {

    // Activity
    private PlaySubtitleActivity subtitleActivity;
    // 屏幕尺寸对象
    private DisplayMetrics displayMetrics;
    // Window根View
    private View rootView;
    // 小号字体
    @ViewInject(R.id.rl_smalltext_video)
    private RelativeLayout smallFontLayout;
    @ViewInject(R.id.iv_smalltext_video)
    private ImageView smallFontImage;
    // 中号字体
    @ViewInject(R.id.rl_middletext_video)
    private RelativeLayout middleFontLayout;
    @ViewInject(R.id.iv_middletext_video)
    private ImageView middleFontImage;
    // 大号字体
    @ViewInject(R.id.rl_largetext_video)
    private RelativeLayout largeFontLayout;
    @ViewInject(R.id.iv_largetext_video)
    private ImageView largeFontImage;

    // 字体大小
    private int textSize = 20;

    // Window状态监听器
    private OnWindowStateListener onWindowStateListener;
    // 字体大小改变监听器
    private OnSubtitleChangeFontSizeListener onSubtitleChangeFontSizeListener;

    @SuppressWarnings("deprecation")
    public SubtitleFontSizeWindow(PlaySubtitleActivity subtitleActivity) {
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
        rootView = LayoutInflater.from(subtitleActivity).inflate(R.layout.pop_textsize, null);
        setContentView(rootView);
        x.view().inject(this, rootView);
        // 小号字体 Click
        smallFontLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSmallFontClick(v);
            }
        });
        // 中号字体 Click
        middleFontLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                changeMiddleFontClick(v);
            }
        });
        // 大号字体 Click
        largeFontLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLargeFontClick(v);
            }
        });
    }

    /**
     * 清除字体状态
     */
    private void clearFontSizeState() {
        smallFontImage.setVisibility(View.GONE);
        middleFontImage.setVisibility(View.GONE);
        largeFontImage.setVisibility(View.GONE);
    }

    /**
     * 字体状态修改回调
     */
    private void changeFontSizeCallbak() {
        if (onSubtitleChangeFontSizeListener != null) {
            onSubtitleChangeFontSizeListener.chengeSubtitleFontSize();
        }
        if (onWindowStateListener != null) {
            onWindowStateListener.windowInternalClose();
        }
        dismiss();
    }

    /**
     * 小号字体
     *
     * @param view
     */
    private void changeSmallFontClick(View view) {
        clearFontSizeState();
        PlayConfig.movietextSize = 18;
        smallFontImage.setVisibility(View.VISIBLE);
        SharedPreTool.setSharedPreDateInt(subtitleActivity, PlayConfig.MOVIE_TEXT_SIZE, 18);
        changeFontSizeCallbak();
    }

    /**
     * 中号字体
     *
     * @param view
     */
    private void changeMiddleFontClick(View view) {
        clearFontSizeState();
        PlayConfig.movietextSize = 20;
        middleFontImage.setVisibility(View.VISIBLE);
        SharedPreTool.setSharedPreDateInt(subtitleActivity, PlayConfig.MOVIE_TEXT_SIZE, 20);
        changeFontSizeCallbak();
    }

    /**
     * 大号字体
     *
     * @param view
     */
    private void changeLargeFontClick(View view) {
        clearFontSizeState();
        PlayConfig.movietextSize = 22;
        largeFontImage.setVisibility(View.VISIBLE);
        SharedPreTool.setSharedPreDateInt(subtitleActivity, PlayConfig.MOVIE_TEXT_SIZE, 22);
        changeFontSizeCallbak();
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
     * 设置字体大小改变监听器
     *
     * @param onSubtitleChangeFontSizeListener
     */
    public void setOnSubtitleChangeFontSizeListener(OnSubtitleChangeFontSizeListener onSubtitleChangeFontSizeListener) {
        this.onSubtitleChangeFontSizeListener = onSubtitleChangeFontSizeListener;
    }

    /**
     * 打开Window
     */
    public void showWindow() {
        // 初始化字体大小
        textSize = SharedPreTool.getSharedPreDateInt(subtitleActivity, PlayConfig.MOVIE_TEXT_SIZE, 18);
        // 初始化字体
        smallFontImage.setVisibility(View.GONE);
        middleFontImage.setVisibility(View.GONE);
        largeFontImage.setVisibility(View.GONE);
        if (textSize == 18) {
            smallFontImage.setVisibility(View.VISIBLE);
        } else if (textSize == 20) {
            middleFontImage.setVisibility(View.VISIBLE);
        } else {
            largeFontImage.setVisibility(View.VISIBLE);
        }
        if (onWindowStateListener != null) {
            onWindowStateListener.windowOpen();
        }
        showAtLocation(subtitleActivity.findViewById(R.id.iv_moreset_video), Gravity.BOTTOM, 0, (int) (200 * displayMetrics.density));
    }

    /**
     * 关闭Window
     */
    public void closeWindow() {
        if (onWindowStateListener != null) {
            onWindowStateListener.windowExternalClose();
        }
        dismiss();
    }

}
