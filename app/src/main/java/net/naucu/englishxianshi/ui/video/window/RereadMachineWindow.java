package net.naucu.englishxianshi.ui.video.window;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jiongbull.jlog.JLog;
import com.lijunsai.httpInterface.tool.CardUtils;

import net.naucu.englishxianshi.R;
import net.naucu.englishxianshi.permission.PermissionsManager;
import net.naucu.englishxianshi.permission.PermissionsResultAction;
import net.naucu.englishxianshi.ui.video.PlayConfig;
import net.naucu.englishxianshi.ui.video.PlaySubtitleActivity;
import net.naucu.englishxianshi.ui.video.window.event.OnWindowStateListener;
import net.naucu.englishxianshi.util.Calculator;
import net.naucu.englishxianshi.widget.dialog.LoadingDialog;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * 字幕复读机
 *
 * @author Yi
 */
public class RereadMachineWindow extends PopupWindow {
    public static final String TAG = RereadMachineWindow.class.getSimpleName();
    //Handler
    public RecordHandler handler;

    // Activity
    private PlaySubtitleActivity subtitleActivity;
    // 屏幕尺寸对象
    private DisplayMetrics displayMetrics;
    // Window根View
    private View rootView;

    // 录音动画
    private ImageView ivRecordAnim;
    // 评分
    @ViewInject(R.id.ll_score)
    private LinearLayout llScore;
    // 录音评测分数
    @ViewInject(R.id.tv_score)
    private TextView scoreText;
    // 录音评测分数详情
    @ViewInject(R.id.tv_score_info)
    private TextView scoreInfoText;
    // 退出按钮
    @ViewInject(R.id.iv_back)
    private ImageView backImageView;
    // 播放按钮
    @ViewInject(R.id.iv_play)
    private ImageView playImageView;
    // 录音按钮
    @ViewInject(R.id.iv_record)
    private ImageView recordImageView;

    // 录音文件夹地址
    private String recordFolderUrl;
    // 录音文件地址
    private String recordFileUrl;
    // 录音播放对象
    private MediaPlayer mediaPlayer;
    // 录音对象
    private MediaRecorder mediaRecorder;
    // 录音动画
    private Animation animation;
    // 开始录音标识
    private boolean isStartRecord = false;


    public RelativeLayout RRALL;

    //Permission
    private boolean isPermission = false;

    // Window关闭监听器
    private OnWindowStateListener onWindowStateListener;
    // Loading
    private LoadingDialog loadingDialog;

    //    开关回调
    private WindowCallBack callBack;

    // 录音评测分数详情
    public RereadMachineWindow(PlaySubtitleActivity subtitleActivity, WindowCallBack callBack) {
        handler = new RecordHandler(this);
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
        WindowManager windowManager = subtitleActivity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        setHeight(getPingMuHeight() / 10);
        // 初始化录音文件夹
        recordFolderUrl = CardUtils.filePath + "MyRecord/";
        File recordFolder = new File(recordFolderUrl);
        if (!recordFolder.exists()) {
            recordFolder.mkdirs();
        }
        // 初始化录音文件
        recordFileUrl = recordFolderUrl + "video_record.amr";
        File recordFile = new File(recordFileUrl);
        if (recordFile.exists()) {
            recordFile.delete();
        }
        // 初始化录音对象
        mediaRecorder = new MediaRecorder();
        // 初始化Loading
        loadingDialog = new LoadingDialog(subtitleActivity);
        // 初始化录音动画
        animation = AnimationUtils.loadAnimation(subtitleActivity, R.anim.scale);


    }

