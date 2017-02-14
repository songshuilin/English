package com.lijunsai.httpInterface.bean;

public class BaiduTranslation {

    private String src;
    private String dst;

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getDst() {
        return dst;
    }

    public void setDst(String dst) {
        this.dst = dst;
    }

    public BaiduTranslation(String src, String dst) {
        super();
        this.src = src;
        this.dst = dst;
    }


}
