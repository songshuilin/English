package net.naucu.englishxianshi.ui.video.bean;

public class ShowTime {

    // 开始显示时间
    private int begin;
    // 结束显示时间
    private int end;

    public int getBegin() {
        return begin;
    }

    public void setBegin(int begin) {
        this.begin = begin;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return "ShowTime [begin=" + begin + ", end=" + end + "]";
    }
}
