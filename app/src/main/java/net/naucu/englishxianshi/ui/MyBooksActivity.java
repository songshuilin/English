package net.naucu.englishxianshi.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.myTool.ActionTitleBarWidget;
import com.app.myTool.ActionTitleBarWidget.ClickListener;
import com.lijunsai.httpInterface.bean.ALLbookDetailsBean;

import net.naucu.englishxianshi.R;
import net.naucu.englishxianshi.adapter.DownloadHomeBooksAdapter;
import net.naucu.englishxianshi.adapter.DownloadHomeBooksAdapter.OnChecked;
import net.naucu.englishxianshi.db.Manager;
import net.naucu.englishxianshi.db.dao.Books;
import net.naucu.englishxianshi.ui.base.BaseActivity;
import net.naucu.englishxianshi.widget.dialog.PromptDialog;
import net.naucu.englishxianshi.widget.dialog.PromptDialog.onPromptClickListener;

import java.io.File;
import java.util.List;

/**
 * 类名: MyBooksActivity.java
 * 描述: TODO 我的书架
 * 作者: youyou_pc
 * 时间: 2015年11月20日  下午3:57:27
 */
public class MyBooksActivity extends BaseActivity implements OnClickListener {
    private ActionTitleBarWidget titlebar;//初始化标题控件
    private List<Books> mybooks;
    private DownloadHomeBooksAdapter downloadHomeBooksAdapter;
    private boolean isAccording = false;
    private GridView gv_myview;
    private LinearLayout bottom_choice;
    private PromptDialog dialog;
    private ALLbookDetailsBean alLbookDetailsBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mygridview);
        initView();
        initDate();
        initTitleBar();
        initEvent();
    }

    private void initView() {
        titlebar = (ActionTitleBarWidget) findViewById(R.id.titlebar);
        gv_myview = (GridView) findViewById(R.id.gv_myview);
        bottom_choice = (LinearLayout) findViewById(R.id.bottom_choice);
    }

    /**
     * 初始化TitleBar
     */
    private void initTitleBar() {
        isTitleRight();
        titlebar.setLeftGravity(Gravity.CENTER);
        titlebar.setLeftIco(R.drawable.back);
        titlebar.setRightGravity(Gravity.CENTER);
        titlebar.setCenterText(getString(R.string.tx_Mybookshelf), 17, Color.BLACK);
        titlebar.setCenterGravity(Gravity.CENTER);
        titlebar.setTitleBarBackgroundColor(Color.WHITE);
    }

    private void initEvent() {
        titlebar.OnTitleBarClickListener(clickListener);
        bottom_choice.setOnClickListener(this);
        gv_myview.setOnItemClickListener(itemClickListener);
    }

    private OnItemClickListener itemClickListener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
