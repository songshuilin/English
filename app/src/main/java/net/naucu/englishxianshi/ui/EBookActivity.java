package net.naucu.englishxianshi.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.naucu.englishxianshi.R;
import net.naucu.englishxianshi.adapter.EBookAdapter;
import net.naucu.englishxianshi.db.Manager;
import net.naucu.englishxianshi.db.dao.Books;
import net.naucu.englishxianshi.ebook.EBook;
import net.naucu.englishxianshi.ebook.EBookUtil;
import net.naucu.englishxianshi.iflytek.TTSIflytek;
import net.naucu.englishxianshi.ui.DictionaryTranslationActivity.TranslateType;
import net.naucu.englishxianshi.ui.base.BaseActivity;
import net.naucu.englishxianshi.ui.video.EBooksCallBack;
import net.naucu.englishxianshi.ui.video.PlayConfig;
import net.naucu.englishxianshi.util.NetTool;
import net.naucu.englishxianshi.util.SharedPreTool;
import net.naucu.englishxianshi.widget.view.SelectTextview;
import net.naucu.englishxianshi.widget.window.AnswerReadWindow;

import java.io.File;
import java.util.List;

/**
 * @author fght 电子书界面
 */
public class EBookActivity extends BaseActivity implements OnClickListener, EBooksCallBack {
    public static final String TAG = EBookActivity.class.getSimpleName();

    private static String bookpath;
    private EBookAdapter adapter;
    private List<EBook> ebook_map;
    // private PopWindows selectedPop, moresetPop;
    private static Books book;
    private TTSIflytek ttsIflytek;
    private boolean showChinese = true, showEnglish = true;
    // 复读机
    private AnswerReadWindow answerReadWindow;
    public int pos = -1;
    public String s = "";


    public String callBackString;

