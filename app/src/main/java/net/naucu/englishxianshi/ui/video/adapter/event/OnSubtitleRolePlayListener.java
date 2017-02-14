package net.naucu.englishxianshi.ui.video.adapter.event;

import net.naucu.englishxianshi.ui.video.bean.SubtitleBean;

import java.util.List;

public interface OnSubtitleRolePlayListener {

    /**
     * 角色扮演选择首句
     */
    void subtitleRolePlayBegin(List<SubtitleBean> beginSubtitleList);

    /**
     * 角色扮演尾句选择
     */
    void subtitleRolePlayEnd(List<SubtitleBean> endSubtitleList, int endPosition);
}
