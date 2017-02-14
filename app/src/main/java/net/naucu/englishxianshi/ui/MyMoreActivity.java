package net.naucu.englishxianshi.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.myTool.ActionTitleBarWidget;
import com.app.myTool.ActionTitleBarWidget.ClickListener;
import com.lijunsai.httpInterface.bean.ALLmovieDetailsBean;

import net.naucu.englishxianshi.R;
import net.naucu.englishxianshi.adapter.DownloadHomeMovieAdapter;
import net.naucu.englishxianshi.adapter.DownloadHomeMovieAdapter.OnChecked;
import net.naucu.englishxianshi.bean.VideoBean;
import net.naucu.englishxianshi.db.Manager;
import net.naucu.englishxianshi.db.dao.Movie;
import net.naucu.englishxianshi.download.DownloadInfo;
import net.naucu.englishxianshi.download.DownloadManager;
import net.naucu.englishxianshi.ui.base.BaseActivity;
import net.naucu.englishxianshi.ui.video.PlaySubtitleActivity;
import net.naucu.englishxianshi.ui.video.TvSelectBlues;
import net.naucu.englishxianshi.widget.dialog.PromptDialog;
import net.naucu.englishxianshi.widget.dialog.PromptDialog.onPromptClickListener;

import java.io.File;
import java.util.List;

/**
 * 类名: MyBooksActivity.java 描述: TODO 我的影库 作者: youyou_pc 时间: 2015年11月20日
 * 下午3:57:27
 */
