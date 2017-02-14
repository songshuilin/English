package net.naucu.englishxianshi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.lijunsai.httpInterface.bean.ALLbookDetailsBean;

import net.naucu.englishxianshi.BaseApplication;
import net.naucu.englishxianshi.R;

import org.xutils.x;

import java.util.List;

/**
 * 类名: BooksClassificationAdapter.java
 * 描述: TODO 电子书分类适配器
 * 作者: youyou_pc
 * 时间: 2015年11月24日  下午5:08:13
 */
public class BooksClassificationAdapter extends BaseAdapter {
    private Context context;
    private List<ALLbookDetailsBean> list;
    private int state;


    public BooksClassificationAdapter(Context context, List<ALLbookDetailsBean> list, int state) {
        this.context = context;
        this.list = list;
        this.state = state;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_booksclassification, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_booksname = (TextView) convertView.findViewById(R.id.tv_booksname);
            viewHolder.tv_author = (TextView) convertView.findViewById(R.id.tv_author);
            viewHolder.tv_introduction = (TextView) convertView.findViewById(R.id.tv_introduction);
            viewHolder.rb_score = (RatingBar) convertView.findViewById(R.id.rb_score);
            viewHolder.tv_score = (TextView) convertView.findViewById(R.id.tv_score);
            viewHolder.Im_CoverImageUrl = (ImageView) convertView.findViewById(R.id.Im_CoverImageUrl);
            convertView.setTag(viewHolder);
            if (state == 2) {
                convertView.findViewById(R.id.iv_coverview).setVisibility(View.GONE);
            } else {
                convertView.findViewById(R.id.iv_coverview).setVisibility(View.VISIBLE);
                ;
            }
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (state == 1) {
            viewHolder.tv_booksname.setText("书名：" + list.get(position).getBookName());
            viewHolder.tv_author.setText("作者：" + list.get(position).getBookAuthor());
        } else {
            viewHolder.tv_booksname.setText("片名：" + list.get(position).getBookName());
            viewHolder.tv_author.setText("导演：" + list.get(position).getBookAuthor());
        }

        viewHolder.tv_introduction.setText("简介：" + list.get(position).getBookAbout());
        viewHolder.rb_score.setRating((float) list.get(position).getBookpingfen());
        viewHolder.tv_score.setText(list.get(position).getBookpingfen() + "");
        x.image().bind(viewHolder.Im_CoverImageUrl, list.get(position).getBookImage(), BaseApplication.getImageOption());
        return convertView;
    }

    class ViewHolder {
        TextView tv_booksname;
        TextView tv_author;
        TextView tv_introduction;
        RatingBar rb_score;
        TextView tv_score;
        ImageView Im_CoverImageUrl;

    }

}
