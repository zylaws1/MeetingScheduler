package com.example.xinshen.comp2100_meetingschedule.main;

public class ScrolledMeetings {
    private int icon;
    private String name;
    private String description;
    private String room;
    private String venue;

    public ScrolledMeetings(int icon, String name, String description, String room, String venue) {
        this.icon = icon;
        this.name = name;
        this.description = description;
        this.room = room;
        this.venue = venue;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }
}
