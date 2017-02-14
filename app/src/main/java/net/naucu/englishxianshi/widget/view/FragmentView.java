package net.naucu.englishxianshi.widget.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * 类名: FragmentView.java
 * 描述: Fragment工具类
 * 作者: youyou_pc
 * 时间: 2015年11月19日  下午4:02:16
 */
public class FragmentView {
    public static void replaces(FragmentManager manager, Fragment center, int layoutid) {
        FragmentTransaction transaction = manager.beginTransaction();
        if (center.isAdded()) {
            transaction.show(center);
        } else {
            transaction.add(layoutid, center);
        }
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public static void replacess(FragmentManager manager, Fragment center, int layoutid) {
        FragmentTransaction transaction = manager.beginTransaction();
        if (center.isAdded()) {
            transaction.show(center);
        } else {
            transaction.replace(layoutid, center);
        }
        transaction.commit();
    }
}
