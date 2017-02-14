package net.naucu.englishxianshi.fragment;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lijunsai.httpInterface.tool.CardUtils;

import net.naucu.englishxianshi.R;
import net.naucu.englishxianshi.ui.MasaGrammarActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdvancedGrammarFragment2 extends Fragment {
    public static final String TAG = AdvancedGrammarFragment2.class.getSimpleName();
    private String key;
    private String a;
    private ArrayList<String> list = new ArrayList<>();
    private HashMap<String, String> map = new HashMap<>();
    private HashMap<String, String> map1 = new HashMap<>();
    private HashMap<String, String> map2 = new HashMap<>();
    private HashMap<String, String> map3 = new HashMap<>();
    private Cursor cursor;
    private AdvancedGrammarxqFragment fg = null;
    private FragmentManager fm = null;
    private FragmentTransaction fmt = null;
    private View view;
    private Context mContext;
    private ListView lv;
    private SQLiteDatabase sqldata;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_yufajj, container, false);
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        lv = (ListView) view.findViewById(R.id.listyufajj);
        lv.setOverScrollMode(View.OVER_SCROLL_NEVER);
        list.clear();
        map.clear();
        key = getArguments().getString("key");
        String DB_PATH = CardUtils.basePath + "/d.db";
        sqldata = SQLiteDatabase.openOrCreateDatabase(DB_PATH, null);
        showData(key);
        return view;
    }

    public static AdvancedGrammarFragment2 getInstance(String key) {
        AdvancedGrammarFragment2 f = new AdvancedGrammarFragment2();
        Bundle args = new Bundle();
        args.putString("key", key);
        f.setArguments(args);
        return f;
    }

    List<Map<String, Object>> kList = new ArrayList<>();
    Map<String, Object> kMap;

    private void showData(String key) {
        if (key.length() > 0) {
//            String value = duiBi(key);
//            cursor = sqldata.query("b", new String[]{"a,id,b,c,d"}, "a like ?", new String[]{"%" + value + "%"}, null,
//                    null, "id asc");
            cursor = sqldata.query("b", new String[]{"a,id,b,c,d"}, "c like ?", new String[]{"%" + key + "%"}, null, null, "id asc");
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                String arr[] = cursor.getString(0).split(",");
                for (int i = 0; i < arr.length; i++) {
                    a = cursor.getString(3);
                    list.add(a);
                    map.put(a, cursor.getString(4));
                    break;
                }
            }
        } else {
            cursor = sqldata.query("b", new String[]{"a,id,b,c,d"}, "c like ?", new String[]{"%" + key + "%"}, null,
                    null, "id asc");
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                String arr[] = cursor.getString(0).split(",");
                for (int i = 0; i < arr.length; i++) {
                    a = cursor.getString(3);
                    list.add(a);
                    map.put(a, cursor.getString(4));
                    break;

                }
            }
        }
        if (cursor.getCount() == 0) {
            Toast.makeText(MasaGrammarActivity.application, "未找到相关内容", Toast.LENGTH_SHORT).show();
        }
        lv.setAdapter(new MyAdapter());
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                fm = getActivity().getSupportFragmentManager();
                fmt = fm.beginTransaction();
                if (fm.findFragmentByTag(AdvancedGrammarFragment2.TAG) != null) {
                    fmt.hide(fm.findFragmentByTag(AdvancedGrammarFragment2.TAG));
                }
                fmt.commit();
                fmt = fm.beginTransaction();
                fg = (AdvancedGrammarxqFragment) fm.findFragmentByTag(AdvancedGrammarxqFragment.TAG);
                if (fg == null) {
                    fg = AdvancedGrammarxqFragment.getInstance(map.get(list.get(position)));
                    fmt.add(R.id.ll_fragment, fg, AdvancedGrammarxqFragment.TAG);
                } else {
                    fg.setKey(map.get(list.get(position)));
                    fmt.show(fg);
                }
                fmt.commit();
            }
        });
    }

    public void setKey(String key) {
        this.key = key;
    }

    public class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder vh = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_yufajj, parent, false);
                vh = new ViewHolder();
                vh.tv = (TextView) convertView.findViewById(R.id.tv_yufajj);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }
            vh.tv.setText(list.get(position));
