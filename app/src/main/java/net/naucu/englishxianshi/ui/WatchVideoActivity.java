package net.naucu.englishxianshi.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.myTool.ActionTitleBarWidget;
import com.app.myTool.ActionTitleBarWidget.ClickListener;
import com.jiongbull.jlog.JLog;
import com.lijunsai.httpInterface.bean.ALLmovieDetailsBean;
import com.lijunsai.httpInterface.bean.AllMoviePath;
import com.lijunsai.httpInterface.implementation.HttpImplementation;
import com.lijunsai.httpInterface.request.NetworkRequestDeal;
import com.lijunsai.httpInterface.tool.CardUtils;

import net.naucu.englishxianshi.BaseApplication;
import net.naucu.englishxianshi.R;
import net.naucu.englishxianshi.adapter.VideoSetAdapter;
import net.naucu.englishxianshi.bean.VideoBean;
import net.naucu.englishxianshi.db.Manager;
import net.naucu.englishxianshi.db.dao.Movie;
import net.naucu.englishxianshi.download.DownloadInfo;
import net.naucu.englishxianshi.download.DownloadManager;
import net.naucu.englishxianshi.download.DownloadState;
import net.naucu.englishxianshi.ui.base.BaseActivity;
import net.naucu.englishxianshi.ui.video.PlaySubtitleActivity;
import net.naucu.englishxianshi.util.ErrorTool;
import net.naucu.englishxianshi.util.NetTool;
import net.naucu.englishxianshi.util.NetUtil;
import net.naucu.englishxianshi.util.SDUtil;
import net.naucu.englishxianshi.widget.dialog.LoadingDialog;

import org.json.JSONException;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * 类名: WatchVideoActivity.java 描述: 观影界面 作者: youyou_pc 时间: 2015年11月24日 下午1:47:06
 */
