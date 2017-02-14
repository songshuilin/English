package net.naucu.englishxianshi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lijunsai.httpInterface.bean.ALLmovieDetailsBean;

import net.naucu.englishxianshi.BaseApplication;
import net.naucu.englishxianshi.R;

import org.xutils.x;

import java.util.List;

/**
 * 类名: MovieClassificationAdapter.java
 * 描述: TODO 电影分类适配器
 * 作者: youyou_pc
 * 时间: 2015年11月24日  下午4:13:30
 */
public class MovieClassificationAdapter extends BaseAdapter {
    private Context context;
    private List<ALLmovieDetailsBean> list;

    public MovieClassificationAdapter(Context context, List<ALLmovieDetailsBean> list) {
        this.context = context;
        this.list = list;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_movieclassification, null);
            viewHolder = new ViewHolder();
            viewHolder.lv_cover = (ImageView) convertView.findViewById(R.id.lv_cover);
            viewHolder.moviename = (TextView) convertView.findViewById(R.id.moviename);
            viewHolder.tv_score = (TextView) convertView.findViewById(R.id.tv_score);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.moviename.setText(list.get(position).getMovieName());
        x.image().bind(viewHolder.lv_cover, list.get(position).getMovieImageUrl(), BaseApplication.getImageOption());
        viewHolder.tv_score.setText(list.get(position).getMoviepingfen());
        return convertView;
    }

    class ViewHolder {
        TextView tv_score;
        ImageView lv_cover;
        TextView moviename;
    }

}
