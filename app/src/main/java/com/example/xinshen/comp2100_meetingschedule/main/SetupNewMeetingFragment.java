package com.example.xinshen.comp2100_meetingschedule.main;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import com.example.xinshen.comp2100_meetingschedule.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SetupNewMeetingFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_frag_setup_new_meeting, null);
        return view;
    }
}
