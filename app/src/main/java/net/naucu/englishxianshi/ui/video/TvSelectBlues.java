package net.naucu.englishxianshi.ui.video;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lijunsai.httpInterface.bean.ALLmovieDetailsBean;
import com.lijunsai.httpInterface.bean.AllMoviePath;

import net.naucu.englishxianshi.R;
import net.naucu.englishxianshi.bean.VideoBean;
import net.naucu.englishxianshi.db.Manager;
import net.naucu.englishxianshi.db.dao.Movie;
import net.naucu.englishxianshi.download.DownloadInfo;
import net.naucu.englishxianshi.download.DownloadManager;
import net.naucu.englishxianshi.ui.MyMoreActivity;
import net.naucu.englishxianshi.ui.WatchVideoActivity;
import net.naucu.englishxianshi.ui.base.BaseActivity;
import net.naucu.englishxianshi.ui.video.adapter.TvSelectBluesAdapter;
import net.naucu.englishxianshi.widget.dialog.LoadingDialog;
import net.naucu.englishxianshi.widget.dialog.PromptDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Y on 2016/10/29.
 */
public class TvSelectBlues extends BaseActivity {
    public String sdCard;
    private List<Movie> movies;
    public static TextView tv_counts;
    public LinearLayout bottom_choice;
    public TextView tv_bianji;
    public LinearLayout tv_backs;
    public TextView Tv_titleName;
    public Boolean isSelect;
    ALLmovieDetailsBean alLmovieDetailsBean = new ALLmovieDetailsBean();
    private DownloadInfo downloadInfo = new DownloadInfo();
    private List<AllMoviePath> moviePaths;
    public TvSelectBluesAdapter adapter;
    public GridView gridView;
    private LoadingDialog loadingDialog;
    final VideoBean videoBean = new VideoBean();
    public String result;
    public DownloadManager downloadManager;
    final List<Map<String, Object>> fileList = new ArrayList<Map<String, Object>>();

