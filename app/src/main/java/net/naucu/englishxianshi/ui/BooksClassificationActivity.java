package net.naucu.englishxianshi.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.app.myTool.ActionTitleBarWidget;
import com.app.myTool.ActionTitleBarWidget.ClickListener;
import com.jiongbull.jlog.JLog;
import com.lijunsai.httpInterface.bean.ALLbookDetailsBean;
import com.lijunsai.httpInterface.implementation.HttpImplementation;
import com.lijunsai.httpInterface.request.NetworkRequestDeal;

import net.naucu.englishxianshi.R;
import net.naucu.englishxianshi.adapter.BooksClassificationAdapter;
import net.naucu.englishxianshi.ui.base.BaseActivity;
import net.naucu.englishxianshi.util.ErrorTool;
import net.naucu.englishxianshi.util.ToastTool;
import net.naucu.englishxianshi.widget.dialog.LoadingDialog;
import net.naucu.englishxianshi.widget.view.PopWindows;
import net.naucu.englishxianshi.widget.view.PopWindows.QueryLevel;

import org.json.JSONException;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 类名: BooksClassificationActivity.java 描述: 电子书分类 作者: youyou_pc 时间: 2015年11月24日
 * 下午4:54:08
 */
@SuppressWarnings("unused")
public class BooksClassificationActivity extends BaseActivity implements OnClickListener, OnItemClickListener {
    private ActionTitleBarWidget titlebar;// 初始化标题控件
    private ListView lv_books;
    private LoadingDialog load;
    private List<ALLbookDetailsBean> booksBeans;
    private String temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booksclassification);
        initView();
        initTitleBar();
        initEvent();
        initData();
    }

    private void initData() {
        temp = getIntent().getStringExtra("TwoCategory");
        if (temp != null) {
            titlebar.setCenterText(temp, 17, Color.WHITE);
            getALLbookSecondary(temp, 3);
        } else {
            ToastTool.showToastLong(BooksClassificationActivity.this, "加载失败");
        }
    }

    private void initView() {
        load = new LoadingDialog(this);
        titlebar = (ActionTitleBarWidget) findViewById(R.id.titlebar);
        lv_books = (ListView) findViewById(R.id.lv_books);
    }

    private void initTitleBar() {
        titlebar.setLeftGravity(Gravity.CENTER);
        titlebar.setLeftIco(R.drawable.backs);
        titlebar.setTitleBackgroundResource(R.drawable.menubutton_select, R.drawable.menubutton_select);
        titlebar.setCenterGravity(Gravity.CENTER);
        titlebar.setRightGravity(Gravity.CENTER);
        titlebar.setRightText("推荐", 17, Color.WHITE);
    }

    private void initEvent() {
        titlebar.OnTitleBarClickListener(clickListener);
        lv_books.setOnItemClickListener(this);

    }

    /**
     * TitleBar左右按钮点击事件
     */
    private ClickListener clickListener = new ClickListener() {

        @Override
        public void onright(View v) {
            PopWindows pop = new PopWindows(BooksClassificationActivity.this);
            pop.setLayoutParams(2.5, 5);
//            pop.setLayoutParams1(3,6);
            List<String> list = new ArrayList<String>();
            String[] temps = getResources().getStringArray(R.array.tx_booksCategory);
            for (String temp : temps) {
                list.add(temp);
            }
            pop.showPopupWindow(v, 0, false);
            pop.sethomeCategoryList(list, 2);
            pop.setview(1);
            pop.onQueryLevel(queryLevel);
        }

        @Override
        public void onleft(View v) {
            finish();
        }

        @Override
        public void oncenter(View arg0) {

        }
    };

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startActivity(
                new Intent(this, BooksIntroducedActivity.class).putExtra("booksInformation", booksBeans.get(position)));
    }

    private QueryLevel queryLevel = new QueryLevel() {

        @Override
        public void level(AdapterView<?> parent, View view, int position, long id) {
            getALLbookSecondary(temp, position);

        }
    };

    private void getALLbookSecondary(String temp, int id) {
        load.setContent("获取中");
        load.show();
        RequestParams params = null;
        if (id >= 0 && id <= 2) {
            params = new RequestParams(HttpImplementation.getQueryBookbyLevel());
            params.addBodyParameter("info", "{'twoCategory':'" + temp + "','level':'" + id + "'}");
        } else {
            params = new RequestParams(HttpImplementation.getALLbookSecondary());
            params.addBodyParameter("info", "{'twoCategory':'" + temp + "'}");
        }
        x.http().get(params, new CommonCallback<String>() {
            @Override
            public void onCancelled(CancelledException cex) {
                load.dismiss();
                JLog.e((cex == null || cex.getMessage() == null) ? "Exception is null" : cex.getMessage());
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                load.dismiss();
                ErrorTool.onError(BooksClassificationActivity.this, isOnCallback);
                JLog.e((ex == null || ex.getMessage() == null) ? "Exception is null" : ex.getMessage());
            }

            @Override
            public void onFinished() {
                load.dismiss();
            }

            @Override
            public void onSuccess(String result) {
                try {
                    if (NetworkRequestDeal.isErrCode(result)) {
                        switch (NetworkRequestDeal.getErrCode(result)) {
                            case 1001:
                                ToastTool.showToastLong(BooksClassificationActivity.this, "加载失败");
                                break;
                            case 1003:
                                ToastTool.showToastLong(BooksClassificationActivity.this, "服务器错误");
                                break;
                        }
                    } else {
                        booksBeans = NetworkRequestDeal.getALLbookSecondary(result);
                        if (booksBeans != null) {
                            lv_books.setAdapter(
                                    new BooksClassificationAdapter(BooksClassificationActivity.this, booksBeans, 1));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                load.dismiss();
            }
        });
    }
}
