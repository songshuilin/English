package net.naucu.englishxianshi.ui.video;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fght.videoplayer.widget.MediaCallBack;
import com.fght.videoplayer.widget.MediaController;
import com.fght.videoplayer.widget.media.VideoView;
import com.jiongbull.jlog.JLog;

import net.naucu.englishxianshi.R;
import net.naucu.englishxianshi.bean.MyRecordBean;
import net.naucu.englishxianshi.bean.VideoBean;
import net.naucu.englishxianshi.db.Manager;
import net.naucu.englishxianshi.db.dao.Movie;
import net.naucu.englishxianshi.ui.base.BaseActivity;

import java.io.File;
import java.lang.ref.WeakReference;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IMediaPlayer.OnPreparedListener;


public abstract class PlayVideoActivity extends FragmentActivity implements MediaCallBack {
    public static final String TAG = PlayVideoActivity.class.getSimpleName();
    // 视频信息
    protected VideoBean videoBean;
    private Movie movie;
    public static final String VIDEO_INFO = "VIDEO_INFO";
    // Handler
    private PlayerHandler handler;
    // Runnable
    private PlayerRunnable runnable;

    private LinearLayout zhezhaocengLayout;

    // Error
    private RelativeLayout errLayout;
    private ImageView errBack;
    private ProgressBar errBar;
    private TextView errText;

    // 视频播放
    private VideoView videoView;
    // 视频控制对象
    public MediaController videoMediaController;
    // 视频播放位置存
    private int videoUpPosition = -1;
    //
    public boolean isExist = false;

    private int dy = 0;
    // 音频播放对象
    private VideoView audioView;
    // 音频控制器
    private MediaController audioController;
    // 音频管理对象
    private AudioManager audioManager;
    public boolean isLoop = true;

    private ImageView iv_record_anim;

    public ImageView getImgAnim() {
        return iv_record_anim;
    }

    public boolean isLearn = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_play_video);
        // 初始化

        zhezhaocengLayout = (LinearLayout) findViewById(R.id.zhezhaoceng);

        init();
        // 初始化数据
        initData();

        if (m == null) {
            m = new com.fght.videoplayer.widget.MediaController(this);
            MediaController.isLearn = false;
        }
    }

    /**
     * 初始化
     */
    private void init() {
        //初始化View

        iv_record_anim = (ImageView) findViewById(R.id.iv_record_anim);
        errLayout = (RelativeLayout) findViewById(R.id.err_layout);
        errBack = (ImageView) findViewById(R.id.err_back);
        errBar = (ProgressBar) findViewById(R.id.err_bar);
        errText = (TextView) findViewById(R.id.err_text);
        videoView = (VideoView) findViewById(R.id.video_view_01);
        audioView = (VideoView) findViewById(R.id.video_view_02);
        // 初始化音频管理对象
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        //
        errBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                zhezhaocengLayout.setVisibility(View.GONE);
            }
        }, 5000);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        //需要修改
        videoBean = (VideoBean) getIntent().getSerializableExtra(VIDEO_INFO);
