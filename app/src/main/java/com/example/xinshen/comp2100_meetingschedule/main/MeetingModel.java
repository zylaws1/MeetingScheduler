package com.example.xinshen.comp2100_meetingschedule.main;

import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.xinshen.comp2100_meetingschedule.R;

import java.util.Date;

// meeting object class
public class MeetingModel {
    public static int id_cnt = 0;
    private final static int DEFAULT_ICON = 700041;
    private int icon = DEFAULT_ICON;
    private int id; // meeting id
    private int start_hour; // meeting start hour in integer
    private int start_time_minute;  // meeting start minute in integer
    private int end_time;   // meeting end hour in integer
    private int day;       // meeting start weekday in int (1-7 from Monday to Sunday)
    private String date_str;    // meeting start date in string
    private String start_time_str = ""; // meeting start time in string
    private String end_time_str = "";   // meeting end time in string
    private String name = "";   // meeting name
    private String description = "";    // meeting description
    private String room = "";   // meeting room
    private String venue = "";  // meeting happening place

    public MeetingModel() {

    }

    public MeetingModel(String name, String room, String venue, String description, int day, String date_str, String time_str, int start_hour, int minute) {
        super();
        id = id_cnt++;
        icon = R.drawable.icon;
        this.name = name;
        this.description = description;
        this.room = room;
        this.venue = venue;
        this.start_hour = start_hour;
        start_time_minute = minute;
        this.day = day;
        end_time = start_hour + 1;
        start_time_str = time_str;
        this.date_str = date_str;
    }

    public MeetingModel(int icon, String name, String description, String room, String venue, int day, String date_str, String time_str, int start_hour) {
        super();
        id = id_cnt++;
        this.icon = icon;
        this.name = name;
        this.description = description;
        this.room = room;
        this.venue = venue;
        this.day = day;
        this.start_hour = start_hour;
        end_time = start_hour + 1;
        start_time_str = time_str;
        this.date_str = date_str;
    }

    public MeetingModel(int icon, String name, String description, String room, String venue, int day, String date_str, String time_str, int start_hour, int end_time) {
        super();
        id = id_cnt++;
        this.icon = icon;
        this.name = name;
        this.description = description;
        this.room = room;
        this.venue = venue;
        this.day = day;
        this.start_hour = start_hour;
        this.end_time = end_time;
        start_time_str = time_str;
        this.date_str = date_str;
    }

//    @Override
//    public String toString() {
//        return "MeetingModel [id=" + id + ", startnum=" + start_time_hour
//                + ", endnum=" + end_time + ", week=" + day + ", starttime="
//                + start_time_str + ", endtime=" + end_time_str + ", name=" + name
//                + ", teacher=" + description + ", classroom=" + room
//                + ", weeknum=" + venue + "]";
//    }

    public void setDate_str(String date_str) {
        this.date_str = date_str;
    }

    public int getStart_time_minute() {
        return start_time_minute;
    }

    public void setStart_time_minute(int start_time_minute) {
        this.start_time_minute = start_time_minute;
    }


    public String getDate_str() {
        return date_str;
    }

    public int getId() {
        return id;
    }

    public int getStar_hour() {
        return Integer.parseInt(start_time_str.substring(0, start_time_str.indexOf(":")));
    }

    public int getEnd_time() {
        return end_time;
    }

    public int getDay() {
//        Log.i("shenxin", "getDay: " + day);
        return day;
    }

    public String getStart_time_str() {
        return start_time_str;
    }

    public String getEnd_time_str() {
        return end_time_str;
    }

    public String getName() {
        return name;
    }


    public String getRoom() {
        return room;
    }

    public String getVenue() {
        return venue;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStart_hour(int start_hour) {
        this.start_hour = start_hour;
    }

    public void setEnd_time(int end_time) {
        this.end_time = end_time;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setStart_time_str(String start_time_str) {
        this.start_time_str = start_time_str;
    }

    public void setEnd_time_str(String end_time_str) {
        this.end_time_str = end_time_str;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getDescription() {
        return description;
    }

    public int getIcon() {
        return icon;
    }

    // get remain time in ms from now to meeting deadline, to set the deadline notification
    // return: long : remain meeting reminding time from now to deadline
    @RequiresApi(api = Build.VERSION_CODES.N)
    public long getTimeRemain() {
        long res = -1;
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss");
        // parse the meeting time to long type
        try {
            String time_str = date_str + "-" + start_time_str + ":00 ";
            Log.i("shenxin", "time_str: " + time_str);
            // calculate the time difference from deadline to current system time
            Date d1 = df.parse(time_str);
            Date d2 = new Date(System.currentTimeMillis());
            res = d1.getTime() - d2.getTime();  // get the ms remain time as return value
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
}
