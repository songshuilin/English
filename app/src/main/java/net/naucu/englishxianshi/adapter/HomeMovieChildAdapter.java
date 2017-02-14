package net.naucu.englishxianshi.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.lijunsai.httpInterface.bean.ALLmovieDetailsBean;

import net.naucu.englishxianshi.BaseApplication;
import net.naucu.englishxianshi.R;
import net.naucu.englishxianshi.ui.WatchVideoActivity;
import net.naucu.englishxianshi.widget.view.CircleImageView;

import org.xutils.x;

import java.util.List;

/**
 * 类名: HomeMovieChildAdapter.java
 * 描述: TODO 视频 列表类别子页面
 * 作者: youyou_pc
 * 时间: 2015年11月17日  下午3:51:05
 */
public class HomeMovieChildAdapter extends BaseAdapter {
    private List<ALLmovieDetailsBean> movieChildBeans;
    private Context context;
    private int state;

    public HomeMovieChildAdapter(Context context, List<ALLmovieDetailsBean> movieChildBeans, int state) {
        this.context = context;
        this.movieChildBeans = movieChildBeans;
        this.state = state;
    }

    @Override
    public int getCount() {
        return movieChildBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return movieChildBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_homemoviechild, null);
            viewHolder = new ViewHolder();
            viewHolder.rb_gomovie = (RadioButton) convertView.findViewById(R.id.rb_gomovie);
            viewHolder.ci_cover = (CircleImageView) convertView.findViewById(R.id.ci_cover);
            viewHolder.rb_score = (TextView) convertView.findViewById(R.id.rb_score);
            viewHolder.tv_moviename = (TextView) convertView.findViewById(R.id.tv_moviename);
            viewHolder.tv_movieIntroduction = (TextView) convertView.findViewById(R.id.tv_movieIntroduction);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        x.image().bind(viewHolder.ci_cover, movieChildBeans.get(position).getMovieImageUrl(), BaseApplication.getImageOption());
        viewHolder.tv_moviename.setText(movieChildBeans.get(position).getMovieName());
        viewHolder.tv_movieIntroduction.setText(movieChildBeans.get(position).getMovieAbout());
        viewHolder.rb_gomovie.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context,
                        WatchVideoActivity.class)
                        .putExtra("movieInformation", movieChildBeans.get(position))
                        .putExtra("Categorystate", state));
                Log.i("TAGccc", "asd465sa4d6a5s4da65s4 =+ " + movieChildBeans.get(position)+"-----"+state);
            }
        });
        viewHolder.rb_score.setText(movieChildBeans.get(position).getMoviepingfen());





        return convertView;
    }

    class ViewHolder {

        TextView rb_score;
        RadioButton rb_gomovie;
        CircleImageView ci_cover;
        TextView tv_moviename;
        TextView tv_movieIntroduction;
    }

}
