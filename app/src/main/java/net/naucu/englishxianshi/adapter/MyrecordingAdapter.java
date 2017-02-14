package net.naucu.englishxianshi.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.naucu.englishxianshi.BaseApplication;
import net.naucu.englishxianshi.R;
import net.naucu.englishxianshi.bean.MyRecordBean;
import net.naucu.englishxianshi.ui.video.PlaySubtitleActivity;
import net.naucu.englishxianshi.util.NetTool;

import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyrecordingAdapter extends BaseAdapter {
    private Context context;
    private List<MyRecordBean> recordlist;
    private boolean showdeltet;
    private Map<Integer, Boolean> selects;
    private Handler handler;
    private boolean payed;

    public MyrecordingAdapter(Context context, List<MyRecordBean> recordlist, Handler handler, boolean payed) {
        this.context = context;
        this.recordlist = recordlist;
        this.handler = handler;
        this.payed = payed;
        selects = new HashMap<>();
        int n = recordlist.size();
        while (--n >= 0) {
            selects.put(n, false);
        }
    }

    public void showDelete(boolean showdeltet) {
        this.showdeltet = showdeltet;
    }

    public List<Integer> getSelects() {
        List<Integer> select = new ArrayList<Integer>();
        if (selects != null) {
            for (int i = selects.size(); --i >= 0; ) {
                if (selects.get(i)) {
                    select.add(i);
                }
            }
        }
        return select;
    }

    @Override
    public int getCount() {
        return recordlist == null ? 0 : recordlist.size();
    }

    @Override
    public Object getItem(int position) {
        return recordlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_myrecording, null);
            viewHolder = new ViewHolder();
            viewHolder.videoPicture = (ImageView) convertView.findViewById(R.id.iv_videoPicture_myrecord);
            viewHolder.videoName = (TextView) convertView.findViewById(R.id.tv_videoName_myrecord);
            viewHolder.recordScore = (TextView) convertView.findViewById(R.id.tv_score_myrecord);
            viewHolder.play = (TextView) convertView.findViewById(R.id.tv_play);
            viewHolder.delete = (CheckBox) convertView.findViewById(R.id.cb_delete);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        x.image().bind(viewHolder.videoPicture, recordlist.get(position).getVideoPictureUrl(),
                BaseApplication.getImageOption());
        viewHolder.videoName.setText(recordlist.get(position).getVideoName());
        viewHolder.recordScore.setText(recordlist.get(position).getScore() + "");
        if (showdeltet) {
            viewHolder.play.setVisibility(View.INVISIBLE);
            viewHolder.delete.setVisibility(View.VISIBLE);
            viewHolder.delete.setChecked(selects.get(position));
        } else {
            viewHolder.play.setVisibility(View.VISIBLE);
            viewHolder.delete.setVisibility(View.INVISIBLE);
        }
        viewHolder.play.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("TAG", "DANGQIANLIST = " + recordlist);
                if (payed) {
                    if (!NetTool.isNetworkConnected(context)) {
                        if (new File(recordlist.get(position).getVideoPath()).exists()) {
                            Intent intent = new Intent(context, PlaySubtitleActivity.class);
                            intent.putExtra(PlaySubtitleActivity.VIDEO_INFO, recordlist.get(position));
                            intent.putExtra("dftyxdf",1);
                            context.startActivity(intent);
                        } else {
                            Toast.makeText(context, "视频解析异常,请连接网络重新下载视频", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Intent intent = new Intent(context, PlaySubtitleActivity.class);

                        intent.putExtra(PlaySubtitleActivity.VIDEO_INFO, recordlist.get(position));
                        intent.putExtra("dftyxdf",1);
                        context.startActivity(intent);
                    }
                } else {
                    Toast.makeText(context, context.getResources().getText(R.string.paynotify), Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
        viewHolder.delete.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                selects.put(position, isChecked);
                handler.sendEmptyMessage(0);
            }
        });

        return convertView;
    }

    class ViewHolder {
        ImageView videoPicture;
        TextView videoName;
        TextView recordScore;
        TextView play;
        CheckBox delete;

    }
}
