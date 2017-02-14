package net.naucu.englishxianshi.ui.video.window;

import android.annotation.SuppressLint;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.lijunsai.httpInterface.tool.SharedPreTool;

import net.naucu.englishxianshi.R;
import net.naucu.englishxianshi.ui.DictionaryTranslationActivity;
import net.naucu.englishxianshi.ui.DictionaryTranslationActivity.TranslateType;
import net.naucu.englishxianshi.iflytek.TTSIflytek;
import net.naucu.englishxianshi.ui.video.adapter.VideoCallback;
import net.naucu.englishxianshi.util.NetTool;
import net.naucu.englishxianshi.ui.video.PlayConfig;
import net.naucu.englishxianshi.ui.video.PlaySubtitleActivity;
import net.naucu.englishxianshi.ui.video.bean.SubtitleBean;
import net.naucu.englishxianshi.ui.video.window.event.OnSubtitleSelectPlayListener;
import net.naucu.englishxianshi.ui.video.window.event.OnWindowStateListener;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * 学习菜单
 *
 * @author Yi
 */
public class SubtitleSelectWindow extends PopupWindow {
    public static final String TAG = SubtitleSelectWindow.class.getSimpleName();

    // Activity
    private PlaySubtitleActivity subtitleActivity;
    // 屏幕尺寸对象
    private DisplayMetrics displayMetrics;
    // Window根View
    private View rootView;
    // 选择播放
    @ViewInject(R.id.iv_selectplay)
    private ImageView selectPlayImageView;
    // 语法解析
    @ViewInject(R.id.iv_analyse)
    private ImageView syntaxParseImageView;
    // 正音朗读
    @ViewInject(R.id.iv_read)
    private ImageView correctReadImageView;
    // 电子词典
    @ViewInject(R.id.iv_word)
    private ImageView electronicDictionaryImageView;
    // 翻译
    @ViewInject(R.id.iv_sentence)
    private ImageView translationImageView;
    private TTSIflytek ttsIflytek;
    private List<SubtitleBean> selectSubtitleList;
    private SubtitlePromptWindow subtitlePromptWindow;
    private OnWindowStateListener onWindowStateListener;
    private OnSubtitleSelectPlayListener onSubtitleSelectPlayListener;
    public DiscolorationCallback discolorationCallback;
    private VideoCallback callback;

    public SubtitleSelectWindow(PlaySubtitleActivity subtitleActivity, DiscolorationCallback discolorationCallback, VideoCallback callback) {
        this.subtitleActivity = subtitleActivity;
        this.discolorationCallback = discolorationCallback;
        this.callback = callback;
        init();
        initView();
    }

    /**
     * 初始化
     */
    private void init() {
        displayMetrics = new DisplayMetrics();
        subtitleActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        setWidth(LayoutParams.MATCH_PARENT);
        setHeight(LayoutParams.WRAP_CONTENT);
        ttsIflytek = new TTSIflytek(subtitleActivity);
    }

