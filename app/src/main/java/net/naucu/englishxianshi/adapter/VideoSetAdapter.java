package net.naucu.englishxianshi.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lijunsai.httpInterface.bean.AllMoviePath;

import net.naucu.englishxianshi.R;
import net.naucu.englishxianshi.download.DownloadInfo;
import net.naucu.englishxianshi.download.DownloadManager;

import java.util.List;

/**
 * 类名: VideoSetAdapter.java
 * 描述: TODO 视频集数
 * <p/>
 * 作者: youyou_pc
 * 时间: 2015年12月31日  下午3:04:10
 */
public class VideoSetAdapter extends BaseAdapter {
    private List<AllMoviePath> listset;
    private Context context;
    private DownloadManager downloadManager;
    private List<DownloadInfo> downloadInfoList;
    public String titlename;
    public int pos;

    public VideoSetAdapter(Context context, List<AllMoviePath> listset, String titlename, int pos) {
        this.listset = listset;
        this.context = context;
        this.titlename = titlename;
        this.pos = pos;
        downloadManager = DownloadManager.getInstance();
        downloadInfoList = downloadManager.getDownloadList();
    }

    @Override
    public int getCount() {
        return listset == null ? 0 : listset.size();
    }

    @Override
    public Object getItem(int position) {

        return listset.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.layout_movieset, null);
        final LinearLayout linearLayoutAll = (LinearLayout) convertView.findViewById(R.id.ll_itemAll);
        final LinearLayout linearLayout = (LinearLayout) convertView.findViewById(R.id.ll_itembianse);
        linearLayoutAll.setBackgroundResource(R.drawable.sleect_button_box);
        if (bluesColor < 10000 && bluesColor == position) {
            linearLayoutAll.setBackgroundResource(R.drawable.sleect_button_box2);
        }
//        ViewTreeObserver vto = linearLayoutAll.getViewTreeObserver();
//        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                linearLayoutAll.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//                if (linearLayoutAll.getHeight() > 0) {
//                    for (int i = 0; i < downloadInfoList.size(); i++) {
//                        if (downloadInfoList.get(i).getFileSavePath().contains(titlename + listset.get(position).getNumber() + ".mp4")) {
//                            int Progress = downloadInfoList.get(i).getProgress();
//                            RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) linearLayout.getLayoutParams(); //取控件textView当前的布局参数
//                            double height = linearLayoutAll.getHeight();
//                            int HP = (int) ((height / 100) * Progress);
//                            linearParams.height = HP;// 控件的高强制设成20
//                            if ((position + 1) == pos) {
//                                linearLayout.setBackgroundResource(R.drawable.sleect_button_box3);
//                            }
//                            linearLayout.setLayoutParams(linearParams); //使设置好的布局参数应用到控件</pre>
//                        }
//                    }
//                }
//            }
//        });
        TextView textView = (TextView) convertView.findViewById(R.id.tv_videoset);
        textView.setText(listset.get(position).getNumber() + "");
        return convertView;
    }

    public int bluesColor = 10000;

    public void setBluesColor(int bluesColor) {
        this.bluesColor = bluesColor;
    }
}
