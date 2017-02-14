package net.naucu.englishxianshi.ui.video.bean;

public class SubtitleBean {

    private int item;
    // 显示时间
    private ShowTime showTime;
    // 显示状态
    private ShowStatus showStatus;
    // 中文字幕
    private String chinese;
    // 英文字幕
    private String english;
    // 选择状态
    private boolean isSelect = false;
    // 角色扮演是否选择
    private boolean isRolePlay = false;
    // 角色扮演录音标识
    private boolean isRecord = false;

    public enum ShowStatus {
        HAVE, CURRENT, NOT
    }

    public int getItem() {
        return item;
    }

    public void setItem(int item) {
        this.item = item;
    }

    public ShowTime getShowTime() {
        return showTime;
    }

    public void setShowTime(ShowTime showTime) {
        this.showTime = showTime;
    }

    public ShowStatus getShowStatus() {
        return showStatus;
    }

    public void setShowStatus(ShowStatus showStatus) {
        this.showStatus = showStatus;
    }

    public String getChinese() {
        return chinese;
    }

    public void setChinese(String chinese) {
        this.chinese = chinese;
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean isSelect) {
        this.isSelect = isSelect;
    }

    public boolean isRolePlay() {
        return isRolePlay;
    }

    public void setRolePlay(boolean isRolePlay) {
        this.isRolePlay = isRolePlay;
    }

    public boolean isRecord() {
        return isRecord;
    }

    public void setRecord(boolean isRecord) {
        this.isRecord = isRecord;
    }

    @Override
    public String toString() {
        return "SubtitleBean [item=" + item + ", showTime=" + showTime + ", showStatus=" + showStatus + ", chinese="
                + chinese + ", english=" + english + ", isSelect=" + isSelect + ", isRolePlay=" + isRolePlay
                + ", isRecord=" + isRecord + "]";
    }

}
