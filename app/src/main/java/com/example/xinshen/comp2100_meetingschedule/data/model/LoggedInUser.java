package com.example.xinshen.comp2100_meetingschedule.data.model;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 *
 * @author Xin Shen, Shaocong Lang
 */
public class LoggedInUser {

    private String userId;
    private String displayName;

    public LoggedInUser(String userId, String displayName) {
        this.userId = userId;
        this.displayName = displayName;
    }

    public String getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }
}
