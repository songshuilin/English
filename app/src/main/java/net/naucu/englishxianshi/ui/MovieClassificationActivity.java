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

import com.app.myTool.ActionTitleBarWidget;
import com.app.myTool.ActionTitleBarWidget.ClickListener;
import com.jiongbull.jlog.JLog;
import com.lijunsai.httpInterface.bean.ALLmovieDetailsBean;
import com.lijunsai.httpInterface.implementation.HttpImplementation;
import com.lijunsai.httpInterface.request.NetworkRequestDeal;

import net.naucu.englishxianshi.R;
import net.naucu.englishxianshi.adapter.MovieClassificationAdapter;
import net.naucu.englishxianshi.ui.base.BaseActivity;
import net.naucu.englishxianshi.util.ErrorTool;
import net.naucu.englishxianshi.util.ToastTool;
import net.naucu.englishxianshi.widget.dialog.LoadingDialog;

import org.json.JSONException;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

/**
 * 类名: MovieClassification.java
 * 描述: TODO 电影分类
 * 作者: youyou_pc
 * 时间: 2015年11月24日  下午3:34:28
 */
public class MovieClassificationActivity extends BaseActivity implements OnClickListener, OnItemClickListener {
    private ActionTitleBarWidget titlebar;//初始化标题控件
    private GridView gv_movies;
    private LoadingDialog dialog;
    private String movieclass = null;
    private int state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movieclassification);
        initView();
        initTitleBar();
        initEvent();
        initData();
    }

    private void initData() {
        movieclass = getIntent().getStringExtra("movieclass");
        state = getIntent().getIntExtra("Categorystate", -1);
        if (movieclass != null) {
            titlebar.setCenterText(movieclass, 17, Color.WHITE);
            getMovieBytwoMovie(movieclass, "3");
        }
    }

    private void initView() {
        dialog = new LoadingDialog(this);
        titlebar = (ActionTitleBarWidget) findViewById(R.id.titlebar);
        gv_movies = (GridView) findViewById(R.id.gv_movies);
    }

    private void initTitleBar() {
        titlebar.setLeftGravity(Gravity.CENTER);
        titlebar.setLeftIco(R.drawable.backs);
        titlebar.setCenterGravity(Gravity.CENTER);
    }

    private void initEvent() {
        titlebar.OnTitleBarClickListener(clickListener);
        gv_movies.setOnItemClickListener(this);

    }

    /**
     * TitleBar左右按钮点击事件
     */
    private ClickListener clickListener = new ClickListener() {

        @Override
        public void onright(View v) {

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

    @Override
    public void onClick(View v) {
        findViewById(R.id.page_v1).setVisibility(View.INVISIBLE);
        findViewById(R.id.page_v2).setVisibility(View.INVISIBLE);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startActivity(new Intent(this, WatchVideoActivity.class).putExtra("movieInformation", alLmovieDetailsBeans.get(position)).putExtra("Categorystate", state));
    }

    private RequestParams params;
    private List<ALLmovieDetailsBean> alLmovieDetailsBeans = null;

    private void getMovieBytwoMovie(String temp, String level) {
        dialog.setContent("查询中");
        dialog.show();
        System.out.println(state);
        if (state == 1) {
            params = new RequestParams(HttpImplementation.getMovieBytwoCategory());
        } else if (state == 3) {
            params = new RequestParams(HttpImplementation.getTvShowTelevBy());
        }
        params.addBodyParameter("info", "{'twoCategory':'" + temp + "','level':'" + level + "'}");
        x.http().get(params, new CommonCallback<String>() {
            @Override
            public void onCancelled(CancelledException e) {
                dialog.dismiss();
                JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());
            }

            @Override
            public void onError(Throwable throwable, boolean isOnCallback) {
                ErrorTool.onError(MovieClassificationActivity.this, isOnCallback);
                dialog.dismiss();
                JLog.e((throwable == null || throwable.getMessage() == null) ? "Throwable is null" : throwable.getMessage());
            }

            @Override
            public void onFinished() {
                dialog.dismiss();
            }

            @Override
            public void onSuccess(String result) {
                try {
                    if (NetworkRequestDeal.isErrCode(result)) {
                        switch (NetworkRequestDeal.getErrCode(result)) {
                            case 1001:
                                ToastTool.showToastLong(MovieClassificationActivity.this, "查询失败");
                                break;
                            case 1003:
                                ToastTool.showToastLong(MovieClassificationActivity.this, "服务器错误");
                                break;
                        }
                    } else {
                        if (state == 1) {
                            alLmovieDetailsBeans = NetworkRequestDeal.getMovieDetails(result);
                        } else if (state == 3) {
                            alLmovieDetailsBeans = NetworkRequestDeal.getTVSlowDetails(result);
                        }
                        if (alLmovieDetailsBeans != null) {

                            gv_movies.setAdapter(new MovieClassificationAdapter(MovieClassificationActivity.this, alLmovieDetailsBeans));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }

        });
    }
}
