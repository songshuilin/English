package net.naucu.englishxianshi.bean;

import java.util.List;

public class MyRecordBean extends VideoBean {
    private List<String> recordPaths;
    private float score;
    private int startPosition;
    private int endPosition;
    private int beginTime;
    private int endTime;
    private List<Integer> selectList;
    private String introduction;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public int getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(int beginTime) {
        this.beginTime = beginTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public List<String> getRecordPaths() {
        return recordPaths;
    }

    public void setRecordPaths(List<String> recordPaths) {
        this.recordPaths = recordPaths;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public int getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(int startPosition) {
        this.startPosition = startPosition;
    }

    public int getEndPosition() {
        return endPosition;
    }

    public void setEndPosition(int endPosition) {
        this.endPosition = endPosition;
    }

    public List<Integer> getSelectList() {
        return selectList;
    }

    public void setSelectList(List<Integer> selectList) {
        this.selectList = selectList;
    }

    @Override
    public String toString() {
        return "MyRecordBean [recordPaths=" + recordPaths + ", score=" + score + ", startPosition=" + startPosition
                + ", endPosition=" + endPosition + ", beginTime=" + beginTime + ", endTime=" + endTime + ", selectList="
                + selectList + ", introduction=" + introduction + ", id=" + id + "]";
    }


}
