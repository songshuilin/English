package net.naucu.englishxianshi.ebook;

/**
 * @author fght
 *         creat at 15-11-26
 *         电子书业务bean
 *
 */
public class EBook {
    private String ebookBodyE;
    private String ebookBodyC;


    public String getEbookBodyE() {
        return ebookBodyE;
    }

    public void setEbookBodyE(String ebookBodyE) {
        this.ebookBodyE = ebookBodyE;
    }

    public String getEbookBodyC() {
        return ebookBodyC;
    }

    public void setEbookBodyC(String ebookBodyC) {
        this.ebookBodyC = ebookBodyC;
    }

    @Override
    public String toString() {
        return "EBook [ebookBodyE=" + ebookBodyE + ", ebookBodyC=" + ebookBodyC + "]";
    }

}
