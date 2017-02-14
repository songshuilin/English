package net.naucu.englishxianshi.iflytek.bean;

public class ISEIflytekBean {
    private String content;//内容
    private String end_pos;//总时间
    private String time_len;//总时间
    private String total_score;//评分
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getEnd_pos() {
        return end_pos;
    }

    public void setEnd_pos(String end_pos) {
        this.end_pos = end_pos;
    }

    public String getTime_len() {
        return time_len;
    }

    public void setTime_len(String time_len) {
        this.time_len = time_len;
    }

    public String getTotal_score() {
        return total_score;
    }

    public void setTotal_score(String total_score) {
        this.total_score = total_score;
    }

    @Override
    public String toString() {
        return "内容：" + content +
                "\n总时间：" + end_pos +
                "\n总时间:" + time_len +
                "\n评分:" + total_score;
    }

    public ISEIflytekBean() {
        super();
    }

    public ISEIflytekBean(String content, String end_pos, String time_len, String total_score) {
        super();
        this.content = content;
        this.end_pos = end_pos;
        this.time_len = time_len;
        this.total_score = total_score;
    }

}
