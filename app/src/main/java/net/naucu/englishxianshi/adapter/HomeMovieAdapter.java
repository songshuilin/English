package net.naucu.englishxianshi.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lijunsai.httpInterface.bean.ALLmovieBean;

import net.naucu.englishxianshi.R;
import net.naucu.englishxianshi.ui.MovieClassificationActivity;
import net.naucu.englishxianshi.util.NetTool;

import java.util.List;

/**
 * 类名: HomeMovieAdapter.java
 * 描述: TODO 视频 列表类别
 * 作者: youyou_pc
 * 时间: 2015年11月17日  下午3:50:09
 */
public class HomeMovieAdapter extends BaseAdapter {
    private List<ALLmovieBean> homeMovieBeans;
    private Context context;
    private int i;

    public HomeMovieAdapter(Context context, List<ALLmovieBean> homeMovieBeans, int i) {
        this.context = context;
        this.homeMovieBeans = homeMovieBeans;
        this.i = i;
    }

    @Override
    public int getCount() {
        return homeMovieBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return homeMovieBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            //首页类型列表
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_homemovie, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_Moviecategory = (TextView) convertView.findViewById(R.id.tv_Moviecategory);
            viewHolder.movieChildlist = (ListView) convertView.findViewById(R.id.movieChildlist);
            viewHolder.iv_classification = (ImageView) convertView.findViewById(R.id.iv_classification);
            viewHolder.textview = (TextView) convertView.findViewById(R.id.textview);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.iv_classification.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (NetTool.isNetworkConnected(context)) {
                    context.startActivity(new Intent(context, MovieClassificationActivity.class).putExtra("movieclass", homeMovieBeans.get(position).getTwoCategory()).putExtra("Categorystate", i));
                } else {
                    Toast.makeText(context, "网络异常,请检查网络连接状态", Toast.LENGTH_SHORT).show();
                }
            }
        });
        viewHolder.textview.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NetTool.isNetworkConnected(context)) {
                    context.startActivity(new Intent(context, MovieClassificationActivity.class).putExtra("movieclass", homeMovieBeans.get(position).getTwoCategory()).putExtra("Categorystate", i));
                } else {
                    Toast.makeText(context, "网络异常,请检查网络连接状态", Toast.LENGTH_SHORT).show();
                }
            }
        });
        viewHolder.tv_Moviecategory.setText(homeMovieBeans.get(position).getTwoCategory());
        if (homeMovieBeans.get(position).getGetalLmovieDetailsBeans() != null) {
            //影片列表
            viewHolder.movieChildlist.setAdapter(new HomeMovieChildAdapter(context, homeMovieBeans.get(position).getGetalLmovieDetailsBeans(), i));
        }
        return convertView;
    }

    class ViewHolder {
        TextView tv_Moviecategory;
        ListView movieChildlist;
        ImageView iv_classification;
        TextView textview;
    }
}
