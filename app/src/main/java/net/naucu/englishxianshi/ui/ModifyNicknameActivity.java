package net.naucu.englishxianshi.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.app.myTool.ActionTitleBarWidget;
import com.app.myTool.ActionTitleBarWidget.ClickListener;

import net.naucu.englishxianshi.R;
import net.naucu.englishxianshi.ui.base.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 类名: ModifyGenderActivity.java
 * 描述: TODO 温馨小贴士
 * 作者: youyou_pc
 * 时间: 2015年11月26日  上午10:32:20
 */
public class ModifyNicknameActivity extends BaseActivity {
    private ActionTitleBarWidget titlebar;//初始化标题控件
    private ListView lv_xiaotieshi;
    RequestParams params = new RequestParams("https://naucu.com:8443/englishStudy/allTips");
    private List<String> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifynickname);
        initView();
        initTitleBar();
        initEvent();
    }

    private void initEvent() {
        titlebar.OnTitleBarClickListener(clickListener);
    }

    private void initView() {
        titlebar = (ActionTitleBarWidget) findViewById(R.id.titlebar);
        lv_xiaotieshi = (ListView) findViewById(R.id.lv_xiaotieshi);


        /**
         * 请求小贴士
         * */
        list = new ArrayList<>();
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String s) {
                try {
                    JSONArray jsonArray = new JSONArray(s);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        String tips = jsonObject.getString("tips");
                        list.add((i + 1) +"."+ tips );
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable throwable, boolean b) {

            }

            @Override
            public void onCancelled(CancelledException e) {

            }

            @Override
            public void onFinished() {
                lv_xiaotieshi.setAdapter(new ArrayAdapter<String>(ModifyNicknameActivity.this, R.layout.item_wxxts, list));
            }
        });


    }

    private void initTitleBar() {
        titlebar.setTitleBackgroundResource(R.drawable.menubutton_select, R.drawable.menubutton_select);
        titlebar.setLeftGravity(Gravity.CENTER);
        titlebar.setLeftIco(R.drawable.back);
        titlebar.setCenterGravity(Gravity.CENTER);
        titlebar.setTitleBarBackgroundColor(Color.WHITE);
        titlebar.setCenterText("温馨小贴士", 17, Color.BLACK);
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

}
