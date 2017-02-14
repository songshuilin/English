package net.naucu.englishxianshi.ui.video.window;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Xml;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.fght.videoplayer.widget.media.VideoView;
import com.jiongbull.jlog.JLog;
import com.lijunsai.httpInterface.tool.CardUtils;

import net.naucu.englishxianshi.R;
import net.naucu.englishxianshi.bean.MyRecordBean;
import net.naucu.englishxianshi.bean.VideoBean;
import net.naucu.englishxianshi.db.Manager;
import net.naucu.englishxianshi.db.dao.Record;
import net.naucu.englishxianshi.iflytek.ISEIflytek;
import net.naucu.englishxianshi.iflytek.TTSIflytek;
import net.naucu.englishxianshi.ui.video.PlayConfig;
import net.naucu.englishxianshi.ui.video.PlayConfig.RoType;
import net.naucu.englishxianshi.ui.video.PlaySubtitleActivity;
import net.naucu.englishxianshi.ui.video.bean.ShowTime;
import net.naucu.englishxianshi.ui.video.bean.SubtitleBean;
import net.naucu.englishxianshi.ui.video.window.event.OnWindowStateListener;
import net.naucu.englishxianshi.ui.video.window.event.SimpleEvaluatorListener;
import net.naucu.englishxianshi.util.GetAmrDuration;
import net.naucu.englishxianshi.util.ToastTool;
import net.naucu.englishxianshi.widget.dialog.PromptDialog;
import net.naucu.englishxianshi.widget.dialog.PromptDialog.onPromptClickListener;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 角色扮演录音
 *
 * @author Yi
 */
public class RolePlayRecordWindow extends PopupWindow {
    public static final String TAG = RolePlayRecordWindow.class.getSimpleName();

    // Activity
    private PlaySubtitleActivity subtitleActivity;
    // 屏幕尺寸对象
    private DisplayMetrics displayMetrics;
    // Window根View
    private View rootView;
    // 评分Layout
    @ViewInject(R.id.ll_top_recordPop)
    private LinearLayout scoreLayout;
    // 评分分数
    @ViewInject(R.id.tv_score_recordPop)
    private TextView ratingScoreText;
    // 总评分
    @ViewInject(R.id.tv_total_scroe)
    private TextView tvTotalScore;
    // 当前录音操作Layout
    @ViewInject(R.id.ll_function_recordPop)
    private LinearLayout currentRecordFuncLayout;
    // 正音朗读
    @ViewInject(R.id.iv_regular_recordPop)
    private TextView correctBtn;
    // 试听
    @ViewInject(R.id.iv_audition_recordPop)
    private TextView auditionBtn;
    // 重录
    @ViewInject(R.id.iv_remake_recordPop)
    private TextView retakeBtn;
    // 录音操作状态提示Layout
    private LinearLayout recordFuncPromptLayout;
    // 进度条
    private ProgressBar countDownProgressBar;
    // 状态提示
    private TextView stateFuncText;
    // 录音操作菜单Layout
    @ViewInject(R.id.ll_bottom_recordPop)
    private LinearLayout recordFuncMenuLayout;
    // 返回按钮
    @ViewInject(R.id.tv_first_recordbottom_pop)
    private TextView backText;
    // 完成状态
    @ViewInject(R.id.tv_second_recordbottom_pop)
    private TextView completeStateText;
    // 操作按钮
    @ViewInject(R.id.tv_third_recordbottom_pop)
    private TextView operationText;

    // 保存标识
    public boolean isSave = false;
    // 录音倒计时最大时间
    private int maxTime = 3000;
    // 录音文件夹
    public String recordFolerUrl;
    // 录音文件
    public String recordFileUrl;
    // 录音评测
    public String recordReviewUrl;
    // 录音语句列表
    public List<SubtitleBean> rolePlayRecordList;
    // 录音文件列表
    public Map<Integer, String> recordFileMap;
    // 录音评分列表
    public Map<Integer, Integer> recordScoreMap;

    public MyRecordBean myRecordBean;
    // 录音对象
    private MediaRecorder mediaRecorder;
    // 录音评测对象
    private ISEIflytek iseIflytek;
    // 正音朗读对象
    private TTSIflytek ttsIflytek;
    // 录音播放对象
    private MediaPlayer mediaPlayer;

    // 中断录音
    boolean interruptRecord = false;
    // 当前录音语句
    private int currentPisition = 0;
    // 录音操作线程
    private static RecordRefreshHandler refreshHandler;
    // VIDEOVIEW操作线程
//    private static VideoPlayHandler videoPlayHandler;

    // Window状态监听器
    private OnWindowStateListener onWindowStateListener;

    //电影原音集合
    List<SubtitleBean> list = new ArrayList<>();

    Record record = new Record();
    List<Record> listRecord = new ArrayList<>();

    public List<SubtitleBean> subtitleBeanList = new ArrayList<>();

    public TextView txt_xiaoyuandian;

    public RolePlayRecordWindow(PlaySubtitleActivity subtitleActivity) {
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
        setWidth(LayoutParams.MATCH_PARENT);
        setHeight(LayoutParams.WRAP_CONTENT);
        // 初始化录音文件夹
        recordFolerUrl = CardUtils.filePath + "MyRecord";
        File folderFile = new File(recordFolerUrl);
        if (!folderFile.exists()) {
            folderFile.mkdirs();
        }
        // 初始化录音文件列表
        recordFileMap = new LinkedHashMap<>();
        // 初始化录音评分列表
        recordScoreMap = new LinkedHashMap<>();
        // 初始化录音操作线程
        refreshHandler = new RecordRefreshHandler();
//        videoPlayHandler = new VideoPlayHandler();
        listRecord = Manager.sleectRecords();
    }

