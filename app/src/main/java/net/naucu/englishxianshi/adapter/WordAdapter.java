package net.naucu.englishxianshi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.naucu.englishxianshi.R;

import java.util.List;
import java.util.Map;

/**
 * Created by Y on 2016/11/3.
 */
public class WordAdapter extends BaseAdapter {
    public List<Map<String, Object>> list;
    public Context context;
    private int select = -1;

    public WordAdapter(List<Map<String, Object>> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void setList(List<Map<String, Object>> list) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_dancibianxi, null);
            viewHolder = new ViewHolder();
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.txtName);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.txtName.setText(list.get(position).get("value").toString());
        viewHolder.txtName.setTextColor(0xffffffff);
        if (select == position) {
            viewHolder.txtName.setTextColor(0xff0000ff);
        }

        return convertView;
    }

    public void setSelect(int select) {
        this.select = select;
    }

    class ViewHolder {
        public TextView txtName;
    }
}
