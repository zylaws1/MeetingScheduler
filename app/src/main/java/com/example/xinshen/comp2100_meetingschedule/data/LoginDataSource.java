package com.example.xinshen.comp2100_meetingschedule.data;

import com.example.xinshen.comp2100_meetingschedule.MeetingApplication;
import com.example.xinshen.comp2100_meetingschedule.data.model.UserInfo;
import com.example.xinshen.comp2100_meetingschedule.database.MeetingDbManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {
    public Result<UserInfo> login(String username, String password) {
        UserInfo info = MeetingDbManager.getInstance().queryUserInfo(username);
        if (info != null && info.getPassword() != null && info.getPassword().equals(password)) {
            return new Result.Success<>(info);
        } else {
            return new Result.Error(new IOException("Error logging in"));
        }
    }

    public int register(UserInfo info) {
        if (info == null) {
            return Result.REGISTER_INFO_NULL;
        }
        UserInfo userInfo = MeetingDbManager.getInstance().queryUserInfo(info.getDisplayName());
        if (userInfo != null) {
            return Result.REGISTER_HAS_REGISTERED;
        } else {
            MeetingDbManager.getInstance().insertUserInfo(info);
            return Result.REGISTER_OK;
        }
    }

    public UserInfo query(String name) {
        if (name == null) {
            return null;
        }
        UserInfo userInfo = MeetingDbManager.getInstance().queryUserInfo(name);
        return userInfo;
    }

    public boolean update(UserInfo info) {
        if (info == null) {
            return false;
        }
        return MeetingDbManager.getInstance().updateUserInfo(info);
    }

    public void logout() {
        // TODO: revoke authentication
    }
}
