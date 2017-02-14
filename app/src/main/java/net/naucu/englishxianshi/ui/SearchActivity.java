package net.naucu.englishxianshi.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

import com.app.myTool.ActionTitleBarWidget;
import com.app.myTool.ActionTitleBarWidget.ClickListener;
import com.app.myTool.ActionTitleBarWidget.addTextChanged;
import com.jiongbull.jlog.JLog;
import com.lijunsai.httpInterface.bean.ALLbookDetailsBean;
import com.lijunsai.httpInterface.bean.ALLmovieDetailsBean;
import com.lijunsai.httpInterface.implementation.HttpImplementation;
import com.lijunsai.httpInterface.request.NetworkRequestDeal;

import net.naucu.englishxianshi.R;
import net.naucu.englishxianshi.adapter.BooksClassificationAdapter;
import net.naucu.englishxianshi.ui.base.BaseActivity;
import net.naucu.englishxianshi.util.ErrorTool;
import net.naucu.englishxianshi.util.ToastTool;

import org.json.JSONException;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 类名: SearchActivity.java 描述: TODO 搜索 作者: youyou_pc 时间: 2015年11月21日 上午10:25:51
 */
public class SearchActivity extends BaseActivity implements OnItemClickListener {
    public static final String TAG = SearchActivity.class.getSimpleName();




    private ActionTitleBarWidget titlebar;// 初始化标题控件
    private ListView lv_search;
    private List<ALLbookDetailsBean> booksBeans;
    private List<ALLmovieDetailsBean> alLmovieDetailsBeans;
    private BooksClassificationAdapter adapter;
    private int state = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aearch);
        initView();
        initTitleBar();

        initEvent();
    }

    private void initView() {
        state = getIntent().getIntExtra("state", -1);
        titlebar = (ActionTitleBarWidget) findViewById(R.id.titlebar);
        lv_search = (ListView) findViewById(R.id.lv_search);

    }

    private void initTitleBar() {
        titlebar.setTitleBackgroundResource(R.drawable.menubutton_select, 0);
        titlebar.setLeftGravity(Gravity.CENTER);
        titlebar.setLeftIco(R.drawable.backs);
        titlebar.setCenterGravity(Gravity.CENTER);
        EditText editText = (EditText) titlebar.getSearchEditTextView("", 4, Color.WHITE, R.drawable.search,
                R.drawable.login_registered_liftup, true, true, 2);
        editText.setHint("请输入您要查找的条目");
        editText.setGravity(Gravity.CENTER_VERTICAL);
        titlebar.setCenterView(editText);
        titlebar.onAddTextChanged(textChanged);
    }

    private void initEvent() {
        titlebar.OnTitleBarClickListener(clickListener);
        lv_search.setOnItemClickListener(this);
    }

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

    private void getQueryBookby(String temp) {

        RequestParams params = new RequestParams(HttpImplementation.getQueryBookby());
        params.addBodyParameter("info", "{'bookName':'" + temp + "'}");
        x.http().get(params, new CommonCallback<String>() {
            @Override
            public void onCancelled(CancelledException e) {
                JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());
            }

            @Override
            public void onError(Throwable throwable, boolean isOnCallback) {
                ErrorTool.onError(SearchActivity.this, isOnCallback);
                JLog.e((throwable == null || throwable.getMessage() == null) ? "Throwable is null" : throwable.getMessage());
            }

            @Override
            public void onFinished() {

            }

            @Override
            public void onSuccess(String result) {
                try {
                    if (NetworkRequestDeal.isErrCode(result)) {
                        adapter = new BooksClassificationAdapter(SearchActivity.this, new ArrayList<ALLbookDetailsBean>(),
                                1);
                        lv_search.setAdapter(adapter);

                        switch (NetworkRequestDeal.getErrCode(result)) {
                            case 1001:
                                ToastTool.showToastLong(SearchActivity.this, "查询内容不存在");
                                break;
                            case 1003:
                                ToastTool.showToastLong(SearchActivity.this, "服务器错误");
                                break;
                        }
                    } else {
                        booksBeans = NetworkRequestDeal.getSerchBook(result);
                        if (booksBeans != null) {
                            adapter = new BooksClassificationAdapter(SearchActivity.this, booksBeans, 1);
                            lv_search.setAdapter(adapter);
                        } else {
                            adapter.notifyDataSetChanged();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * 查询电影
     *
     * @param temp
     */
    private void getQueryMovie(String temp) {
        Log.i("URL", HttpImplementation.getQueryBookby());
        Log.i("info", "{'movieName':'" + temp + "'}");
        RequestParams params = new RequestParams(HttpImplementation.getqueryMoviesbysearch());
        params.addBodyParameter("info", "{'movieName':'" + temp + "'}");
        x.http().post(params, new CommonCallback<String>() {
            @Override
            public void onCancelled(CancelledException e) {
                JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());
            }

            @Override
            public void onError(Throwable throwable, boolean isOnCallback) {
                ErrorTool.onError(SearchActivity.this, isOnCallback);
                JLog.e((throwable == null || throwable.getMessage() == null) ? "Throwable is null" : throwable.getMessage());
            }

            @Override
            public void onFinished() {
            }

            @Override
            public void onSuccess(String result) {
                try {
                    if (NetworkRequestDeal.isErrCode(result)) {
                        adapter = new BooksClassificationAdapter(SearchActivity.this, new ArrayList<ALLbookDetailsBean>(),
                                2);
                        lv_search.setAdapter(adapter);
                        switch (NetworkRequestDeal.getErrCode(result)) {
                            case 1001:
                                ToastTool.showToastLong(SearchActivity.this, "查询内容不存在");
                                break;
                            case 1003:
                                ToastTool.showToastLong(SearchActivity.this, "服务器错误");
                                break;
                        }
                    } else {
                        alLmovieDetailsBeans = NetworkRequestDeal.getSerchMovie(result);
                        if (alLmovieDetailsBeans != null) {
                            booksBeans = new ArrayList<>();
                            for (int i = 0; i < alLmovieDetailsBeans.size(); i++) {
                                ALLbookDetailsBean alLbookDetailsBean = new ALLbookDetailsBean();
                                alLbookDetailsBean.setBookName(alLmovieDetailsBeans.get(i).getMovieName());
                                alLbookDetailsBean.setBookImage(alLmovieDetailsBeans.get(i).getMovieImageUrl());
                                alLbookDetailsBean.setBookpingfen(3);
                                alLbookDetailsBean.setBookAuthor(alLmovieDetailsBeans.get(i).getMovieAuthor());
                                alLbookDetailsBean.setBookAbout(alLmovieDetailsBeans.get(i).getMovieAbout());
                                booksBeans.add(alLbookDetailsBean);
                            }

                        }
                        adapter = new BooksClassificationAdapter(SearchActivity.this, booksBeans, 2);
                        lv_search.setAdapter(adapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private addTextChanged textChanged = new addTextChanged() {
        @Override
        public void TextChanged(String arg0, CharSequence arg1, int arg2, int arg3) {
            if (!arg0.equals("")) {
                if (state != -1) {
                    if (state == 1) {
                        getQueryBookby(arg0);
                    } else if (state == 2) {
                        getQueryMovie(arg0);
                    }
                }
            }
        }
    };

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (state != -1) {
            if (state == 1) {
                startActivity(new Intent(this, BooksIntroducedActivity.class).putExtra("booksInformation",
                        booksBeans.get(position)));
            } else if (state == 2) {
                startActivity(new Intent(this, WatchVideoActivity.class).putExtra("movieInformation",
                        alLmovieDetailsBeans.get(position)));
            }

        }
    }
}
