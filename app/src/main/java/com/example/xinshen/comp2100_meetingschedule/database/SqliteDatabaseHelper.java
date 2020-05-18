package com.example.xinshen.comp2100_meetingschedule.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SqliteDatabaseHelper extends SQLiteOpenHelper {
    public static int DB_VERSION = 1;
    public static String TABLE_NAME_USER = "userInfo";
    public static String USER_ID = "user_id";
    public static String NAME = "name";
    public static String NICK_NAME = "nickname";

    public SqliteDatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME_USER + "(" + USER_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, name text,password text,age integer,gender integer,phone text, email text)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            String dropsql = "DROP TABLE IF EXISTS " + TABLE_NAME_USER;
            db.execSQL(dropsql);
            onCreate(db);
        }
    }

}
