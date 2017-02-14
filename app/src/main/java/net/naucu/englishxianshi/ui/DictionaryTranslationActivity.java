package net.naucu.englishxianshi.ui;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.Html;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;

import com.app.myTool.ActionTitleBarWidget;
import com.app.myTool.ActionTitleBarWidget.ClickListener;
import com.jiongbull.jlog.JLog;
import com.lijunsai.httpInterface.bean.BaiduTranslation;
import com.lijunsai.httpInterface.implementation.HttpImplementation;
import com.lijunsai.httpInterface.request.NetworkRequestDeal;

import net.naucu.englishxianshi.BaseApplication;
import net.naucu.englishxianshi.R;
import net.naucu.englishxianshi.adapter.MyDiaLogAdapter;
import net.naucu.englishxianshi.db.Manager;
import net.naucu.englishxianshi.db.dao.MyNote;
import net.naucu.englishxianshi.fragment.ElectronicdictionaryFragment;
import net.naucu.englishxianshi.fragment.GrammarAnalysisFragment;
import net.naucu.englishxianshi.fragment.GrammarAnalysisFragment.CallBackValue;
import net.naucu.englishxianshi.fragment.SentenceStructureFragment;
import net.naucu.englishxianshi.fragment.TranslationFragment;
import net.naucu.englishxianshi.fragment.WordFragment;
import net.naucu.englishxianshi.iflytek.TTSIflytek;
import net.naucu.englishxianshi.util.ErrorTool;
import net.naucu.englishxianshi.util.SharedPreTool;
import net.naucu.englishxianshi.util.ToastTool;
import net.naucu.englishxianshi.widget.view.FragmentView;
import net.naucu.englishxianshi.widget.window.AnswerReadWindow;

import org.json.JSONException;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import static android.view.MotionEvent.ACTION_DOWN;
import static net.naucu.englishxianshi.ui.DictionaryTranslationActivity.TranslateType.analyse;


/**
 * 翻译  语法解析 电子词典
 */
@ContentView(R.layout.activity_dictionarytranslation)
public class DictionaryTranslationActivity extends FragmentActivity implements OnClickListener, CallBackValue {
    public static final String TAG = DictionaryTranslationActivity.class.getSimpleName();

    public static String YUFA = "yufa";
    public static String YUJU = "yuju";
    public static String DANCI = "danci";

    private ActionTitleBarWidget titlebar;// 初始化标题控件
    private ImageView _translation;
    private ImageView _Grammartranslation;
    private ImageView _Electronicdictionary;
    private TranslationFragment translationFragment;// 翻译fragment
    private ElectronicdictionaryFragment electronicdictionaryFragment;// 电子词典fragment

    private GrammarAnalysisFragment grammarFragment;// 语法解析fragment
    private SentenceStructureFragment sentenceStructureFragment;// 语句结构fragment
    private WordFragment wordFragment;// 单词辨析fragment
    FragmentManager fm;
    FragmentTransaction fmt;
    boolean locked1 = false;
    private static TranslateType type;
    private boolean autoinput = true;
    private boolean locked = true;
    private static String content = "";
    private String inputtext = "";

    private TTSIflytek ttsiflytek;
    //0不作操作 1隐藏单词 2显示句子
    private static int returnType = 0;
    private int cd = 1;
    private WindowManager.LayoutParams lp;
    private View inflate;


    public void setReturnType(int returnType) {
        this.returnType = returnType;
    }

    private View view;
    @ViewInject(R.id.iv_read)
    private ImageView _read;
    @ViewInject(R.id.iv_resume)
    private ImageView _resume;
    @ViewInject(R.id.iv_search)
    private ImageView _search;
    @ViewInject(R.id.iv_hint)
    private ImageView _hint;
    @ViewInject(R.id.iv_lock)
    private ImageView _lock;
    @ViewInject(R.id.ed_inputword)
    private EditText _input;
    @ViewInject(R.id.ll_title)
    private LinearLayout _title;
    @ViewInject(R.id.yufa)
    private TextView yufa;
    @ViewInject(R.id.yuju)
    private TextView yuju;
    @ViewInject(R.id.danci)
    private TextView danci;
    @ViewInject(R.id.readlinelayout)
    private LinearLayout readlinelayout;
    @ViewInject(R.id.jiexiline)
    private LinearLayout jiexiline;
    @ViewInject(R.id.backs)
    private LinearLayout backs;
    @ViewInject(R.id.collection)
    private ImageView collection;
    @ViewInject(R.id.shares)
    private LinearLayout shares;
    // 复读机
    private AnswerReadWindow answerReadWindow;
    private ListView listView;
    private TextView dialog_cancel;


    private String fouceContent = "";
    private List<HashMap<ClickTextBean, String>> hashMapList = new ArrayList<>();
    private Integer curOff;
    private Window window;
    LayoutInflater layoutInflater;
    private List<String> list = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        x.view().inject(this);
        initView();
        initEvent();
        if (type != null) {
            setType(type);
        }
        if (locked) {
            _lock.setImageResource(R.drawable.suoding2);
        } else {
            _lock.setImageResource(R.drawable.suoding);
        }
        if (content != null) {
            getConnection(content.trim());
            if (locked) {
                _lock.setImageResource(R.drawable.suoding2);
            } else {
                _lock.setImageResource(R.drawable.suoding);
            }

        }
    }

