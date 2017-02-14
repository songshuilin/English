package net.naucu.englishxianshi.ui.video.adapter.event;

import net.naucu.englishxianshi.ui.video.bean.SubtitleBean;

import java.util.List;

public interface OnSubtitleSelectListener {

    /**
     * @param selectSubtitleList
     */
    void callbackSubtitleSelectList(List<SubtitleBean> selectSubtitleList);
}
