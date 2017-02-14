package net.naucu.englishxianshi.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jiongbull.jlog.JLog;

import net.naucu.englishxianshi.R;
import net.naucu.englishxianshi.ui.DictionaryTranslationActivity;
import net.naucu.englishxianshi.ui.EBookDbDao;
import net.naucu.englishxianshi.ui.PhraseAnalysis;
import net.naucu.englishxianshi.util.StreamTools;
import net.naucu.englishxianshi.widget.view.CustomExpandableListView;
import net.naucu.englishxianshi.widget.view.CustomListView;

import org.json.JSONException;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class GrammarAnalysisFragment extends Fragment {

    private Typeface faceCn;
    private Typeface faceEn;

    public static final String TAG = GrammarAnalysisFragment.class.getSimpleName();

    private View view, dashfenju, dashdulizhuge;
    private String sentence;
    private CustomListView lvjuxingjiegou;
    private CustomExpandableListView lvduanyu, lvfenju, lvdulizhuge, lvdanci;
    private LinearLayout jiegouq;
    private TextView tvfenju, tvdulizhuge;
    private HashMap<String, String> flow = new HashMap<String, String>();
    private HashMap<String, ArrayList<String>> phrase = new HashMap<String, ArrayList<String>>();
    private HashMap<String, ArrayList<String>> sbar = new HashMap<String, ArrayList<String>>();
    private HashMap<String, ArrayList<String>> zz = new HashMap<String, ArrayList<String>>();
    private HashMap<String, ArrayList<String>> word = new HashMap<String, ArrayList<String>>();

    private HashMap<String, String> jutai = new HashMap<String, String>();
    private HashMap<String, String> juxing = new HashMap<String, String>();

    private JSONObject jsonObject;
    private ImageView onekeyopenclose;
    private HashMap<String, String> map = new HashMap<String, String>();

    private MyExpandableListAdapter aphrase, asbar, azz, aword;
    CallBackValue callBackValue;

    @Override
    public void onAttach(Context context) {
        super.onAttach(getContext());
        callBackValue = (CallBackValue) getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_yufajiexi, container, false);
        faceCn = Typeface.createFromAsset(getActivity().getAssets(), "fonts/default.ttf");
        faceEn = Typeface.createFromAsset(getActivity().getAssets(), "fonts/defaultEng.ttf");
        initview();
        initmap();
        return view;
    }

    public interface CallBackValue {
        public void sendMessageValue(String str);
    }
    public void initview() {
        onekeyopenclose = (ImageView) view.findViewById(R.id.im_onkeyopenclose);
        dashfenju = view.findViewById(R.id.dashfenju);
        dashdulizhuge = view.findViewById(R.id.dashdulizhuge);
        tvfenju = (TextView) view.findViewById(R.id.tvfenju);
        tvdulizhuge = (TextView) view.findViewById(R.id.tvdulizhuge);

        lvjuxingjiegou = (CustomListView) view.findViewById(R.id.lvjuxingjiegou);
        lvduanyu = (CustomExpandableListView) view.findViewById(R.id.lvduanyu);
        lvfenju = (CustomExpandableListView) view.findViewById(R.id.lvfenju);
        lvdulizhuge = (CustomExpandableListView) view.findViewById(R.id.lvdulizhuge);
        lvdanci = (CustomExpandableListView) view.findViewById(R.id.lvdanci);

        lvduanyu.setGroupIndicator(null);
        lvfenju.setGroupIndicator(null);
        lvdulizhuge.setGroupIndicator(null);
        lvdanci.setGroupIndicator(null);

        lvjuxingjiegou.setDivider(null);
        lvduanyu.setDivider(null);
        lvduanyu.setChildDivider(null);
        lvfenju.setDivider(null);
        lvfenju.setChildDivider(null);
        lvdulizhuge.setDivider(null);
        lvdulizhuge.setChildDivider(null);
        lvdanci.setDivider(null);
        lvdanci.setChildDivider(null);
        lvjuxingjiegou.setDivider(null);

        jiegouq = (LinearLayout) view.findViewById(R.id.jiegouq);
        onekeyopenclose.setImageResource(R.drawable.open1);
        onekeyopenclose.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (jiegouq.getVisibility() == View.VISIBLE) {
                    jiegouq.setVisibility(View.INVISIBLE);
                    onekeyopenclose.setImageResource(R.drawable.close);
                } else {
                    jiegouq.setVisibility(View.VISIBLE);
                    onekeyopenclose.setImageResource(R.drawable.open1);
                }
            }
        });

    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    flow.clear();
                    phrase.clear();
                    sbar.clear();
                    zz.clear();
                    word.clear();

                    jsonObject = (JSONObject) JSON.parse((String) msg.obj);
                    if (jsonObject != null) {
                        if (jsonObject.get("msg").equals("200")) {

                            jsonObject = (JSONObject) jsonObject.get("result");
                            Log.i("shui", "handleMessage: "+jsonObject.toString());
                            initflow();
                            initphrase();
                            initsbar();
                            initzz();
                            initword();
                            aphrase = new MyExpandableListAdapter(getActivity(), phrase);
                            asbar = new MyExpandableListAdapter(getActivity(), sbar);
                            azz = new MyExpandableListAdapter(getActivity(), zz);
                            aword = new MyExpandableListAdapter(getActivity(), word);

                            view.findViewById(R.id.ll_loading).setVisibility(View.GONE);

                            lvjuxingjiegou.setAdapter(new MyListAdapter(getActivity(), flow));
                            lvduanyu.setAdapter(aphrase);
                            lvfenju.setAdapter(asbar);
                            lvdulizhuge.setAdapter(azz);
                            lvdanci.setAdapter(aword);

                            lvduanyu.setOnGroupExpandListener(new MyOnGroupExpandListener(lvduanyu, aphrase));
                            lvfenju.setOnGroupExpandListener(new MyOnGroupExpandListener(lvfenju, asbar));
                            lvdulizhuge.setOnGroupExpandListener(new MyOnGroupExpandListener(lvdulizhuge, azz));
                            lvdanci.setOnGroupExpandListener(new MyOnGroupExpandListener(lvdanci, aword));

                            if (sbar.size() == 0) {
                                dashfenju.setVisibility(View.GONE);
                                tvfenju.setVisibility(View.GONE);
                            } else {
                                dashfenju.setVisibility(View.VISIBLE);
                                tvfenju.setVisibility(View.VISIBLE);
                            }
                            if (zz.size() == 0) {
                                dashdulizhuge.setVisibility(View.GONE);
                                tvdulizhuge.setVisibility(View.GONE);
                            } else {
                                dashdulizhuge.setVisibility(View.VISIBLE);
                                tvdulizhuge.setVisibility(View.VISIBLE);
                            }
                        } else {
                            handler.sendEmptyMessage(2);
                        }
                    }

                    break;

                case 2:
                    view.findViewById(R.id.pb_loading).setVisibility(View.GONE);
                    ((TextView) view.findViewById(R.id.tv_loading)).setText("加载失败!");
                    ((TextView) view.findViewById(R.id.tv_loading_info)).setText("未查找到相关数据 ，或选择句子过长");
                    break;
                default:
                    break;
            }
            return false;
        }
    });
    public class MyOnGroupExpandListener implements OnGroupExpandListener {
        ExpandableListView explist;
        ExpandableListAdapter ad;
        MyOnGroupExpandListener(ExpandableListView ex, ExpandableListAdapter a) {
            this.ad = a;
            this.explist = ex;
        }
        @Override
        public void onGroupExpand(int groupPosition) {
            for (int i = 0; i < ad.getGroupCount(); i++) {
                if (groupPosition != i) {
                    explist.collapseGroup(i);
                }
            }
        }
    }
    public class MyListAdapter extends BaseAdapter {
        ArrayList<String> a = new ArrayList<>();
        HashMap<String, String> map = new HashMap<>();
        Context context;
        public MyListAdapter(Context context, HashMap<String, String> map) {
            this.map = map;
            this.context = context;
            Set<String> set = map.keySet();
            Iterator<String> it = set.iterator();
            while (it.hasNext()) {
                String key = it.next();
                a.add(key);
            }
        }

        @Override
        public int getCount() {
            return a.size();
        }
        @Override
        public Object getItem(int position) {
            return position;
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @SuppressLint("ViewHolder")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_yutai, parent, false);
            TextView yutai = (TextView) convertView.findViewById(R.id.yutai);
            TextView yutaijiegou = (TextView) convertView.findViewById(R.id.yutaijiegou);
            yutai.setTypeface(faceCn);
            yutaijiegou.setTypeface(faceCn);
            yutai.setText(jutai.get(a.get(position)));
            yutaijiegou.setText(juxing.get(map.get(a.get(position))));
            yutai.setTextColor(Color.WHITE);
            final String key = map.get(a.get(position));
            yutaijiegou.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getActivity(), PhraseAnalysis.class);
                    i.putExtra("duanyu", key);
                    startActivity(i);
                }
            });
            return convertView;
        }
    }

    public class MyExpandableListAdapter extends BaseExpandableListAdapter {
        ArrayList<String> group;
        ArrayList<ArrayList<String>> children;
        Context context;
        Boolean flag = false;

        public MyExpandableListAdapter(Context context, HashMap<String, ArrayList<String>> map) {
            this.context = context;
            Set<String> set = map.keySet();
            group = new ArrayList<>();
            children = new ArrayList<>();
            Iterator<String> it = set.iterator();
            while (it.hasNext()) {
                String key = it.next().trim();
                char c = key.charAt(key.length() - 1);
                if (c >= 32 && c <= 64 || c >= 91 && c <= 96 || c >= 123 && c <= 126) {
                    continue;
                } else {
                    group.add(key);
                }
                ArrayList<String> a = map.get(key);
                children.add(a);
            }
        }
        @Override
        public int getGroupCount() {
            return group.size();
        }
        @Override
        public int getChildrenCount(int groupPosition) {
            return children.get(groupPosition).size();
        }
        @Override
        public Object getGroup(int groupPosition) {
            return group.get(groupPosition);
        }
        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return children.get(groupPosition).get(childPosition);
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
            return true;
        }
        @Override
        public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_group, parent, false);
            TextView txt = (TextView) convertView.findViewById(R.id.group);
            ImageView im = (ImageView) convertView.findViewById(R.id.im_ex);
            txt.setTypeface(faceCn);

            if (map.containsKey(group.get(groupPosition))) {
                txt.setText(map.get(group.get(groupPosition)));
            } else {
                txt.setText(group.get(groupPosition));
            }

            if (isExpanded) {
                im.setImageResource(R.drawable.open1);
            } else {
                im.setImageResource(R.drawable.close);
            }
            txt.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    flag = false;
                    Intent i = new Intent(getActivity(), PhraseAnalysis.class);
                    i.putExtra("duanyu", group.get(groupPosition));
                    startActivity(i);
                }
            });
            return convertView;
        }

        @Override
        public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild,
                                                            View convertView, final ViewGroup parent) {
            ItemHolder itemHolder;
            final int[] width = new int[1];
            final int[] height = new int[1];
            if (convertView == null) {
                convertView = (View) LayoutInflater.from(context).inflate(R.layout.item_children, parent, false);
                itemHolder = new ItemHolder();
                itemHolder.txt = (TextView) convertView.findViewById(R.id.children);
                itemHolder.but = (TextView) convertView.findViewById(R.id.fanyi);
                itemHolder.txt.setTypeface(faceEn);
                itemHolder.but.setTypeface(faceCn);


                itemHolder.re = (RelativeLayout) convertView.findViewById(R.id.re);
                itemHolder.hr1 = convertView.findViewById(R.id.hr1);
                itemHolder.hr2 = convertView.findViewById(R.id.hr2);
                itemHolder.hr3 = convertView.findViewById(R.id.hr3);
                convertView.setTag(itemHolder);
            } else {
                itemHolder = (ItemHolder) convertView.getTag();
            }
            final TextView b = (TextView) convertView.findViewById(R.id.fanyi);
            final View h1 = convertView.findViewById(R.id.hr1);
            final View h2 = convertView.findViewById(R.id.hr2);
            final View h3 = convertView.findViewById(R.id.hr3);

            final ItemHolder finalItemHolder = itemHolder;
            String s = children.get(groupPosition).get(childPosition);
            itemHolder.txt.setText(s
                    .replaceAll("\\[\\d+\\-\\d+\\]", "")
                    .replaceAll("\\[\\-\\d+\\-\\d+\\]", "")
                    .replaceAll("\\.\\[\\-\\d+\\-\\d+\\]", "")
                    .replaceAll("\\[\\d+\\]", ""));

            itemHolder.txt.post(new Runnable() {
                @Override
                public void run() {
                    width[0] = finalItemHolder.txt.getWidth();
                    height[0] = finalItemHolder.txt.getHeight();
                    int[] s = new int[]{width[0], height[0]};
                    if (s[0] > 310 && s[1] > 80) {
                        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams
                                (RelativeLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                        int he = height[0] - 40;
                        params.setMargins(80, he, 0, 0);
                        int count = finalItemHolder.txt.getLineCount();
                        if (count == 2) {
                            h1.setVisibility(View.VISIBLE);
                            h2.setVisibility(View.GONE);
                            h3.setVisibility(View.GONE);
                        } else if (count == 3) {
                            h1.setVisibility(View.VISIBLE);
                            h2.setVisibility(View.VISIBLE);
                            h3.setVisibility(View.GONE);
                        } else if (count == 4) {
                            h1.setVisibility(View.VISIBLE);
                            h2.setVisibility(View.VISIBLE);
                            h3.setVisibility(View.VISIBLE);
                        } else {
                            h1.setVisibility(view.GONE);
                            h2.setVisibility(view.GONE);
                            h3.setVisibility(view.GONE);
                        }
                    } else {
                        h1.setVisibility(View.GONE);
                        h2.setVisibility(View.GONE);
                        h3.setVisibility(View.GONE);
                    }
                }
            });
            itemHolder.txt.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (flag) {
                        Intent i = new Intent(getActivity(), PhraseAnalysis.class);
                        i.putExtra("key", group.get(groupPosition));// PRP
                        i.putExtra("duanyu", children.get(groupPosition).get(childPosition));
                        i.putExtra("sentence", sentence);
                        startActivity(i);
                    } else {
                        v.setBackgroundResource(R.drawable.select_item_childrenclick);
                        b.setBackgroundResource(R.drawable.select_item_childrenclick);
                        b.setText("翻译");
                        callBackValue.sendMessageValue(children.get(groupPosition).get(childPosition));
                        flag = true;
                    }
                }
            });

            itemHolder.txt.setFocusable(true);
            itemHolder.txt.setFocusableInTouchMode(true);
            itemHolder.txt.setOnFocusChangeListener(new View.OnFocusChangeListener() {

                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        v.setBackgroundResource(R.drawable.select_item_childrenclick);
                        b.setBackgroundResource(R.drawable.select_item_childrenclick);
                        b.setText("翻译");
                        flag = true;
                        callBackValue.sendMessageValue(children.get(groupPosition).get(childPosition));
                    } else {
                        v.setBackgroundResource(R.drawable.select_item_children);
                        b.setBackgroundResource(R.drawable.select_item_childrenonclick);
                        b.setText("");
                        flag = true;
                        callBackValue.sendMessageValue("");
                    }
                }
            });
            itemHolder.but.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    DictionaryTranslationActivity dt = (DictionaryTranslationActivity) context;
                    RequestParams params = dt.translateParams(children.get(groupPosition).get(childPosition)
                            .replaceAll("\\[\\d+\\-\\d+\\]", "")
                            .replaceAll("\\[\\d+\\]", "")
                    );
                    x.http().post(params, new Callback.CommonCallback<String>() {
                        @Override
                        public void onCancelled(CancelledException e) {

                        }

                        @Override
                        public void onError(Throwable throwable, boolean isOnCallback) {

                        }

                        @Override
                        public void onFinished() {

                        }

                        @Override
                        public void onSuccess(String result) {

                            if (result != null && !result.equals("")) {
                                try {
                                    org.json.JSONObject jsonObject = new org.json.JSONObject(result);
                                    org.json.JSONArray jsonArray = jsonObject.optJSONArray("trans_result");
                                    org.json.JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                                    String s = jsonObject1.getString("dst");
                                    AlertDialog.Builder normalDia = new AlertDialog.Builder(context);
                                    normalDia.setMessage(s);
                                    normalDia.create().show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });


                }
            });

            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        class ItemHolder {
            public TextView txt;
            public TextView but;
            public RelativeLayout re;
            public View hr1;
            public View hr2;
            public View hr3;
        }
    }



    public void initphrase() {
        String jstring = jsonObject.getString("phrase");
        Log.e(TAG, "initphrase=" + jstring);

        ArrayList<String> a;
        String[] wordarray;
        JSONObject jphrase = JSONObject.parseObject(jstring);
        Set<String> set = jphrase.keySet();
        Iterator<String> it = set.iterator();
        String key;
        while (it.hasNext()) {
            key = it.next();
            a = new ArrayList<>();
            if (!key.equals("word1") && !key.equals("word2") && !key.equals("word3")) {
                wordarray = jphrase.get(key).toString().split("#");
                for (String s : wordarray) {
                    a.add(s);
                }

                phrase.put(key, a);
            }
        }
    }

    public void initsbar() {
        ArrayList<String> a;
        String[] wordarray;
        try {
            String jstring = jsonObject.getString("sbar");
            JSONObject jsbar = JSONObject.parseObject(jstring);
            Set<String> set = jsbar.keySet();
            Iterator<String> it = set.iterator();
            String key = "";
            while (it.hasNext()) {
                key = it.next();
                a = new ArrayList<String>();
                wordarray = jsbar.get(key).toString().split("#");
                for (String s : wordarray) {
                    a.add(s);
                }
                sbar.put(key, a);
            }
        } catch (Exception e) {
        }
    }

    public void initzz() {
        ArrayList<String> a;
        String[] wordarray;
        try {
            String jstring = jsonObject.getString("zz");
            Log.e(TAG, "initzz=" + jstring);

            JSONObject jzz = JSONObject.parseObject(jstring);
            Set<String> set = jzz.keySet();
            Iterator<String> it = set.iterator();
            String key = "";
            while (it.hasNext()) {
                key = it.next();
                a = new ArrayList<>();
                wordarray = jzz.get(key).toString().split("#");
                for (String s : wordarray) {
                    a.add(s);
                }

                zz.put(key, a);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initword() {
        String jstring = jsonObject.getString("word");
        Log.e(TAG, "initword=" + jstring);

        ArrayList<String> a;
        String[] wordarray;

        JSONObject jword = JSONObject.parseObject(jstring);
        Set<String> set = jword.keySet();
        Iterator<String> it = set.iterator();
        String key = "";
        while (it.hasNext()) {
            a = new ArrayList<>();
            key = it.next();
            wordarray = jword.get(key).toString().split("#");
            for (String s : wordarray) {
                a.add(s);
            }
            word.put(key, a);
        }
    }

    public void initflow() {
        JSONObject jflow = jsonObject.getJSONObject("flow");

        Set<String> set = jflow.keySet();
        Iterator<String> it = set.iterator();
        String sss;
        String ggg;

        while (it.hasNext()) {
            sss = it.next();
            ggg = jflow.getString(sss);
            if (!sss.equals("dot")) {
                flow.put(sss, ggg);
            }
        }
    }

    URL surl;

    public void getData(final String keyword, Context context) {
        final String type = DictionaryTranslationActivity.YUFA;
        if (view != null) {
            view.findViewById(R.id.ll_loading).setVisibility(View.VISIBLE);
            view.findViewById(R.id.pb_loading).setVisibility(View.VISIBLE);
            ((TextView) view.findViewById(R.id.tv_loading)).setText("加载中...");
            ((TextView) view.findViewById(R.id.tv_loading_info)).setText("");
        }

        final EBookDbDao dao = new EBookDbDao(context);
        String json = dao.select(keyword, type);
        handler.obtainMessage(1, json).sendToTarget();
        sentence = keyword;
        String word;
        try {
            word = URLEncoder.encode(keyword, "UTF-8");
            String url = "http://101.201.212.118/parser/getData?words=" + word + "&phone=15000000000";
            surl = new URL(url);
        } catch (Exception e) {
            JLog.e(e.getMessage() == null ? "Exception is null" : e.getMessage());
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
                    e.printStackTrace();
                } finally {
                    if (s != null && s.length() > 0) {
                        handler.obtainMessage(1, s).sendToTarget();
                        dao.add(keyword, s, type);
                    } else {
                        handler.sendEmptyMessage(2);
                    }
                }
            }
        }.start();
    }

    public void initmap() {

//        map.put("VP", "动词短语");

        map.put("GD", "独立主格");
        map.put("LB", "并列句");
        map.put("BV", "不定式");
        map.put("CV", "省略to的不定式");
        map.put("DV", "现在分词短语");
        map.put("EV", "过去分词短语");
        map.put("FV", "谓语");
        map.put("ADJP", "形容词短语");
        map.put("ADVP", "副词短语");
        map.put("DP", "限定词短语");
        map.put("FRAG", "不完整或有省略");
        map.put("LCP", "方位词短语");
        map.put("NP", "名词短语");
        map.put("PP", "介词短语");
        map.put("PU", "标点符号");
        map.put("QP", "量词短语");
        map.put("SBAR", "从句");
        map.put("WHADJP", "wh形容词短语");
        map.put("WHADVP", "wh副词短语");
        map.put("WHNP", "wh名词短语");
        map.put("WHPP", "wh介词短语");
        map.put("PRT", "小品词短语");
        map.put("CONJP", "连接词短语");
        map.put("INTJ", "感叹词");
        map.put("PRN", "插入语");
        map.put("UCP", "不对称的同位短语");
        map.put("CC", "并列连接词");
        map.put("CD", "基数");
        map.put("DT", "限定词");
        map.put("EX", "存在句引导词");
        map.put("FW", "非英语单词");
        map.put("IN", "介词或连接词");
        map.put("INC", "从属连词");
        map.put("JJ", "形容词");
        map.put("JJR", "形容词，比较级");
        map.put("JJS", "形容词，最高级");
        map.put("LS", "列表项标记");
        map.put("MD", "情态动词");
        map.put("NN", "名词");
        map.put("NNS", "名词，复数");
        map.put("NNP", "专有名词单数");
        map.put("NNPS", "专有名词复数");
        map.put("PDT", "限定词");
        map.put("POS", "名词所有格符号");
        map.put("PRP", "人称代词");
        map.put("PRP$", "物主代词");
        map.put("RB", "副词");
        map.put("RBR", "副词，比较级");
        map.put("RBS", "副词，最高级");
        map.put("RP", "小品词");
        map.put("SYM", "符号（数学或科学）");
        map.put("TO", "不定式引导词");
        map.put("UH", "感叹词");
        map.put("VB", "动词原形");
        map.put("VBD", "动词过去式");
        map.put("VBG", "现在分词");
        map.put("VBN", "过去分词");
        map.put("VBP", "动词非第三人称单数");
        map.put("VBZ", "动词第三人称单数");
        map.put("WDT", "关系或疑问限定词");
        map.put("WDTS", "关系限定词");
        map.put("WP", "关系或疑问代词");
        map.put("WPS", "关系代词");
        map.put("WP", "关系或疑问代词所有格");
        map.put("WP$S", "关系代词所有格");
        map.put("WRB", "关系或疑问副词");
        map.put("WRBS", "关系副词");
        jutai.put("flow1", "时态:");
        jutai.put("flow235b", "语态:");
        jutai.put("flow235c", "句式:");
        jutai.put("flow235a", "语气:");
        jutai.put("flow4", "句型:");
        juxing.put("11", "一般现在时");
        juxing.put("12", "一般过去时");
        juxing.put("13", "一般将来时");
        juxing.put("14", "过去将来时");
        juxing.put("15", "现在完成时");
        juxing.put("16", "过去完成时");
        juxing.put("17", "将来完成时");
        juxing.put("18", "过去将来完成时");
        juxing.put("19", "现在进行时");
        juxing.put("110", "过去进行时");
        juxing.put("111", "将来进行时");
        juxing.put("112", "过去将来进行时");
        juxing.put("113", "现在完成进行时");
        juxing.put("114", "过去完成进行时");
        juxing.put("115", "将来完成进行时");
        juxing.put("116", "过去将来完成进行时");
        juxing.put("21", "there be存在句");
        juxing.put("22", "there倒装句");
        juxing.put("23", "it引导的强调句");
        juxing.put("24", "it形式主语结构");
        juxing.put("25", "it形式宾语结构");
        juxing.put("26", "it宾语(间宾)结构");
        juxing.put("31", "否定语气");
        juxing.put("32", "do强调语气");
        juxing.put("33", "虚拟语气");
        juxing.put("41", "一般疑问句");
        juxing.put("42", "反义疑问句");
        juxing.put("43", "特殊疑问句");
        juxing.put("44", "选择疑问句");
        juxing.put("45", "倒装句");
        juxing.put("46", "感叹句");
        juxing.put("47", "祈使句");
        juxing.put("48", "陈述句");
        juxing.put("49", "句子不完整或有省略");
        juxing.put("51", "被动语态");

    }


}

