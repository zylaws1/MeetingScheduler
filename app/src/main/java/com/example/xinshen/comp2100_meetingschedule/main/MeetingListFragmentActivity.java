package com.example.xinshen.comp2100_meetingschedule.main;

import android.net.wifi.WifiInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;

import com.example.xinshen.comp2100_meetingschedule.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.example.xinshen.comp2100_meetingschedule.main.MainActivity.SCREEN_HEIGHT;
import static com.example.xinshen.comp2100_meetingschedule.main.MainActivity.SCREEN_WIDTH;

public class MeetingListFragmentActivity extends Fragment {
    private MeetingsListview lv_coming_meetins;
    public List<ScrolledMeetings> meetings_list;
    public ScrolledMeetingAdapter meetings_list_adapter;

    private LinearLayout mutil_del_meetings_controls;
    private LinearLayout add_meetings_controls;

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
                "meeting agenda", "110", "CSIT ground floor"));
        data.add(new ScrolledMeetings(R.drawable.icon, "engn6528",
                "project perspective", "111", "CSIT ground floor"));
        data.add(new ScrolledMeetings(R.drawable.icon, "comp8600",
                "stage 1 tasks", "3.32", "hancock 3rd floor"));
        data.add(new ScrolledMeetings(R.drawable.icon, "comp8330",
                "comp2100 assignment group meeting", "113", "Lena building 9th floor"));
        data.add(new ScrolledMeetings(R.drawable.icon, "comp1110",
                "meeting agenda", "114", "CSIT ground floor"));
        data.add(new ScrolledMeetings(R.drawable.icon, "engn6528",
                "project perspective", "115", "CSIT ground floor"));
        data.add(new ScrolledMeetings(R.drawable.icon, "comp8600",
                "stage 1 tasks", "3.36", "hancock 3rd floor"));
        data.add(new ScrolledMeetings(R.drawable.icon, "comp8330",
                "comp2100 assignment group meeting", "117", "Lena building 9th floor"));
        data.add(new ScrolledMeetings(R.drawable.icon, "comp8300",
                "comp2100 assignment group meeting", "118", "Lena building 9th floor"));
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
        lv_coming_meetins = (MeetingsListview) view.findViewById(R.id.scroll_coming_meetingLv);
        meetings_list_adapter = new ScrolledMeetingAdapter(getContext(),
                R.layout.scrolled_meetings_listview, meetings_list);
        lv_coming_meetins.setAdapter(meetings_list_adapter);
        lv_coming_meetins.getLayoutParams().height = (int) (SCREEN_HEIGHT * 0.8);
        lv_coming_meetins.setOnDeleteListener(new MeetingsListview.OnEditListener() {
            @Override
            public void onEdit(int index) {
//                Log.i("shenxin", "activity onEdit index:"+index);
                meetings_list.remove(index);
                meetings_list_adapter.notifyDataSetChanged();
            }

            @Override
            public void onMutilEdit(int[] indexs) {
                meetings_list.remove(indexs[0]);
                meetings_list_adapter.notifyDataSetChanged();
            }

            @Override
            public void show_delete_all_btn() {
                add_meetings_controls = (LinearLayout) getView().findViewById(R.id.add_meetings_controls);
                mutil_del_meetings_controls = (LinearLayout) getView().findViewById(R.id.mutil_delete_meetings_controls);
                add_meetings_controls.setVisibility(View.GONE);
                mutil_del_meetings_controls.setVisibility(View.VISIBLE);
            }

            @Override
            public void hide_delete_all_btn() {
                if (add_meetings_controls == null) {
                    add_meetings_controls = (LinearLayout) getView().findViewById(R.id.add_meetings_controls);
                    mutil_del_meetings_controls = (LinearLayout) getView().findViewById(R.id.mutil_delete_meetings_controls);
                }
                add_meetings_controls.setVisibility(View.VISIBLE);
                mutil_del_meetings_controls.setVisibility(View.GONE);
            }
        });
        return view;
    }

    public void deleteSelectedMeetings(View v) {
        Log.i("shenxin", "delete btn: " + lv_coming_meetins.selecting_cbs.size());
//        Log.i("shenxin", "bef:" + meetings_list.size());

        for (int i = lv_coming_meetins.selecting_cbs.size() - 1; i >= 0; i--) {
//            Log.i("shenxin", "delete id: " + lv_coming_meetins.selecting_cbs.get(i));
            //lv_coming_meetins.removeViewAt(lv_coming_meetins.selecting_cbs.get(i));
            meetings_list.remove((int) lv_coming_meetins.selecting_cbs.get(i));
        }
        meetings_list_adapter.notifyDataSetChanged();
        if (add_meetings_controls == null) {
            add_meetings_controls = (LinearLayout) getView().findViewById(R.id.add_meetings_controls);
            mutil_del_meetings_controls = (LinearLayout) getView().findViewById(R.id.mutil_delete_meetings_controls);
        }
        add_meetings_controls.setVisibility(View.VISIBLE);
        mutil_del_meetings_controls.setVisibility(View.GONE);
    }

}