//    private String filterStr(String s) {
//        if (s != null && s.length() > 0) {
//            StringBuilder buffer = new StringBuilder();
//            for (int i = 0; i < s.length(); i++) {
//                char c = s.charAt(i);
//                if (c != 35 && c != 42) {
//                    buffer.append(c);
//                }
//            }
//            return buffer.toString();
//        }
//        return "";
//    }

    public String filterAllStr(String str) {

        if (TextUtils.isEmpty(str)){//为空判断
            return "";
        }
        if (str.length() <= 0) {
            return "";
        } else {
            StringBuilder builder = new StringBuilder();
            char[] chars = str.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                char aChar = chars[i];
                if (aChar >= 33 && aChar <= 38
                        || aChar >= 40 && aChar <= 47
                        || aChar >= 58 && aChar <= 64) {
                    builder.append(aChar + " ");
                } if (aChar == 39){
                    char bChar = chars[i-1];
                    builder.append(" "+bChar);

                }else {
                    builder.append(aChar);
                }
            }
            return builder.toString();
        }
    }


  /* public void sendMessageValue(String str) {

        if (str == null || str.length() <= 0) {
            return;
        }
        String ss = _input.getText().toString();
        int start = 0;
        int end = 0;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == 91) {
                start = i;
            }
            if (c == 93) {
                end = i;
            }
        }
        int strStart = 0;
        int strEnd = 0;
        String needStr = str.substring(start + 1, end);
        if (needStr.contains("-")) {
            String[] str1 = needStr.split("-");
            strStart = Integer.parseInt(str1[0]) - 1;
            strEnd = Integer.parseInt(str1[1]) - 1;
        } else {
            strStart = Integer.parseInt(needStr) - 1;
            strEnd = Integer.parseInt(needStr) - 1;
        }
        String[] strs = ss.split(" ");
        String mid = "";
        String need = "";
        for (int i = strStart; i <= strEnd; i++) {
            if (i <= strs.length) {
                if (i == strs.length) {
                    break;
                } else {
                    need += "<font color=\"#06A33A\">" + strs[i] + " " + "</font>";
                }
            }
        }
        for (int j = 0; j < strs.length; j++) {
            if (j == strStart) {
                mid += need;
            } else {
                if (j < strStart || j > strEnd) {
                    mid += strs[j] + " ";
                }
            }
        }
        _input.setText(Html.fromHtml(mid));
    }*/

    //实现CallBackValue接口中的方法
    public void sendMessageValue(String str) {
        if (str == null || str.length() <= 0) {
            return;
        }
        int start=0;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == 91) {
                start = i;
            }
        }
        String splitStr=str.substring(0, start);
        String ss = _input.getText().toString();//获取输入框里的内容
        String need = "";
               need += "<font color=\"#06A33A\">" +splitStr+ " " + "</font>";
        String replaceStr=ss.replace(splitStr,need);

        _input.setText(Html.fromHtml(replaceStr));
    }

    float dx = 0, dy = 0;

    private void initView() {
        _translation = (ImageView) findViewById(R.id.br_translation);
        _Grammartranslation = (ImageView) findViewById(R.id.rb_Grammartranslation);
        _Electronicdictionary = (ImageView) findViewById(R.id.rb_Electronicdictionary);
    }

    private void initEvent() {
        backs.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent m) {
                switch (m.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        backs.setBackgroundColor(0x33000000);
                        break;
                    case MotionEvent.ACTION_UP:
                        backs.setBackgroundColor(0x00FFFFFF);
                        finish();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        backs.setBackgroundColor(0x33000000);
                        break;
                }
                return false;
            }
        });
        shares.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent m) {
                switch (m.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        shares.setBackgroundColor(0x33000000);
                        break;
                    case MotionEvent.ACTION_UP:
                        shares.setBackgroundColor(0x00FFFFFF);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        shares.setBackgroundColor(0x33000000);
                        break;
                }
                return false;
            }
        });
        collection.setOnClickListener(this);
        shares.setOnClickListener(this);
        _read.setOnClickListener(this);
        _resume.setOnClickListener(this);
        _hint.setOnClickListener(this);
        _lock.setOnClickListener(this);
        _search.setOnClickListener(this);
        yufa.setOnClickListener(this);
        yuju.setOnClickListener(this);
        danci.setOnClickListener(this);
        _translation.setOnClickListener(this);
        _Grammartranslation.setOnClickListener(this);
        _Electronicdictionary.setOnClickListener(this);
        _input.setOnTouchListener(new MyOnTouchListener());
    }

    public String getInputText() {
        return _input.getText().toString().trim();
    }

    public class MyOnTouchListener implements OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (locked) {
                switch (event.getAction()) {
                    case ACTION_DOWN:
                        if (locked) {
                            _input.setCursorVisible(false);
                        } else {
                            _input.setCursorVisible(true);
                        }
                        dx = event.getX();
                        dy = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        v.performClick();
                        if ((type == TranslateType.word) && electronicdictionaryFragment != null) {
                            float xx, yy;
                            xx = event.getX() - dx;
                            yy = event.getY() - dy;
                            if (xx * xx + yy * yy < 100) {
                                String[] selectWord = getSelectWord(_input.getEditableText(),
                                        extractWordCurOff(_input.getLayout(), (int) event.getX() - 30, (int) event.getY()));

                                inputtext = selectWord[0];
                                getConnection(inputtext);
                                if (inputtext != null && !inputtext.equals("")) {
                                    electronicdictionaryFragment.translate(inputtext);
                                }
                            }
                        }
                        if (type == TranslateType.sentence) {
                            if (translationFragment == null) {
                                translationFragment = new TranslationFragment();
                            }
                            FragmentManager fm = getSupportFragmentManager();
                            FragmentTransaction fmt = fm.beginTransaction();
                            fmt.replace(R.id.layout_frame, translationFragment);
                            fmt.commit();
                            float xx, yy;
                            xx = event.getX() - dx;
                            yy = event.getY() - dy;
                            if (xx * xx + yy * yy < 100) {
                                curOff = extractWordCurOff(_input.getLayout(), (int) event.getX() - 30, (int) event.getY());
                                String[] selectWord = getSelectWord(_input.getEditableText(), curOff);
                                fouceContent = selectWord[0];
                                getConnection(fouceContent);
                                if (hashMapList != null && hashMapList.size() > 0) {
                                    SpannableStringBuilder style02 = new SpannableStringBuilder(_input.getText().toString());
                                    for (int i = 0; i < hashMapList.size(); i++) {
                                        if ((Integer.valueOf(selectWord[1])
                                                == ((ClickTextBean) hashMapList.get(i).keySet().toArray()[0]).getStart()) &&
                                                (Integer.valueOf(selectWord[2])
                                                        == ((ClickTextBean) hashMapList.get(i).keySet().toArray()[0]).getEnd())) {

                                            hashMapList.remove(i);

                                            for (int j = 0; j < hashMapList.size(); j++) {
                                                style02.setSpan(new BackgroundColorSpan(Color.parseColor("#2E8593"))
                                                        , ((ClickTextBean) hashMapList.get(j).keySet().toArray()[0]).getStart()
                                                        , ((ClickTextBean) hashMapList.get(j).keySet().toArray()[0]).getEnd()
                                                        , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                            }
                                            _input.setText(style02);

                                            return locked;
                                        }
                                    }
                                }
                                HashMap<ClickTextBean, String> map = new HashMap<>();
                                map.put(new ClickTextBean(curOff, Integer.valueOf(selectWord[1])
                                                , Integer.valueOf(selectWord[2]))
                                        , fouceContent);
                                hashMapList.add(map);
                                SpannableStringBuilder style01 = new SpannableStringBuilder(_input.getText().toString());
                                for (int i = 0; i < hashMapList.size(); i++) {
                                    style01.setSpan(new BackgroundColorSpan(Color.parseColor("#2E8593"))
                                            , ((ClickTextBean) hashMapList.get(i).keySet().toArray()[0]).getStart()
                                            , ((ClickTextBean) hashMapList.get(i).keySet().toArray()[0]).getEnd()
                                            , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                }
                                _input.setText(style01);
                                for (int i = 0; i < hashMapList.size(); i++) {
                                    for (int j = 0; j < hashMapList.size() - 1 - i; j++) {
                                        if (((ClickTextBean) hashMapList.get(j).keySet().toArray()[0]).getCurOff()
                                                > ((ClickTextBean) hashMapList.get(j + 1).keySet().toArray()[0]).getCurOff()) {
                                            HashMap<ClickTextBean, String> hashMap = hashMapList.get(j);
                                            hashMapList.set(j, hashMapList.get(j + 1));
                                            hashMapList.set(j + 1, hashMap);
                                        }
                                    }
                                }

                            }
                        }
                        break;
                    default:
                        break;
                }
            }
            return locked;
        }
    }

    private boolean isAdd = false;

    private void setType(final TranslateType type) {
        _translation.setImageResource(R.drawable.translation_n);
        _Electronicdictionary.setImageResource(R.drawable.electronicdictionary_n);
        _Grammartranslation.setImageResource(R.drawable.syntaxanalysis_n);


        if ((type == TranslateType.word) && !autoinput) {
            _hint.setVisibility(View.VISIBLE);
        } else {
            _hint.setVisibility(View.GONE);
        }
        final FragmentManager fm = getSupportFragmentManager();
        final FragmentTransaction fmt = fm.beginTransaction();
        switch (type) {
            case sentence:
                _lock.setVisibility(View.VISIBLE);
                _translation.setImageResource(R.drawable.translation_f);
                readlinelayout.setVisibility(View.VISIBLE);
                jiexiline.setVisibility(View.GONE);
                if (content != null) {
                    _input.setText(content.trim());
                }
                if (!_input.getText().toString().equals("")) {
                    locked = true;
                    _lock.setImageResource(R.drawable.suoding2);
                    _input.setCursorVisible(false);

                } else {
                    locked = false;
                    _lock.setImageResource(R.drawable.suoding);
                    _input.setCursorVisible(true);
                }
                if (locked) {
                    if (translationFragment == null) {
                        translationFragment = new TranslationFragment();
                        fmt.replace(R.id.layout_frame, translationFragment);
                        fmt.commit();
                    }

                    if (content != null) {
                        translate(content.trim());
                    } else {
                        _input.setHint("请输入语句");
                    }
                    if (returnType == 2) {
                        _lock.setImageResource(R.drawable.suoding2);
                    }
                }
                break;
            case analyse:
                locked = false;
                _input.setEnabled(true);
                _input.setClickable(true);
                _input.setCursorVisible(true);

                _lock.setVisibility(View.INVISIBLE);
                _Grammartranslation.setImageResource(R.drawable.syntaxanalysis_f);
                readlinelayout.setVisibility(View.GONE);
                jiexiline.setVisibility(View.VISIBLE);
                if (sentenceStructureFragment == null)
                    sentenceStructureFragment = new SentenceStructureFragment(this);
                fmt.replace(R.id.layout_frame, sentenceStructureFragment);
                fmt.commit();
                if (content != null) {
                    _input.setText(content.trim());
                    translate(content.trim());
                } else {
                    _input.setHint("请输入语句");
                }
                if (returnType == 2) {
                    _lock.setImageResource(R.drawable.suoding2);
                }
                break;
            case word:
                _lock.setVisibility(View.VISIBLE);
                _Electronicdictionary.setImageResource(R.drawable.electronicdictionary_f);
                readlinelayout.setVisibility(View.VISIBLE);
                jiexiline.setVisibility(View.GONE);


                autoinput = SharedPreTool.getSharedPreDateBoolean(getApplicationContext(), "autoInput", true);

                if (!_input.getText().toString().equals("")) {
                    locked = true;
                    _lock.setImageResource(R.drawable.suoding2);
                    _input.setCursorVisible(false);
                } else {
                    locked = false;
                    _lock.setImageResource(R.drawable.suoding);
                    _input.setCursorVisible(true);
                }


                if (electronicdictionaryFragment == null)
                    electronicdictionaryFragment = new ElectronicdictionaryFragment();
                fmt.replace(R.id.layout_frame, electronicdictionaryFragment);
                fmt.commit();
                if (content != null) {
                    if (autoinput) {
                        _input.setText(content.trim());
                        translate(content.trim());
                        locked = true;
                    } else {
                        if (returnType == 1) {
                            locked1 = false;
                            locked = !locked;
                            if (locked) {
                                _lock.setImageResource(R.drawable.suoding2);
                            } else {
                                _lock.setImageResource(R.drawable.suoding);
                            }
                            //_input.setText(content);

                        }
                        locked = false;
                    }
                } else {
                    _input.setHint("请输入单词");
                }
                break;
            default:
                if (translationFragment == null)
                    translationFragment = new TranslationFragment();
                fmt.replace(R.id.layout_frame, translationFragment);
                fmt.commit();
                break;
        }
        getConnection(_input.getText().toString());
        if (!autoinput) {
            _input.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    String str = _input.getText().toString();
                    if (content == null || content.equals("") || content == "") {
                        return;
                    }
                    if (str.split(" ").length > 1) {
                        _input.setTextColor(getResources().getColor(R.color.read));
                        return;
                    }
                    str = str.replaceAll(" ", "");
                    if (str.equalsIgnoreCase(content)) {
                        _input.setTextColor(getResources().getColor(R.color.white));
                    } else {
                        if (fm.getFragments().toString().contains("lectronicdictionaryFragment")) {
                            _input.setTextColor(getResources().getColor(R.color.read));
                        } else {
                            _input.setTextColor(getResources().getColor(R.color.white));
                        }
                    }
                }
            });
        } else {
//            _input.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void onTextChanged(CharSequence s, int start, int before, int count) {
//                    if (s.length() > 0) {
//                        char c = s.charAt(s.length() - 1);
//                        if (c >= 32 && c <= 126) {
//                            if ((c >= 33 && c <= 47) || (c >= 58 && c <= 64) || (c >= 91 && c <= 96) || (c >= 123 && c <= 126)) {
//                                isAdd = true;
//                            }
//                        } else {
//                            Toast.makeText(DictionaryTranslationActivity.this, "非法字符", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                }
//
//                @Override
//                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                }
//
//                @Override
//                public void afterTextChanged(Editable s) {
//                    if (isAdd) {
//                        isAdd = false;
//                        _input.setText(s + " ");
//                        _input.setSelection(_input.getText().toString().length());
//
//                    }
//                }
//            });
        }
    }

    private ClickListener clickListener = new ClickListener() {

        @Override
        public void onright(View v) {
            ShareSDK.initSDK(DictionaryTranslationActivity.this);
            OnekeyShare oks = new OnekeyShare();
            //关闭sso授权
            oks.disableSSOWhenAuthorize();
            // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
            oks.setTitle("标题");
            // titleUrl是标题的网络链接，QQ和QQ空间等使用
            oks.setTitleUrl("http://sharesdk.cn");
            // text是分享文本，所有平台都需要这个字段
            String shareText=_input.getText().toString();
            if (TextUtils.isEmpty(shareText)){
                Toast.makeText(getApplicationContext(), "你分享的内容为空 ！", Toast.LENGTH_SHORT).show();
            }else {
                oks.setText(shareText);
            }
            // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
            //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
            // url仅在微信（包括好友和朋友圈）中使用
            oks.setUrl("http://sharesdk.cn");
            // comment是我对这条分享的评论，仅在人人网和QQ空间使用
            oks.setComment("我是测试评论文本");
            // site是分享此内容的网站名称，仅在QQ空间使用
            oks.setSite(getString(R.string.app_name));
            // siteUrl是分享此内容的网站地址，仅在QQ空间使用
            oks.setSiteUrl("http://sharesdk.cn");

            // 启动分享GUI
            oks.show(DictionaryTranslationActivity.this);
        }

        @Override
        public void onleft(View v) {
            finish();
        }

        @Override
        public void oncenter(View arg0) {// 收藏
            if (!_input.getText().toString().equals("") && (_input.getText().toString() != "")) {
                String content;
                content = _input.getText().toString();
                if (content.equals(""))
                    return;
                if (!getConnection(content.trim())) {
                    MyNote note = new MyNote();
                    note.setContent(content.trim());
                    note.setCreatTime(System.currentTimeMillis());
                    note.setType(type.value);
                    Manager.InsertNote(note);
                    titlebar.CenterViewRemove();
                    titlebar.setCenterIco(R.drawable.collectioned);
                    Toast.makeText(getApplicationContext(), "收藏成功", Toast.LENGTH_SHORT).show();
                } else {
                    Manager.DeleteNote(content.trim(), type.value);
                    titlebar.CenterViewRemove();
                    titlebar.setCenterIco(R.drawable.collection);
                    Toast.makeText(getApplicationContext(), "取消收藏", Toast.LENGTH_SHORT).show();
                }
                getConnection(content.trim());
            } else {
                if (cd == 1) {
                    Toast.makeText(DictionaryTranslationActivity.this, "请输入语句", Toast.LENGTH_SHORT).show();
                } else if (cd == 2) {
                    Toast.makeText(DictionaryTranslationActivity.this, "请输入语句", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DictionaryTranslationActivity.this, "请输入单词", Toast.LENGTH_SHORT).show();
                }

            }

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        content = filterAllStr(content);
        if (answerReadWindow == null) {
            answerReadWindow = new AnswerReadWindow(this);
            answerReadWindow.setCloseCallBack(new AnswerReadWindow.OnCloseCallBack() {
                @Override
                public void replacePic() {
                    _resume.setImageResource(R.drawable.yuyin);
                }
            });
            answerReadWindow.setOnDismissListener(new OnDismissListener() {
                @Override
                public void onDismiss() {
                    answerReadWindow.stopPlayer();
                }
            });
        }
        JPushInterface.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
        if (ttsiflytek != null && ttsiflytek.getSpeechSynthesizer().isSpeaking()) {
            ttsiflytek.getSpeechSynthesizer().stopSpeaking();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        answerReadWindow.dismiss();
    }

    public RequestParams translateParams(String something) {
        RequestParams params = new RequestParams(HttpImplementation.getBaiduTranslation());
        params.addBodyParameter("info",
                "{\"id\":\"" + ((BaseApplication) getApplication()).getLoginApplication().getUid() + "\",\"packag\":\""
                        + getPackageName() + "\",\"something\":\"" + something + "\"}");
        return params;
    }

    private void BaiduTranslation(String something) {
        RequestParams params = new RequestParams(HttpImplementation.getBaiduTranslation() + "?");
        params.addBodyParameter("info",
                "{\"id\":\"" + ((BaseApplication) getApplication()).getLoginApplication().getUid() + "\",\"packag\":\""
                        + getPackageName() + "\",\"something\":\"" + something + "\"}");
        x.http().get(params, new CommonCallback<String>() {

            @Override
            public void onCancelled(CancelledException e) {
                JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());
            }

            @Override
            public void onError(Throwable throwable, boolean isOnCallback) {
                ErrorTool.onError(DictionaryTranslationActivity.this, isOnCallback);
                JLog.e((throwable == null || throwable.getMessage() == null) ? "Throwable is null" : throwable.getMessage());
            }

            @Override
            public void onFinished() {
            }

            @Override
            public void onSuccess(String result) {

                try {
                    if (NetworkRequestDeal.isErrCode(result)) {
                        switch (NetworkRequestDeal.getErrCode(result)) {
                            case 1001:
                                ToastTool.showToastLong(DictionaryTranslationActivity.this, "该用户没有支付!");
                                break;
                            case 1002:
                                ToastTool.showToastLong(DictionaryTranslationActivity.this, "请检查特殊字符");
                                break;
                            case 1003:
                                ToastTool.showToastLong(DictionaryTranslationActivity.this, "服务器错误!");
                                break;
                        }
                    } else {
                        BaiduTranslation baiduTranslation = NetworkRequestDeal.getBaiduTranslation(result);
                        if (baiduTranslation != null && translationFragment != null) {
                            translationFragment.setResult(baiduTranslation.getDst());
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    String dialogChoice = "";
    String dialogChoice2 = "";

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_read:
                if (!_input.getText().toString().equals("")) {
                    if (ttsiflytek == null) {
                        ttsiflytek = new TTSIflytek(this);
                    }
                    ttsiflytek.set_read(_read, 1);
                    if (ttsiflytek != null && ttsiflytek.getSpeechSynthesizer().isSpeaking()) {
                        ttsiflytek.getSpeechSynthesizer().stopSpeaking();
                        _read.setImageResource(R.drawable.yu);
                    } else {
                        if (type == TranslateType.word && inputtext != null && !inputtext.equals("")) {
                            ttsiflytek.setTextflytek(inputtext);
                        } else {
                            ttsiflytek.setTextflytek(_input.getText().toString());
                        }
                    }
                } else {
                    Toast.makeText(DictionaryTranslationActivity.this, "请输入要朗读的内容", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.iv_resume:
                if (locked1) {
                    _resume.setImageResource(R.drawable.yuyin);
                } else {
                    _resume.setImageResource(R.drawable.bianse2);
                }
                locked1 = !locked1;
                if (answerReadWindow.isShowing()) {
                    answerReadWindow.dismiss();
                } else {
//                    findViewById(R.id.layout_frame).setVisibility(View.GONE);
                    answerReadWindow.showAtLocation(_resume, Gravity.BOTTOM, 0, 0);
                }
                break;
            case R.id.iv_search:
                if (type == TranslateType.word) {
                    if (!_input.getText().toString().trim().equals("")) {
                        String s = _input.getText().toString();
                        for (int i = 0; i < s.length(); i++) {
                            int c = s.charAt(i);
                            if (c < 32 || c > 126) {
                                Toast.makeText(this, "请选择单词进行查询", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                        findViewById(R.id.layout_frame).setVisibility(View.VISIBLE);
                        if (_title.getVisibility() != View.VISIBLE) {
                            titlebar.setVisibility(View.VISIBLE);
                            _title.setVisibility(View.VISIBLE);
                            _search.setImageResource(R.drawable.rzsearchssend);
                        } else {
                            translate(_input.getText().toString().trim());
                        }
                    } else {
                        Toast.makeText(DictionaryTranslationActivity.this, "请输入要翻译的内容", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (!_input.getText().toString().equals("") && hashMapList.size() == 0) {
                        findViewById(R.id.layout_frame).setVisibility(View.VISIBLE);
                        if (_title.getVisibility() != View.VISIBLE) {
                            titlebar.setVisibility(View.VISIBLE);
                            _title.setVisibility(View.VISIBLE);
                            _search.setImageResource(R.drawable.rzsearchssend);
                        } else {
                            translate(_input.getText().toString());
                        }

                    } else if (!_input.getText().toString().equals("") && hashMapList.size() != 0) {
                        String str = "";
                        for (int i = 0; i < hashMapList.size(); i++) {
                            str += hashMapList.get(i).values().toArray()[0] + " ";
                        }
                        translate(str);
                    } else {
                        Toast.makeText(DictionaryTranslationActivity.this, "请输入要翻译的内容", Toast.LENGTH_SHORT).show();
                    }
                }


                break;
            case R.id.iv_lock:
                locked = !locked;
                if (locked) {
                    _lock.setImageResource(R.drawable.suoding2);
                    _input.setCursorVisible(true);
                    _input.setFocusable(true);
                    _input.setFocusableInTouchMode(true);
                    _input.requestFocus();
                } else {
                    _lock.setImageResource(R.drawable.suoding);
                    _input.setCursorVisible(false);
                }
                break;
            case R.id.iv_hint:
                if (content != null) {
                    AlertDialog.Builder normalDia = new AlertDialog.Builder(this);
                    normalDia.setMessage(content.trim());
                    normalDia.create().show();
                } else {
                    Toast toast = Toast.makeText(this, "本次没有提醒内容", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                break;
            case R.id.yufa:
                danci.setText("单词辨析");
                String s = _input.getText().toString();
                StringBuilder strBuilder = new StringBuilder();
                for (int i = 0; i <s.length() ; i++) {
                    char c = s.charAt(i);
                    char c1 = 0;
                    if (i > 0) {
                        c1 = s.charAt(i - 1);
                    }
                    if (c==35||c==42){//如句中有#或者*等符号，不参与解析

                    }else if (c == 39 && c1 != 32) {
                        strBuilder.append(" " + c);
                    } else {
                        strBuilder.append(c);
                    }
                }
                s = strBuilder.toString();
                _input.setText(s);
                if (s.equals("")) {
                    Toast.makeText(this, "请输入语句", Toast.LENGTH_SHORT).show();
                    return;
                }
                int a = 0;
                char[] chars = s.toCharArray();
                List<String> stringList = new ArrayList<>();
                for (int i = 0; i < chars.length; i++) {
                    if (chars[i] == 45 && i > 0) {
                        if (chars[i - 1] == 33 || chars[i - 1] == 34 || chars[i - 1] == 39 || chars[i - 1] == 44 ||
                                chars[i - 1] == 58 || chars[i - 1] == 59 || chars[i - 1] == 63 || chars[i - 1] == 46 ||
                                chars[i - 2] == 33 || chars[i - 2] == 34 || chars[i - 2] == 39 || chars[i - 2] == 44 ||
                                chars[i - 2] == 58 || chars[i - 2] == 59 || chars[i - 2] == 63 || chars[i - 2] == 46

                                ) {
                            stringList.add(s.substring(a, i - 1));
                            a = i;
                        }
                    }
                    if (i == chars.length - 1 && !s.substring(a).equals("")) {
                        stringList.add(s.substring(a));
                    }
                }
                List<String> tempList01 = new ArrayList<>();
                if (stringList.size() != 0) {
                    for (String temp : stringList) {
                        if (temp.contains("?")) {
                            String[] split = temp.split("[?]");
                            for (int i = 0; i < split.length; i++) {
                                if (i == 0) {
                                    tempList01.add(split[0] + "?");
                                } else {
                                    tempList01.add(split[i]);
                                }
                            }

                            continue;
                        }
                        tempList01.add(temp);
                    }
                }
                List<String> tempList02 = new ArrayList<>();
                if (tempList01.size() != 0) {
                    for (String temp2 :
                            tempList01) {
                        if (temp2.contains("!")) {
                            String[] split2 = temp2.split("!");
                            for (int i = 0; i < split2.length; i++) {
                                if (i == 0) {
                                    tempList02.add(split2[0] + "!");
                                } else {
                                    tempList02.add(split2[i]);
                                }
                            }

                            continue;
                        }
                        tempList02.add(temp2);
                    }
                }
                final List<String> tempList03 = new ArrayList<>();
                if (tempList02.size() != 0) {

                    for (String temp3 : tempList02) {
                        int start = 0;
                        String[] split = temp3.split("\\.");
                        for (String temp4 : split) {
                            String[] split1 = (temp4.trim() + ".").split(" ");
                            if (split1.length > 0 && split1[split1.length - 1].toCharArray()[0] >= 61 &&
                                    split1[split1.length - 1].toCharArray()[0] <= 122 &&
                                    split1[split1.length - 1].toCharArray()[split1[split1.length - 1].length() - 1] == 46) {
                                int end = temp3.indexOf(temp4);
                                tempList03.add(temp3.substring(start, end).trim());
                                start = end;
                            }
                        }
                        tempList03.add(temp3.substring(start).trim());
                    }
                    for (int i = 0; i < tempList03.size(); i++) {
                        if (tempList03.get(i).equals("") || tempList03.get(i).equals(" ")) {
                            tempList03.remove(i);
                        }
                    }
                    if (tempList03.size() > 1) {
                        for (int i = 0; i < tempList03.size() - 1; i++) {

                            char[] chars1 = tempList03.get(i).toCharArray();
                            char[] chars2 = tempList03.get(i + 1).toCharArray();
                            int c = 0;
                            for (int j = 0; j < chars2.length; j++) {
                                if (chars2[j] == 32) {
                                    c++;
                                }
                            }
                            if (c == 0 && chars1.length > 0 && chars1[chars1.length - 1] == 46) {
                                tempList03.set(i, tempList03.get(i) + tempList03.get(i + 1));
                                tempList03.remove(i + 1);
                            }
                        }
                    }
                }
                for (int i = 0; i < tempList03.size(); i++) {
                    String s1 = tempList03.get(i).replaceAll("\\-", "");
                    tempList03.set(i, s1.trim());
                }
                String[] strings = new String[tempList03.size()];
                String[] toArray = tempList03.toArray(strings);
                AlertDialog.Builder builder = new AlertDialog.Builder(DictionaryTranslationActivity.this);

                layoutInflater = LayoutInflater.from(DictionaryTranslationActivity.this);
                inflate = layoutInflater.inflate(R.layout.dialog_list, null);
                listView = (ListView) inflate.findViewById(R.id.dialog_list);
                dialog_cancel = (TextView) inflate.findViewById(R.id.dialog_cancel);
                builder.setView(inflate);
                final AlertDialog dialog = builder.create();
                dialog_cancel.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    }
                });
                listView.setAdapter(new MyDiaLogAdapter(this, tempList03));
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        _input.setText(tempList03.get(i));
                        String b = _input.getText().toString();
                        if (b.length() > 0 && b.split(" ").length < 40) {
                            String input = StringFilter(b);
                            if (!input.equals("")) {
                                yufa.setTextColor(0xff1BE2E9);
                                yuju.setTextColor(0xffFFFFFF);
                                danci.setTextColor(0xffFFFFFF);
                                if (grammarFragment == null)
                                    grammarFragment = new GrammarAnalysisFragment();
                                fm = getSupportFragmentManager();
                                while (fm.getBackStackEntryCount() > 0) {
                                    fm.popBackStackImmediate();
                                }
                                fmt = fm.beginTransaction();
                                fmt.replace(R.id.layout_frame, grammarFragment);
                                fmt.commit();
                                char[] chars1 = input.toCharArray();
                                String buffer = "";
                                for (int j = 0; j < chars1.length; j++) {
                                    if ((chars1[j] >= 33 && chars1[j] <= 38 || chars1[j] > 39 && chars1[j] <= 47 || chars1[j] >= 58 &&
                                            chars1[j] <= 64 || chars1[j] >= 91 && chars1[j] <= 95)
                                            && ((j + 1) < chars1.length && chars1[j + 1] != 32)) {
                                        buffer += String.valueOf(chars1[j]) + " ";
                                    } else {
                                        if (j < chars1.length) {
                                            buffer += String.valueOf(chars1[j]);
                                        }
                                    }
                                }
                                _input.setText(buffer);
                                grammarFragment.getData(buffer.trim(), DictionaryTranslationActivity.this);
                            } else {
                                Toast.makeText(DictionaryTranslationActivity.this, "请输入语句", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(DictionaryTranslationActivity.this, "单词过多，请重新选择后再进行查询", Toast.LENGTH_SHORT).show();
                        }

                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    }
                });


//                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(DictionaryTranslationActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
//                dialogChoice = tempList03.get(0);
//                alertBuilder.setSingleChoiceItems(toArray, 0, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialogChoice = tempList03.get(which);
//                    }
//                });
//                alertBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        if (!dialogChoice.equals("")) {
//                            _input.setText(dialogChoice);
//                            String b = _input.getText().toString();
//                            if (b.length() > 0 && b.split(" ").length < 40) {
//                                String input = StringFilter(b);
//                                if (!input.equals("")) {
//                                    yufa.setTextColor(0xff1BE2E9);
//                                    yuju.setTextColor(0xffFFFFFF);
//                                    danci.setTextColor(0xffFFFFFF);
//                                    if (grammarFragment == null)
//                                        grammarFragment = new GrammarAnalysisFragment();
//                                    fm = getSupportFragmentManager();
//                                    while (fm.getBackStackEntryCount() > 0) {
//                                        fm.popBackStackImmediate();
//                                    }
//                                    fmt = fm.beginTransaction();
//                                    fmt.replace(R.id.layout_frame, grammarFragment);
//                                    fmt.commit();
//                                    char[] chars1 = input.toCharArray();
//                                    String buffer = "";
//                                    for (int i = 0; i < chars1.length; i++) {
//                                        if ((chars1[i] >= 33 && chars1[i] <= 38 || chars1[i] > 39 && chars1[i] <= 47 || chars1[i] >= 58 &&
//                                                chars1[i] <= 64 || chars1[i] >= 91 && chars1[i] <= 95)
//                                                && ((i + 1) < chars1.length && chars1[i + 1] != 32)) {
//                                            buffer += String.valueOf(chars1[i]) + " ";
//                                        } else {
//                                            if (i < chars1.length) {
//                                                buffer += String.valueOf(chars1[i]);
//                                            }
//                                        }
//                                    }
//                                    _input.setText(buffer);
//                                    grammarFragment.getData(buffer.trim(), DictionaryTranslationActivity.this);
//                                } else {
//                                    Toast.makeText(DictionaryTranslationActivity.this, "请输入语句", Toast.LENGTH_SHORT).show();
//                                }
//                            } else {
//                                Toast.makeText(DictionaryTranslationActivity.this, "单词过多，请重新选择后再进行查询", Toast.LENGTH_SHORT).show();
//                            }
//
//                        }
//                        tempList03.clear();
//                        dialog.dismiss();
//                    }
//                });

                if (tempList03.size() > 1) {
                    window = dialog.getWindow();
                    if (window != null) {
                        lp = window.getAttributes();
                        window.setGravity(Gravity.CENTER);
                        lp.verticalMargin = 15f;
                        window.setAttributes(lp);
                    }
                    dialog.show();
                } else {
                    s = _input.getText().toString();
                    if (s.length() > 0 && s.split(" ").length < 40) {
                        String input = StringFilter(s);
                        if (!input.equals("")) {
                            yufa.setTextColor(0xff1BE2E9);
                            yuju.setTextColor(0xffFFFFFF);
                            danci.setTextColor(0xffFFFFFF);
                            if (grammarFragment == null)
                                grammarFragment = new GrammarAnalysisFragment();
                            fm = getSupportFragmentManager();
                            while (fm.getBackStackEntryCount() > 0) {
                                fm.popBackStackImmediate();
                            }
                            fmt = fm.beginTransaction();
                            fmt.replace(R.id.layout_frame, grammarFragment);
                            fmt.commit();
                            char[] chars01 = input.toCharArray();
                            String buffer = "";
                            for (int i = 0; i < chars01.length; i++) {
                                if ((chars01[i] >= 33 && chars01[i] <= 38 || chars01[i] > 39 && chars01[i] <= 47 || chars01[i] >= 58 &&
                                        chars01[i] <= 64 || chars01[i] >= 91 && chars01[i] <= 95)
                                        && ((i + 1) < chars01.length && chars01[i + 1] != 32)) {
                                    buffer += String.valueOf(chars01[i]) + " ";
                                } else {
                                    if (i < chars01.length) {
                                        buffer += String.valueOf(chars01[i]);
                                    }

                                }
                            }
                            _input.setText(buffer);
                            grammarFragment.getData(buffer, this);
                        } else {
                            Toast.makeText(this, "请输入语句", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "单词过多，请重新选择后再进行查询", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.yuju:
                danci.setText("单词辨析");
                String s2 = _input.getText().toString();
                StringBuilder strBuilder2 = new StringBuilder();
                for (int i = 0; i <s2.length() ; i++) {
                    char c = s2.charAt(i);
                    char c1 = 0;
                    if (i > 0) {
                        c1 = s2.charAt(i - 1);
                    }
                    if (c == 39&&c1!=32){
                        strBuilder2.append(" " + c);
                    }else{
                        strBuilder2.append(c);
                    }
                }
                if (s2.equals("")) {
                    Toast.makeText(DictionaryTranslationActivity.this, "请输入语句", Toast.LENGTH_SHORT).show();
                    return;
                }
                int a1 = 0;
                char[] chars1 = s2.toCharArray();
                List<String> stringList11 = new ArrayList<>();
                for (int i = 0; i < chars1.length; i++) {
                    if (chars1[i] == 45 && i > 0) {
                        if (chars1[i - 1] == 33 || chars1[i - 1] == 34 || chars1[i - 1] == 39 || chars1[i - 1] == 44 ||
                                chars1[i - 1] == 58 || chars1[i - 1] == 59 || chars1[i - 1] == 63 || chars1[i - 1] == 46 ||
                                chars1[i - 2] == 33 || chars1[i - 2] == 34 || chars1[i - 2] == 39 || chars1[i - 2] == 44 ||
                                chars1[i - 2] == 58 || chars1[i - 2] == 59 || chars1[i - 2] == 63 || chars1[i - 2] == 46

                                ) {
                            stringList11.add(s2.substring(a1, i - 1));
                            a1 = i;
                        }
                    }
                    if (i == chars1.length - 1 && !s2.substring(a1).equals("")) {
                        stringList11.add(s2.substring(a1));
                    }
                }
                List<String> tempList22 = new ArrayList<>();
                if (stringList11.size() != 0) {
                    for (String temp : stringList11) {
                        if (temp.contains("?")) {
                            String[] split = temp.split("[?]");
                            for (int i = 0; i < split.length; i++) {
                                if (i == 0) {
                                    tempList22.add(split[0] + "?");
                                } else {
                                    tempList22.add(split[i]);
                                }
                            }
                            continue;
                        }
                        tempList22.add(temp);
                    }
                }
                List<String> tempList33 = new ArrayList<>();
                if (tempList22.size() != 0) {
                    for (String temp2 :
                            tempList22) {
                        if (temp2.contains("!")) {
                            String[] split2 = temp2.split("!");
                            for (int i = 0; i < split2.length; i++) {
                                if (i == 0) {
                                    tempList33.add(split2[0] + "!");
                                } else {
                                    tempList33.add(split2[i]);
                                }
                            }
                            continue;
                        }
                        tempList33.add(temp2);
                    }
                }
                final List<String> tempList44 = new ArrayList<>();
                if (tempList33.size() != 0) {
                    for (String temp3 : tempList33) {
                        int start = 0;
                        String[] split = temp3.split("\\.");
                        for (String temp4 : split) {
                            String[] split1 = (temp4.trim() + ".").split(" ");
                            if (split1.length > 0 && split1[split1.length - 1].toCharArray()[0] >= 61 &&
                                    split1[split1.length - 1].toCharArray()[0] <= 122 &&
                                    split1[split1.length - 1].toCharArray()[split1[split1.length - 1].length() - 1] == 46) {
                                int end = temp3.indexOf(temp4);
                                tempList44.add(temp3.substring(start, end).trim());
                                start = end;
                            }
                        }
                        tempList44.add(temp3.substring(start).trim());
                    }
                }
                for (int i = 0; i < tempList44.size(); i++) {
                    if (tempList44.get(i).equals("") || tempList44.get(i).equals(" ")) {
                        tempList44.remove(i);
                    }
                }
                if (tempList44.size() > 1) {
                    for (int i = 0; i < tempList44.size() - 1; i++) {

                        char[] chars3 = tempList44.get(i).toCharArray();
                        char[] chars4 = tempList44.get(i + 1).toCharArray();
                        int c = 0;
                        for (int j = 0; j < chars4.length; j++) {
                            if (chars4[j] == 32) {
                                c++;
                            }
                        }
                        if (c == 0 && chars3.length > 0 && chars3[chars3.length - 1] == 46) {
                            tempList44.set(i, tempList44.get(i) + tempList44.get(i + 1));
                            tempList44.remove(i + 1);
                        }
                    }
                }

                for (int i = 0; i < tempList44.size(); i++) {
                    String s1 = tempList44.get(i).replaceAll("\\-", "");
                    tempList44.set(i, s1.trim());
                }
                String[] strings1 = new String[tempList44.size()];
                String[] toArray1 = tempList44.toArray(strings1);

                AlertDialog.Builder builder1 = new AlertDialog.Builder(DictionaryTranslationActivity.this);


                layoutInflater = LayoutInflater.from(DictionaryTranslationActivity.this);
                inflate = layoutInflater.inflate(R.layout.dialog_list, null);
                listView = (ListView) inflate.findViewById(R.id.dialog_list);
                dialog_cancel = (TextView) inflate.findViewById(R.id.dialog_cancel);
                builder1.setView(inflate);
                final AlertDialog dialog1 = builder1.create();
                dialog_cancel.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog1.dismiss();
                    }
                });
                listView.setAdapter(new MyDiaLogAdapter(this, tempList44));
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        _input.setText(tempList44.get(i));
                        String b = _input.getText().toString();
                        if (b.length() > 0 && b.split(" ").length < 40) {
                            String input = StringFilter(b);
                            if (!input.equals("")) {
                                yufa.setTextColor(0xff1BE2E9);
                                yuju.setTextColor(0xffFFFFFF);
                                danci.setTextColor(0xffFFFFFF);
                                if (grammarFragment == null)
                                    grammarFragment = new GrammarAnalysisFragment();
                                fm = getSupportFragmentManager();
                                while (fm.getBackStackEntryCount() > 0) {
                                    fm.popBackStackImmediate();
                                }
                                fmt = fm.beginTransaction();
                                fmt.replace(R.id.layout_frame, grammarFragment);
                                fmt.commit();
                                char[] chars1 = input.toCharArray();
                                String buffer = "";
                                for (int j = 0; j < chars1.length; j++) {
                                    if ((chars1[j] >= 33 && chars1[j] <= 38 || chars1[j] > 39 && chars1[j] <= 47 || chars1[j] >= 58 &&
                                            chars1[j] <= 64 || chars1[j] >= 91 && chars1[j] <= 95)
                                            && ((j + 1) < chars1.length && chars1[j + 1] != 32)) {
                                        buffer += String.valueOf(chars1[j]) + " ";
                                    } else {
                                        if (j < chars1.length) {
                                            buffer += String.valueOf(chars1[j]);
                                        }
                                    }
                                }
                                _input.setText(buffer);
                                grammarFragment.getData(buffer.trim(), DictionaryTranslationActivity.this);
                            } else {
                                Toast.makeText(DictionaryTranslationActivity.this, "请输入语句", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(DictionaryTranslationActivity.this, "单词过多，请重新选择后再进行查询", Toast.LENGTH_SHORT).show();
                        }
                        dialog1.dismiss();
                    }
                });


//                AlertDialog.Builder alertBuilder1 = new AlertDialog.Builder(DictionaryTranslationActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
//                alertBuilder1.setCustomTitle(inflate);
//                dialogChoice2 = tempList44.get(0);
//                alertBuilder1.setSingleChoiceItems(toArray1, 0, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialogChoice2 = tempList44.get(which);
//                    }
//                });
//                alertBuilder1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        if (!dialogChoice2.equals("")) {
//                            _input.setText(dialogChoice2);
//                            if (_input.getText().toString().length() > 0 && _input.getText().toString().split(" ").length < 40) {
//                                content = StringFilter(_input.getText().toString());
//                                if (!content.equals("") && content != null) {
//                                    yufa.setTextColor(0xffFFFFFF);
//                                    yuju.setTextColor(0xff1BE2E9);
//                                    danci.setTextColor(0xffFFFFFF);
//                                    if (sentenceStructureFragment == null)
//                                        sentenceStructureFragment = new SentenceStructureFragment(DictionaryTranslationActivity.this);
//                                    fm = getSupportFragmentManager();
//                                    while (fm.getBackStackEntryCount() > 0) {
//                                        fm.popBackStackImmediate();
//                                    }
//                                    fmt = fm.beginTransaction().replace(R.id.layout_frame, sentenceStructureFragment);
//                                    fmt.commit();
//                                    char[] chars11 = content.toCharArray();
//                                    String buffer = "";
//                                    for (int i = 0; i < chars11.length; i++) {
//                                        if ((chars11[i] >= 33 && chars11[i] <= 38 || chars11[i] > 39 && chars11[i] <= 47 || chars11[i] >= 58 &&
//                                                chars11[i] <= 64 || chars11[i] >= 91 && chars11[i] <= 95)
//                                                && ((i + 1) < chars11.length && chars11[i + 1] != 32)) {
//                                            buffer += String.valueOf(chars11[i]) + " ";
//                                        } else {
//                                            if (i < chars11.length - 1) {
//                                                buffer += String.valueOf(chars11[i]);
//                                            }
//
//                                        }
//                                    }
//                                    _input.setText(buffer);
//                                    sentenceStructureFragment.analyse(buffer.trim());
//                                } else {
//                                    Toast.makeText(DictionaryTranslationActivity.this, "请输入语句", Toast.LENGTH_SHORT).show();
//                                }
//                            } else {
//                                Toast.makeText(DictionaryTranslationActivity.this, "单词过多，请重新选择后再进行查询", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    }
//                });
//                alertBuilder1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
                if (tempList44.size() > 1) {
                    window = dialog1.getWindow();
                    if (window != null) {
                        lp = window.getAttributes();
                        window.setGravity(Gravity.CENTER);
                        lp.verticalMargin = 15f;
                        window.setAttributes(lp);
                    }
                    dialog1.show();
                } else {
                    if (_input.getText().toString().length() > 0 && _input.getText().toString().split(" ").length < 40) {
                        content = StringFilter(_input.getText().toString());
                        if (!content.equals("") && content != null) {
                            yufa.setTextColor(0xffFFFFFF);
                            yuju.setTextColor(0xff1BE2E9);
                            danci.setTextColor(0xffFFFFFF);
                            if (sentenceStructureFragment == null)
                                sentenceStructureFragment = new SentenceStructureFragment(this);
                            fm = getSupportFragmentManager();
                            while (fm.getBackStackEntryCount() > 0) {
                                fm.popBackStackImmediate();
                            }
                            fmt = fm.beginTransaction()
                                    .replace(R.id.layout_frame, sentenceStructureFragment);
                            fmt.commit();
                            char[] chars22 = content.toCharArray();
                            String buffer = "";
                            for (int i = 0; i < chars1.length; i++) {
                                if (((i + 1) < chars22.length && chars22[i + 1] != 32)
                                        && (chars22[i] >= 33 && chars22[i] <= 38
                                        || chars22[i] > 39 && chars22[i] <= 47
                                        || chars22[i] >= 58 && chars22[i] <= 64
                                        || chars22[i] >= 91 && chars22[i] <= 95)
                                        ) {
                                    buffer += String.valueOf(chars22[i]) + " ";
                                } else {
                                    if (i < chars1.length - 1) {
                                        buffer += String.valueOf(chars22[i]);
                                    }
                                }
                            }
                            _input.setText(buffer);
                            sentenceStructureFragment.analyse(buffer.trim());
                        } else {
                            Toast.makeText(this, "请输入语句", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "单词过多，请重新选择后再进行查询", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.danci:
                danci.setText("单词辨析");
                content = StringFilter(_input.getText().toString());
                if (!content.equals("") && content != null) {
                    yufa.setTextColor(0xffFFFFFF);
                    yuju.setTextColor(0xffFFFFFF);
                    danci.setTextColor(0xff1BE2E9);
                    if (wordFragment == null)
                        wordFragment = WordFragment.getInstance(_input.getText().toString(), this, bianSeCallBack);
                    if (wordFragment.isWeb()) {
                        wordFragment.setBack();
                    }
                    fm = getSupportFragmentManager();
                    while (fm.getBackStackEntryCount() > 0) {
                        fm.popBackStackImmediate();
                    }
                    fmt = fm.beginTransaction();
                    fmt.replace(R.id.layout_frame, wordFragment);
                    fmt.commit();
                    wordFragment.analyse(content.trim());
                } else {
                    Toast.makeText(this, "请输入单词", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.br_translation:
                if (!_input.getText().toString().equals("")) {
                    locked = true;
                    _input.setCursorVisible(false);
                } else {
                    locked = false;
                    _input.setCursorVisible(true);
                }
                Selection.removeSelection(_input.getEditableText());
                cd = 1;
                locked1 = false;
                _resume.setImageResource(R.drawable.yuyin);
                if (answerReadWindow != null && answerReadWindow.isShowing()) {
                    answerReadWindow.dismiss();
                }
                _translation.setImageResource(R.drawable.translation_n);
                _Electronicdictionary.setImageResource(R.drawable.electronicdictionary_n);
                _Grammartranslation.setImageResource(R.drawable.syntaxanalysis_n);
                readlinelayout.setVisibility(View.VISIBLE);
                jiexiline.setVisibility(View.GONE);
                _hint.setVisibility(View.GONE);
                _lock.setVisibility(View.VISIBLE);
                if (locked) {
                    _lock.setImageResource(R.drawable.suoding2);
                } else {
                    _lock.setImageResource(R.drawable.suoding);
                }
                _translation.setImageResource(R.drawable.translation_f);
                if (translationFragment == null)
                    translationFragment = new TranslationFragment();
                type = TranslateType.sentence;
                _input.setHint("请输入语句");
                FragmentView.replacess(getSupportFragmentManager(), translationFragment, R.id.layout_frame);
                getConnection(_input.getText().toString());
                break;
            case R.id.rb_Grammartranslation:
                _input.setText(_input.getText().toString());
                if (hashMapList != null) {
                    hashMapList.clear();
                }
                Selection.removeSelection(_input.getEditableText());
                cd = 2;
                if (answerReadWindow != null && answerReadWindow.isShowing()) {
                    answerReadWindow.dismiss();
                }
                _translation.setImageResource(R.drawable.translation_n);
                _Electronicdictionary.setImageResource(R.drawable.electronicdictionary_n);
                _Grammartranslation.setImageResource(R.drawable.syntaxanalysis_n);
                readlinelayout.setVisibility(View.GONE);
                jiexiline.setVisibility(View.VISIBLE);
                _Grammartranslation.setImageResource(R.drawable.syntaxanalysis_f);
                if (sentenceStructureFragment == null)
                    sentenceStructureFragment = new SentenceStructureFragment(this);
                type = analyse;
                _input.setHint("请输入语句");
                FragmentView.replacess(getSupportFragmentManager(), sentenceStructureFragment, R.id.layout_frame);
                getConnection(_input.getText().toString());
                break;
            case R.id.rb_Electronicdictionary:
                if (!_input.getText().toString().equals("")) {
                    locked = true;
                    _input.setCursorVisible(false);
                } else {
                    locked = false;
                    _input.setCursorVisible(true);
                }
                String s3 = _input.getText().toString();
                _input.setText(s3);
                if (hashMapList != null) {
                    hashMapList.clear();
                }
                if (!autoinput) {
                    _hint.setVisibility(View.VISIBLE);
                }
                cd = 3;
                if (returnType == 2) {
                    _lock.setImageResource(R.drawable.suoding2);
                    locked = true;
                }
                locked1 = false;
                _resume.setImageResource(R.drawable.yuyin);
                if (answerReadWindow != null && answerReadWindow.isShowing()) {
                    answerReadWindow.dismiss();
                }
                _translation.setImageResource(R.drawable.translation_n);
                _Electronicdictionary.setImageResource(R.drawable.electronicdictionary_n);
                _Grammartranslation.setImageResource(R.drawable.syntaxanalysis_n);
                _lock.setVisibility(View.VISIBLE);
                readlinelayout.setVisibility(View.VISIBLE);
                jiexiline.setVisibility(View.GONE);
                _Electronicdictionary.setImageResource(R.drawable.electronicdictionary_f);
                if (electronicdictionaryFragment == null)
                    electronicdictionaryFragment = new ElectronicdictionaryFragment();
                type = TranslateType.word;
                _input.setHint("请输入单词");
                FragmentView.replacess(getSupportFragmentManager(), electronicdictionaryFragment, R.id.layout_frame);
                inputtext = _input.getText().toString();
                if (inputtext.length() > 0) {
                    _lock.setImageResource(R.drawable.suoding2);
                }
                getConnection(inputtext);
                break;
            case R.id.collection:
                if (!_input.getText().toString().equals("") && (_input.getText().toString() != "")) {
                    String content = "";
                    content = _input.getText().toString();
                    if (content == null || content.equals(""))
                        return;
                    if (!getConnection(content.trim())) {
                        MyNote note = new MyNote();
                        note.setContent(content.trim());
                        note.setCreatTime(System.currentTimeMillis());
                        note.setType(type.value);
                        Manager.InsertNote(note);
                        collection.setImageResource(R.drawable.collectioned);
                        Toast.makeText(getApplicationContext(), "收藏成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Manager.DeleteNote(content, type.value);
                        collection.setImageResource(R.drawable.collection);
                        Toast.makeText(getApplicationContext(), "取消收藏", Toast.LENGTH_SHORT).show();
                    }
                    getConnection(content.trim());
                } else {
                    if (cd == 1) {
                        Toast.makeText(DictionaryTranslationActivity.this, "请输入语句", Toast.LENGTH_SHORT).show();
                    } else if (cd == 2) {
                        Toast.makeText(DictionaryTranslationActivity.this, "请输入语句", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(DictionaryTranslationActivity.this, "请输入单词", Toast.LENGTH_SHORT).show();
                    }

                }
                break;
            case R.id.shares:
                ShareSDK.initSDK(DictionaryTranslationActivity.this);
                OnekeyShare oks = new OnekeyShare();
                oks.disableSSOWhenAuthorize();
                oks.setTitle("标题");
                oks.setTitleUrl("http://sharesdk.cn");
                String shareText=_input.getText().toString();
                if (TextUtils.isEmpty(shareText)){
                    Toast.makeText(DictionaryTranslationActivity.this, "你分享的内容为空 ！", Toast.LENGTH_SHORT).show();
                }else {
                    oks.setText(shareText);
                }
                oks.setUrl("http://sharesdk.cn");
                oks.setComment("我是测试评论文本");
                oks.setSite(getString(R.string.app_name));
                oks.setSiteUrl("http://sharesdk.cn");
                oks.show(DictionaryTranslationActivity.this);
                break;
            default:
                break;
        }

    }

    private boolean getConnection(String content) {
        if (content != null && !content.equals("") && Manager.SelectNoteContent(content, type.value) != null) {
            collection.setImageResource(R.drawable.collectioned);
            return true;
        } else {
            collection.setImageResource(R.drawable.collection);
            return false;
        }
    }

    private void translate(String content) {
        if (content.equals(""))
            return;
        int num = 0;
        for (int i = 0; i < content.length(); i++) {
            if (content.charAt(i) >= 'a' && content.charAt(i) <= 'z' || content.charAt(i) >= 'A' && content.charAt(i) <= 'Z') {
                num = i;
                break;
            }
        }
        content = content.substring(num, content.length());
        switch (type) {
            case sentence:
                BaiduTranslation(StringFilter(content.trim()));
                return;
            case analyse:
                if (sentenceStructureFragment != null && sentenceStructureFragment.isVisible()) {
                    sentenceStructureFragment.analyse(StringFilter(content.trim()));
                }
                return;
            case word:
                if (electronicdictionaryFragment != null && electronicdictionaryFragment.isVisible()) {
                    electronicdictionaryFragment.translate(content.trim());
                }
                return;
            default:
                return;
        }
    }

    public static String StringFilter(String str) throws PatternSyntaxException {
        str = str.replaceAll("【", "[").replaceAll("】", "]").replaceAll("！", "!").replaceAll("，", ",")
                .replaceAll("。", ".").replaceAll("？", "?").replaceAll("：", ":").replaceAll("·", "`")
                .replaceAll("；", ";").replaceAll("‘", "'").replaceAll("’", "'").replaceAll("”", "'")
                .replaceAll("“", "'").replaceAll("\"", "\'");
        String regEx = "[『』]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        String trim = m.replaceAll("").trim();
        return trim;
    }

    private int extractWordCurOff(Layout layout, int x, int y) {
        int line;
        line = layout.getLineForVertical(_input.getScrollY() + y - 15);
        int curOff = layout.getOffsetForHorizontal(line, x);
        return curOff;
    }

    private String[] getSelectWord(Editable edittext, int curOff) {
        String word = "";
        int start = getWordLeftIndex(edittext, curOff);
        int end = getWordRightIndex(edittext, curOff);
        if (start >= 0 && end >= 0) {
            word = edittext.subSequence(start, end).toString();
            if (!"".equals(word)) {
                _input.setFocusableInTouchMode(true);
                _input.requestFocus();
                Selection.setSelection(edittext, start, end);
            }
        }

        return new String[]{word, String.valueOf(start), String.valueOf(end)};
    }

    private int getWordLeftIndex(Editable edittext, int cur) {
        String editableText = edittext.toString();
        if (cur >= editableText.length())
            return cur;
        int temp = 0;
        if (cur >= 20)
            temp = cur - 20;
        Pattern pattern = Pattern.compile("[^'A-Za-z]");
        Matcher m = pattern.matcher(editableText.charAt(cur) + "");
        if (m.find())
            return cur;
        String text = editableText.subSequence(temp, cur).toString();
        int i = text.length() - 1;
        for (; i >= 0; i--) {
            Matcher mm = pattern.matcher(text.charAt(i) + "");
            if (mm.find())
                break;
        }
        int start = i + 1;
        start = cur - (text.length() - start);
        return start;
    }

    private int getWordRightIndex(Editable edittext, int cur) {
        String editableText = edittext.toString();
        if (cur >= editableText.length())
            return cur;
        int templ = editableText.length();
        if (cur <= templ - 20)
            templ = cur + 20;
        Pattern pattern = Pattern.compile("[^'A-Za-z]");
        Matcher m = pattern.matcher(editableText.charAt(cur) + "");
        if (m.find())
            return cur;
        String text1 = editableText.subSequence(cur, templ).toString();
        int i = 0;
        for (; i < text1.length(); i++) {
            Matcher mm = pattern.matcher(text1.charAt(i) + "");
            if (mm.find())
                break;
        }
        int end = i;
        end = cur + end;
        return end;
    }

    public static void startActivity(final Context context, TranslateType type, String content, int returnType) {
        DictionaryTranslationActivity.type = type;
        DictionaryTranslationActivity.content = content;
        DictionaryTranslationActivity.returnType = returnType;
        context.startActivity(new Intent(context, DictionaryTranslationActivity.class));
    }

    public enum TranslateType {
        sentence(1), analyse(2), word(3);
        private final int value;

        TranslateType(int value) {
            this.value = value;
        }

        public int value() {
            return value;
        }

        public static TranslateType valueOf(int value) {
            switch (value) {
                case 1:
                    return sentence;
                case 2:
                    return analyse;
                case 3:
                    return word;
                default:
                    return sentence;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        answerReadWindow.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    MasaGrammarActivity.BianSeCallBack bianSeCallBack = new MasaGrammarActivity.BianSeCallBack() {
        @Override
        public void callback(String name) {
            if (name.equals(WordFragment.class.getSimpleName())) {
                danci.setText("〈 单词辨析");
            }
        }
    };

    class ClickTextBean {
        int curOff;
        int start;
        int end;

        public ClickTextBean(int curOff, int start, int end) {
            this.curOff = curOff;
            this.start = start;
            this.end = end;
        }

        public int getCurOff() {
            return curOff;
        }

        public void setCurOff(int curOff) {
            this.curOff = curOff;
        }

        public int getStart() {
            return start;
        }

        public void setStart(int start) {
            this.start = start;
        }

        public int getEnd() {
            return end;
        }

        public void setEnd(int end) {
            this.end = end;
        }
    }
}