    public int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ebook);
        Toast.makeText(EBookActivity.this, "请浏览全文或选择单句进行操作", Toast.LENGTH_SHORT).show();
        initView();

    }

    public void setS(String s) {
        this.s = s;
    }

    private ListView _listView;
    private LinearLayout _topcontrol, _bottomcontrol;
    private ImageView _translate, _edic, _read, _analysis, _moreset, _back, _flag;
    // private TextView _review;// _readall, _repeat;
    private ImageView _readall, _repeat;

    private void initView() {
        _topcontrol = (LinearLayout) findViewById(R.id.ll_topcontrol_ebook);
        _bottomcontrol = (LinearLayout) findViewById(R.id.ll_bottomcontrol_ebook);
        _back = (ImageView) findViewById(R.id.iv_back_ebook);
        _flag = (ImageView) findViewById(R.id.iv_flag_ebook);
        // _review = (TextView) findViewById(R.id.tv_review_ebook);
        _readall = (ImageView) findViewById(R.id.tv_readall_ebook);
        _repeat = (ImageView) findViewById(R.id.tv_repeat_ebook);
        _translate = (ImageView) findViewById(R.id.iv_translate_ebook);
        _edic = (ImageView) findViewById(R.id.iv_edic_ebook);
        _read = (ImageView) findViewById(R.id.iv_read_ebook);
        _analysis = (ImageView) findViewById(R.id.iv_analysis_ebook);
        _moreset = (ImageView) findViewById(R.id.iv_moreset_ebook);
        _back.setOnClickListener(this);
        _flag.setOnClickListener(this);
        _readall.setOnClickListener(this);
        _repeat.setOnClickListener(this);
        _translate.setOnClickListener(this);
        _edic.setOnClickListener(this);
        _read.setOnClickListener(this);
        _analysis.setOnClickListener(this);
        _moreset.setOnClickListener(this);

        _listView = (ListView) findViewById(R.id.ebook_lv);
        _listView.setFastScrollEnabled(true);
        _listView.setOverScrollMode(View.OVER_SCROLL_NEVER);

        if (bookpath != null && new File(bookpath).exists()) {
            ebook_map = EBookUtil.instance(bookpath, false).getEBookMap();
            if (ebook_map != null) {
                adapter = new EBookAdapter(EBookActivity.this, ebook_map, this, this);
                _listView.setAdapter(adapter);
                if (book != null && book.getLastPosition() != 0) {
                    _listView.setSelection(book.getLastPosition());
                }
                _listView.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (pos == position) {
                            adapter.notifyDataSetChanged();
                            adapter.setCurrentPosition(position);
                            if (!hidePop(pop_select)) {
                                hidePop(pop_textSize);
                                hidePop(pop_moreset);
                                hidePop(pop_speed);
                                if (callBackString == null) {
                                    return;
                                }
                                if (callBackString.equals("")) {
                                    showSelectPop(ebook_map.get(adapter.getCurrentPosition()).getEbookBodyE());
                                } else {
                                    if (!com.lijunsai.httpInterface.tool.SharedPreTool.getSharedPreDateBoolean(EBookActivity.this, PlayConfig.AUTO_INPUT, true)) {
                                        if (index > 0) {
                                            showSelectPop(callBackString);
                                        } else {

                                        }
                                    } else {
                                        if (index > 0) {
                                            DictionaryTranslationActivity.startActivity(EBookActivity.this, TranslateType.word, callBackString, 2);
                                        }
                                    }
                                }

                            }

                            //改
                            initMoreSetPop();
                            if (pop_moreset.isShowing()) {
                                _moreset.setImageResource(R.drawable.video_moreset_n);
                                pop_moreset.dismiss();
                                pop_moreset = null;
                            } else {
                                _moreset.setImageResource(R.drawable.video_moreset_n);
                                pop_moreset.dismiss();
                                pop_moreset = null;
                            }

                            _read.setImageResource(R.drawable.video_read_n);
                            _readall.setImageResource(R.drawable.readall_b);
                            if (ttsIflytek != null && ttsIflytek.getSpeechSynthesizer().isSpeaking()) {
                                ttsIflytek.getSpeechSynthesizer().stopSpeaking();
                            }
                        } else {
                            callBackString = parent.getAdapter().getItem(position).toString();
                            adapter.notifyDataSetChanged();
                            pos = position;
                            adapter.setCurrentPosition(position);
                        }
                        if (_topcontrol.getVisibility() != View.VISIBLE) {
                            showControl();
                        }
                    }
                });

                _listView.setOnTouchListener(new OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        Log.i("asdasd", "AAAAAAA = " + event.getAction());
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:

                                break;
                            case MotionEvent.ACTION_MOVE:
                                hideControl();
                                hidePop(pop_select);
                                hidePop(pop_moreset);
                                hidePop(pop_textSize);
                                hidePop(pop_speed);
                                break;
                            default:

                                break;
                        }
                        return false;
                    }
                });
            }
        }

    }

    @Override
    public void onClick(View v) {
        String content = "";
        if (ebook_map != null && adapter != null) {
            content = ebook_map.get(adapter.getCurrentPosition()).getEbookBodyE();
        }
        switch (v.getId()) {
            case R.id.iv_back_ebook:
                finish();
                break;
            // case R.id.tv_review_ebook:
            //
            // break;
            case R.id.tv_readall_ebook:
                if (!NetTool.isNetworkConnected(this)) {
                    Toast.makeText(this, "网络异常,请检查网络连接状态", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (adapter != null) {
                    if (ttsIflytek != null && ttsIflytek.getSpeechSynthesizer().isSpeaking()) {
                        ttsIflytek.getSpeechSynthesizer().stopSpeaking();
                        _readall.setImageResource(R.drawable.readall_b);
                    } else {
                        String text = "";
                        int start = _listView.getFirstVisiblePosition();

                        if (ebook_map.size() - start > 5) {
                            for (int i = start + 1; i < start + 5; i++) {
                                text += ebook_map.get(i).getEbookBodyE();
                            }
                        } else {
                            for (int i = start + 1; i < ebook_map.size(); i++) {
                                text += ebook_map.get(i).getEbookBodyE();
                            }
                        }
                        if (ttsIflytek == null) {
                            ttsIflytek = new TTSIflytek(getApplicationContext());
                        }
                        ttsIflytek.setTextflytek(text.toString());
                        _readall.setImageResource(R.drawable.readall_f);

                    }
                }
                if (answerReadWindow != null && answerReadWindow.isShowing()) {
                    answerReadWindow.dismiss();
                    _bottomcontrol.setVisibility(View.VISIBLE);
                    _repeat.setImageResource(R.drawable.repeat_b);
                }

                //改
                hidePop(pop_select);
                hidePop(pop_textSize);
                hidePop(pop_speed);
                initMoreSetPop();
                if (pop_moreset.isShowing()) {
                    _moreset.setImageResource(R.drawable.video_moreset_n);
                    pop_moreset.dismiss();
                    pop_moreset = null;
                } else {
                    _moreset.setImageResource(R.drawable.video_moreset_n);
                    pop_moreset.dismiss();
                    pop_moreset = null;
                }

                _read.setImageResource(R.drawable.video_read_n);
                break;
            case R.id.tv_repeat_ebook:
                if (answerReadWindow.isShowing()) {
                    answerReadWindow.dismiss();
                    _bottomcontrol.setVisibility(View.VISIBLE);
                    _repeat.setImageResource(R.drawable.repeat_b);
                } else {
                    answerReadWindow.showAtLocation(_listView, Gravity.BOTTOM, 0, 0);
                    _bottomcontrol.setVisibility(View.GONE);
                    _repeat.setImageResource(R.drawable.repeat_f);
                }
                if (ttsIflytek != null && ttsIflytek.getSpeechSynthesizer().isSpeaking()) {
                    ttsIflytek.getSpeechSynthesizer().stopSpeaking();
                    _readall.setImageResource(R.drawable.readall_b);
                }

                //改
                hidePop(pop_select);
                hidePop(pop_textSize);
                hidePop(pop_speed);
                initMoreSetPop();
                if (pop_moreset.isShowing()) {
                    _moreset.setImageResource(R.drawable.video_moreset_n);
                    pop_moreset.dismiss();
                    pop_moreset = null;
                } else {
                    _moreset.setImageResource(R.drawable.video_moreset_n);
                    pop_moreset.dismiss();
                    pop_moreset = null;
                }

                _read.setImageResource(R.drawable.video_read_n);
                _readall.setImageResource(R.drawable.readall_b);
                if (ttsIflytek != null && ttsIflytek.getSpeechSynthesizer().isSpeaking()) {
                    ttsIflytek.getSpeechSynthesizer().stopSpeaking();
                }
                break;
            case R.id.iv_flag_ebook:

                break;
            case R.id.iv_translate_ebook:
                if (!NetTool.isNetworkConnected(this)) {
                    Toast.makeText(this, "网络异常,请检查网络连接状态", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (content != null && !content.equals("")) {
                    if (s != null) {
                        if (s.equals("")) {
                            DictionaryTranslationActivity.startActivity(EBookActivity.this, TranslateType.sentence, content, 1);
                        } else {
                            DictionaryTranslationActivity.startActivity(EBookActivity.this, TranslateType.sentence, s, 2);
                            s = "";
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "请选择要操作的字段", Toast.LENGTH_LONG).show();
                }

                //改
                hidePop(pop_select);
                hidePop(pop_textSize);
                hidePop(pop_speed);
                initMoreSetPop();
                if (pop_moreset.isShowing()) {
                    _moreset.setImageResource(R.drawable.video_moreset_n);
                    pop_moreset.dismiss();
                    pop_moreset = null;
                } else {
                    _moreset.setImageResource(R.drawable.video_moreset_n);
                    pop_moreset.dismiss();
                    pop_moreset = null;
                }

                break;
            case R.id.iv_edic_ebook:
                if (!NetTool.isNetworkConnected(this)) {
                    Toast.makeText(this, "网络异常,请检查网络连接状态", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!hidePop(pop_select)) {
                    hidePop(pop_textSize);
                    hidePop(pop_moreset);
                    hidePop(pop_speed);
                    if (!com.lijunsai.httpInterface.tool.SharedPreTool.getSharedPreDateBoolean(EBookActivity.this, PlayConfig.AUTO_INPUT, true)) {
                        if (callBackString != null) {
                            if (callBackString.equals("")) {
                                showSelectPop(ebook_map.get(adapter.getCurrentPosition()).getEbookBodyE());
                            } else {
                                showSelectPop(callBackString);
                            }
                        }
                    } else {
                        DictionaryTranslationActivity.startActivity(EBookActivity.this, TranslateType.word, ebook_map.get(adapter.getCurrentPosition()).getEbookBodyE(), 2);
                    }

                }

                //改
                initMoreSetPop();
                if (pop_moreset.isShowing()) {
                    _moreset.setImageResource(R.drawable.video_moreset_n);
                    pop_moreset.dismiss();
                    pop_moreset = null;
                } else {
                    _moreset.setImageResource(R.drawable.video_moreset_n);
                    pop_moreset.dismiss();
                    pop_moreset = null;
                }

                _read.setImageResource(R.drawable.video_read_n);
                _readall.setImageResource(R.drawable.readall_b);
                if (ttsIflytek != null && ttsIflytek.getSpeechSynthesizer().isSpeaking()) {
                    ttsIflytek.getSpeechSynthesizer().stopSpeaking();
                }
                break;
            case R.id.iv_read_ebook:
                if (!NetTool.isNetworkConnected(this)) {
                    Toast.makeText(this, "网络异常,请检查网络连接状态", Toast.LENGTH_SHORT).show();
                    return;
                }
                _read.setImageResource(R.drawable.video_read_n);
                if (ttsIflytek != null && ttsIflytek.getSpeechSynthesizer().isSpeaking()) {
                    ttsIflytek.getSpeechSynthesizer().stopSpeaking();
                } else {
                    if (callBackString != null && !callBackString.equals("") && callBackString.length() > 0) {
                        if (ttsIflytek == null) {
                            ttsIflytek = new TTSIflytek(getApplicationContext());
                        }
                        ttsIflytek.set_read(_read, 2);
                        ttsIflytek.setTextflytek(callBackString);
                    } else if (content != null && !content.equals("") && content.length() > 0) {
                        if (ttsIflytek == null) {
                            ttsIflytek = new TTSIflytek(getApplicationContext());
                        }
                        ttsIflytek.set_read(_read, 2);
                        ttsIflytek.setTextflytek(content);
                        Log.i("TAG", "TTSIFLYTEKCONTENT" + callBackString);
                    }
                    if (content == null && content.equals("") && content.length() == 0) {
                        Toast.makeText(getApplicationContext(), "请选择要操作的字段", Toast.LENGTH_LONG).show();
                    }
                }

                //改
                hidePop(pop_select);
                hidePop(pop_textSize);
                hidePop(pop_speed);
                initMoreSetPop();
                if (pop_moreset.isShowing()) {
                    _moreset.setImageResource(R.drawable.video_moreset_n);
                    pop_moreset.dismiss();
                    pop_moreset = null;
                } else {
                    _moreset.setImageResource(R.drawable.video_moreset_n);
                    pop_moreset.dismiss();
                    pop_moreset = null;
                }
                _readall.setImageResource(R.drawable.readall_b);

                break;
            case R.id.iv_analysis_ebook:
                if (!NetTool.isNetworkConnected(this)) {
                    Toast.makeText(this, "网络异常,请检查网络连接状态", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (content != null && !content.equals("")) {
                    if (callBackString != null) {
                        if (callBackString.equals("")) {
                            DictionaryTranslationActivity.startActivity(EBookActivity.this, TranslateType.analyse, content, 1);
                            Log.i("TAG", "poppoppoppoppoppoppop 01 = " + s);
                        } else {
                            DictionaryTranslationActivity.startActivity(EBookActivity.this, TranslateType.analyse, callBackString, 2);
                            Log.i("TAG", "poppoppoppoppoppoppop 02 = " + s);
                            s = "";
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "请选择要操作的字段", Toast.LENGTH_LONG).show();
                }

                break;
            case R.id.iv_moreset_ebook:
                hidePop(pop_select);
                hidePop(pop_textSize);
                hidePop(pop_speed);
                initMoreSetPop();
                if (pop_moreset.isShowing()) {
                    _moreset.setImageResource(R.drawable.video_moreset_n);
                    pop_moreset.dismiss();
                    pop_moreset = null;
                } else {
                    _moreset.setImageResource(R.drawable.video_moreset_f);
                    pop_moreset.showAtLocation(_listView, Gravity.BOTTOM,
                            _moreset.getLeft() + _analysis.getRight() - 40 >> 1, _bottomcontrol.getHeight());
                    _moresetLL.setBackgroundResource(R.drawable.pop_videomoreset_bg_bottom);
                }

                _read.setImageResource(R.drawable.video_read_n);
                _readall.setImageResource(R.drawable.readall_b);
                if (ttsIflytek != null && ttsIflytek.getSpeechSynthesizer().isSpeaking()) {
                    ttsIflytek.getSpeechSynthesizer().stopSpeaking();
                }

                break;
            case R.id.chahao:
                pop_select.dismiss();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (answerReadWindow == null) {
            answerReadWindow = new AnswerReadWindow(this);
            answerReadWindow.setOnDismissListener(new OnDismissListener() {
                @Override
                public void onDismiss() {
                    answerReadWindow.stopPlayer();
                    _bottomcontrol.setVisibility(View.VISIBLE);
                    _repeat.setImageResource(R.drawable.repeat_b);
                }
            });
        }

        //改
        hidePop(pop_select);
        hidePop(pop_textSize);
        hidePop(pop_speed);
        initMoreSetPop();
        if (pop_moreset.isShowing()) {
            _moreset.setImageResource(R.drawable.video_moreset_n);
            pop_moreset.dismiss();
            pop_moreset = null;
        } else {
            _moreset.setImageResource(R.drawable.video_moreset_n);
            pop_moreset.dismiss();
            pop_moreset = null;
        }

        _read.setImageResource(R.drawable.video_read_n);
        _readall.setImageResource(R.drawable.readall_b);

        int textsize = SharedPreTool.getSharedPreDateInt(getApplicationContext(), "booktextSize", 14);
        if (textsize > 0 && adapter != null) {
            adapter.setTextSize(textsize);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (ttsIflytek != null && ttsIflytek.getSpeechSynthesizer().isSpeaking()) {
            ttsIflytek.getSpeechSynthesizer().stopSpeaking();
        }
        if (book != null && ebook_map != null) {
            book.setLastPosition(_listView.getFirstVisiblePosition());
            Manager.InsertBooks(book);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hidePop(answerReadWindow);
    }

    private void hideControl() {
        if (_topcontrol.getVisibility() == View.VISIBLE)
            _topcontrol.setVisibility(View.INVISIBLE);
        if (_bottomcontrol.getVisibility() == View.VISIBLE)
            _bottomcontrol.setVisibility(View.GONE);
    }

    void showControl() {
        Log.i(TAG, "LOL_SHOW");
        if (_topcontrol.getVisibility() != View.VISIBLE)
            _topcontrol.setVisibility(View.VISIBLE);
        if (_bottomcontrol.getVisibility() != View.VISIBLE
                && !(answerReadWindow != null && answerReadWindow.isShowing()))
            _bottomcontrol.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean haspop = false;
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (hidePop(pop_select) | hidePop(pop_textSize) | hidePop(pop_speed)) {
                haspop = true;
            } else {
                if (hidePop(pop_moreset)) {
                    haspop = true;
                } else {
                    if (hidePop(answerReadWindow)) {
                        haspop = true;
                    } else {
                        if (_bottomcontrol.getVisibility() == View.VISIBLE) {
                            haspop = true;
                            _bottomcontrol.setVisibility(View.GONE);
                        }
                        if (_topcontrol.getVisibility() == View.VISIBLE) {
                            haspop = true;
                            _topcontrol.setVisibility(View.INVISIBLE);
                        }
                    }
                }
            }
            /**
             *   逐级返回·····
             *   重要逻辑~~~~~~~~
             * */
            if (!haspop){
                finish();
            }
        }
//
        return false;
    }

    private PopupWindow pop_select;
    private SelectTextview selectTextview;
    private ImageView chahao;

    private void initSelectPop() {
        if (pop_select == null) {
            View view = LayoutInflater.from(this).inflate(R.layout.pop_selected, new LinearLayout(this), false);
            pop_select = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            selectTextview = (SelectTextview) view.findViewById(R.id.tv_select);
            chahao = (ImageView) view.findViewById(R.id.chahao);
            chahao.setOnClickListener(this);
        }
    }
    // =============================更多设置popupwindow===============================
    /**
     * 更多设置popupwindow
     */
    private PopupWindow pop_moreset;
    private LinearLayout _moresetLL;

    private void initMoreSetPop() {
        TextView _hideChinese, _hideEnglish, _textSize, _inputMode, _speed;
        if (pop_moreset == null) {
            View view = LayoutInflater.from(this).inflate(R.layout.pop_moreset_video, new LinearLayout(this), false);
            pop_moreset = new PopupWindow(view, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

            _moresetLL = (LinearLayout) view.findViewById(R.id.ll_moreset_videopop);
            _hideChinese = (TextView) view.findViewById(R.id.tv_hideChinese_video);
            _hideEnglish = (TextView) view.findViewById(R.id.tv_hideEnglish_video);
            _textSize = (TextView) view.findViewById(R.id.tv_textSize_video);
            _speed = (TextView) view.findViewById(R.id.tv_speed_video);
            _inputMode = (TextView) view.findViewById(R.id.tv_inputMode_video);
            if (SharedPreTool.getSharedPreDateBoolean(getApplicationContext(), "autoInput", true)) {
                _inputMode.setText("智能词典");
            } else {
                _inputMode.setText("手动词典");
            }
            OnClickListener moresetlisition = new OnClickListener() {
                @Override
                public void onClick(View v) {
                    hidePop(pop_select);
                    hidePop(pop_textSize);
                    hidePop(pop_speed);
                    switch (v.getId()) {
                        case R.id.tv_hideChinese_video:// 隐藏|显示中文
                            showChinese = !showChinese;
                            adapter.showChinese(showChinese);
                            if (showChinese) {
                                ((TextView) v).setText("隐藏中文");
                            } else {
                                ((TextView) v).setText("显示中文");
                            }
                            break;
                        case R.id.tv_hideEnglish_video:// 隐藏|显示英文
                            showEnglish = !showEnglish;
                            adapter.showEnglish(showEnglish);
                            if (showEnglish) {
                                ((TextView) v).setText("隐藏英文");
                            } else {
                                ((TextView) v).setText("显示英文");
                            }
                            break;
                        case R.id.tv_inputMode_video:// 查询方式
                            boolean autoInput = SharedPreTool.getSharedPreDateBoolean(getApplicationContext(), "autoInput",
                                    true);
                            SharedPreTool.setSharedPreDateBoolean(getApplicationContext(), "autoInput", !autoInput);
                            if (!autoInput) {
                                ((TextView) v).setText("智能词典");
                            } else {
                                ((TextView) v).setText("手动词典");
                            }
                            break;
                        case R.id.tv_textSize_video:// 设置字体大小
                            initTextSizePop();
                            if (pop_textSize.isShowing()) {
                                pop_textSize.dismiss();
                            } else {
                                pop_textSize.showAtLocation(_listView, Gravity.BOTTOM, 0, 220);
                                _moresetLL.setVisibility(View.GONE);
                            }
                            pop_moreset.dismiss();
                            break;
                        case R.id.tv_speed_video:
                            initSpeedPop();
                            if (pop_speed.isShowing()) {
                                pop_speed.dismiss();
                            } else {
                                pop_speed.showAtLocation(_listView, Gravity.BOTTOM, 0, 220);
                                _moresetLL.setVisibility(View.GONE);
                            }
                            pop_moreset.dismiss();
                            break;
                        default:
                            break;
                    }
                }
            };

            _hideChinese.setOnClickListener(moresetlisition);
            _hideEnglish.setOnClickListener(moresetlisition);
            _textSize.setOnClickListener(moresetlisition);
            _speed.setOnClickListener(moresetlisition);
            _inputMode.setOnClickListener(moresetlisition);
        }
    }


    // =============================语速设置popupwindow===============================

    private PopupWindow pop_speed;

    private void initSpeedPop() {
        RelativeLayout manLayout, zhongLayout, kuaiLayout;
        final ImageView manImage, zhongImage, kuaiImage;
        if (pop_speed == null) {
            View view = LayoutInflater.from(this).inflate(R.layout.pop_speed, new LinearLayout(this), false);
            pop_speed = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            manLayout = (RelativeLayout) view.findViewById(R.id.rl_man_video);
            zhongLayout = (RelativeLayout) view.findViewById(R.id.rl_zhong_video);
            kuaiLayout = (RelativeLayout) view.findViewById(R.id.rl_kuai_video);
            manImage = (ImageView) view.findViewById(R.id.iv_man_video);
            zhongImage = (ImageView) view.findViewById(R.id.iv_zhong_video);
            kuaiImage = (ImageView) view.findViewById(R.id.iv_kuai_video);
            manImage.setVisibility(View.GONE);
            zhongImage.setVisibility(View.GONE);
            kuaiImage.setVisibility(View.GONE);
            switch (SharedPreTool.getSharedPreDateInt(EBookActivity.this, "EadLanguageShared", 50)) {
                case 0:
                    manImage.setVisibility(View.VISIBLE);
                    zhongImage.setVisibility(View.GONE);
                    kuaiImage.setVisibility(View.GONE);
                    break;
                case 50:
                    manImage.setVisibility(View.GONE);
                    zhongImage.setVisibility(View.VISIBLE);
                    kuaiImage.setVisibility(View.GONE);
                    break;
                case 100:
                    manImage.setVisibility(View.GONE);
                    zhongImage.setVisibility(View.GONE);
                    kuaiImage.setVisibility(View.VISIBLE);
                    break;
            }
            manLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreTool.setSharedPreDateInt(EBookActivity.this, "EadLanguageShared", 0);
                    manImage.setVisibility(View.VISIBLE);
                    zhongImage.setVisibility(View.GONE);
                    kuaiImage.setVisibility(View.GONE);
                }
            });
            zhongLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreTool.setSharedPreDateInt(EBookActivity.this, "EadLanguageShared", 50);
                    manImage.setVisibility(View.GONE);
                    zhongImage.setVisibility(View.VISIBLE);
                    kuaiImage.setVisibility(View.GONE);
                }
            });
            kuaiLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreTool.setSharedPreDateInt(EBookActivity.this, "EadLanguageShared", 100);
                    manImage.setVisibility(View.GONE);
                    zhongImage.setVisibility(View.GONE);
                    kuaiImage.setVisibility(View.VISIBLE);
                }
            });
        }

    }


    // =============================字体大小设置popupwindow===============================
    /**
     * 字体大小设置popupwindow
     */
    private PopupWindow pop_textSize;

    private void initTextSizePop() {
        RelativeLayout _smalllayout, _middlelayout, _largelayout;
        final ImageView _smallimage, _middleimage, _largeimage;
        if (pop_textSize == null) {
            View view = LayoutInflater.from(this).inflate(R.layout.pop_textsize, new LinearLayout(this), false);
            pop_textSize = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            _smalllayout = (RelativeLayout) view.findViewById(R.id.rl_smalltext_video);
            _middlelayout = (RelativeLayout) view.findViewById(R.id.rl_middletext_video);
            _largelayout = (RelativeLayout) view.findViewById(R.id.rl_largetext_video);
            _smallimage = (ImageView) view.findViewById(R.id.iv_smalltext_video);
            _middleimage = (ImageView) view.findViewById(R.id.iv_middletext_video);
            _largeimage = (ImageView) view.findViewById(R.id.iv_largetext_video);
            _smallimage.setVisibility(View.INVISIBLE);
            _middleimage.setVisibility(View.INVISIBLE);
            _largeimage.setVisibility(View.INVISIBLE);
            Log.i(TAG, "LOL_缓存 = " + SharedPreTool.getSharedPreDateInt(getApplicationContext(), "booktextSize", 14));
            switch (SharedPreTool.getSharedPreDateInt(getApplicationContext(), "booktextSize", 14)) {
                case 14:
                    _smallimage.setVisibility(View.VISIBLE);
                    break;
                case 17:
                    _middleimage.setVisibility(View.VISIBLE);
                    break;
                case 20:
                    _largeimage.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
            OnClickListener pop_textSizeListener = new OnClickListener() {
                @Override
                public void onClick(View v) {
                    _smallimage.setVisibility(View.INVISIBLE);
                    _middleimage.setVisibility(View.INVISIBLE);
                    _largeimage.setVisibility(View.INVISIBLE);
                    switch (v.getId()) {
                        case R.id.rl_smalltext_video:
                            _smallimage.setVisibility(View.VISIBLE);
                            SharedPreTool.setSharedPreDateInt(getApplicationContext(), "booktextSize", 14);
                            adapter.setTextSize(14);
                            break;
                        case R.id.rl_middletext_video:
                            _middleimage.setVisibility(View.VISIBLE);
                            SharedPreTool.setSharedPreDateInt(getApplicationContext(), "booktextSize", 17);
                            adapter.setTextSize(18);
                            break;
                        case R.id.rl_largetext_video:
                            _largeimage.setVisibility(View.VISIBLE);
                            SharedPreTool.setSharedPreDateInt(getApplicationContext(), "booktextSize", 20);
                            adapter.setTextSize(22);
                            break;

                        default:
                            break;
                    }
                }
            };
            _smalllayout.setOnClickListener(pop_textSizeListener);
            _middlelayout.setOnClickListener(pop_textSizeListener);
            _largelayout.setOnClickListener(pop_textSizeListener);
        }
    }

    private void showSelectPop(String text) {
        initSelectPop();
        if (_listView != null
                )
            pop_select.showAtLocation(_listView, Gravity.BOTTOM, 0, _bottomcontrol.getHeight());

        selectTextview.setText(text);
    }

    private boolean hidePop(PopupWindow popWindow) {
        // && popWindow.isShowing()
        if (popWindow != null) {
            if (popWindow == pop_textSize) {
                pop_textSize.dismiss();
                pop_textSize = null;
            } else if (popWindow == pop_select) {
                pop_select.dismiss();
                pop_select = null;
            } else if (popWindow == pop_moreset) {
                _moreset.setImageResource(R.drawable.video_moreset_n);
                pop_moreset.dismiss();
                pop_moreset = null;
            } else if (popWindow == answerReadWindow) {
                answerReadWindow.dismiss();
                answerReadWindow = null;
            } else if (popWindow == pop_speed) {
                pop_speed.dismiss();
                pop_speed = null;
            } else {
                popWindow.dismiss();
            }
            return true;
        } else {
            return false;
        }
    }

    public static void startActivity(Context context, Books book) {
        EBookActivity.book = book;
        EBookActivity.bookpath = book.getBooksLocalUrl();
        context.startActivity(new Intent(context, EBookActivity.class));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        answerReadWindow.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void callBack(String s, int index) {
        this.callBackString = s;
        this.index = index;
    }
}
