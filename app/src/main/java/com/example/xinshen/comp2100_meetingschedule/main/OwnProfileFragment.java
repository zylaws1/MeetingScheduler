package com.example.xinshen.comp2100_meetingschedule.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;

import com.example.xinshen.comp2100_meetingschedule.MeetingApplication;
import com.example.xinshen.comp2100_meetingschedule.R;
import com.example.xinshen.comp2100_meetingschedule.data.Result;
import com.example.xinshen.comp2100_meetingschedule.data.model.MessageEvent;
import com.example.xinshen.comp2100_meetingschedule.database.SpManager;
import com.example.xinshen.comp2100_meetingschedule.ui.login.LoginActivity;
import com.example.xinshen.comp2100_meetingschedule.ui.login.RegisterActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import com.example.xinshen.comp2100_meetingschedule.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class OwnProfileFragment extends Fragment implements View.OnClickListener {
    TextView mTvUser;
    TextView mTvLogin;
    RelativeLayout mInfoModification;
    RelativeLayout mMyMeeting;
    RelativeLayout mMyNotes;
    RelativeLayout mMyTimeslotPreference;
    boolean isLogin;
    String userName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_personal_center, null);
        mInfoModification = view.findViewById(R.id.layout_info_modification);
        mMyMeeting = view.findViewById(R.id.layout_my_meeting);
        mMyNotes = view.findViewById(R.id.layout_my_notes);
        mMyTimeslotPreference= view.findViewById(R.id.layout_timeslot_preference);
        mTvUser = view.findViewById(R.id.tv_user);
        mTvLogin = view.findViewById(R.id.tv_login);
        mInfoModification.setOnClickListener(this);
        mMyMeeting.setOnClickListener(this);
        mMyNotes.setOnClickListener(this);
        mMyTimeslotPreference.setOnClickListener(this);
        mTvUser.setOnClickListener(this);
        mTvLogin.setOnClickListener(this);
        userName = SpManager.getInstance(getActivity().getApplicationContext()).getUserName();
        if (userName != null) {
            isLogin = true;
            mTvUser.setVisibility(View.VISIBLE);
            mTvLogin.setVisibility(View.GONE);
            mTvUser.setText(userName);
            mTvUser.setEnabled(false);
        } else {
            isLogin = false;
            mTvUser.setVisibility(View.GONE);
            mTvLogin.setVisibility(View.VISIBLE);
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.tv_login:
                EventBus.getDefault().register(this);
                intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.layout_info_modification:
                if (isLogin) {
                    intent = new Intent(getActivity(), RegisterActivity.class);
                    intent.putExtra("isLogin", isLogin);
                    intent.putExtra("userName", userName);
                    startActivity(intent);
                } else {
                    showToast(getString(R.string.no_login));
                }
                break;
            case R.id.layout_my_meeting:
                break;
            case R.id.layout_my_notes:
                break;
            case R.id.layout_timeslot_preference:
                MainActivity.setmTitleBarInactive();
                FragmentTransaction transaction = MainActivity.mFraManager.beginTransaction();
                transaction.replace(R.id.main_linear, MainActivity.setPreferTimeslotFragment);
                transaction.commit();
                break;
            default:
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event.getLoginState() == Result.LOGIN_OK) {
            mTvUser.setVisibility(View.VISIBLE);
            mTvUser.setText(event.getMessage());
            mTvLogin.setVisibility(View.GONE);
            isLogin = true;
            userName = SpManager.getInstance(getActivity().getApplicationContext()).getUserName();
        } else if (event.getLoginState() == Result.LOGIN_ERROR) {
            mTvUser.setVisibility(View.GONE);
            mTvLogin.setVisibility(View.VISIBLE);
            isLogin = false;
            userName = SpManager.getInstance(getActivity().getApplicationContext()).getUserName();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void showToast(String info) {
        Toast.makeText(getActivity().getApplicationContext(), info, Toast.LENGTH_SHORT).show();
    }

}
