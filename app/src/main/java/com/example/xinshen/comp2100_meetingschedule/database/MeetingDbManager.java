package com.example.xinshen.comp2100_meetingschedule.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.xinshen.comp2100_meetingschedule.MeetingApplication;
import com.example.xinshen.comp2100_meetingschedule.data.model.UserInfo;

public class MeetingDbManager {
    private static volatile MeetingDbManager instance;

    public static String DB_NAME = "person.db";
    SQLiteDatabase dB;

    private MeetingDbManager() {
        SqliteDatabaseHelper helper = new SqliteDatabaseHelper(MeetingApplication.mContext, DB_NAME, null, 1);
        dB = helper.getWritableDatabase();
    }

    public static MeetingDbManager getInstance() {
        if (instance == null) {
            synchronized (MeetingDbManager.class) {
                if (instance == null) {
                    instance = new MeetingDbManager();
                }
            }
        }
        return instance;
    }

    public boolean insertUserInfo(UserInfo userInfo) {
        if (userInfo == null) {
            return false;
        }
        try {
            ContentValues values=new ContentValues();
            values.put("name",userInfo.getDisplayName());
            values.put("password",userInfo.getPassword());
            values.put("age",userInfo.getAge());
            values.put("gender",userInfo.getGender());
            values.put("phone",userInfo.getPhone());
            values.put("email",userInfo.getEmail());
            dB.insert(SqliteDatabaseHelper.TABLE_NAME_USER,null,values);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public UserInfo queryUserInfo(String username) {
        try {
            UserInfo info = null;
            Cursor cursor = dB.query(SqliteDatabaseHelper.TABLE_NAME_USER, new String[]{"name", "password", "age", "gender", "phone", "email"}, "name=?", new String[]{username}, null, null, null);
            if (cursor.getCount() <= 0) {
                return null;
            }
            info = new UserInfo();
            while (cursor.moveToNext()) {
                info.setDisplayName(cursor.getString(cursor.getColumnIndex("name")));
                info.setPassword(cursor.getString(cursor.getColumnIndex("password")));
                info.setAge(cursor.getInt(cursor.getColumnIndex("age")));
                info.setGender(cursor.getInt(cursor.getColumnIndex("gender")));
                info.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
                info.setEmail(cursor.getString(cursor.getColumnIndex("email")));
                Log.d("abao:","name=="+info.getDisplayName());
                Log.d("abao:","password=="+info.getPassword());
                Log.d("abao:","age=="+info.getAge());
                Log.d("abao:","phone=="+info.getPhone());
                Log.d("abao:","email=="+info.getEmail());
            }
            return info;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean updateUserInfo(UserInfo info) {
        try {
            ContentValues values = new ContentValues();
            values.put("age", info.getAge());
            values.put("gender", info.getGender());
            values.put("phone", info.getPhone());
            values.put("email", info.getEmail());
            String whereClause = "name=?";
            dB.update(SqliteDatabaseHelper.TABLE_NAME_USER, values, whereClause, new String[]{info.getDisplayName()});
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateUserPassword(String password) {
        try {
            ContentValues values = new ContentValues();
            values.put("password", password);
            String whereClause = "name=?";
            String[] whereArgs = {String.valueOf(1)};
            dB.update(SqliteDatabaseHelper.TABLE_NAME_USER, values, whereClause, whereArgs);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
