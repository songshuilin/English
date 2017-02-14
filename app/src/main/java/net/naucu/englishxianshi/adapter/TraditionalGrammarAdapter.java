package net.naucu.englishxianshi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.naucu.englishxianshi.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Y on 2016/11/2.
 */
public class TraditionalGrammarAdapter extends BaseAdapter {
    public Context context;
    public List<Map<String, Object>> list = new ArrayList<>();
    private int select = -1;

    public TraditionalGrammarAdapter(Context context, List<Map<String, Object>> list) {
        this.context = context;
        this.list = list;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_chuantongyufa, null);
            viewHolder = new ViewHolder();
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.txtName);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.txtName.setText(list.get(position).get("value").toString());
        viewHolder.txtName.setTextColor(0xffffffff);
        if (position == select){
            viewHolder.txtName.setTextColor(0xff1BE2E9);
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
