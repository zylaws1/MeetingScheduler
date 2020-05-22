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

import static com.example.xinshen.comp2100_meetingschedule.main.MainActivity.SCREEN_HEIGHT;

public class MeetingListFragment extends Fragment {
    private static final String TAG = "shenxin";
    private MeetingsListview lv_meetins;
    public ArrayList<MeetingModel> meetings_list;
    private ScrolledMeetingAdapter meetings_list_adapter;
    private LinearLayout mutil_del_meetings_controls;
    private LinearLayout add_meetings_controls;

    // set the meetings list and refresh ui if loaded
    public void setMeetings_list(ArrayList<MeetingModel> meetings_list) {
        this.meetings_list = meetings_list;
        if (meetings_list_adapter != null)
            meetings_list_adapter.notifyDataSetChanged();
    }

    // init coming meetings data from server or mocked local data
    public MeetingListFragment() {
        super();
        meetings_list = MainActivity.instance.getComing_meetings_data();
//        meetings_list = get_mock_data();
    }

    // init past meetings data from server or mocked local data
    MeetingListFragment(boolean isPast) {
        super();
        meetings_list = MainActivity.instance.getPast_meetings_data();
//        meetings_list = get_mock_past_data();
    }

    // init meetings list from specific data list for test
    MeetingListFragment(ArrayList<MeetingModel> data) {
        super();
        if (data.size() == 0) {
            meetings_list = get_mock_past_data();
        } else {
            meetings_list = data;
        }
    }

    // return the MeetingModel object by meeting name
    public MeetingModel getModelByName(String name) {
        for (int i = 0; i < meetings_list.size(); i++) {
            if (meetings_list.get(i).getName().equals(name))
                return meetings_list.get(i);
        }
        return null;
    }

    // get mocked  local data for test
    static public ArrayList<MeetingModel> get_mock_data() {
        ArrayList<MeetingModel> data = new ArrayList<>();
        data.add(new MeetingModel(R.drawable.icon, "2100 group assigment",
                "meeting agenda", "110", "CSIT ground floor", 1, "2020-05-22", "11:30", 11, 12));
        data.add(new MeetingModel(R.drawable.icon, "engn6528",
                "project perspective", "111", "CSIT ground floor", 1, "2020-05-22", "15:30", 15, 16));
        data.add(new MeetingModel(R.drawable.icon, "8110 ass3",
                "stage 1 tasks", "3.32", "hancock 3rd floor", 2, "2020-05-22", "9:30", 9, 11));
        data.add(new MeetingModel(R.drawable.icon, "comp8330",
                "comp2100 assignment group meeting", "113", "Lena building 9th floor", 3, "2020-05-22", "15:30", 14));
        data.add(new MeetingModel(R.drawable.icon, "critical writing seminar",
                "meeting agenda", "114", "CSIT ground floor", 4, "2020-05-22", "18:30", 18, 19));
        data.add(new MeetingModel(R.drawable.icon, "family call",
                "project perspective", "115", "CSIT ground floor", 4, "2020-05-22", "15:30", 15, 16));
        data.add(new MeetingModel(R.drawable.icon, "comp8600",
                "stage 1 tasks", "3.36", "hancock 3rd floor", 6, "2020-05-22", "12:30", 12, 13));
        data.add(new MeetingModel(R.drawable.icon, "database workshop",
                "comp2100 assignment group meeting", "117", "Lena building 9th floor", 6, "2020-05-22", "10:30", 10));
        data.add(new MeetingModel(R.drawable.icon, "beer party",
                "comp2100 assignment group meeting", "118", "Lena building 9th floor", 7, "2020-05-22", "14:30", 14, 15));
        return data;
    }

