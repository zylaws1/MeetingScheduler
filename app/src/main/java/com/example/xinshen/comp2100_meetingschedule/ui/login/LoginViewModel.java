package com.example.xinshen.comp2100_meetingschedule.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Patterns;

import com.example.xinshen.comp2100_meetingschedule.data.LoginRepository;
import com.example.xinshen.comp2100_meetingschedule.data.Result;

import com.example.xinshen.comp2100_meetingschedule.data.model.UserInfo;
import com.example.xinshen.comp2100_meetingschedule.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String username, String password) {
        // can be launched in a separate asynchronous job

        Result<UserInfo> result = loginRepository.login(username, password);

        if (result instanceof Result.Success) {
            UserInfo data = ((Result.Success<UserInfo>) result).getData();
            loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));
        } else {
            loginResult.setValue(new LoginResult(R.string.login_failed));
        }
    }


    public void regiester(UserInfo info) {
        int result = loginRepository.register(info);
        if (result == Result.REGISTER_OK) {
            loginResult.setValue(new LoginResult(new LoggedInUserView(info.getDisplayName())));
        } else if (result == Result.REGISTER_HAS_REGISTERED) {
            loginResult.setValue(new LoginResult(R.string.register_has_registered));
        } else if (result == Result.REGISTER_INFO_NULL) {
            loginResult.setValue(new LoginResult(R.string.register_has_registered));
        } else {
            loginResult.setValue(new LoginResult(R.string.register_error));
        }
    }

    public UserInfo query(String name) {
        return loginRepository.query(name);
    }

    public boolean update(UserInfo info) {
        return loginRepository.update(info);
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    public void registerDataChanged(String username, String password, String confirmPassword, String age, String phone, String email) {
        int phoneResult = checkPhoneValid(phone);
        int userAge = -1;
        try {
            userAge = Integer.valueOf(age);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else if (!confirmPassword.equals(password)) {
            loginFormState.setValue(new LoginFormState(null, null, R.string.invalid_confirm_password, null, null, null));
        } else if (phoneResult != 0) {
            loginFormState.setValue(new LoginFormState(null, null, null, null, phoneResult, null));
        } else if (userAge <= 0 || userAge > 150) {
            loginFormState.setValue(new LoginFormState(null, null, null, R.string.age_range_or_format_error, null, null));
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

    private int checkPhoneValid(String phoneNumber) {
        String regExp = "^((13[0-9])|(14[5,7,9])|(15[0-3,5-9])|(166)|(17[3,5,6,7,8])" +
                "|(18[0-9])|(19[8,9]))\\d{8}$";
        if (phoneNumber.length() != 11) {
            return R.string.phone_digit_count_error;
        } else {
            Pattern p = Pattern.compile(regExp);
            Matcher m = p.matcher(phoneNumber);
            boolean isMatch = m.matches();
            if (isMatch) {
                return 0;
            } else {
                return R.string.phone_number_format_error;
            }
        }
    }
}