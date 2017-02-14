package net.naucu.englishxianshi.bean;

public class Node {
    public Node parent;

    public String text;
    public String text1;
    public boolean isEX = false;
    public boolean isHasChild = false;
    public boolean isSelect = false;

    public Node(Node parent, String text, String text1) {
        this.parent = parent;
        this.text = text;
        this.text1 = text1;
    }


    @Override
    public String toString() {
        return "Node [parent=" + parent + ", text=" + text + ", text1=" + text1 + ", isEX=" + isEX + ", isHasChild="
                + isHasChild + ", isSelect=" + isSelect + "]";
    }


}
