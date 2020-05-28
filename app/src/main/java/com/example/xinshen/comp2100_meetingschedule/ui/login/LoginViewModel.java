package com.example.xinshen.comp2100_meetingschedule.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Patterns;

import com.example.xinshen.comp2100_meetingschedule.data.LoginRepository;
import com.example.xinshen.comp2100_meetingschedule.data.Result;

import com.example.xinshen.comp2100_meetingschedule.data.model.UserInfo;
import com.example.xinshen.comp2100_meetingschedule.R;
import com.example.xinshen.comp2100_meetingschedule.main.UserInfoCallback;

/**
 * Login view model to wrap login information view
 *
 * @author Xin Shen, Shaocong Lang
 */
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

    public LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String username, final String password) {
        // can be launched in a separate asynchronous job

        loginRepository.login(username, new UserInfoCallback() {

            @Override
            public void callback(UserInfo userInfo) {
                if (userInfo != null && userInfo.getPassword() != null && userInfo.getPassword().equals(password)) {
                    loginResult.setValue(new LoginResult(new LoggedInUserView(userInfo.getDisplayName())));
                } else {
                    loginResult.setValue(new LoginResult(R.string.login_failed));
                }
            }
        });
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

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }

    // Set login result value
    public void setLoginResultValue(String value, boolean isLoginSuccess) {
        if (isLoginSuccess) {
            loginResult.setValue(new LoginResult(new LoggedInUserView(value)));
        } else {
            loginResult.setValue(new LoginResult(R.string.login_failed));
        }
    }

}
