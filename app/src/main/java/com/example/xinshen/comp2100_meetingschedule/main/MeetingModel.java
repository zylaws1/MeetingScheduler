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
    private int id;
    private int star_time_hour;
    private int star_time_minute;
    private int end_time;
    private int day;
    private String date_str;
    private String start_time_str = "";
    private String end_time_str = "";
    private String name = "";
    private String description = "";
    private String room = "";
    private String venue = "";

    public String getDate_str() {
        return date_str;
    }

    public MeetingModel(String name, String room, String venue, String description, int date, String date_str, String time_str, int hour, int minute) {
        super();
        id = id_cnt++;
        icon = R.drawable.icon;
        this.name = name;
        this.description = description;
        this.room = room;
        this.venue = venue;
        star_time_hour = hour - 7;
        star_time_minute = minute;
        this.day = date;
        end_time = star_time_hour + 1;
        start_time_str = time_str;
        this.date_str = date_str;
    }

    public MeetingModel(int icon, String name, String description, String room, String venue, int day, String date_str, String time_str, int star_time_hour) {
        super();
        id = id_cnt++;
        this.icon = icon;
        this.name = name;
        this.description = description;
        this.room = room;
        this.venue = venue;
        this.day = day;
        this.star_time_hour = star_time_hour - 7;
        end_time = star_time_hour - 7;
        start_time_str = time_str;
        this.date_str = date_str;
    }

    public MeetingModel(int icon, String name, String description, String room, String venue, int day, String date_str, String time_str, int star_time_hour, int end_time) {
        super();
        id = id_cnt++;
        this.icon = icon;
        this.name = name;
        this.description = description;
        this.room = room;
        this.venue = venue;
        this.day = day;
        this.star_time_hour = star_time_hour - 7;
        this.end_time = end_time - 7;
        start_time_str = time_str;
        this.date_str = date_str;
    }

    public MeetingModel(int start_time, int end_time, int day, String date_str,
                        String start_time_str, String end_time_str, String name, String descripsion,
                        String room, String venue) {
        super();
        id = id_cnt++;
        this.icon = DEFAULT_ICON;
        this.id = id;
        this.star_time_hour = start_time - 7;
        this.end_time = end_time - 7;
        this.day = day;
        this.start_time_str = start_time_str;
        this.end_time_str = end_time_str;
        this.name = name;
        this.description = description;
        this.room = room;
        this.venue = venue;
        this.date_str = date_str;
    }

    @Override
    public String toString() {
        return "TimeTableModel [id=" + id + ", startnum=" + star_time_hour
                + ", endnum=" + end_time + ", week=" + day + ", starttime="
                + start_time_str + ", endtime=" + end_time_str + ", name=" + name
                + ", teacher=" + description + ", classroom=" + room
                + ", weeknum=" + venue + "]";
    }

    public int getId() {
        return id;
    }

    public int getStar_time() {
        return star_time_hour;
    }

    public int getEnd_time() {
        return end_time;
    }

    public int getDay() {
//        Log.i("sx", "getDay: " + day);
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

    public void setStar_time_hour(int star_time_hour) {
        this.star_time_hour = star_time_hour;
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


    @RequiresApi(api = Build.VERSION_CODES.N)
    public long getTimeRemain() {
        long res = -1;
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss");
        try {
            String time_str = date_str + "-" + start_time_str + ":00 ";
            Log.i("shenxin", "time_str: " + time_str);
            Date d1 = df.parse(time_str);
            Date d2 = new Date(System.currentTimeMillis());
            res = d1.getTime() - d2.getTime();//这样得到的差值是微秒级别
        } catch (Exception e) {
        }
        return res;
    }
}
