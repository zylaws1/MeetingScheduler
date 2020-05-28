package com.example.xinshen.comp2100_meetingschedule.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
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
    public static String DB_PATH="data/data/com.example.xinshen.comp2100_meetingschedule/NoteBook.db";

    private NoteDBManager(Context context) {
        MeetingSQLiteOpenHelper helper = new MeetingSQLiteOpenHelper(context, DB_PATH, null, 1);
        db = helper.getWritableDatabase();
    }

    public static NoteDBManager getInstance(Context context) {
        if (instance == null) {
            synchronized (MeetingDbManager.class) {
                if (instance == null) {
                    instance = new NoteDBManager(context);
                }
            }
        }
        return instance;
    }

    public Cursor queryAll() {
        return db.rawQuery("select * from NoteBook ", null);
    }

    public Cursor queryNoteById(Integer id) {
        return db.rawQuery("select * from NoteBook where _id =?", new String[]{id.toString()});
    }

    public void deleteNoteById(Integer id) {
        if (id == null)
            return;
        db.delete("NoteBook", "_id=?", new String[]{id.toString()});
    }

    public void updateNoteById(Integer id, ContentValues values) {
        db.update("NoteBook", values, "_id=?", new String[]{id.toString()});
    }

    /**
     * Add a note, and record the time of the current addition
     *
     * @param values Each field value in the table
     */
    public void addNote(ContentValues values) {
        Log.i("shenxin", "addNote: " + values.toString());
        values.put("createTime", DateFormat.format("yyyy-MM-dd kk:mm:ss", System.currentTimeMillis()).toString());
        long res = db.insert("NoteBook", null, values);
        Log.i("shenxin", "addNote res: " + res);
    }
}
