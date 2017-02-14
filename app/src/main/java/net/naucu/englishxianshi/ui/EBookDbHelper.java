package net.naucu.englishxianshi.ui;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Y on 2016/9/23.
 */
public class EBookDbHelper extends SQLiteOpenHelper {
    // 数据库版本号
    private static final int DATABASE_VERSION = 1;
    // 数据库名
    private static final String DATABASE_NAME = "TestDB.db";
    // 数据表名，一个数据库中可以有多个表（虽然本例中只建立了一个表）
    public static final String TABLE_NAME = "PersonTable";


    public EBookDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        StringBuffer sBuffer = new StringBuffer();

        sBuffer.append("CREATE TABLE [" + TABLE_NAME + "] (");
        sBuffer.append("[_id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, ");
        sBuffer.append("[name] varchar(200),");
        sBuffer.append("[content] varchar(2000),");
        sBuffer.append("[type] varchar(20))");
        sqLiteDatabase.execSQL(sBuffer.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

}
