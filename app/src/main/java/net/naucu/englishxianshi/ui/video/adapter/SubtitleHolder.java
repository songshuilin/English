package net.naucu.englishxianshi.ui.video.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import net.naucu.englishxianshi.R;
import net.naucu.englishxianshi.ui.video.PlayConfig;
import net.naucu.englishxianshi.ui.video.bean.SubtitleBean;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class SubtitleHolder {
    public static final String TAG = SubtitleHolder.class.getSimpleName();

    public View rootView;
    // 英文字幕显示View
    @ViewInject(R.id.video_subtitle_item_english_text)
    public TextView itemSubtitleEnglish;
    // 中文字幕显示
    @ViewInject(R.id.video_subtitle_item_chinese_text)
    public TextView itemSubtitleChinese;
    // 播放本句
    @ViewInject(R.id.video_subtitle_item_play_img)
    public ImageView itemPlay;
    // 字幕选择
    @ViewInject(R.id.video_subtitle_item_checkbox_01)
    public ImageView itemCheckBox01;
    // 字幕选择
    @ViewInject(R.id.video_subtitle_item_checkbox_02)
    public ImageView itemCheckBox02;

    public SubtitleHolder(View rootView) {
        this.rootView = rootView;
        x.view().inject(this, rootView);
    }

    private SubtitleItemAdapter adapter;

    public SubtitleHolder(View rootView, SubtitleItemAdapter adapter) {
        this.rootView = rootView;
        this.adapter = adapter;
        x.view().inject(this, rootView);
    }

    /**
     * 设置Holder显示样式
     *
     * @param subtitleBean
     * @param hoType
     */
    public void changeHolderShowStyle(SubtitleBean subtitleBean, PlayConfig.HoType hoType) {
        switch (hoType) {
            case PLAY:
                setPlayHolderStyle(subtitleBean);
                break;
            case ROLE_BEGIN:
                setRolpeBeginHolderStyle(subtitleBean);
                break;
            case ROLE_END:
                setRoleEndHolderStyle(subtitleBean);
                break;
            case ROLE_RECORD:
                setRoleRecordHolderStyle(subtitleBean);
                break;
            case SELECT_SUB:
                setSelectSubHolderStyle(subtitleBean);
                break;
            case SELECT_PLAY:
                setSelectPlayHolderStyle(subtitleBean);
                break;
        }
    }

    /**
     * 设置Holder显示样式为播放
     *
     * @param subtitleBean
     */
    private void setPlayHolderStyle(SubtitleBean subtitleBean) {
        // 设置View显示
        itemPlay.setVisibility(View.VISIBLE);
        itemCheckBox01.setVisibility(View.GONE);
        itemCheckBox02.setVisibility(View.GONE);
        // 设置View默认显示
        itemCheckBox01.setImageResource(R.drawable.srtparagraph_select_n);
        itemCheckBox02.setImageResource(R.drawable.srtbody_select_n);
        // 字幕颜色设置
        switch (subtitleBean.getShowStatus()) {
            case HAVE:
                itemSubtitleChinese.setTextColor(PlayConfig.PLAY_NOT_COLOR);
                itemSubtitleEnglish.setTextColor(PlayConfig.PLAY_NOT_COLOR);
                break;
            case CURRENT:
                itemSubtitleChinese.setTextColor(PlayConfig.PLAY_CURRENT_COLOR);
                itemSubtitleEnglish.setTextColor(PlayConfig.PLAY_CURRENT_COLOR);
                break;
            case NOT:
                itemSubtitleChinese.setTextColor(PlayConfig.PLAY_NOT_COLOR);
                itemSubtitleEnglish.setTextColor(PlayConfig.PLAY_NOT_COLOR);
                break;
        }
    }

    /**
     * 设置Holder显示样式为角色扮演选择首句
     *
     * @param subtitleBean
     */
    private void setRolpeBeginHolderStyle(SubtitleBean subtitleBean) {

//		int beginPosition = adapter.getbeginItem();
//		if (subtitleBean.getItem() == beginPosition){
//			itemCheckBox01.setImageResource(R.drawable.srtparagraph_select_n);
//		}
        // 设置View显示
        itemPlay.setVisibility(View.GONE);
        itemCheckBox01.setVisibility(View.VISIBLE);
        itemCheckBox02.setVisibility(View.GONE);
        // 设置View默认显示
        itemCheckBox01.setImageResource(R.drawable.srtparagraph_select_n);
        itemCheckBox02.setImageResource(R.drawable.srtbody_select_n);
        // 字幕颜色设置
        switch (subtitleBean.getShowStatus()) {
            case HAVE:
                itemSubtitleChinese.setTextColor(PlayConfig.PLAY_NOT_COLOR);
                itemSubtitleEnglish.setTextColor(PlayConfig.PLAY_NOT_COLOR);
                break;
            case CURRENT:
                itemSubtitleChinese.setTextColor(PlayConfig.PLAY_CURRENT_COLOR);
                itemSubtitleEnglish.setTextColor(PlayConfig.PLAY_CURRENT_COLOR);
                break;
            case NOT:
                itemSubtitleChinese.setTextColor(PlayConfig.PLAY_NOT_COLOR);
                itemSubtitleEnglish.setTextColor(PlayConfig.PLAY_NOT_COLOR);
                break;
        }
    }

    /**
     * 设置Holder显示样式为角色扮演选择尾句
     *
     * @param subtitleBean
     */
    private void setRoleEndHolderStyle(SubtitleBean subtitleBean) {
        // 设置View显示
        itemPlay.setVisibility(View.GONE);
        itemCheckBox01.setVisibility(View.VISIBLE);
        itemCheckBox02.setVisibility(View.VISIBLE);
        // 设置View默认显示
        itemCheckBox01.setImageResource(R.drawable.srtparagraph_select_n);
        itemCheckBox02.setImageResource(R.drawable.srtbody_select_n);
        // 字幕颜色设置
        switch (subtitleBean.getShowStatus()) {
            case HAVE:
                itemSubtitleChinese.setTextColor(PlayConfig.PLAY_NOT_COLOR);
                itemSubtitleEnglish.setTextColor(PlayConfig.PLAY_NOT_COLOR);
                break;
            case CURRENT:
                itemSubtitleChinese.setTextColor(PlayConfig.PLAY_CURRENT_COLOR);
                itemSubtitleEnglish.setTextColor(PlayConfig.PLAY_CURRENT_COLOR);
                break;
            case NOT:
                itemSubtitleChinese.setTextColor(PlayConfig.PLAY_NOT_COLOR);
                itemSubtitleEnglish.setTextColor(PlayConfig.PLAY_NOT_COLOR);
                break;
        }
    }

    /**
     * 设置Holder显示样式为角色扮演语句录音
     *
     * @param subtitleBean
     */
    private void setRoleRecordHolderStyle(SubtitleBean subtitleBean) {
        // 设置View显示
        itemPlay.setVisibility(View.GONE);
        itemCheckBox01.setVisibility(View.GONE);
        itemCheckBox02.setVisibility(View.GONE);
        // 设置View默认显示
        itemCheckBox01.setImageResource(R.drawable.srtparagraph_select_n);
        itemCheckBox02.setImageResource(R.drawable.srtbody_select_n);
        // 字幕颜色设置
        switch (subtitleBean.getShowStatus()) {
            case HAVE:
                itemSubtitleChinese.setTextColor(PlayConfig.ROLE_PLAY_NOT_COLOR);
                itemSubtitleEnglish.setTextColor(PlayConfig.ROLE_PLAY_NOT_COLOR);
                break;
            case CURRENT:
                itemSubtitleChinese.setTextColor(PlayConfig.ROLE_PLAY_CURRENT_COLOR);
                itemSubtitleEnglish.setTextColor(PlayConfig.ROLE_PLAY_CURRENT_COLOR);
                break;
            case NOT:
                if (subtitleBean.isRolePlay() && subtitleBean.isRecord()) {
                    itemSubtitleChinese.setTextColor(PlayConfig.SELECT_SUBTITLE_COLOR);
                    itemSubtitleEnglish.setTextColor(PlayConfig.SELECT_SUBTITLE_COLOR);
                } else {
                    itemSubtitleChinese.setTextColor(PlayConfig.ROLE_PLAY_NOT_COLOR);
                    itemSubtitleEnglish.setTextColor(PlayConfig.ROLE_PLAY_NOT_COLOR);
                }
                break;
        }
    }

    /**
     * 设置Holder显示样式为选择字幕
     *
     * @param subtitleBean
     */
    private void setSelectSubHolderStyle(SubtitleBean subtitleBean) {
        // 设置View显示
        itemPlay.setVisibility(View.GONE);
        itemCheckBox01.setVisibility(View.GONE);
        itemCheckBox02.setVisibility(View.VISIBLE);
        // 设置View默认显示
        itemCheckBox01.setImageResource(R.drawable.srtparagraph_select_n);
        itemCheckBox02.setImageResource(R.drawable.srtbody_select_n);
        // 字幕颜色设置
        switch (subtitleBean.getShowStatus()) {
            case HAVE:
                itemSubtitleChinese.setTextColor(PlayConfig.PLAY_NOT_COLOR);
                itemSubtitleEnglish.setTextColor(PlayConfig.PLAY_NOT_COLOR);
                break;
            case CURRENT:
                itemSubtitleChinese.setTextColor(PlayConfig.PLAY_CURRENT_COLOR);
                itemSubtitleEnglish.setTextColor(PlayConfig.PLAY_CURRENT_COLOR);
                break;
            case NOT:
                itemSubtitleChinese.setTextColor(PlayConfig.PLAY_NOT_COLOR);
                itemSubtitleEnglish.setTextColor(PlayConfig.PLAY_NOT_COLOR);
                break;
        }
    }

    /**
     * 设置Holder显示样式为选择字幕播放
     *
     * @param subtitleBean
     */
    private void setSelectPlayHolderStyle(SubtitleBean subtitleBean) {
        // 设置View显示
        itemPlay.setVisibility(View.GONE);
        itemCheckBox01.setVisibility(View.GONE);
        itemCheckBox02.setVisibility(View.GONE);
        // 设置View默认显示
        itemCheckBox01.setImageResource(R.drawable.srtparagraph_select_n);
        itemCheckBox02.setImageResource(R.drawable.srtbody_select_n);
        // 字幕颜色设置
        switch (subtitleBean.getShowStatus()) {
            case HAVE:
                itemSubtitleChinese.setTextColor(PlayConfig.PLAY_NOT_COLOR);
                itemSubtitleEnglish.setTextColor(PlayConfig.PLAY_NOT_COLOR);
                break;
            case CURRENT:
                itemSubtitleChinese.setTextColor(PlayConfig.PLAY_CURRENT_COLOR);
                itemSubtitleEnglish.setTextColor(PlayConfig.PLAY_CURRENT_COLOR);
                break;
            case NOT:
                if (subtitleBean.isSelect()) {
                    itemSubtitleChinese.setTextColor(PlayConfig.SELECT_SUBTITLE_COLOR);
                    itemSubtitleEnglish.setTextColor(PlayConfig.SELECT_SUBTITLE_COLOR);
                } else {
                    itemSubtitleChinese.setTextColor(PlayConfig.PLAY_NOT_COLOR);
                    itemSubtitleEnglish.setTextColor(PlayConfig.PLAY_NOT_COLOR);
                }
                break;
        }
    }
}
