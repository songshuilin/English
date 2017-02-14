package net.naucu.englishxianshi.ui.video.adapter;

import net.naucu.englishxianshi.ui.video.bean.SubtitleBean;

import java.util.List;

/**
 * Created by Y on 2016/12/8.
 */
public interface VideoCallback {
    void callBackSelect(List<SubtitleBean> selectSubtitleList);
}
