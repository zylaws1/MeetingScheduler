package com.example.xinshen.comp2100_meetingschedule.data;

import com.example.xinshen.comp2100_meetingschedule.MeetingApplication;
import com.example.xinshen.comp2100_meetingschedule.data.model.UserInfo;
import com.example.xinshen.comp2100_meetingschedule.database.MeetingDbManager;
import com.example.xinshen.comp2100_meetingschedule.main.UserInfoCallback;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 *
 * @author Xin Shen, Shaocong Lang
 */
public class LoginDataSource {
    private UserInfo mUserInfo = null;

    public LoginDataSource(){
        MeetingDbManager.getInstance().setUserInfoCallback(userInfoCallback);
    }

    UserInfoCallback userInfoCallback = new UserInfoCallback() {
        @Override
        public void callback(UserInfo userInfo) {
            mUserInfo = userInfo;
        }
    };

    public void login(String username, UserInfoCallback callback) {
        MeetingDbManager.getInstance().queryUserInfoFromFirebase(username, callback);
    }

    //user information register
    public void register(UserInfo info, UserInfoCallback callback) {
        MeetingDbManager.getInstance().queryUserInfoFromFirebase(info.getDisplayName(),callback);
    }

    //user information query
    public void query(String name, UserInfoCallback callback) {
        if (name == null) {
            return;
        }
        MeetingDbManager.getInstance().queryUserInfoFromFirebase(name, callback);
    }

    //user information update
    public boolean update(UserInfo info) {
        if (info == null) {
            return false;
        }
        return MeetingDbManager.getInstance().updateUserInfoFromFirebase(info);
    }

    public void logout() {
        // TODO: revoke authentication
    }
}
