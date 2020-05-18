package com.example.xinshen.comp2100_meetingschedule.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.xinshen.comp2100_meetingschedule.R;

import java.util.ArrayList;
import java.util.List;

import static com.example.xinshen.comp2100_meetingschedule.main.MainActivity.SCREEN_HEIGHT;
import static com.example.xinshen.comp2100_meetingschedule.main.MainActivity.SCREEN_WIDTH;

public class MeetingListFragmentActivity extends Fragment {
    private ListView lv_coming_meetins;
    List<ScrolledMeetings> meetings_list;
    ScrolledMeetingAdapter meetings_list_adapter;

    MeetingListFragmentActivity() {
        super();
        meetings_list = get_mock_data();
    }

    MeetingListFragmentActivity(List<ScrolledMeetings> data) {
        super();
        if (data.size() == 0) {
            meetings_list = get_mock_past_data();
        } else {
            meetings_list = data;
        }
    }

    List<ScrolledMeetings> get_mock_data() {
        List<ScrolledMeetings> data = new ArrayList<>();
        data.add(new ScrolledMeetings(R.drawable.icon, "comp1110",
                "meeting agenda", "113", "CSIT ground floor"));
        data.add(new ScrolledMeetings(R.drawable.icon, "engn6528",
                "project perspective", "115", "CSIT ground floor"));
        data.add(new ScrolledMeetings(R.drawable.icon, "comp8600",
                "stage 1 tasks", "3.31", "hancock 3rd floor"));
        data.add(new ScrolledMeetings(R.drawable.icon, "comp8330",
                "comp2100 assignment group meeting", "115", "Lena building 9th floor"));
        data.add(new ScrolledMeetings(R.drawable.icon, "comp1110",
                "meeting agenda", "113", "CSIT ground floor"));
        data.add(new ScrolledMeetings(R.drawable.icon, "engn6528",
                "project perspective", "115", "CSIT ground floor"));
        data.add(new ScrolledMeetings(R.drawable.icon, "comp8600",
                "stage 1 tasks", "3.31", "hancock 3rd floor"));
        data.add(new ScrolledMeetings(R.drawable.icon, "comp8330",
                "comp2100 assignment group meeting", "115", "Lena building 9th floor"));
        data.add(new ScrolledMeetings(R.drawable.icon, "comp8300",
                "comp2100 assignment group meeting", "115", "Lena building 9th floor"));
        return data;
    }

    List<ScrolledMeetings> get_mock_past_data() {
        List<ScrolledMeetings> data = new ArrayList<>();
        data.add(new ScrolledMeetings(R.drawable.icon, "comp2100",
                "team formation", "101", "CSIT ground floor"));
        data.add(new ScrolledMeetings(R.drawable.icon, "comp6442",
                "Choose the topic for assignment", "108", "Hanna Building 1st floor"));
        return data;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_meeting_lv, null);
        lv_coming_meetins = (ListView) view.findViewById(R.id.scroll_coming_meetingLv);
        meetings_list_adapter = new ScrolledMeetingAdapter(getContext(),
                R.layout.scrolled_meetings_listview, meetings_list);
        lv_coming_meetins.setAdapter(meetings_list_adapter);
        lv_coming_meetins.getLayoutParams().height = (int) (SCREEN_HEIGHT * 0.8);
        ;
        return view;
    }
}
