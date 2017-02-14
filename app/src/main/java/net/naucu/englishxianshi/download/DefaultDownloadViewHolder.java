package net.naucu.englishxianshi.download;

import android.view.View;

import com.jiongbull.jlog.JLog;

import org.xutils.common.Callback;

import java.io.File;

/**
 * Created by wyouflf on 15/11/11.
 */
public class DefaultDownloadViewHolder extends DownloadViewHolder {
    public static final String TAG = DefaultDownloadViewHolder.class.getSimpleName();

    public DefaultDownloadViewHolder(View view, DownloadInfo downloadInfo) {
        super(view, downloadInfo);
    }

    @Override
    public void onWaiting() {
        JLog.e("Download Waiting");
    }

    @Override
    public void onStarted() {
        JLog.e("Download Started");
    }

    @Override
    public void onLoading(long total, long current) {
        //  JLog.e("Download Loading total=" + total + ",current=" + current);
    }

    @Override
    public void onSuccess(File result) {
//        JLog.e("Download Success filePath=" + result.getPath());
    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        String msg = (ex == null || ex.getMessage() == null) ? "Throwable is null" : ex.getMessage();
        JLog.e("Download Error msg=" + msg);
    }

    @Override
    public void onCancelled(Callback.CancelledException cex) {
        String msg = (cex == null || cex.getMessage() == null) ? "Exception is null" : cex.getMessage();
        JLog.e("Download Cancelled msg=" + msg);
    }
}