    /**
     * 初始化View
     */
    @SuppressLint("InflateParams")
    private void initView() {
        rootView = LayoutInflater.from(subtitleActivity).inflate(R.layout.srt_select, null);
        setContentView(rootView);
        x.view().inject(this, rootView);
        selectPlayImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPlayClick(v);
            }
        });
        syntaxParseImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.callBackSelect(selectSubtitleList);
                syntaxParse(v);
            }
        });
        // 正音朗读 Click
        correctReadImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                correctReading(v);
            }
        });
        electronicDictionaryImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.callBackSelect(selectSubtitleList);
                electronicDictionary(v);
            }
        });
        translationImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.callBackSelect(selectSubtitleList);
                translation(v);
            }
        });
    }

    /**
     * 选择字幕播放
     */
    private void selectPlayClick(View view) {
        if (ttsIflytek != null && ttsIflytek.getSpeechSynthesizer().isSpeaking()) {
            ttsIflytek.getSpeechSynthesizer().stopSpeaking();
            correctReadImageView.setImageResource(R.drawable.video_read_n);
        }
        if (selectSubtitleList == null || selectSubtitleList.size() <= 0) {
            Toast.makeText(subtitleActivity, "请选择操作字幕", Toast.LENGTH_SHORT).show();
            return;
        }
        if (onSubtitleSelectPlayListener != null) {
            onSubtitleSelectPlayListener.subtitleSelectPlay(selectSubtitleList);
            if (selectSubtitleList!=null){
                flag = true;
                discolorationCallback.callback(selectSubtitleList, flag);
            }

        }
    }

    /**
     * 语法解析
     *
     * @param view
     */
    private void syntaxParse(View view) {
        if (!NetTool.isNetworkConnected(subtitleActivity)) {
            Toast.makeText(subtitleActivity, "网络异常,请检查网络连接状态", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectSubtitleList == null || selectSubtitleList.size() <= 0) {
            Toast.makeText(subtitleActivity, "请选择操作字幕", Toast.LENGTH_SHORT).show();
            return;
        }
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < selectSubtitleList.size(); i++) {
            if (selectSubtitleList.get(i).getChinese().length() != 0 && selectSubtitleList.get(i).getEnglish().length() == 0) {
                Toast.makeText(subtitleActivity, "请选择英文字幕", Toast.LENGTH_SHORT).show();
                return;
            } else {
                char c1 = selectSubtitleList.get(i).getEnglish().charAt(selectSubtitleList.get(i).getEnglish().length() - 1);
                if (c1 >= 'A' && c1 <= 'Z' || c1 >= 'a' && c1 <= 'z' || c1 >= '0' && c1 <= '9') {
                    buffer.append(selectSubtitleList.get(i).getEnglish() + " ");
                } else {
                    buffer.append(selectSubtitleList.get(i).getEnglish());
                }
            }
        }
        String subtitle = buffer.toString();
        if (subtitle.endsWith("...")) {
            Toast.makeText(subtitleActivity, "您可能选取了不完整的句子", Toast.LENGTH_SHORT).show();
        }
        subtitle = subtitle.replaceAll("\\.\\.\\.", " ");
        DictionaryTranslationActivity.startActivity(subtitleActivity, TranslateType.analyse, subtitle.trim(), 2);

    }

    private boolean flag = false;

    /**
     * 正音朗读
     *
     * @param view
     */
    private void correctReading(View view) {
        subtitleActivity.pausePlay();
        if (!NetTool.isNetworkConnected(subtitleActivity)) {
            Toast.makeText(subtitleActivity, "网络异常,请检查网络连接状态", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectSubtitleList == null || selectSubtitleList.size() <= 0) {
            Toast.makeText(subtitleActivity, "请选择操作字幕", Toast.LENGTH_SHORT).show();
            return;
        }
        if (ttsIflytek.getSpeechSynthesizer().isSpeaking()) {
            ttsIflytek.getSpeechSynthesizer().stopSpeaking();
            correctReadImageView.setImageResource(R.drawable.video_read_n);
        } else {
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < selectSubtitleList.size(); i++) {
                buffer.append(selectSubtitleList.get(i).getEnglish());
            }
            String subtitle = buffer.toString();
            if (subtitle.endsWith("...")) {
                Toast.makeText(subtitleActivity, "您可能选取了不完整的句子", Toast.LENGTH_SHORT).show();
            }
            ttsIflytek.set_read(correctReadImageView, 2);
            subtitle = subtitle.replaceAll("\\.\\.\\.", " ");
            ttsIflytek.setTextflytek(subtitle.trim());//设置发音文本
            flag = true;
            discolorationCallback.callback(selectSubtitleList, flag);//改变颜色
        }
    }


    /**
     * 电子词典
     *
     * @param view
     */
    private void electronicDictionary(View view) {
        if (!NetTool.isNetworkConnected(subtitleActivity)) {
            Toast.makeText(subtitleActivity, "网络异常,请检查网络连接状态", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectSubtitleList == null || selectSubtitleList.size() <= 0) {
            Toast.makeText(subtitleActivity, "请选择操作字幕", Toast.LENGTH_SHORT).show();
            return;
        }
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < selectSubtitleList.size(); i++) {
            if (selectSubtitleList.get(i).getChinese().length() != 0 && selectSubtitleList.get(i).getEnglish().length() == 0) {
                Toast.makeText(subtitleActivity, "请选择英文字幕", Toast.LENGTH_SHORT).show();
                return;
            } else {
                char c1 = selectSubtitleList.get(i).getEnglish().charAt(selectSubtitleList.get(i).getEnglish().length() - 1);
                if (c1 >= 'A' && c1 <= 'Z' || c1 >= 'a' && c1 <= 'z' || c1 >= '0' && c1 <= '9') {
                    buffer.append(selectSubtitleList.get(i).getEnglish() + " ");
                } else {
                    buffer.append(selectSubtitleList.get(i).getEnglish());
                }
            }
        }
        String subtitle = buffer.toString();
        if (subtitle.endsWith("...")) {
            Toast.makeText(subtitleActivity, "您可能选取了不完整的句子", Toast.LENGTH_SHORT).show();
        }
        subtitle = subtitle.replaceAll("\\.\\.\\.", " ");

        boolean isAuto = SharedPreTool.getSharedPreDateBoolean(subtitleActivity, PlayConfig.AUTO_INPUT, true);
        if (!isAuto) {
            subtitlePromptWindow.setSubtitle(subtitle.trim());
            subtitlePromptWindow.showWindow();
        } else {
            DictionaryTranslationActivity.startActivity(subtitleActivity, TranslateType.word, subtitle.trim(), 2);
        }
    }

    /**
     * 翻译
     *
     * @param view
     */
    private void translation(View view) {
        if (!NetTool.isNetworkConnected(subtitleActivity)) {
            Toast.makeText(subtitleActivity, "网络异常,请检查网络连接状态", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectSubtitleList == null || selectSubtitleList.size() <= 0) {
            Toast.makeText(subtitleActivity, "请选择操作字幕", Toast.LENGTH_SHORT).show();
            return;
        }
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < selectSubtitleList.size(); i++) {
            if (selectSubtitleList.get(i).getChinese().length() != 0 && selectSubtitleList.get(i).getEnglish().length() == 0) {
                Toast.makeText(subtitleActivity, "请选择英文字幕", Toast.LENGTH_SHORT).show();
                return;
            } else {
                char c1 = selectSubtitleList.get(i).getEnglish().charAt(selectSubtitleList.get(i).getEnglish().length() - 1);
                if (c1 >= 'A' && c1 <= 'Z' || c1 >= 'a' && c1 <= 'z' || c1 >= '0' && c1 <= '9') {
                    buffer.append(selectSubtitleList.get(i).getEnglish() + " ");
                } else {
                    buffer.append(selectSubtitleList.get(i).getEnglish());
                }
            }
        }
        String subtitle = buffer.toString();
        if (subtitle.endsWith("...")) {
            Toast.makeText(subtitleActivity, "您可能选取了不完整的句子", Toast.LENGTH_SHORT).show();
        }
        subtitle = subtitle.replaceAll("\\.\\.\\.", " ");
        DictionaryTranslationActivity.startActivity(subtitleActivity, TranslateType.sentence, subtitle.trim(), 2);

    }

    /**
     * 设置选择字幕列表
     *
     * @param selectSubtitleList
     */
    public void setSelectSubtitleList(List<SubtitleBean> selectSubtitleList) {
        this.selectSubtitleList = selectSubtitleList;
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
     * 设置选择字幕播放监听器
     *
     * @param onSubtitleSelectPlayListener
     */
    public void setOnSubtitleSelectPlayListener(OnSubtitleSelectPlayListener onSubtitleSelectPlayListener) {
        this.onSubtitleSelectPlayListener = onSubtitleSelectPlayListener;
    }

    /**
     * 设置字体提示Window
     *
     * @param subtitlePromptWindow
     */
    public void setSubtitlePromptWindow(SubtitlePromptWindow subtitlePromptWindow) {
        this.subtitlePromptWindow = subtitlePromptWindow;
    }

    /**
     * 打开Window
     */
    @SuppressLint("NewApi")
    public void showWindow() {
        if (onWindowStateListener != null) {
            onWindowStateListener.windowOpen();
        }
        SelectRolePlayWindow selectRolePlayWindow = subtitleActivity.getSelectRolePlayWindow();
        RereadMachineWindow rereadMachineWindow = subtitleActivity.getRereadMachineWindow();
        SubtitleSettingWindow subtitleSettingWindow = subtitleActivity.getSubtitleSettingWindow();
        if (!PlayConfig.isFullStatus) {
            if (rereadMachineWindow != null && rereadMachineWindow.isShowing()) {
                showAsDropDown(subtitleActivity.findViewById(R.id.iv_rollPlay), 0, (int) (5 * displayMetrics.density),
                        Gravity.BOTTOM);
                callBack.openCallBack();

            } else {
                showAsDropDown(subtitleActivity.findViewById(R.id.iv_srtSelect), 0, (int) (displayMetrics.density * 5),
                        Gravity.BOTTOM);
            }
            if (subtitleSettingWindow != null && subtitleSettingWindow.isShowing()) {

                showAsDropDown(subtitleActivity.findViewById(R.id.iv_rollPlay), 0, (int) (5 * displayMetrics.density),
                        Gravity.BOTTOM);
                callBack.openCallBack();
            } else {
                showAsDropDown(subtitleActivity.findViewById(R.id.iv_srtSelect), 0, (int) (displayMetrics.density * 5),
                        Gravity.BOTTOM);
            }

        } else {
            if (subtitleSettingWindow != null && subtitleSettingWindow.isShowing()) {
                showAsDropDown(subtitleActivity.findViewById(R.id.iv_rollPlay), 0, (int) (7 * displayMetrics.density),
                        Gravity.TOP);
                callBack.openCallBack();
            }
            if (rereadMachineWindow != null && rereadMachineWindow.isShowing()) {
                showAsDropDown(subtitleActivity.findViewById(R.id.iv_rollPlay), 0, (int) (7 * displayMetrics.density),
                        Gravity.TOP);
                callBack.openCallBack();
            } else {
                showAsDropDown(subtitleActivity.findViewById(R.id.iv_rollPlay), 0, (int) (7 * displayMetrics.density),
                        Gravity.TOP);
            }
        }
        if (selectRolePlayWindow != null && selectRolePlayWindow.isShowing()) {
            selectRolePlayWindow.closeWindow();
            subtitleActivity.deletePopWindowList("角色扮演");
        }
        subtitleActivity.insertPopWindowList("学习菜单");
    }

    /**
     * 关闭Window
     */
    private WindowCallBack callBack = null;

    public void setCloseCallBack(WindowCallBack callBack) {
        this.callBack = callBack;
    }

    public void closeWindow() {
        if (subtitleActivity.getLearnSelectSubtitleList() != null) {
            subtitleActivity.getLearnSelectSubtitleList().clear();
        }

        if (onWindowStateListener != null) {
            flag = false;
            discolorationCallback.callback(selectSubtitleList, flag);
            onWindowStateListener.windowExternalClose();
        }
        dismiss();
        RereadMachineWindow rereadMachineWindow = subtitleActivity.getRereadMachineWindow();
        SubtitleSettingWindow subtitleSettingWindow = subtitleActivity.getSubtitleSettingWindow();
        if (PlayConfig.isFullStatus) {
            if (rereadMachineWindow != null && rereadMachineWindow.isShowing()) {
                callBack.closeCallBack();
            }
            if (subtitleSettingWindow != null && subtitleSettingWindow.isShowing()) {
                callBack.closeCallBack();
            }
        } else {
            if (subtitleSettingWindow != null && subtitleSettingWindow.isShowing()) {
                callBack.openCallBack();
            }
        }
        if ((rereadMachineWindow != null && rereadMachineWindow.isShowing()) || (subtitleSettingWindow != null && subtitleSettingWindow.isShowing())) {
            if (subtitleActivity.isVideoPlaying()) {
                subtitleActivity.pausePlay();
            }
        }
    }

    public interface WindowCallBack {
        void closeCallBack();

        void openCallBack();
    }

    public void closettsIflytek() {
        if (ttsIflytek != null && ttsIflytek.getSpeechSynthesizer().isSpeaking()) {
            ttsIflytek.getSpeechSynthesizer().stopSpeaking();
            correctReadImageView.setImageResource(R.drawable.video_read_n);
        }
    }
}
