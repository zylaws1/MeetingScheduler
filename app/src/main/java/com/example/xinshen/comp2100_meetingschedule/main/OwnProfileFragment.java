package com.example.xinshen.comp2100_meetingschedule.main;

import android.os.Bundle;

import com.example.xinshen.comp2100_meetingschedule.R;
import com.example.xinshen.comp2100_meetingschedule.data.Result;
import com.example.xinshen.comp2100_meetingschedule.data.model.MessageEvent;
import com.example.xinshen.comp2100_meetingschedule.database.SpManager;
import com.example.xinshen.comp2100_meetingschedule.ui.login.LoginFragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.xinshen.comp2100_meetingschedule.ui.login.RegisterFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * User own profile and login/register fragment
 *
 * @author Xin Shen, Shaocong Lang
 */
public class OwnProfileFragment extends Fragment implements View.OnClickListener {
    TextView mTvUser;
    TextView mTvLogin;
    RelativeLayout mInfoModification;
    RelativeLayout mMyMeeting;
    RelativeLayout mMyNotes;
    RelativeLayout mMyTimeslotPreference;
    private boolean isLogin;
    String userName;
    LoginFragment loginFragment;
    RegisterFragment registerFragment;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // bond the views and controls for the initialization
        view = inflater.inflate(R.layout.activity_personal_center, null);
        mInfoModification = view.findViewById(R.id.layout_info_modification);
        mMyMeeting = view.findViewById(R.id.layout_my_meeting);
        mMyNotes = view.findViewById(R.id.layout_my_notes);
        mMyTimeslotPreference = view.findViewById(R.id.layout_timeslot_preference);
        mTvUser = view.findViewById(R.id.tv_user);
        mTvLogin = view.findViewById(R.id.tv_login);
        mInfoModification.setOnClickListener(this);
        mMyMeeting.setOnClickListener(this);
        mMyNotes.setOnClickListener(this);
        mMyTimeslotPreference.setOnClickListener(this);
        mTvUser.setOnClickListener(this);
        mTvLogin.setOnClickListener(this);
        loginFragment = new LoginFragment();
        registerFragment = new RegisterFragment();
        userName = SpManager.getInstance(getActivity().getApplicationContext()).getUserName();
        MainActivity.instance.setShowTitleBar();
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
        // add listener to each functional link by id.
        FragmentTransaction transaction;
        switch (v.getId()) {
            case R.id.tv_login:
                FragmentManager fraManager = getFragmentManager();
                transaction = fraManager.beginTransaction();
                transaction.addToBackStack(null);
                transaction.replace(R.id.main_linear, loginFragment);
                if (view.findViewById(R.id.main_linear) != null) transaction.commit();
                break;
            case R.id.layout_info_modification:
                if (isLogin) {
                    FragmentManager fraManager1 = getFragmentManager();
                    FragmentTransaction transaction1 = fraManager1.beginTransaction();
                    transaction1.addToBackStack(null);
                    transaction1.replace(R.id.main_linear, registerFragment);
                    if (view.findViewById(R.id.main_linear) != null) transaction1.commit();
                } else {
                    showToast(getString(R.string.no_login));
                }
                break;
            case R.id.layout_my_meeting:
                FragmentManager fraManager1 = getFragmentManager();
                transaction = fraManager1.beginTransaction();
                transaction.replace(R.id.main_linear, MainActivity.instance.getComingMeetingsFragment());
                MainActivity.instance.setmTitleBarStyle(true);
                MainActivity.instance.getBotm_navigation().setSelectedItemId(R.id.navigation_meeting_lists);
                if (view.findViewById(R.id.main_linear) != null) transaction.commit();
                break;
            case R.id.layout_my_notes:
                transaction = MainActivity.mFraManager.beginTransaction();
                transaction.addToBackStack(null);
                transaction.replace(R.id.main_linear, MainActivity.instance.getNoteListFragment());
                transaction.addToBackStack(null);
                if (view.findViewById(R.id.main_linear) != null) transaction.commit();
                break;
            case R.id.layout_timeslot_preference:
                MainActivity.setmTitleBarInactive();
                transaction = MainActivity.mFraManager.beginTransaction();
                transaction.replace(R.id.main_linear, MainActivity.setPreferTimeslotFragment);
                transaction.addToBackStack(null);
                if (view.findViewById(R.id.main_linear) != null) transaction.commit();
                break;
            default:
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        // Monitor the log in status
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

    public void setLogin(boolean login) {
        isLogin = login;
    }

}
