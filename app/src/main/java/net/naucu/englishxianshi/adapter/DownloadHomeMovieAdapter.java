package net.naucu.englishxianshi.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import net.naucu.englishxianshi.BaseApplication;
import net.naucu.englishxianshi.R;
import net.naucu.englishxianshi.db.Manager;
import net.naucu.englishxianshi.db.dao.Movie;
import net.naucu.englishxianshi.download.DownloadInfo;
import net.naucu.englishxianshi.download.DownloadManager;
import net.naucu.englishxianshi.util.DataCleanManager;

import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DownloadHomeMovieAdapter extends BaseAdapter {
    public static long l = 0l;
    private Context context;
    private List<Movie> movies;
    private boolean isSelect;
    private OnChecked checked;
    private DownloadManager downloadManager;
    private List<DownloadInfo> downloadInfoList;

    public DownloadHomeMovieAdapter(Context context, List<Movie> movies, boolean isSelect) {
        this.context = context;
        this.movies = movies;
        this.isSelect = isSelect;
        downloadManager = DownloadManager.getInstance();
        downloadInfoList = downloadManager.getDownloadList();
    }

    @Override
    public int getCount() {
        return movies.size();
    }

    @Override
    public Object getItem(int position) {
        return movies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setSelect(boolean isSelect) {
        this.isSelect = isSelect;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Log.i("TAG", "132416746844 = " + movies);
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_downloadmovie, null);
            viewHolder = new ViewHolder();
            viewHolder.iv_movieimg = (ImageView) convertView.findViewById(R.id.iv_movieimg);
            viewHolder.tv_movie_text = (TextView) convertView.findViewById(R.id.tv_movie_text);
            viewHolder.jindu = (TextView) convertView.findViewById(R.id.jindu);
            viewHolder.cb_chooses = (CheckBox) convertView.findViewById(R.id.cb_chooses);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        if (isSelect) {
            viewHolder.cb_chooses.setVisibility(View.VISIBLE);
        } else {
            viewHolder.cb_chooses.setVisibility(View.GONE);
        }
        x.image().bind(viewHolder.iv_movieimg, movies.get(position).getFilmCovers(), BaseApplication.getImageOption());
        viewHolder.tv_movie_text.setText(movies.get(position).getFilmName());
        final File f = new File("/storage/emulated/0/.EnglishXS/file/video/");

        final List<Map<String, Object>> fileList = new ArrayList<>();
        Map<String, Object> map;
        if (f.isDirectory()) {
            File[] fileArray = f.listFiles();
            if (null != fileArray && 0 != fileArray.length) {
                for (int i = 0; i < fileArray.length; i++) {
                    map = new HashMap<>();
                    map.put("filename", fileArray[i].getName());
                    fileList.add(map);
                }
            }
        }
        if (fileList.size() > 0) {
            for (int i = 0; i < fileList.size(); i++) {
                Log.i("TAG", "XIAZAIJINDUTIAOZHENG 01 = " + fileList.size());
                if (!fileList.get(i).get("filename").equals(movies.get(position).getFilmName() + ".mp4")) {
                    viewHolder.jindu.setText("已完成");
                }
            }
        }
        for (int i = 0; i < downloadInfoList.size(); i++) {
            if (downloadInfoList.get(i).getLabel().equals(movies.get(position).getFilmName() + ".mp4")) {
                if (downloadInfoList.get(i).getProgress() != 0 && downloadInfoList.get(i).getProgress() != 100) {
                    DecimalFormat df = new DecimalFormat("######0.00");
                    viewHolder.jindu.setText("已下载" + df
                            .format(downloadInfoList.get(i).getProgress()) + "%");


                } else if (downloadInfoList.get(i).getProgress() == 100) {
                    viewHolder.jindu.setText("已完成");
                } else {
                    viewHolder.jindu.setText("已下载0%");
                }
            }
        }

        if (movies.get(position).isTV()) {
            viewHolder.jindu.setText("美剧");
        }
        viewHolder.cb_chooses.setChecked(false);
        viewHolder.cb_chooses.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (position < movies.size()) {
                    movies.get(position).setLogos(isChecked);
                    int count = 0;
                    for (int i = 0; i < movies.size(); i++) {
                        if (movies.get(i).isLogos()) {
                            count++;
                        }
                    }
                    checked.Count(count);
                }
            }
        });
        checked.Count(0);
        return convertView;
    }

    class ViewHolder {
        ImageView iv_movieimg;
        TextView tv_movie_text;
        CheckBox cb_chooses;
        TextView jindu;
    }

    public interface OnChecked {
        void Count(int i);
    }

    public void setOnChecked(OnChecked checked) {
        if (movies.size() == 0) {
            checked.Count(0);
        }
        this.checked = checked;
    }

    public void SelectedDelete(BaseApplication application) {
        for (int i = 0; i < movies.size(); i++) {
            if (movies.get(i).isLogos() == true) {
                DataCleanManager dataCleanManager = new DataCleanManager();
                final File f = new File("/storage/emulated/0/.EnglishXS/file/video/");
                if (f.isDirectory()) {
                    File[] fileArray = f.listFiles();
                    if (null != fileArray && 0 != fileArray.length) {
                        for (int j = 0; j < fileArray.length; j++) {
                            if (fileArray[j].isDirectory()) {
                                if (fileArray[j].getName().contains(movies.get(i).getFilmName())) {
                                    fileArray[j].delete();
                                }
                            } else if (fileArray[j].getName().contains(movies.get(i).getFilmName())) {
                                fileArray[j].delete();
                            }
                        }
                    }
                }
                for (int j = 0; j < downloadInfoList.size(); j++) {
                    if (downloadInfoList.get(j).getLabel().contains(movies.get(i).getFilmName())) {
                        try {
                            downloadManager.removeDownload(downloadInfoList.get(j), true);
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                    }
                }
                dataCleanManager.cleanInternalCache(application);
                Manager.DeleteMyMovie(movies.get(i).getDownloadId());
            }
        }
    }
}
