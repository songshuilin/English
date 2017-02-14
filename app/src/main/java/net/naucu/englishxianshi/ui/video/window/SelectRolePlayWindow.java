package net.naucu.englishxianshi.ui.video.window;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.lijunsai.httpInterface.tool.SharedPreTool;

import net.naucu.englishxianshi.R;
import net.naucu.englishxianshi.ui.video.PlayConfig;
import net.naucu.englishxianshi.ui.video.PlaySubtitleActivity;
import net.naucu.englishxianshi.ui.video.window.event.OnRolePlaySelectListener;
import net.naucu.englishxianshi.ui.video.window.event.OnWindowStateListener;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * 选择角色扮演
 *
 * @author Yi
 */
public class SelectRolePlayWindow extends PopupWindow {

    // Activity
    private PlaySubtitleActivity subtitleActivity;
    // 屏幕尺寸对象
    private DisplayMetrics displayMetrics;
    // Window根View
    private View rootView;
    // 设置
    @ViewInject(R.id.tv_set)
    private TextView rolePlaySetText;
    // 首句
    @ViewInject(R.id.tv_begin)
    private TextView rolePlayFirstText;
    // 线条
    @ViewInject(R.id.v_beginto)
    private View beginLineView;
    // 尾句
    @ViewInject(R.id.tv_end)
    private TextView rolePlayTailText;
    // 线条
    @ViewInject(R.id.v_endto)
    private View endLineView;
    // 对话
    @ViewInject(R.id.tv_body)
    private TextView rolePlayDialogueText;
    @ViewInject(R.id.tv_start)
    private TextView rolePlayStartText;

    // 角色扮演设置
    private SettingRolePlayWindow settingRolePlayWindow;
    // 角色扮演录音
    private RolePlayRecordWindow rolePlayRecordWindow;

    // Window关闭监听器
    private OnWindowStateListener onWindowStateListener;
    //
    private OnRolePlaySelectListener onRolePlaySelectListener;

    private int size = 0;

    private SelectCallBack callBack;

    public SelectRolePlayWindow(PlaySubtitleActivity subtitleActivity, SelectCallBack callBack) {
        this.subtitleActivity = subtitleActivity;
        this.callBack = callBack;
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
        setWidth(LayoutParams.MATCH_PARENT);
        setHeight(LayoutParams.WRAP_CONTENT);
        // 初始化录音设置
        PlayConfig.isBeginPlayPause = SharedPreTool.getSharedPreDateBoolean(subtitleActivity,
                PlayConfig.BEGIN_PLAY_PAUSE, false);
        PlayConfig.isRecordLimitTime = SharedPreTool.getSharedPreDateBoolean(subtitleActivity,
                PlayConfig.RECORD_LIMIT_TIME, false);
        PlayConfig.isCompletePausePlay = SharedPreTool.getSharedPreDateBoolean(subtitleActivity,
                PlayConfig.COMPLETE_PAUES_PLAY, false);
    }

