package net.naucu.englishxianshi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.naucu.englishxianshi.R;
import net.naucu.englishxianshi.bean.HelpVideoBean;

import java.util.List;

/**
 * Created by Y on 2017/1/4.
 */

public class MyHelpVideoAdapter extends BaseAdapter {
    private List<HelpVideoBean> list;
    private LayoutInflater inflater;
    private HelpVideoBean helpVideoBean;

    public MyHelpVideoAdapter(Context context, List<HelpVideoBean> list) {
        inflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public int getCount() {
        return list != null ? list.size() : 0;
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
        MyHolder holder;
        if (convertView == null) {
            holder = new MyHolder();
            convertView = inflater.inflate(R.layout.item_help_video, null);
            holder.textView = (TextView) convertView.findViewById(R.id.help_video_title);
            convertView.setTag(holder);
        } else {
            holder = (MyHolder) convertView.getTag();
        }
        helpVideoBean = list.get(position);
        holder.textView.setText(String.valueOf(position + 1) +"."+ helpVideoBean.getVideoTitle());

        return convertView;
    }

    private class MyHolder {

        TextView textView;
    }
}