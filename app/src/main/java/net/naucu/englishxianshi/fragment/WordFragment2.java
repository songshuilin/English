package net.naucu.englishxianshi.fragment;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jiongbull.jlog.JLog;
import com.lijunsai.httpInterface.tool.CardUtils;

import net.naucu.englishxianshi.R;
import net.naucu.englishxianshi.adapter.WordAdapter;
import net.naucu.englishxianshi.util.StreamTools;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class WordFragment2 extends Fragment {
    public static final String TAG = WordFragment2.class.getSimpleName();

    private View view;
    private JSONObject jsonObject;
    private String jsonString = null;// {"msg":"200","result":{"ids":"632,1308"},"success":true}

    private Cursor cursor;
    private String DB_PATH = CardUtils.basePath + "/d.db";
    ExpandableListView exlist;
    ArrayList<String> list = new ArrayList<>();
    HashMap<String, ArrayList<String>> map = new HashMap<>();
    private MyexAdapter myexadapter = null;
    private FragmentManager fm;
    private FragmentTransaction fmt;
    private WordxqFragment wordxqFragment;
    private Context context;
    private SQLiteDatabase sqldata;
    Map<String, Object> allMap;
    List<Map<String, Object>> allList = new ArrayList<>();
    WordAdapter adapter;
    ListView wordList;
    public String key;

    public WordFragment2(Context context) {
        this.context = context;
    }


    public static WordFragment2 getInstance(String key, Context context) {
        WordFragment2 f = new WordFragment2(context);
        Bundle args = new Bundle();
        args.putString("key", key);
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_danci, container, false);
        if (getArguments() != null) {
            if (getArguments().getString("key") != null) {
                key = getArguments().getString("key");
            }
        }
        wordList = (ListView) view.findViewById(R.id.wordList);
        wordList.setVisibility(View.VISIBLE);
        sqldata = SQLiteDatabase.openOrCreateDatabase(DB_PATH, null);
        exlist = (ExpandableListView) view.findViewById(R.id.exdanci);
        exlist.setDivider(null);
        exlist.setGroupIndicator(null);
        exlist.setOverScrollMode(View.OVER_SCROLL_NEVER);
        exlist.setOnGroupExpandListener(new OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                for (int i = 0; i < myexadapter.getGroupCount(); i++) {
                    if (groupPosition != i) {
                        exlist.collapseGroup(i);
                    }
                }
            }
        });
        analyse(key);
        return view;
    }

    public void analyse(String content) {
        if (content == null || content.equals("")) {
            cursor = sqldata.query("c", new String[]{"id,fenlei,title,content"}, "title like ?", new String[]{"%" + content + "%"}, null,
                    null, "id asc");
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                String arr[] = cursor.getString(0).split(",");
                for (int i = 0; i < arr.length; i++) {
                    allMap = new HashMap<>();
                    allMap.put("value", cursor.getString(2));
                    boolean add = allList.add(allMap);
                    break;
                }
            }
            for (int i = 0; i < allList.size(); i++) {
                if (allList.get(i).get("value").toString().equals("标题") || allList.get(i).get("value").toString() == "标题") {
                    allList.remove(i);
                }
            }
            adapter = new WordAdapter(allList, WordFragment2.this.context);
            exlist.setVisibility(View.GONE);
            wordList.setAdapter(adapter);
            wordList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    wordList.setVisibility(View.GONE);
                    int index = adapterView.getAdapter().getItem(position).toString().indexOf("=");
                    String name = adapterView.getAdapter().getItem(position).toString().substring(index + 1, adapterView.getAdapter().getItem(position).toString().length() - 1);
                    cursor = sqldata.query("c", new String[]{"id,fenlei,title,content"}, "title like ?", new String[]{"%" + name + "%"}, null,
                            null, "id asc");
                    if (cursor.moveToFirst() || !cursor.isAfterLast() || cursor.moveToNext()) {
                        fm = getActivity().getSupportFragmentManager();
                        fmt = fm.beginTransaction();
                        if (fm.findFragmentByTag(WordFragment2.TAG) != null) {
                            fmt.hide(fm.findFragmentByTag(WordFragment2.TAG));
                        }
                        fmt.commit();
                        fmt = fm.beginTransaction();
                        wordxqFragment = (WordxqFragment) fm.findFragmentByTag(WordxqFragment.TAG);
                        wordxqFragment = WordxqFragment.getInstance(cursor.getString(3));
                        fmt.add(R.id.ll_fragment, wordxqFragment, WordxqFragment.TAG);
                        fmt.commit();
                    }
                }
            });
            view.findViewById(R.id.ll_loading).setVisibility(View.GONE);
        } else {
            getData(content);
        }
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    jsonString = (String) msg.obj;
                    jsonObject = JSON.parseObject(jsonString);
                    String m = jsonObject.getString("msg");
                    if (m.equals("200")) {
                        jsonObject = jsonObject.getJSONObject("result");

                        if (jsonObject.toString().equals("{}")) {
                            view.findViewById(R.id.pb_loading).setVisibility(View.GONE);
                            view.findViewById(R.id.tv_loading).setVisibility(View.GONE);
//                            handler.sendEmptyMessage(2);
//                            Toast.makeText(getActivity(), "未找到相关内容", Toast.LENGTH_LONG).show();
                        } else {
                            Set<String> keyset = jsonObject.keySet();
                            Iterator<String> it = keyset.iterator();
                            map.clear();
                            String key;

                            while (it.hasNext()) {
                                list = new ArrayList<>();
                                key = it.next();
                                String[] ss = jsonObject.getString(key).split(",");
                                for (int i = 0; i < ss.length; i++) {
                                    list.add(ss[i]);
                                }
                                map.put(key, list);
                            }
                            myexadapter = new MyexAdapter();
                            wordList.setVisibility(View.GONE);
                            exlist.setVisibility(View.VISIBLE);
                            exlist.setAdapter(myexadapter);
                            view.findViewById(R.id.ll_loading).setVisibility(View.GONE);
                        }
                    } else {
                        view.findViewById(R.id.pb_loading).setVisibility(View.GONE);
                        view.findViewById(R.id.tv_loading).setVisibility(View.GONE);
                    }
