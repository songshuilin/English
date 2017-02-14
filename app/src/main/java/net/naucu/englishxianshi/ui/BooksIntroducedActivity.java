package net.naucu.englishxianshi.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.myTool.ActionTitleBarWidget;
import com.app.myTool.ActionTitleBarWidget.ClickListener;
import com.jiongbull.jlog.JLog;
import com.lijunsai.httpInterface.bean.ALLbookDetailsBean;
import com.lijunsai.httpInterface.implementation.HttpImplementation;
import com.lijunsai.httpInterface.request.NetworkRequestDeal;
import com.lijunsai.httpInterface.tool.CardUtils;

import net.naucu.englishxianshi.BaseApplication;
import net.naucu.englishxianshi.R;
import net.naucu.englishxianshi.db.Manager;
import net.naucu.englishxianshi.db.dao.Books;
import net.naucu.englishxianshi.download.DownloadManager;
import net.naucu.englishxianshi.download.DownloadViewHolder;
import net.naucu.englishxianshi.ui.base.BaseActivity;
import net.naucu.englishxianshi.util.ErrorTool;
import net.naucu.englishxianshi.widget.dialog.PromptDialog;
import net.naucu.englishxianshi.widget.dialog.PromptDialog.onPromptClickListener;

import org.xutils.common.Callback.CancelledException;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;

/**
 * 类名: BooksIntroducedActivity.java 描述: 电子书介绍 作者: youyou_pc 时间: 2015年11月21日
 * 下午3:24:32
 */