//            vh.tv.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//            });
            return convertView;
        }

    }

    public class ViewHolder {
        public TextView tv;
    }

    public String duiBi(String s) {
        if (("逗号").equals(s)) {
            return ",";
        }
        if (("过去将来完成时").equals(s)) {
            return "18";
        }
        if (("将来完成时").equals(s)) {
            return "17";
        }
        if (("过去完成时").equals(s)) {
            return "16";
        }
        if (("现在完成时").equals(s)) {
            return "15";
        }
        if (("过去将来时").equals(s)) {
            return "14";
        }
        if (("一般将来时").equals(s)) {
            return "12";
        }
        if (("一般过去时").equals(s)) {
            return "12";
        }
        if (("一般现在时").equals(s)) {
            return "11";
        }
        if (("句号").equals(s)) {
            return ".";
        }
        if (("过去完成进行时").equals(s)) {
            return "114";
        }
        if (("现在完成进行时").equals(s)) {
            return "113";
        }
        if (("过去将来进行时").equals(s)) {
            return "112";
        }
        if (("将来进行时").equals(s)) {
            return "111";
        }
        if (("过去进行时").equals(s)) {
            return "110";
        }
        if (("现在进行时").equals(s)) {
            return "19";
        }
        if (("过去将来完成时").equals(s)) {
            return "18";
        }
        if (("特殊疑问句").equals(s)) {
            return "43";
        }
        if (("反义疑问句").equals(s)) {
            return "42";
        }
        if (("一般疑问句").equals(s)) {
            return "41";
        }
        if (("过去将来完成进行时").equals(s)) {
            return "116";
        }
        if (("将来完成进行时").equals(s)) {
            return "115";
        }
        if (("过去完成进行时").equals(s)) {
            return "114";
        }
        if (("现在完成进行时").equals(s)) {
            return "113";
        }
        if (("句子不完整或有省略").equals(s)) {
            return "49";
        }
        if (("陈述句").equals(s)) {
            return "48";
        }
        if (("祈使句").equals(s)) {
            return "47";
        }
        if (("感叹句").equals(s)) {
            return "46";
        }
        if (("倒装句").equals(s)) {
            return "45";
        }
        if (("选择疑问句").equals(s)) {
            return "44";
        }
        if (("it形式宾语（间宾）结构").equals(s)) {
            return "26";
        }
        if (("it形式宾语结构").equals(s)) {
            return "25";
        }
        if (("it形式主语结构").equals(s)) {
            return "24";
        }
        if (("it引导的强调句").equals(s)) {
            return "23";
        }
        if (("there倒装句").equals(s)) {
            return "22";
        }
        if (("there be 存在句").equals(s)) {
            return "21";
        }
        if (("被动语态").equals(s)) {
            return "51";
        }
        if (("复合宾语").equals(s)) {
            return "a3";
        }
        if (("逻辑主语").equals(s)) {
            return "a2";
        }
        if (("主语").equals(s)) {
            return "a1";
        }
        if (("虚拟语气").equals(s)) {
            return "33";
        }
        if (("do强调语气").equals(s)) {
            return "32";
        }
        if (("否定语气").equals(s)) {
            return "31";
        }

        if (("状语").equals(s)) {
            return "a9";
        }
        if (("宾语").equals(s)) {
            return "a8";
        }
        if (("主语补足语").equals(s)) {
            return "a7";
        }
        if (("介词宾语").equals(s)) {
            return "a6";
        }
        if (("宾语补足语").equals(s)) {
            return "a5";
        }
        if (("表语").equals(s)) {
            return "a4";
        }
        if (("后置定语").equals(s)) {
            return "b6";
        }
        if (("同位语").equals(s)) {
            return "b5";
        }
        if (("定语").equals(s)) {
            return "b4";
        }
        if (("间接宾语").equals(s)) {
            return "b3";
        }
        if (("时间状语").equals(s)) {
            return "b2";
        }
        if (("直接宾语").equals(s)) {
            return "b1";
        }
        if (("形容词宾语").equals(s)) {
            return "c3";
        }
        if (("被it替代的宾语").equals(s)) {
            return "c2";
        }
        if (("被it替代的主语").equals(s)) {
            return "c1";
        }
        if (("动词进行时态").equals(s)) {
            return "b9";
        }
        if (("副词补足语").equals(s)) {
            return "b8";
        }
        if (("形容词补足语").equals(s)) {
            return "b7";
        }
        if (("同位语从句").equals(s)) {
            return "d3";
        }
        if (("宾语从句").equals(s)) {
            return "d2";
        }
        if (("被it替代的主语从句").equals(s)) {
            return "d1";
        }
        if (("表语从句").equals(s)) {
            return "c9";
        }
        if (("it强调句非强调部分").equals(s)) {
            return "c8";
        }
        if (("形容词宾语从句").equals(s)) {
            return "c7";
        }
        if (("定语从句").equals(s)) {
            return "c6";
        }
        if (("状语从句").equals(s)) {
            return "c5";
        }
        if (("动词被动语态").equals(s)) {
            return "c4";
        }
        if (("并列句").equals(s)) {
            return "LB";
        }
        if (("独立主格").equals(s)) {
            return "GD";
        }
        if (("谓语动词").equals(s)) {
            return "e1";
        }
        if (("it替代的宾语从句").equals(s)) {
            return "d6";
        }
        if (("介词宾语从句").equals(s)) {
            return "d5";
        }
        if (("主语从句").equals(s)) {
            return "d4";
        }
        if (("副词短语").equals(s)) {
            return "ADVP";
        }
        if (("形容词短语").equals(s)) {
            return "ADJP";
        }
        if (("谓语动词短语").equals(s)) {
            return "FV";
        }
        if (("过去分词短语").equals(s)) {
            return "EV";
        }
        if (("现在分词短语").equals(s)) {
            return "DV";
        }
        if (("省略to的不定式").equals(s)) {
            return "CV";
        }
        if (("不定式").equals(s)) {
            return "BV";
        }
        if (("量词短语").equals(s)) {
            return "QP";
        }
        if (("标点符号").equals(s)) {
            return "PU";
        }
        if (("介词短语").equals(s)) {
            return "PP";
        }
        if (("名词短语").equals(s)) {
            return "NP";
        }
        if (("方位词短语").equals(s)) {
            return "LCP";
        }
        if (("不完整或有省略").equals(s)) {
            return "FRAG";
        }
        if (("限定词短语").equals(s)) {
            return "DP";
        }
        if (("连接词短语").equals(s)) {
            return "CONJP";
        }
        if (("小品词短语").equals(s)) {
            return "PRT";
        }
        if (("wh介词短语").equals(s)) {
            return "WHPP";
        }
        if (("wh名词短语").equals(s)) {
            return "WHNP";
        }
        if (("wh副词短语").equals(s)) {
            return "WHADJVP";
        }
        if (("wh形容词短语").equals(s)) {
            return "WHADJP";
        }
        if (("从句").equals(s)) {
            return "SBAR";
        }
        if (("数词").equals(s)) {
            return "CD";
        }
        if (("连词").equals(s)) {
            return "CC";
        }
        if (("不对称的同位短语").equals(s)) {
            return "UCP";
        }
        if (("插入语").equals(s)) {
            return "PRN";
        }
        if (("感叹词").equals(s)) {
            return "INTJ";
        }
        if (("形容词，最高级").equals(s)) {
            return "JJS";
        }
        if (("形容词，比较级").equals(s)) {
            return "JJR";
        }
        if (("形容词").equals(s)) {
            return "JJ";
        }
        if (("介词").equals(s)) {
            return "IN";
        }
        if (("非英语单词").equals(s)) {
            return "FW";
        }
        if (("存在句引导词").equals(s)) {
            return "EX";
        }
        if (("限定词").equals(s)) {
            return "DT";
        }
        if (("名词所有格符号").equals(s)) {
            return "POS";
        }
        if (("限定词").equals(s)) {
            return "PDT*";
        }
        if (("专有名词复数").equals(s)) {
            return "NNPS";
        }
        if (("专有名词单数").equals(s)) {
            return "NNP";
        }
        if (("名词，复数").equals(s)) {
            return "NNS";
        }
        if (("名词").equals(s)) {
            return "NN";
        }
        if (("情态动词").equals(s)) {
            return "MD";
        }
        if (("列表项标记").equals(s)) {
            return "LS*";
        }
        if (("小品词").equals(s)) {
            return "RP";
        }
        if (("副词，最高级").equals(s)) {
            return "RBS";
        }
        if (("副词，比较级").equals(s)) {
            return "RBR";
        }
        if (("副词").equals(s)) {
            return "RB";
        }
        if (("物主代词").equals(s)) {
            return "PRP$";
        }
        if (("人称代词").equals(s)) {
            return "PRP";
        }
        if (("动词非第三人称单数").equals(s)) {
            return "VBP";
        }
        if (("过去分词").equals(s)) {
            return "VBN";
        }
        if (("现在分词").equals(s)) {
            return "VBG";
        }
        if (("动词过去式").equals(s)) {
            return "VBD";
        }
        if (("动词原形").equals(s)) {
            return "VB";
        }
        if (("感叹词").equals(s)) {
            return "UH";
        }
        if (("不定式引导词").equals(s)) {
            return "TO";
        }
        if (("符号（数学或科学）").equals(s)) {
            return "SYM";
        }
        if (("关系限定词").equals(s)) {
            return "WDTS";
        }
        if (("疑问副词").equals(s)) {
            return "WRB";
        }
        if (("疑问代词所有格").equals(s)) {
            return "WP$";
        }
        if (("疑问代词").equals(s)) {
            return "WP";
        }
        if (("疑问限定词").equals(s)) {
            return "WDT";
        }
        if (("动词第三人称单数").equals(s)) {
            return "VBZ";
        }
        if (("短语").equals(s)) {
            return "phrase";
        }
        if (("独立主格").equals(s)) {
            return "Zz";
        }
        if (("句型").equals(s)) {
            return "flow4";
        }
        if (("语气").equals(s)) {
            return "flow235d";
        }
        if (("句式").equals(s)) {
            return "235c";
        }
        if (("语态").equals(s)) {
            return "235b";
        }
        if (("时态").equals(s)) {
            return "flow1";
        }
        if (("关系副词").equals(s)) {
            return "WRBS";
        }
        if (("关系代词所有格").equals(s)) {
            return "WP$S";
        }
        if (("关系代词").equals(s)) {
            return "WPS";
        }

        if (("短语").equals(s)) {
            return "phrase";
        }
        if (("单词").equals(s)) {
            return "word";
        }


        return s;
    }

    public void initmap() {
        map1.put("GD", "独立主格");
        map1.put("LB", "并列句");
        map1.put("BV", "不定式");
        map1.put("CV", "省略to的不定式");
        map1.put("DV", "现在分词短语");
        map1.put("EV", "过去分词短语");
        map1.put("FV", "谓语动词");
        map1.put("ADJP", "形容词短语");
        map1.put("ADVP", "副词短语");
        map1.put("DP", "限定词短语");
        map1.put("FRAG", "不完整或有省略");
        map1.put("LCP*", "方位词短语");
        map1.put("NP", "名词短语");
        map1.put("PP", "介词短语");
        map1.put("PU*", "标点符号");
        map1.put("QP*", "量词短语");
        map1.put("SBAR", "从句");
        map1.put("WHADJP", "wh形容词短语");
        map1.put("WHADVP", "wh副词短语");
        map1.put("WHNP", "wh名词短语");
        map1.put("WHPP", "wh介词短语");
        map1.put("PRT", "小品词短语");
        map1.put("CONJP", "连接词短语");
        map1.put("INTJ", "感叹词");
        map1.put("PRN", "插入语");
        map1.put("UCP", "不对称的同位短语");
        map1.put("CC", "并列连接词");
        map1.put("CD", "基数");
        map1.put("DT", "限定词");
        map1.put("EX", "存在句引导词");
        map1.put("FW", "非英语单词");
        map1.put("IN", "介词或连接词");
        map1.put("INC", "从属连词");
        map1.put("JJ", "形容词");
        map1.put("JJR", "形容词，比较级");
        map1.put("JJS", "形容词，最高级");
        map1.put("LS*", "列表项标记");
        map1.put("MD", "情态动词");
        map1.put("NN", "名词");
        map1.put("NNS", "名词，复数");
        map1.put("NNP", "专有名词单数");
        map1.put("NNPS", "专有名词复数");
        map1.put("PDT", "限定词");
        map1.put("POS", "名词所有格符号");
        map1.put("PRP", "人称代词");
        map1.put("PRP$", "物主代词");
        map1.put("RB", "副词");
        map1.put("RBR", "副词，比较级");
        map1.put("RBS", "副词，最高级");
        map1.put("RP", "小品词");
        map1.put("SYM", "符号（数学或科学）");
        map1.put("TO", "不定式引导词");
        map1.put("UH", "感叹词");
        map1.put("VB", "动词原形");
        map1.put("VBD", "动词过去式");
        map1.put("VBG", "现在分词");
        map1.put("VBN", "过去分词");
        map1.put("VBP", "动词非第三人称单数");
        map1.put("VBZ", "动词第三人称单数");
        map1.put("WDT", "关系或疑问限定词");
        map1.put("WDTS", "关系限定词");
        map1.put("WP", "关系或疑问代词");
        map1.put("WPS", "关系代词");
        map1.put("WP$", "关系或疑问代词所有格");
        map1.put("WP$S", "关系代词所有格");
        map1.put("WRB", "关系或疑问副词");
        map1.put("WRBS", "关系副词");
        map2.put("flow1", "时态:");
        map2.put("flow235b", "语态:");
        map2.put("flow235c", "句式:");
        map2.put("flow235d", "语气:");
        map2.put("flow4", "句型:");
        map3.put("11", "一般现在时");
        map3.put("12", "一般过去时");
        map3.put("13", "一般将来时");
        map3.put("14", "过去将来时");
        map3.put("15", "现在完成时");
        map3.put("16", "过去完成时");
        map3.put("17", "将来完成时");
        map3.put("18", "过去将来完成时");
        map3.put("19", "现在进行时");
        map3.put("110", "过去进行时");
        map3.put("111", "将来进行时");
        map3.put("112", "过去将来进行时");
        map3.put("113", "现在完成进行时");
        map3.put("114", "过去完成进行时");
        map3.put("115", "将来完成进行时");
        map3.put("116", "过去将来完成进行时");
        map3.put("21", "there be存在句");
        map3.put("22", "there倒装句");
        map3.put("23", "it引导的强调句");
        map3.put("24", "it形式主语结构");
        map3.put("25", "it形式宾语结构");
        map3.put("26", "it宾语(间宾)结构");
        map3.put("31", "否定语气");
        map3.put("32", "do强调语气");
        map3.put("33", "虚拟语气");
        map3.put("41", "一般疑问句");
        map3.put("42", "反义疑问句");
        map3.put("43", "特殊疑问句");
        map3.put("44", "选择疑问句");
        map3.put("45", "倒装句");
        map3.put("46", "感叹句");
        map3.put("47", "祈使句");
        map3.put("48", "陈述句");
        map3.put("49", "句子不完整或有省略");
        map3.put("51", "被动语态");
    }
}