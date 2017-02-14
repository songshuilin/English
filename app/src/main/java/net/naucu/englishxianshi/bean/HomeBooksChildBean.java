package net.naucu.englishxianshi.bean;

/**
 * 类名: HomeBooksBean.java
 * 描述: TODO 书详细内容
 * 作者: youyou_pc
 * <p/>
 * 时间: 2015年11月17日  上午11:34:12
 */
public class HomeBooksChildBean {
    private String CoverImageUrl;//封面图片
    private String BooksName;//电子书名字
    private String BooksAuthor;//电子书作者

    public String getCoverImageUrl() {
        return CoverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        CoverImageUrl = coverImageUrl;
    }

    public String getBooksName() {
        return BooksName;
    }

    public void setBooksName(String booksName) {
        BooksName = booksName;
    }

    public String getBooksAuthor() {
        return BooksAuthor;
    }

    public void setBooksAuthor(String booksAuthor) {
        BooksAuthor = booksAuthor;
    }

    @Override
    public String toString() {
        return "HomeBooksChildBean [CoverImageUrl=" + CoverImageUrl + ", BooksName=" + BooksName + ", BooksAuthor="
                + BooksAuthor + "]";
    }

    public HomeBooksChildBean(String coverImageUrl, String booksName, String booksAuthor) {
        super();
        CoverImageUrl = coverImageUrl;
        BooksName = booksName;
        BooksAuthor = booksAuthor;
    }

    public HomeBooksChildBean() {
        super();
        // TODO Auto-generated constructor stub
    }


}