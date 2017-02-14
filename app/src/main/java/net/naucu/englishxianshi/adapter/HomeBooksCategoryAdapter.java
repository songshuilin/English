package net.naucu.englishxianshi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.naucu.englishxianshi.R;

import java.util.List;

/**
 * 类名: HomeBooksCategory.java
 * 描述: TODO 首页电子书分类
 * 作者: youyou_pc
 * 时间: 2015年11月18日  下午3:04:51
 */
public class HomeBooksCategoryAdapter extends BaseAdapter {
    private Context context;
    private List<String> list;

    public HomeBooksCategoryAdapter(Context context, List<String> list) {
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
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_textview, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_textcategory = (TextView) convertView.findViewById(R.id.tv_textcategory);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_textcategory.setText(list.get(position).toString());
        return convertView;
    }

    class ViewHolder {
        TextView tv_textcategory;
    }
}
