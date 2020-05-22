package com.example.xinshen.comp2100_meetingschedule.main;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 *  SQLite Dbhelper for local saving, most functions
 *   are replaced by Google Firebase online server.
 *
 * @author Xin Shen, Shaocong Lang
 */
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

    private static final String MEETING_TABLE = "MeetingTable";
    private static final String MEETING_COL1 = "meetingID";
    private static final String MEETING_COL2 = "meetingName";
    private static final String MEETING_COL3 = "meetingTime";
    private static final String MEETING_COL4 = "description";

    private static final String MEETING_OWNER_TABLE = "MeetingOwnerTable";
    private static final String MEETING_OWNER_COL1 = "meetingID";
    private static final String MEETING_OWNER_COL2 = "userID";

    private static final String MEETING_STUDENT_TABLE = "MeetingStudentTable";
    private static final String MEETING_STUDENT_COL1 = "meetingID";
    private static final String MEETING_STUDENT_COL2 = "studentID";
    private static final String MEETING_STUDENT_COL3 = "score";

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

        String createMeetingTable = "CREATE TABLE " + MEETING_TABLE + " ( "
                + MEETING_COL1 + " CHAR(50) PRIMARY KEY NOT NULL, "
                + MEETING_COL2 + " CHAR(50) NOT NULL, "
                + MEETING_COL3 + " CHAR(100) NOT NULL, "
                + MEETING_COL4 + " TEXT NOT NULL) ";
        db.execSQL(createMeetingTable);

        String createMeetingOwnerTable = "CREATE TABLE " + MEETING_OWNER_TABLE + " ( "
                + MEETING_OWNER_COL1 + " INTEGER NOT NULL, "
                + MEETING_OWNER_COL2 + " INTEGER NOT NULL,"
                + "PRIMARY KEY(" + MEETING_OWNER_COL1 + "," + MEETING_OWNER_COL2 + "),"
                + "FOREIGN KEY(" + MEETING_OWNER_COL2 + ") REFERENCES " + USER_TABLE + "(" + USER_COL1 + "),"
                + "FOREIGN KEY(" + MEETING_OWNER_COL1 + ") REFERENCES " + MEETING_TABLE + "(" + MEETING_COL1 + ")"
                + ")";
        db.execSQL(createMeetingOwnerTable);

        String createMeetingStuTable = "CREATE TABLE " + MEETING_STUDENT_TABLE + " ( "
                + MEETING_STUDENT_COL1 + " CHAR(50) NOT NULL, "
                + MEETING_STUDENT_COL2 + " INTEGER NOT NULL, "
                + MEETING_STUDENT_COL3 + " INTEGER , "
                + "PRIMARY KEY(" + MEETING_STUDENT_COL1 + "," + MEETING_STUDENT_COL2 + "),"
                + "FOREIGN KEY(" + MEETING_STUDENT_COL2 + ") REFERENCES " + USER_TABLE + "(" + USER_COL1 + "),"
                + "FOREIGN KEY(" + MEETING_STUDENT_COL1 + ") REFERENCES " + MEETING_TABLE + "(" + MEETING_COL1 + ")"
                + ")";
        db.execSQL(createMeetingStuTable);

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
        db.execSQL("DROP TABLE IF EXISTS " + MEETING_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + MEETING_OWNER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + MEETING_STUDENT_TABLE);
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

    public boolean selectMeeting(int stuID, String meetingID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MEETING_STUDENT_COL1, meetingID);
        contentValues.put(MEETING_STUDENT_COL2, stuID);


        long result = db.insert(MEETING_STUDENT_TABLE, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public void setScore(int stuID, String meetingID, int score) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        String sql = "UPDATE meetingstudenttable SET score =? WHERE meetingID = ? and studentID = ?";
        db.rawQuery(sql, new String[]{String.valueOf(score), meetingID, String.valueOf(stuID)});
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

        } else {
            cursor.close();
            return false;
        }
    }


    public Cursor getMeetingTable(int stuID) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("select meetingTable.meetingID, meetingTable.meetingName, meetingTable.meetingTime, meetingTable.description\n" +
                "    from meetingTable, meetingstudenttable\n" +
                "    where meetingstudenttable.studentID = " +
                stuID + " and meetingstudenttable.meetingID = meetingTable.meetingID;", null);
        return data;
    }

    public Cursor searchMeetingByKeyword(String keyword) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "select * from meetingtable " +
                "where meetingid like '%" + keyword + "%' " +
                "or meetingname like '%" + keyword + "%' " +
                "or description like '%" + keyword + "%';";
        Cursor data = db.rawQuery(sql, null);
        return data;
    }

    /**
     * user for the
     *
     * @return all data of specific meeting
     */
    public Cursor searchMeetingByID(String meetingID) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + MEETING_TABLE + " where meetingid =?", new String[]{meetingID});
        return data;
    }


    public int deleteAll(String table_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        int deleted_num = db.delete(table_name, null, null);
        return deleted_num;//return deleted number of datas
    }

}
