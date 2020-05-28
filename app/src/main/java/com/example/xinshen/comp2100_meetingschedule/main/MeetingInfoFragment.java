package com.example.xinshen.comp2100_meetingschedule.main;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.xinshen.comp2100_meetingschedule.R;


// Fragment to show detailed meeting information
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
    private ImageView meet_icon;
    View root_view;
    private Activity base_activity;
    private MeetingModel meeting_obj;
    public int touched_id = -1;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // save the activity when first time attached
        base_activity = getActivity();
        Log.i(TAG, "onAttach context: " + base_activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // bond the views and controls for the first time initialization
        if (root_view == null) {
            root_view = inflater.inflate(R.layout.activity_meeting_info, null);
            button_exit = (Button) root_view.findViewById(R.id.button_exit_info);
            button_share = (Button) root_view.findViewById(R.id.button_share_info);
            meet_name = (TextView) root_view.findViewById(R.id.textView_meeting_name);
            meet_room = (TextView) root_view.findViewById(R.id.textView_room);
            meet_venue = (TextView) root_view.findViewById(R.id.textView_venue);
            meet_description = (TextView) root_view.findViewById(R.id.textView_description);
            meet_date = (TextView) root_view.findViewById(R.id.textView_date);
            meet_time = (TextView) root_view.findViewById(R.id.textView_time);
            meet_icon = (ImageView) root_view.findViewById(R.id.imageView_meeting_icon);
        }
        // Fill the content according to the chosen way from coming fragment
        Log.i(TAG, "init info frag:" + touched_id + " meeting_obj null:" + (meeting_obj == null));
        Log.i(TAG, "init info frag meetinglv size:" + MainActivity.instance.comingMeetingsFragment.meetings_list.size());
        if (touched_id != -1) {
            Log.i(TAG, "info by id:" + touched_id);
//            MainActivity m = (MainActivity) base_activity;
            MeetingModel obj = MainActivity.instance.comingMeetingsFragment.meetings_list.get(touched_id);
            setMeetingInfo(obj);
        } else if (meeting_obj != null) {
            Log.i(TAG, "info by obj:" + meeting_obj.getName());
            setMeetingInfo(meeting_obj);
        } else {
            meeting_obj = new MeetingModel();
        }
        // Make a copy to mainactivity public param_model in order for quick share by token
        MainActivity.param_model.setName(meet_name.getText().toString());
        MainActivity.param_model.setRoom(meet_room.getText().toString());
        MainActivity.param_model.setDescription(meet_description.getText().toString());
        MainActivity.param_model.setVenue(meet_venue.getText().toString());
        String time_str = meet_time.getText().toString();
        MainActivity.param_model.setStart_time_str(time_str);
        int hour = Integer.parseInt(time_str.substring(0, time_str.indexOf(":")));
        int minute = Integer.parseInt(time_str.substring(time_str.indexOf(":") + 1));
        MainActivity.param_model.setStart_hour(hour);
        MainActivity.param_model.setStart_time_minute(minute);
        MainActivity.param_model.setVenue(meet_venue.getText().toString());
        String date_str = meet_date.getText().toString();
        MainActivity.param_model.setDay(AddNewMeetingFragment.dateToWeek(date_str));
        MainActivity.param_model.setDate_str(date_str);
        return root_view;
    }

    // set filling method by user touched id from listview
    public void setTouched_id(int touched_id) {
        this.touched_id = touched_id;
    }

    // fill the content meeting information by id
//    public void setMeetingInfo(int i) {
//        MainActivity m = (MainActivity) base_activity;
//        if (base_activity == null) Log.i(TAG, "null base_activity");
//        if (m.comingMeetingsFragment == null) Log.i(TAG, "null ComingMeetingsFragment");
//        if (m.comingMeetingsFragment.meetings_list == null) Log.i(TAG, "null meetings_list");
//        MeetingModel obj = m.comingMeetingsFragment.meetings_list.get(i);
////      if (obj == null) Log.i(TAG, "null obj");
//        meet_icon.setImageResource(obj.getIcon());
//        meet_name.setText(obj.getName());
//        meet_room.setText(obj.getRoom());
//        meet_venue.setText(obj.getVenue());
//        meet_description.setText(obj.getDescription());
//        meet_date.setText(obj.getDate_str());
//        meet_time.setText(obj.getStart_time_str());
//    }

    // set filling method by MeetingModel object parameter
    public void setMeetingModel(MeetingModel model) {
        this.meeting_obj = model;
    }

    // fill the content meeting information by MeetingModel object
    public void setMeetingInfo(MeetingModel obj) {
        //Log.i(TAG, "setInfo obj name:" + obj.getName());
        meet_icon.setImageResource(obj.getIcon());
        meet_name.setText(obj.getName());
        meet_room.setText(obj.getRoom());
        meet_venue.setText(obj.getVenue());
        meet_description.setText(obj.getDescription());
        meet_date.setText(obj.getDate_str());
        meet_time.setText(obj.getStart_time_str());
        touched_id = -1;
    }

    public TextView getMeet_name() {
        return meet_name;
    }
}
