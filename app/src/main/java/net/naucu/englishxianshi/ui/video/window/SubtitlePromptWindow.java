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

import net.naucu.englishxianshi.R;
import net.naucu.englishxianshi.ui.video.PlayConfig;
import net.naucu.englishxianshi.ui.video.PlaySubtitleActivity;
import net.naucu.englishxianshi.ui.video.window.event.OnWindowStateListener;
import net.naucu.englishxianshi.widget.view.SelectTextview;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * 字幕提示
 *
 * @author Yi
 */
public class SubtitlePromptWindow extends PopupWindow {

    // Activity
    private PlaySubtitleActivity subtitleActivity;
    // 屏幕尺寸对象
    private DisplayMetrics displayMetrics;
    // Window根View
    private View rootView;
    // 字幕
    @ViewInject(R.id.tv_select)
    private SelectTextview subtitleText;
    // 关闭按钮
    @ViewInject(R.id.chahao)
    private ImageView closeImageView;

    // Window状态监听器
    private OnWindowStateListener onWindowStateListener;

    @SuppressWarnings("deprecation")
    public SubtitlePromptWindow(PlaySubtitleActivity subtitleActivity) {
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
        rootView = LayoutInflater.from(subtitleActivity).inflate(R.layout.pop_selected, null);
        setContentView(rootView);
        x.view().inject(this, rootView);
        // 关闭 Clikc
        closeImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onWindowStateListener != null) {
                    onWindowStateListener.windowInternalClose();
                }
                dismiss();
            }
        });
        //
    }

    /**
     * 设置字幕
     *
     * @param subtitle
     */
    public void setSubtitle(String subtitle) {
        subtitleText.setText(subtitle);
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
        if(PlayConfig.isFullStatus){
            showAtLocation(subtitleActivity.findViewById(R.id.root_layout), Gravity.BOTTOM, 0, (int) (displayMetrics.density * 103));
        }else{
            showAtLocation(subtitleActivity.findViewById(R.id.root_layout), Gravity.BOTTOM, 0, 0);
        }

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
