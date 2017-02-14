package net.naucu.englishxianshi.ui.video;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.fght.videoplayer.widget.media.VideoView;
import com.lijunsai.httpInterface.tool.SharedPreTool;

import net.naucu.englishxianshi.R;
import net.naucu.englishxianshi.bean.MyRecordBean;
import net.naucu.englishxianshi.download.DownloadInfo;
import net.naucu.englishxianshi.download.DownloadManager;
import net.naucu.englishxianshi.ui.LoginActivity;
import net.naucu.englishxianshi.ui.video.PlayConfig.HoType;
import net.naucu.englishxianshi.ui.video.PlayConfig.OpType;
import net.naucu.englishxianshi.ui.video.PlayConfig.ShType;
import net.naucu.englishxianshi.ui.video.adapter.SubtitleItemAdapter;
import net.naucu.englishxianshi.ui.video.adapter.VideoCallback;
import net.naucu.englishxianshi.ui.video.adapter.event.OnSubtitleItemPlayListener;
import net.naucu.englishxianshi.ui.video.adapter.event.OnSubtitleItemPromptListener;
import net.naucu.englishxianshi.ui.video.adapter.event.OnSubtitleRolePlayListener;
import net.naucu.englishxianshi.ui.video.adapter.event.OnSubtitleSelectListener;
import net.naucu.englishxianshi.ui.video.bean.ShowTime;
import net.naucu.englishxianshi.ui.video.bean.SubtitleBean;
import net.naucu.englishxianshi.ui.video.bean.SubtitleBean.ShowStatus;
import net.naucu.englishxianshi.ui.video.bean.SubtitleParseUtil;
import net.naucu.englishxianshi.ui.video.bean.SubtitleParseUtil.OnSubtitleLoadCompleteListener;
import net.naucu.englishxianshi.ui.video.window.DiscolorationCallback;
import net.naucu.englishxianshi.ui.video.window.RereadMachineWindow;
import net.naucu.englishxianshi.ui.video.window.RolePlayRecordWindow;
import net.naucu.englishxianshi.ui.video.window.SelectRolePlayWindow;
import net.naucu.englishxianshi.ui.video.window.SettingRolePlayWindow;
import net.naucu.englishxianshi.ui.video.window.SubtitleFontSizeWindow;
import net.naucu.englishxianshi.ui.video.window.SubtitlePromptWindow;
import net.naucu.englishxianshi.ui.video.window.SubtitleSelectWindow;
import net.naucu.englishxianshi.ui.video.window.SubtitleSettingWindow;
import net.naucu.englishxianshi.ui.video.window.SubtitleSpeedWindow;
import net.naucu.englishxianshi.ui.video.window.event.OnRolePlaySelectListener;
import net.naucu.englishxianshi.ui.video.window.event.OnSubtitleChangeFontSizeListener;
import net.naucu.englishxianshi.ui.video.window.event.OnSubtitleHideOrShowListener;
import net.naucu.englishxianshi.ui.video.window.event.OnSubtitleSelectPlayListener;
import net.naucu.englishxianshi.ui.video.window.event.OnWindowStateListener;
import net.naucu.englishxianshi.util.NetTool;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static net.naucu.englishxianshi.R.id.iv_rollPlay;
import static net.naucu.englishxianshi.ui.base.BaseActivity.application;

