package com.example.xinshen.comp2100_meetingschedule;

import android.app.Application;

public class MeetingApplication extends Application {
    public static MeetingApplication mContext;

    public MeetingApplication() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext= (MeetingApplication) getApplicationContext();
        if(mContext==null){
            mContext.onCreate();
        }
    }

}
