package net.naucu.englishxianshi.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.app.myTool.ActionTitleBarWidget;
import com.app.myTool.ActionTitleBarWidget.ClickListener;
import com.app.myTool.ActionTitleBarWidget.SearchEditOnClickListener;
import com.jiongbull.jlog.JLog;
import com.lijunsai.httpInterface.bean.ALLbookBean;
import com.lijunsai.httpInterface.implementation.HttpImplementation;
import com.lijunsai.httpInterface.request.NetworkRequestDeal;

import net.naucu.englishxianshi.R;
import net.naucu.englishxianshi.adapter.HomeBooksListAdapter;
import net.naucu.englishxianshi.util.ErrorTool;
import net.naucu.englishxianshi.util.NetTool;
import net.naucu.englishxianshi.util.ToastTool;
import net.naucu.englishxianshi.widget.dialog.LoadingDialog;
import net.naucu.englishxianshi.widget.view.PopWindows;

import org.json.JSONException;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

public class HomeBooksActivity extends Fragment {

    private ListView lv_homebooks;// 电子书ListView
    private HomeBooksListAdapter adapter;// 适配器
    // private List<HomeBooksBean>booksBeans;//电子书实体
    private View view;
    private LoadingDialog load;
    private ActionTitleBarWidget titlebar;// 初始化标题控件
    private PopWindows pop;// 自定义视图窗口
    private LinearLayout ll_aredata, ll_nodata;

    public View onCreateView(android.view.LayoutInflater inflater, android.view.ViewGroup container,
                             Bundle savedInstanceState) {
        view = LayoutInflater.from(getActivity()).inflate(R.layout.activity_homebooks, container, false);
        initView();
        initEvent();
        initTitleBar();
        getAllBooks();
        return view;
    }

    ;

    /**
     * 初始化事件
     */
    private void initEvent() {
        titlebar.OnTitleBarClickListener(clickListener);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        load = new LoadingDialog(getActivity());
        lv_homebooks = (ListView) view.findViewById(R.id.lv_homebooks);
        titlebar = (ActionTitleBarWidget) view.findViewById(R.id.titlebar);
        ll_aredata = (LinearLayout) view.findViewById(R.id.aredata);
        ll_nodata = (LinearLayout) view.findViewById(R.id.nodata);
    }

    private SearchEditOnClickListener searchEditOnClickListener = new SearchEditOnClickListener() {

        @Override
        public void onClick(View v) {
            startActivity(new Intent(getActivity(), SearchActivity.class).putExtra("state", 1));
        }
    };

    /**
     * 初始化TitleBar
     */
    private void initTitleBar() {
        titlebar.setLeftGravity(Gravity.CENTER);
        titlebar.setTitleBackgroundResource(R.drawable.menubutton_select, R.drawable.menubutton_select);
        titlebar.setLeftIco(R.drawable.show_spreads);
//        titlebar.setRightGravity(Gravity.CENTER);
//        titlebar.setRightIco(R.drawable.classification_menu);
        titlebar.setCenterGravity(Gravity.CENTER);
        titlebar.setCenterView(titlebar.getSearchEditTextView("书籍", 4, Color.WHITE, R.drawable.search,
                R.drawable.login_registered_liftup, true, false, 1));
        titlebar.setSearchEditOnClickListener(searchEditOnClickListener);
    }

    private void getAllBooks() {
        if (!NetTool.isNetworkConnected(getContext())) {
            isnodata(false);
            return;
        }
        load.setContent("获取中");
        load.show();
        RequestParams params = new RequestParams(HttpImplementation.getALLbook());
        x.http().get(params, new CommonCallback<String>() {
            @Override
            public void onCancelled(CancelledException e) {
                load.dismiss();
                JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());
            }

            @Override
            public void onError(Throwable throwable, boolean isOnCallback) {
                load.dismiss();
                ErrorTool.onError(getActivity(), isOnCallback);
                isnodata(false);
                JLog.e((throwable == null || throwable.getMessage() == null) ? "Throwable is null" : throwable.getMessage());
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
                                ToastTool.showToastLong(getActivity(), "加载失败!");
                                isnodata(false);
                                break;
                            case 1002:
                                ToastTool.showToastLong(getActivity(), "服务器错误!");
                                isnodata(false);
                                break;
                        }
                    } else {
                        List<ALLbookBean> alLbookBeans = NetworkRequestDeal.getALLbook(result);
                        if (alLbookBeans != null) {
                            isnodata(true);
                            adapter = new HomeBooksListAdapter(getActivity(), alLbookBeans);
                            lv_homebooks.setAdapter(adapter);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                load.dismiss();
            }
        });

    }

//    private void getDirectoryAccess(final View v) {
//        RequestParams params = new RequestParams(HttpImplementation.getDirectoryAccess());
//        x.http().get(params, new CommonCallback<String>() {
//            @Override
//            public void onCancelled(CancelledException e) {
//                load.dismiss();
//                JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());
//            }
//
//            @Override
//            public void onError(Throwable throwable, boolean isOnCallback) {
//                load.dismiss();
//                ErrorTool.onError(getActivity(), isOnCallback);
//                JLog.e((throwable == null || throwable.getMessage() == null) ? "Throwable is null" : throwable.getMessage());
//            }
//
//            @Override
//            public void onFinished() {
//                load.dismiss();
//            }
//
//            @Override
//            public void onSuccess(String result) {
//                try {
//                    if (NetworkRequestDeal.isErrCode(result)) {
//                        switch (NetworkRequestDeal.getErrCode(result)) {
//
//                        }
//                    } else {
//                        List<String> list = NetworkRequestDeal.getDirectoryAccess(result);
//                        if (list != null) {
//                            pop = new PopWindows(getActivity());
//                            pop.setLayoutParams(4, 1);
//                            pop.showPopupWindow(v, 0, false);
//                            pop.sethomeCategoryList(list, 1);
//                            pop.setview(0);
//                        }
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                load.dismiss();
//            }
//        });
//
//    }

    /**
     * TitleBar左右按钮点击事件
     */
    private ClickListener clickListener = new ClickListener() {

        @Override
        public void onright(View v) {
            v.setEnabled(false);
//            getDirectoryAccess(v);
        }

        @Override
        public void onleft(View v) {
            HomeActivity.isshow();
        }

        @Override
        public void oncenter(View arg0) {

        }
    };

    public void isnodata(boolean b) {
        if (ll_aredata == null || ll_nodata == null)
            return;
        if (b) {
            ll_aredata.setVisibility(View.VISIBLE);
            ll_nodata.setVisibility(View.GONE);
        } else {
            ll_nodata.setVisibility(View.VISIBLE);
            ll_aredata.setVisibility(View.GONE);
        }
    }
}
