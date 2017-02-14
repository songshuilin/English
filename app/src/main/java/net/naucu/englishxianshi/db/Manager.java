package net.naucu.englishxianshi.db;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.jiongbull.jlog.JLog;

import net.naucu.englishxianshi.bean.MyRecordBean;
import net.naucu.englishxianshi.db.dao.Books;
import net.naucu.englishxianshi.db.dao.Movie;
import net.naucu.englishxianshi.db.dao.MyNote;
import net.naucu.englishxianshi.db.dao.Record;

import org.xutils.DbManager;
import org.xutils.DbManager.DaoConfig;
import org.xutils.common.util.KeyValue;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 类名: Manager.java
 * 描述: 对数据库的操作
 * 作者: fght
 * <p/>
 * 时间: 2015年12月17日  上午10:24:43
 */
public class Manager {
    private static DbManager.DaoConfig daoConfig;

    private static void initdb() {
        if (daoConfig == null) {
            daoConfig = new DaoConfig().setDbName("Naucu").setDbVersion(1).setAllowTransaction(false).setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                @Override
                public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                    System.out.println("onUpgrade");
                }
            });
//			x.getDb(daoConfig).getDaoConfig();
        }
    }

    //电子书操作
    //电子书查询全部
    public static List<Books> SelectBooks() {
        initdb();
        DbManager db = x.getDb(daoConfig);
        List<Books> books2 = new ArrayList<>();
        try {
            books2 = db.selector(Books.class).findAll();
        } catch (DbException e) {
            JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());
        }
        return books2;
    }

    /**
     * 电子书id
     *
     * @param
     * @return
     */
    public static Books SelectIdBooks(String downloadId) {
        initdb();
        DbManager db = x.getDb(daoConfig);
        Books books2 = new Books();
        try {
            books2 = db.selector(Books.class).where("DOWNLOADID", "=", downloadId).findFirst();
        } catch (DbException e) {
            JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());
        }
        return books2;
    }

    public static void InsertBooks(Books books) {
        if (books == null) return;
        initdb();
        DbManager db = x.getDb(daoConfig);
        if (SelectIdBooks(books.getDownloadId()) != null) {
            try {
                KeyValue[] keyValues = {new KeyValue("LASTPOSITION", books.getLastPosition()),
                        new KeyValue("DOWNLOADSTATUS", books.getDownloadStatus()),
                        new KeyValue("BOOKSCASESTATUS", books.getBookcaseStatus()),
                        new KeyValue("BOOKSLOCALURL", books.getBooksLocalUrl())};
                db.update(Books.class, WhereBuilder.b("DOWNLOADID", "=", books.getDownloadId()), keyValues);
            } catch (DbException e) {
                JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());
            }
        } else {
            try {
                db.save(books);
            } catch (DbException e) {
                JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());
            }
        }
    }

    public static void DeleteBooks(int downloadId) {
        initdb();
        DbManager db = x.getDb(daoConfig);
        try {
            db.delete(Books.class, WhereBuilder.b("DOWNLOADID", "=", downloadId));
        } catch (DbException e) {
            JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());
        }

    }

    public static void DeleteMyBooks(int id) {
        initdb();
        DbManager db = x.getDb(daoConfig);
        try {
            db.delete(Books.class, WhereBuilder.b("Id", "=", id));
        } catch (DbException e) {
            JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());
        }

    }

    //查询所有电影
    public static List<Movie> SelectMovie() {
        initdb();
        DbManager db = x.getDb(daoConfig);
        List<Movie> movies = new ArrayList<>();
        try {
            movies = db.selector(Movie.class).findAll();
        } catch (DbException e) {
            JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());
        }
        return movies;
    }

    //根据id查电影
    public static Movie SelectMovieId(int downloadId) {
        initdb();
        DbManager db = x.getDb(daoConfig);
        Movie movie = new Movie();
        try {
            movie = db.selector(Movie.class).where("DOWNLOADID", "=", downloadId).findFirst();
        } catch (DbException e) {
            JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());
        }
        return movie;
    }

    //电影插入数据
    public static void InsertMovie(Movie movie) {
        if (movie == null) return;
        initdb();
        DbManager db = x.getDb(daoConfig);
        if (SelectMovieId(movie.getDownloadId()) != null) {
            try {
                KeyValue[] keyValues = {new KeyValue("LASTPOSITION", movie.getLastPosition()),
                        new KeyValue("YINGKUIDENTIFIER", movie.getYingKuIdentifier())};
                db.update(Movie.class, WhereBuilder.b("DOWNLOADID", "=", movie.getDownloadId()), keyValues);
            } catch (DbException e) {
                JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());
            }
        } else {
            try {
                db.save(movie);
            } catch (DbException e) {
                JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());
            }
        }
    }

    public static void InsertRecord(Record record) {
        if (record == null) return;
        initdb();
        DbManager db = x.getDb(daoConfig);
        if (selectRecordId(record.getId()) == null) {
            try {
                db.save(record);
                Log.i("TAG", "EDGEDG_RECORD = " + record);
            } catch (DbException e) {
                JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());
            }
        } else {
            try {
                db.update(Record.class, WhereBuilder.b("ID", "=", record.getId()), new KeyValue("INTRODUCTION", record.getIntroduction()));
            } catch (DbException e) {
                JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());
            }
        }

    }

    public static void InsertRecord(MyRecordBean bean) {
        if (bean == null) return;
        InsertRecord(MyRecordBean2Record(bean));
    }

    public static List<Record> sleectRecords() {
        initdb();
        List<Record> records = null;
        DbManager db = x.getDb(daoConfig);
        try {
            records = db.selector(Record.class).findAll();
            Log.i("TAG" , "EDGEDG_RECORD Return =" + records);
        } catch (DbException e) {
            JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());
        }
        return records;
    }

    public static Record selectRecordId(int id) {
        initdb();
        Record record = null;
        DbManager db = x.getDb(daoConfig);
        try {
            record = db.selector(Record.class).where("Id", "=", id).findFirst();
        } catch (DbException e) {
            JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());
        }
        return record;
    }

    public static void DeleteRecord(int id) {
        Log.i("TAG", "46546531657416574641 = " + id);
        initdb();
        DbManager db = x.getDb(daoConfig);
        try {
            Record record = selectRecordId(id);
            if (record != null) {
                db.delete(Record.class, WhereBuilder.b("Id", "=", id));
                List<String> recordPaths = JSONObject.parseArray(record.getRecordPaths(), String.class);
                for (String path : recordPaths) {
                    File file = new File(path);
                    if (file.exists()) file.delete();
                }
            }
        } catch (DbException e) {
            JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());
        }

    }

    public static void DeleteMyMovie(int id) {
        initdb();
        DbManager db = x.getDb(daoConfig);
        try {
            db.delete(Movie.class, WhereBuilder.b("DOWNLOADID", "=", id));
        } catch (DbException e) {
            JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());
        }
    }

    public static void deleteMovieDownloadId(int id) {
        initdb();
        DbManager db = x.getDb(daoConfig);
        try {
            db.delete(Movie.class, WhereBuilder.b("DOWNLOADID", "=", id));
        } catch (DbException e) {
            JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());
        }
    }

    public static void InsertNote(MyNote note) {
        if (note == null) return;
        initdb();
        DbManager db = x.getDb(daoConfig);
        if (SelectNoteId(note.getId()) == null) {
            try {
                db.save(note);
            } catch (DbException e) {
                JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());
            }
        } else {
            try {
                db.update(MyNote.class, WhereBuilder.b("ID", "=", note.getId()), new KeyValue("UPDATETIME", System.currentTimeMillis()));
            } catch (DbException e) {
                JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());
            }
        }
    }

    public static MyNote SelectNoteId(int id) {
        initdb();
        MyNote myNote = null;
        DbManager db = x.getDb(daoConfig);
        try {
            myNote = db.selector(MyNote.class).where("Id", "=", id).findFirst();
        } catch (DbException e) {
            JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());
        }
        return myNote;
    }

    public static MyNote SelectNoteContent(String content, int type) {
        initdb();
        MyNote myNote = null;
        DbManager db = x.getDb(daoConfig);
        try {
            myNote = db.selector(MyNote.class).where("CONTENT", "=", content)
                    .and("TYPE", "=", type).findFirst();
        } catch (DbException e) {
            JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());
            return null;
        }
        return myNote;
    }

    public static List<MyNote> SelectNotes() {
        initdb();
        List<MyNote> myNotes = null;
        DbManager db = x.getDb(daoConfig);
        try {
            myNotes = db.selector(MyNote.class).findAll();
        } catch (DbException e) {
            JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());
        }
        return myNotes;
    }

    public static void DeleteNote(int id) {
        initdb();
        DbManager db = x.getDb(daoConfig);
        try {
            db.delete(MyNote.class, WhereBuilder.b("Id", "=", id));
        } catch (DbException e) {
            JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());
        }
    }

    public static void DeleteNote(String content, int type) {
        initdb();
        DbManager db = x.getDb(daoConfig);
        try {
            db.delete(MyNote.class, WhereBuilder.b("CONTENT", "=", content).and("TYPE", "=", type));
        } catch (DbException e) {
            JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());
        }
    }

    public static MyRecordBean Record2MyRecordBean(Record bean) {
        if (bean == null) return null;
        MyRecordBean bean2 = new MyRecordBean();
        bean2.setBeginTime(bean.getBeginTime());
        bean2.setDownloadId(bean.getDownloadId());
        bean2.setEndPosition(bean.getEndPosition());
        bean2.setEndTime(bean.getEndTime());
        bean2.setId(bean.getId());
        bean2.setIntroduction(bean.getIntroduction());
        bean2.setRecordPaths(JSONObject.parseArray(bean.getRecordPaths(), String.class));
        bean2.setScore(bean.getScore());
        bean2.setSelectList(JSONObject.parseArray(bean.getSelectList(), Integer.class));
        bean2.setSrtPath(bean.getSrtLocalPath());
        bean2.setStartPosition(bean.getStartPosition());
        bean2.setVideoName(bean.getVideoName());
        bean2.setVideoPath(bean.getVideoLocalPath());
        bean2.setMp3Path(bean.getMp3LocalPath());
        bean2.setVideoPictureUrl(bean.getVideoPictureUrl());
        return bean2;
    }

    public static List<MyRecordBean> Records2MyRecordBeans(List<Record> beans) {
        if (beans == null) return null;
        List<MyRecordBean> beans2 = new ArrayList<MyRecordBean>();
        for (Record record : beans) {
            beans2.add(Record2MyRecordBean(record));
        }
        return beans2;
    }

    public static Record MyRecordBean2Record(MyRecordBean bean) {
        if (bean == null) return null;
        Record bean2 = new Record();
        bean2.setBeginTime(bean.getBeginTime());
        bean2.setDownloadId(bean.getDownloadId());
        bean2.setEndPosition(bean.getEndPosition());
        bean2.setEndTime(bean.getEndTime());
        bean2.setId(bean.getId());
        bean2.setIntroduction(bean.getIntroduction());
        bean2.setRecordPaths(JSONObject.toJSONString(bean.getRecordPaths()));
        bean2.setScore(bean.getScore());
        bean2.setSelectList(JSONObject.toJSONString(bean.getSelectList()));
        bean2.setSrtLocalPath(bean.getSrtPath());
        bean2.setStartPosition(bean.getStartPosition());
        bean2.setVideoName(bean.getVideoName());
        bean2.setVideoLocalPath(bean.getVideoPath());
        bean2.setVideoPictureUrl(bean.getVideoPictureUrl());
        return bean2;
    }

    public static List<Record> MyRecordBeans2Records(List<MyRecordBean> beans) {
        if (beans == null) return null;
        List<Record> beans2 = new ArrayList<Record>();
        for (MyRecordBean record : beans) {
            beans2.add(MyRecordBean2Record(record));
        }
        return beans2;
    }
}
