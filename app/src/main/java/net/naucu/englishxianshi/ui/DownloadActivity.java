package net.naucu.englishxianshi.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jiongbull.jlog.JLog;

import net.naucu.englishxianshi.R;
import net.naucu.englishxianshi.download.DownloadInfo;
import net.naucu.englishxianshi.download.DownloadManager;
import net.naucu.englishxianshi.download.DownloadState;
import net.naucu.englishxianshi.download.DownloadViewHolder;
import net.naucu.englishxianshi.ui.base.BaseActivity;

import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;

@ContentView(R.layout.activity_download)
public class DownloadActivity extends BaseActivity {
    @ViewInject(R.id.lv_download)
    private ListView downloadList;

    private DownloadManager downloadManager;
    private DownloadListAdapter downloadListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        downloadManager = DownloadManager.getInstance();
        downloadListAdapter = new DownloadListAdapter();
        downloadList.setAdapter(downloadListAdapter);
    }

    private class DownloadListAdapter extends BaseAdapter {

        private Context mContext;
        private final LayoutInflater mInflater;

        private DownloadListAdapter() {
            mContext = getBaseContext();
            mInflater = LayoutInflater.from(mContext);
        }

        @Override
        public int getCount() {
            if (downloadManager == null) return 0;
            return downloadManager.getDownloadListCount();
        }

        @Override
        public Object getItem(int i) {
            return downloadManager.getDownloadInfo(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            DownloadItemViewHolder holder = null;
            DownloadInfo downloadInfo = downloadManager.getDownloadInfo(i);
            if (view == null) {
                view = mInflater.inflate(R.layout.download_item, null);
                holder = new DownloadItemViewHolder(view, downloadInfo);
                view.setTag(holder);
                holder.refresh();
            } else {
                holder = (DownloadItemViewHolder) view.getTag();
                holder.update(downloadInfo);
            }

            if (downloadInfo.getState().value() < DownloadState.FINISHED.value()) {
                try {
                    downloadManager.startDownload(
                            downloadInfo.getUrl(),
                            downloadInfo.getLabel(),
                            downloadInfo.getFileSavePath(),
                            downloadInfo.isAutoResume(),
                            downloadInfo.isAutoRename(),
                            holder);
                } catch (DbException e) {
                    JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());
                    Toast.makeText(x.app(), "添加下载失败", Toast.LENGTH_LONG).show();
                }
            }

            return view;
        }
    }

    public class DownloadItemViewHolder extends DownloadViewHolder {
        @ViewInject(R.id.download_label)
        TextView label;
        @ViewInject(R.id.download_state)
        TextView state;
        @ViewInject(R.id.download_fileSize)
        TextView fileSize;
        @ViewInject(R.id.download_pb)
        ProgressBar progressBar;
        @ViewInject(R.id.download_stop_btn)
        Button stopBtn;


        public DownloadItemViewHolder(View view, DownloadInfo downloadInfo) {
            super(view, downloadInfo);
            refresh();
        }

        @Event(R.id.download_stop_btn)
        private void toggleEvent(View view) {
            DownloadState state = downloadInfo.getState();
            switch (state) {
                case WAITING:
                case STARTED:
                    downloadManager.stopDownload(downloadInfo);
                    break;
                case ERROR:
                case STOPPED:
                    try {
                        downloadManager.startDownload(
                                downloadInfo.getUrl(),
                                downloadInfo.getLabel(),
                                downloadInfo.getFileSavePath(),
                                downloadInfo.isAutoResume(),
                                downloadInfo.isAutoRename(),
                                this);
                    } catch (DbException e) {
                        JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());
                        Toast.makeText(x.app(), "添加下载失败", Toast.LENGTH_LONG).show();
                    }
                    break;
                case FINISHED:
                    Toast.makeText(x.app(), "已经下载完成", Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        }

        @Event(R.id.download_remove_btn)
        private void removeEvent(View view) {
            try {
                downloadManager.removeDownload(downloadInfo, true);
                downloadListAdapter.notifyDataSetChanged();
            } catch (DbException e) {
                JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());
                Toast.makeText(x.app(), "移除任务失败", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void update(DownloadInfo downloadInfo) {
            super.update(downloadInfo);
            refresh();
        }

        @Override
        public void onWaiting() {
            refresh();
        }

        @Override
        public void onStarted() {
            refresh();
        }

        @Override
        public void onLoading(long total, long current) {
            refresh();
        }

        @Override
        public void onSuccess(File result) {
            refresh();
            fileSize.setText(Length2Space(result.length())
                    + "/" + Length2Space(result.length()));
        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {
            refresh();
        }

        @Override
        public void onCancelled(Callback.CancelledException cex) {
            refresh();
        }

        public void refresh() {
            label.setText(downloadInfo.getLabel());
            state.setText(downloadInfo.getState().toString());
            progressBar.setProgress((int)downloadInfo.getProgress());
            stopBtn.setVisibility(View.VISIBLE);
            stopBtn.setText(x.app().getString(R.string.stop));
            fileSize.setText(Length2Space(downloadInfo.getFileLength() * (int)downloadInfo.getProgress() / 100)
                    + "/" + Length2Space(downloadInfo.getFileLength()));
            DownloadState state = downloadInfo.getState();
            switch (state) {
                case WAITING:
                case STARTED:
                    stopBtn.setText(x.app().getString(R.string.stop));
                    break;
                case ERROR:
                case STOPPED:
                    stopBtn.setText(x.app().getString(R.string.start));
                    break;
                case FINISHED:
                    stopBtn.setVisibility(View.INVISIBLE);
                    break;
                default:
                    stopBtn.setText(x.app().getString(R.string.start));
                    break;
            }
        }
    }

    public static String Length2Space(long length) {
        if (length >> 10 == 0) {
            return length + "B";
        } else {
            if (length >> 20 == 0) {
                return (length >> 10) + "KB";
            } else {
                if (length >> 30 == 0) {
                    return String.format("%.2fMB", (length >> 10) / 1024f);
                } else {
                    return String.format("%.2fGB", (length >> 20) / 1024f);
                }
            }
        }
    }
}