//        if (videoBean == null || TextUtils.isEmpty(videoBean.getVideoPath())
//                || TextUtils.isEmpty(videoBean.getMp3Path())) {
//            Toast.makeText(this, "解析视频信息错误", Toast.LENGTH_SHORT).show();
//            errBar.setVisibility(View.GONE);
//            errText.setText("解析视频失败...");
//            isExist = false;
//            return;
//        }
        isExist = true;
    }


    /**
     * Player Runnalbe
     *
     * @author Y
     */
    private class PlayerRunnable implements Runnable {
        //        private boolean isLoop = true;
        @Override
        public void run() {

            do {
                if (videoView == null || !videoView.isPlaying()) {
                    initVideo();
                } else if (videoView.isPlaying() && (audioView == null || !audioView.isPlaying())) {
                    initAudio();
                } else if (videoView.isPlaying() && audioView.isPlaying()) {
                    isLoop = false;
                }
                try {
                    Thread.sleep(2500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (isLoop);
//            videoView.pause();
//            audioView.pause();


        }


        /**
         * 初始化视频
         */
        private void initVideo() {
            File videoFile = new File(videoBean.getVideoPath());
            if (!videoFile.exists()) {
                videoFile = new File(videoFile.getPath() + ".tmp");
                if (!videoFile.exists()) {
                    return;
                }
            }
            // Load Video
            Message message = handler.obtainMessage();
            message.what = PlayerHandler.LOAD_VIDEO;
            message.obj = videoFile.getPath();
            handler.sendMessage(message);
        }

        /**
         * 初始化音频
         */
        private void initAudio() {
            File audioFile = new File(videoBean.getMp3Path());
            if (!audioFile.exists()) {
                audioFile = new File(audioFile.getPath() + ".tmp");
                if (!audioFile.exists()) {
                    return;
                }
            }
            // Load Audio
            Message message = handler.obtainMessage();
            message.what = PlayerHandler.LOAD_AUDIO;
            message.obj = audioFile.getPath();
            handler.sendMessage(message);
        }
    }

    private WeakReference<PlayVideoActivity> reference;

    /**
     * Player Handler
     *
     * @author Y
     */
    public class PlayerHandler extends Handler {


        public PlayerHandler(PlayVideoActivity activity) {
            reference = new WeakReference<>(activity);
        }

        // 加载视频
        public static final int LOAD_VIDEO = 11;
        // 加载音频
        public static final int LOAD_AUDIO = 12;

        @Override
        public void handleMessage(Message msg) {
            if (reference.get() == null) {
                return;
            }
            switch (msg.what) {
                case LOAD_VIDEO:
                    if (msg.obj != null) {
                        loadVideo(String.valueOf(msg.obj));

                    } else {
                        errBar.setVisibility(View.GONE);
                        errText.setText("初始化视频失败");
                        JLog.e("加载视频失败");
                    }
                    break;
                case LOAD_AUDIO:
                    if (msg.obj != null) {
                        loadAudio(String.valueOf(msg.obj));
                    } else {
                        JLog.e("加载音频失败");
                    }
                    break;
                default:
                    break;
            }
        }

        /**
         * 加载视频
         *
         * @param url
         */
        private void loadVideo(String url) {
            if (videoView == null) {
                videoView = (VideoView) findViewById(R.id.video_view_01);
            }
            if (videoMediaController == null) {
                videoMediaController = new MediaController(reference.get(), null);
                reference.get().videoView.setMediaController(reference.get().videoMediaController);
            }
            reference.get().videoMediaController.setTitle(reference.get().videoBean.getVideoName());
            reference.get().videoView.setVideoPath(url);
            reference.get().videoView.setVolume(reference.get().getSystemVolume(false),
                    reference.get().getSystemVolume(false));
            reference.get().startVideo(reference.get().videoUpPosition);
//            if(getMyRecordBean() != null){
//                reference.get().stopPlay();
//            }
            reference.get().errLayout.setVisibility(View.GONE);
            PlayVideoActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
        }


        /**
         * 加载音频
         *
         * @param url
         */
        private void loadAudio(String url) {
            if (audioView == null) {
                reference.get().audioView = (VideoView) reference.get().findViewById(R.id.video_view_02);
            }
            if (reference.get().audioController == null) {
                reference.get().audioController = new MediaController(reference.get());
                reference.get().audioView.setMediaController(reference.get().audioController);
            }
            reference.get().audioController.setMediaCallBack(PlayVideoActivity.this);
            reference.get().videoView.setVolume(reference.get().getSystemVolume(false),
                    reference.get().getSystemVolume(false));
            reference.get().audioView.setVolume(reference.get().getSystemVolume(true),
                    reference.get().getSystemVolume(true));
            reference.get().audioView.setVideoPath(url);
            reference.get().audioView.setOnPreparedListener(new OnPreparedListener() {
                @Override
                public void onPrepared(IMediaPlayer mp) {
                    reference.get().loadSubtitle();
                }
            });
            reference.get().startAudio(-1);
        }

    }

    /**
     * 获取视频信息
     *
     * @return
     */
    public VideoBean getVideoBean() {
        return videoBean;
    }

    /**
     * 获取录音试听对象
     *
     * @return
     */
    public MyRecordBean getMyRecordBean() {
        if (videoBean instanceof MyRecordBean) {
            return (MyRecordBean) videoBean;
        }
        return null;
    }

    /**
     * 获取系统音量
     */
    public float getSystemVolume(boolean isOpen) {
        if (isOpen) {
            return audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        }
        return -1f;
    }

    /**
     * 横竖屏切换界面显示修改
     */
    public com.fght.videoplayer.widget.MediaController m;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (m == null) {
            m = new com.fght.videoplayer.widget.MediaController(this);
        }
        if (isScreenOriatationPortrait()) {
            if (videoView != null) {
                videoView.layout();
                m.changePortrait(false);
            }
            if (reference.get().videoView.getType() == 1) {
                findViewById(R.id.subtitle_fragment_layout).setVisibility(View.VISIBLE);
                if (findViewById(R.id.subtitle_fragment_layout).getY() == dip2px(this, 210)) {
                    findViewById(R.id.subtitle_fragment_layout).setY(dip2px(this, -1));
                }
            } else {
                findViewById(R.id.subtitle_fragment_layout).setVisibility(View.VISIBLE);
                findViewById(R.id.subtitle_fragment_layout).setY(dip2px(this, 210));
            }
        } else {
            findViewById(R.id.subtitle_fragment_layout).setVisibility(View.GONE);
            if (videoView != null) {
                videoView.layoutheng();
                m.changeLand(false);
            }
        }

    }

    public abstract void OpenWindow();

    /**
     * 返回当前屏幕是否为竖屏。
     *
     * @return 当且仅当当前屏幕为竖屏时返回true, 否则返回false。
     */
    private boolean isScreenOriatationPortrait() {
        return this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    /**
     * 设置字幕显示
     *
     * @param chinese
     * @param english
     */
    protected void setSubtitleShow(String chinese, String english) {
        if (videoMediaController != null) {
            videoMediaController.setSrt(chinese, english);
        }
    }

    /**
     * 获取视频播放状态
     *
     * @return
     */
    public boolean isVideoPlaying() {
        if (videoView != null && videoMediaController != null) {
            return videoView.isPlaying();
        }
        return false;
    }

    public VideoView getVideoView() {
        return videoView;
    }

    /**
     * 获取视频播放位置
     *
     * @return
     */
    public int getVideoPosition() {
        if (videoView != null && videoMediaController != null) {
            return videoView.getCurrentPosition();
        }
        return -1;
    }

    /**
     * 播放视频
     *
     * @param position
     */
    public void startVideo(int position) {


        if (videoView == null || videoMediaController == null) {
            return;
        }
        if (position > -1) {
//            Log.i(TAG , "123_播放视频获取到的值:" + position);
            videoView.seekTo(position);
        }
        videoView.setFixedSize(videoView.getWidth(), videoView.getHeight());
        videoView.start();

        videoView.layout();
    }

    /**
     * 暂停视频
     */
    public void pauseVideo() {
        if (videoView == null || videoMediaController == null) {
            return;
        }
        videoView.pause();
    }

    /**
     * 停止视频播放
     */
    public void stopVideo() {
        if (videoView == null || videoMediaController == null) {
            return;
        }
        videoView.stopPlayback();
        videoView = null;
    }

    /**
     * 加载字幕
     */
    protected void loadSubtitle() {
        if (movie == null) {
            movie = Manager.SelectMovieId(videoBean.getDownloadId());
            startPlay(movie.getLastPosition());
            Log.i("TAG", "qwe798sda3168w7 进了");
        }
    }

    /**
     * 获取音频播放状态
     *
     * @return
     */
    protected boolean isAudioPlaying() {
        if (audioView != null && audioController != null) {
            return audioView.isPlaying();
        }
        return false;
    }

    /**
     * 获取音频播放位置
     *
     * @return
     */
    public int getAudioPosition() {
        if (audioView != null && audioController != null) {
            return audioView.getCurrentPosition();
        }
        return -1;
    }

    /**
     * 播放音频
     *
     * @param position
     */
    public void startAudio(int position) {
        if (audioView == null || audioController == null) {
            return;
        }
        if (position > -1) {
            Log.i(TAG, "123_获取的position : " + position);
            audioView.seekTo(position);
        }
        audioView.start();

//
    }

    public VideoView getAudioView() {
        return audioView;
    }

    /**
     * 暂停音频
     */
    public void pauseAudio() {
        if (audioView == null || audioController == null) {
            return;
        }
        audioView.pause();
    }

    /**
     * 停止音频
     */
    public void stopAudio() {
        if (audioView == null || audioController == null) {
            return;
        }
        audioView.stopPlayback();
        audioView = null;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (dy == 0) {
            chushi();
            dy++;
        }


    }

    //    boolean closeThread = false;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (videoView != null && thread != null) {
//                closeThread = true;
                thread.interrupt();

                videoView.releaseWithoutStop();
                videoView.release(false);
                videoView = null;
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    Thread thread;

    public void chushi() {
        if (isExist) {
            if (handler == null) {
                handler = new PlayerHandler(this);
            }
            if (runnable == null || videoView == null) {
                runnable = new PlayerRunnable();
                thread = new Thread(runnable);
                thread.start();
                errText.setText("初始化视频...");
            }
        }
    }


    public void clearViewid(View viewid) {
        viewid = null;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (getVideoPosition() != -1) {
            videoUpPosition = getVideoPosition();
        }
        runnable = null;
        pausePlay();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (movie != null) {
            movie.setLastPosition(getVideoPosition());
            Manager.InsertMovie(movie);
        }
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        pausePlay();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (videoView != null && videoMediaController != null) {
            videoView = null;
            videoMediaController = null;
        }
        if (audioView != null && audioController != null) {
            audioController = null;
            audioView = null;
        }
    }

    public void seekTo(int position) {
        if (videoView != null && audioView != null) {
            if (position != 0) {
                videoView.seekTo(position);
                audioView.seekTo(position);
            } else {
                videoView.seekTo(0);
                audioView.seekTo(0);
            }
        }
    }

    /**
     * 开始播放
     *
     * @param position
     */
    public void startPlay(int position) {
        startAudio(position);
        startVideo(position);
    }

    /**
     * 暂停播放
     */
    public void pausePlay() {
        pauseVideo();
        pauseAudio();
    }

    /**
     * 停止播放
     */
    public void stopPlay() {
        stopVideo();
        stopAudio();
    }

    /**
     * 静音
     */
    public void mutePlay() {
        if (videoView != null && audioView != null) {
            videoView.setVolume(getSystemVolume(false), getSystemVolume(false));
            audioView.setVolume(getSystemVolume(false), getSystemVolume(false));
        }
    }

    /**
     * 放音
     */
    public void soundPlay() {
        if (videoView != null) {
            videoView.setVolume(getSystemVolume(false), getSystemVolume(false));
        }
        if (audioView != null) {
            audioView.setVolume(getSystemVolume(true), getSystemVolume(true));
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            finish();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
