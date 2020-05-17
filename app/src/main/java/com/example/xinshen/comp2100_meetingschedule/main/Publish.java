
package com.example.xinshen.comp2100_meetingschedule.main;

public class Publish {
    private int publishID;
    private int userID;
    private String date;
    private String content;//store the photo address or video address
    /*
    example string of date
    "2012-12-30 12:10:04"
     */

    public Publish(int id, int userID, String date, String content){
        this.content = content;
        this.date = date;
        this.publishID = id;
        this.userID = userID;
    }
    public Publish(int userID, String date, String content){
        this.content = content;
        this.date = date;
        this.userID = userID;
    }

    public String getContent() {
        return content;
    }

    public int getUserID() {
        return userID;
    }

    public String getDate() {
        return date;
    }

    public int getPublishID() {
        return publishID;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setPublishID(int publishID) {
        this.publishID = publishID;
    }
}
