package com.example.xinshen.comp2100_meetingschedule.ui.login;

/**
 * Class exposing authenticated user details to the UI.
 *
 * @author Xin Shen, Shaocong Lang
 */
class LoggedInUserView {
    private String displayName;
    //... other data fields that may be accessible to the UI

    LoggedInUserView(String displayName) {
        this.displayName = displayName;
    }

    String getDisplayName() {
        return displayName;
    }
}
