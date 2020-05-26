package com.example.xinshen.comp2100_meetingschedule.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.format.DateFormat;
import android.util.Log;

import com.example.xinshen.comp2100_meetingschedule.MeetingApplication;


/**
 * Data management class, providing operations on the database for notes
 *
 * @author Xin Shen, Shaocong Lang
 */
public class NoteDBManager {
    private static SQLiteDatabase db = null;
    private static volatile NoteDBManager instance;
    public static String DB_NAME = "NoteBook.db";

    private NoteDBManager() {
//        SqliteDatabaseHelper helper = new SqliteDatabaseHelper(MeetingApplication.mContext, DB_NAME, null, 1);
//        db = helper.getWritableDatabase();
    }

    public static NoteDBManager getInstance() {
        if (instance == null) {
            synchronized (MeetingDbManager.class) {
                if (instance == null) {
                    instance = new NoteDBManager();
                }
            }
        }
        return instance;
    }

    static {
        // Create and open db
        db = SQLiteDatabase.openOrCreateDatabase("data/data/com.example.xinshen.comp2100_meetingschedule/NoteBook.db", null);
        String sql = "create table NoteBook(_id integer primary key autoincrement,title varchar(255),content TEXT, createTime varchar(25))";
        // Determine whether there is a table NoteBook, if it does not exist will throw an exception, create the table after catching the exception
        try {
            db.rawQuery("select count(1) from NoteBook ", null);
        } catch (Exception e) {
            db.execSQL(sql);
        }
    }

    public static Cursor queryAll() {
        return db.rawQuery("select * from NoteBook ", null);
    }

    public static Cursor queryNoteById(Integer id) {
        return db.rawQuery("select * from NoteBook where _id =?", new String[]{id.toString()});
    }

    public static void deleteNoteById(Integer id) {
        if (id == null)
            return;
        db.delete("NoteBook", "_id=?", new String[]{id.toString()});
    }

    public static void updateNoteById(Integer id, ContentValues values) {
        db.update("NoteBook", values, "_id=?", new String[]{id.toString()});
    }

    /**
     * Add a note, and record the time of the current addition
     *
     * @param values Each field value in the table
     */
    public static void addNote(ContentValues values) {
        Log.i("shenxin", "addNote: " + values.toString());
        values.put("createTime", DateFormat.format("yyyy-MM-dd kk:mm:ss", System.currentTimeMillis()).toString());
        long res = db.insert("NoteBook", null, values);
        Log.i("shenxin", "addNote res: " + res);
    }
}