    /**
     * 初始化View
     *
     * @return
     */
    @SuppressLint("InflateParams")
    private void initView() {
        rootView = LayoutInflater.from(subtitleActivity).inflate(R.layout.pop_record, null);
        setContentView(rootView);
        x.view().inject(this, rootView);
        ivRecordAnim = subtitleActivity.getImgAnim();


        // 退出复读机
        backImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackImageClick(v);
            }
        });
        // 播放录音
        playImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onPlayImageClick(v);
            }
        });
        // 开始录音
        recordImageView.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return onStartRecordImageClick(v);

            }
        });
        recordImageView.setOnTouchListener(new OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return onEndRecordImageClick(v, event);
            }
        });
    }

    /**
     * 点击退出
     *
     * @param view
     */


    private void onBackImageClick(View view) {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (onWindowStateListener != null) {
            onWindowStateListener.windowInternalClose();
        }
        subtitleActivity.startPlay(-1);
        callBack.closeCallBack();
        dismiss();
        closeWindow();
    }

    /**
     * 播放录音
     *
     * @param view
     */
    private void onPlayImageClick(View view) {
        // 准备Loading
        loadingDialog.setContent("准备播放录音");
        // 暂停视频播放
        subtitleActivity.pausePlay();

        File recordFile = new File(recordFileUrl);
        if (!recordFile.exists()) {
            Toast.makeText(subtitleActivity, "尚未录音,请开始录音", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mediaPlayer != null) {
            try {
                mediaPlayer.stop();
            } catch (RuntimeException e) {
//                Log.e(TAG, e.getMessage());
            } finally {
                mediaPlayer.release();
                mediaPlayer = null;
            }
        }
        try {
            // Loading
            loadingDialog.show();

            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(recordFileUrl);
            mediaPlayer.setOnPreparedListener(new OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    // 关闭Loading
                    loadingDialog.dismiss();
                    // 开始播放录音
                    mediaPlayer.start();
                }
            });
            mediaPlayer.setOnErrorListener(new OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    // 关闭Loading
                    loadingDialog.dismiss();
                    // 释放播放对象
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null;
                    // LOG
                    Log.e(TAG, "复读机播放录音Error  What:" + what + ",Extra:" + extra);
                    Toast.makeText(subtitleActivity, "播放录音失败,请稍后再试", Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
            mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    loadingDialog.dismiss();
                    // 释放播放对象
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null;
                }
            });
            mediaPlayer.prepareAsync();
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
     * 开始录音
     *
     * @param view
     */
    private boolean onStartRecordImageClick(View view) {
        subtitleActivity.allStop();
        recordImageView.setBackgroundResource(R.drawable.record_f);
        recordImageView.startAnimation(animation);
        // 暂停视频播放
        subtitleActivity.pausePlay();
        // 开始录音
        startMediaRecorder();
        // 修改录音状态
        isStartRecord = true;

        new Thread(new Runnable() {

            @Override
            public void run() {
                do {
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
                } while (isStartRecord);
            }
        }).start();
        ivRecordAnim.setVisibility(View.VISIBLE);
        llScore.setVisibility(View.INVISIBLE);
        return true;
    }

    /**
     * 停止录音
     *
     * @param view
     * @param event
     * @return
     */
    private boolean onEndRecordImageClick(View view, MotionEvent event) {
        if (isStartRecord) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_UP:
                    stopMediaRecorder();
                    break;
                case MotionEvent.ACTION_CANCEL:
                    stopMediaRecorder();
                    break;
            }
        }
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(2000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
////                Random random=new Random(10);
//                int i=(int)(50+Math.random()*(100-50+1));
//                Log.i("GM",""+Math.random());
//                    Toast.makeText(subtitleActivity,"60"+"",Toast.LENGTH_SHORT).show();
//            }
//        }).start();
        return false;
    }

    /**
     * 开始录音对象
     */
    private void startMediaRecorder() {
        try {
            if (mediaRecorder == null) {
                mediaRecorder = new MediaRecorder();
            } else {
                stopMediaRecorder();
                startMediaRecorder();
                return;
            }
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC); // 设置音频采集原
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP); // 内容输出格式
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
            mediaRecorder.setOutputFile(recordFileUrl);
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IllegalStateException e) {
            JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());
        } catch (IOException e) {
            JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());
        }
    }

    private void stopMediaRecorder() {
        recordImageView.setBackgroundResource(R.drawable.record_n);
        recordImageView.clearAnimation();
        // 释放录音对象
        if (mediaRecorder != null) {
            try {
                mediaRecorder.stop();
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            Thread.sleep(2000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                        subtitleActivity.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//
//                                int i = (int) (50 + Math.random() * (100 - 50 + 1));
//                                Log.i("GM", "" + Math.random());
//                                Toast.makeText(subtitleActivity, "评分:" + i + "分", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//
//                    }
//                }).start();
            } catch (RuntimeException e) {
//                Log.e(TAG, e.getMessage());
            } finally {
                mediaRecorder.release();
                mediaRecorder = null;
            }
        }
        // 修改录音状态
        isStartRecord = false;

        ivRecordAnim.setVisibility(View.INVISIBLE);
        llScore.setVisibility(View.GONE);
    }

    public static class RecordHandler extends Handler {
        private final WeakReference<RereadMachineWindow> reference;

        public RecordHandler(RereadMachineWindow window) {
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

    /**
     * 设置Window状态监听器
     *
     * @param onWindowStateListener
     */
    public void setOnWindowStateListener(OnWindowStateListener onWindowStateListener) {
        this.onWindowStateListener = onWindowStateListener;
    }

    public int getPingMuHeight() {
        WindowManager wm = (WindowManager) subtitleActivity
                .getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        return height;
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
        SelectRolePlayWindow selectRolePlayWindow = subtitleActivity.getSelectRolePlayWindow();
        subtitleActivity.pausePlay();
        if (PlayConfig.isFullStatus) {
            SubtitleSelectWindow subtitleSelectWindow = subtitleActivity.getSubtitleSelectWindow();
            SubtitleSettingWindow subtitleSettingWindow = subtitleActivity.getSubtitleSettingWindow();
            if (subtitleSelectWindow != null && subtitleSelectWindow.isShowing() && (subtitleSettingWindow == null || !subtitleSettingWindow.isShowing())) {
                showAsDropDown(subtitleActivity.findViewById(R.id.iv_rollPlay), getPingMuWidth(), (int) (60 * displayMetrics.density),
                        Gravity.TOP);
            }
            if (subtitleSelectWindow != null && subtitleSelectWindow.isShowing() && subtitleSettingWindow != null && subtitleSettingWindow.isShowing()) {
                showAsDropDown(subtitleActivity.findViewById(R.id.iv_rollPlay), getPingMuWidth(), (int) (60 * displayMetrics.density),
                        Gravity.TOP);
                callBack.openCallBack();
            }
            if (subtitleSelectWindow == null || !subtitleSelectWindow.isShowing() || subtitleSettingWindow == null || !subtitleSettingWindow.isShowing()) {
                showAsDropDown(subtitleActivity.findViewById(R.id.iv_rollPlay), getPingMuWidth(), (int) (7 * displayMetrics.density),
                        Gravity.TOP);
            }
        } else {
            showAtLocation(subtitleActivity.findViewById(R.id.root_layout), Gravity.BOTTOM, 0, 0);
        }
        if (selectRolePlayWindow != null && selectRolePlayWindow.isShowing()) {
            selectRolePlayWindow.closeWindow();
            subtitleActivity.deletePopWindowList("角色扮演");
        }
        requestPermission();
        subtitleActivity.insertPopWindowList("复读机");
    }


    /**
     * 关闭Window
     */
    public void closeWindow() {
        if ((mediaPlayer != null && mediaPlayer.isPlaying())||(mediaPlayer != null && !mediaPlayer.isPlaying())) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (onWindowStateListener != null) {
            onWindowStateListener.windowExternalClose();
        }
//        subtitleActivity.startPlay(-1);
        SubtitleSelectWindow subtitleSelectWindow = subtitleActivity.getSubtitleSelectWindow();
        SubtitleSettingWindow subtitleSettingWindow = subtitleActivity.getSubtitleSettingWindow();
        dismiss();
        if (PlayConfig.isFullStatus) {
            if (subtitleSettingWindow != null && subtitleSettingWindow.isShowing()) {
                callBack.closeCallBack();
            }
        }
        if ((subtitleSelectWindow != null && subtitleSelectWindow.isShowing()) || (subtitleSettingWindow != null && subtitleSettingWindow.isShowing())) {
            if (subtitleActivity.isVideoPlaying()) {
                subtitleActivity.pausePlay();
            }
        }
//        subtitleActivity.pausePlay();
    }

    @SuppressLint("NewApi")
    public void yidongUp() {
        dismiss();
        showAsDropDown(subtitleActivity.findViewById(R.id.iv_resume), 0, (int) (60 * displayMetrics.density),
                Gravity.TOP);
    }

    @SuppressLint("NewApi")
    public void yidongDown() {
        dismiss();
        showAsDropDown(subtitleActivity.findViewById(R.id.iv_resume), 0, (int) (7 * displayMetrics.density),
                Gravity.TOP);
    }

    private void requestPermission() {
        PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(subtitleActivity, new String[]{
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

    public interface WindowCallBack {
        void openCallBack();

        void closeCallBack();
    }
}