    // get mocked  local data for test
    ArrayList<MeetingModel> get_mock_past_data() {
        ArrayList<MeetingModel> data = new ArrayList<>();
        data.add(new MeetingModel(R.drawable.icon, "comp2100 group formation",
                "team formation", "101", "CSIT ground floor", 3, "2020-05-22", "8:30", 8));
        data.add(new MeetingModel(R.drawable.icon, "comp6442",
                "Choose the topic for assignment", "108", "Hanna Building 1st floor", 3, "2020-05-22", "8:30", 8));
        return data;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // bond the views and controls for the first time initialization
        View view = inflater.inflate(R.layout.fragment_meeting_lv, null);
        lv_meetins = (MeetingsListview) view.findViewById(R.id.scroll_coming_meetingLv);
        if (meetings_list == null) {    // reinitialise the meetings_list if the data come from server delayed
            Log.w(TAG, "MeetingListFragment onCreateView: meetings_list null");
            meetings_list = new ArrayList<>();
        }
        meetings_list_adapter = new ScrolledMeetingAdapter(getContext(),
                R.layout.scrolled_meetings_listview, meetings_list);
        if (lv_meetins == null)  // reinitialise the lv_meetins if the data come from server delayed
            Log.e(TAG, "MeetingListFragment onCreateView: lv_coming_meetins null");
        if (meetings_list_adapter == null)  // reinitialise the meetings_list_adapter if the data come from server delayed
            Log.e(TAG, "MeetingListFragment onCreateView: meetings_list_adapter null");
        lv_meetins.setAdapter(meetings_list_adapter);
        lv_meetins.getLayoutParams().height = (int) (SCREEN_HEIGHT * 0.8);
        // bond listener for deleting and multi deleting
        lv_meetins.setOnDeleteListener(new MeetingsListview.OnEditListener() {
            @Override
            public void onDeletePressed(int index) {    //  when delete button clicked, remove the chosen item
//                Log.i("shenxin", "activity onEdit index:"+index);
                meetings_list.remove(index);
                lv_meetins.items_view_ary.remove(index);
                MeetingSchedulerFragment.rm_idx_ary.add(index);
                meetings_list_adapter.notifyDataSetChanged();   // refresh the listview UI
            }

            @Override
            public void onMultiDeleted(int[] indexs) {
                //  when multi delete button clicked, do nothing until press delete all btn
                meetings_list_adapter.notifyDataSetChanged();
            }

            @Override
            public void show_delete_all_btn() {  // on long pressed show all checkboxes on right side for multi deleting
                add_meetings_controls = (LinearLayout) getView().findViewById(R.id.add_meetings_controls);
                mutil_del_meetings_controls = (LinearLayout) getView().findViewById(R.id.mutil_delete_meetings_controls);
                // change the apply control in bottom
                add_meetings_controls.setVisibility(View.GONE);
                mutil_del_meetings_controls.setVisibility(View.VISIBLE);
            }

            @Override
            public void hide_delete_all_btn() {  // when all checkboxes are showing then hide then if touch other places
                if (add_meetings_controls == null) {        // deactivate the controls for deleting
                    add_meetings_controls = (LinearLayout) getView().findViewById(R.id.add_meetings_controls);
                    mutil_del_meetings_controls = (LinearLayout) getView().findViewById(R.id.mutil_delete_meetings_controls);
                }
                // change the apply control in bottom
                add_meetings_controls.setVisibility(View.VISIBLE);
                mutil_del_meetings_controls.setVisibility(View.GONE);
            }
        });
        lv_meetins.bondAdapter(meetings_list_adapter);
        return view;
    }

    // Delete all selected meetings from data list and refresh the list view
    // return :Integer[] indexes have been deleted from data array
    public Integer[] deleteSelectedMeetings(View v) {
        Log.i("shenxin", "delete btn: " + lv_meetins.selecting_cbs.size());
        lv_meetins.isMultiDeleteShown = false;
        // iterate all indexes in array and remove meetings in reverse order
        for (int i = lv_meetins.selecting_cbs.size() - 1; i >= 0; i--) {
            int id = lv_meetins.selecting_cbs.get(i);
            if (id < meetings_list.size()) {
                meetings_list.remove(id);
                lv_meetins.items_view_ary.remove(id);
            }
        }
        // refresh ui list view
        meetings_list_adapter.notifyDataSetChanged();
        // deactivate the controls for deleting
        if (add_meetings_controls == null) {
            add_meetings_controls = (LinearLayout) getView().findViewById(R.id.add_meetings_controls);
            mutil_del_meetings_controls = (LinearLayout) getView().findViewById(R.id.mutil_delete_meetings_controls);
        }
        // change the apply control in bottom
        add_meetings_controls.setVisibility(View.VISIBLE);
        mutil_del_meetings_controls.setVisibility(View.GONE);
        // return the indexes have been deleted from data array in type Integer[]
        return lv_meetins.selecting_cbs.toArray(new Integer[lv_meetins.selecting_cbs.size()]);
    }

}
