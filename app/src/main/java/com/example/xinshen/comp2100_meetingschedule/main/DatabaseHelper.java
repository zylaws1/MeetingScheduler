

package com.example.xinshen.comp2100_meetingschedule.main;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Meetings_info.db";

    private static final String USER_TABLE = "UserTable";
    private static final String USER_COL1 = "userID";
    private static final String USER_COL2 = "userName";
    private static final String USER_COL3 = "realName";
    private static final String USER_COL4 = "gender";
    private static final String USER_COL5 = "age";
    private static final String USER_COL6 = "email";
    private static final String USER_COL7 = "password";

    private static final String LESSON_TABLE = "LessonTable";
    private static final String LESSON_COL1 = "lessonID";
    private static final String LESSON_COL2 = "lessonName";
    private static final String LESSON_COL3 = "lessonTime";
    private static final String LESSON_COL4 = "description";

    private static final String LESSON_OWNER_TABLE = "LessonOwnerTable";
    private static final String LESSON_OWNER_COL1 = "lessonID";
    private static final String LESSON_OWNER_COL2 = "userID";

    private static final String LESSON_STUDENT_TABLE = "LessonStudentTable";
    private static final String LESSON_STUDENT_COL1 = "lessonID";
    private static final String LESSON_STUDENT_COL2 = "studentID";
    private static final String LESSON_STUDENT_COL3 = "score";

    private static final String PUBLISH_TABLE = "PublishTable";
    private static final String PUBLISH_COL1 = "publishID";
    private static final String PUBLISH_COL2 = "userID";
    private static final String PUBLISH_COL3 = "time";
    private static final String PUBLISH_COL4 = "content";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUserTable = "CREATE TABLE " + USER_TABLE + " ( "
                + USER_COL1 + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                + USER_COL2 + " CHAR(50) NOT NULL, "
                + USER_COL3 + " CHAR(50) NOT NULL, "
                + USER_COL4 + " CHAR(10) NOT NULL, "
                + USER_COL5 + " INTEGER NOT NULL, "
                + USER_COL7 + " CHAR(50) NOT NULL, "
                + USER_COL6 + " CHAR(50) NOT NULL UNIQUE) ";
        db.execSQL(createUserTable);

        String createLessonTable = "CREATE TABLE " + LESSON_TABLE + " ( "
                + LESSON_COL1 + " CHAR(50) PRIMARY KEY NOT NULL, "
                + LESSON_COL2 + " CHAR(50) NOT NULL, "
                + LESSON_COL3 + " CHAR(100) NOT NULL, "
                + LESSON_COL4 + " TEXT NOT NULL) ";
        db.execSQL(createLessonTable);

        String createLessonOwnerTable = "CREATE TABLE " + LESSON_OWNER_TABLE + " ( "
                + LESSON_OWNER_COL1 + " INTEGER NOT NULL, "
                + LESSON_OWNER_COL2 + " INTEGER NOT NULL,"
                + "PRIMARY KEY(" + LESSON_OWNER_COL1 + "," + LESSON_OWNER_COL2 + "),"
                + "FOREIGN KEY(" + LESSON_OWNER_COL2 + ") REFERENCES " + USER_TABLE + "(" + USER_COL1 + "),"
                + "FOREIGN KEY(" + LESSON_OWNER_COL1 + ") REFERENCES " + LESSON_TABLE + "(" + LESSON_COL1 + ")"
                + ")";
        db.execSQL(createLessonOwnerTable);

        String createLessonStuTable = "CREATE TABLE " + LESSON_STUDENT_TABLE + " ( "
                + LESSON_STUDENT_COL1 + " CHAR(50) NOT NULL, "
                + LESSON_STUDENT_COL2 + " INTEGER NOT NULL, "
                + LESSON_STUDENT_COL3 + " INTEGER , "
                + "PRIMARY KEY(" + LESSON_STUDENT_COL1 + "," + LESSON_STUDENT_COL2 + "),"
                + "FOREIGN KEY(" + LESSON_STUDENT_COL2 + ") REFERENCES " + USER_TABLE + "(" + USER_COL1 + "),"
                + "FOREIGN KEY(" + LESSON_STUDENT_COL1 + ") REFERENCES " + LESSON_TABLE + "(" + LESSON_COL1 + ")"
                + ")";
        db.execSQL(createLessonStuTable);

        String createPublishTable = "CREATE TABLE " + PUBLISH_TABLE + " ( "
                + PUBLISH_COL1 + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + PUBLISH_COL2 + " INTEGER  NOT NULL,"
                + PUBLISH_COL3 + " DATETIME  NOT NULL,"
                + PUBLISH_COL4 + " CHAR(50)  NOT NULL,"
                + "FOREIGN KEY(" + PUBLISH_COL2 + ") REFERENCES " + USER_TABLE + "(" + USER_COL1 + ")"
                + ")";
        db.execSQL(createPublishTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + LESSON_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + LESSON_OWNER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + LESSON_STUDENT_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + PUBLISH_TABLE);
        onCreate(db);

    }

    /**
     * @return
     */
    public boolean addPublish(Publish publish) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PUBLISH_COL2, publish.getUserID());
        contentValues.put(PUBLISH_COL3, publish.getDate().toString());
        contentValues.put(PUBLISH_COL4, publish.getContent());

        long result = db.insert(PUBLISH_TABLE, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean selectLesson(int stuID, String lessonID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(LESSON_STUDENT_COL1, lessonID);
        contentValues.put(LESSON_STUDENT_COL2, stuID);


        long result = db.insert(LESSON_STUDENT_TABLE, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public void setScore(int stuID, String lessonID, int score) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        String sql = "UPDATE lessonstudenttable SET score =? WHERE lessonID = ? and studentID = ?";
        db.rawQuery(sql, new String[]{String.valueOf(score), lessonID, String.valueOf(stuID)});
    }


    /**
     * @param email
     * @param password
     * @return the userID in int
     */
    public boolean login(String email, String password) {
        SQLiteDatabase sdb = this.getReadableDatabase();

        String sql = "select * from user where username=? and password=?";

        Cursor cursor = sdb.rawQuery(sql, new String[]{email, password});

        if (cursor.moveToFirst() == true) {

            cursor.close();

            return true;

        }else {
            cursor.close();
            return false;
        }
    }


    public Cursor getLessonTable(int stuID) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("select lessonTable.lessonID, lessonTable.lessonName, lessonTable.lessonTime, lessonTable.description\n" +
                "    from lessonTable, lessonstudenttable\n" +
                "    where lessonstudenttable.studentID = " +
                stuID + " and lessonstudenttable.lessonID = lessonTable.lessonID;", null);
        return data;
    }

    public Cursor searchLessonByKeyword(String keyword) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "select * from lessontable " +
                "where lessonid like '%" + keyword + "%' " +
                "or lessonname like '%" + keyword + "%' " +
                "or description like '%" + keyword + "%';";
        Cursor data = db.rawQuery(sql, null);
        return data;
    }

    /**
     * user for the
     *
     * @return all data of specific lesson
     */
    public Cursor searchLessonByID(String lessonID) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + LESSON_TABLE + " where lessonid =?", new String[]{lessonID});
        return data;
    }

    /**
     * @return all the publish from the newest to oldest
     */
    public Cursor getPublish() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + PUBLISH_TABLE + " order by time desc", null);
        return data;
    }

    public int deleteAll(String table_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        int deleted_num = db.delete(table_name, null, null);
        return deleted_num;//return deleted number of datas
    }


}
