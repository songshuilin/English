package net.naucu.englishxianshi.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lijunsai.httpInterface.bean.ALLbookDetailsBean;

import net.naucu.englishxianshi.R;
import net.naucu.englishxianshi.BaseApplication;

import org.xutils.x;

import java.util.List;

/**
 * 类名: HomeBooksGridAdapter.java
 * 描述: TODO 首页电子书子级
 * 作者: youyou_pc
 * 时间: 2015年11月17日  上午11:39:13
 */
public class HomeBooksGridAdapter extends BaseAdapter {
    private Context context;

    private List<ALLbookDetailsBean> homeBooksChildBeans;

    public HomeBooksGridAdapter(Context context, List<ALLbookDetailsBean> homeBooksChildBeans) {
        this.context = context;
        this.homeBooksChildBeans = homeBooksChildBeans;
    }

    @Override
    public int getCount() {
        return homeBooksChildBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return homeBooksChildBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_homebooksgrid, null);
            viewHolder = new ViewHolder();
            viewHolder.Im_CoverImageUrl = (ImageView) convertView.findViewById(R.id.Im_CoverImageUrl);
            viewHolder.tv_BooksName = (TextView) convertView.findViewById(R.id.tv_BooksName);
            viewHolder.tv_BooksIntroduction = (TextView) convertView.findViewById(R.id.tv_BooksIntroduction);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_BooksName.setText(homeBooksChildBeans.get(position).getBookName());
        viewHolder.tv_BooksIntroduction.setText(homeBooksChildBeans.get(position).getBookAuthor());
        x.image().bind(viewHolder.Im_CoverImageUrl, homeBooksChildBeans.get(position).getBookImage(), BaseApplication.getImageOption());
        return convertView;
    }

    class ViewHolder {
        ImageView Im_CoverImageUrl;
        TextView tv_BooksName;
        TextView tv_BooksIntroduction;
    }
}