//			if(application.getLoginApplication().getState()==null||
//					!application.getLoginApplication().getState().equals("0")){
//				Toast.makeText(getApplicationContext(), getResources().getText(R.string.paynotify), 2).show();
//				return;
//			}
            dialog = new PromptDialog(MyBooksActivity.this);
            alLbookDetailsBean = new ALLbookDetailsBean();
            alLbookDetailsBean.setId(mybooks.get(position).getDownloadId());
            alLbookDetailsBean.setBookImage(mybooks.get(position).getPic());
            alLbookDetailsBean.setBookName(mybooks.get(position).getBooksName());
            alLbookDetailsBean.setBookAuthor(mybooks.get(position).getBooksAuthor());
            alLbookDetailsBean.setBookpingfen(mybooks.get(position).getScore());
            if (mybooks.get(position).getBookcaseStatus().equals("2")) {
                if (mybooks.get(position).getBooksLocalUrl() != null) {
                    File file = new File(mybooks.get(position).getBooksLocalUrl());
                    if (file.exists()) {
                        EBookActivity.startActivity(MyBooksActivity.this, mybooks.get(position));
                    } else {
                        dialog.setContent("《" + mybooks.get(position).getBooksName() + "》文件丢失是否重新下载?");
                        dialog.setClickListener(clickListener2);
                        dialog.show();
                    }
                }
            } else if (mybooks.get(position).getBookcaseStatus().equals("3")) {
                dialog.setContent("《" + mybooks.get(position).getBooksName() + "》没有下载完整,是否继续下载?");
                dialog.setClickListener(clickListener2);
                dialog.show();
            } else {
                dialog.setContent("本地没有找到《" + mybooks.get(position).getBooksName() + "》是下载?");
                dialog.setClickListener(clickListener2);
                dialog.show();
            }
        }
    };
    private onPromptClickListener clickListener2 = new onPromptClickListener() {

        @Override
        public void onDetermine(View v) {
            dialog.dismiss();
            startActivity(new Intent(MyBooksActivity.this, BooksIntroducedActivity.class).putExtra("booksInformation", alLbookDetailsBean).putExtra("isbooks", true));
        }

        @Override
        public void onCancel(View v) {
            dialog.dismiss();

        }
    };
    /**
     * TitleBar左右按钮点击事件
     */
    private ClickListener clickListener = new ClickListener() {

        @Override
        public void onright(View v) {
            if (mybooks != null) {
                if (mybooks.size() != 0) {
                    isAccordings(isAccording);
                }
            }


        }

        @Override
        public void onleft(View v) {
            finish();
        }

        @Override
        public void oncenter(View arg0) {
            // TODO Auto-generated method stub

        }
    };

    private void initDate() {
        selectview(false);
    }

    /**
     * 统计
     */
    private OnChecked checked = new OnChecked() {

        @Override
        public void Count(int i) {
            ((TextView) findViewById(R.id.tv_counts)).setText("(" + i + ")");
        }
    };

    private void selectview(boolean b) {
        mybooks = Manager.SelectBooks();
        if (mybooks != null) {
            if (mybooks.size() != 0) {
                downloadHomeBooksAdapter = new DownloadHomeBooksAdapter(application, mybooks, b);
                downloadHomeBooksAdapter.setOnChecked(checked);
                gv_myview.setAdapter(downloadHomeBooksAdapter);
                isnodate(true);
            } else {
                isnodate(false);
            }
        } else {
            isnodate(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bottom_choice:
                if (((TextView) findViewById(R.id.tv_counts)).getText().equals("(0)") || ((TextView) findViewById(R.id.tv_counts)).getText() == "(0)") {
                    Toast toast = Toast.makeText(MyBooksActivity.this, "请选择要删除的图书", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {
                    final PromptDialog dialog = new PromptDialog(this);
                    dialog.setContent(getString(R.string.tx_deleteinformation));
                    dialog.show();
                    dialog.setClickListener(new onPromptClickListener() {

                        @Override
                        public void onDetermine(View v) {
                            downloadHomeBooksAdapter.SelectedDelete(application);
                            selectview(false);
                            isAccordings(true);
                            dialog.dismiss();
                        }

                        @Override
                        public void onCancel(View v) {
                            dialog.dismiss();
                        }
                    });
                }
                break;
        }
    }

    public void isAccordings(Boolean isAccording) {
        titlebar.RightViewRemove();
        if (isAccording) {
            selectview(false);
            isTitleRight();
            bottom_choice.setVisibility(View.GONE);
            this.isAccording = false;
        } else {
            titlebar.setRightText(getString(R.string.tx_cancel), 17, Color.BLACK);
            bottom_choice.setVisibility(View.VISIBLE);
            selectview(true);
            this.isAccording = true;
        }
    }

    private void isTitleRight() {
        if (mybooks != null) {
            if (mybooks.size() != 0) {
                titlebar.setTitleBackgroundResource(R.drawable.menubutton_select, R.drawable.menubutton_select);
                titlebar.setRightText(getString(R.string.tx_editor), 17, Color.BLACK);
            } else {
                titlebar.setTitleBackgroundResource(R.drawable.menubutton_select, R.color.transparent);
                titlebar.setRightText("", 17, Color.BLACK);
            }
        }
    }

    @Override
    protected void onResume() {
        selectview(false);
        super.onResume();
    }
}