public class WatchVideoActivity extends BaseActivity
        implements OnCheckedChangeListener, OnItemClickListener, OnClickListener {
    public static final String TAG = WatchVideoActivity.class.getSimpleName();
    private PlaySubtitleActivity subtitleActivity;
    private ActionTitleBarWidget titlebar;// 初始化标题控件
    private Button bt_watch;
    private ImageView im_cover;
    private TextView tv_moviename;
    public TextView tv_score;
    private TextView tv_moviedirector;
    private TextView tv_moviesource;
    private TextView tv_movietype;
    private TextView tv_movieintroduction;
    private Movie dbmovie;
    private Movie localmovie = new Movie();
    private GridView gl_videoset;
    // private int Categorystate;
    private String videoStyle = ".mp4";

    private CheckBox addYingKu;

    public int pos;

    private boolean isExist = true;
    private LoadingDialog loadingDialog;

    private DownloadManager downloadManager;
    private DownloadInfo downloadInfo;
    public RefershDownloadJudu refershDownloadJudu;
    public boolean aflag = false;

    public VideoSetAdapter videoSetAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watchvideo);

        loadingDialog = new LoadingDialog(this);
        aflag = false;
        initView();
        initTitleBar();
        initEvent();
        initDate();
        if (!dbmovie.isTV()) {
            final File f = new File("/storage/emulated/0/.EnglishXS/file/video/");
            if (f.isDirectory()) {
                File[] fileArray = f.listFiles();
                if (null != fileArray && 0 != fileArray.length) {
                    for (int j = 0; j < fileArray.length; j++) {
                        if (fileArray[j].getName().contains(dbmovie.getFilmName())) {
                            aflag = true;
                        }
                    }
                }
            }
            for (int i = downloadManager.getDownloadListCount(); --i >= 0; ) {// 暂停所有正在下载的任务
                downloadInfo = downloadManager.getDownloadInfo(i);
                if (downloadInfo.getLabel().contains(dbmovie.getFilmName())) {
                    if (downloadInfo.getLabel().contains("mp4")) {
                        if (downloadInfo.getProgress() == 100 && aflag) {
                            refershDownloadJudu.sendEmptyMessage(RefershDownloadJudu.refershOver);
                            bt_watch.setText("可播放");
                        } else {
                            addYingKu.setText("继续缓存");
                        }
                    }
                }
            }
        }
        if (addYingKu.getText().equals("缓存完毕")) {
            addYingKu.setClickable(false);
        }
    }


    private void initView() {
        if (NetTool.isNetworkConnected(WatchVideoActivity.this)) {
            if (NetUtil.getNetWorkState(WatchVideoActivity.this) == 0) {
                Toast.makeText(WatchVideoActivity.this, "当前网络状态:3G/4G", Toast.LENGTH_LONG).show();
            } else if (NetUtil.getNetWorkState(WatchVideoActivity.this) == 1) {
                Toast.makeText(WatchVideoActivity.this, "当前网络状态:Wi-Fi", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(WatchVideoActivity.this, "当前无网络连接", Toast.LENGTH_LONG).show();
        }

        refershDownloadJudu = new RefershDownloadJudu();
        titlebar = (ActionTitleBarWidget) findViewById(R.id.titlebar);
        bt_watch = (Button) findViewById(R.id.bt_watch);
        im_cover = (ImageView) findViewById(R.id.im_cover);
        tv_moviename = (TextView) findViewById(R.id.tv_moviename);
        tv_score = (TextView) findViewById(R.id.tv_score);
        tv_moviesource = (TextView) findViewById(R.id.tv_moviesource);
        tv_movietype = (TextView) findViewById(R.id.tv_movietype);
        tv_moviedirector = (TextView) findViewById(R.id.tv_moviedirector);
        tv_movieintroduction = (TextView) findViewById(R.id.tv_movieintroduction);
        gl_videoset = (GridView) findViewById(R.id.gl_videoset);
        addYingKu = (CheckBox) findViewById(R.id.addYingKu);
        downloadManager = DownloadManager.getInstance();
        downloadInfo = new DownloadInfo();

    }


    private void initTitleBar() {
        titlebar.setTitleBackgroundResource(R.drawable.menubutton_select, R.drawable.menubutton_select);
        titlebar.setLeftGravity(Gravity.CENTER);
        titlebar.setLeftIco(R.drawable.backs);
        titlebar.setRightGravity(Gravity.CENTER);
        titlebar.setRightText("影库", 17, Color.WHITE);
        titlebar.setCenterGravity(Gravity.CENTER);
    }

    @Override
    protected void onResume() {
        super.onResume();
        flag = 0;
        if (dbmovie != null) {
            if (!dbmovie.isTV()) {
                for (int i = downloadManager.getDownloadListCount(); --i >= 0; ) {// 暂停所有正在下载的任务
                    downloadInfo = downloadManager.getDownloadInfo(i);
                    if (downloadInfo.getLabel().contains(dbmovie.getFilmName())) {
                        if (downloadInfo.getState().value() == 1) {
                            addYingKu.setChecked(true);
                            return;
                        } else {
                            addYingKu.setChecked(false);
                        }
                    }
                }
            }
        }
    }


    public class RefershDownloadJudu extends Handler {
        public static final int refersh = 0;
        public static final int refershOver = 1;

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case refersh:
                    if (!dbmovie.isTV()) {
                        for (int i = downloadManager.getDownloadListCount(); --i >= 0; ) {
                            downloadInfo = downloadManager.getDownloadInfo(i);
                            if (downloadInfo.getLabel().contains(dbmovie.getFilmName())) {
                                if (downloadInfo.getLabel().contains("mp4")) {
                                    if (downloadInfo.getProgress() != 0 && downloadInfo.getProgress() != 100) {
                                        DecimalFormat df = new DecimalFormat("######0.00");
                                        addYingKu.setText("已下载" + df
                                                .format(downloadInfo.getProgress()) + "%");
                                        flag = 0;
                                    } else if (downloadInfo.getProgress() == 100) {
                                        addYingKu.setText("缓存完毕");
                                        bt_watch.setText("可播放");
                                        if (timer != null) {
                                            timer.cancel();
                                        }
                                    } else {
                                        addYingKu.setText("已缓存0.00%");
                                    }
                                }
                            }
                        }
                    } else {
                        for (int i = downloadManager.getDownloadListCount(); --i >= 0; ) {
                            downloadInfo = downloadManager.getDownloadInfo(i);
                            if (downloadInfo.getFileSavePath().contains(dbmovie.getFilmName() + (bluesPosition + 1))) {
                                if (downloadInfo.getLabel().contains("mp4")) {
                                    String url = dbmovie.getFilmName() + (bluesPosition + 1);
                                    if (url.equals(BaseApplication.getDownUrl())) {
                                        if (downloadInfo.getProgress() != 0) {
                                            DecimalFormat df = new DecimalFormat("######0.00");
                                            addYingKu.setText("已下载" + df
                                                    .format(downloadInfo.getProgress()) + "%");
                                            flag = 0;
                                        } else {
                                            addYingKu.setText("已缓存0.00%");
                                        }
                                    } else {
                                        addYingKu.setText("继续缓存");
                                        bt_watch.setText("边下边播");
                                    }
                                    return;
                                }
                            } else {
                                addYingKu.setText("缓存本地");
                            }
                        }
                    }
                    break;
                case refershOver:
                    addYingKu.setEnabled(false);
                    addYingKu.setText("缓存完毕");
                    bt_watch.setText("可播放");
                    break;
            }
        }
    }

    private void initDate() {
        ALLmovieDetailsBean alLmovieDetailsBean = (ALLmovieDetailsBean) getIntent()
                .getSerializableExtra("movieInformation");
        pos = getIntent().getIntExtra("jishu", 0);
        if (alLmovieDetailsBean != null) {
            x.image().bind(im_cover, alLmovieDetailsBean.getMovieImageUrl(), BaseApplication.getImageOption());
            tv_moviename.setText(alLmovieDetailsBean.getMovieName());
            tv_score.setText(alLmovieDetailsBean.getMoviepingfen());
            tv_moviedirector.setText(alLmovieDetailsBean.getMovieAuthor());
            tv_moviesource.setText(alLmovieDetailsBean.getMovieMadeBy());
            tv_movietype.setText(alLmovieDetailsBean.getTwoCategory());
            tv_movieintroduction.setText(alLmovieDetailsBean.getMovieAbout());
            dbmovie = new Movie();
            dbmovie.setId(alLmovieDetailsBean.getId());
            dbmovie.setAuthor(alLmovieDetailsBean.getMovieAuthor());
            dbmovie.setDownloadId(alLmovieDetailsBean.getId());
            dbmovie.setFilmCovers(alLmovieDetailsBean.getMovieImageUrl());
            dbmovie.setFilmGenre(alLmovieDetailsBean.getTwoCategory());
            dbmovie.setFilmName(alLmovieDetailsBean.getMovieName());
            dbmovie.setIntroduction(alLmovieDetailsBean.getMovieAbout());
            dbmovie.setNationality(alLmovieDetailsBean.getMovieMadeBy());
            dbmovie.setScore(alLmovieDetailsBean.getMoviepingfen());
            dbmovie.setTV(alLmovieDetailsBean.isTV());
            dbmovie.setLocalVideoPath("");
            dbmovie.setLocalSrtPath("");
            if (application.getLoginApplication() != null) {
                getMovieSet(alLmovieDetailsBean.getId(), application.getLoginApplication().getUid());// 电视剧
            }
        }
    }

    private void initEvent() {
        titlebar.OnTitleBarClickListener(clickListener);
        bt_watch.setOnClickListener(this);
        gl_videoset.setOnItemClickListener(this);
        addYingKu.setOnCheckedChangeListener(this);
    }

    private ClickListener clickListener = new ClickListener() {

        @Override
        public void onright(View v) {
            startActivity(new Intent(WatchVideoActivity.this, MyMoreActivity.class));
        }

        @Override
        public void onleft(View v) {
            finish();
        }

        @Override
        public void oncenter(View arg0) {

        }
    };


    boolean canBeDownLoad = true;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_watch:

                if (SDUtil.getSDAvailableSize() <= 200) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);  //先得到构造器
                    builder.setTitle("提示"); //设置标题
                    builder.setMessage("您的SD卡容量不足200M，请清理之后在进行下载学习"); //设置内容
                    builder.setPositiveButton("返回", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            canBeDownLoad = false;
                        }
                    });
                }


                if (canBeDownLoad) {
                    if (!dbmovie.isTV()) {
                        if (!NetTool.isNetworkConnected(this)) {
                            if (new File(localmovie.getLocalVideoPath()).exists()) {
                                if (dbmovie != null) {
                                    try {
                                        play(0);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                Toast.makeText(this, "视频下载失败,请查看网络连接状态", LENGTH_SHORT).show();
                            }
                        } else {
                            if (dbmovie != null) {
                                try {
                                    play(0);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    } else {
                        if (bluesPosition >= 0 && bluesPosition < 10000) {
                            try {
                                play(bluesPosition);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast toast = Toast.makeText(WatchVideoActivity.this, "请选择下面的集数进行观看!", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                    }
                }

                break;

        }
    }

    public int bluesPosition = 10000;

    public void setBluesPosition(int bluesPosition) {
        this.bluesPosition = bluesPosition;
    }

    // 点击集数
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (!NetTool.isNetworkConnected(this)) {
            if (new File(localmovie.getLocalVideoPath()).exists()) {
                if (dbmovie != null) {
                    setBluesPosition(position);
                    if (videoSetAdapter != null) {
                        videoSetAdapter.setBluesColor(position);
                        videoSetAdapter.notifyDataSetChanged();
                    }
                    final File f = new File("/storage/emulated/0/.EnglishXS/file/video/" + dbmovie.getFilmName() + "/");
                    if (f.isDirectory()) {
                        File[] fileArray = f.listFiles();
                        if (null != fileArray && 0 != fileArray.length) {
                            for (int j = 0; j < fileArray.length; j++) {
                                if (dbmovie.getLocalVideoPath().contains(fileArray[j].getName())) {
                                    aflag = true;
                                }
                            }
                        }
                    }
                    for (int i = downloadManager.getDownloadListCount(); --i >= 0; ) {// 暂停所有正在下载的任务
                        downloadInfo = downloadManager.getDownloadInfo(i);
                        if (downloadInfo.getFileSavePath().contains(dbmovie.getFilmName() + (position + 1))) {
                            if (downloadInfo.getLabel().contains("mp4")) {
                                if (downloadInfo.getProgress() == 100 && aflag) {
                                    refershDownloadJudu.sendEmptyMessage(RefershDownloadJudu.refershOver);
                                    bt_watch.setText("可播放");
                                    addYingKu.setText("已缓存");
                                    aflag = false;
                                } else {
                                    bt_watch.setText("边下边播");
                                    addYingKu.setText("缓存本地");
                                }
                            } else {
                                bt_watch.setText("边下边播");
                            }
                        } else {
                            bt_watch.setText("边下边播");
                        }
                    }
                }
            } else {
                Toast.makeText(this, "视频下载失败,请查看网络连接状态", LENGTH_SHORT).show();
            }
        } else {
            if (dbmovie != null) {
                setBluesPosition(position);
                if (videoSetAdapter != null) {
                    videoSetAdapter.setBluesColor(position);
                    videoSetAdapter.notifyDataSetChanged();
                }
                final File f = new File("/storage/emulated/0/.EnglishXS/file/video/" + dbmovie.getFilmName() + "/");
                if (f.isDirectory()) {
                    File[] fileArray = f.listFiles();
                    if (null != fileArray && 0 != fileArray.length) {
                        for (int j = 0; j < fileArray.length; j++) {
                            if (fileArray[j].getName().contains(dbmovie.getFilmName() + (position + 1))) {
                                if (fileArray[j].getName().contains(".mp4")) {
                                    aflag = true;
                                }
                            }
                        }
                    }
                }
                for (int i = downloadManager.getDownloadListCount(); --i >= 0; ) {// 暂停所有正在下载的任务
                    downloadInfo = downloadManager.getDownloadInfo(i);
                    if (downloadInfo.getFileSavePath().contains(dbmovie.getFilmName() + (position + 1))) {
                        if (downloadInfo.getLabel().contains("mp4")) {
                            if (downloadInfo.getProgress() == 100 && aflag) {
                                refershDownloadJudu.sendEmptyMessage(RefershDownloadJudu.refershOver);
                                bt_watch.setText("可播放");
                                addYingKu.setText("已缓存");
                                aflag = false;
                            } else {
                                bt_watch.setText("边下边播");
                                addYingKu.setText("缓存本地");
                            }
                        } else {
                            bt_watch.setText("边下边播");
                            addYingKu.setText("缓存本地");
                        }
                    } else {
                        bt_watch.setText("边下边播");
                        addYingKu.setText("缓存本地");
                    }
                }
            }
        }
    }

    final VideoBean videoBean = new VideoBean();
    private RequestParams params;
    private List<AllMoviePath> moviePaths;

    private void play(int number) throws JSONException {
        loadingDialog.setContent("初始化视频...");
        if (!TextUtils.isEmpty(result) && NetworkRequestDeal.isErrCode(result)) {
            if (NetworkRequestDeal.getErrCode(result) == 1001) {
                Toast.makeText(this, "该视频已失效或已下架", LENGTH_SHORT).show();
            }
            return;
        }
        loadingDialog.show();
        if (moviePaths != null) {
            String url = moviePaths.get(number).getMovieurl();
            videoStyle = url.substring(url.length() - 4, url.length());
            if (dbmovie.isTV()) {
                dbmovie.setLocalVideoPath(CardUtils.videoPath + dbmovie.getFilmName() + "/" + dbmovie.getFilmName()
                        + moviePaths.get(number).getNumber() + videoStyle);
                dbmovie.setLocalSrtPath(CardUtils.videoPath + dbmovie.getFilmName() + "/" + dbmovie.getFilmName()
                        + moviePaths.get(number).getNumber() + ".srt");
                dbmovie.setLocalMP3Path(CardUtils.videoPath + dbmovie.getFilmName() + "/" + dbmovie.getFilmName()
                        + moviePaths.get(number).getNumber() + ".mp3");
            } else {
                dbmovie.setLocalVideoPath(CardUtils.videoPath + dbmovie.getFilmName() + videoStyle);
                dbmovie.setLocalSrtPath(CardUtils.videoPath + dbmovie.getFilmName() + ".srt");
                dbmovie.setLocalMP3Path(CardUtils.videoPath + dbmovie.getFilmName() + ".mp3");
            }
            Manager.InsertMovie(dbmovie);// 所有播放过的影片本地数据库里都有记录
            DownloadManager downloadManager = DownloadManager.getInstance();
            for (int i = downloadManager.getDownloadListCount(); --i >= 0; ) {// 暂停所有正在下载的任务
                downloadInfo = downloadManager.getDownloadInfo(i);
                if (downloadInfo.getState() == DownloadState.STARTED) {
                    downloadManager.stopDownload(i);
                }
            }
            if (!new File(dbmovie.getLocalSrtPath()).exists()) {
                download(moviePaths.get(number).getMoviestrurl(), dbmovie.getLocalSrtPath(),
                        dbmovie.getFilmName() + ".srt");
            }
            if (!new File(dbmovie.getLocalVideoPath()).exists()) {

                download(moviePaths.get(number).getMovieurl(), dbmovie.getLocalVideoPath(),
                        dbmovie.getFilmName() + videoStyle);
            }
            if (!new File(dbmovie.getLocalMP3Path()).exists()) {
                download(moviePaths.get(number).getMoviemp3url(), dbmovie.getLocalMP3Path(),
                        dbmovie.getFilmName() + ".mp3");
            }
            isExist = false;
        } else {
            if (localmovie != null && new File(localmovie.getLocalVideoPath() + ".tmp").exists()) {
                dbmovie.setLocalVideoPath(localmovie.getLocalVideoPath());
                dbmovie.setLocalSrtPath(localmovie.getLocalSrtPath());
                dbmovie.setLocalMP3Path(localmovie.getLocalMP3Path());
            }
            if (NetworkRequestDeal.hasErrMsg(result)) {
                Toast.makeText(getApplication(), NetworkRequestDeal.getErrMsg(result), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplication(), "加载失败", Toast.LENGTH_LONG).show();
            }

            isExist = true;
        }


        videoBean.setVideoPath(dbmovie.getLocalVideoPath());
        videoBean.setSrtPath(dbmovie.getLocalSrtPath());
        videoBean.setMp3Path(dbmovie.getLocalMP3Path());
        if (dbmovie.isTV()) {
            if (dbmovie != null && moviePaths != null) {
                videoBean.setVideoName(dbmovie.getFilmName() + moviePaths.get(number).getNumber());
            }
        } else {
            if (dbmovie != null && moviePaths != null) {
                videoBean.setVideoName(dbmovie.getFilmName());
            }
        }
        videoBean.setVideoPictureUrl(dbmovie.getFilmCovers());
        videoBean.setDownloadId(dbmovie.getDownloadId());
        loadingDialog.dismiss();
        Intent intent = new Intent(WatchVideoActivity.this, PlaySubtitleActivity.class);
        intent.putExtra(PlaySubtitleActivity.VIDEO_INFO, videoBean);
        /**
         * 首先对SD卡进行一番判断
         *
         * */

        AlertDialog.Builder builder = new AlertDialog.Builder(this);  //先得到构造器
        if (SDUtil.getSDAvailableSize() <= 50) {
            builder.setTitle("提示"); //设置标题
            builder.setMessage("手机存储介质容量过低，请清理后再进行观看"); //设置内容
            builder.setPositiveButton("继续", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    return;
                }
            });
            builder.show();
        } else {
            startActivity(intent);
        }


    }

    private String result;

    private void getMovieSet(int id, int userid) {
        if (dbmovie.isTV()) {
            params = new RequestParams(HttpImplementation.getTvShowTelevFlie());
        } else {
            params = new RequestParams(HttpImplementation.getMoviesSubtitles());
        }
        params.addBodyParameter("info", "{'id':'" + id + "','userid':'" + userid + "'}");
        // Log.e(TAG, HttpImplementation.getTvShowTelevFlie() + "?info={'id':'"
        // + id + "','userid':'" + userid + "'}");
        x.http().get(params, new CommonCallback<String>() {
            @Override
            public void onCancelled(CancelledException e) {
                JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());
            }

            @Override
            public void onError(Throwable throwable, boolean isOnCallback) {
                ErrorTool.onError(WatchVideoActivity.this, isOnCallback);
                JLog.e((throwable == null || throwable.getMessage() == null) ? "Throwable is null" : throwable.getMessage());
            }

            @Override
            public void onFinished() {
            }

            @Override
            public void onSuccess(String result) {
                // Log.e(TAG, result);
                try {
                    if (NetworkRequestDeal.isErrCode(result)) {
                        WatchVideoActivity.this.result = result;
                    } else {
                        if (dbmovie.isTV()) {
                            moviePaths = NetworkRequestDeal.getMoviesTelevFlieSubtitles(result);
                            videoSetAdapter = new VideoSetAdapter(WatchVideoActivity.this, moviePaths, tv_moviename.getText().toString(), pos);
                            if (moviePaths != null) {
//                                bt_watch.setText("边下边播");
//                                bt_watch.setOnClickListener(new OnClickListener() {
//                                    @Override
//                                    public void onClick(View view) {
//                                        Toast toast = Toast.makeText(WatchVideoActivity.this, "请选择下面的集数进行观看!", Toast.LENGTH_SHORT);
//                                        toast.setGravity(Gravity.CENTER, 0, 0);
//                                        toast.show();
//                                    }
//                                });
                                gl_videoset.setAdapter(videoSetAdapter);
                            }
                        } else {
                            moviePaths = NetworkRequestDeal.getMoviesSubtitles(result);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }

    private void download(String url, String path, String label) {
        if (url == null || url.equals("") || new File(path).exists())
            return;
        try {
            DownloadManager.getInstance().startDownload(url, label, path, true, true, null);
        } catch (DbException e) {
            JLog.e((e.getMessage() == null) ? "Exception is null" : e.getMessage());
        }
    }

    Timer timer = null;
    public int flag = 0;

    /**
     * 选择按钮状态
     */
    public void selectButton(int state) {
        switch (state) {
            case 1:// 默认按钮
                if (addYingKu.getText().equals("缓存完毕")) {
                    addYingKu.setEnabled(false);
                    return;
                }
                addYingKu.setText("缓存本地");
                if (timer != null) {
                    timer.cancel();
                }

                break;
            case 2:// 点击过已后

                break;
            case 3:// 完成后
                Toast toast = Toast.makeText(getBaseContext(), "再次点击暂停下载", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                timer = new Timer();
                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        refershDownloadJudu.sendEmptyMessage(RefershDownloadJudu.refersh);
                        if (addYingKu.getText().toString().equals("已下载100%")) {
                            refershDownloadJudu.sendEmptyMessage(RefershDownloadJudu.refershOver);
                            timer.cancel();
                        }
                    }
                };
                timer.schedule(timerTask, 0, 10);
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }

    boolean canBeDownLoad2 = true;

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (SDUtil.getSDAvailableSize() <= 200) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);  //先得到构造器
            builder.setTitle("提示"); //设置标题
            builder.setMessage("您的SD卡容量不足200M，请清理之后在进行下载学习"); //设置内容
            builder.setPositiveButton("返回", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    canBeDownLoad2 = false;
                }
            });
        }

        if (canBeDownLoad2) {
            if (addYingKu.getText().toString().equals("已缓存")) {
                return;
            }
            try {
                if (!TextUtils.isEmpty(result) && NetworkRequestDeal.isErrCode(result)) {
                    if (NetworkRequestDeal.getErrCode(result) == 1001) {
                        Toast.makeText(this, "该视频已失效或已下架", LENGTH_SHORT).show();
                    }
                    return;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (dbmovie != null) {
                if (!dbmovie.isTV()) {
                    if (isChecked) {
                        selectButton(3);
                        dbmovie.setYingKuIdentifier(1);
                        Manager.InsertMovie(dbmovie);
                        startDownLoad(0);
                    } else {
                        selectButton(1);
                        dbmovie.setYingKuIdentifier(0);
                        pauseDownLoad();
                    }
                } else {
                    if (bluesPosition >= 0 && bluesPosition < 10000) {
                        if (isChecked) {
                            selectButton(3);
                            dbmovie.setYingKuIdentifier(1);
                            Manager.InsertMovie(dbmovie);
                            startDownLoad(bluesPosition);
                            String url = dbmovie.getFilmName() + (bluesPosition + 1);
                            BaseApplication.setDownUrl(url);
                        } else {
                            selectButton(1);
                            dbmovie.setYingKuIdentifier(0);
                            pauseDownLoad();
                            BaseApplication.setDownUrl("");
                        }
                    } else {
                        Toast toast = Toast.makeText(WatchVideoActivity.this, "请选择下面的集数进行下载!", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                }
            }
        }
    }


    public void startDownLoad(int number) {
        if (moviePaths != null) {
            String url = moviePaths.get(number).getMovieurl();
            Log.i("TAG", "downdowndowndowndowndownload = " + url);
            videoStyle = url.substring(url.length() - 4, url.length());
            if (dbmovie.isTV()) {
                dbmovie.setLocalVideoPath(CardUtils.videoPath + dbmovie.getFilmName() + "/" + dbmovie.getFilmName()
                        + moviePaths.get(number).getNumber() + videoStyle);
                dbmovie.setLocalSrtPath(CardUtils.videoPath + dbmovie.getFilmName() + "/" + dbmovie.getFilmName()
                        + moviePaths.get(number).getNumber() + ".srt");
                dbmovie.setLocalMP3Path(CardUtils.videoPath + dbmovie.getFilmName() + "/" + dbmovie.getFilmName()
                        + moviePaths.get(number).getNumber() + ".mp3");
            } else {
                dbmovie.setLocalVideoPath(CardUtils.videoPath + dbmovie.getFilmName() + videoStyle);
                dbmovie.setLocalSrtPath(CardUtils.videoPath + dbmovie.getFilmName() + ".srt");
                dbmovie.setLocalMP3Path(CardUtils.videoPath + dbmovie.getFilmName() + ".mp3");
            }
            Manager.InsertMovie(dbmovie);// 所有播放过的影片本地数据库里都有记录
            DownloadManager downloadManager = DownloadManager.getInstance();
            for (int i = downloadManager.getDownloadListCount(); --i >= 0; ) {// 暂停所有正在下载的任务
                downloadInfo = downloadManager.getDownloadInfo(i);
                if (downloadInfo.getState() == DownloadState.STARTED) {
                    downloadManager.stopDownload(i);
                }
            }
            if (!new File(dbmovie.getLocalSrtPath()).exists()) {
                download(moviePaths.get(number).getMoviestrurl(), dbmovie.getLocalSrtPath(),
                        dbmovie.getFilmName() + ".srt");
            }
            if (!new File(dbmovie.getLocalVideoPath()).exists()) {

                download(moviePaths.get(number).getMovieurl(), dbmovie.getLocalVideoPath(),
                        dbmovie.getFilmName() + videoStyle);
            }
            if (!new File(dbmovie.getLocalMP3Path()).exists()) {
                download(moviePaths.get(number).getMoviemp3url(), dbmovie.getLocalMP3Path(),
                        dbmovie.getFilmName() + ".mp3");
            }
            isExist = false;
        } else {
            if (localmovie != null && new File(localmovie.getLocalVideoPath() + ".tmp").exists()) {
                dbmovie.setLocalVideoPath(localmovie.getLocalVideoPath());
                dbmovie.setLocalSrtPath(localmovie.getLocalSrtPath());
                dbmovie.setLocalMP3Path(localmovie.getLocalMP3Path());
            }
            isExist = true;
        }


        videoBean.setVideoPath(dbmovie.getLocalVideoPath());
        videoBean.setSrtPath(dbmovie.getLocalSrtPath());
        videoBean.setMp3Path(dbmovie.getLocalMP3Path());
        if (dbmovie.isTV()) {
            if (dbmovie != null && moviePaths != null) {
                videoBean.setVideoName(dbmovie.getFilmName() + moviePaths.get(number).getNumber());
            }
        } else {
            if (dbmovie != null && moviePaths != null) {
                videoBean.setVideoName(dbmovie.getFilmName());
            }
        }
        videoBean.setVideoPictureUrl(dbmovie.getFilmCovers());
        videoBean.setDownloadId(dbmovie.getDownloadId());
    }


    public void pauseDownLoad() {
        Manager.InsertMovie(dbmovie);// 所有播放过的影片本地数据库里都有记录
        DownloadManager downloadManager = DownloadManager.getInstance();
        for (int i = downloadManager.getDownloadListCount(); --i >= 0; ) {// 暂停所有正在下载的任务
            downloadInfo = downloadManager.getDownloadInfo(i);
            if (downloadInfo.getState() == DownloadState.STARTED) {
                downloadManager.stopDownload(i);
            }
        }
    }
}