    /**
     * 初始化View
     */
    @SuppressLint("InflateParams")
    private void initView() {
        rootView = LayoutInflater.from(subtitleActivity).inflate(R.layout.rollplayer_selectspace, null);
        setContentView(rootView);
        x.view().inject(this, rootView);
        // 设置 Click
        rolePlaySetText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                rolePlaySetting(v);
            }
        });
        // 开始 Click
        rolePlayStartText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                subtitleActivity.setFalg(true);
                if (size <= 0) {
                    Toast.makeText(subtitleActivity, "请选择句子", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (subtitleActivity.getList().size() > 0) {
                    subtitleActivity.startPlay(subtitleActivity.getList().get(0).getShowTime().getBegin());
                }
                subtitleActivity.qiangzhishuping();
                subtitleActivity.setP();
                subtitleActivity.pausePlay();
                rolePlayStartRecord(v);
            }
        });
        // 首句 Click
        rolePlayFirstText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                firstClick(v);
            }
        });
        // 尾句 Click
        rolePlayTailText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                tailClick(v);
            }
        });
        rolePlayDialogueText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (subtitleActivity.getAdapters() != null) {
                    subtitleActivity.getAdapters().setEndPosition(-1);
                    subtitleActivity.getAdapters().notifyDataSetChanged();
                    subtitleActivity.getAdapters().callToWindow();
                    //subtitleActivity.getAdapters().clearRolePlayTail();
                }
            }
        });
        rolePlayStartText.setClickable(false);
    }

    /**
     * 角色扮演设置
     *
     * @param view
     */
    private void rolePlaySetting(View view) {
        if (!settingRolePlayWindow.isShowing()) {
            settingRolePlayWindow.showWindow();
        }
    }

    /**
     * 开始录音
     *
     * @param view
     */
    private void rolePlayStartRecord(View view) {
        if (!rolePlayRecordWindow.isShowing()) {
            rolePlayRecordWindow.showWindow();
            if (onWindowStateListener != null) {
                onWindowStateListener.windowInternalClose();
            }
            setRolePlayBeginSubtitle(0);
            dismiss();
        }
    }

    /**
     * 首句
     */


    private void firstClick(View view) {
        if (onRolePlaySelectListener != null) {
            rolePlayFirstText.setBackgroundColor(Color.parseColor("#ffffff"));
            rolePlayFirstText.setBackgroundResource(R.drawable.stroke_ring_white3);
            rolePlayFirstText.setTextColor(Color.parseColor("#ffffff"));
            onRolePlaySelectListener.rolePlaySelectFirst();

        }
    }

    /**
     * 尾句
     *
     * @param view
     */
    private void tailClick(View view) {
        Log.i("TAG", "qe87r6da35s876wqe");
        if (onRolePlaySelectListener != null) {
            onRolePlaySelectListener.rolePlaySelectTail();
        }
    }

    /**
     * 设置首句显示状态
     *
     * @param size
     */
    public void setRolePlayBeginSubtitle(int size) {
        if (size <= 0) {
            beginLineView.setBackgroundColor(Color.parseColor("#FFFFFF"));
            rolePlayTailText.setTextColor(subtitleActivity.getResources().getColor(R.color.white));
            rolePlayTailText.setBackgroundResource(R.drawable.stroke_ring_white3);
            endLineView.setBackgroundColor(Color.parseColor("#FFFFFF"));
            rolePlayDialogueText.setTextColor(subtitleActivity.getResources().getColor(R.color.white));
            rolePlayDialogueText.setBackgroundResource(R.drawable.stroke_ring_white3);
            rolePlayStartText.setTextColor(subtitleActivity.getResources().getColor(R.color.white));
            rolePlayStartText.setBackgroundResource(R.drawable.stroke_corner_white2);
            rolePlayStartText.setClickable(false);
        } else {

            beginLineView.setBackgroundColor(Color.parseColor("#45fbf0"));
            rolePlayTailText.setTextColor(Color.parseColor("#45fbf0"));
            rolePlayTailText.setBackgroundResource(R.drawable.stroke_ring_blue3);
            endLineView.setBackgroundColor(Color.parseColor("#FFFFFF"));
            rolePlayDialogueText.setTextColor(subtitleActivity.getResources().getColor(R.color.white));
            rolePlayDialogueText.setBackgroundResource(R.drawable.stroke_ring_white3);
            rolePlayStartText.setTextColor(subtitleActivity.getResources().getColor(R.color.white));
            rolePlayStartText.setBackgroundResource(R.drawable.stroke_corner_white2);
            rolePlayStartText.setClickable(false);
        }
    }

    /**
     * 设置尾句显示状态
     *
     * @param size
     */
    public void setRolePlayEndSubtitle(int size, int endPosition) {
        this.size = size;
        if (size <= 0) {
            rolePlayTailText.setTextColor(subtitleActivity.getResources().getColor(R.color.white));
            rolePlayTailText.setBackgroundResource(R.drawable.stroke_ring_white3);
        } else {
            beginLineView.setBackgroundColor(Color.parseColor("#45fbf0"));
            if (endPosition != -1) {
                rolePlayDialogueText.setTextColor(Color.parseColor("#45fbf0"));
                rolePlayDialogueText.setBackgroundResource(R.drawable.stroke_ring_blue3);
                endLineView.setBackgroundColor(Color.parseColor("#45fbf0"));
                rolePlayStartText.setTextColor(0xffffffff);
                rolePlayStartText.setBackgroundResource(R.drawable.corner_green);
                rolePlayStartText.setClickable(true);
            } else {
                rolePlayDialogueText.setTextColor(subtitleActivity.getResources().getColor(R.color.white));
                rolePlayDialogueText.setBackgroundResource(R.drawable.stroke_ring_white3);
                rolePlayStartText.setTextColor(subtitleActivity.getResources().getColor(R.color.white));
                rolePlayStartText.setBackgroundResource(R.drawable.stroke_corner_white2);
                rolePlayStartText.setClickable(false);
            }
            rolePlayTailText.setTextColor(Color.parseColor("#45fbf0"));
            rolePlayTailText.setBackgroundResource(R.drawable.stroke_ring_blue3);
        }
    }

    /**
     * 设置Window状态监听器
     *
     * @param onWindowStateListener
     */
    public void setOnWindowStateListener(OnWindowStateListener onWindowStateListener) {
        this.onWindowStateListener = onWindowStateListener;
    }

    public void setOnRolePlaySelectListener(OnRolePlaySelectListener onRolePlaySelectListener) {
        this.onRolePlaySelectListener = onRolePlaySelectListener;
    }

    /**
     * 设置角色扮演设置
     *
     * @param settingRolePlayWindow
     */
    public void setSettingRolePlayWindow(SettingRolePlayWindow settingRolePlayWindow) {
        this.settingRolePlayWindow = settingRolePlayWindow;
    }

    /**
     * 设置角色扮演录音
     *
     * @param rolePlayRecordWindow
     */
    public void setRolePlayRecordWindow(RolePlayRecordWindow rolePlayRecordWindow) {
        this.rolePlayRecordWindow = rolePlayRecordWindow;
    }

    /**
     * 打开Window
     */
    @SuppressLint("NewApi")
    public void showWindow() {
        callBack.selectOpencakkBack();
        // 回调Window状态
        if (onWindowStateListener != null) {
            onWindowStateListener.windowOpen();
        }
        if (PlayConfig.isFullStatus) {
            showAsDropDown(subtitleActivity.findViewById(R.id.iv_rollPlay), 0, (int) (7 * displayMetrics.density),
                    Gravity.TOP);
        } else {
            showAsDropDown(subtitleActivity.findViewById(R.id.iv_rollPlay), 0, (int) (displayMetrics.density * 5),
                    Gravity.BOTTOM);
        }
        if (onRolePlaySelectListener != null) {
            rolePlayFirstText.setBackgroundColor(Color.parseColor("#ffffff"));
            rolePlayFirstText.setBackgroundResource(R.drawable.stroke_ring_white3);
            rolePlayFirstText.setTextColor(Color.parseColor("#ffffff"));
            onRolePlaySelectListener.rolePlaySelectFirst();
        }
        subtitleActivity.insertPopWindowList("角色扮演");
    }

    /**
     * 关闭Window
     */
    public void closeWindow() {
        callBack.selectClosecakkBack();
        rolePlayFirstText.setTextColor(0xffffffff);
        rolePlayFirstText.setBackgroundResource(R.drawable.stroke_ring_white3);
        if (onRolePlaySelectListener != null) {
            onRolePlaySelectListener.rolePlaySelectFirst();
        }
        // 回调Window状态
        if (onWindowStateListener != null) {
            onWindowStateListener.windowExternalClose();
        }
        setRolePlayBeginSubtitle(0);
        dismiss();
    }


    public void setTextColors() {
        rolePlayFirstText.setBackgroundColor(Color.parseColor("#45fbf0"));
        rolePlayFirstText.setBackgroundResource(R.drawable.stroke_ring_blue3);
        rolePlayFirstText.setTextColor(Color.parseColor("#45fbf0"));
    }

    public interface SelectCallBack {
        void selectClosecakkBack();

        void selectOpencakkBack();
    }
}
