package net.naucu.englishxianshi.widget.window;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.jiongbull.jlog.JLog;
import com.lijunsai.httpInterface.tool.CardUtils;

import net.naucu.englishxianshi.R;
import net.naucu.englishxianshi.iflytek.ISEIflytek;
import net.naucu.englishxianshi.permission.PermissionsManager;
import net.naucu.englishxianshi.permission.PermissionsResultAction;
import net.naucu.englishxianshi.util.Calculator;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * 复读机
 *
 * @author Y
 * @version 0.1
 * @date 2016年7月18日09:59:51
 */
public class AnswerReadWindow extends PopupWindow implements OnClickListener {
    public static final String TAG = AnswerReadWindow.class.getSimpleName();
    // Handler
    private RecordHandler handler;

    private View rootView;
    private Activity activity;
    private DisplayMetrics displayMetrics;
    //
    private int mark = 0;// 0电子书 1翻译 2词典

    // 录音动画
    private ImageView ivRecordAnim;
    // 评分
    private LinearLayout llScore;
    private TextView tvScore;
    private TextView tvScoreInfo;
    // 录音
    private ImageView ivRecord;
    private ImageView ivPlay;
    private ImageView ivBack;
    // 录音动画
    private Animation recordAnimation;

    // 录音对象
    private MediaRecorder mediaRecorder;
    // 录音文件地址
    private String recordFile;
    // 开始录音
    private boolean isStartRecord = false;
    // 播放对象
    private MediaPlayer mediaPlayer;

    //Permission
    private boolean isPermission = false;

    // 录音评测对象
    private ISEIflytek iseIflytek;

