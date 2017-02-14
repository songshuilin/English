package net.naucu.englishxianshi.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.lijunsai.httpInterface.bean.ALLbookBean;

import net.naucu.englishxianshi.R;
import net.naucu.englishxianshi.ui.BooksClassificationActivity;
import net.naucu.englishxianshi.ui.BooksIntroducedActivity;

import java.util.List;

/**
 * 类名: HomeBooksAdapter.java
 * 描述: TODO 首页电子书适配器
 * 作者: youyou_pc
 * 时间: 2015年11月17日  上午11:19:08
 */
public class HomeBooksListAdapter extends BaseAdapter {
    private Context context;

    private List<ALLbookBean> booksBeans;

    public HomeBooksListAdapter(Context context, List<ALLbookBean> booksBeans) {
        this.context = context;
        this.booksBeans = booksBeans;
    }

    @Override
    public int getCount() {
        return booksBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return booksBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_homebookslist, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_elevisionType = (TextView) convertView.findViewById(R.id.tv_elevisionType);
            viewHolder.gr_booksChild = (GridView) convertView.findViewById(R.id.gr_booksChild);
            viewHolder.iv_jump = (ImageView) convertView.findViewById(R.id.iv_jump);
            viewHolder.textview= (TextView) convertView.findViewById(R.id.textview);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.iv_jump.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, BooksClassificationActivity.class).putExtra("TwoCategory", booksBeans.get(position).getTwoCategory()));
            }
        });
        viewHolder.textview.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, BooksClassificationActivity.class).putExtra("TwoCategory", booksBeans.get(position).getTwoCategory()));
            }
        });
        viewHolder.tv_elevisionType.setEnabled(false);
        viewHolder.tv_elevisionType.setFocusable(false);
        viewHolder.tv_elevisionType.setText(booksBeans.get(position).getTwoCategory());
        if (booksBeans.get(position).getGetbookDetailsBeans() != null) {
            viewHolder.gr_booksChild.setAdapter(new HomeBooksGridAdapter(context, booksBeans.get(position).getGetbookDetailsBeans()));
            viewHolder.gr_booksChild.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int positions, long id) {
                    context.startActivity(new Intent(context, BooksIntroducedActivity.class).putExtra("booksInformation", booksBeans.get(position).getGetbookDetailsBeans().get(positions)));
                }
            });
        }
        return convertView;
    }

    class ViewHolder {
        TextView tv_elevisionType;
        GridView gr_booksChild;
        ImageView iv_jump;
        TextView textview;
    }
}
