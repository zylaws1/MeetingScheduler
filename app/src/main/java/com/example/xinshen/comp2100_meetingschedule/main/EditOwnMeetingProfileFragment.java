package com.example.xinshen.comp2100_meetingschedule.main;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.xinshen.comp2100_meetingschedule.R;

public class EditOwnMeetingProfileFragment extends Fragment {

    private TextView textView4;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_own_meeting_profile, null);
        return view;
    }
}