@SuppressWarnings("unused")
public class MyMoreActivity extends BaseActivity implements OnClickListener, OnItemClickListener {
    public static final String TAG = MyMoreActivity.class.getSimpleName();
    private ActionTitleBarWidget titlebar;// 初始化标题控件
    private List<Movie> movies ;
    private DownloadHomeMovieAdapter downloadHomeMovieAdapter;
    private boolean isAccording = false;
    private GridView gv_myview;
    private LinearLayout bottom_choice;
    private PromptDialog dialog;
    private DownloadManager downloadManager;
    private List<DownloadInfo> downloadInfoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mygridview);
        initView();
        initTitleBar();
        initEvent();
    }



    private void initView() {
        downloadManager = DownloadManager.getInstance();
        downloadInfoList = downloadManager.getDownloadList();
        titlebar = (ActionTitleBarWidget) findViewById(R.id.titlebar);
        gv_myview = (GridView) findViewById(R.id.gv_myview);
        bottom_choice = (LinearLayout) findViewById(R.id.bottom_choice);
    }

    /**
     * 初始化TitleBar
     */
    private void initTitleBar() {
        isTitleRight();
        titlebar.setTitleBackgroundResource(R.drawable.menubutton_select, R.drawable.menubutton_select);
        titlebar.setLeftGravity(Gravity.CENTER);
        titlebar.setLeftIco(R.drawable.back);
        titlebar.setRightGravity(Gravity.CENTER);
        titlebar.setRightText(getString(R.string.tx_editor), 17, Color.BLACK);
        titlebar.setCenterText(getString(R.string.tx_MyYingKu), 17, Color.BLACK);
        titlebar.setCenterGravity(Gravity.CENTER);
        titlebar.setTitleBarBackgroundColor(Color.WHITE);
    }

    private void initEvent() {
        titlebar.OnTitleBarClickListener(clickListener);
        bottom_choice.setOnClickListener(this);
        gv_myview.setOnItemClickListener(this);
    }

    /**
     * TitleBar左右按钮点击事件
     */
    private ClickListener clickListener = new ClickListener() {
        @Override
        public void onright(View v) {
            if (movies != null && movies.size() > 0) {
                isAccordings(isAccording);
            }
        }

        @Override
        public void onleft(View v) {
            finish();
        }

        @Override
        public void oncenter(View arg0) {
            // TODO Auto-generated method stub
        }
    };

    private void initDate() {
        // selectview(false);
    }

    /**
     * 统计
     */
    private OnChecked checked = new OnChecked() {

        @Override
        public void Count(int i) {
            ((TextView) findViewById(R.id.tv_counts)).setText("(" + i + ")");
        }
    };

    @Override
    protected void onResume() {
        super.onResume();

        if (downloadHomeMovieAdapter != null) {
            downloadHomeMovieAdapter.notifyDataSetChanged();
        }else{
            initView();
            initEvent();
        }
        selectview(false);
    }

    private void selectview(boolean b) {
        if (movies == null||movies.isEmpty()) {
            movies = Manager.SelectMovie();
        } else {
            movies.clear();
            movies.addAll(Manager.SelectMovie());
        }
        if (movies != null) {
            if (!movies.isEmpty()) {
                if (downloadHomeMovieAdapter != null) {
                    downloadHomeMovieAdapter.setSelect(b);
                    downloadHomeMovieAdapter.notifyDataSetChanged();
                } else {
                    downloadHomeMovieAdapter = new DownloadHomeMovieAdapter(this, movies, b);
                    downloadHomeMovieAdapter.setOnChecked(checked);
                    gv_myview.setAdapter(downloadHomeMovieAdapter);
                    gv_myview.setOnItemClickListener(this);

                }
                isnodate(true);
            } else {
                isnodate(false);
            }
        } else {
            isnodate(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bottom_choice:
                if (((TextView) findViewById(R.id.tv_counts)).getText().equals("(0)") || ((TextView) findViewById(R.id.tv_counts)).getText() == "(0)") {
                    Toast toast = Toast.makeText(MyMoreActivity.this, "请选择要删除的电影", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {
                    final PromptDialog dialog = new PromptDialog(this);
                    dialog.setContent(getString(R.string.tx_deleteinformation));
                    dialog.show();
                    dialog.setClickListener(new onPromptClickListener() {
                        @Override
                        public void onDetermine(View v) {
                            downloadHomeMovieAdapter.SelectedDelete(application);
                            selectview(false);
                            isAccordings(true);
                            dialog.dismiss();
                        }

                        @Override
                        public void onCancel(View v) {
                            dialog.dismiss();
                        }
                    });
                }
                break;
        }
    }

    public void isAccordings(Boolean isAccording) {
        titlebar.RightViewRemove();
        if (isAccording) {
            selectview(false);
            isTitleRight();
            bottom_choice.setVisibility(View.GONE);
            this.isAccording = false;
        } else {
            titlebar.setRightText(getString(R.string.tx_cancel), 17, Color.BLACK);
            bottom_choice.setVisibility(View.VISIBLE);
            selectview(true);
            this.isAccording = true;
        }
    }

    private void isTitleRight() {
        if (movies != null) {
            if (movies.size() != 0) {
                titlebar.setTitleBackgroundResource(R.drawable.menubutton_select, R.drawable.menubutton_select);
                titlebar.setRightText(getString(R.string.tx_editor), 17, Color.BLACK);
            } else {
                titlebar.setTitleBackgroundResource(R.drawable.menubutton_select, R.color.transparent);
                titlebar.setRightText("", 17, Color.BLACK);
            }
        }
    }

    public TextView jindu;

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        jindu = (TextView) findViewById(R.id.jindu);
        Movie movie = movies.get(arg2);
        if (!movie.isTV()) {
            if (new File(movie.getLocalVideoPath()).exists()) {
                VideoBean bean = new VideoBean();
                bean.setDownloadId(movie.getDownloadId());
                bean.setSrtPath(movie.getLocalSrtPath());
                bean.setVideoName(movie.getFilmName());
                bean.setVideoPath(movie.getLocalVideoPath());
                bean.setMp3Path(movie.getLocalMP3Path());
                bean.setVideoPictureUrl(movie.getFilmCovers());
                Intent intent = new Intent(this, PlaySubtitleActivity.class);
                intent.putExtra(PlaySubtitleActivity.VIDEO_INFO, bean);
                startActivity(intent);
            } else {
                ALLmovieDetailsBean alLmovieDetailsBean = new ALLmovieDetailsBean();
                alLmovieDetailsBean.setMovieAuthor(movie.getAuthor());
                alLmovieDetailsBean.setId(movie.getDownloadId());
                alLmovieDetailsBean.setMovieImageUrl(movie.getFilmCovers());
                alLmovieDetailsBean.setTwoCategory(movie.getFilmGenre());
                alLmovieDetailsBean.setMovieName(movie.getFilmName());
                alLmovieDetailsBean.setMovieAbout(movie.getIntroduction());
                alLmovieDetailsBean.setMovieMadeBy(movie.getNationality());
                alLmovieDetailsBean.setMoviepingfen(movie.getScore());
                alLmovieDetailsBean.setTV(movie.isTV());
                startActivity(new Intent(this, WatchVideoActivity.class).putExtra("movieInformation", alLmovieDetailsBean));
            }
        } else {
            ALLmovieDetailsBean alLmovieDetailsBean = new ALLmovieDetailsBean();
            alLmovieDetailsBean.setMovieAuthor(movie.getAuthor());
            alLmovieDetailsBean.setId(movie.getDownloadId());
            alLmovieDetailsBean.setMovieImageUrl(movie.getFilmCovers());
            alLmovieDetailsBean.setTwoCategory(movie.getFilmGenre());
            alLmovieDetailsBean.setMovieName(movie.getFilmName());
            alLmovieDetailsBean.setMovieAbout(movie.getIntroduction());
            alLmovieDetailsBean.setMovieMadeBy(movie.getNationality());
            alLmovieDetailsBean.setMoviepingfen(movie.getScore());
            alLmovieDetailsBean.setTV(movie.isTV());
            startActivity(new Intent(this, TvSelectBlues.class).putExtra("movieInformation", alLmovieDetailsBean));
        }
    }

    public void setMovieSize(int size, String movieName) {
        if (size == 0) {
            for (int i = 0; i < Manager.SelectMovie().size(); i++) {
                if (Manager.SelectMovie().get(i).getFilmName().contains(movieName)) {
                    Manager.DeleteMyMovie(Manager.SelectMovie().get(i).getId());
                }
            }
        }
    }
}