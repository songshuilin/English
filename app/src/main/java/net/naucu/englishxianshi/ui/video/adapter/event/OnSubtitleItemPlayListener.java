package net.naucu.englishxianshi.ui.video.adapter.event;

import android.view.View;

public interface OnSubtitleItemPlayListener {

    /**
     * 选择字幕播放
     *
     * @param view
     * @param position
     */
    void itemClickPlay(View view, int mark, int position);

}