    public static TextView getTv_counts() {
        return tv_counts;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tvselectblues);
        initView();
    }

    public void initView() {
        Intent intent = getIntent();
        alLmovieDetailsBean = (ALLmovieDetailsBean) intent.getSerializableExtra("movieInformation");
        downloadManager = DownloadManager.getInstance();
//        for (int i = 0; i < downloadManager.getDownloadList().size(); i++) {
//            if (downloadManager.getDownloadList().get(i).getFileSavePath().equals("/storage/emulated/0/.EnglishXS/file/video/欲望都市/欲望都市1.mp4")) {
//                Log.i("TAG", "downloadxiazailiebiao = " + downloadManager.getDownloadList().get(i).getProgress());
//            }
//        }
        loadingDialog = new LoadingDialog(this);
        tv_counts = (TextView) findViewById(R.id.tv_counts);
        bottom_choice = (LinearLayout) findViewById(R.id.bottom_choice);
        bottom_choice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("TAG", "239r834034e0r9gu = " + tv_counts.getText());
                if (tv_counts.getText().toString().equals("") || tv_counts.getText().toString() == "") {
                    Toast toast = Toast.makeText(TvSelectBlues.this, "请选择要删除的影片", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {
                    if (Integer.parseInt(tv_counts.getText().toString()) == 0) {
                        Toast toast = Toast.makeText(TvSelectBlues.this, "请选择要删除的影片", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    } else {
                        final PromptDialog dialog = new PromptDialog(TvSelectBlues.this);
                        dialog.setContent(getString(R.string.tx_deleteinformation));
                        dialog.show();
                        dialog.setClickListener(new PromptDialog.onPromptClickListener() {
                            @Override
                            public void onDetermine(View v) {
                                adapter.SelectedDelete(application);
                                adapter.notifyDataSetChanged();
                                Toast toast = Toast.makeText(TvSelectBlues.this, "删除成功!", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                                tv_bianji.setText("编辑");
                                isSelect = false;
                                adapter.isSelect(isSelect);
                                bottom_choice.setVisibility(View.GONE);
                                dialog.dismiss();
                            }

                            @Override
                            public void onCancel(View v) {
                                dialog.dismiss();
                            }
                        });
                    }
                }
            }
        });
        isSelect = false;
        tv_bianji = (TextView) findViewById(R.id.tv_bianji);
        tv_bianji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tv_bianji.getText().equals("编辑") || tv_bianji.getText() == "编辑") {
                    tv_bianji.setText("取消");
                    isSelect = true;
                    adapter.isSelect(isSelect);
                    adapter.notifyDataSetChanged();
                    bottom_choice.setVisibility(View.VISIBLE);
                } else {
                    tv_bianji.setText("编辑");
                    isSelect = false;
                    adapter.isSelect(isSelect);
                    adapter.notifyDataSetChanged();
                    bottom_choice.setVisibility(View.GONE);
                }
            }
        });
        gridView = (GridView) findViewById(R.id.tvGradView);
        tv_backs = (LinearLayout) findViewById(R.id.tv_backs);

        tv_backs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyMoreActivity m = new MyMoreActivity();
                m.setMovieSize(fileList.size(), alLmovieDetailsBean.getMovieName());
                finish();
            }
        });
        Tv_titleName = (TextView) findViewById(R.id.Tv_titleName);

        Tv_titleName.setText(alLmovieDetailsBean.getMovieName());
        sdCard = "/storage/emulated/0/.EnglishXS/file/video/" + Tv_titleName.getText() + "/";
        final File f = new File("/storage/emulated/0/.EnglishXS/file/video/" + Tv_titleName.getText() + "/");

        Map<String, Object> map;
        if (f.isDirectory()) {
            File[] fileArray = f.listFiles();
            if (null != fileArray && 0 != fileArray.length) {
                for (int i = 0; i < fileArray.length; i++) {
                    int index = fileArray[i].getName().indexOf(".");
                    map = new HashMap<String, Object>();
                    map.put("filename", fileArray[i].getName().substring(0, index));
                    fileList.add(map);
                }
            }
        }
        for (int i = 0; i < fileList.size() - 1; i++) {
            for (int j = fileList.size() - 1; j > i; j--) {
                if (fileList.get(j).get("filename").equals(fileList.get(i).get("filename"))) {
                    fileList.remove(j);
                }
            }
        }
        if (movies == null) {
            movies = Manager.SelectMovie();
        } else {
            movies.clear();
            movies.addAll(Manager.SelectMovie());
        }
        adapter = new TvSelectBluesAdapter(this, fileList, alLmovieDetailsBean, isSelect, movies);
        if (movies != null && movies.size() > 0 && adapter != null) {
            gridView.setAdapter(adapter);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    List<ALLmovieDetailsBean> lisy = new ArrayList<ALLmovieDetailsBean>();
                    lisy.add(alLmovieDetailsBean);
                    String str = "";
                    if (adapterView.getAdapter().getItem(position).toString().trim() != null && adapterView.getAdapter().getItem(position).toString().trim() != "") {
                        for (int i = 0; i < adapterView.getAdapter().getItem(position).toString().trim().length(); i++) {
                            if (adapterView.getAdapter().getItem(position).toString().trim().charAt(i) >= 48 && adapterView.getAdapter().getItem(position).toString().trim().charAt(i) <= 57) {
                                str += adapterView.getAdapter().getItem(position).toString().trim().charAt(i);
                            }
                        }
                    }
                    int pos = Integer.parseInt(str);
                    startActivity(new Intent(TvSelectBlues.this, WatchVideoActivity.class).putExtra("movieInformation", lisy.get(0)).putExtra("Categorystate", 3).putExtra("jishu", pos));
                }
            });
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            MyMoreActivity m = new MyMoreActivity();
            m.setMovieSize(fileList.size(), alLmovieDetailsBean.getMovieName());
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