    /**
     * 初始化View
     */
    @SuppressLint("InflateParams")
    private void initView() {
        rootView = LayoutInflater.from(subtitleActivity).inflate(R.layout.pop_rollplaying, null);
        setContentView(rootView);
        x.view().inject(this, rootView);
        recordFuncPromptLayout = (LinearLayout) subtitleActivity.findViewById(R.id.video_subtitle_operation_01_layout);
        countDownProgressBar = (ProgressBar) subtitleActivity.findViewById(R.id.pb_countdown_record);
        stateFuncText = (TextView) subtitleActivity.findViewById(R.id.tv_prompt_record);
        txt_xiaoyuandian = (TextView) subtitleActivity.findViewById(R.id.txt_xiaoyuandian);
        countDownProgressBar.setVisibility(View.GONE);
        // 返回 Click
        backText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                subtitleActivity.startPlay(subtitleActivity.getP());
                interruptRecord = true;
                subtitleActivity.setFalg(false);
                subtitleActivity.setTextColors();
                if (!isSave && PlayConfig.roType == RoType.COMPLETE_RECORD) {
                    final PromptDialog dialog = new PromptDialog(subtitleActivity);
                    dialog.setContent("是否保存本次录音");
                    dialog.show();
                    dialog.setClickListener(new onPromptClickListener() {
                        @Override
                        public void onCancel(View v) {
                            dialog.dismiss();
                            clearCurrentRecord();
                        }

                        @Override
                        public void onDetermine(View v) {
                            dialog.dismiss();
                            saveCurrentRecord();
                        }
                    });
                } else if (PlayConfig.roType != RoType.COMPLETE_RECORD) {
                    clearCurrentRecord();
                }
                if (onWindowStateListener != null) {
                    onWindowStateListener.windowInternalClose();
                }
                subtitleActivity.soundPlay();
                dismiss();
                subtitleActivity.hengshuping();
            }
        });
        // 完成状态 Click
        completeStateText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                completeStateClick(v);
            }
        });
        // 操作按钮
        operationText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                operationClick(v);
            }
        });
        // 正音朗读 Click
        correctBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (correctBtn.getText().toString().equals("正音朗读")) {
                    correctBtnClick(v, 0);
                } else {
                    completeStateClick(v);
                }
            }
        });
        // 试听 Click
        auditionBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                auditionBtnClick(v);
            }
        });
        // 重录
        retakeBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                retakeBtnClick(v);
            }
        });
        final Timer[] timer1 = new Timer[1];
        final Timer[] timer2 = new Timer[1];
        final Timer[] timer3 = new Timer[1];
        final Timer[] timer4 = new Timer[1];
        final Timer[] timer5 = new Timer[1];
        // 电影原音
        rootView.findViewById(R.id.tv_dianyingyuanyin).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ttsIflytek != null) {
                    if (ttsIflytek.getSpeechSynthesizer().isSpeaking()) {
                        ttsIflytek.getSpeechSynthesizer().stopSpeaking();
                        ttsIflytek = null;
                    }
                }
                if (mediaPlayer != null) {
                    try {
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        mediaPlayer = null;
                        subtitleActivity.soundPlay();
                    } catch (RuntimeException e) {
                        JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());
                    }
                }
                if (timer1[0] != null) {
                    timer1[0].cancel();

                }
                if (timer2[0] != null) {
                    timer2[0].cancel();
                }
                if (timer3[0] != null) {
                    timer3[0].cancel();
                }
                if (timer4[0] != null) {
                    timer4[0].cancel();
                }
                if (subtitleActivity.getMyRecordBean() != null) {
                    mVideoView = subtitleActivity.getVideoView();
                    int startPosition = subtitleActivity.getMyRecordBean().getStartPosition();
                    subtitleActivity.startPlay(startPosition);
                    for (int i = 0; i < subtitleActivity.getList().size(); i++) {
                        int begin = subtitleActivity.getList().get(i).getShowTime().getBegin();
                        int end = subtitleActivity.getList().get(i).getShowTime().getEnd();
                        if (startPosition >= begin && startPosition <= end) {
                            subtitleActivity.subtitleListView.setSelection(i);
                        }
                    }

                    timer5[0] = new Timer();
                    TimerTask task = new TimerTask() {
                        @Override
                        public void run() {
                            now = subtitleActivity.getAudioPosition();
                            if (now - subtitleActivity.getMyRecordBean().getEndPosition() > 300) {
                                mVideoView.pause();
                                timer5[0].cancel();
                                now = 0;
                            }
                        }
                    };
                    timer5[0].schedule(task, 0, 1);
                } else {
                    list = subtitleActivity.getList();
                    mVideoView = subtitleActivity.getVideoView();
                    if (list.size() > 0) {
                        subtitleActivity.startPlay(list.get(0).getShowTime().getBegin());


                    } else {
                        return;
                    }
                    final Timer timer = new Timer();


                    TimerTask task = new TimerTask() {
                        @Override
                        public void run() {
                            now = subtitleActivity.getAudioPosition();
                            if (now - list.get(list.size() - 1).getShowTime().getEnd() > 300) {
                                mVideoView.pause();
                                timer.cancel();
                                now = 0;
                            }
                        }
                    };
                    timer.schedule(task, 0, 1);
                }
            }
        });


        // 我的录音
        rootView.findViewById(R.id.tv_wodeluyin).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (ttsIflytek != null) {
                    if (ttsIflytek.getSpeechSynthesizer().isSpeaking()) {
                        ttsIflytek.getSpeechSynthesizer().stopSpeaking();
                        ttsIflytek = null;
                    }
                }
                if (timer5[0] != null) {
                    timer5[0].cancel();
                }
                subtitleActivity.soundPlay();
                // 在我的配音中点击我的录音时
                if (subtitleActivity.getMyRecordBean() != null) {
                    for (int i = 0; i < subtitleActivity.getList().size(); i++) {
                        int begin = subtitleActivity.getList().get(i).getShowTime().getBegin();
                        int end = subtitleActivity.getList().get(i).getShowTime().getEnd();
                        if (subtitleActivity.getMyRecordBean().getStartPosition() >= begin
                                && subtitleActivity.getMyRecordBean().getStartPosition() <= end) {
                            subtitleActivity.subtitleListView.setSelection(i);
                        }
                    }


                    // (我的配音) 如果限制时间
                    if (PlayConfig.isRecordLimitTime == true) {
                        mVideoView = subtitleActivity.getVideoView();
                        subtitleActivity.startPlay(subtitleActivity.getMyRecordBean().getStartPosition());
                        timer1[0] = new Timer();
                        TimerTask task = new TimerTask() {
                            @Override
                            public void run() {
                                now = subtitleActivity.getAudioPosition();
                                for (int i = 0; i < rolePlayRecordList.size(); i++) {
                                    if (now - rolePlayRecordList.get(i).getShowTime().getBegin() < 5 && now - rolePlayRecordList.get(i).getShowTime().getBegin() > -5) {
                                        subtitleActivity.mutePlay();
                                        if (mediaPlayer == null) {
                                            mediaPlayer = new MediaPlayer();
                                        }
                                        if (i == 0) {
                                            startPlayRecord(i);
                                        } else {
                                            if (Completion == true) {
                                                startPlayRecord(i);
                                            }
                                        }

                                    }
                                    if (Completion == true) {
                                        mVideoView.start();
                                        subtitleActivity.soundPlay();
                                    }
                                }
                                if (now - subtitleActivity.getMyRecordBean().getEndPosition() > 300) {
                                    mVideoView.pause();
                                    timer1[0].cancel();
                                    mediaPlayer = null;
                                    now = 0;
                                }
                            }
                        };
                        timer1[0].schedule(task, 0, 1);
                        // (我的配音) 如果不限制时间
                    } else {
                        final int[] now2 = new int[1];
                        mVideoView = subtitleActivity.getVideoView();
                        subtitleActivity.startPlay(subtitleActivity.getMyRecordBean().getStartPosition());
                        timer2[0] = new Timer();
                        TimerTask timerTask = new TimerTask() {
                            @Override
                            public void run() {
                                now = subtitleActivity.getAudioPosition();
                                for (int i = 0; i < rolePlayRecordList.size(); i++) {
                                    if (recordFileMap != null && rolePlayRecordList != null && rolePlayRecordList.get(i) != null && rolePlayRecordList.get(i).getItem() > 0) {
                                        final File f = new File(recordFileMap.get(rolePlayRecordList.get(i).getItem()));
                                        try {
                                            if (GetAmrDuration.getAmrDuration(f) > (rolePlayRecordList.get(i).getShowTime().getEnd() - rolePlayRecordList.get(i).getShowTime().getBegin())) {
                                                if (now - rolePlayRecordList.get(i).getShowTime().getBegin() < 5 && now - rolePlayRecordList.get(i).getShowTime().getBegin() > -5) {
                                                    subtitleActivity.mutePlay();
                                                    if (mediaPlayer == null) {
                                                        mediaPlayer = new MediaPlayer();
                                                    }
                                                    if (i == 0) {
                                                        startPlayRecord(i);
                                                    } else {
                                                        if (Completion == true) {
                                                            startPlayRecord(i);
                                                        }
                                                    }
                                                }
                                                if (now - rolePlayRecordList.get(i).getShowTime().getEnd() < 5 && now - rolePlayRecordList.get(i).getShowTime().getEnd() > -5) {
                                                    mVideoView.pause();
                                                }
                                                if (subtitleActivity.isVideoPlaying() == true) {
                                                    now = subtitleActivity.getAudioPosition();
                                                    if (now > now2[0]) {
                                                        now2[0] = now;
                                                    }
                                                }
                                                if (mVideoView.isPlaying() == false) {
                                                    now2[0] += 1;
                                                }
                                                if (Completion == true) {
                                                    mVideoView.start();
                                                    subtitleActivity.soundPlay();
                                                }
                                            } else {
                                                if (now - rolePlayRecordList.get(i).getShowTime().getBegin() < 5 && now - rolePlayRecordList.get(i).getShowTime().getBegin() > -5) {
                                                    subtitleActivity.mutePlay();
                                                    if (mediaPlayer == null) {
                                                        mediaPlayer = new MediaPlayer();
                                                    }
                                                    if (i == 0) {
                                                        startPlayRecord(i);
                                                    } else {
                                                        if (Completion == true) {
                                                            startPlayRecord(i);
                                                        }
                                                    }
                                                }
                                                if (Completion == true) {
                                                    subtitleActivity.soundPlay();
                                                }
                                            }
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                                if (now - subtitleActivity.getMyRecordBean().getEndPosition() > 300) {
                                    mVideoView.pause();
                                    timer2[0].cancel();
                                    mediaPlayer = null;
                                    now = 0;
                                    now2[0] = 0;
                                }
                            }
                        };
                        timer2[0].schedule(timerTask, 0, 1);
                    }
                    // 在观影界面点击我的录音时
                } else {
                    // (观影界面) 如果限制时间
                    if (PlayConfig.isRecordLimitTime == true) {
                        list = subtitleActivity.getList();
                        mVideoView = subtitleActivity.getVideoView();
                        subtitleActivity.startPlay(list.get(0).getShowTime().getBegin());
                        timer3[0] = new Timer();
                        TimerTask task = new TimerTask() {
                            @Override
                            public void run() {
                                now = subtitleActivity.getAudioPosition();
                                for (int i = 0; i < rolePlayRecordList.size(); i++) {
                                    if (now - rolePlayRecordList.get(i).getShowTime().getBegin() < 5 && now - rolePlayRecordList.get(i).getShowTime().getBegin() > -5) {
                                        subtitleActivity.mutePlay();
                                        if (mediaPlayer == null) {
                                            mediaPlayer = new MediaPlayer();
                                        }
                                        if (i == 0) {
                                            Completion = false;
                                            startPlayRecord(i);
                                        } else {
                                            if (Completion == true) {
                                                Completion = false;
                                                startPlayRecord(i);
                                            }
                                        }
                                    }
                                    if (Completion == true) {
                                        mVideoView.start();
                                        subtitleActivity.soundPlay();
                                    }
                                }
                                if (now - list.get(list.size() - 1).getShowTime().getEnd() > 300) {
                                    mVideoView.pause();
                                    timer3[0].cancel();
                                    mediaPlayer = null;
                                    now = 0;
                                }
                            }
                        };
                        timer3[0].schedule(task, 0, 1);
                        // (观影界面) 如果不限制时间
                    } else {//                            startPlayRecord(i);
                        final int[] now2 = new int[1];
                        list = subtitleActivity.getList();
                        mVideoView = subtitleActivity.getVideoView();
                        subtitleActivity.startPlay(list.get(0).getShowTime().getBegin());
                        timer4[0] = new Timer();
                        TimerTask timerTask = new TimerTask() {
                            @Override
                            public void run() {
                                now = subtitleActivity.getAudioPosition();
                                for (int i = 0; i < rolePlayRecordList.size(); i++) {
                                    if (recordFileMap != null && recordFileMap.size() > 0 && rolePlayRecordList != null && rolePlayRecordList.get(i) != null && rolePlayRecordList.get(i).getItem() > 0) {
                                        final File f = new File(recordFileMap.get(rolePlayRecordList.get(i).getItem()));
                                        try {
                                            if (GetAmrDuration.getAmrDuration(f) > (rolePlayRecordList.get(i).getShowTime().getEnd() - rolePlayRecordList.get(i).getShowTime().getBegin())) {
                                                if (now - rolePlayRecordList.get(i).getShowTime().getBegin() < 5 && now - rolePlayRecordList.get(i).getShowTime().getBegin() > -5) {
                                                    subtitleActivity.mutePlay();
                                                    if (mediaPlayer == null) {
                                                        mediaPlayer = new MediaPlayer();
                                                    }
                                                    if (i == 0) {
                                                        startPlayRecord(i);
                                                    } else {
                                                        if (Completion == true) {
                                                            startPlayRecord(i);
                                                        }
                                                    }
                                                }
                                                if (now - rolePlayRecordList.get(i).getShowTime().getEnd() < 5 && now - rolePlayRecordList.get(i).getShowTime().getEnd() > -5) {
                                                    mVideoView.pause();
                                                }
                                                if (subtitleActivity.isVideoPlaying() == true) {
                                                    now = subtitleActivity.getAudioPosition();
                                                    if (now > now2[0]) {
                                                        now2[0] = now;
                                                    }
                                                }
                                                if (mVideoView.isPlaying() == false) {
                                                    now2[0] += 1;
                                                }
                                                if (Completion == true) {
                                                    mVideoView.start();
                                                    subtitleActivity.soundPlay();
                                                }
                                            } else {
                                                if (now - rolePlayRecordList.get(i).getShowTime().getBegin() < 5 && now - rolePlayRecordList.get(i).getShowTime().getBegin() > -5) {
                                                    subtitleActivity.mutePlay();
                                                    if (mediaPlayer == null) {
                                                        mediaPlayer = new MediaPlayer();
                                                    }
                                                    if (i == 0) {
                                                        startPlayRecord(i);
                                                    } else {
                                                        if (Completion == true) {
                                                            startPlayRecord(i);
                                                        }
                                                    }
                                                }
                                                if (Completion == true) {
                                                    subtitleActivity.soundPlay();
                                                }
                                            }
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                                if (now - list.get(list.size() - 1).getShowTime().getEnd() > 300) {
                                    Log.i("TAG", "sad465sa1v3xce68f73 = Stop");
                                    mVideoView.pause();
                                    timer4[0].cancel();
                                    mediaPlayer = null;
                                    now = 0;
                                    now2[0] = 0;
                                }
                            }
                        };
                        timer4[0].schedule(timerTask, 0, 1);
                    }
                }
            }
        });
        // 正音朗读
        rootView.findViewById(R.id.tv_zhengyinlangdu).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timer1[0] != null) {
                    timer1[0].cancel();
                }
                if (timer2[0] != null) {
                    timer2[0].cancel();
                }
                if (timer3[0] != null) {
                    timer3[0].cancel();
                }
                if (timer4[0] != null) {
                    timer4[0].cancel();
                }
                if (timer5[0] != null) {
                    timer5[0].cancel();
                }
                correctBtnClick(v, 1);
            }
        });
        // 退出
        rootView.findViewById(R.id.tv_back).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                subtitleActivity.startPlay(subtitleActivity.getP());
                subtitleActivity.setFalg(false);
                interruptRecord = true;
                if (!isSave && PlayConfig.roType == RoType.COMPLETE_RECORD) {
                    final PromptDialog dialog = new PromptDialog(subtitleActivity);
                    dialog.setContent("是否保存本次录音");
                    dialog.show();
                    dialog.setClickListener(new onPromptClickListener() {
                        @Override
                        public void onCancel(View v) {
                            dialog.dismiss();
                            clearCurrentRecord();
                        }

                        @Override
                        public void onDetermine(View v) {
                            dialog.dismiss();
                            saveCurrentRecord();
                        }
                    });
                } else if (PlayConfig.roType != RoType.COMPLETE_RECORD) {
                    clearCurrentRecord();
                }
                if (onWindowStateListener != null) {
                    onWindowStateListener.windowInternalClose();
                }
                dismiss();
            }
        });
        // 保存
        rootView.findViewById(R.id.tv_save).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCurrentRecord();
                ToastTool.showToastLong(v.getContext(), "保存成功");
                ((TextView) rootView.findViewById(R.id.tv_save)).setText("录音已保存");
                v.setClickable(false);
            }
        });
    }

    /**
     * 完成状态按钮
     *
     * @param view
     */
    private void completeStateClick(View view) {
        if (PlayConfig.roType == RoType.START) {
            refreshHandler.sendEmptyMessage(RecordRefreshHandler.RECORD_PREARE);
        } else if (PlayConfig.roType == RoType.SENTENCE_END) {
            if ((currentPisition + 1) > rolePlayRecordList.size() - 1) {
                refreshHandler.sendEmptyMessage(RecordRefreshHandler.RECORD_COMPLETE);
            } else {
                currentPisition += 1;
                refreshHandler.sendEmptyMessage(RecordRefreshHandler.RECORD_PREARE);
            }
        }


    }

    /**
     * 操作按钮
     *
     * @param view
     */
    private void operationClick(View view) {
        if (PlayConfig.roType == RoType.COMPLETE_RECORD) {
            isSave = true;
            saveCurrentRecord();

            final PromptDialog dialog = new PromptDialog(subtitleActivity);
            dialog.setContent("保存成功");
            dialog.show();
            dialog.setClickListener(new onPromptClickListener() {
                @Override
                public void onCancel(View v) {
                    dialog.dismiss();
                }

                @Override
                public void onDetermine(View v) {
                    dialog.dismiss();
                }
            });
        } else if (PlayConfig.roType == RoType.SENTENCE_END) {
            final PromptDialog dialog = new PromptDialog(subtitleActivity);
            dialog.setContent("角色对话未完成,确定要结束对话吗？");
            dialog.show();
            dialog.setClickListener(new onPromptClickListener() {
                @Override
                public void onCancel(View v) {
                    dialog.dismiss();
                }

                @Override
                public void onDetermine(View v) {
                    dialog.dismiss();
                    clearCurrentRecord();
                }
            });
        }
    }

    /**
     * 点击正音朗读
     *
     * @param view
     */
    private void correctBtnClick(View view, int opt) {
        subtitleBeanList = subtitleActivity.getSubtitleBeanList();
        if (subtitleActivity.getMyRecordBean() != null) {
            if (subtitleBeanList.size() > 0 && subtitleBeanList != null) {
                if (opt == 0) {
                    if ((currentPisition + 1) > subtitleBeanList.size() - 1) {
                        refreshHandler.sendEmptyMessage(RecordRefreshHandler.RECORD_COMPLETE);
                    } else {
                        currentPisition += 1;
                        refreshHandler.sendEmptyMessage(RecordRefreshHandler.RECORD_PREARE);
                    }
                } else {
                    String subtitle = "";
                    if (PlayConfig.roType == RoType.COMPLETE_RECORD) {
                        for (int i = 0; i < subtitleBeanList.size(); i++) {
                            if (subtitleBeanList.get(i).getShowTime().getEnd() == subtitleActivity.getMyRecordBean().getEndPosition()) {
                                subtitle += subtitleBeanList.get(i).getEnglish();
                                break;
                            }
                            if (subtitleBeanList.get(i).getShowTime().getBegin() >= subtitleActivity.getMyRecordBean().getStartPosition()) {
                                subtitle += subtitleBeanList.get(i).getEnglish() + "\r\n";
                            }
                        }
                    } else {
                        subtitle = subtitleBeanList.get(currentPisition).getEnglish();
                    }
                    if (ttsIflytek == null) {
                        ttsIflytek = new TTSIflytek(subtitleActivity);
                    }
                    if (ttsIflytek.getSpeechSynthesizer().isSpeaking()) {
                        ttsIflytek.getSpeechSynthesizer().stopSpeaking();
                    }
                    if (subtitleActivity.isVideoPlaying()) {
                        subtitleActivity.pausePlay();
                    }
                    if (mediaPlayer != null) {
                        try {
                            mediaPlayer.stop();
                            mediaPlayer.release();
                            mediaPlayer = null;
                            subtitleActivity.soundPlay();
                        } catch (RuntimeException e) {
                            JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());
                        }
                    }

                    for (int i = 0; i < subtitleActivity.getList().size(); i++) {
                        String english = subtitleActivity.getList().get(i).getEnglish();
                        String[] split = subtitle.split("[\r\n]");
                        for (String s : split) {
                            if (s.equals(english)) {
                                subtitleActivity.subtitleListView.setSelection(i);
//                                subtitleActivity.subtitleListView.getAdapter().
                            }
                        }
                    }
                    ttsIflytek.setTextflytek(subtitle);

                }
            }
        } else {
            list = subtitleActivity.getList();
            if (opt == 0) {
                if ((currentPisition + 1) > list.size() - 1) {
                    refreshHandler.sendEmptyMessage(RecordRefreshHandler.RECORD_COMPLETE);
                } else {
                    currentPisition += 1;
                    refreshHandler.sendEmptyMessage(RecordRefreshHandler.RECORD_PREARE);
                }
            } else {
                String subtitle = "";
                if (PlayConfig.roType == RoType.COMPLETE_RECORD) {
                    for (int i = 0; i < list.size(); i++) {
                        subtitle += list.get(i).getEnglish();
                        if (i < list.size() - 1) {
                            subtitle += "\r\n";
                        }
                    }
                } else {
                    subtitle = list.get(currentPisition).getEnglish();
                }
                if (ttsIflytek == null) {
                    ttsIflytek = new TTSIflytek(subtitleActivity);
                }
                if (ttsIflytek.getSpeechSynthesizer().isSpeaking()) {
                    ttsIflytek.getSpeechSynthesizer().stopSpeaking();
                }
                if (subtitleActivity.isVideoPlaying()) {
                    subtitleActivity.pausePlay();
                }
                if (mediaPlayer != null) {
                    try {
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        mediaPlayer = null;
                        subtitleActivity.soundPlay();
                    } catch (RuntimeException e) {
                        JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());
                    }
                }
                for (int i = 0; i < subtitleActivity.getList().size(); i++) {
                    String english = subtitleActivity.getList().get(i).getEnglish();
                    String[] split = subtitle.split("[\r\n]");
                    List<String> strings = new ArrayList<>();
                    for (int j = 0; j < split.length; j++) {
                        if (split[j].equals("")) {
                            continue;
                        }
                        strings.add(split[j]);
                    }
                    for (String s : strings) {
                        if (s.equals(english)) {
                            subtitleActivity.subtitleListView.setSelection(i);
                        }
                    }
                }
                ttsIflytek.setTextflytek(subtitle);
            }
        }
    }

    /**
     * 点击试听
     *
     * @param view
     */
    private VideoView mVideoView;
    private Boolean Completion = false;
    private int now = 0;

    private void auditionBtnClick(View view) {
        if (PlayConfig.roType == RoType.SENTENCE_END) {
            startPlayRecord(currentPisition);
            Log.e(TAG, String.valueOf(currentPisition));
        } else if (PlayConfig.roType == RoType.COMPLETE_RECORD) {
            startPlayRecord(0);
        }
    }

    /**
     * 开始播放录音
     */
    private void startPlayRecord(final int position) {
        Completion = false;
        if (ttsIflytek != null && ttsIflytek.getSpeechSynthesizer().isSpeaking()) {
            ttsIflytek.getSpeechSynthesizer().stopSpeaking();
        }

        if (mediaPlayer != null) {
            try {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            } catch (RuntimeException e) {
                JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());
            }
        }

        mediaPlayer = new MediaPlayer();
        try {
            String recordUrl = recordFileMap.get(rolePlayRecordList.get(position).getItem());
            Log.i("987454846153156416", "1049663042 播放了 = " + recordFileMap.get(rolePlayRecordList.get(position).getItem()));
            mediaPlayer.setDataSource(recordUrl);
            mediaPlayer.prepare();
            mediaPlayer.start();
            mediaPlayer.setOnPreparedListener(new OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {

                }
            });
            mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    try {
                        if (mediaPlayer != null) {
                            if (mediaPlayer.isPlaying()) {
                                mediaPlayer.stop();
                            }
                            mediaPlayer.release();
                        }
                        if (PlayConfig.roType == RoType.COMPLETE_RECORD) {
                            if ((position + 1) < rolePlayRecordList.size()) {
                                mediaPlayer = null;
                                startPlayRecord(position + 1);
                            }
                        }
                        if (mediaPlayer != null) {
                            mediaPlayer.pause();
                        }
                    } catch (IllegalStateException e) {
                        JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());
                    }
                    Completion = true;

                }
            });
            mediaPlayer.setOnErrorListener(new OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    JLog.e("Error code=" + what + ",extra=" + extra);
                    return false;
                }
            });
        } catch (IllegalArgumentException e) {
            JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());
        } catch (SecurityException e) {
            JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());
        } catch (IllegalStateException e) {
            JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());
        } catch (IOException e) {
            JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());
        } catch (NullPointerException e) {
            JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());
        }
    }

    /**
     * 重录
     *
     * @param view
     */
    private void retakeBtnClick(View view) {
        if (PlayConfig.roType == RoType.COMPLETE_RECORD) {
            currentPisition = 0;
        }
        completeStateVisible(false);
        countDownProgressBar.setVisibility(View.VISIBLE);
        countDownProgressBar.setMax(maxTime);
        countDownProgressBar.setProgress(maxTime);
        stateFuncText.setText("准备好,马上该你了!");
        stateFuncText.setTextColor(0xffFFFF00);
        completeStateText.setText("录音倒计时");
        operationText.setText("OK");
        // 准备好,马上该你了!
        subtitleActivity.seekTo(rolePlayRecordList.get(currentPisition).getShowTime().getBegin());
        refreshHandler.sendEmptyMessage(RecordRefreshHandler.RECORD_COUNT_DOWN);
    }

    boolean flag = false;

    @SuppressLint("HandlerLeak")
    private class RecordRefreshHandler extends Handler {
        // 准备录音
        public static final int RECORD_PREARE = 41;
        // 倒计时
        public static final int RECORD_COUNT_DOWN = 42;
        // 录音开始
        public static final int RECORD_TAPE = 43;
        // 句子录音结束
        public static final int RECORD_SENTENCE_END = 44;
        // 录音完成
        public static final int RECORD_COMPLETE = 45;

        // 修改倒计时
        public static final int UPDATE_COUNT_DOWN = 46;
        // 录音监听刷新时间
        public static final int LISTENER_TIME = 300;
        // 录音开始监听
        public static final int RECORD_TAPE_LISTENER = 47;
        public static final int aa = 48;
        public static final int bb = 49;

        int item;
        int playPosition;

        @Override
        public void handleMessage(Message msg) {
            if (interruptRecord) {
                return;
            }
            switch (msg.what) {
                case bb:
                    flag = true;
                    completeStateText.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            subtitleActivity.mutePlay();
                            Log.i("TAG", "jmy22222222222222222222222222222");
                            if (completeStateText.getText().toString().equals("完成本句")) {
                                completeStateText.setEnabled(true);
                                completeStateClick(v);
                            } else {
                                subtitleActivity.pausePlay();
                                recordTape();
                            }
                        }
                    });
                    break;
                case aa:
                    flag = true;
                    recordPreare();
                    break;
                case RECORD_PREARE://准备好,马上该你了!
                    if (!flag) {
                        if (subtitleActivity.isVideoPlaying()) {
                            subtitleActivity.stopPlay();
                        }
                        stateFuncText.setText("点击开始配音");
                        stateFuncText.setTextColor(0xffFFFF00);
                        completeStateText.setText("开始配音");
                        rootView.findViewById(R.id.ll_top_recordPop).setVisibility(View.GONE);
                        rootView.findViewById(R.id.ll_function_recordPop).setVisibility(View.GONE);
                        completeStateText.setEnabled(true);
                        completeStateText.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                completeStateText.setEnabled(false);
                                PlayConfig.roType = RoType.PREARE_RECORD;
                                stateFuncText.setText("电影原音播放ing");
                                stateFuncText.setTextColor(0xffFFFF00);
                                if (rolePlayRecordList.size() > 0 && subtitleActivity.getList().size() > 0) {
                                    //如果是要录音的第一句
                                    if (currentPisition == 0) {
                                        //循环所有句子
                                        for (int i = 0; i < subtitleActivity.getList().size(); i++) {
                                            //查询当前视频进度属于哪个句子
                                            if (subtitleActivity.getAudioPosition() >= subtitleActivity.getList().get(i).getShowTime().getBegin() && subtitleActivity.getAudioPosition() <= subtitleActivity.getList().get(i).getShowTime().getEnd()) {
                                                //提取这个句子的Item
                                                item = subtitleActivity.getList().get(i).getItem();
                                            }
                                        }
                                        //如果要录音的第一句等于所有句子的第一句
                                        Log.i("TAG", "jinlemujinlemujinlemujinlemu item 01 = " + item);
                                        Log.i("TAG", "jinlemujinlemujinlemujinlemu item 02 = " + rolePlayRecordList.get(0).getItem());
                                        if (item == rolePlayRecordList.get(0).getItem()) {
                                            //直接录
                                            if (!PlayConfig.isBeginPlayPause) {
                                                refreshHandler.sendEmptyMessage(RecordRefreshHandler.aa);
                                                Log.i("TAG", "jinlemujinlemujinlemujinlemu 1 =  进了");
                                            } else {
                                                Log.i("TAG", "jinlemujinlemujinlemujinlemu 01 =  进了 需要点击");
                                                refreshHandler.sendEmptyMessage(RecordRefreshHandler.bb);
                                            }
                                        } else {//如果要录音的第一句不是所有句子的第一句
                                            //先播放视频
                                            subtitleActivity.startPlay(subtitleActivity.getAudioPosition());
//                                mVideoView.start();
                                            //启动Timer，
                                            final Timer timer = new Timer();
                                            TimerTask timerTask = new TimerTask() {
                                                @Override
                                                public void run() {
                                                    //每一毫秒获取一次视频当前进度
                                                    now = subtitleActivity.getAudioPosition();
                                                    //当now大于要录音的第一句的开始位置
                                                    if (rolePlayRecordList.size() > 0) {
                                                        if (now > rolePlayRecordList.get(0).getShowTime().getBegin()) {
                                                            //进入 case RECORD_PREARE
                                                            //关闭timer
                                                            if (PlayConfig.isBeginPlayPause == false) {
                                                                refreshHandler.sendEmptyMessage(RecordRefreshHandler.aa);
                                                                Log.i("TAG", "jinlemujinlemujinlemujinlemu 2 =  进了");
//                                                }
                                                            } else {
                                                                Log.i("TAG", "jinlemujinlemujinlemujinlemu 02 =  进了 需要点击");
                                                                refreshHandler.sendEmptyMessage(RecordRefreshHandler.bb);
                                                            }
                                                            timer.cancel();
                                                            now = 0;
                                                        }
                                                    }

                                                }
                                            };
                                            timer.schedule(timerTask, 0, 1);
                                        }
                                    } else {
                                        //如果前一句要录音的item+1不等于这一句要录音的
                                        if (rolePlayRecordList.get(currentPisition - 1).getItem() + 1 != rolePlayRecordList.get(currentPisition).getItem()) {
                                            //循环所有句子
                                            for (int i = 0; i < subtitleActivity.getList().size(); i++) {
                                                //查询当前视频进度属于哪个句子
                                                if (subtitleActivity.getAudioPosition() >= subtitleActivity.getList().get(i).getShowTime().getBegin() && subtitleActivity.getAudioPosition() <= subtitleActivity.getList().get(i).getShowTime().getEnd()) {
                                                    //提取这个句子的Item
                                                    item = subtitleActivity.getList().get(i).getItem();
                                                }
                                            }
                                            if (item == rolePlayRecordList.get(currentPisition).getItem()) {
                                                if (PlayConfig.isBeginPlayPause == false) {
                                                    refreshHandler.sendEmptyMessage(RecordRefreshHandler.aa);
                                                    Log.i("TAG", "jinlemujinlemujinlemujinlemu 3 =  进了");
//                                    }
                                                } else {
                                                    Log.i("TAG", "jinlemujinlemujinlemujinlemu 03 =  进了 需要点击");
                                                    refreshHandler.sendEmptyMessage(RecordRefreshHandler.bb);
                                                }
                                            } else {
                                                for (int i = 0; i < subtitleActivity.getList().size(); i++) {
                                                    if (subtitleActivity.getList().get(i).getItem() == rolePlayRecordList.get(currentPisition - 1).getItem() + 1) {
                                                        playPosition = subtitleActivity.getList().get(i).getShowTime().getBegin();
                                                    }
                                                }
                                                stateFuncText.setText("电影原音播放ing");
                                                completeStateText.setText("准备录音");
                                                stateFuncText.setTextColor(0xffFFFF00);
                                                //先播放视频
                                                subtitleActivity.startPlay(playPosition);
//                                    mVideoView.start();
                                                //启动Timer，当now等于要录音的第一句的开始位置
                                                final Timer timer = new Timer();
                                                TimerTask timerTask = new TimerTask() {
                                                    @Override
                                                    public void run() {
                                                        now = subtitleActivity.getAudioPosition();
                                                        if (rolePlayRecordList.size() > 0) {
                                                            if (now > rolePlayRecordList.get(currentPisition).getShowTime().getBegin()) {
                                                                //进入 case RECORD_PREARE
                                                                //关闭timer
                                                                if (PlayConfig.isBeginPlayPause == false) {
                                                                    refreshHandler.sendEmptyMessage(RecordRefreshHandler.aa);
                                                                    Log.i("TAG", "jinlemujinlemujinlemujinlemu 4 =  进了");
//                                                    }
                                                                } else {
                                                                    Log.i("TAG", "jinlemujinlemujinlemujinlemu 04 =  进了 需要点击");
                                                                    refreshHandler.sendEmptyMessage(RecordRefreshHandler.bb);
                                                                }
                                                                timer.cancel();
                                                                now = 0;
                                                            }
                                                        }
                                                    }
                                                };
                                                timer.schedule(timerTask, 0, 1);
                                            }
                                            //进入 case RECORD_PREARE
                                        } else {//如果前一句要录音的item+1等于这一句要录音的
                                            //直接录
                                            if (PlayConfig.isBeginPlayPause == false) {
                                                Log.i("TAG", "jinlemujinlemujinlemujinlemu 5 =  进了");
                                                refreshHandler.sendEmptyMessage(RecordRefreshHandler.aa);
//                                }
                                            } else {
                                                Log.i("TAG", "jinlemujinlemujinlemujinlemu 05 =  进了 需要点击");
                                                refreshHandler.sendEmptyMessage(RecordRefreshHandler.bb);
                                            }
                                        }
                                    }
                                }
                            }
                        });
                    } else {
                        PlayConfig.roType = RoType.PREARE_RECORD;
                        stateFuncText.setText("电影原音播放ing");
                        stateFuncText.setTextColor(0xffFFFF00);
                        rootView.findViewById(R.id.ll_top_recordPop).setVisibility(View.GONE);
                        rootView.findViewById(R.id.ll_function_recordPop).setVisibility(View.GONE);
                        if (rolePlayRecordList.size() > 0 && subtitleActivity.getList().size() > 0) {
                            //如果是要录音的第一句
                            if (currentPisition == 0) {
                                //循环所有句子
                                for (int i = 0; i < subtitleActivity.getList().size(); i++) {
                                    //查询当前视频进度属于哪个句子
                                    if (subtitleActivity.getAudioPosition() >= subtitleActivity.getList().get(i).getShowTime().getBegin() && subtitleActivity.getAudioPosition() <= subtitleActivity.getList().get(i).getShowTime().getEnd()) {
                                        //提取这个句子的Item
                                        item = subtitleActivity.getList().get(i).getItem();
                                    }
                                }
                                //如果要录音的第一句等于所有句子的第一句
                                Log.i("TAG", "jinlemujinlemujinlemujinlemu item 01 = " + item);
                                Log.i("TAG", "jinlemujinlemujinlemujinlemu item 02 = " + rolePlayRecordList.get(0).getItem());
                                if (item == rolePlayRecordList.get(0).getItem()) {
                                    //直接录
                                    if (PlayConfig.isBeginPlayPause == false) {
                                        refreshHandler.sendEmptyMessage(RecordRefreshHandler.aa);
                                        Log.i("TAG", "jinlemujinlemujinlemujinlemu 1 =  进了");
                                    } else {
                                        Log.i("TAG", "jinlemujinlemujinlemujinlemu 01 =  进了 需要点击");
                                        refreshHandler.sendEmptyMessage(RecordRefreshHandler.bb);
                                    }
                                } else {//如果要录音的第一句不是所有句子的第一句
                                    //先播放视频
                                    subtitleActivity.startPlay(subtitleActivity.getAudioPosition());
//                                mVideoView.start();
                                    //启动Timer，
                                    final Timer timer = new Timer();
                                    TimerTask timerTask = new TimerTask() {
                                        @Override
                                        public void run() {
                                            //每一毫秒获取一次视频当前进度
                                            now = subtitleActivity.getAudioPosition();
                                            //当now大于要录音的第一句的开始位置
                                            if (rolePlayRecordList.size() > 0) {
                                                if (now > rolePlayRecordList.get(0).getShowTime().getBegin()) {
                                                    //进入 case RECORD_PREARE
                                                    //关闭timer
                                                    if (PlayConfig.isBeginPlayPause == false) {
                                                        refreshHandler.sendEmptyMessage(RecordRefreshHandler.aa);
                                                        Log.i("TAG", "jinlemujinlemujinlemujinlemu 2 =  进了");
//                                                }
                                                    } else {
                                                        Log.i("TAG", "jinlemujinlemujinlemujinlemu 02 =  进了 需要点击");
                                                        refreshHandler.sendEmptyMessage(RecordRefreshHandler.bb);
                                                    }
                                                    timer.cancel();
                                                    now = 0;
                                                }
                                            }

                                        }
                                    };
                                    timer.schedule(timerTask, 0, 1);
                                }
                            } else {
                                //如果前一句要录音的item+1不等于这一句要录音的
                                if (rolePlayRecordList.get(currentPisition - 1).getItem() + 1 != rolePlayRecordList.get(currentPisition).getItem()) {
                                    //循环所有句子
                                    for (int i = 0; i < subtitleActivity.getList().size(); i++) {
                                        //查询当前视频进度属于哪个句子
                                        if (subtitleActivity.getAudioPosition() >= subtitleActivity.getList().get(i).getShowTime().getBegin() && subtitleActivity.getAudioPosition() <= subtitleActivity.getList().get(i).getShowTime().getEnd()) {
                                            //提取这个句子的Item
                                            item = subtitleActivity.getList().get(i).getItem();
                                        }
                                    }
                                    if (item == rolePlayRecordList.get(currentPisition).getItem()) {
                                        if (PlayConfig.isBeginPlayPause == false) {
                                            refreshHandler.sendEmptyMessage(RecordRefreshHandler.aa);
                                            Log.i("TAG", "jinlemujinlemujinlemujinlemu 3 =  进了");
//                                    }
                                        } else {
                                            Log.i("TAG", "jinlemujinlemujinlemujinlemu 03 =  进了 需要点击");
                                            refreshHandler.sendEmptyMessage(RecordRefreshHandler.bb);
                                        }
                                    } else {
                                        for (int i = 0; i < subtitleActivity.getList().size(); i++) {
                                            if (subtitleActivity.getList().get(i).getItem() == rolePlayRecordList.get(currentPisition - 1).getItem() + 1) {
                                                playPosition = subtitleActivity.getList().get(i).getShowTime().getBegin();
                                            }
                                        }
                                        stateFuncText.setText("电影原音播放ing");
                                        completeStateText.setText("准备录音");
                                        stateFuncText.setTextColor(0xffFFFF00);
                                        //先播放视频
                                        subtitleActivity.startPlay(playPosition);
//                                    mVideoView.start();
                                        //启动Timer，当now等于要录音的第一句的开始位置
                                        final Timer timer = new Timer();
                                        TimerTask timerTask = new TimerTask() {
                                            @Override
                                            public void run() {
                                                now = subtitleActivity.getAudioPosition();
                                                if (rolePlayRecordList.size() > 0) {
                                                    if (now > rolePlayRecordList.get(currentPisition).getShowTime().getBegin()) {
                                                        //进入 case RECORD_PREARE
                                                        //关闭timer
                                                        if (PlayConfig.isBeginPlayPause == false) {
                                                            refreshHandler.sendEmptyMessage(RecordRefreshHandler.aa);
                                                            Log.i("TAG", "jinlemujinlemujinlemujinlemu 4 =  进了");
//                                                    }
                                                        } else {
                                                            Log.i("TAG", "jinlemujinlemujinlemujinlemu 04 =  进了 需要点击");
                                                            refreshHandler.sendEmptyMessage(RecordRefreshHandler.bb);
                                                        }
                                                        timer.cancel();
                                                        now = 0;
                                                    }
                                                }
                                            }
                                        };
                                        timer.schedule(timerTask, 0, 1);
                                    }
                                    //进入 case RECORD_PREARE
                                } else {//如果前一句要录音的item+1等于这一句要录音的
                                    //直接录
                                    if (PlayConfig.isBeginPlayPause == false) {
                                        Log.i("TAG", "jinlemujinlemujinlemujinlemu 5 =  进了");
                                        refreshHandler.sendEmptyMessage(RecordRefreshHandler.aa);
//                                }
                                    } else {
                                        Log.i("TAG", "jinlemujinlemujinlemujinlemu 05 =  进了 需要点击");
                                        refreshHandler.sendEmptyMessage(RecordRefreshHandler.bb);
                                    }
                                }
                            }
                        }
                    }
                    Log.i("TAG", "luyinsuoyoudeliucheng = 准备好,马上该你了!");
                    break;
                case RECORD_COUNT_DOWN://录音倒计时
                    PlayConfig.roType = RoType.COUNT_DOWN;
                    recordCountDown();
                    Log.i("TAG", "luyinsuoyoudeliucheng = 录音倒计时");
                    break;
                case RECORD_TAPE:
                    PlayConfig.roType = RoType.TAPE_RECORD;
                    try {
                        recordTape();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Log.i("TAG", "luyinsuoyoudeliucheng = 正在录音");
                    break;
                case RECORD_SENTENCE_END://准备录音
                    PlayConfig.roType = RoType.SENTENCE_END;
                    Log.i("TAG", "luyinsuoyoudeliucheng = 准备录音");
                    if (PlayConfig.isRecordLimitTime == true) {
                        recordSentenceEnd();
                    } else {
                        completeStateText.setText("完成本句");
                        completeStateText.setEnabled(true);
                        completeStateText.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                recordSentenceEnd();
                            }
                        });
                    }
                    break;
                case RECORD_COMPLETE://录音完成
                    PlayConfig.roType = RoType.COMPLETE_RECORD;
                    recordComplete();
                    Log.i("TAG", "luyinsuoyoudeliucheng = 录音完成");
                    Log.i("TAG", "luyinsuoyoudeliucheng = -------------------");
                    break;
                case UPDATE_COUNT_DOWN:
                    if (subtitleActivity.isVideoPlaying()) {
                        subtitleActivity.pausePlay();
                    }
                    if (msg.arg1 > 0) {
                        countDownProgressBar.setProgress(msg.arg1);
                    }
                    break;
                case RECORD_TAPE_LISTENER:
                    ShowTime showTime = rolePlayRecordList.get(currentPisition).getShowTime();
                    if (subtitleActivity.getAudioPosition() < showTime.getEnd()) {
                        sendEmptyMessageDelayed(RECORD_TAPE_LISTENER, LISTENER_TIME);
                    } else {
                        stateFuncText.setText("录音还在继续，录完点击完成本句");
                        stateFuncText.setTextColor(0xffff0000);
                        txt_xiaoyuandian.setVisibility(View.GONE);
                        subtitleActivity.pausePlay();
                        sendEmptyMessage(RECORD_SENTENCE_END);
                    }
                    break;
            }
        }

    }

    /**
     * 开始录音
     */
    private void recordStart() {
        completeStateVisible(false);
        countDownProgressBar.setVisibility(View.GONE);
        stateFuncText.setText("准备录制");
        stateFuncText.setTextColor(0xffFFFF00);
        completeStateText.setText("准备录音");
        operationText.setText("OK");
    }

    /**
     * 准备录音
     */
    private void recordPreare() {
        subtitleActivity.mutePlay();
        completeStateVisible(false);
        countDownProgressBar.setVisibility(View.VISIBLE);
        countDownProgressBar.setMax(maxTime);
        countDownProgressBar.setProgress(maxTime);
        stateFuncText.setText("准备好,马上该你了!");
        stateFuncText.setTextColor(0xffFFFF00);
        completeStateText.setText("录音倒计时");
        operationText.setText("OK");
        // 准备好,马上该你了!
        subtitleActivity.seekTo(rolePlayRecordList.get(currentPisition).getShowTime().getBegin());
        refreshHandler.sendEmptyMessage(RecordRefreshHandler.RECORD_COUNT_DOWN);
    }

    /**
     * 录音倒计时
     */
    private void recordCountDown() {
        now = 0;
        new Thread(new Runnable() {

            // 刷新时间
            private int refreshTime = 100;
            // 最大时间
            private int maxRefreshTime = maxTime;

            @Override
            public void run() {
                do {
                    try {
                        Thread.sleep(refreshTime);
                    } catch (InterruptedException e) {
                        JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());
                    }
                    // 修改显示倒计时时间
                    Message message = refreshHandler.obtainMessage();
                    message.what = RecordRefreshHandler.UPDATE_COUNT_DOWN;
                    message.arg1 = maxRefreshTime;
                    refreshHandler.sendMessage(message);
                    // 修改倒计时时间
                    maxRefreshTime -= refreshTime;
                } while (maxRefreshTime > 0);
                refreshHandler.sendEmptyMessage(RecordRefreshHandler.RECORD_TAPE);
            }
        }).start();
    }

    /**
     * 录音开始
     */
    private void recordTape() {
        if (isSave) {
            isSave = false;
        }
        completeStateVisible(false);
        countDownProgressBar.setVisibility(View.GONE);
        txt_xiaoyuandian.setVisibility(View.VISIBLE);
        stateFuncText.setText("正在录音...");
        stateFuncText.setTextColor(0xffff0000);
        completeStateText.setText("正在配音");
        completeStateText.setEnabled(false);
        operationText.setText("OK");
        // 静音
        subtitleActivity.mutePlay();
        // 播放视频
        if (currentPisition >= 0) {
            if ((rolePlayRecordList.get(currentPisition).getShowTime().getBegin()
                    - subtitleActivity.getAudioPosition()) > 1000) {
                subtitleActivity.startPlay(rolePlayRecordList.get(currentPisition).getShowTime().getBegin());
            } else {
                subtitleActivity.startPlay(-1);
            }
        } else {
            subtitleActivity.startPlay(rolePlayRecordList.get(currentPisition).getShowTime().getBegin());
        }
        startMediaRecorder();
        // 开始监听
//        if (PlayConfig.isRecordLimitTime == true) {
        refreshHandler.sendEmptyMessageDelayed(RecordRefreshHandler.RECORD_TAPE_LISTENER,
                RecordRefreshHandler.LISTENER_TIME);
        Log.i("TAG", "jmy111111111111111111111111111112");

    }

    /**
     * 开始录音对象录音
     */
    private void startMediaRecorder() {
        if (interruptRecord) {
            return;
        }
        if (mediaRecorder == null) {
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC); // 设置音频采集原
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP); // 内容输出格式
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB);
        }
        if (iseIflytek == null) {
            iseIflytek = new ISEIflytek(subtitleActivity);
        }
        String fileName = recordFolerUrl + File.separator + new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(System.currentTimeMillis());
        recordFileUrl = fileName + ".amr";
        Log.i("TAG", "luyinwenjiandangedelujing = " + recordFileUrl);
        mediaRecorder.setOutputFile(recordFileUrl);
        recordReviewUrl = fileName + ".wav";
        // 启动录音对象
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
            iseIflytek.setTextInput(rolePlayRecordList.get(currentPisition).getEnglish(), recordReviewUrl,
                    simpleEvaluatorListener);
        } catch (Exception e) {
            JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());

        }

    }

    /**
     * 本句录音结束
     */
    private void recordSentenceEnd() {
        // 结束录音对象
        endMediaRecorder();
        // 放音
        subtitleActivity.soundPlay();
        txt_xiaoyuandian.setVisibility(View.GONE);
        rootView.findViewById(R.id.dibuanniu).setVisibility(View.VISIBLE);
        rootView.findViewById(R.id.record_start_layout).setVisibility(View.VISIBLE);
        rootView.findViewById(R.id.record_comple_layout).setVisibility(View.GONE);
        if (PlayConfig.isCompletePausePlay) {
            completeStateVisible(true);
            countDownProgressBar.setVisibility(View.GONE);
            stateFuncText.setText("录制完成");
            stateFuncText.setTextColor(0xffFFFF00);
            completeStateText.setText("开始下一句");
            completeStateText.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if ((currentPisition + 1) > rolePlayRecordList.size() - 1) {
                        refreshHandler.sendEmptyMessage(RecordRefreshHandler.RECORD_COMPLETE);//wancheng
                        Log.i("TAG", "luyinsuoyoudeliucheng12 = 准备录音 完成");
                    } else {
                        currentPisition += 1;
                        refreshHandler.sendEmptyMessage(RecordRefreshHandler.RECORD_PREARE);//jixu
                        Log.i("TAG", "luyinsuoyoudeliucheng12 = 准备录音 继续");
                    }
                }
            });
            operationText.setText("OK");
        } else {
            if ((currentPisition + 1) > rolePlayRecordList.size() - 1) {
                refreshHandler.sendEmptyMessage(RecordRefreshHandler.RECORD_COMPLETE);//wancheng
                Log.i("TAG", "luyinsuoyoudeliucheng11 = 准备录音 完成");
            } else {
                currentPisition += 1;
                refreshHandler.sendEmptyMessage(RecordRefreshHandler.RECORD_PREARE);//jixu
                Log.i("TAG", "luyinsuoyoudeliucheng11 = 准备录音 继续");
            }
        }
    }

    /**
     * 结束录音对象录音
     */
    public void endMediaRecorder() {
        if (mediaRecorder == null) {
            return;
        }
        // 结束录音对象
        try {
            mediaRecorder.stop();
        } catch (RuntimeException e) {
            JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());
        } finally {
            mediaRecorder.release();
            mediaRecorder = null;
        }
        recordFileMap.put(rolePlayRecordList.get(currentPisition).getItem(), recordFileUrl);
        // 结束评测对象
        iseIflytek.stop();
    }

    /**
     * 录音完成
     */
    private void recordComplete() {
        flag = false;
        rootView.findViewById(R.id.record_start_layout).setVisibility(View.GONE);
        rootView.findViewById(R.id.record_comple_layout).setVisibility(View.VISIBLE);

        int totalScore = 0;
        for (Integer scoreKey : recordScoreMap.keySet()) {
            totalScore += recordScoreMap.get(scoreKey);
        }
        totalScore /= rolePlayRecordList.size();
        tvTotalScore.setText(totalScore + "分");
        completeStateVisible(true);
        countDownProgressBar.setVisibility(View.GONE);
        stateFuncText.setText("录音完成");
        stateFuncText.setTextColor(0xffFFFF00);
        completeStateText.setText("完成录制");
        operationText.setText("save");
//        if (PlayConfig.isRecordLimitTime == true) {
        if (subtitleActivity.getMyRecordBean() != null) {
            if (subtitleActivity.getAudioPosition() < subtitleActivity.getMyRecordBean().getEndPosition()) {
                subtitleActivity.startPlay(subtitleActivity.getAudioPosition());
                final Timer timer = new Timer();
                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        now = subtitleActivity.getAudioPosition();
                        if (now > subtitleActivity.getMyRecordBean().getEndPosition()) {
                            subtitleActivity.pausePlay();
                            timer.cancel();
                            now = 0;
                        }
                    }
                };
                timer.schedule(timerTask, 0, 1);
            }
        } else {
            if (subtitleActivity.getList().size() > 0) {
                if (subtitleActivity.getAudioPosition() < subtitleActivity.getList().get(subtitleActivity.getList().size() - 1).getShowTime().getEnd()) {//java.lang.ArrayIndexOutOfBoundsException: length=12; index=-1
                    subtitleActivity.startPlay(subtitleActivity.getAudioPosition());
                    final Timer timer = new Timer();
                    TimerTask timerTask = new TimerTask() {
                        @Override
                        public void run() {
                            now = subtitleActivity.getAudioPosition();
                            if (now > 0 && subtitleActivity.getList().size() > 0) {
                                if (now > subtitleActivity.getList().get(subtitleActivity.getList().size() - 1).getShowTime().getEnd()) {
                                    subtitleActivity.pausePlay();
                                    timer.cancel();
                                    now = 0;
                                }
                            }
                        }
                    };
                    timer.schedule(timerTask, 0, 1);
                }
            }
        }
