package com.example.xinshen.comp2100_meetingschedule.main;

import com.example.xinshen.comp2100_meetingschedule.R;

// meeting object class
public class MeetingModel {
    private int icon;
    private int id;
    private int star_time_hour;
    private int star_time_minute;
    private int end_time;
    private int day;
    private String start_time_str = "";
    private String end_time_str = "";
    private String name = "";
    private String description = "";
    private String room = "";
    private String venue = "";

    public MeetingModel(String name, String room, String venue, String description, int date, int hour, int minute) {
        super();
        icon = R.drawable.icon;
        this.name = name;
        this.description = description;
        this.room = room;
        this.venue = venue;
        star_time_hour = hour;
        star_time_minute = minute;
        end_time = star_time_hour + 1;
    }

    public MeetingModel(int icon, String name, String description, String room, String venue, int day, int star_time_hour) {
        super();
        this.icon = icon;
        this.name = name;
        this.description = description;
        this.room = room;
        this.venue = venue;
        this.day = day;
        this.star_time_hour = star_time_hour - 7;
        end_time = star_time_hour - 7;
    }

    public MeetingModel(int icon, String name, String description, String room, String venue, int day, int star_time_hour, int end_time) {
        super();
        this.icon = icon;
        this.name = name;
        this.description = description;
        this.room = room;
        this.venue = venue;
        this.day = day;
        this.star_time_hour = star_time_hour - 7;
        this.end_time = end_time - 7;
    }

    public MeetingModel(int id, int start_time, int end_time, int day,
                        String start_time_str, String end_time_str, String name, String descripsion,
                        String room, String venue) {
        super();
        this.id = id;
        this.star_time_hour = start_time;
        this.end_time = end_time;
        this.day = day;
        this.start_time_str = start_time_str;
        this.end_time_str = end_time_str;
        this.name = name;
        this.description = description;
        this.room = room;
        this.venue = venue;
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

    public MeetingModel() {
        // TODO Auto-generated constructor stub
    }


    public String getDescription() {
        return description;
    }

    public int getIcon() {
        return icon;
    }
}
