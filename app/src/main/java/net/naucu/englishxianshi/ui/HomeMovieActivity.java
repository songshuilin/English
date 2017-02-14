package net.naucu.englishxianshi.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.app.myTool.ActionTitleBarWidget;
import com.app.myTool.ActionTitleBarWidget.ClickListener;
import com.app.myTool.ActionTitleBarWidget.OnTabCheckedChangeListener;
import com.jiongbull.jlog.JLog;
import com.lijunsai.httpInterface.bean.ALLmovieBean;
import com.lijunsai.httpInterface.implementation.HttpImplementation;
import com.lijunsai.httpInterface.request.NetworkRequestDeal;

import net.naucu.englishxianshi.BaseApplication;
import net.naucu.englishxianshi.R;
import net.naucu.englishxianshi.adapter.HomeMovieAdapter;
import net.naucu.englishxianshi.util.ErrorTool;
import net.naucu.englishxianshi.util.NetTool;
import net.naucu.englishxianshi.util.ToastTool;
import net.naucu.englishxianshi.widget.dialog.LoadingDialog;

import org.json.JSONException;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.HashMap;
import java.util.List;

/**
/**
 * 类名: HomeMovieActivity.java 描述: 首页电影 作者: youyou_pc 时间: 2015年11月16日 下午3:32:35
 */
public class HomeMovieActivity extends Fragment {
    public static final String TAG = HomeMovieActivity.class.getSimpleName();
    private ListView lv_homemoview;// 电影ListView
    private HomeMovieAdapter adapter;// 电影适配器
    // private List<HomeMovieBean> homeMovieBeans;
    private ActionTitleBarWidget titlebar;// 初始化标题控件
    private View view;
    private LoadingDialog load;
    private LinearLayout ll_aredata, ll_nodata;
    public static boolean needShow = false;

    public View onCreateView(android.view.LayoutInflater inflater, android.view.ViewGroup container,
                             Bundle savedInstanceState) {
        view = LayoutInflater.from(getActivity()).inflate(R.layout.activity_homemovie, container, false);
        initView();
        initEvent();
        initTitleBar();
        getAllMovie(1);
        if (needShow) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    HomeActivity.isshow();
                    needShow = false;
                }
            }, 1000);
        }
        return view;
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        titlebar.OnTitleBarClickListener(clickListener);
        titlebar.OnTabCheckedChange(checkedChangeListener);
    }

    private OnTabCheckedChangeListener checkedChangeListener = new OnTabCheckedChangeListener() {

        @Override
        public void button3() {
            if (NetTool.isNetworkConnected(getContext())) {
                if (((BaseApplication) getActivity().getApplication()).getLoginApplication() == null) {
                    startActivity(new Intent(getContext(), LoginActivity.class));
                    HomeMovieActivity.this.getActivity().finish();
                } else {
                    getAllMovie(3);
                }
            } else {
                getAllMovie(3);
                Toast.makeText(getContext(), "网络异常,请检查网络连接状态", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void button1() {
            if (NetTool.isNetworkConnected(getContext())) {
                if (((BaseApplication) getActivity().getApplication()).getLoginApplication() == null) {
                    startActivity(new Intent(getContext(), LoginActivity.class));
                    HomeMovieActivity.this.getActivity().finish();
                } else {
                    getAllMovie(1);
                }
            } else {
                getAllMovie(1);
                Toast.makeText(getContext(), "网络异常,请检查网络连接状态", Toast.LENGTH_SHORT).show();
            }
        }
    };

    /**
     * 初始化TitleBar
     */
    private void initTitleBar() {
        titlebar.setLeftGravity(Gravity.CENTER);
        titlebar.setTitleBackgroundResource(R.drawable.menubutton_select, R.drawable.menubutton_select);
        titlebar.setLeftIco(R.drawable.show_spreads);
        titlebar.setRightGravity(Gravity.CENTER);
        titlebar.setCenterGravity(Gravity.CENTER);
        titlebar.setCenterView(titlebar.getTABView(R.color.home_tab_text, R.drawable.login_registered_liftup, 4, "电影",
                "名家演讲", "美剧", R.drawable.shape_home_title_left, R.drawable.shape_home_title_center,
                R.drawable.shape_home_title_right));
        titlebar.setRightIco(R.drawable.search);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        load = new LoadingDialog(getActivity());
        lv_homemoview = (ListView) view.findViewById(R.id.lv_homemoview);
        titlebar = (ActionTitleBarWidget) view.findViewById(R.id.titlebar);
        ll_aredata = (LinearLayout) view.findViewById(R.id.aredata);
        ll_nodata = (LinearLayout) view.findViewById(R.id.nodata);
    }

    /**
     * TitleBar左右按钮点击事件
     */
    private ClickListener clickListener = new ClickListener() {

        @Override
        public void onright(View v) {
            if (NetTool.isNetworkConnected(getContext())) {
                startActivity(new Intent(getActivity(), SearchActivity.class).putExtra("state", 2));
            } else {
                Toast.makeText(getContext(), "网络异常,请检查网络连接状态", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onleft(View v) {
            HomeActivity.isshow();
        }

        @Override
        public void oncenter(View arg0) {

        }
    };

    @Override
    public void onResume() {
        super.onResume();

    }

    private RequestParams params;

    private void getAllMovie(final int i) {
        if (!NetTool.isNetworkConnected(getContext())) {
            isnodata(false);
            return;
        }
        load.setContent("获取中");
        load.show();
        if (i == 1) {
            params = new RequestParams(HttpImplementation.getMovie());
        } else if (i == 3) {
            params = new RequestParams(HttpImplementation.getTVShow());
        }

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
                        List<ALLmovieBean> alLmovieBeans = null;
                        if (i == 1) {
                            alLmovieBeans = NetworkRequestDeal.getMovie(result);
                        } else if (i == 3) {
                            alLmovieBeans = NetworkRequestDeal.getSlowMovie(result);
                        }
                        if (alLmovieBeans != null) {
                            isnodata(true);
                            //首页分类列表
                            adapter = new HomeMovieAdapter(getActivity(), alLmovieBeans, i);
                            lv_homemoview.setAdapter(adapter);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                load.dismiss();
            }
        });

    }

    private void isnodata(boolean b) {
        if (b) {
            ll_aredata.setVisibility(View.VISIBLE);
            ll_nodata.setVisibility(View.GONE);
        } else {
            ll_nodata.setVisibility(View.VISIBLE);
            ll_aredata.setVisibility(View.GONE);
        }
    }

}
