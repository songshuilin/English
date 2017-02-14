package net.naucu.englishxianshi.ui.video.window;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lijunsai.httpInterface.tool.SharedPreTool;

import net.naucu.englishxianshi.R;
import net.naucu.englishxianshi.ui.video.PlayConfig;
import net.naucu.englishxianshi.ui.video.PlayConfig.ShType;
import net.naucu.englishxianshi.ui.video.PlaySubtitleActivity;
import net.naucu.englishxianshi.ui.video.window.event.OnSubtitleHideOrShowListener;
import net.naucu.englishxianshi.ui.video.window.event.OnWindowStateListener;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * 字幕设置
 *
 * @author Yi
 */
public class SubtitleSettingWindow extends PopupWindow {

    // Activity
    private PlaySubtitleActivity subtitleActivity;
    // 屏幕尺寸对象
    private DisplayMetrics displayMetrics;
    // Window根View
    private View rootView;
    // 隐藏中文
    @ViewInject(R.id.tv_hideChinese_video)
    private TextView hideChineseText;
    // 隐藏英文
    @ViewInject(R.id.tv_hideEnglish_video)
    private TextView hideEnglishText;
    // 模式更换 自动 手动
    @ViewInject(R.id.tv_inputMode_video)//tv_ChoiceModel_video
    private TextView videoInputModeText;
    // 选择模式更换 单选字幕 多选字幕
    @ViewInject(R.id.tv_ChoiceModel_video)
    private TextView videoChoiceModelText;
    // 字体大小
    @ViewInject(R.id.tv_textSize_video)
    private TextView subtitleFontSizeText;

    // 语速设置
    @ViewInject(R.id.tv_speed_video)
    private TextView subtitleSpeedText;

    // 输入模式
    private boolean autoInput = true;
    // 选择模式
    private boolean choiseModel = true;
    // 修改字体大小Window
    private SubtitleFontSizeWindow subtitleFontSizeWindow;

    // 修改语速设置Window
    private SubtitleSpeedWindow subtitleSpeedWindow;

    // Window关闭监听器
    private OnWindowStateListener onWindowStateListener;
    // 文字显示状态改变回调
    private OnSubtitleHideOrShowListener onSubtitleHideOrShowListener;

    public SubtitleSettingWindow(PlaySubtitleActivity subtitleActivity) {
        this.subtitleActivity = subtitleActivity;

        // 初始化
        init();
        // 初始化View
        initView();
    }

    /**
     * 初始化
     */
    private void init() {
        // 初始化屏幕尺寸对象
        displayMetrics = new DisplayMetrics();
        subtitleActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        // 设置Window显示大小
        setWidth(LayoutParams.WRAP_CONTENT);
        setHeight(LayoutParams.WRAP_CONTENT);
        // 初始化输入模式
        autoInput = SharedPreTool.getSharedPreDateBoolean(subtitleActivity, PlayConfig.AUTO_INPUT, true);
        choiseModel = SharedPreTool.getSharedPreDateBoolean(subtitleActivity, PlayConfig.CHOICE_MODEL, true);
    }

    /**
     * 初始化View
     */


    public interface closePopwindowCallBack {
        void close();
    }

    public void setCallBack(closePopwindowCallBack callBack) {
        this.callBack = callBack;
    }

    closePopwindowCallBack callBack;