public class BooksIntroducedActivity extends BaseActivity implements OnClickListener {
    public static final String TAG = BooksIntroducedActivity.class.getSimpleName();
    private ActionTitleBarWidget titlebar;// 初始化标题控件
    private ALLbookDetailsBean detailsBean;
    private TextView tv_booksname;
    private TextView tv_author;
    private TextView tv_score;
    private TextView tv_category;
    private RatingBar rb_score;
    private ImageView Im_CoverImageUrl;
    private TextView tv_Introduction;
    private TextView tv_downloadtext;
    private CheckBox cb_bookcase;
    private Books books;
    private Books booksdb;

    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booksintroduced);
        initView();
        initTitleBar();
        initEvent();
        initDate();
    }

    private void initDate() {
        detailsBean = (ALLbookDetailsBean) getIntent().getSerializableExtra("booksInformation");
        if (detailsBean != null) {
            tv_booksname.setText(detailsBean.getBookName());
            tv_author.setText(detailsBean.getBookAuthor());
            ;
            tv_score.setText(String.valueOf(detailsBean.getBookpingfen()));
            if (getIntent().getStringExtra("Category") != null) {
                tv_category.setText(getIntent().getStringExtra("Category"));
            }
            rb_score.setRating((float) detailsBean.getBookpingfen());
            tv_Introduction.setText(detailsBean.getBookAbout());
            x.image().bind(Im_CoverImageUrl, detailsBean.getBookImage(), BaseApplication.getImageOption());
            titlebar.setCenterText(detailsBean.getBookName(), 17, Color.BLACK);
            books = Manager.SelectIdBooks(detailsBean.getId());
            if (books != null) {
                int i = Integer.valueOf(books.getBookcaseStatus());
                switch (i) {
                    case 1:
                        setbuttonState(3, 0, false);
                        break;
                    case 2:
                        setbuttonState(3, 0, true);
                        break;
                    case 3:
                        setbuttonState(4, 0, true);
                        break;
                    default:
                        setbuttonState(1, 0, true);
                        break;
                }
            }
            booksdb = new Books();
            booksdb.setBooksName(detailsBean.getBookName());
            booksdb.setBooksAuthor(detailsBean.getBookAuthor());
            booksdb.setPic(detailsBean.getBookImage());
            booksdb.setUid(application.getLoginApplication().getUid());
            booksdb.setDownloadId(Integer.parseInt(detailsBean.getId()) + "");
            booksdb.setPaymentStatus(application.getLoginApplication().getState());
            booksdb.setScore(detailsBean.getBookpingfen());
            booksdb.setIntroduction(detailsBean.getBookAbout());
        }
    }

    private void initView() {
        titlebar = (ActionTitleBarWidget) findViewById(R.id.titlebar);
        tv_booksname = (TextView) findViewById(R.id.tv_booksname);
        tv_author = (TextView) findViewById(R.id.tv_author);
        tv_score = (TextView) findViewById(R.id.tv_score);
        tv_category = (TextView) findViewById(R.id.tv_category);
        rb_score = (RatingBar) findViewById(R.id.rb_score);
        Im_CoverImageUrl = (ImageView) findViewById(R.id.Im_CoverImageUrl);
        tv_Introduction = (TextView) findViewById(R.id.tv_Introduction);
        tv_downloadtext = (TextView) findViewById(R.id.tv_downloadtext);
        cb_bookcase = (CheckBox) findViewById(R.id.cb_bookcase);
    }

    private void initTitleBar() {
        titlebar.setTitleBackgroundResource(R.drawable.menubutton_select, R.drawable.menubutton_select);
        titlebar.setLeftGravity(Gravity.CENTER);
        titlebar.setLeftIco(R.drawable.back);
        titlebar.setRightGravity(Gravity.CENTER);
        titlebar.setRightText(getString(R.string.tx_bookcase), 17, Color.BLACK);
        titlebar.setCenterGravity(Gravity.CENTER);
    }

    private OnCheckedChangeListener changeListener = new OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                setbuttonState(3, 0, false);
                if (books == null) {
                    booksdb.setBookcaseStatus(1 + "");
                    Manager.InsertBooks(booksdb);
                }
            } else {
                setbuttonState(1, 0, false);
                if (books != null) {
                    Manager.DeleteBooks(Integer.parseInt(detailsBean.getId()));
                    if (books.getBooksLocalUrl() != null) {
                        new File(books.getBooksLocalUrl()).delete();
                    }
                }
            }
        }
    };
    private ClickListener clickListener = new ClickListener() {

        @Override
        public void onright(View v) {
            boolean isbooks = getIntent().getBooleanExtra("isbooks", false);
            if (isbooks) {
                finish();
            } else {
                startActivity(new Intent(BooksIntroducedActivity.this, MyBooksActivity.class));
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

    private void initEvent() {
        titlebar.OnTitleBarClickListener(clickListener);
        tv_downloadtext.setOnClickListener(this);
        cb_bookcase.setOnCheckedChangeListener(changeListener);
    }

    private void getDownload(String id) {
        RequestParams params = new RequestParams(HttpImplementation.getQueryDownload());
        params.addBodyParameter("info",
                "{'id':'" + id + "','userid':'" + application.getLoginApplication().getUid() + "'}");
        x.http().get(params, new CommonCallback<String>() {
            @Override
            public void onCancelled(CancelledException cex) {
                JLog.e((cex == null || cex.getMessage() == null) ? "Exception is null" : cex.getMessage());
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ErrorTool.onError(BooksIntroducedActivity.this, isOnCallback);
                JLog.e((ex == null || ex.getMessage() == null) ? "Exception is null" : ex.getMessage());
            }

            @Override
            public void onFinished() {
            }

            @Override
            public void onSuccess(String result) {
                String url = NetworkRequestDeal.getQueryDownload(result);
                if (url != null) {
                    booksdb.setBooksLocalUrl(CardUtils.booksPath + booksdb.getBooksName() + ".txt");
                    Download(url, booksdb.getBooksName() + ".txt", booksdb.getBooksLocalUrl());
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        books = Manager.SelectIdBooks(detailsBean.getId());
        switch (v.getId()) {
            case R.id.tv_downloadtext:
                if (detailsBean != null) {
                    if (books != null) {
                        if (books.getDownloadStatus() != null) {
                            if (books.getDownloadStatus().equals("onSuccess")) {
                                if (new File(books.getBooksLocalUrl()).exists()) {
                                    EBookActivity.startActivity(BooksIntroducedActivity.this, books);
                                } else {
                                    final PromptDialog dialog = new PromptDialog(this);
                                    dialog.setContent("文件丢失是否重新下载?");
                                    dialog.show();
                                    dialog.setClickListener(new onPromptClickListener() {

                                        @Override
                                        public void onDetermine(View v) {
                                            getDownload(detailsBean.getId());
                                            dialog.dismiss();
                                        }

                                        @Override
                                        public void onCancel(View v) {
                                            dialog.dismiss();
                                        }
                                    });
                                }
                            } else {
                                getDownload(detailsBean.getId());
                            }
                        } else {
                            getDownload(detailsBean.getId());
                        }
                    } else {
                        getDownload(detailsBean.getId());
                    }
                }
                break;
        }
    }

    private void Download(String bookUrl, String label, String savePath) {
        try {
            DownloadManager.getInstance().startDownload(bookUrl, label, savePath, true, true, new ViewHolder());
        } catch (DbException e) {
            JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());
            Toast.makeText(this, "下载异常,请稍后再试", Toast.LENGTH_LONG).show();
        }
    }

    class ViewHolder extends DownloadViewHolder {

        public ViewHolder() {
            super(null, null);
        }

        @Override
        public void onWaiting() {

        }

        @Override
        public void onStarted() {

        }

        @Override
        public void onLoading(long total, long current) {
            int count = (int) (current * 100 / total);
            setbuttonState(2, count, false);
            booksdb.setDownloadStatus("onWaiting");
            booksdb.setBookcaseStatus(3 + "");
            Manager.InsertBooks(booksdb);

        }

        @Override
        public void onSuccess(File result) {
            setbuttonState(3, 0, true);
            booksdb.setDownloadStatus("onSuccess");
            booksdb.setBookcaseStatus(2 + "");
            Manager.InsertBooks(booksdb);

        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {
            setbuttonState(4, 0, false);
            booksdb.setDownloadStatus("onWaiting");
            booksdb.setBookcaseStatus(3 + "");
            Manager.InsertBooks(booksdb);
            JLog.e((ex == null || ex.getMessage() == null) ? "Exception is null" : ex.getMessage());
        }

        @Override
        public void onCancelled(CancelledException cex) {
            JLog.e((cex == null || cex.getMessage() == null) ? "Exception is null" : cex.getMessage());
        }

    }

    // 设置按钮的状态
    @SuppressLint("NewApi")
    public void setbuttonState(int state, int count, boolean is) {
        Drawable drawable;
        switch (state) {
            case 1:// 没有下载

                if (books!=null) {
                    Manager.DeleteMyBooks(books.getId());
                }
                tv_downloadtext.setText("下载全文");
                tv_downloadtext.setBackgroundResource(R.drawable.shape_books_download);
                //drawable = getResources().getDrawable(R.drawable.bookdownload, null);
                drawable = getResources().getDrawable(R.drawable.bookdownload);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                tv_downloadtext.setCompoundDrawables(drawable, null, null, null);
                cb_bookcase.setChecked(false);
                cb_bookcase.setText("放入书架");
                break;
            case 2:// 下载中
                tv_downloadtext.setText("下载中,已下载" + count + "%");
                tv_downloadtext.setEnabled(false);
                tv_downloadtext.setBackgroundResource(R.drawable.shape_books_downloadpress);
                //drawable = getResources().getDrawable(R.drawable.bookdownload, null);
                drawable = getResources().getDrawable(R.drawable.bookdownload);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                tv_downloadtext.setCompoundDrawables(drawable, null, null, null);
                cb_bookcase.setEnabled(false);
                break;
            case 4:// 下载退出后
                tv_downloadtext.setText("继续下载");
                tv_downloadtext.setBackgroundResource(R.drawable.shape_books_download);
                //drawable = getResources().getDrawable(R.drawable.bookdownload, null);
                drawable = getResources().getDrawable(R.drawable.bookdownload);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                tv_downloadtext.setCompoundDrawables(drawable, null, null, null);
                cb_bookcase.setChecked(false);
                cb_bookcase.setEnabled(false);
                break;
            case 3:// 下载完成
                if (is) {
                    tv_downloadtext.setText("阅读");
                    tv_downloadtext.setEnabled(true);
                    cb_bookcase.setEnabled(true);
                    tv_downloadtext.setBackgroundResource(R.drawable.shape_books_opendownloadpress);
                    tv_downloadtext.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.open_book, 0, 0, 0);
                    //drawable = getResources().getDrawable(R.drawable.open_book, null);
                    drawable = getResources().getDrawable(R.drawable.open_book);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    tv_downloadtext.setCompoundDrawables(drawable, null, null, null);
                }
                cb_bookcase.setChecked(true);
                cb_bookcase.setText("移除书架");
                break;
        }
    }

    @Override
    protected void onResume() {
        if (detailsBean != null) {
            books = Manager.SelectIdBooks(detailsBean.getId());
            if (books == null) {
                setbuttonState(1, 0, true);
            }
        }

        super.onResume();
    }
}