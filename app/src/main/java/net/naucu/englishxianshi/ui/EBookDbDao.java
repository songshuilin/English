package net.naucu.englishxianshi.ui;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Y on 2016/9/23.
 */
public class EBookDbDao {
    private EBookDbHelper dbHelper;
    private SQLiteDatabase db;
    private Context context;

    public EBookDbDao(Context context) {
//        if (dbHelper == null){
        this.context = context;
        this.dbHelper = new EBookDbHelper(context);
        //  this.db=dbHelper.getReadableDatabase();
//        }
    }

    public void add(String name, String content, String type) {
        Log.i("-----", "name = " + name);
        Log.i("-----", "content = " + content);
        Log.i("-----", "type = " + type);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        database.execSQL("INSERT INTO " + EBookDbHelper.TABLE_NAME
                + " VALUES(NULL,?, ?,?)", new String[]{name,
                content, type});
        database.close();


    }

    public String select(String name, String type) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + EBookDbHelper.TABLE_NAME + " where name = ? and type = ? ", new String[]{name, type});
        String value = "";
        if (cursor != null) {
            while (cursor.moveToNext()) {
                value = cursor.getString(cursor.getColumnIndex("content"));
                System.out.println("00000000000000 ---- 333" + "+++" + cursor.getColumnName(0) + "++");
                db.close();
                return value;
            }
        }
        db.close();
        return value;
    }


}
