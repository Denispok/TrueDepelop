package com.example.truedevelop;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.truedevelop.dto.RemindDTO;

import java.util.List;

public class DBHelper extends SQLiteOpenHelper{

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "trueDb";
    public static final String TABLE_NEWS = "news";

    public static final String KEY_ID = "_id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_DATE = "date";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NEWS + "(" + KEY_ID
                + " integer primary key," + KEY_TITLE + " text," + KEY_DATE + " text" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void addRemindDTOList(List<RemindDTO> remindList, DBHelper dbHelper) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        database.delete(DBHelper.TABLE_NEWS, null, null);

        for (int i = 0; i < remindList.size(); i++) {
            ContentValues insertValues = new ContentValues();
            insertValues.put(DBHelper.KEY_ID, remindList.get(i).getId());
            insertValues.put(DBHelper.KEY_TITLE, remindList.get(i).getTitle());
            insertValues.put(DBHelper.KEY_DATE, remindList.get(i).getRemindDate().toString());
            database.insert(TABLE_NEWS, null, insertValues);
        }
    }
}
