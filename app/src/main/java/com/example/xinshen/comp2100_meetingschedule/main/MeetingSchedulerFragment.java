package com.example.xinshen.comp2100_meetingschedule.main;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.xinshen.comp2100_meetingschedule.R;

import java.util.ArrayList;
import java.util.List;

public class MeetingSchedulerFragment extends Fragment {
    public MeetingSchedulerView mTimaTableView;
    public ArrayList<MeetingModel> mList = new ArrayList<MeetingModel>();
    public static ArrayList<Integer> rm_idx_ary = new ArrayList<>();
    private View rootView;
    private List<MeetingModel> meetings_list;

    MeetingSchedulerFragment(List<MeetingModel> meetings_list) {
        this.meetings_list = meetings_list;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_sheduler, null);
            mList.clear();
            if (meetings_list == null) {
                meetings_list = MainActivity.instance.comingMeetingsFragment.meetings_list;
            }
            mList.addAll(meetings_list);
            mTimaTableView = (MeetingSchedulerView) rootView.findViewById(R.id.scheduler_timetable_ly);
        }


        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("shenxin", "scheduler onResume: " + meetings_list.size());
        mList.clear();
        mList.addAll(meetings_list);
        mTimaTableView.setTimeTable(mList);
    }

    //    private void addList() {
//        mList.add(new MeetingModel(0, 1, 2, 1, "8:20", "10:10", "comp6442",
//                "disscuss about ...", "csit101", " CSIT"));
//        mList.add(new MeetingModel(0, 3, 4, 1, "8:20", "10:10", "2100 gourp project",
//                "disscuss about ...", "home", " home"));
//        mList.add(new MeetingModel(0, 6, 7, 1, "8:20", "10:10", "engn6528",
//                "disscuss about ...", "zoom ", "home"));
//
//
//        mList.add(new MeetingModel(0, 6, 7, 2, "8:20", "10:10", "2100 ass agenda",
//                "disscuss about ...", "csit112", "CSIT"));
//        mList.add(new MeetingModel(0, 8, 9, 2, "8:20", "10:10", "comp6442",
//                "disscuss about ...", "HNB 3.02", "Hanna Building"));
//
//        mList.add(new MeetingModel(0, 1, 2, 3, "8:20", "10:10", "comp6710",
//                "disscuss about ...", "hancock 3.33", "Hancock library"));
//
//        mList.add(new MeetingModel(0, 6, 7, 3, "8:20", "10:10", "2100 ass agenda",
//                "disscuss about ...", "csit101", "CSIT"));
//        mList.add(new MeetingModel(0, 8, 9, 4, "8:20", "10:10", "social dance",
//                "disscuss about ...", "hancock3.30", "Hancock library"));
//        mList.add(new MeetingModel(0, 3, 5, 4, "8:20", "10:10", "jogging group",
//                "disscuss about ...", "csit309", "CSIT"));
//        mList.add(new MeetingModel(0, 6, 8, 5, "8:20", "10:10", "family reunion",
//                "disscuss about ...", "csit110", "CSIT"));
//        mList.add(new MeetingModel(0, 3, 5, 5, "8:20", "10:10", "comp9999",
//                "disscuss about ...", "Hanna building 102", "Hanna Building"));
//
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.main_screenshot:
//                ScreenshotUtil.getBitmapByView(this, (ScrollView) findViewById(R.id.main_scrollview));
//                break;
//        }
        return true;
    }
}
