package com.example.xinshen.comp2100_meetingschedule.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.xinshen.comp2100_meetingschedule.main.MainActivity;
import com.example.xinshen.comp2100_meetingschedule.main.UserInfoCallback;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hjq.bar.TitleBar;

import org.greenrobot.eventbus.EventBus;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

/**
 * Users can register information on this fragment
 *
 * @author Xin Shen, Shaocong Lang
 */
public class RegisterFragment extends Fragment {
    RegisterViewModel viewModel;
    EditText etUserName, etPassword, etConfirmPassword, etAge, etPhone, etEmail;
    RadioGroup rgGender;
    Button btRegister;
    TextView tvTopTitle;
    TextView tvTopRight;
    ImageView ivBack;
    public String userName = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_register,null);
        viewModel = ViewModelProviders.of(this, new LoginViewModelFactory())
                .get(RegisterViewModel.class);
        etUserName=view.findViewById(R.id.et_register_username);
        etPassword=view.findViewById(R.id.et_register_password);
        etConfirmPassword=view.findViewById(R.id.et_confirm_password);
        rgGender=view.findViewById(R.id.rg_gender);
        etAge=view.findViewById(R.id.et_age);
        etPhone=view.findViewById(R.id.et_phone);
        etEmail=view.findViewById(R.id.et_email);
        btRegister=view.findViewById(R.id.btn_register);
        tvTopTitle = view.findViewById(R.id.top_title);
        tvTopRight = view.findViewById(R.id.tv_right);
        ivBack = view.findViewById(R.id.iv_back);
        MainActivity.setHideTitleBar();
        userName = SpManager.getInstance(getActivity().getApplicationContext()).getUserName();
        if (userName != null) {
            viewModel.query(userName, new UserInfoCallback() {
                @Override
                public void callback(UserInfo userInfo) {
                    if (userInfo != null) {
                        setEditTextEnable(userInfo, true);
                        btRegister.setText(R.string.user_info_modification);
                        btRegister.setEnabled(true);
                        tvTopRight.setVisibility(View.GONE);
                        tvTopTitle.setVisibility(View.VISIBLE);
                        ivBack.setVisibility(View.VISIBLE);
                        tvTopTitle.setText(getActivity().getString(R.string.user_info_modification));
                        ivBack.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getFragmentManager().popBackStack();
                            }
                        });
                    }
                }
            });
        } else {
            tvTopRight.setVisibility(View.GONE);
            tvTopTitle.setVisibility(View.VISIBLE);
            ivBack.setVisibility(View.VISIBLE);
            tvTopTitle.setText(getResources().getString(R.string.register_value));
            ivBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getFragmentManager().popBackStack();
                }
            });
        }

        viewModel.getLoginFormState().observe(getActivity(), new Observer<LoginFormState>() {
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

        viewModel.getLoginResult().observe(getActivity(), new Observer<LoginResult>() {
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
                //Complete and destroy login activity once successful
                getFragmentManager().popBackStack();
                MainActivity.instance.setShowTitleBar();
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
                if (userName != null) {
                    boolean result = viewModel.update(info);
                    if (result) {
                        showToast(getResources().getString(R.string.update_ok));
                    } else {
                        showToast(getResources().getString(R.string.update_error));
                    }
                    getFragmentManager().popBackStack();
                } else {
                    viewModel.regiester(info);
                }
            }
        });
        return view;
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
        SpManager.getInstance(getContext()).setUserName(model.getDisplayName(),true);
        Toast.makeText(getContext(), welcome, Toast.LENGTH_LONG).show();
    }

    public void showFailed(@StringRes Integer errorString) {
        Toast.makeText(getContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    public void showToast(String info) {
        Toast.makeText(getContext(), info, Toast.LENGTH_SHORT).show();
    }

}
