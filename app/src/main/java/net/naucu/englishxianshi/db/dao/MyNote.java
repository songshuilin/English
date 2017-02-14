package net.naucu.englishxianshi.db.dao;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "note")
public class MyNote {
    @Column(name = "Id", isId = true)
    private int id;//检索id
    @Column(name = "CONTENT")
    private String content;//笔记内容
    @Column(name = "TYPE")
    private int type;//笔记类型（TranslateType）
    @Column(name = "CREATTIME")
    private long creatTime;//创建时间
    @Column(name = "UPDATETIME")
    private long updateTime;//更新时间


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(long creatTime) {
        this.creatTime = creatTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }


}
