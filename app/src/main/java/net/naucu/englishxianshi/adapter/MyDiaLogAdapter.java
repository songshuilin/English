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
 * Created by Administrator on 2017/1/24.
 */

public class MyDiaLogAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<String> list;

    public MyDiaLogAdapter(Context context, List<String> list) {
        inflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public int getCount() {
        return list != null ? list.size() : 0;
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.dialog_item, null);
            holder.textView = (TextView) view.findViewById(R.id.item_dialog_tv);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.textView.setText(list.get(i));

        return view;
    }

    class ViewHolder {
        TextView textView;
    }
}