//
public class PlaySubtitleActivity extends PlayVideoActivity implements OnClickListener, OnSubtitleLoadCompleteListener,
        OnSubtitleHideOrShowListener, OnSubtitleChangeFontSizeListener, OnSubtitleSelectListener,
        OnSubtitleSelectPlayListener, OnSubtitleRolePlayListener, PlaySubtitleToActivityCallback, DiscolorationCallback, VideoCallback, SelectRolePlayWindow.SelectCallBack
{
    public static int VideoPosition;
    public static int AudioPosition;
    public static final String TAG = PlaySubtitleActivity.class.getSimpleName();
    public static final String VIDEO_POSITION = "VIDEO_POSITION";
    private SubtitleParseUtil subtitleParseUtil;
    public ListView subtitleListView;
    private List<SubtitleBean> subtitleBeanList;
    private SubtitleItemAdapter subtitleItemAdapter;
    public RereadMachineWindow rereadMachineWindow;
    private SelectRolePlayWindow selectRolePlayWindow;
    private SettingRolePlayWindow settingRolePlayWindow;
    private RolePlayRecordWindow rolePlayRecordWindow;
    public SubtitleSelectWindow subtitleSelectWindow;
    private SubtitlePromptWindow subtitlePromptWindow;
    private SubtitleSettingWindow subtitleSettingWindow;
    private SubtitleFontSizeWindow subtitleFontSizeWindow;
    private SubtitleSpeedWindow subtitleSpeedWindow;
    public boolean isRecordAudition = false;
    private boolean isSelectPlay = false;
    private List<SubtitleBean> selectSubtitleList;
    private int subtitlePosition = 0;
    private DisplayMetrics displayMetrics;
    private SubtitleRefreshHandler refreshHandler;
    private List<SubtitleBean> learnSelectSubtitleList;
    private DownloadManager downloadManager;
    private List<DownloadInfo> downloadInfoList;
    public List<MyRecordBean> RecodeList = new ArrayList<>();
    private int pausePosition = 0;
    public View headView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);

        init();
        initView();
        if (isScreenOriatationPortrait()) {
            findViewById(R.id.subtitle_fragment_layout).setVisibility(View.GONE);
        } else {
            findViewById(R.id.subtitle_fragment_layout).setVisibility(View.GONE);
        }
        if (getMyRecordBean() == null) {
            if (savedInstanceState != null && savedInstanceState.getInt(VIDEO_POSITION, 0) > 0) {
                startPlay(savedInstanceState.getInt(VIDEO_POSITION, 0));
                loadSubtitle();
            }
        }
        downloadManager = DownloadManager.getInstance();
        downloadInfoList = downloadManager.getDownloadList();

    }

    private void init() {
        AudioPosition = getAudioPosition();
        VideoPosition = getVideoPosition();
        displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        refreshHandler = new SubtitleRefreshHandler(this);
        PlayConfig.movietextSize = SharedPreTool.getSharedPreDateInt(this, PlayConfig.MOVIE_TEXT_SIZE, 14);
    }

    private void initView() {
        subtitleListView = (ListView) findViewById(R.id.subtitle_list_view);
        addHeadView();
        subtitleListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        if (subtitleSettingWindow != null) {
                            subtitleSettingWindow.closeWindow();
                            ((ImageView) findViewById(R.id.iv_moreset_video)).setImageResource(R.drawable.video_moreset_n);
                            PlayConfig.opType = OpType.NONE;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (subtitleSettingWindow != null) {
                            subtitleSettingWindow.closeWindow();
                            ((ImageView) findViewById(R.id.iv_moreset_video)).setImageResource(R.drawable.video_moreset_n);
                            PlayConfig.opType = OpType.NONE;
                        }
                        break;
                }
                return false;
            }
        });

        findViewById(R.id.video_fragment_layout).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        findViewById(R.id.iv_fullSrt).setOnClickListener(this);
        findViewById(R.id.iv_resume).setOnClickListener(this);
        findViewById(iv_rollPlay).setOnClickListener(this);
        findViewById(R.id.iv_srtSelect).setOnClickListener(this);
        findViewById(R.id.iv_moreset_video).setOnClickListener(this);
        subtitleListView.setBackgroundColor(getResources().getColor(R.color.white));
        if (subtitleSettingWindow != null) {
            subtitleSettingWindow.setOnWindowStateListener(new OnWindowStateListener() {
                @Override
                public void windowOpen() {
                }

                @Override
                public void windowInternalClose() {
                    if (PlayConfig.opType == OpType.MORE_SETTING) {
                        PlayConfig.opType = OpType.NONE;
                        ((ImageView) findViewById(R.id.iv_moreset_video)).setImageResource(R.drawable.video_moreset_n);
                    }
                }

                @Override
                public void windowExternalClose() {
                    ((ImageView) findViewById(R.id.iv_moreset_video)).setImageResource(R.drawable.video_moreset_n);
                    if (subtitleSelectWindow != null && subtitleSelectWindow.isShowing()) {
                        PlayConfig.opType = OpType.SUBTITLE_SELECT;
                        ((ImageView) findViewById(R.id.iv_moreset_video)).setImageResource(R.drawable.video_moreset_n);
                        if (subtitleItemAdapter != null) {
                            subtitleItemAdapter.changeItemHolderStyle(PlayConfig.opType);
                        }
                    }

                }
            });
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        int recordListView = getIntent().getIntExtra("dftyxdf", 0);

        if (recordListView != 1) {
            startPlay(p);
        }
        pausePlay();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pausePlay();
            }
        }, 1000);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
        if (subtitleBeanList != null) {
            subtitleItemAdapter.selectSubtitleMap = new LinkedHashMap<>();
            subtitleItemAdapter.setDataList(subtitleBeanList);
            subtitleItemAdapter.notifyDataSetChanged();
        }

    }

    int p = 0;

    public void setP() {
        p = getVideoPosition();
    }

    public int getP() {
        return p;
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (subtitleBeanList != null) {
            p = getVideoPosition();
            subtitleItemAdapter.selectSubtitleMap = new LinkedHashMap<>();
            subtitleItemAdapter.setDataList(subtitleBeanList);
        }

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (getMyRecordBean() == null) {
            if (p > 0) {
                startPlay(savedInstanceState.getInt(VIDEO_POSITION, p));
            } else {
                startPlay(savedInstanceState.getInt(VIDEO_POSITION, 0));
            }
            loadSubtitle();
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(VIDEO_POSITION, getAudioPosition());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void loadSubtitle() {
        super.loadSubtitle();
        if (subtitleParseUtil == null) {
            subtitleParseUtil = new SubtitleParseUtil();
            subtitleParseUtil.initLoadSubtitle(videoBean.getSrtPath());
            subtitleParseUtil.setOnSubtitleLoadCompleteListener(this);
        }
        subtitleParseUtil.startSubtitleParse();
    }

    public List<SubtitleBean> getSubtitleBeanList() {
        if (subtitleBeanList.size() > 0 && subtitleBeanList != null) {
            return subtitleBeanList;
        }
        return null;
    }

    @Override
    public void loadComplete(final List<SubtitleBean> subtitleBeanList) {
        if (subtitleBeanList == null || subtitleBeanList.size() <= 0) {
            return;
        }
        if (isScreenOriatationPortrait()) {
            findViewById(R.id.subtitle_fragment_layout).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.subtitle_fragment_layout).setVisibility(View.GONE);
        }
        if (this.subtitleBeanList != null) {
            this.subtitleBeanList = null;
        }
        if (this.subtitleBeanList == null) {
            this.subtitleBeanList = subtitleBeanList;
            subtitleItemAdapter = new SubtitleItemAdapter(this, this.subtitleBeanList, this, this, bsCallback);
            subtitleListView.setAdapter(subtitleItemAdapter);
            refreshHandler.sendEmptyMessageDelayed(SubtitleRefreshHandler.REFRESH_WHILE,
                    SubtitleRefreshHandler.REFRESH_TIME);
            subtitleItemAdapter.setOnSubtitleItemPlayListener(new OnSubtitleItemPlayListener() {
                @Override
                public void itemClickPlay(View view, int mark, int position) {
                    if (mark == 1) {
                        if (selectSubtitleList == null) {
                            selectSubtitleList = new ArrayList<>();
                        }
                        if (selectSubtitleList.size() > 0) {
                            selectSubtitleList.clear();
                            isSelectPlay = false;
                        }
                        selectSubtitleList.add(PlaySubtitleActivity.this.subtitleBeanList.get(position));
                        refreshHandler.sendEmptyMessage(SubtitleRefreshHandler.SELECT_SUBTITLE);
                    } else {
                        if (PlayConfig.opType == OpType.NONE && PlayConfig.hoType == HoType.PLAY) {
                            onClick(findViewById(R.id.iv_srtSelect));
                        }
                    }
                }
            });
            subtitleItemAdapter.setOnSubtitleItemPromptListener(new OnSubtitleItemPromptListener() {

                @Override
                public void itemClickPrompt(View view, int position) {
                    if (PlayConfig.opType != OpType.SUBTITLE_SELECT) {
                        return;
                    }
                }
            });
            subtitleItemAdapter.setOnSubtitleSelectListener(this);
            subtitleItemAdapter.setOnSubtitleRolePlayListener(this);
        } else {
            this.subtitleBeanList.clear();
            this.subtitleBeanList.addAll(subtitleBeanList);
            subtitleItemAdapter.notifyDataSetChanged();
        }
        if (PlayConfig.opType == OpType.ANSWER_READ || PlayConfig.opType == OpType.ROLE_PLAY
                || PlayConfig.hoType == HoType.ROLE_RECORD || PlayConfig.opType == OpType.SUBTITLE_SELECT) {
            pausePlay();
        }
        if (getMyRecordBean() != null && !isRecordAudition) {
            isRecordAudition = true;
            List<SubtitleBean> rolePlayRecordList = new ArrayList<>();
            for (int i = 0; i < this.subtitleBeanList.size(); i++) {
                for (int j = 0; j < getMyRecordBean().getSelectList().size(); j++) {
                    if (this.subtitleBeanList.get(i).getItem() == getMyRecordBean().getSelectList().get(j)) {
                        subtitleBeanList.get(i).setRolePlay(true);
                        subtitleBeanList.get(i).setRecord(true);
                        rolePlayRecordList.add(this.subtitleBeanList.get(i));
                        break;
                    }
                }
            }
            if (rolePlayRecordList.size() > 0) {
                if (rolePlayRecordWindow == null) {
                    rolePlayRecordWindow = new RolePlayRecordWindow(this);
                    rolePlayRecordWindow.setOnWindowStateListener(rolePlayRecordWindowStateListener);
                }
                rolePlayRecordWindow.setRolePlayRecordList(rolePlayRecordList);
                rolePlayRecordWindow.setMyRecordBean(getMyRecordBean());

                findViewById(R.id.video_subtitle_operation_include_layout).setVisibility(View.GONE);
                findViewById(R.id.video_subtitle_operation_record_layout).setVisibility(View.VISIBLE);
                subtitleListView.setBackgroundColor(0xffaa9999);
                LayoutParams listParams = (LayoutParams) subtitleListView.getLayoutParams();
                if (PlayConfig.isFullStatus) {
                    listParams.bottomMargin = (int) (45 * displayMetrics.density);
                } else {
                    listParams.topMargin = (int) (45 * displayMetrics.density);
                }
                subtitleListView.setLayoutParams(listParams);
                PlayConfig.opType = OpType.ROLE_PLAY;
                PlayConfig.hoType = HoType.ROLE_RECORD;
                subtitleItemAdapter.notifyDataSetChanged();
                rolePlayRecordWindow.showWindow();
            }
        }
    }

    @Override
    public void onClick(View v) {
        clearSubtitleActionBarShowState(v);
        switch (v.getId()) {
            case R.id.iv_fullSrt:
                onSubtitleFullClick(v);
                break;
            case R.id.iv_resume:
                if (v.getId() == R.id.iv_resume) {
                    onRereadMachineClick(v);
                } else {
                    onRereadMachineClick(v);
                }
                break;
            case iv_rollPlay:
                if (NetTool.isNetworkConnected(this)) {
                    if (application.getLoginApplication() == null) {
                        startActivity(new Intent(this, LoginActivity.class));
                        finish();
                    } else {
                        if (v.getId() == iv_rollPlay) {
                            onRolePlayClick(v);
                        } else {
                            onRolePlayClick(v);
                        }
                    }
                } else {
                    Toast.makeText(this, "网络异常,请检查网络连接状态", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.iv_srtSelect:
                if (v.getId() == R.id.iv_srtSelect) {
                    onSubtitleSelectClick(v);
                } else {
                    onSubtitleSelectClick(v);
                    if (selectSubtitleList != null) {
                        selectSubtitleList.clear();
                    }
                }
                break;
            case R.id.iv_moreset_video:
                if (v.getId() == R.id.iv_moreset_video) {
                    onMoreSettingClick(v);
                } else {
                    onMoreSettingClick(v);
                }
                break;
        }
        if (subtitleSelectWindow != null && subtitleSelectWindow.isShowing() && v.getId() == R.id.iv_resume)
            return;
        if (subtitleItemAdapter != null) {
            subtitleItemAdapter.changeItemHolderStyle(PlayConfig.opType);
        }
        changeHead(v.getId());
    }

    private void changeHead(int id) {
        boolean isGone = false;
        if (PlayConfig.isFullStatus) {
            headGone();
            return;
        }
        if (selectRolePlayWindow != null) {
            if (selectRolePlayWindow.isShowing()) {
                headVisiable();
                isGone = true;
            } else {
                headGone();
                isGone = true;
            }
        }
        if (subtitleSelectWindow != null) {
            if (subtitleSelectWindow.isShowing()) {
                headVisiable();
            } else {
                if (!isGone) {
                    headGone();
                }
            }
        }
        if (subtitleItemAdapter != null) {
            subtitleItemAdapter.notifyDataSetChanged();
        }
    }

    private void addHeadView() {
        View a = LayoutInflater.from(this).inflate(R.layout.item_head, null);
        headView = a.findViewById(R.id.headView);
        subtitleListView.addHeaderView(a);
    }

    private void headVisiable() {
        if (headView != null) {
            headView.setVisibility(View.VISIBLE);
        }
    }

    private void headGone() {
        if (headView != null) {
            headView.setVisibility(View.GONE);
        }
    }

    private void clearSubtitleActionBarShowState(View view) {
        switch (PlayConfig.opType) {
            case ANSWER_READ:
                if (view.getId() != R.id.iv_resume && view.getId() != R.id.iv_srtSelect && view.getId() != R.id.iv_moreset_video) {
                    PlayConfig.opType = PlayConfig.OpType.NONE;
                    ((ImageView) findViewById(R.id.iv_resume)).setImageResource(R.drawable.video_resume_n);
                    if (rereadMachineWindow != null && rereadMachineWindow.isShowing()) {
                        rereadMachineWindow.closeWindow();
                    }
                    PlayConfig.opType = PlayConfig.OpType.NONE;
                    ((ImageView) findViewById(R.id.iv_srtSelect)).setImageResource(R.drawable.xuanqu_zimu);
                    if (subtitleSelectWindow != null && subtitleSelectWindow.isShowing()) {
                        subtitleSelectWindow.closeWindow();
                    }
                }
                break;
            case ROLE_PLAY:
                if (view.getId() != iv_rollPlay) {
                    PlayConfig.opType = PlayConfig.OpType.NONE;
                    ((ImageView) findViewById(iv_rollPlay)).setImageResource(R.drawable.video_rollplayer_n);
                    if (selectRolePlayWindow != null && selectRolePlayWindow.isShowing()) {
                        selectRolePlayWindow.closeWindow();
                    }
                }
                break;
            case FULL:
                if (view.getId() != R.id.iv_fullSrt) {
                    if (PlayConfig.isFullStatus) {
                        onSubtitleFullClick(findViewById(R.id.iv_fullSrt));
                    }
                }
                break;
            case SUBTITLE_SELECT:
                if (view.getId() != R.id.iv_srtSelect && view.getId() != R.id.iv_resume && view.getId() != R.id.iv_moreset_video) {
                    PlayConfig.opType = PlayConfig.OpType.NONE;
                    ((ImageView) findViewById(R.id.iv_srtSelect)).setImageResource(R.drawable.xuanqu_zimu);
                    if (subtitleSelectWindow != null && subtitleSelectWindow.isShowing()) {
                        subtitleSelectWindow.closeWindow();
                    }
                    PlayConfig.opType = PlayConfig.OpType.NONE;
                    ((ImageView) findViewById(R.id.iv_resume)).setImageResource(R.drawable.video_resume_n);
                    if (rereadMachineWindow != null && rereadMachineWindow.isShowing()) {
                        rereadMachineWindow.closeWindow();
                    }
                }
                break;
            case MORE_SETTING:
                if (view.getId() != R.id.iv_moreset_video && view.getId() != R.id.iv_srtSelect && view.getId() != R.id.iv_resume) {
                    PlayConfig.opType = PlayConfig.OpType.NONE;
                    ((ImageView) findViewById(R.id.iv_moreset_video)).setImageResource(R.drawable.video_moreset_n);
                    if (subtitleSettingWindow != null && subtitleSettingWindow.isShowing()) {
                        subtitleSettingWindow.closeWindow();
                        ((ImageView) findViewById(R.id.iv_moreset_video)).setImageResource(R.drawable.video_moreset_n);
                    }
                }
                break;
            case NONE:
                break;
        }
    }

    public List<String> popWindowList = new ArrayList<>();

    public void insertPopWindowList(String viewId) {
        if (viewId.equals("角色扮演")) {
            popWindowList.clear();
        }
        boolean isOk = true;
        for (String s : popWindowList) {
            if (s.equals(viewId)) {
                isOk = false;
            }
        }
        if (isOk) {
            popWindowList.add(viewId);
        }
    }

    public void deletePopWindowList(String viewId) {
        if (popWindowList != null && popWindowList.size() > 0) {
            for (int i = 0; i < popWindowList.size(); i++) {
                if (popWindowList.get(i).equals(viewId)) {
                    popWindowList.remove(i);
                }
            }
        }
    }

    public void onSubtitleFullClick(View view) {
        LayoutParams operationParams = (LayoutParams) findViewById(R.id.video_subtitle_operation_01_layout)
                .getLayoutParams();
        LayoutParams listParams = (LayoutParams) subtitleListView.getLayoutParams();
        if (PlayConfig.isFullStatus) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            PlayConfig.isFullStatus = false;
            operationParams.gravity = Gravity.TOP;
            listParams.topMargin = (int) (45 * displayMetrics.density);
            listParams.bottomMargin = 0;
            ((ImageView) findViewById(R.id.iv_fullSrt)).setImageResource(R.drawable.video_srtfull_n);
            chushi();
            startPlay(-1);
            isLoop = true;
            findViewById(R.id.video_fragment_layout).setVisibility(View.VISIBLE);
            //改
            if (popWindowList != null && popWindowList.size() > 0) {
                for (int i = 0; i < popWindowList.size(); i++) {
                    if (popWindowList.get(i).equals("复读机")) {
                        if (rereadMachineWindow.isShowing()) {
                            rereadMachineWindow.dismiss();
                        }
                        ((ImageView) findViewById(R.id.iv_resume)).setImageResource(R.drawable.video_resume_f);
                        rereadMachineWindow.showWindow();
                        pausePlay();
                    }
                    if (popWindowList.get(i).equals("角色扮演")) {
                        if (selectRolePlayWindow.isShowing()) {
                            selectRolePlayWindow.dismiss();
                        }
                        ((ImageView) findViewById(iv_rollPlay)).setImageResource(R.drawable.video_rollplayer_f);
                        selectRolePlayWindow.showWindow();
                        pausePlay();
                    }
                    if (popWindowList.get(i).equals("学习菜单")) {
                        if (subtitleSelectWindow.isShowing()) {
                            subtitleSelectWindow.dismiss();
                        }
                        ((ImageView) findViewById(R.id.iv_srtSelect)).setImageResource(R.drawable.xuanqu_zimuclose);
                        subtitleSelectWindow.showWindow();
                        pausePlay();
                    }
                    if (popWindowList.get(i).equals("更多设置")) {
                        if (subtitleSettingWindow.isShowing()) {
                            subtitleSettingWindow.dismiss();
                        }
                        ((ImageView) findViewById(R.id.iv_moreset_video)).setImageResource(R.drawable.video_moreset_f);
                        subtitleSettingWindow.showWindow();
                        pausePlay();
                    }
                }
            }
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
            PlayConfig.isFullStatus = true;
            operationParams.gravity = Gravity.BOTTOM;
            listParams.topMargin = 0;
            listParams.bottomMargin = (int) (45 * displayMetrics.density);
            ((ImageView) findViewById(R.id.iv_fullSrt)).setImageResource(R.drawable.video_srtfull_f);
            pausePlay();
            isLoop = false;
            findViewById(R.id.video_fragment_layout).setVisibility(View.GONE);
            if (popWindowList != null && popWindowList.size() > 0) {
                for (int i = 0; i < popWindowList.size(); i++) {
                    if (popWindowList.get(i).equals("复读机")) {
                        if (rereadMachineWindow.isShowing()) {
                            rereadMachineWindow.dismiss();
                        }
                        ((ImageView) findViewById(R.id.iv_resume)).setImageResource(R.drawable.video_resume_f);
                        rereadMachineWindow.showWindow();
                        pausePlay();
                    }
                    if (popWindowList.get(i).equals("角色扮演")) {
                        if (selectRolePlayWindow.isShowing()) {
                            selectRolePlayWindow.dismiss();
                        }
                        ((ImageView) findViewById(iv_rollPlay)).setImageResource(R.drawable.video_rollplayer_f);
                        selectRolePlayWindow.showWindow();
                        pausePlay();
                    }
                    if (popWindowList.get(i).equals("学习菜单")) {
                        if (subtitleSelectWindow.isShowing()) {
                            subtitleSelectWindow.dismiss();
                        }
                        ((ImageView) findViewById(R.id.iv_srtSelect)).setImageResource(R.drawable.xuanqu_zimuclose);

                        ((ImageView) findViewById(iv_rollPlay)).setImageResource(R.drawable.video_rollplayer_n);
                        subtitleSelectWindow.showWindow();
                        pausePlay();
                    }
                    if (popWindowList.get(i).equals("更多设置")) {
                        if (subtitleSettingWindow.isShowing()) {
                            subtitleSettingWindow.dismiss();

                        }
                        ((ImageView) findViewById(R.id.iv_moreset_video)).setImageResource(R.drawable.video_moreset_f);
                        subtitleSettingWindow.showWindow();
                        pausePlay();
                    }
                }
            }
        }
        findViewById(R.id.video_subtitle_operation_01_layout).setLayoutParams(operationParams);
        subtitleListView.setLayoutParams(listParams);
    }

    public void onRereadMachineClick(View view) {
        ((ImageView) findViewById(R.id.iv_rollPlay)).setImageResource(R.drawable.video_rollplayer_n);
        for (int j = 0; j < popWindowList.size(); j++) {
            if (popWindowList.get(j).equals("角色扮演")) {
                deletePopWindowList("角色扮演");
            }
        }
        if (rereadMachineWindow == null) {
            rereadMachineWindow = new RereadMachineWindow(this, rereadCallBack);
            rereadMachineWindow.setBackgroundDrawable(new BitmapDrawable());

            rereadMachineWindow.setOnWindowStateListener(new OnWindowStateListener() {
                @Override
                public void windowOpen() {
                }

                @Override
                public void windowInternalClose() {
                    if (PlayConfig.opType == OpType.ANSWER_READ) {
                        if (subtitleSelectWindow != null && subtitleSelectWindow.isShowing()) {
                            PlayConfig.opType = OpType.SUBTITLE_SELECT;
                        } else {
                            PlayConfig.opType = OpType.NONE;
                        }
                        PlayConfig.opType = PlayConfig.OpType.NONE;
                        ((ImageView) findViewById(R.id.iv_resume)).setImageResource(R.drawable.video_resume_n);
                    }
                }

                @Override
                public void windowExternalClose() {
                    if (popWindowList != null && popWindowList.size() > 0 && popWindowList.contains("复读机")) {
                        popWindowList.remove("复读机");
                    }
                    ((ImageView) findViewById(R.id.iv_resume)).setImageResource(R.drawable.video_resume_n);
                    pausePlay();

                }
            });
        }

        if (!rereadMachineWindow.isShowing()) {
            PlayConfig.opType = PlayConfig.OpType.ANSWER_READ;
            ((ImageView) findViewById(R.id.iv_resume)).setImageResource(R.drawable.video_resume_f);
            pausePlay();
            isLoop = false;
            rereadMachineWindow.showWindow();
        } else {
            if (subtitleSelectWindow != null && subtitleSelectWindow.isShowing()) {
                PlayConfig.opType = OpType.SUBTITLE_SELECT;
            } else if (subtitleSettingWindow != null && subtitleSettingWindow.isShowing()) {
                PlayConfig.opType = OpType.MORE_SETTING;
            } else {
                PlayConfig.opType = PlayConfig.OpType.NONE;
            }
            ((ImageView) findViewById(R.id.iv_resume)).setImageResource(R.drawable.video_resume_n);
            if (!PlayConfig.isFullStatus) {
                chushi();
                if (subtitleSelectWindow == null) {
                    startPlay(-1);
                } else if (!subtitleSelectWindow.isShowing()) {
                    startPlay(-1);
                }
                isLoop = true;
            }
            if (rereadMachineWindow.isShowing()) {
                rereadMachineWindow.closeWindow();
                deletePopWindowList("复读机");
                ((ImageView) findViewById(R.id.iv_resume)).setImageResource(R.drawable.video_resume_n);
            }
        }
    }

    public void onRolePlayClick(View view) {
        if (selectRolePlayWindow == null) {
            selectRolePlayWindow = new SelectRolePlayWindow(this, this);
            selectRolePlayWindow.setOnWindowStateListener(new OnWindowStateListener() {
                @Override
                public void windowOpen() {
                }

                @Override
                public void windowInternalClose() {
                    if (PlayConfig.opType == OpType.ROLE_PLAY) {
                        PlayConfig.opType = PlayConfig.OpType.NONE;
                        ((ImageView) findViewById(iv_rollPlay)).setImageResource(R.drawable.video_rollplayer_n);
                        findViewById(R.id.video_subtitle_operation_include_layout).setVisibility(View.GONE);
                        findViewById(R.id.video_subtitle_operation_record_layout).setVisibility(View.VISIBLE);
                        PlayConfig.hoType = HoType.ROLE_RECORD;
                        subtitleItemAdapter.notifyDataSetChanged();
                        subtitleListView.setBackgroundColor(0xffaa9999);
                        LayoutParams listParams = (LayoutParams) subtitleListView.getLayoutParams();
                        if (PlayConfig.isFullStatus) {
                            listParams.bottomMargin = (int) (40 * displayMetrics.density);
                        } else {
                            listParams.topMargin = (int) (40 * displayMetrics.density);
                        }
                        subtitleListView.setLayoutParams(listParams);
                    }
                }

                @Override
                public void windowExternalClose() {

                }
            });
            selectRolePlayWindow.setOnRolePlaySelectListener(new OnRolePlaySelectListener() {

                @Override
                public void rolePlaySelectFirst() {
                    subtitleItemAdapter.clearRolePlayFirst();
                }

                @Override
                public void rolePlaySelectTail() {
                    subtitleItemAdapter.clearRolePlayFirst();
                }
            });
        }
        if (settingRolePlayWindow == null) {
            settingRolePlayWindow = new SettingRolePlayWindow(this);
            selectRolePlayWindow.setSettingRolePlayWindow(settingRolePlayWindow);
        }
        if (rolePlayRecordWindow == null) {
            rolePlayRecordWindow = new RolePlayRecordWindow(this);
            rolePlayRecordWindow.setOnWindowStateListener(rolePlayRecordWindowStateListener);
        }
        rolePlayRecordWindow.setMyRecordBean(null);
        selectRolePlayWindow.setRolePlayRecordWindow(rolePlayRecordWindow);
        switch (PlayConfig.opType) {
            case ROLE_PLAY:
                PlayConfig.opType = PlayConfig.OpType.NONE;
                ((ImageView) findViewById(iv_rollPlay)).setImageResource(R.drawable.video_rollplayer_n);
                if (!PlayConfig.isFullStatus) {
                    chushi();
                    startPlay(-1);
                    isLoop = true;
                }
                if (selectRolePlayWindow.isShowing()) {
                    selectRolePlayWindow.closeWindow();
                    deletePopWindowList("角色扮演");
                    hengshuping();
                }
                break;
            default:
                PlayConfig.opType = PlayConfig.OpType.ROLE_PLAY;
                ((ImageView) findViewById(iv_rollPlay)).setImageResource(R.drawable.video_rollplayer_f);
                pausePlay();
                isLoop = false;
                if (!selectRolePlayWindow.isShowing()) {
                    selectRolePlayWindow.showWindow();
                    if (rereadMachineWindow != null) {
                        if (rereadMachineWindow.isShowing()) {
                            rereadMachineWindow.closeWindow();
                            ((ImageView) findViewById(R.id.iv_resume)).setImageResource(R.drawable.video_resume_n);
                        }
                    }
                    if (subtitleSelectWindow != null) {
                        if (subtitleSelectWindow.isShowing()) {
                            subtitleSelectWindow.closeWindow();
                            ((ImageView) findViewById(R.id.iv_srtSelect)).setImageResource(R.drawable.xuanqu_zimu);
                        }
                    }
                    if (subtitleSettingWindow != null) {
                        if (subtitleSettingWindow.isShowing()) {
                            subtitleSettingWindow.closeWindow();
                            ((ImageView) findViewById(R.id.iv_moreset_video)).setImageResource(R.drawable.video_moreset_n);
                        }
                    }
                    qiangzhishuping();
                }
        }
    }

    public void qiangzhishuping() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public void hengshuping() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
    }

    private OnWindowStateListener rolePlayRecordWindowStateListener = new OnWindowStateListener() {
        @Override
        public void windowOpen() {
        }

        @Override
        public void windowInternalClose() {
            findViewById(R.id.video_subtitle_operation_include_layout).setVisibility(View.VISIBLE);
            findViewById(R.id.video_subtitle_operation_record_layout).setVisibility(View.GONE);
            PlayConfig.hoType = HoType.PLAY;
            subtitleItemAdapter.notifyDataSetChanged();
            subtitleListView.setBackgroundColor(getResources().getColor(R.color.white));
            LayoutParams listParams = (LayoutParams) subtitleListView.getLayoutParams();
            if (PlayConfig.isFullStatus) {
                listParams.bottomMargin = (int) (45 * displayMetrics.density);
            } else {
                listParams.topMargin = (int) (45 * displayMetrics.density);
            }
            subtitleListView.setLayoutParams(listParams);
            //
            if (!PlayConfig.isFullStatus) {
                chushi();
                startPlay(-1);
                isLoop = true;
            }
        }

        @Override
        public void windowExternalClose() {
        }
    };

    public void onSubtitleSelectClick(View view) {
        ((ImageView) findViewById(R.id.iv_rollPlay)).setImageResource(R.drawable.video_rollplayer_n);
        for (int j = 0; j < popWindowList.size(); j++) {
            if (popWindowList.get(j).equals("角色扮演")) {
                deletePopWindowList("角色扮演");
            }
        }
        if (subtitleSelectWindow == null) {
            subtitleSelectWindow = new SubtitleSelectWindow(this, this, this);
            subtitleSelectWindow.setCloseCallBack(selectCallback);
            subtitleSelectWindow.setOnWindowStateListener(new OnWindowStateListener() {
                @Override
                public void windowOpen() {
                }

                @Override
                public void windowInternalClose() {
                    if (PlayConfig.opType == OpType.SUBTITLE_SELECT) {
                        if (selectRolePlayWindow.isShowing()) {
                            PlayConfig.opType = OpType.ROLE_PLAY;
                        } else {
                            PlayConfig.opType = OpType.NONE;
                        }
                        PlayConfig.hoType = HoType.PLAY;
                        ((ImageView) findViewById(R.id.iv_srtSelect)).setImageResource(R.drawable.xuanqu_zimu);

                        subtitleItemAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void windowExternalClose() {
                }
            });
            subtitleSelectWindow.setOnSubtitleSelectPlayListener(this);
        }
        if (subtitlePromptWindow == null) {
            subtitlePromptWindow = new SubtitlePromptWindow(this);
            subtitleSelectWindow.setSubtitlePromptWindow(subtitlePromptWindow);
        }

        if (!subtitleSelectWindow.isShowing()) {
            PlayConfig.opType = PlayConfig.OpType.SUBTITLE_SELECT;
            ((ImageView) findViewById(R.id.iv_srtSelect)).setImageResource(R.drawable.xuanqu_zimuclose);
            pausePlay();
            isLoop = false;
            subtitleSelectWindow.showWindow();
        } else {
            if (rereadMachineWindow != null && rereadMachineWindow.isShowing()) {
                PlayConfig.opType = OpType.SUBTITLE_SELECT;
            } else if (subtitleSettingWindow != null && subtitleSettingWindow.isShowing()) {
                PlayConfig.opType = OpType.MORE_SETTING;
            } else {
                PlayConfig.opType = PlayConfig.OpType.NONE;
            }
            ((ImageView) findViewById(R.id.iv_srtSelect)).setImageResource(R.drawable.xuanqu_zimu);
            if (!PlayConfig.isFullStatus) {

                chushi();
                if (rereadMachineWindow == null) {
                    startPlay(-1);
                } else if (!rereadMachineWindow.isShowing()) {
                    startPlay(-1);
                }
                isLoop = true;
            }
            if (subtitleSelectWindow.isShowing()) {
                subtitleSelectWindow.closeWindow();
                deletePopWindowList("学习菜单");
                ((ImageView) findViewById(R.id.iv_srtSelect)).setImageResource(R.drawable.xuanqu_zimu);
            }
        }
    }

    public void onMoreSettingClick(View view) {
        ((ImageView) findViewById(R.id.iv_rollPlay)).setImageResource(R.drawable.video_rollplayer_n);
        for (int j = 0; j < popWindowList.size(); j++) {
            if (popWindowList.get(j).equals("角色扮演")) {
                deletePopWindowList("角色扮演");
            }
        }
        if (subtitleSettingWindow == null) {
            subtitleSettingWindow = new SubtitleSettingWindow(this);
            subtitleSettingWindow.setOnSubtitleHideOrShowListener(this);
            subtitleSettingWindow.setOnWindowStateListener(new OnWindowStateListener() {
                @Override
                public void windowOpen() {
                }

                @Override
                public void windowInternalClose() {
                    if (PlayConfig.opType == OpType.MORE_SETTING) {
                        PlayConfig.opType = OpType.NONE;
                        ((ImageView) findViewById(R.id.iv_moreset_video)).setImageResource(R.drawable.video_moreset_n);
                    }
                }

                @Override
                public void windowExternalClose() {
                    ((ImageView) findViewById(R.id.iv_moreset_video)).setImageResource(R.drawable.video_moreset_n);
                    if (subtitleSelectWindow != null && subtitleSelectWindow.isShowing()) {
                        PlayConfig.opType = OpType.SUBTITLE_SELECT;
                        ((ImageView) findViewById(R.id.iv_moreset_video)).setImageResource(R.drawable.video_moreset_n);
                        if (subtitleItemAdapter != null) {
                            subtitleItemAdapter.changeItemHolderStyle(PlayConfig.opType);
                        }
                    }

                }
            });
        }
        if (subtitleFontSizeWindow == null) {
            subtitleFontSizeWindow = new SubtitleFontSizeWindow(this);
            subtitleSettingWindow.setSubtitleFontSizeWindow(subtitleFontSizeWindow);
            subtitleFontSizeWindow.setOnSubtitleChangeFontSizeListener(this);
        }
        if (subtitleSpeedWindow == null) {
            subtitleSpeedWindow = new SubtitleSpeedWindow(this);
            subtitleSettingWindow.setSubtitleSpeedWindow(subtitleSpeedWindow);
        }

        if (!subtitleSettingWindow.isShowing()) {
            PlayConfig.opType = PlayConfig.OpType.MORE_SETTING;
            ((ImageView) findViewById(R.id.iv_moreset_video)).setImageResource(R.drawable.video_moreset_f);
            pausePlay();
            isLoop = false;

            subtitleSettingWindow.showWindow();
        } else {
            if (subtitleSelectWindow != null && subtitleSelectWindow.isShowing()) {
                PlayConfig.opType = OpType.SUBTITLE_SELECT;
            } else if (rereadMachineWindow != null && rereadMachineWindow.isShowing()) {
                PlayConfig.opType = OpType.ROLE_PLAY;
            } else {
                PlayConfig.opType = OpType.NONE;
            }
            ((ImageView) findViewById(R.id.iv_moreset_video)).setImageResource(R.drawable.video_moreset_n);
            if (!PlayConfig.isFullStatus) {
                chushi();
                startPlay(-1);
                isLoop = true;
            }
            if (subtitleSettingWindow.isShowing()) {
                subtitleSettingWindow.closeWindow();
                deletePopWindowList("更多设置");
                ((ImageView) findViewById(R.id.iv_moreset_video)).setImageResource(R.drawable.video_moreset_n);
            }
        }
    }

    public List<SubtitleBean> list = new ArrayList<>();

    @Override
    public void callBack(List<SubtitleBean> list) {
        this.list = list;
        setList(list);
    }

    public List<SubtitleBean> getList() {
        return list;
    }

    public void setList(List<SubtitleBean> list) {
        this.list = list;
    }

    @Override
    public void callback(List<SubtitleBean> list, boolean flag) {
        if (list != null) {
            if (list.size() > 0) {
                int[] listItem = new int[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    listItem[i] = list.get(i).getItem();
                }
                subtitleItemAdapter.clearSelection(listItem, flag);
                subtitleItemAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void callBackSelect(List<SubtitleBean> selectSubtitleList) {
        learnSelectSubtitleList = selectSubtitleList;
        VideoView video = getVideoView();
        if (video != null) {
            pausePosition = video.getCurrentPosition();
        }
    }

    public SubtitleItemAdapter getAdapters() {
        return subtitleItemAdapter;
    }

    @Override
    public void selectClosecakkBack() {
        if (m != null) {
            m.setIsLearn(false);
        }
    }

    @Override
    public void selectOpencakkBack() {
        if (m != null) {
            m.setIsLearn(true);
        }
    }

    public void allStop() {
        if (subtitleSelectWindow != null) {
            subtitleSelectWindow.closettsIflytek();
        }
    }


    public void openPopWindowCallback() {
        if (popWindowList != null && popWindowList.size() > 0) {
            popWindowList.clear();
        }
        if (subtitleSelectWindow == null) {
            subtitleSelectWindow = new SubtitleSelectWindow(this, this, this);
            subtitleSelectWindow.setCloseCallBack(selectCallback);
            subtitleSelectWindow.setOnWindowStateListener(new OnWindowStateListener() {
                @Override
                public void windowOpen() {
                }

                @Override
                public void windowInternalClose() {
                    if (PlayConfig.opType == OpType.SUBTITLE_SELECT) {
                        if (selectRolePlayWindow.isShowing()) {
                            PlayConfig.opType = OpType.ROLE_PLAY;
                        } else {
                            PlayConfig.opType = OpType.NONE;
                        }
                        PlayConfig.hoType = HoType.PLAY;
                        ((ImageView) findViewById(R.id.iv_srtSelect)).setImageResource(R.drawable.xuanqu_zimu);
                        subtitleItemAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void windowExternalClose() {
                }
            });
            subtitleSelectWindow.setOnSubtitleSelectPlayListener(this);
        }
        if (!subtitleSelectWindow.isShowing()) {
            PlayConfig.opType = PlayConfig.OpType.SUBTITLE_SELECT;
            ((ImageView) findViewById(R.id.iv_srtSelect)).setImageResource(R.drawable.xuanqu_zimuclose);
            pausePlay();
            isLoop = false;
            subtitleSelectWindow.showWindow();
        } else {
            if (rereadMachineWindow != null && rereadMachineWindow.isShowing()) {
                PlayConfig.opType = OpType.SUBTITLE_SELECT;
            } else if (subtitleSettingWindow != null && subtitleSettingWindow.isShowing()) {
                PlayConfig.opType = OpType.MORE_SETTING;
            } else {
                PlayConfig.opType = PlayConfig.OpType.NONE;
            }
            ((ImageView) findViewById(R.id.iv_srtSelect)).setImageResource(R.drawable.xuanqu_zimu);
            if (!PlayConfig.isFullStatus) {

                chushi();
                if (rereadMachineWindow == null) {
                    startPlay(-1);
                } else if (!rereadMachineWindow.isShowing()) {
                    startPlay(-1);
                }
                isLoop = true;
            }
            if (subtitleSelectWindow.isShowing()) {
                subtitleSelectWindow.closeWindow();
                ((ImageView) findViewById(R.id.iv_srtSelect)).setImageResource(R.drawable.xuanqu_zimu);
            }
        }

    }



    /**
     * 65535
     */
    private static class SubtitleRefreshHandler extends Handler {
        private final WeakReference<PlaySubtitleActivity> reference;
        public static final int REFRESH_TIME = 300;
        public static final int REFRESH_WHILE = 31;
        public static final int SELECT_SUBTITLE = 32;
        public static final int aa = 30;
        int now = 0;

        public SubtitleRefreshHandler(PlaySubtitleActivity subtitleActivity) {
            reference = new WeakReference<>(subtitleActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            if (reference.get() == null) {
                return;
            }
            switch (msg.what) {
                case REFRESH_WHILE:
                    if (reference.get().subtitleBeanList == null || reference.get().subtitleBeanList.size() <= 0) {
                        removeCallbacksAndMessages(null);
                        return;
                    }
                    if (!reference.get().isVideoPlaying()) {
                        reference.get().pauseAudio();
                    } else {
                        reference.get().startAudio(-1);
                        reference.get().adjustAudioError();
                        reference.get().setVideoSubtitleShow();
                        reference.get().switchCurrentSubtitleShowState();
                    }
                    if (reference.get().isSelectPlay) {
                        sendEmptyMessage(SELECT_SUBTITLE);
                    }
                    sendEmptyMessageDelayed(REFRESH_WHILE, REFRESH_TIME);
                    break;
                case SELECT_SUBTITLE:
                    if (reference.get().selectSubtitleList != null && reference.get().selectSubtitleList.size() > 0) {
                        reference.get().startPlay(reference.get().selectSubtitleList.get(0).getShowTime().getBegin());
                        final Timer timer = new Timer();
                        TimerTask task = new TimerTask() {
                            @Override
                            public void run() {
                                if (reference.get() != null) {
                                    now = reference.get().getAudioPosition();
                                    if (reference.get().selectSubtitleList.size() >0) {
                                        if (now > reference.get().selectSubtitleList.get(reference.get().selectSubtitleList.size() - 1).getShowTime().getEnd()) {
                                            sendEmptyMessage(SubtitleRefreshHandler.aa);
                                            reference.get().getVideoView().pause();
                                            timer.cancel();
                                            now = 0;

                                        }
                                    }
                                }
                            }
                        };
                        timer.schedule(task, 0, 1);
                    }
                    break;
                case aa:
                    clearSelect();
                    break;
            }
        }

        private void clearSelect() {
            reference.get().isSelectPlay = false;
            reference.get().pausePlay();
            reference.get().selectSubtitleList.clear();
            if (PlayConfig.hoType == HoType.SELECT_PLAY) {
                PlayConfig.hoType = HoType.SELECT_SUB;
            }
            reference.get().subtitleItemAdapter.notifyDataSetChanged();
        }

    }

    private void adjustAudioError() {
        if (PlayConfig.hoType == HoType.ROLE_RECORD || (selectSubtitleList != null && selectSubtitleList.size() > 0)) {
            return;
        }
        if (Math.abs(getVideoPosition() - getAudioPosition()) > 1000) {
            startAudio(getVideoPosition());
        }
    }

    private void setVideoSubtitleShow() {
        if (subtitlePosition <= subtitleBeanList.size() - 1) {

            ShowTime showTime = subtitleBeanList.get(subtitlePosition).getShowTime();
            if (getAudioPosition() >= showTime.getBegin() && showTime.getEnd() > getAudioPosition()) {
                String[] subtitles = getCurrentShowSubtitle();
                setSubtitleShow(subtitles[0], subtitles[1]);
            } else {
                setSubtitleShow("", "");
                switchCurrentShowSubtitle();
            }
        }
    }

    /**
     * get666
     *
     * @return
     */
    private String[] getCurrentShowSubtitle() {
        SubtitleBean subtitleBean = subtitleBeanList.get(subtitlePosition);

        String[] subtitles = {"", ""};

        if (subtitleBean != null) {
            switch (PlayConfig.shType) {
                case NONE:
                    subtitles[0] = subtitleBean.getChinese();
                    subtitles[1] = subtitleBean.getEnglish();
                    break;
                case CHINESE:
                    subtitles[1] = subtitleBean.getEnglish();
                    break;
                case ENGLISH:
                    subtitles[0] = subtitleBean.getChinese();
                    break;
                default:
                    break;
            }
        }
        return subtitles;
    }

    private void switchCurrentShowSubtitle() {
        if (subtitlePosition <= subtitleBeanList.size() - 1) {
            ShowTime showTime = subtitleBeanList.get(subtitlePosition).getShowTime();
            if (getAudioPosition() < showTime.getBegin()) {
                if ((subtitlePosition - 1) < 0) {
                    return;
                }
                showTime = subtitleBeanList.get(subtitlePosition - 1).getShowTime();
                if (getAudioPosition() >= showTime.getBegin() && showTime.getEnd() > getAudioPosition()) {
                    subtitlePosition = subtitlePosition + 1;
                } else {
                    for (int i = 0; i < subtitlePosition; i++) {
                        showTime = subtitleBeanList.get(i).getShowTime();
                        if (getAudioPosition() >= showTime.getBegin() && showTime.getEnd() > getAudioPosition()) {
                            subtitlePosition = i;
                            break;
                        }
                    }
                }
            } else if (getAudioPosition() > showTime.getEnd()) {
                if ((subtitlePosition + 1) > subtitleBeanList.size() - 1) {
                    return;
                }
                showTime = subtitleBeanList.get(subtitlePosition + 1).getShowTime();
                if (showTime.getBegin() <= getAudioPosition() && showTime.getEnd() > getAudioPosition()) {
                    subtitlePosition += 1;
                } else {
                    for (int i = subtitlePosition + 1; i < subtitleBeanList.size(); i++) {
                        showTime = subtitleBeanList.get(i).getShowTime();
                        if (showTime.getBegin() <= getAudioPosition() && showTime.getEnd() > getAudioPosition()) {
                            subtitlePosition = i;
                            break;
                        }
                    }
                }
            }
        }
    }

    public boolean flag = false;

    public void setFalg(boolean falg) {
        flag = falg;
    }

    private void switchCurrentSubtitleShowState() {
        if (subtitlePosition <= subtitleBeanList.size() - 1) {
            SubtitleBean subtitleBean = subtitleBeanList.get(subtitlePosition);
            ShowTime showTime = subtitleBean.getShowTime();
            if (showTime.getBegin() <= getAudioPosition() && showTime.getEnd() > getAudioPosition()) {
                subtitleBean.setShowStatus(ShowStatus.CURRENT);
            } else if (getAudioPosition() > showTime.getEnd()) {
                subtitleBean.setShowStatus(ShowStatus.HAVE);
            } else if (getAudioPosition() < showTime.getBegin()) {
                subtitleBean.setShowStatus(ShowStatus.NOT);
            }
            for (int i = 0; i < subtitlePosition; i++) {
                if (subtitleBeanList.get(i).getShowStatus() != ShowStatus.HAVE) {
                    subtitleBeanList.get(i).setShowStatus(ShowStatus.HAVE);
                } else {
                    continue;
                }
            }
            for (int i = subtitlePosition + 1; i < subtitleBeanList.size(); i++) {
                if (subtitleBeanList.get(i).getShowStatus() != ShowStatus.NOT) {
                    subtitleBeanList.get(i).setShowStatus(ShowStatus.NOT);
                } else {
                    continue;
                }
            }
            subtitleItemAdapter.notifyDataSetChanged();
            if (selectSubtitleList != null && selectSubtitleList.size() > 0) {
                return;
            }

            switch (PlayConfig.movietextSize) {
                case 22:
                    if (subtitlePosition > 2) {
                        subtitleListView.setSelection(subtitlePosition - 2);
                    }
                    break;
                case 24:
                    if (subtitlePosition > 1) {
                        subtitleListView.setSelection(subtitlePosition - 1);
                    }
                    break;
                default:
                    if (flag == false) {
                        if (subtitlePosition > 3) {
                            subtitleListView.setSelection(subtitlePosition);
                        }
                    } else {
                        subtitleListView.setSelection(subtitlePosition + 1);
                    }
                    break;
            }
        }
    }

    @Override
    public void changeSubtitleHideOrShow() {
        subtitleItemAdapter.notifyDataSetChanged();
    }

    @Override
    public void chengeSubtitleFontSize() {
        subtitleItemAdapter.notifyDataSetChanged();

    }

    @Override
    public void callbackSubtitleSelectList(List<SubtitleBean> selectSubtitleList) {
        if (subtitleSelectWindow == null) {
            return;
        }
        subtitleSelectWindow.setSelectSubtitleList(selectSubtitleList);
    }

    @Override
    public void subtitleSelectPlay(List<SubtitleBean> selectSubtitleList) {
        if (this.selectSubtitleList == null) {
            this.selectSubtitleList = new ArrayList<>();
        }
        if (this.selectSubtitleList.size() > 0) {
            this.selectSubtitleList.clear();
            isSelectPlay = false;
        }
        this.selectSubtitleList.addAll(selectSubtitleList);
        refreshHandler.sendEmptyMessage(SubtitleRefreshHandler.SELECT_SUBTITLE);
        PlayConfig.hoType = HoType.SELECT_PLAY;
//        subtitleListView.setAdapter(subtitleItemAdapter);
        subtitleItemAdapter.notifyDataSetChanged();
    }

    @Override
    public void subtitleRolePlayBegin(List<SubtitleBean> beginSubtitleList) {
        if (selectRolePlayWindow == null) {
            selectRolePlayWindow = new SelectRolePlayWindow(this, this);
        }
        if (beginSubtitleList == null || beginSubtitleList.size() <= 1) {
            selectRolePlayWindow.setRolePlayBeginSubtitle(0);
        } else {
            selectRolePlayWindow.setRolePlayBeginSubtitle(beginSubtitleList.size());
        }
    }

    public interface bsCallback {
        void callBack();

    }

    public void setTextColors() {
        selectRolePlayWindow.setTextColors();
    }

    bsCallback bsCallback = new bsCallback() {
        @Override
        public void callBack() {
            selectRolePlayWindow.setTextColors();
        }
    };

    @Override
    public void subtitleRolePlayEnd(List<SubtitleBean> endSubtitleList, int endPosition) {
        rolePlayRecordWindow.setRolePlayRecordList(endSubtitleList);
        selectRolePlayWindow.setRolePlayEndSubtitle(endSubtitleList.size(), endPosition);
    }

    @Override
    protected void onDestroy() {
        if (refreshHandler != null) {
            refreshHandler.removeCallbacksAndMessages(null);
        }
        resetConfig();
        super.onDestroy();
    }

    @Override
    public void finish() {
        if (refreshHandler != null) {
            refreshHandler.removeCallbacksAndMessages(null);
        }
        resetConfig();
        super.finish();
    }

    private void resetConfig() {
        PlayConfig.opType = OpType.NONE;
        PlayConfig.shType = ShType.NONE;
        PlayConfig.hoType = HoType.PLAY;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (rereadMachineWindow != null) {
            rereadMachineWindow.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        int screenWidth, screenHeight;
        WindowManager windowManager = this.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        screenWidth = display.getWidth();
        screenHeight = display.getHeight();
        if (m == null) {
            m = new com.fght.videoplayer.widget.MediaController(this);
        }
        if (screenWidth > screenHeight) {
            m.mSrtChinese.setTextSize(16);
            m.mSrtEnglish.setTextSize(16);
        } else {
            OpenWindow();
            m.mSrtChinese.setTextSize(12);
            m.mSrtEnglish.setTextSize(12);
        }

        if (screenWidth > screenHeight) {
            if (rereadMachineWindow != null && rereadMachineWindow.isShowing()) {
                rereadMachineWindow.dismiss();
                ((ImageView) findViewById(R.id.iv_resume)).setImageResource(R.drawable.video_resume_n);
            }
            if (selectRolePlayWindow != null && selectRolePlayWindow.isShowing()) {
                selectRolePlayWindow.dismiss();
                ((ImageView) findViewById(iv_rollPlay)).setImageResource(R.drawable.video_rollplayer_n);
            }
            if (rolePlayRecordWindow != null && rolePlayRecordWindow.isShowing()) {
                rolePlayRecordWindow.dismiss();
            }
            if (subtitleSelectWindow != null && subtitleSelectWindow.isShowing()) {
                subtitleSelectWindow.dismiss();
            }
            if (subtitlePromptWindow != null && subtitlePromptWindow.isShowing()) {
                subtitlePromptWindow.dismiss();
            }
            if (subtitleFontSizeWindow != null && subtitleFontSizeWindow.isShowing()) {
                subtitleFontSizeWindow.dismiss();
            }
            if (subtitleSpeedWindow != null && subtitleSpeedWindow.isShowing()) {
                subtitleSpeedWindow.dismiss();
            }
            if (subtitleSettingWindow != null && subtitleSettingWindow.isShowing()) {
                subtitleSettingWindow.dismiss();
                ((ImageView) findViewById(R.id.iv_moreset_video)).setImageResource(R.drawable.video_moreset_n);

            }
        }
        if (screenWidth < screenHeight) {
            if (subtitleListView == null) {
                initView();
            } else if (subtitleListView.getAdapter() == null) {
                loadSubtitle();
            }
            findViewById(R.id.subtitle_fragment_layout).setVisibility(View.VISIBLE);
            switch (PlayConfig.opType) {
                case ANSWER_READ:
                    PlayConfig.opType = PlayConfig.OpType.ANSWER_READ;
                    ((ImageView) findViewById(R.id.iv_resume)).setImageResource(R.drawable.video_resume_f);
                    pausePlay();
                    isLoop = false;
                    if (!rereadMachineWindow.isShowing()) {
                        rereadMachineWindow.showWindow();
                    }
                    break;
                case ROLE_PLAY:
                    PlayConfig.opType = PlayConfig.OpType.ROLE_PLAY;
                    ((ImageView) findViewById(iv_rollPlay)).setImageResource(R.drawable.video_rollplayer_f);
                    pausePlay();
                    isLoop = false;
                    if (selectRolePlayWindow != null) {
                        if (!selectRolePlayWindow.isShowing()) {
                            selectRolePlayWindow.showWindow();
                        }
                    }
                    break;
                case SUBTITLE_SELECT:
                    PlayConfig.opType = PlayConfig.OpType.SUBTITLE_SELECT;
                    ((ImageView) findViewById(R.id.iv_srtSelect)).setImageResource(R.drawable.xuanqu_zimuclose);
                    pausePlay();
                    isLoop = false;
                    if (!subtitleSelectWindow.isShowing()) {
                        subtitleSelectWindow.showWindow();
                    }
                    break;
                case MORE_SETTING:
                    PlayConfig.opType = PlayConfig.OpType.MORE_SETTING;
                    ((ImageView) findViewById(R.id.iv_moreset_video)).setImageResource(R.drawable.video_moreset_f);
                    pausePlay();
                    isLoop = false;
                    if (!subtitleSettingWindow.isShowing()) {
                        subtitleSettingWindow.showWindow();
                    }
                    break;
            }
        } else {
            if (settingRolePlayWindow != null) {
                settingRolePlayWindow.closeWindow();
            }
            if (subtitlePromptWindow != null) {
                subtitlePromptWindow.closeWindow();
            }
            if (subtitleFontSizeWindow != null) {
                subtitleFontSizeWindow.closeWindow();
            }
            if (rereadMachineWindow != null) {
                rereadMachineWindow.closeWindow();
            }
            switch (PlayConfig.opType) {
                case ANSWER_READ:
                    PlayConfig.opType = PlayConfig.OpType.ANSWER_READ;
                    ((ImageView) findViewById(R.id.iv_resume)).setImageResource(R.drawable.video_resume_f);
                    if (!PlayConfig.isFullStatus) {
                        chushi();
                        startPlay(-1);
                        isLoop = true;
                    }
                    if (rereadMachineWindow.isShowing()) {
                        rereadMachineWindow.closeWindow();
                        ((ImageView) findViewById(R.id.iv_resume)).setImageResource(R.drawable.video_resume_n);
                    }

                    break;
                case ROLE_PLAY:
                    PlayConfig.opType = PlayConfig.OpType.ROLE_PLAY;
                    ((ImageView) findViewById(iv_rollPlay)).setImageResource(R.drawable.video_rollplayer_n);
                    if (!PlayConfig.isFullStatus) {
                        chushi();

                        startPlay(-1);


                        isLoop = true;
                    }
                    if (selectRolePlayWindow != null) {
                        if (selectRolePlayWindow.isShowing()) {
                            selectRolePlayWindow.closeWindow();
                        }
                    }
                    break;
                case SUBTITLE_SELECT:
                    PlayConfig.opType = PlayConfig.OpType.SUBTITLE_SELECT;
                    ((ImageView) findViewById(R.id.iv_srtSelect)).setImageResource(R.drawable.xuanqu_zimu);
                    if (!PlayConfig.isFullStatus) {
                        chushi();
                        startPlay(-1);
                        isLoop = true;
                    }
                    if (subtitleSelectWindow.isShowing()) {
                        subtitleSelectWindow.closeWindow();
                    }
                    break;
                case MORE_SETTING:
                    PlayConfig.opType = PlayConfig.OpType.MORE_SETTING;
                    ((ImageView) findViewById(R.id.iv_moreset_video)).setImageResource(R.drawable.video_moreset_n);
                    if (!PlayConfig.isFullStatus) {
                        chushi();
                        startPlay(-1);
                        isLoop = true;
                    }
                    if (subtitleSettingWindow.isShowing()) {
                        subtitleSettingWindow.closeWindow();
                        ((ImageView) findViewById(R.id.iv_moreset_video)).setImageResource(R.drawable.video_moreset_n);
                    }
                    break;
            }
            if (findViewById(R.id.video_view_01).getLayoutParams().getClass().getName().equals("android.widget.LinearLayout$LayoutParams")) {
                LinearLayout.LayoutParams lp;
                lp = (LinearLayout.LayoutParams) findViewById(R.id.video_view_01).getLayoutParams();
                lp.width = getPingMuWidth();
                lp.height = getPingMuHeight();
                findViewById(R.id.video_view_01).setLayoutParams(lp);
                findViewById(R.id.subtitle_fragment_layout).setVisibility(View.GONE);
            } else {
                FrameLayout.LayoutParams lp;
                lp = (FrameLayout.LayoutParams) findViewById(R.id.video_view_01).getLayoutParams();
                lp.width = getPingMuWidth();
                lp.height = getPingMuHeight();
                findViewById(R.id.video_view_01).setLayoutParams(lp);
                findViewById(R.id.subtitle_fragment_layout).setVisibility(View.GONE);
            }
        }

    }

    @Override
    public void OpenWindow() {
        openPopWindowCallback();
    }

    public int getPingMuHeight() {
        WindowManager wm = (WindowManager) getBaseContext()
                .getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        return height;
    }

    public int getPingMuWidth() {
        WindowManager wm = (WindowManager) getBaseContext()
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        return width;
    }

    private boolean isScreenOriatationPortrait() {
        return this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    @Override
    public int mediaCallback() {
        for (int i = 0; i < downloadInfoList.size(); i++) {
            int index = downloadInfoList.get(i).getFileSavePath().lastIndexOf("/");
            if (downloadInfoList.get(i).getFileSavePath().substring(index + 1, downloadInfoList.get(i).getFileSavePath().length())
                    .equals(videoBean.getVideoName() + ".mp4") ||
                    downloadInfoList.get(i).getFileSavePath().substring(index + 1, downloadInfoList.get(i).getFileSavePath().length())
                            == videoBean.getVideoName() + ".mp4") {
                return 10 * (int) downloadInfoList.get(i).getProgress();
            }
        }
        return 1000;
    }

    @Override
    public boolean mediaCallbackQP() {
        if (isScreenOriatationPortrait() == false) {
        }
        return isScreenOriatationPortrait();
    }


    public SubtitleSelectWindow getSubtitleSelectWindow() {
        return subtitleSelectWindow;
    }

    public RereadMachineWindow getRereadMachineWindow() {
        return rereadMachineWindow;
    }

    public SelectRolePlayWindow getSelectRolePlayWindow() {
        return selectRolePlayWindow;
    }

    public SubtitleSettingWindow getSubtitleSettingWindow() {
        return subtitleSettingWindow;
    }

    SubtitleSelectWindow.WindowCallBack
            selectCallback = new SubtitleSelectWindow.WindowCallBack() {
        @Override
        public void closeCallBack() {
            if (selectRolePlayWindow != null && selectRolePlayWindow.isShowing()) {
                selectRolePlayWindow.closeWindow();
                if (popWindowList != null && popWindowList.contains("角色扮演")) {
                    popWindowList.remove("角色扮演");
                }
            }
            if (PlayConfig.isFullStatus) {
                if (rereadMachineWindow != null && rereadMachineWindow.isShowing()) {
                    rereadMachineWindow.yidongDown();
                }
                if (subtitleSettingWindow != null && subtitleSettingWindow.isShowing()) {
                    subtitleSettingWindow.yidongDown();
                }
            } else {
                if (subtitleSettingWindow != null && subtitleSettingWindow.isShowing()) {
                    subtitleSettingWindow.yidongDown();
                }
            }
        }

        @Override
        public void openCallBack() {
            if (PlayConfig.isFullStatus) {
                if (rereadMachineWindow != null && rereadMachineWindow.isShowing()) {
                    rereadMachineWindow.yidongUp();
                }
                if (subtitleSettingWindow != null && subtitleSettingWindow.isShowing()) {
                    subtitleSettingWindow.yidongUp();
                }
            } else {
                if (subtitleSettingWindow != null && subtitleSettingWindow.isShowing()) {
                    subtitleSettingWindow.yidongUp();
                }
                if (subtitleSettingWindow != null && subtitleSettingWindow.isShowing()) {
                    subtitleSettingWindow.yidongDown();
                }
            }
        }
    };

    RereadMachineWindow.WindowCallBack rereadCallBack = new RereadMachineWindow.WindowCallBack() {
        @Override
        public void openCallBack() {
            if (subtitleSettingWindow != null && subtitleSettingWindow.isShowing()) {
                subtitleSettingWindow.yidongUp();
            }
        }

        @Override
        public void closeCallBack() {
            if (PlayConfig.isFullStatus) {
                if (subtitleSettingWindow != null && subtitleSettingWindow.isShowing()) {
                    subtitleSettingWindow.yidongDown();
//                subtitleSettingWindow.down
                }
            }

        }
    };

    public List<SubtitleBean> getLearnSelectSubtitleList() {
        return learnSelectSubtitleList;
    }
}
