package com.example.xinshen.comp2100_meetingschedule.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Patterns;

import com.example.xinshen.comp2100_meetingschedule.data.LoginRepository;
import com.example.xinshen.comp2100_meetingschedule.data.Result;

import com.example.xinshen.comp2100_meetingschedule.data.model.UserInfo;
import com.example.xinshen.comp2100_meetingschedule.R;
import com.example.xinshen.comp2100_meetingschedule.database.MeetingDbManager;
import com.example.xinshen.comp2100_meetingschedule.main.UserInfoCallback;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Register view model ,to wrap register view
 *
 * @author Xin Shen, Shaocong Lang
 */
public class RegisterViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;

    public RegisterViewModel() {

    }

    RegisterViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void regiester(final UserInfo info) {
        if (info == null) {
            loginResult.setValue(new LoginResult(R.string.register_info_null));
            return;
        }
        loginRepository.register(info, new UserInfoCallback() {
            @Override
            public void callback(UserInfo userInfo) {
                if (userInfo != null) {
                    loginResult.setValue(new LoginResult(R.string.register_has_registered));
                } else {
                    boolean result = MeetingDbManager.getInstance().insertUserInfoInFirebase(info);
                    if (result) {
                        loginResult.setValue(new LoginResult(new LoggedInUserView(info.getDisplayName())));
                    } else {
                        loginResult.setValue(new LoginResult(R.string.register_error));
                    }
                }
            }
        });
    }

    public void query(String name, UserInfoCallback callback) {
        loginRepository.query(name, callback);
    }

    public boolean update(UserInfo info) {
        return loginRepository.update(info);
    }

    public void registerDataChanged(String username, String password, String confirmPassword, String age, String phone, String email) {
        int phoneResult = checkPhoneValid(phone);
        int userAge = -1;
        try {
            if (age != null && !age.equals("")) {
                userAge = Integer.valueOf(age);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else if (!confirmPassword.equals(password)) {
            loginFormState.setValue(new LoginFormState(null, null, R.string.invalid_confirm_password, null, null, null));
        } else if (userAge <= 0 || userAge > 150) {
            loginFormState.setValue(new LoginFormState(null, null, null, R.string.age_range_or_format_error, null, null));
        } else if (phoneResult != 0) {
            loginFormState.setValue(new LoginFormState(null, null, null, null, phoneResult, null));
        } else if (!isEmailValid(email)) {
            loginFormState.setValue(new LoginFormState(null, null, null, null, null, R.string.invalid_email));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    private boolean isEmailValid(String email) {
        if (email == null) {
            return false;
        }
        if (email.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches();
        } else {
            return false;
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }

    public int checkPhoneValid(String phoneNumber) {
        if (phoneNumber == null || "".equals(phoneNumber)) {
            return R.string.phone_number_format_error;
        }
        String regex = "^1[3|4|5|8][0-9]\\d{8}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(phoneNumber);
        boolean isMatch = m.matches();
        if (isMatch) {
            return 0;
        } else {
            return R.string.phone_number_format_error;
        }
    }
}
