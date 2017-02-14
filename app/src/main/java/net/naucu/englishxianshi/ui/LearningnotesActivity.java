package net.naucu.englishxianshi.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.app.myTool.ActionTitleBarWidget;
import com.app.myTool.ActionTitleBarWidget.ClickListener;

import net.naucu.englishxianshi.R;
import net.naucu.englishxianshi.db.Manager;
import net.naucu.englishxianshi.db.dao.MyNote;
import net.naucu.englishxianshi.ui.DictionaryTranslationActivity.TranslateType;
import net.naucu.englishxianshi.ui.base.BaseActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 类名: LearningnotesActivity.java 描述: 学习笔记 作者: youyou_pc 时间: 2015年11月20日
 * 下午2:36:19
 */
public class LearningnotesActivity extends BaseActivity implements OnCheckedChangeListener {
    private ActionTitleBarWidget titlebar;// 初始化标题控件
    private ListView _listview;
    private List<MyNote> notes;
    private List<MyNote> notes_analyse;
    private List<MyNote> notes_translate;
    private List<MyNote> notes_word;
    private NoteAdapter adapter;
    private RadioGroup _radioGroup;
    private LinearLayout _bottom;
    private TextView _bottomdelete;
    public CheckBox cb_delete;
    int a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learningnotes);

        initView();
        initTitleBar();
        initEvent();

    }

    @Override
    protected void onResume() {
        super.onResume();
        initDate();
    }

    private void initView() {
        cb_delete = (CheckBox) findViewById(R.id.cb_delete);
        titlebar = (ActionTitleBarWidget) findViewById(R.id.titlebar);
        _listview = (ListView) findViewById(R.id.lv_Learningnotes);
        _radioGroup = (RadioGroup) findViewById(R.id.rg_notes);
        _bottom = (LinearLayout) findViewById(R.id.bottom_choice);
        _bottomdelete = (TextView) findViewById(R.id.tv_counts);
        _radioGroup.setOnCheckedChangeListener(this);

//        _listview.setOnItemClickListener(new OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> arg0, View arg1, int i, long arg3) {
//                if (adapter != null && _bottom.getVisibility() != View.VISIBLE) {
//                DictionaryTranslationActivity.startActivity(LearningnotesActivity.this,
//                        TranslateType.valueOf(((MyNote) adapter.getItem(i)).getType()),
//                        ((MyNote) adapter.getItem(i)).getContent(), 1);
//                }
//            }
//        });
        _bottom.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (adapter != null) {
                    if (_bottomdelete.getText().equals("0") || _bottomdelete.getText() == "0" || _bottomdelete.getText().equals("") || _bottomdelete.getText() == "") {
                        Toast toast = Toast.makeText(LearningnotesActivity.this, "请选择要删除的笔记", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    } else {
                        for (int i : adapter.getSelects()) {
                            Manager.DeleteNote((int) adapter.getItemId(i));
                        }
                        titlebar.RightViewRemove();
                        titlebar.setRightText(getString(R.string.tx_editor), 17, Color.BLACK);
                        _bottomdelete.setText("0");
                        adapter.showDelete(false);
                        initDate();
                        _bottom.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    /**
     * 初始化TitleBar
     */
    private void initTitleBar() {
        titlebar.setTitleBackgroundResource(R.drawable.menubutton_select, R.drawable.menubutton_select);
        titlebar.setLeftGravity(Gravity.CENTER);
        titlebar.setLeftIco(R.drawable.back);
        titlebar.setRightGravity(Gravity.CENTER);
        titlebar.setRightText(getString(R.string.tx_editor), 17, Color.BLACK);
        titlebar.setCenterText(getString(R.string.tx_Learningnotes), 17, Color.BLACK);
        titlebar.setCenterGravity(Gravity.CENTER);
        titlebar.setTitleBarBackgroundColor(Color.WHITE);
    }

    private void initDate() {
        notes = Manager.SelectNotes();
        if (notes != null) {
            adapter = new NoteAdapter(notes);
            _listview.setAdapter(adapter);
            notes_analyse = new ArrayList<>();
            notes_translate = new ArrayList<>();
            notes_word = new ArrayList<>();
            for (MyNote note : notes) {
                TranslateType type = TranslateType.valueOf(note.getType());
                switch (type) {
                    case sentence:
                        notes_translate.add(note);
                        break;
                    case analyse:
                        notes_analyse.add(note);
                        break;
                    case word:
                        notes_word.add(note);
                        break;
                }
            }
            switch (_radioGroup.getCheckedRadioButtonId()) {
                case R.id.rb_translate:
                    adapter.setNotes(notes_translate);
                    break;
                case R.id.rb_analyse:
                    adapter.setNotes(notes_analyse);
                    break;
                case R.id.rb_word:
                    adapter.setNotes(notes_word);
                    break;
            }
        }
    }

    private void initEvent() {
        titlebar.OnTitleBarClickListener(clickListener);
    }

    private ClickListener clickListener = new ClickListener() {

        @Override
        public void onright(View v) {
            if (notes == null || notes.size() <= 0) {
                return;
            }
            if (_bottom.getVisibility() == View.VISIBLE) {
                titlebar.RightViewRemove();
                titlebar.setRightText(getString(R.string.tx_editor), 17, Color.BLACK);
                adapter.showDelete(false);
                a = 1;
                _bottom.setVisibility(View.GONE);
            } else {
                titlebar.RightViewRemove();
                titlebar.setRightText(getString(R.string.tx_cancel), 17, Color.BLACK);
                adapter.showDelete(true);
                a = 1;
                _bottom.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onleft(View v) {
            finish();
        }

        @Override
        public void oncenter(View arg0) {

        }
    };


    public class NoteAdapter extends BaseAdapter {
        private List<MyNote> mynotes;
        private boolean showdeltet;
        private Map<Integer, Boolean> selects;

        public NoteAdapter(List<MyNote> notes) {
            this.mynotes = notes;
            selects = new HashMap<>();
            int n = mynotes.size();
            while (--n >= 0) {
                selects.put(n, false);
            }
        }

        public void setNotes(List<MyNote> notes) {
            this.mynotes = notes;
            selects = new HashMap<>();
            int n = mynotes.size();
            while (--n >= 0) {
                selects.put(n, false);
            }
            notifyDataSetChanged();
        }

        public void showDelete(boolean showdeltet) {
            this.showdeltet = showdeltet;
        }

        public List<Integer> getSelects() {
            List<Integer> select = new ArrayList<Integer>();
            if (selects != null) {
                for (int i = selects.size(); --i >= 0; ) {
                    if (selects.get(i)) {
                        select.add(i);
                    }
                }
            }
            return select;
        }

        @Override
        public int getCount() {
            return mynotes == null ? 0 : mynotes.size();
        }

        @Override
        public Object getItem(int i) {
            return mynotes.get(i);
        }

        @Override
        public long getItemId(int i) {
            return mynotes.get(i).getId();
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewgroup) {
            final ViewHolder holder;

            if (view == null) {
                view = LayoutInflater.from(LearningnotesActivity.this).inflate(R.layout.item_note, viewgroup, false);
                holder = new ViewHolder();
                holder._note = (TextView) view.findViewById(R.id.tv_note);
                holder._delete = (CheckBox) view.findViewById(R.id.cb_delete);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            holder._delete.setChecked(false);
            holder._note.setText(mynotes.get(i).getContent());
            holder._note.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (_bottom.getVisibility() == View.VISIBLE) {
                        if (a == 0) {
                            holder._delete.setChecked(true);
                            a = 1;
                        } else if (a == 1) {
                            holder._delete.setChecked(false);
                            a = 0;
                        }
                    } else {
                        DictionaryTranslationActivity.startActivity(LearningnotesActivity.this,
                                TranslateType.valueOf(((MyNote) mynotes.get(i)).getType()),
                                ((MyNote) mynotes.get(i)).getContent(), 2);
                    }
                }
            });
            holder._delete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                    selects.put(i, arg1);
                    _bottomdelete.setText(getSelects().size() + "");
                }
            });
            if (showdeltet) {
                holder._delete.setVisibility(View.VISIBLE);
                holder._delete.setChecked(selects.get(i));
                if (selects.get(i).equals("true") || selects.get(i).toString() == "true") {
                    a = 1;
                } else {
                    a = 0;
                }
            } else {
                holder._delete.setVisibility(View.INVISIBLE);
            }
            return view;
        }

    }

    class ViewHolder {
        TextView _note;
        CheckBox _delete;
    }

    @Override
    public void onCheckedChanged(RadioGroup radiogroup, int i) {
        if (notes == null)
            return;
        switch (radiogroup.getCheckedRadioButtonId()) {
            case R.id.rb_translate:
                adapter.setNotes(notes_translate);
                break;
            case R.id.rb_analyse:
                adapter.setNotes(notes_analyse);
                break;
            case R.id.rb_word:
                adapter.setNotes(notes_word);
                break;
        }

    }
}
