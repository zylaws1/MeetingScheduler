package com.example.xinshen.comp2100_meetingschedule.database;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.xinshen.comp2100_meetingschedule.R;

public class SpManager {
    private static volatile SpManager instance;
    private Context sPContext;
    private SharedPreferences sP;
    private SharedPreferences.Editor editor;

    private SpManager(Context context) {
        sPContext=context;
        sP = context.getSharedPreferences("meeting", Context.MODE_PRIVATE);
        editor = sP.edit();
    }

    public static SpManager getInstance(Context context) {
        if (instance == null) {
            synchronized (SpManager.class) {
                if (instance == null) {
                    instance = new SpManager(context);
                }
            }
        }
        return instance;
    }

    public void setUserName(String name, boolean applyNow) {
        editor.putString(sPContext.getString(R.string.sp_name), name);
        editor.apply();
    }

    public String getUserName() {
        return sP.getString(sPContext.getString(R.string.sp_name), null);
    }
}