    public AnswerReadWindow(Context context) {
        handler = new RecordHandler(this);
        iseIflytek = new ISEIflytek(context);

        activity = (Activity) context;
        displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        init(context);
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    /**
     * 初始化
     *
     * @param context
     */
    @SuppressLint("InflateParams")
    private void init(Context context) {
        rootView = LayoutInflater.from(context).inflate(R.layout.window_answer_read, null);
        setContentView(rootView);
        //
        recordAnimation = AnimationUtils.loadAnimation(context, R.anim.scale);
        // 设置大小
        setWidth(LayoutParams.MATCH_PARENT);
        setHeight(LayoutParams.WRAP_CONTENT);
        // 初始化View
        ivRecordAnim = (ImageView) rootView.findViewById(R.id.iv_record_anim);
        llScore = (LinearLayout) rootView.findViewById(R.id.ll_score);
        tvScore = (TextView) rootView.findViewById(R.id.tv_score);
        tvScoreInfo = (TextView) rootView.findViewById(R.id.tv_score_info);
        ivRecord = (ImageView) rootView.findViewById(R.id.iv_record);
        ivPlay = (ImageView) rootView.findViewById(R.id.iv_play);
        ivBack = (ImageView) rootView.findViewById(R.id.iv_back);
        // View Click
        ivRecord.setOnTouchListener(new RecordTouchListener());
        ivPlay.setOnClickListener(this);
        ivBack.setOnClickListener(this);

        ivRecord.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                startRecorder();
                return true;
            }
        });
    }

    public interface OnCloseCallBack{
        void replacePic();
    }

    public void setCloseCallBack(OnCloseCallBack closeCallBack) {
        this.closeCallBack = closeCallBack;
    }

    OnCloseCallBack closeCallBack;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_play:
                startPlayer();
                break;
            case R.id.iv_back:
                closeCallBack.replacePic();
                dismiss();
                break;
            default:
                break;
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private class RecordTouchListener implements OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            int action = event.getAction();
            if (isStartRecord) {
                switch (action) {
                    case MotionEvent.ACTION_UP:
                        stopRecorder();
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        stopRecorder();
                        break;
                }
            }

            return false;
        }

    }

    /**
     * 准备录音
     */
    private void startRecorder() {
        if (mediaRecorder == null) {
            mediaRecorder = new MediaRecorder();
        } else {
            stopRecorder();
            startRecorder();
            return;
        }
        /**
         * mediaRecorder.setAudioSource设置声音来源。
         * MediaRecorder.AudioSource这个内部类详细的介绍了声音来源。
         * 该类中有许多音频来源，不过最主要使用的还是手机上的麦克风，MediaRecorder.AudioSource.MIC
         */
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        /**
         * mediaRecorder.setOutputFormat代表输出文件的格式。该语句必须在setAudioSource之后，在prepare之前。
         * OutputFormat内部类，定义了音频输出的格式，主要包含MPEG_4、THREE_GPP、RAW_AMR……等。
         */
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        /**
         * mediaRecorder.setAddioEncoder()方法可以设置音频的编码
         * AudioEncoder内部类详细定义了两种编码：AudioEncoder.DEFAULT、AudioEncoder.AMR_NB
         */
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        if (recordFile == null || recordFile.length() <= 0) {
            recordFile = CardUtils.filePath + "MyRecord/";
            File recordFolder = new File(recordFile);
            if (!recordFolder.exists()) {
                recordFolder.mkdirs();
            }
            if (mark == 1) {
                recordFolder = new File(recordFile += "translation_record.amr");
            } else if (mark == 2) {
                recordFolder = new File(recordFile += "dictionary_record.amr");
            } else {
                recordFolder = new File(recordFile += "books_recrod.amr");
            }
            if (recordFolder.exists()) {
                recordFolder.delete();
            }
        }
        try {
            mediaRecorder.setOutputFile(recordFile);
            mediaRecorder.prepare();
            mediaRecorder.start();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    do {
                        try {
                            int volume = mediaRecorder.getMaxAmplitude();
                            if (volume > 0 && volume < 1000000) {
                                Calculator.setDbCount(20 * (float) (Math.log10(volume)));
                            }
                            Log.e(TAG, "分贝 = " + Calculator.dbstart);
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());
                            }
                            handler.sendEmptyMessage(RecordHandler.UPDATE_DB);
                        } catch (RuntimeException e) {
                            JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());
                        }
                    } while (isStartRecord);
                }
            }).start();
            isStartRecord = true;
            ivRecord.startAnimation(recordAnimation);

            ivRecordAnim.setVisibility(View.VISIBLE);
            llScore.setVisibility(View.INVISIBLE);
        } catch (IllegalStateException e) {
            JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());
        } catch (IOException e) {
            JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());
        }
    }

    /**
     * 停止录音
     */
    private void stopRecorder() {
        if (mediaRecorder == null && !isStartRecord) {
            return;
        }
        try {
            mediaRecorder.stop();
        } catch (RuntimeException e) {
            JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());
        } finally {
            mediaRecorder.release();
            mediaRecorder = null;
        }

        isStartRecord = false;
        ivRecord.clearAnimation();

        ivRecordAnim.setImageResource(R.drawable.ic_record_01);
        ivRecordAnim.setVisibility(View.GONE);
        llScore.setVisibility(View.GONE);
    }

    /**
     * 准备播放
     */
    private void startPlayer() {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        } else {
            stopPlayer();
            startPlayer();
            return;
        }

        if (recordFile == null || recordFile.length() <= 0) {
            recordFile = CardUtils.filePath + "MyRecord/";
            File recordFolder = new File(recordFile);
            if (!recordFolder.exists()) {
                recordFolder.mkdirs();
            }
            recordFolder = new File(recordFile += "reread-machine.amr");
            if (!recordFolder.exists()) {
                Toast.makeText(activity, "当前没有录音", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        try {
            mediaPlayer.setDataSource(recordFile);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer paramMediaPlayer) {
                    paramMediaPlayer.start();
                    ivPlay.setClickable(false);
                }
            });
            mediaPlayer.setOnErrorListener(new OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer paramMediaPlayer, int paramInt1, int paramInt2) {
                    stopPlayer();
                    return false;
                }
            });
            mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer paramMediaPlayer) {
                    stopPlayer();
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
        }
    }

    /**
     * 停止播放
     */
    public void stopPlayer() {
        if (mediaPlayer == null) {
            return;
        }
        try {
            mediaPlayer.stop();
        } catch (RuntimeException e) {
            JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());
        } finally {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        ivPlay.setClickable(true);
    }

    public static class RecordHandler extends Handler {
        private final WeakReference<AnswerReadWindow> reference;

        public RecordHandler(AnswerReadWindow window) {
            reference = new WeakReference<>(window);
        }

        // 更新分贝
        public static final int UPDATE_DB = 11;

        @Override
        public void handleMessage(Message msg) {
            if (reference.get() == null) {
                return;
            }
            switch (msg.what) {
                case UPDATE_DB:
                    updateDB();
                    break;

                default:
                    break;
            }
        }

        /**
         * 更新分贝
         */
        private void updateDB() {
            int db = (int) Calculator.dbstart / 7;
            switch (db) {
                case 1:
                    reference.get().ivRecordAnim.setImageResource(R.drawable.ic_record_01);
                    break;
                case 2:
                    reference.get().ivRecordAnim.setImageResource(R.drawable.ic_record_02);
                    break;
                case 3:
                    reference.get().ivRecordAnim.setImageResource(R.drawable.ic_record_03);
                    break;
                case 4:
                    reference.get().ivRecordAnim.setImageResource(R.drawable.ic_record_04);
                    break;
                case 5:
                    reference.get().ivRecordAnim.setImageResource(R.drawable.ic_record_05);
                    break;
                case 6:
                    reference.get().ivRecordAnim.setImageResource(R.drawable.ic_record_06);
                    break;
                case 7:
                    reference.get().ivRecordAnim.setImageResource(R.drawable.ic_record_07);
                    break;
                case 8:
                    reference.get().ivRecordAnim.setImageResource(R.drawable.ic_record_08);
                    break;
                case 9:
                    reference.get().ivRecordAnim.setImageResource(R.drawable.ic_record_09);
                    break;
                case 10:
                    reference.get().ivRecordAnim.setImageResource(R.drawable.ic_record_10);
                    break;
                case 11:
                    reference.get().ivRecordAnim.setImageResource(R.drawable.ic_record_11);
                    break;
                case 12:
                    reference.get().ivRecordAnim.setImageResource(R.drawable.ic_record_12);
                    break;
                case 13:
                    reference.get().ivRecordAnim.setImageResource(R.drawable.ic_record_13);
                    break;
                default:
                    reference.get().ivRecordAnim.setImageResource(R.drawable.ic_record_14);
                    break;
            }
        }
    }

    private void requestPermission() {
        PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(activity, new String[]{
                Manifest.permission.RECORD_AUDIO
        }, new PermissionsResultAction() {
            @Override
            public void onGranted() {
                isPermission = true;
            }

            @Override
            public void onDenied(String permission) {
                isPermission = false;
            }
        });
    }


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
    }

}