    @SuppressLint("InflateParams")
    private void initView() {
        rootView = LayoutInflater.from(subtitleActivity).inflate(R.layout.pop_moreset_video, null);
        setContentView(rootView);
        x.view().inject(this, rootView);

        // 隐藏中文 Click
        hideChineseText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                chineseHideOrShow(v);
            }
        });
        // 隐藏英文 Click
        hideEnglishText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                englishHideOrShow(v);
            }
        });
        // 初始化默认输入模式
        if (autoInput) {
            videoInputModeText.setText("智能词典");
        } else {
            videoInputModeText.setText("手动词典");
        }
        if (choiseModel) {
            videoChoiceModelText.setText("多选字幕");
        } else {
            videoChoiceModelText.setText("单选字幕");
        }
        // 输入模式 Click
        videoInputModeText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                changeAutoInput(v);
            }
        });
        // 选择模式 Click
        videoChoiceModelText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                changeChoiceMode(v);
            }
        });
        // 字体大小 Click
        subtitleFontSizeText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (subtitleFontSizeWindow != null && !subtitleFontSizeWindow.isShowing()) {
                    subtitleFontSizeWindow.showWindow();
                    if (onWindowStateListener != null) {
                        onWindowStateListener.windowExternalClose();
                        //((ImageView) findViewById(R.id.iv_moreset_video)).setImageResource(R.drawable.video_moreset_n);
                        dismiss();
                    }
                }
            }
        });


        // 语速设置 Click
        subtitleSpeedText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (subtitleSpeedWindow != null && !subtitleSpeedWindow.isShowing()) {
                    if (onWindowStateListener != null) {
                        onWindowStateListener.windowExternalClose();
                    }
                    subtitleSpeedWindow.showWindow();
                    if (callBack != null) {
                        callBack.close();
                    }
                    if (onWindowStateListener != null) {
                        onWindowStateListener.windowInternalClose();
                        dismiss();
                    }
                }
            }
        });
    }

    /**
     * 中文隐藏或显示
     *
     * @param view
     */


    private void chineseHideOrShow(View view) {
        if (hideChineseText.getText().equals("隐藏中文")) {
            hideChineseText.setText("显示中文");
            subtitleActivity.videoMediaController.mSrtChinese.setVisibility(View.INVISIBLE);
            subtitleActivity.videoMediaController.mSrtChinese_big.setVisibility(View.INVISIBLE);
            switch (PlayConfig.shType) {
                case ENGLISH:
                    PlayConfig.shType = ShType.ALL;
                    break;
                case NONE:
                    PlayConfig.shType = ShType.CHINESE;
                    break;
                default:
                    break;
            }
        } else {
            hideChineseText.setText("隐藏中文");
            subtitleActivity.videoMediaController.mSrtChinese.setVisibility(View.VISIBLE);
            subtitleActivity.videoMediaController.mSrtChinese_big.setVisibility(View.VISIBLE);
            switch (PlayConfig.shType) {
                case CHINESE:
                    PlayConfig.shType = ShType.NONE;
                    break;
                case ALL:
                    PlayConfig.shType = ShType.ENGLISH;
                    break;
                default:
                    break;
            }
        }
        if (onSubtitleHideOrShowListener != null) {
            onSubtitleHideOrShowListener.changeSubtitleHideOrShow();
        }
    }

    /**
     * 英文隐藏或显示
     *
     * @param view
     */
    private void englishHideOrShow(View view) {
        if (hideEnglishText.getText().equals("隐藏英文")) {
            hideEnglishText.setText("显示英文");
            subtitleActivity.videoMediaController.mSrtEnglish.setVisibility(View.INVISIBLE);
            subtitleActivity.videoMediaController.mSrtEnglish_big.setVisibility(View.INVISIBLE);
            switch (PlayConfig.shType) {
                case CHINESE:
                    PlayConfig.shType = ShType.ALL;
                    break;
                case NONE:
                    PlayConfig.shType = ShType.ENGLISH;
                    break;
                default:
                    break;
            }
        } else {
            hideEnglishText.setText("隐藏英文");
            subtitleActivity.videoMediaController.mSrtEnglish.setVisibility(View.VISIBLE);
            subtitleActivity.videoMediaController.mSrtEnglish_big.setVisibility(View.VISIBLE);
            switch (PlayConfig.shType) {
                case ENGLISH:
                    PlayConfig.shType = ShType.NONE;
                    break;
                case ALL:
                    PlayConfig.shType = ShType.CHINESE;
                    break;
                default:
                    break;
            }
        }
        if (onSubtitleHideOrShowListener != null) {
            onSubtitleHideOrShowListener.changeSubtitleHideOrShow();
        }
    }

    /**
     * 修改输入模式
     *
     * @param view
     */
    private void changeAutoInput(View view) {
        if (autoInput) {
            autoInput = false;
            videoInputModeText.setText("手动词典");
        } else {
            autoInput = true;
            videoInputModeText.setText("智能词典");
        }
        SharedPreTool.setSharedPreDateBoolean(subtitleActivity, PlayConfig.AUTO_INPUT, autoInput);
    }

    private void changeChoiceMode(View view) {
        if (choiseModel) {
            choiseModel = false;
            videoChoiceModelText.setText("单选字幕");
        } else {
            choiseModel = true;
            videoChoiceModelText.setText("多选字幕");
        }
        SharedPreTool.setSharedPreDateBoolean(subtitleActivity, PlayConfig.CHOICE_MODEL, choiseModel);
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
     * 设置文字显示状态改变监听器
     *
     * @param onSubtitleHideOrShowListener
     */
    public void setOnSubtitleHideOrShowListener(OnSubtitleHideOrShowListener onSubtitleHideOrShowListener) {
        this.onSubtitleHideOrShowListener = onSubtitleHideOrShowListener;
    }

    /**
     * 设置字体大小修改Window
     *
     * @param subtitleFontSizeWindow
     */
    public void setSubtitleFontSizeWindow(SubtitleFontSizeWindow subtitleFontSizeWindow) {
        this.subtitleFontSizeWindow = subtitleFontSizeWindow;
    }

    public void setSubtitleSpeedWindow(SubtitleSpeedWindow subtitleSpeedWindow) {
        this.subtitleSpeedWindow = subtitleSpeedWindow;
    }

    public int getPingMuWidth() {
        WindowManager wm = (WindowManager) subtitleActivity
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        return width;
    }


    /**
     * 打开Window
     */
    @SuppressLint("NewApi")
    public void showWindow() {
        if (onWindowStateListener != null) {
            onWindowStateListener.windowOpen();
        }
        SubtitleSelectWindow subtitleSelectWindow = subtitleActivity.getSubtitleSelectWindow();
        RereadMachineWindow rereadMachineWindow = subtitleActivity.getRereadMachineWindow();
        SelectRolePlayWindow selectRolePlayWindow = subtitleActivity.getSelectRolePlayWindow();
        if (PlayConfig.isFullStatus) {
            /**
             * 全屏模式下
             * */
            if (subtitleSelectWindow != null && subtitleSelectWindow.isShowing() &&
                    rereadMachineWindow != null && rereadMachineWindow.isShowing()) {
                /**
                 * 复读机和学习菜单都在
                 * */
                showAsDropDown(subtitleActivity.findViewById(R.id.iv_rollPlay), getPingMuWidth(), (int) (105 * displayMetrics.density),
                        Gravity.TOP);
            } else if (rereadMachineWindow != null && rereadMachineWindow.isShowing() &&
                    !(subtitleSelectWindow != null && subtitleSelectWindow.isShowing())) {
                /**
                 * 只有复读机在
                 * */
                showAsDropDown(subtitleActivity.findViewById(R.id.iv_rollPlay), getPingMuWidth(), (int) (53 * displayMetrics.density),
                        Gravity.TOP);
            } else if (!(rereadMachineWindow != null && rereadMachineWindow.isShowing()) &&
                    (subtitleSelectWindow != null && subtitleSelectWindow.isShowing())) {
                /**
                 * 只有学习菜单在
                 * */
                showAsDropDown(subtitleActivity.findViewById(R.id.iv_rollPlay), getPingMuWidth(), (int) (60 * displayMetrics.density),
                        Gravity.TOP);
            } else if ((subtitleSelectWindow == null || !subtitleSelectWindow.isShowing()) &&
                    (rereadMachineWindow == null || !rereadMachineWindow.isShowing())) {
                /**
                 * 复读机和学习菜单都不在
                 * */
                showAsDropDown(subtitleActivity.findViewById(R.id.iv_rollPlay), getPingMuWidth(), (int) (7 * displayMetrics.density),
                        Gravity.TOP);
            }
        } else {
            /**
             *  非 全屏模式下
             * */
            if (subtitleSelectWindow != null && subtitleSelectWindow.isShowing()) {
                showAsDropDown(subtitleActivity.findViewById(R.id.iv_moreset_video), (int) -(displayMetrics.density * 5),
                        (int) (displayMetrics.density * 57), Gravity.BOTTOM);
            } else {
                showAsDropDown(subtitleActivity.findViewById(R.id.iv_moreset_video), (int) -(displayMetrics.density * 5),
                        (int) (displayMetrics.density * 5), Gravity.BOTTOM);
            }
        }
        if (selectRolePlayWindow != null && selectRolePlayWindow.isShowing()) {
            selectRolePlayWindow.closeWindow();
            subtitleActivity.deletePopWindowList("角色扮演");
        }
        subtitleActivity.insertPopWindowList("更多设置");
    }

    /**
     * 关闭Window
     */
    public void closeWindow() {
        if (onWindowStateListener != null) {
            onWindowStateListener.windowInternalClose();
        }
        SubtitleSelectWindow subtitleSelectWindow = subtitleActivity.getSubtitleSelectWindow();
        RereadMachineWindow rereadMachineWindow = subtitleActivity.getRereadMachineWindow();
        if ((subtitleSelectWindow != null && subtitleSelectWindow.isShowing()) || (rereadMachineWindow != null && rereadMachineWindow.isShowing())) {
            if (subtitleActivity.isVideoPlaying()) {
                subtitleActivity.pausePlay();
            }
        }
        dismiss();
    }

    public void yidongUp() {
        dismiss();
        SubtitleSelectWindow subtitleSelectWindow = subtitleActivity.getSubtitleSelectWindow();
        RereadMachineWindow rereadMachineWindow = subtitleActivity.getRereadMachineWindow();
        if (PlayConfig.isFullStatus) {
            if ((subtitleSelectWindow != null && subtitleSelectWindow.isShowing()) || (rereadMachineWindow != null && rereadMachineWindow.isShowing())) {
                if (subtitleSelectWindow != null && subtitleSelectWindow.isShowing() && rereadMachineWindow != null && rereadMachineWindow.isShowing()) {
                    Log.i("TAG", "wqe75as35d487waeas5das684d 偏移 二");
                    showAsDropDown(subtitleActivity.findViewById(R.id.iv_rollPlay), getPingMuWidth(), (int) (110 * displayMetrics.density),
                            Gravity.TOP);
                } else {
                    Log.i("TAG", "wqe75as35d487waeas5das684d 偏移 一");
                    showAsDropDown(subtitleActivity.findViewById(R.id.iv_rollPlay), getPingMuWidth(), (int) (57 * displayMetrics.density),
                            Gravity.TOP);
                }
            }
        } else {
            if (subtitleSelectWindow != null && subtitleSelectWindow.isShowing()) {
                showAsDropDown(subtitleActivity.findViewById(R.id.iv_moreset_video), (int) -(displayMetrics.density * 5),
                        (int) (displayMetrics.density * 57), Gravity.BOTTOM);
            } else {
                showAsDropDown(subtitleActivity.findViewById(R.id.iv_moreset_video), (int) -(displayMetrics.density * 5),
                        (int) (displayMetrics.density * 5), Gravity.BOTTOM);
            }
        }
    }

    public void yidongDown() {
        dismiss();
        SubtitleSelectWindow subtitleSelectWindow = subtitleActivity.getSubtitleSelectWindow();
        RereadMachineWindow rereadMachineWindow = subtitleActivity.getRereadMachineWindow();
        if ((subtitleSelectWindow != null && subtitleSelectWindow.isShowing()) || (rereadMachineWindow != null && rereadMachineWindow.isShowing())) {
            if (subtitleSelectWindow != null && subtitleSelectWindow.isShowing() && rereadMachineWindow != null && rereadMachineWindow.isShowing()) {
                Log.i("TAG", "wqe75as35d487waeas5das684d 偏移 二");
                showAsDropDown(subtitleActivity.findViewById(R.id.iv_rollPlay), getPingMuWidth(), (int) (110 * displayMetrics.density),
                        Gravity.TOP);
            } else if (subtitleSelectWindow != null && subtitleSelectWindow.isShowing() && !(rereadMachineWindow != null && rereadMachineWindow.isShowing())) {
                Log.i("TAG", "wqe75as35d487waeas5das684d 偏移 一");
                showAsDropDown(subtitleActivity.findViewById(R.id.iv_rollPlay), getPingMuWidth(), (int) (57 * displayMetrics.density),
                        Gravity.TOP);
            }
        } else {
            showAsDropDown(subtitleActivity.findViewById(R.id.iv_rollPlay), getPingMuWidth(), (int) (7 * displayMetrics.density),
                    Gravity.TOP);
        }
    }
}
