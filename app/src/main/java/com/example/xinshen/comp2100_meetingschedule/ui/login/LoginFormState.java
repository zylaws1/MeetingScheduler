package com.example.xinshen.comp2100_meetingschedule.ui.login;

import androidx.annotation.Nullable;

/**
 * Data validation state of the login form.
 *
 * @author Xin Shen, Shaocong Lang
 */
public class LoginFormState {
    @Nullable
    private Integer usernameError;
    @Nullable
    private Integer passwordError;
    @Nullable
    private Integer confirmPasswordError;
    @Nullable
    private Integer ageError;
    @Nullable
    private Integer phoneError;
    @Nullable
    private Integer emailError;

    private boolean isDataValid;

    @Nullable
    public Integer getAgeError() {
        return ageError;
    }

    @Nullable
    public Integer getPhoneError() {
        return phoneError;
    }

    @Nullable
    public Integer getEmailError() {
        return emailError;
    }

    @Nullable
    public Integer getConfirmPasswordError() {
        return confirmPasswordError;
    }

    LoginFormState(@Nullable Integer usernameError, @Nullable Integer passwordError) {
        this.usernameError = usernameError;
        this.passwordError = passwordError;
        this.isDataValid = false;
    }

    LoginFormState(@Nullable Integer usernameError, @Nullable Integer passwordError, @Nullable Integer confirmPasswordError, @Nullable Integer ageError, @Nullable Integer phoneError, @Nullable Integer emailError) {
        this.usernameError = usernameError;
        this.passwordError = passwordError;
        this.confirmPasswordError = confirmPasswordError;
        this.ageError = ageError;
        this.phoneError = phoneError;
        this.emailError = emailError;
        this.isDataValid = false;
    }

    LoginFormState(boolean isDataValid) {
        this.usernameError = null;
        this.passwordError = null;
        this.confirmPasswordError = null;
        this.ageError = null;
        this.phoneError = null;
        this.emailError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    Integer getUsernameError() {
        return usernameError;
    }

    @Nullable
    Integer getPasswordError() {
        return passwordError;
    }

    public boolean isDataValid() {
        return isDataValid;
    }
}
