package com.example.xinshen.comp2100_meetingschedule.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.xinshen.comp2100_meetingschedule.MeetingApplication;
import com.example.xinshen.comp2100_meetingschedule.data.model.UserInfo;
import com.example.xinshen.comp2100_meetingschedule.main.UserInfoCallback;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Data management class, providing operations on the database
 *
 * @author Xin Shen, Shaocong Lang
 */
public class MeetingDbManager {
    private static final String TAG = "shenxin";
    private static volatile MeetingDbManager instance;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    public static String DB_NAME = "person.db";
    public static String FDB_NAME = "user_info";
    public static String TEST_NAME = "userTest";
    public static String ADMIN_NAME = "admin";
    SQLiteDatabase dB;

    private MeetingDbManager() {
        SqliteDatabaseHelper helper = new SqliteDatabaseHelper(MeetingApplication.mContext, DB_NAME, null, 1);
        dB = helper.getWritableDatabase();
        // Add callback listener to get meetings data from google firebase.
        loadDataFromFirebase();
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
    ChildEventListener dataListener = new ChildEventListener() {

        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            Log.w(TAG, "loadPost:onChildAdded");
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            Log.w(TAG, "loadPost:onChildChanged");
        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            Log.w(TAG, "loadPost:onChildRemoved");
        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            Log.w(TAG, "loadPost:onChildMoved");
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.w(TAG, "loadPost:onCancelled");
        }
    };

    //insert user info in firebase
    public boolean insertUserInfoInFirebase(UserInfo userInfo) {
        if (userInfo == null || userInfo.getDisplayName() == null) {
            return false;
        }
        try {
            mDatabase.child(FDB_NAME).child(userInfo.getDisplayName()).setValue(userInfo);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //query user info from firebase
    public void queryUserInfoFromFirebase(final String username, final UserInfoCallback callback) {
        try {
            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child(FDB_NAME);
            rootRef.orderByChild("displayName").equalTo(username).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    boolean isFind = false;
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        UserInfo userInfo = ds.getValue(UserInfo.class);
                        if (userInfo != null) {
                            isFind = true;
                            callback.callback(userInfo);
                        }
                    }
                    if (!isFind) {
                        callback.callback(null);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Randomly update a node to trigger the callback to get data from server
    private void loadDataFromFirebase() {
        mDatabase.child(FDB_NAME).addChildEventListener(dataListener);
        UserInfo userInfo=new UserInfo();
        userInfo.setDisplayName(ADMIN_NAME);
        userInfo.setPassword("11111111");
        UserInfo userInfo1=new UserInfo();
        userInfo1.setDisplayName(TEST_NAME);
        userInfo1.setPassword("11111111");
        mDatabase.child(FDB_NAME).child("admin").setValue(userInfo);
        mDatabase.child(FDB_NAME).child(TEST_NAME).setValue(userInfo1);
    }

    //update user info from firebase
    public boolean updateUserInfoFromFirebase(UserInfo info) {
        try {
            mDatabase.child(FDB_NAME).child(info.getDisplayName()).setValue(info);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

//    public boolean insertUserInfo(UserInfo userInfo) {
//        if (userInfo == null) {
//            return false;
//        }
//        try {
//            ContentValues values=new ContentValues();
//            values.put("name",userInfo.getDisplayName());
//            values.put("password",userInfo.getPassword());
//            values.put("age",userInfo.getAge());
//            values.put("gender",userInfo.getGender());
//            values.put("phone",userInfo.getPhone());
//            values.put("email",userInfo.getEmail());
//            dB.insert(SqliteDatabaseHelper.TABLE_NAME_USER,null,values);
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    public UserInfo queryUserInfo(String username) {
//        try {
//            UserInfo info = null;
//            Cursor cursor = dB.query(SqliteDatabaseHelper.TABLE_NAME_USER, new String[]{"name", "password", "age", "gender", "phone", "email"}, "name=?", new String[]{username}, null, null, null);
//            if (cursor.getCount() <= 0) {
//                return null;
//            }
//            info = new UserInfo();
//            while (cursor.moveToNext()) {
//                info.setDisplayName(cursor.getString(cursor.getColumnIndex("name")));
//                info.setPassword(cursor.getString(cursor.getColumnIndex("password")));
//                info.setAge(cursor.getInt(cursor.getColumnIndex("age")));
//                info.setGender(cursor.getInt(cursor.getColumnIndex("gender")));
//                info.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
//                info.setEmail(cursor.getString(cursor.getColumnIndex("email")));
//            }
//            return info;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    public boolean updateUserInfo(UserInfo info) {
//        try {
//            ContentValues values = new ContentValues();
//            values.put("age", info.getAge());
//            values.put("gender", info.getGender());
//            values.put("phone", info.getPhone());
//            values.put("email", info.getEmail());
//            String whereClause = "name=?";
//            dB.update(SqliteDatabaseHelper.TABLE_NAME_USER, values, whereClause, new String[]{info.getDisplayName()});
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }

//    public boolean updateUserPassword(String password) {
//        try {
//            ContentValues values = new ContentValues();
//            values.put("password", password);
//            String whereClause = "name=?";
//            String[] whereArgs = {String.valueOf(1)};
//            dB.update(SqliteDatabaseHelper.TABLE_NAME_USER, values, whereClause, whereArgs);
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }

}
