package com.example.xinshen.comp2100_meetingschedule.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.xinshen.comp2100_meetingschedule.R;

public class MeetingInfoFragment extends Fragment {

    private static final String TAG = "shenxin";
    private Button button_exit;
    private Button button_share;
    private TextView meet_name;
    private TextView meet_room;
    private TextView meet_venue;
    private TextView meet_description;
    private TextView meet_date;
    private TextView meet_time;
    View root_view;
    private Activity base_activity;

    public void setTouched_id(int touched_id) {
        this.touched_id = touched_id;
    }

    public int touched_id;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        base_activity = getActivity();
        Log.i(TAG, "onAttach: context" + base_activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (root_view==null){
            root_view = inflater.inflate(R.layout.activity_meeting_info, null);
            button_exit = (Button) root_view.findViewById(R.id.button_exit_info);
            button_share = (Button) root_view.findViewById(R.id.button_share_info);
            meet_name = (TextView) root_view.findViewById(R.id.textView_meeting_name);
            meet_room = (TextView) root_view.findViewById(R.id.textView_room);
            meet_venue = (TextView) root_view.findViewById(R.id.textView_venue);
            meet_description = (TextView) root_view.findViewById(R.id.textView_description);
            meet_date = (TextView) root_view.findViewById(R.id.textView_date);
            meet_time = (TextView) root_view.findViewById(R.id.textView_time);
        }
        setMeetingInfo(touched_id);
        return root_view;
    }

    public void setMeetingInfo(int i) {
        MainActivity m = (MainActivity) base_activity;
        if (base_activity == null) Log.i(TAG, "null base_activity");
        MeetingModel obj = m.ComingMeetingsFragment.meetings_list.get(i);
//        if (obj == null) Log.i(TAG, "null obj");
        meet_name.setText(obj.getName());
        meet_room.setText(obj.getName());
        meet_venue.setText(obj.getVenue());
        meet_description.setText(obj.getDescription());
        meet_date.setText("123");//obj.getDay());
        meet_time.setText(obj.getStart_time_str());
    }

    public void setMeetingInfo(String name, String room, String venue, String des, String date, String time) {
        meet_name.setText(name);
        meet_room.setText(room);
        meet_venue.setText(venue);
        meet_description.setText(des);
        meet_date.setText(date);
        meet_time.setText(time);
    }
}