//                    else {
//                        handler.sendEmptyMessage(2);
//                        Toast.makeText(getActivity(), "请求错误", Toast.LENGTH_LONG).show();
//                    }
                    break;
//                case 2:
//                    view.findViewById(R.id.pb_loading).setVisibility(View.GONE);
//                    ((TextView) view.findViewById(R.id.tv_loading)).setText("加载失败!");
//                    ((TextView) view.findViewById(R.id.tv_loading_info)).setText("选择句子过长,请选择短句");
//                    break;
                default:
                    break;
            }
            return false;
        }
    });

    URL surl;

    public void getData(String keyword) {
        if (view != null) {
            view.findViewById(R.id.ll_loading).setVisibility(View.VISIBLE);
            view.findViewById(R.id.pb_loading).setVisibility(View.VISIBLE);
            ((TextView) view.findViewById(R.id.tv_loading)).setText("加载中...");
            ((TextView) view.findViewById(R.id.tv_loading_info)).setText("");
        }

        try {
            keyword = URLEncoder.encode(keyword, "UTF-8");
            String url = "http://101.201.212.118/parser/getDict?words=" + keyword + "&phone=1500106";
            surl = new URL(url);

        } catch (Exception e) {
            JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());
        }
        new Thread() {
            public void run() {

                String s = null;
                HttpURLConnection con;
                try {
                    con = (HttpURLConnection) surl.openConnection();
                    con.setReadTimeout(5000);
                    con.setRequestMethod("GET");
                    if (con.getResponseCode() == 200) {
                        InputStream is = con.getInputStream();
                        s = StreamTools.streamToString(is);
                    }
                } catch (IOException e) {
                    JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());
                } finally {
                    if (s == null || s.length() <= 0) {
                        handler.sendEmptyMessage(2);
                    } else {
                        handler.obtainMessage(1, s).sendToTarget();
                    }
                }
            }

            ;
        }.start();
    }



    public class MyexAdapter extends BaseExpandableListAdapter {
        ArrayList<String> a = new ArrayList<>();
        ArrayList<ArrayList<String>> b = new ArrayList<>();
        SQLiteDatabase sqldata = null;

        public MyexAdapter() {
            a.clear();
            b.clear();
            Iterator<Entry<String, ArrayList<String>>> it = map.entrySet().iterator();
            String key;
            sqldata = SQLiteDatabase.openOrCreateDatabase(DB_PATH, null);
            while (it.hasNext()) {
                Entry<String, ArrayList<String>> entry = it.next();
                key = entry.getKey();
                a.add(key);
                b.add(entry.getValue());

            }
        }

        @Override
        public int getGroupCount() {

            return a.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {

            return b.get(groupPosition).size();
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
            GroupHolder gho = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_group, parent, false);
                gho = new GroupHolder();
                gho.gtv = (TextView) convertView.findViewById(R.id.group);
                gho.giv = (ImageView) convertView.findViewById(R.id.im_ex);
                convertView.setTag(gho);
            } else {
                gho = (GroupHolder) convertView.getTag();
            }
            gho.gtv.setText(a.get(groupPosition));
            if (isExpanded) {
                gho.giv.setImageResource(R.drawable.open1);
            } else {
                gho.giv.setImageResource(R.drawable.close);
            }
            return convertView;
        }

        @Override
        public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild,
                                 View convertView, ViewGroup parent) {
            ChildHolder cho = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_dancichild, parent, false);
                cho = new ChildHolder();
                cho.ctv = (TextView) convertView.findViewById(R.id.itemdancichild);
                convertView.setTag(cho);
            } else {
                cho = (ChildHolder) convertView.getTag();
            }
            cursor = sqldata.rawQuery("select * from c where id=?",
                    new String[]{b.get(groupPosition).get(childPosition)});
            cursor.moveToFirst();

            cho.ctv.setText(cursor.getString(2));
            cho.ctv.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    cursor = sqldata.rawQuery("select * from c where id=?",
                            new String[]{b.get(groupPosition).get(childPosition)});
                    cursor.moveToFirst();
                    wordxqFragment = WordxqFragment.getInstance(cursor.getString(3));
                    fm = getActivity().getSupportFragmentManager();
                    // fm.popBackStack();
                    while (fm.getBackStackEntryCount() > 0) {
                        fm.popBackStackImmediate();
                    }
                    fmt = fm.beginTransaction();
                    fmt.replace(R.id.layout_frame, wordxqFragment);
                    fmt.addToBackStack(null);
                    fmt.commit();
                }
            });
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {

            return false;
        }

    }

    public class GroupHolder {
        TextView gtv;
        ImageView giv;
    }

    public class ChildHolder {
        TextView ctv;
    }
}