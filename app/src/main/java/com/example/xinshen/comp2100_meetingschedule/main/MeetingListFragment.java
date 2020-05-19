package com.example.xinshen.comp2100_meetingschedule.main;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.example.xinshen.comp2100_meetingschedule.R;

import java.util.ArrayList;
import java.util.List;

import static com.example.xinshen.comp2100_meetingschedule.main.MainActivity.SCREEN_HEIGHT;

public class MeetingListFragment extends Fragment {
    private MeetingsListview lv_coming_meetins;
    public ArrayList<MeetingModel> meetings_list;
    public ScrolledMeetingAdapter meetings_list_adapter;

    private LinearLayout mutil_del_meetings_controls;
    private LinearLayout add_meetings_controls;

    MeetingListFragment() {
        super();
        meetings_list = get_mock_data();
    }

    MeetingListFragment(ArrayList<MeetingModel> data) {
        super();
        if (data.size() == 0) {
            meetings_list = get_mock_past_data();
        } else {
            meetings_list = data;
        }
    }

    static public ArrayList<MeetingModel> get_mock_data() {
        ArrayList<MeetingModel> data = new ArrayList<>();
        data.add(new MeetingModel(R.drawable.icon, "2100 group assigment",
                "meeting agenda", "110", "CSIT ground floor",1,11,12));
        data.add(new MeetingModel(R.drawable.icon, "engn6528",
                "project perspective", "111", "CSIT ground floor",1,15,16));
        data.add(new MeetingModel(R.drawable.icon, "8110 ass3",
                "stage 1 tasks", "3.32", "hancock 3rd floor",2,9,11));
        data.add(new MeetingModel(R.drawable.icon, "comp8330",
                "comp2100 assignment group meeting", "113", "Lena building 9th floor",3,14));
        data.add(new MeetingModel(R.drawable.icon, "critical writing seminar",
                "meeting agenda", "114", "CSIT ground floor",4,18,19));
        data.add(new MeetingModel(R.drawable.icon, "family call",
                "project perspective", "115", "CSIT ground floor",4,14,15));
        data.add(new MeetingModel(R.drawable.icon, "comp8600",
                "stage 1 tasks", "3.36", "hancock 3rd floor",6,12,13));
        data.add(new MeetingModel(R.drawable.icon, "database workshop",
                "comp2100 assignment group meeting", "117", "Lena building 9th floor",6,10));
        data.add(new MeetingModel(R.drawable.icon, "beer party",
                "comp2100 assignment group meeting", "118", "Lena building 9th floor",7,14,15));
        return data;
    }

    ArrayList<MeetingModel> get_mock_past_data() {
        ArrayList<MeetingModel> data = new ArrayList<>();
        data.add(new MeetingModel(R.drawable.icon, "comp2100 group formation",
                "team formation", "101", "CSIT ground floor",3,8));
        data.add(new MeetingModel(R.drawable.icon, "comp6442",
                "Choose the topic for assignment", "108", "Hanna Building 1st floor",3,8));
        return data;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        Log.i("shenxin", "ssxx2 meetingLv onCreateView");
        View view = inflater.inflate(R.layout.activity_fragment_meeting_lv, null);
        lv_coming_meetins = (MeetingsListview) view.findViewById(R.id.scroll_coming_meetingLv);
        meetings_list_adapter = new ScrolledMeetingAdapter(getContext(),
                R.layout.scrolled_meetings_listview, meetings_list);
        lv_coming_meetins.setAdapter(meetings_list_adapter);
        lv_coming_meetins.getLayoutParams().height = (int) (SCREEN_HEIGHT * 0.8);
        lv_coming_meetins.setOnDeleteListener(new MeetingsListview.OnEditListener() {
            @Override
            public void onDeletePressed(int index) {
//                Log.i("shenxin", "activity onEdit index:"+index);
                meetings_list.remove(index);
                lv_coming_meetins.items_view_ary.remove(index);
                MeetingSchedulerFragment.rm_idx_ary.add(index);
                meetings_list_adapter.notifyDataSetChanged();
            }

            @Override
            public void onMutilEdit(int[] indexs) {
                //meetings_list.remove(indexs[0]);
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
        lv_coming_meetins.bondAdapter(meetings_list_adapter);
        return view;
    }

    public Integer[] deleteSelectedMeetings(View v) {
        Log.i("shenxin", "delete btn: " + lv_coming_meetins.selecting_cbs.size());
        lv_coming_meetins.isMutilDeleteShown = false;
        for (int i = lv_coming_meetins.selecting_cbs.size() - 1; i >= 0; i--) {
            int id = lv_coming_meetins.selecting_cbs.get(i);
            if (id < meetings_list.size()) {
                meetings_list.remove(id);
                lv_coming_meetins.items_view_ary.remove(id);
            }
        }
        meetings_list_adapter.notifyDataSetChanged();
        if (add_meetings_controls == null) {
            add_meetings_controls = (LinearLayout) getView().findViewById(R.id.add_meetings_controls);
            mutil_del_meetings_controls = (LinearLayout) getView().findViewById(R.id.mutil_delete_meetings_controls);
        }
        add_meetings_controls.setVisibility(View.VISIBLE);
        mutil_del_meetings_controls.setVisibility(View.GONE);
        return lv_coming_meetins.selecting_cbs.toArray(new Integer[lv_coming_meetins.selecting_cbs.size()]);
    }

}
