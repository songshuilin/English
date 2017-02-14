package net.naucu.englishxianshi.ui.video.bean;

import com.jiongbull.jlog.JLog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SubtitleParseUtil {
    public static final String TAG = "SUBTITLE_PARSE_UTIL";
    private final String ZH_REG = "([\u4e00-\u9fa5]+)";// 中文正则
    // 字幕文件地址
    private String subtitleUrl;
    // File
    private File fileSrt;
    private InputStream inputStreamSrt;
    // 字幕列表
    private List<SubtitleBean> subtitleBeanList;
    private OnSubtitleLoadCompleteListener onSubtitleLoadCompleteListener;

    /**
     * 初始化加载字幕
     *
     * @param srtUrl
     */
    public void initLoadSubtitle(String srtUrl) {
        this.subtitleUrl = srtUrl;

        fileSrt = new File(subtitleUrl);
        if (!fileSrt.exists())
            return;
        try {
            inputStreamSrt = new FileInputStream(fileSrt);
        } catch (FileNotFoundException e) {
            JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());
        }
    }

    /**
     * 开始解析
     */
    public void startSubtitleParse() {
        if (inputStreamSrt == null)
            return;
        try {
            InputStreamReader isr = new InputStreamReader(inputStreamSrt, "GBK");
            BufferedReader br = new BufferedReader(isr);

            String line = "";
            int item = 1;
            int rowCount = 0;
            boolean isLoad = false;
            boolean loadStart = false;
            ShowTime showTime = null;
            SubtitleBean subtitleBean = null;
            while ((line = br.readLine()) != null) {
                if (line.trim().length() > 0 && line.trim().equals(String.valueOf(item))) {
                    isLoad = true;
                    loadStart = true;
                    showTime = new ShowTime();
                    subtitleBean = new SubtitleBean();
                    subtitleBean.setItem(item);
                } else {
                    if (line.trim().length() <= 0 || line.trim() == null) {
                        loadStart = false;
                    }
                }

                if (loadStart) {
                    switch (rowCount) {
                        case 1:
                            String startTime = line.substring(0, 12);
                            showTime.setBegin(praseTime(startTime));
                            String stopTime = line.substring(line.length() - 12);
                            showTime.setEnd(praseTime(stopTime));
                            break;
                        case 2:
                            Pattern pattern = Pattern.compile(ZH_REG);
                            Matcher matcher = pattern.matcher(line);
                            if (matcher.find()) {
                                subtitleBean.setChinese(line);
                                subtitleBean.setEnglish("");
                            } else {
                                subtitleBean.setEnglish(line);
                            }
                            break;
                        case 3:
                            subtitleBean.setChinese(line);
                            break;
                    }
                }

                if (!loadStart && isLoad) {
                    item++;
                    rowCount = 0;
                    if (subtitleBean != null) {
                        if (subtitleBeanList == null) {
                            subtitleBeanList = new ArrayList<>();
                        }
                        subtitleBean.setShowTime(showTime);
                        subtitleBean.setShowStatus(SubtitleBean.ShowStatus.NOT);
                        subtitleBeanList.add(subtitleBean);
                    }
                } else {
                    if (isLoad && loadStart) {
                        rowCount++;
                    }
                }
            }
            br.close();
            isr.close();
        } catch (UnsupportedEncodingException e) {
            JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());
        } catch (IOException e) {
            JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());
        } finally {
            try {
                inputStreamSrt.close();
            } catch (IOException e) {
                JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());
            }
        }
        if (subtitleBeanList != null) {
            onSubtitleLoadCompleteListener.loadComplete(subtitleBeanList);
        }
    }

    synchronized int praseTime(String time) {
        String times[] = time.split(":");
        if (times != null && times.length == 3) {
            int hour = Integer.parseInt(times[0]);
            int point = Integer.parseInt(times[1]);
            String seconds = times[2];
            int sec = Integer.parseInt(seconds.substring(0, 2));
            int ms = Integer.parseInt(seconds.substring(3));

            return (hour * 3600 + point * 60 + sec) * 1000 + ms;
        }
        return 0;
    }

    public void setOnSubtitleLoadCompleteListener(OnSubtitleLoadCompleteListener onSubtitleLoadCompleteListener) {
        this.onSubtitleLoadCompleteListener = onSubtitleLoadCompleteListener;
    }

    /**
     * @author Yi
     */
    public interface OnSubtitleLoadCompleteListener {

        void loadComplete(List<SubtitleBean> subtitleBeanList);
    }
}
