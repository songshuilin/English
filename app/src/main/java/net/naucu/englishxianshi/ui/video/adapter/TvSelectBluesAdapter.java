package net.naucu.englishxianshi.ui.video.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.lijunsai.httpInterface.bean.ALLmovieDetailsBean;

import net.naucu.englishxianshi.BaseApplication;
import net.naucu.englishxianshi.R;
import net.naucu.englishxianshi.db.dao.Movie;
import net.naucu.englishxianshi.download.DownloadInfo;
import net.naucu.englishxianshi.download.DownloadManager;
import net.naucu.englishxianshi.ui.video.TvSelectBlues;

import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Created by Y on 2016/10/29.
 */
public class TvSelectBluesAdapter extends BaseAdapter {
    public String sdCard;
    public List<Movie> movies;
    public int count;
    public Boolean isSelect;
    public Context context;
    public List<Map<String, Object>> list;
    public ALLmovieDetailsBean alLmovieDetailsBean;
    private List<DownloadInfo> downloadInfoList;
    private DownloadManager downloadManager;
    public String[] needDelete;

    public TvSelectBluesAdapter(Context context, List<Map<String, Object>> list, ALLmovieDetailsBean alLmovieDetailsBean, boolean isSelect, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
        this.isSelect = isSelect;
        this.list = list;
        this.alLmovieDetailsBean = alLmovieDetailsBean;
        needDelete = new String[list.size()];
        downloadManager = DownloadManager.getInstance();
        downloadInfoList = downloadManager.getDownloadList();

    }

    public void isSelect(boolean isSelect) {
        this.isSelect = isSelect;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            sdCard = "/storage/emulated/0/.EnglishXS/file/video/" + alLmovieDetailsBean.getMovieName() + "/";
            convertView = LayoutInflater.from(context).inflate(R.layout.item_tv, null);
            viewHolder = new ViewHolder();
            viewHolder.cb_chooses = (CheckBox) convertView.findViewById(R.id.cb_chooses);
            viewHolder.jindu = (TextView) convertView.findViewById(R.id.jindu);
            viewHolder.pingfen = (TextView) convertView.findViewById(R.id.pingfen);
            viewHolder.tv_movie_text = (TextView) convertView.findViewById(R.id.tv_movie_text);
            viewHolder.iv_movieimg = (ImageView) convertView.findViewById(R.id.iv_movieimg);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (isSelect == true) {
            viewHolder.cb_chooses.setVisibility(View.VISIBLE);
        } else {
            viewHolder.cb_chooses.setVisibility(View.GONE);
        }
        viewHolder.cb_chooses.setChecked(false);
        viewHolder.cb_chooses.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (viewHolder.cb_chooses.isChecked()) {
                    needDelete[position] = list.get(position).get("filename").toString();
                    count += 1;
                    TvSelectBlues.getTv_counts().setText("" + count);
                } else {
                    needDelete[position] = null;
                    count -= 1;
                    TvSelectBlues.getTv_counts().setText("" + count);
                }
            }
        });
        for (int i = 0; i < downloadInfoList.size(); i++) {
            if (downloadInfoList.get(i).getFileSavePath().contains(sdCard + list.get(position).get("filename").toString() + ".mp4") || downloadInfoList.get(i).getFileSavePath() == sdCard + list.get(position).get("filename").toString() + ".mp4") {
                String a = downloadInfoList.get(i).getProgress() + "";
                int b = a.indexOf(".");
                String c = a.substring(0, b + 3);
                viewHolder.jindu.setText("已下载:" + c + "%");
            }
        }
        viewHolder.tv_movie_text.setText(list.get(position).get("filename").toString());
        viewHolder.pingfen.setText("评分:" + alLmovieDetailsBean.getMoviepingfen());
        x.image().bind(viewHolder.iv_movieimg, alLmovieDetailsBean.getMovieImageUrl(), BaseApplication.getImageOption());
        return convertView;
    }

    class ViewHolder {
        CheckBox cb_chooses;
        TextView jindu;
        TextView pingfen;
        TextView tv_movie_text;
        ImageView iv_movieimg;
    }

    public void SelectedDelete(BaseApplication application) {
        for (int i = 0; i < needDelete.length; i++) {
            if (needDelete[i] != null) {
                for (int j = 0; j < downloadInfoList.size(); j++) {
                    if (downloadInfoList.get(j).getFileSavePath().contains(needDelete[i])) {
                        try {
                            downloadManager.removeDownload(downloadInfoList.get(j), true);
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                    }
                }
                for (int j = 0; j < list.size(); j++) {
                    if (list.get(j).get("filename").equals(needDelete[i]) || list.get(j).get("filename") == needDelete[i]) {
                        list.remove(j);
                    }
                }
                String path = sdCard;
                final File f = new File(path);
                if (f.isDirectory()) {
                    File[] fileArray = f.listFiles();
                    if (null != fileArray && 0 != fileArray.length) {
                        for (int j = 0; j < fileArray.length; j++) {
                            if (fileArray[j].getName().contains(needDelete[i])) {
                                fileArray[j].delete();
                            }
                        }
                    }
                }
            }
        }
        this.notifyDataSetChanged();
    }
}
