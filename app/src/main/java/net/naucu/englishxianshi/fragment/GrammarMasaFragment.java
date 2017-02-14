package net.naucu.englishxianshi.fragment;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jiongbull.jlog.JLog;

import net.naucu.englishxianshi.R;
import net.naucu.englishxianshi.ui.MasaGrammarActivity;

public class GrammarMasaFragment extends Fragment {
    public static final String TAG = GrammarMasaFragment.class.getSimpleName();
    private View view;
    private ExpandableListView exlist;
    private MyExpandableListAdapter myad;
    private GrammarMasaxqFragment yf;
    private FragmentManager fm;
    private FragmentTransaction fmt;
    public MasaGrammarActivity.BianSeCallBack bianSeCallBack;
    private String[] zhang = new String[]{"前言", "第1章 英语词法", "第2章 语言的6大句子成分", "第3章 英语6大句子成分的排列位置", "第4章 英语句子的构成公式"};
    private String[][] zhangjie = new String[][]{{"Masa将英语语法公式化"},
            {"第一节 单词的6种词性", "第二节 英语名词", "第三节 英语动词", "第四节 英语形容词", "第五节 英语副词", "第六节 英语介词", "第七节 英语连词", "第八节 英语的词尾",
                    "第九节 总结"},
            {"前言", "第一节 全球语言通用的6大句子成分", "第二节 句子的简化结构", "第三节 用汉语举例说明6大句子成分", "第四节 什么词可以担当6大句子成分", "总结"},
            {"前言", "第一节 英语主、谓、宾的排列及英语的基本句型", "第二节 英语定、状、补的安放及英语句子的修饰", "第三节 打破常规的排列-倒装", "总结"},
            {"前言", "第一节 英语句子的结构一共有三种", "第二节 构成句子基础结构与装饰成分的组件"}};

    private WebView webview;
    private boolean isWeb = false;
    private int selectGroupPosition;
    private int selectChildPosition;

    public GrammarMasaFragment() {
    }

    public GrammarMasaFragment(MasaGrammarActivity.BianSeCallBack bianSeCallBack) {
        this.bianSeCallBack = bianSeCallBack;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_yufamasa, container, false);
        exlist = (ExpandableListView) view.findViewById(R.id.listmasa);
        exlist.setGroupIndicator(null);
        exlist.setOverScrollMode(View.OVER_SCROLL_NEVER);
        myad = new MyExpandableListAdapter();
        exlist.setAdapter(myad);
        exlist.setOnGroupExpandListener(new OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                for (int i = 0; i < exlist.getExpandableListAdapter().getGroupCount(); i++) {
                    if (groupPosition != i) {
                        exlist.collapseGroup(i);
                    }
                }
            }
        });
        return view;
    }

    public class MyExpandableListAdapter implements ExpandableListAdapter {

        @Override
        public void registerDataSetObserver(DataSetObserver observer) {

        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver observer) {

        }

        @Override
        public int getGroupCount() {

            return zhang.length;
        }

        @Override
        public int getChildrenCount(int groupPosition) {

            return zhangjie[groupPosition].length;
        }

        @Override
        public Object getGroup(int groupPosition) {

            return null;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {

            return null;
        }

        @Override
        public long getGroupId(int groupPosition) {

            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {

            return childPosition;
        }

        @Override
        public boolean hasStableIds() {

            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            GroupViewHolder gvh;
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.item_group, parent, false);
                gvh = new GroupViewHolder();
                gvh.gtv = (TextView) convertView.findViewById(R.id.group);
                gvh.giv = (ImageView) convertView.findViewById(R.id.im_ex);
                convertView.setTag(gvh);
            } else {
                gvh = (GroupViewHolder) convertView.getTag();
            }
            gvh.gtv.setText(zhang[groupPosition]);
            if (isExpanded) {
                gvh.giv.setImageResource(R.drawable.open1);
            } else {
                gvh.giv.setImageResource(R.drawable.close);
            }
            return convertView;
        }

        @Override
        public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild,
                                 View convertView, ViewGroup parent) {
            final ChildViewHolder cvh;
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.item_children, parent, false);
                cvh = new ChildViewHolder();
                cvh.ctv = (TextView) convertView.findViewById(R.id.children);
                convertView.setTag(cvh);
            } else {
                cvh = (ChildViewHolder) convertView.getTag();
            }
            cvh.ctv.setTextColor(0xffffffff);
            cvh.ctv.setText(zhangjie[groupPosition][childPosition]);
            if (selectGroupPosition == groupPosition && selectChildPosition == childPosition) {
                cvh.ctv.setTextColor(0xff1BE2E9);
            }
            cvh.ctv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (bianSeCallBack != null) {
                        bianSeCallBack.callback(GrammarMasaFragment.class.getSimpleName());
                    }
                    isWeb = true;
                    showWeb("masa" + groupPosition + childPosition);
                    webview.setVisibility(View.VISIBLE);
                    selectGroupPosition = groupPosition;
                    selectChildPosition = childPosition;
//                    } else {
//                        yf = GrammarMasaxqFragment.getInstance("masa" + groupPosition + childPosition);
//                        fm = getActivity().getSupportFragmentManager();
//                        // fm.popBackStack();
//                        while (fm.getBackStackEntryCount() > 0) {
//                            fm.popBackStackImmediate();
//                        }
//                        fmt = fm.beginTransaction();
//                        fmt.replace(R.id.yufact, yf);
//                        fmt.addToBackStack(null);
//                        fmt.commit();
//                    }
                }
            });
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {

            return true;
        }

        @Override
        public boolean areAllItemsEnabled() {

            return false;
        }

        @Override
        public boolean isEmpty() {

            return false;
        }

        @Override
        public void onGroupExpanded(int groupPosition) {

        }

        @Override
        public void onGroupCollapsed(int groupPosition) {

        }

        @Override
        public long getCombinedChildId(long groupId, long childId) {

            return 0;
        }

        @Override
        public long getCombinedGroupId(long groupId) {

            return 0;
        }

    }

    public class GroupViewHolder {
        TextView gtv;
        ImageView giv;
    }

    public class ChildViewHolder {
        TextView ctv;
    }

    private void showWeb(String key) {
        try {
            if (webview == null) {
                webview = (WebView) view.findViewById(R.id.web_masaxqyufa);
                webview.getSettings().setJavaScriptEnabled(true);
                webview.setBackgroundColor(0);
                webview.setOverScrollMode(View.OVER_SCROLL_NEVER);
                webview.getSettings().setDefaultFontSize(18);
            }
            webview.loadUrl("file:///android_asset/" + key + ".html");
        } catch (Exception e) {
            JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());
            Toast.makeText(getActivity(), "文件为找到", Toast.LENGTH_LONG).show();
        }
    }

    public void setBack() {
        if (webview == null) {
            webview = (WebView) view.findViewById(R.id.web_masaxqyufa);
        }
        webview.setVisibility(View.GONE);
    }

    public boolean isWeb() {
        return isWeb;
    }
}
