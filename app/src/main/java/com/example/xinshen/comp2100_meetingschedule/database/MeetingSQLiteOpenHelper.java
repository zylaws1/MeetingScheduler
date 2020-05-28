package com.example.xinshen.comp2100_meetingschedule.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MeetingSQLiteOpenHelper extends SQLiteOpenHelper {
    public static String DB_NAME = "NoteBook.db";

    public MeetingSQLiteOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table NoteBook(_id integer primary key autoincrement,title varchar(255),content TEXT, createTime varchar(25))";
        // Determine whether there is a table NoteBook, if it does not exist will throw an exception, create the table after catching the exception
        try {
            db.rawQuery("select count(1) from NoteBook ", null);
        } catch (Exception e) {
            db.execSQL(sql);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            String dropsql = "DROP TABLE IF EXISTS " + DB_NAME;
            db.execSQL(dropsql);
            onCreate(db);
        }
    }
}
