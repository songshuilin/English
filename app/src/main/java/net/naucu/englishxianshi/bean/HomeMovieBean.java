package net.naucu.englishxianshi.bean;

import java.util.List;

/**
 * 类名: HomeMovieBean.java
 * 描述: TODO 电影类别实体类
 * 作者: youyou_pc
 * <p/>
 * 时间: 2015年11月17日  下午3:59:14
 */
public class HomeMovieBean {
    private String Moviecategory;
    private List<HomeMovieChildBean> movieChildBeans;

    public String getMoviecategory() {
        return Moviecategory;
    }

    public void setMoviecategory(String moviecategory) {
        Moviecategory = moviecategory;
    }

    public List<HomeMovieChildBean> getMovieChildBeans() {
        return movieChildBeans;
    }

    public void setMovieChildBeans(List<HomeMovieChildBean> movieChildBeans) {
        this.movieChildBeans = movieChildBeans;
    }

    @Override
    public String toString() {
        return "HomeMovieBean [Moviecategory=" + Moviecategory + ", movieChildBeans=" + movieChildBeans + "]";
    }

    public HomeMovieBean(String moviecategory, List<HomeMovieChildBean> movieChildBeans) {
        super();
        Moviecategory = moviecategory;
        this.movieChildBeans = movieChildBeans;
    }

    public HomeMovieBean() {
        super();
    }

}
