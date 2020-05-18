package com.example.xinshen.comp2100_meetingschedule.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xinshen.comp2100_meetingschedule.R;
import com.example.xinshen.comp2100_meetingschedule.data.Result;
import com.example.xinshen.comp2100_meetingschedule.data.model.MessageEvent;
import com.example.xinshen.comp2100_meetingschedule.data.model.UserInfo;
import com.example.xinshen.comp2100_meetingschedule.database.SpManager;

import org.greenrobot.eventbus.EventBus;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class RegisterActivity extends AppCompatActivity {
    LoginViewModel viewModel;
    EditText etUserName, etPassword, etConfirmPassword, etAge, etPhone, etEmail;
    RadioGroup rgGender;
    Button btRegister;
    TextView tvTopTitle;
    TextView tvTopRight;
    ImageView ivBack;
    boolean isLogin = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Intent intent = getIntent();
        isLogin = intent.getBooleanExtra("isLogin", false);
        String userName = null;
        if (isLogin) {
            userName = intent.getStringExtra("userName");
        }
        viewModel = ViewModelProviders.of(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);
        etUserName=findViewById(R.id.et_register_username);
        etPassword=findViewById(R.id.et_register_password);
        etConfirmPassword=findViewById(R.id.et_confirm_password);
        rgGender=findViewById(R.id.rg_gender);
        etAge=findViewById(R.id.et_age);
        etPhone=findViewById(R.id.et_phone);
        etEmail=findViewById(R.id.et_email);
        btRegister=findViewById(R.id.btn_register);
        tvTopTitle = findViewById(R.id.top_title);
        tvTopRight = findViewById(R.id.tv_right);
        ivBack = findViewById(R.id.iv_back);
        if (isLogin) {
            UserInfo info = viewModel.query(userName);
            if (info != null) {
                setEditTextEnable(info, true);
                btRegister.setText(R.string.user_info_modification);
                btRegister.setEnabled(true);
                tvTopRight.setVisibility(View.GONE);
                tvTopTitle.setVisibility(View.VISIBLE);
                ivBack.setVisibility(View.VISIBLE);
                tvTopTitle.setText(getApplication().getString(R.string.user_info_modification));
                ivBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
            }
        } else {
            tvTopRight.setVisibility(View.GONE);
            tvTopTitle.setVisibility(View.GONE);
            ivBack.setVisibility(View.GONE);
        }

        viewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                btRegister.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    etUserName.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    etPassword.setError(getString(loginFormState.getPasswordError()));
                }
                if (loginFormState.getConfirmPasswordError() != null) {
                    etConfirmPassword.setError(getString(loginFormState.getConfirmPasswordError()));
                }
                if (loginFormState.getAgeError() != null) {
                    etAge.setError(getString(loginFormState.getAgeError()));
                }
                if (loginFormState.getPhoneError() != null) {
                    etPhone.setError(getString(loginFormState.getPhoneError()));
                }
                if (loginFormState.getEmailError() != null) {
                    etEmail.setError(getString(loginFormState.getEmailError()));
                }
            }
        });

        viewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }

                if (loginResult.getError() != null) {
                    showFailed(loginResult.getError());
                    MessageEvent event=new MessageEvent();
                    event.setLoginState(Result.LOGIN_ERROR);
                    EventBus.getDefault().post(event);
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                    MessageEvent event = new MessageEvent();
                    event.setLoginState(Result.LOGIN_OK);
                    event.setMessage(loginResult.getSuccess().getDisplayName());
                    EventBus.getDefault().post(event);

                }
                setResult(Activity.RESULT_OK);

                //Complete and destroy login activity once successful
                finish();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                viewModel.registerDataChanged(etUserName.getText().toString().trim(),
                        etPassword.getText().toString().trim(), etConfirmPassword.getText().toString().trim(),
                        etAge.getText().toString().trim(), etPhone.getText().toString().trim(), etEmail.getText().toString().trim());
            }
        };
        etUserName.addTextChangedListener(afterTextChangedListener);
        etPassword.addTextChangedListener(afterTextChangedListener);
        etConfirmPassword.addTextChangedListener(afterTextChangedListener);
        etAge.addTextChangedListener(afterTextChangedListener);
        etPhone.addTextChangedListener(afterTextChangedListener);
        etEmail.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    viewModel.login(etUserName.getText().toString(),
                            etPassword.getText().toString());
                }
                return false;
            }
        });

        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInfo info = new UserInfo();
                info.setDisplayName(etUserName.getText().toString().trim());
                info.setPassword(etPassword.getText().toString().trim());
                int index = -1;
                for (int i = 0; i < rgGender.getChildCount(); i++) {
                    RadioButton radioButton = (RadioButton) rgGender.getChildAt(i);
                    if (radioButton.isChecked()) {
                        index = i;
                    }
                }
                info.setGender(index);
                info.setAge(Integer.valueOf(etAge.getText().toString().trim()));
                info.setPhone(etPhone.getText().toString().trim());
                info.setEmail(etEmail.getText().toString().trim());
                if (isLogin) {
                    boolean result = viewModel.update(info);
                    if (result) {
                        showToast(getApplication().getString(R.string.update_ok));
                    } else {
                        showToast(getApplication().getString(R.string.update_error));
                    }
                    finish();
                } else {
                    viewModel.regiester(info);
                }
            }
        });
    }

    public void setEditTextEnable(UserInfo info, boolean isEnable) {
        if (info == null) {
            return;
        }
        etUserName.setText(info.getDisplayName());
        etUserName.setEnabled(false);
        etPassword.setText(info.getPassword());
        etPassword.setEnabled(isEnable);
        etConfirmPassword.setText(info.getPassword());
        etConfirmPassword.setEnabled(isEnable);
        if (info.getGender() == 0) {
            RadioButton radioButton = (RadioButton) rgGender.getChildAt(0);
            radioButton.setChecked(true);
        } else {
            RadioButton radioButton = (RadioButton) rgGender.getChildAt(1);
            radioButton.setChecked(true);
        }
        etAge.setText(info.getAge()+"");
        etAge.setEnabled(isEnable);
        etPhone.setText(info.getPhone());
        etPhone.setEnabled(isEnable);
        etEmail.setText(info.getEmail());
        etEmail.setEnabled(isEnable);
    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        SpManager.getInstance(getApplicationContext()).setUserName(model.getDisplayName(),true);
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    private void showToast(String info) {
        Toast.makeText(getApplicationContext(), info, Toast.LENGTH_SHORT).show();
    }

}
