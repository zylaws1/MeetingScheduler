package com.example.xinshen.comp2100_meetingschedule.ui.login;

import androidx.annotation.Nullable;

/**
 * Authentication result : success (user details) or error message.
 *
 * @author Xin Shen, Shaocong Lang
 */
class LoginResult {
    @Nullable
    private LoggedInUserView success;
    @Nullable
    private Integer error;

    LoginResult(@Nullable Integer error) {
        this.error = error;
    }

    LoginResult(@Nullable LoggedInUserView success) {
        this.success = success;
    }

    @Nullable
    LoggedInUserView getSuccess() {
        return success;
    }

    @Nullable
    Integer getError() {
        return error;
    }
}