//        }
    }

    /**
     * 设置完成状态可见
     *
     * @param isVisible
     */
    private void completeStateVisible(boolean isVisible) {
        if (isVisible) {
            scoreLayout.setVisibility(View.VISIBLE);
            currentRecordFuncLayout.setVisibility(View.VISIBLE);
        } else {
            scoreLayout.setVisibility(View.GONE);
            currentRecordFuncLayout.setVisibility(View.GONE);
        }
    }

    private SimpleEvaluatorListener simpleEvaluatorListener = new SimpleEvaluatorListener() {
        public void onResult(com.iflytek.cloud.EvaluatorResult result, boolean isLast) {
            if (isLast) {
                StringBuilder builder = new StringBuilder();
                builder.append(result.getResultString());
                if (!TextUtils.isEmpty(builder)) {
                    try {
                        XmlPullParser parser = Xml.newPullParser();
                        parser.setInput(new ByteArrayInputStream(builder.toString().getBytes()), "utf-8");
                        int event = parser.getEventType();
                        while (event != XmlPullParser.END_DOCUMENT) {
                            switch (event) {
                                case XmlPullParser.START_TAG:
                                    if (parser.getName() != null && parser.getName().equals("read_chapter")) {
                                        if (parser.getAttributeCount() == 7) {
                                            String total_score = parser.getAttributeValue(5);
                                            int score = (int) Float.parseFloat(total_score) * 20;
                                            recordScoreMap.put(rolePlayRecordList.get(currentPisition).getItem(), score);
                                            recordFileMap.put(rolePlayRecordList.get(currentPisition).getItem(),
                                                    recordFileUrl);
                                            ratingScoreText.setText(String.valueOf(score) + "分");
                                        }
                                    }
                                    break;
                                case XmlPullParser.END_TAG:

                                    break;
                            }
                            event = parser.next();
                        }
                    } catch (XmlPullParserException e) {
                        JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());
                    } catch (IOException e) {
                        JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());
                    } finally {
                        File reviewFile = new File(recordReviewUrl);
                        if (reviewFile.exists()) {
                            reviewFile.delete();
                        }
                    }
                }
            }
        }

        public void onError(com.iflytek.cloud.SpeechError error) {
            JLog.e((error == null || error.getMessage() == null) ? "Exception is null" : error.getMessage());
            File reviewFile = new File(recordReviewUrl);
            if (reviewFile.exists()) {
                reviewFile.delete();
            }
//            Toast.makeText(subtitleActivity, "录音评分失败", Toast.LENGTH_SHORT).show();
        }

    };

    /**
     * 保存录音
     */
    private void saveCurrentRecord() {
        if (myRecordBean != null) {
            Manager.DeleteRecord(myRecordBean.getId());
        }
//        List<SubtitleBean> listSub = new ArrayList<>();
        VideoBean videoBean = subtitleActivity.getVideoBean();
        list = subtitleActivity.getList();
        record.setStartPosition(list.get(0).getShowTime().getBegin());
        record.setEndPosition(list.get(list.size() - 1).getShowTime().getEnd());
        record.setBeginTime(rolePlayRecordList.get(0).getShowTime().getBegin());
        record.setEndTime(rolePlayRecordList.get(rolePlayRecordList.size() - 1).getShowTime().getEnd());
        List<Integer> selectList = new ArrayList<>();
        for (int i = 0; i < rolePlayRecordList.size(); i++) {
            selectList.add(rolePlayRecordList.get(i).getItem());
        }
        record.setSelectList(JSONObject.toJSONString(selectList));
        List<String> recordUrlList = new ArrayList<>();
        for (Integer urlKey : recordFileMap.keySet()) {
            recordUrlList.add(recordFileMap.get(urlKey));
        }
        record.setRecordPaths(JSONObject.toJSONString(recordUrlList));
        record.setSrtLocalPath(videoBean.getSrtPath());
//        record.setVideoLocalPath(videoBean.getVideoPath());
        record.setMp3LocalPath(videoBean.getMp3Path());
        int totalScore = 0;
        List<Integer> scoreList = new ArrayList<>();
        for (Integer scoreKey : recordScoreMap.keySet()) {
            totalScore += recordScoreMap.get(scoreKey);
            scoreList.add(recordScoreMap.get(scoreKey));
        }
        record.setSentenceScore(JSONObject.toJSONString(scoreList));
        record.setScore(totalScore);
        record.setVideoLocalPath(videoBean.getVideoPath());
        record.setVideoName(videoBean.getVideoName());
        record.setVideoPictureUrl(videoBean.getVideoPictureUrl());
        record.setDownloadId(videoBean.getDownloadId());

        isSave = true;
        Manager.InsertRecord(record);
    }

    /**
     * 清除本次录音
     */
    private void clearCurrentRecord() {
        if (myRecordBean != null) {
            Manager.DeleteRecord(myRecordBean.getId());
        }
        for (Integer urlKey : recordFileMap.keySet()) {
            File recordFile = new File(recordFileMap.get(urlKey));
            if (recordFile.exists()) {
                recordFile.delete();
            }
        }
        recordFileMap.clear();
        recordScoreMap.clear();
    }

    /**
     * 设置录音语句
     *
     * @param rolePlayRecordList
     */
    public void setRolePlayRecordList(List<SubtitleBean> rolePlayRecordList) {
        PlayConfig.roType = RoType.START;
        if (this.rolePlayRecordList == null) {
            this.rolePlayRecordList = new ArrayList<>();
        } else {
            if (this.rolePlayRecordList.size() > 0) {
                currentPisition = 0;
                this.rolePlayRecordList.clear();
                recordFileMap.clear();
                recordScoreMap.clear();
            }
        }
        this.rolePlayRecordList.addAll(rolePlayRecordList);
    }

    /**
     * 设置试听录音
     *
     * @param myRecordBean
     */
    public void setMyRecordBean(MyRecordBean myRecordBean) {
        if (myRecordBean == null) {
            this.myRecordBean = null;
        } else {
            isSave = true;
            PlayConfig.roType = RoType.COMPLETE_RECORD;
            this.myRecordBean = myRecordBean;
            for (int i = 0; i < myRecordBean.getRecordPaths().size(); i++) {
                recordFileMap.put(rolePlayRecordList.get(i).getItem(), myRecordBean.getRecordPaths().get(i));
            }
            refreshHandler.sendEmptyMessage(RecordRefreshHandler.RECORD_COMPLETE);
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

    /**
     * 打开Window
     */
    public void showWindow() {
        if (PlayConfig.isFullStatus) {
            subtitleActivity.onSubtitleFullClick(null);
        }
        isSave = false;
        interruptRecord = false;

        ((TextView) rootView.findViewById(R.id.tv_save)).setText("保存录音");
        completeStateText.setText("准备录音");

        rootView.findViewById(R.id.record_start_layout).setVisibility(View.VISIBLE);
        rootView.findViewById(R.id.record_comple_layout).setVisibility(View.GONE);

        if (PlayConfig.roType == RoType.START) {
            refreshHandler.sendEmptyMessage(RecordRefreshHandler.RECORD_PREARE);
        } else if (PlayConfig.roType == RoType.COMPLETE_RECORD) {
            currentPisition = rolePlayRecordList.size() - 1;
            recordComplete();
            if (subtitleActivity.getMyRecordBean() == null) {
                list = subtitleActivity.getList();
                subtitleActivity.startPlay(list.get(0).getShowTime().getBegin());
            }
            subtitleActivity.pausePlay();
        }
        subtitleActivity.pausePlay();
        if (onWindowStateListener != null) {
            onWindowStateListener.windowOpen();
        }
        showAtLocation(subtitleActivity.findViewById(R.id.root_layout), Gravity.BOTTOM, 0, 0);
    }

    /**
     * 关闭Window
     */
    public void closeWindow() {
        if (rolePlayRecordList.size() > 0) {
            currentPisition = 0;
            rolePlayRecordList.clear();
        }
        if (recordFileMap.size() > 0) {
            recordFileMap.clear();
        }
        if (recordScoreMap.size() > 0) {
            recordScoreMap.clear();
        }
        PlayConfig.roType = RoType.START;
        if (onWindowStateListener != null) {
            onWindowStateListener.windowExternalClose();
        }
        dismiss();
    }

//    public void closeAll() {
//        if (onWindowStateListener != null) {
//            onWindowStateListener.windowExternalClose();
//        }
//        dismiss();
//    }

}