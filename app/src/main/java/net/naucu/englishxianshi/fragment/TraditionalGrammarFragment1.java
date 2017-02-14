package net.naucu.englishxianshi.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import net.naucu.englishxianshi.R;
import net.naucu.englishxianshi.adapter.TraditionalGrammarAdapter;
import net.naucu.englishxianshi.ui.MasaGrammarActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static android.R.attr.value;

public class TraditionalGrammarFragment1 extends Fragment {
    public static final String TAG = TraditionalGrammarFragment1.class.getSimpleName();
    private View view;
    private WebView webview;
    public ListView TGFlistview;
    public TraditionalGrammarAdapter adapter;
    private HashMap<String, String> map = new HashMap<String, String>();
    private HashMap<String, String> jutai = new HashMap<String, String>();
    private HashMap<String, String> juxing = new HashMap<String, String>();
    public List<Map<String, Object>> list = new ArrayList<>();
    public MasaGrammarActivity.BianSeCallBack bianSeCallBack;
    private boolean isWeb = false;
    private WebView webView;
    private String key;

    public TraditionalGrammarFragment1() {
    }

    public TraditionalGrammarFragment1(MasaGrammarActivity.BianSeCallBack bianSeCallBack) {
        this.bianSeCallBack = bianSeCallBack;
    }

    public TraditionalGrammarFragment1(MasaGrammarActivity.BianSeCallBack bianSeCallBack, String key) {
        this.bianSeCallBack = bianSeCallBack;
        this.key = key;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_yufact, container, false);
        webview = (WebView) view.findViewById(R.id.web_ctyufa);
        webview.setBackgroundColor(0);
        webview.setOverScrollMode(View.OVER_SCROLL_NEVER);
        TGFlistview = (ListView) view.findViewById(R.id.TGFlistview);
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setDefaultFontSize(18);
        initmap();
        Map<String, Object> valueMap;
        for (String v : map.values()) {
            valueMap = new HashMap<>();
            valueMap.put("value", v);
            list.add(valueMap);
        }
        for (String v : jutai.values()) {
            valueMap = new HashMap<>();
            valueMap.put("value", v);
            list.add(valueMap);
        }
        for (String v : juxing.values()) {
            valueMap = new HashMap<>();
            valueMap.put("value", v);
            list.add(valueMap);
        }
        if (key != null) {
            setWeb(key);
        }
        return view;
    }

    public void setKey(String key) {
        setOnitemclick();
        if (TGFlistview.getAdapter() == null) {
            webview.setVisibility(View.GONE);
            TGFlistview.setVisibility(View.VISIBLE);
            if (key == null) {
                key = "";
            }
            adapter = new TraditionalGrammarAdapter(getActivity(), getNeedList(key));
            TGFlistview.setAdapter(adapter);
        } else {
            adapter.setList(getNeedList(key));
            adapter.notifyDataSetChanged();
        }
    }

    private List<Map<String, Object>> getNeedList(String key) {
        List<Map<String, Object>> needList = new ArrayList<>();
        for (Map<String, Object> stringObjectMap : list) {
            String value = (String) stringObjectMap.get("value");
            if (value.contains(key)) {
                needList.add(stringObjectMap);
            }
        }
        return needList;
    }

    private void setOnitemclick() {
        TGFlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                bianSeCallBack.callback(TraditionalGrammarFragment1.class.getSimpleName());
                isWeb = true;
                adapter.setSelect(i);
                int index = adapterView.getAdapter().getItem(i).toString().lastIndexOf("=");
                String duibiKey = adapterView.getAdapter().getItem(i).toString().substring(index + 1, adapterView.getAdapter().getItem(i).toString().length() - 1);
                String ia = duiBi(duibiKey);
                if (ia.equals("PRN")) {
                    webview.loadUrl("file:///android_asset/PRN1.html");
                } else {
                    webview.loadUrl("file:///android_asset/" + ia + ".html");
                }
                TGFlistview.setVisibility(View.GONE);
                webview.setVisibility(View.VISIBLE);
                webview.setWebChromeClient(new WebChromeClient() {
                    @Override
                    public void onReceivedTitle(WebView view, String title) {
                        super.onReceivedTitle(view, title);

                    }
                });
                webview.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);
                        view.setVisibility(View.VISIBLE);
                        Log.i("GM", "显示2");
                        return super.shouldOverrideUrlLoading(view, url);
                    }

                    //当网页面加载失败时，会调用 这个方法，所以我们在这个方法中处理
                    @Override
                    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                        view.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "未找到相关内容", Toast.LENGTH_SHORT).show();
                    }
                });
                Log.i("TAG", "duibikey = " + duibiKey);
            }
        });
    }

    List<Map<String, Object>> kList = new ArrayList<>();
    Map<String, Object> kMap;

    private void showWeb(final String key) {
        webview.setVisibility(View.VISIBLE);
        isWeb = true;
        webview.setVisibility(View.VISIBLE);
        TGFlistview.setVisibility(View.GONE);
        if (key.length() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).get("value").equals(key)) {
                    kMap = new HashMap<>();
                    kMap.put("value", list.get(i).get("value").toString());
                    kList.add(kMap);
                    break;
                }
            }
            if (kList.size() > 0 && kList != null) {
                adapter = new TraditionalGrammarAdapter(getActivity(), kList);
            } else {
                Toast.makeText(getActivity(), "未找到相关内容", Toast.LENGTH_SHORT).show();
            }
        } else {
            adapter = new TraditionalGrammarAdapter(getActivity(), list);
        }
        TGFlistview.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    public void goneWebView() {
        if (webview != null) {
            webview.setVisibility(View.GONE);
        }
    }


    public void setWeb(String key) {
        TGFlistview.setVisibility(View.GONE);
        webview.setVisibility(View.VISIBLE);
        String ia = duiBi(key);
        if (ia.equals("PRN")) {
            webview.loadUrl("file:///android_asset/PRN1.html");
        } else {
            webview.loadUrl("file:///android_asset/" + ia + ".html");
        }
    }

    public boolean isWeb() {
        return isWeb;
    }

    public void setBack() {
        webview.setVisibility(View.GONE);
        TGFlistview.setVisibility(View.VISIBLE);
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
            return "PDT";
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
            return "LS";
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
            return "EV";
        }
        if (("现在分词").equals(s)) {
            return "DV";
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
            return "flow235a";
        }
        if (("句式").equals(s)) {
            return "flow235c";
        }
        if (("语态").equals(s)) {
            return "flow235b";
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
        map.put("GD", "独立主格");
        map.put("LB", "并列句");
        map.put("BV", "不定式");
        map.put("CV", "省略to的不定式");
        map.put("DV", "现在分词短语");
        map.put("EV", "过去分词短语");
        map.put("FV", "谓语动词");
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
        map.put("WP$", "关系或疑问代词所有格");
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
