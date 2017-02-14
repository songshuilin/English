package net.naucu.englishxianshi.ui;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.app.myTool.ActionTitleBarWidget;
import com.app.myTool.ActionTitleBarWidget.ClickListener;

import net.naucu.englishxianshi.R;
import net.naucu.englishxianshi.adapter.MyHelpVideoAdapter;
import net.naucu.englishxianshi.bean.HelpVideoBean;
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
 * 描述: TODO 帮助视频
 * 作者: youyou_pc
 * 时间: 2015年11月26日  上午10:32:20
 */
public class ModifyGenderActivity extends BaseActivity {
    private ActionTitleBarWidget titlebar;//初始化标题控件
    private ListView listView;
    private List<HelpVideoBean> helpVideoBeanList;
    private HelpVideoBean videoBean;
    private final static String helpUIL = "https://naucu.com:8443/englishStudy/helpVideo";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifygender);
        initView();
        initTitleBar();
        initEvent();
        initDate();
    }

    private void initEvent() {
        titlebar.OnTitleBarClickListener(clickListener);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HelpVideoBean bean = helpVideoBeanList.get(position);
                String videourl = "https://naucu.com:8443/englishStudy/" + bean.getVideourl();
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
                Uri data = Uri.parse(videourl);
                intent.setDataAndType(data, "video/*");
                startActivity(intent);
            }
        });
    }

    private void initView() {
        titlebar = (ActionTitleBarWidget) findViewById(R.id.titlebar);
        listView = (ListView) findViewById(R.id.help_video);
    }

    private void initTitleBar() {
        titlebar.setTitleBackgroundResource(R.drawable.menubutton_select, R.drawable.menubutton_select);
        titlebar.setLeftGravity(Gravity.CENTER);
        titlebar.setLeftIco(R.drawable.back);
        titlebar.setCenterGravity(Gravity.CENTER);
        titlebar.setCenterText("帮助视频", 17, Color.BLACK);
    }

    private void initDate() {
        helpVideoBeanList = new ArrayList<>();
        x.http().get(new RequestParams(helpUIL), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String s) {
                try {
                    JSONArray jsonArray = new JSONArray(s);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        videoBean = new HelpVideoBean();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int id = jsonObject.getInt("id");
                        String videourl = jsonObject.getString("videourl");
                        String videotitle = jsonObject.getString("videotitle");
                        videoBean.setId(id);
                        videoBean.setVideourl(videourl);
                        videoBean.setVideoTitle(videotitle);
                        helpVideoBeanList.add(videoBean);
                    }

                    MyHelpVideoAdapter adapter = new MyHelpVideoAdapter(ModifyGenderActivity.this, helpVideoBeanList);
                    listView.setAdapter(adapter);
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
                MyHelpVideoAdapter adapter = new MyHelpVideoAdapter(ModifyGenderActivity.this, helpVideoBeanList);
                listView.setAdapter(adapter);

            }
        });

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
