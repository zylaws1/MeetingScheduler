package com.example.xinshen.comp2100_meetingschedule;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.testing.FragmentScenario;

import com.example.xinshen.comp2100_meetingschedule.main.MainActivity;
import com.example.xinshen.comp2100_meetingschedule.main.MeetingInfoFragment;
import com.example.xinshen.comp2100_meetingschedule.main.MeetingListFragment;
import com.example.xinshen.comp2100_meetingschedule.main.MeetingSchedulerFragment;
import com.example.xinshen.comp2100_meetingschedule.main.MeetingsListview;
import com.example.xinshen.comp2100_meetingschedule.main.SearchActivity;
import com.example.xinshen.comp2100_meetingschedule.main.WelcomeActivity;

import org.apache.tools.ant.Main;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.shadows.ShadowToast;
import org.robolectric.util.FragmentTestUtil;

import static org.junit.Assert.*;
import static org.robolectric.Shadows.shadowOf;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@RunWith(RobolectricTestRunner.class)
public class MainactivityTest {
    public static final String TAG = "sx test";

    // test add work flow function
    @Test
    public void addMeetingInMainActivity() {
        MainActivity activity = Robolectric.setupActivity(MainActivity.class);
        int ori_size = activity.getComingMeetingsFragment().meetings_list.size();
        activity.findViewById(R.id.add_meetings_controls).performClick();
        activity.findViewById(R.id.btn_submit_add).performClick();

        Intent actual = shadowOf(RuntimeEnvironment.application).getNextStartedActivity();
        assertEquals(null, actual);

        assertEquals(ori_size,
                activity.getComingMeetingsFragment().meetings_list.size());

        activity.findViewById(R.id.add_meetings_controls).performClick();
        activity.findViewById(R.id.btn_cancel_add).performClick();

        assertEquals(ori_size,
                activity.getComingMeetingsFragment().meetings_list.size());
    }

    // test intent to search activity
    @Test
    public void startSearchActivity() {
        ActivityController<MainActivity> mainController
                = Robolectric.buildActivity(MainActivity.class);
        MainActivity activity = mainController.create().start().resume().get();
        activity.findViewById(R.id.navigation_search).performClick();

        Intent actual = shadowOf(RuntimeEnvironment.application).getNextStartedActivity();
        assertEquals(SearchActivity.class, actual);
    }

    // test search activity
    @Test
    public void searchActivity() {
        ActivityController<SearchActivity> mainController
                = Robolectric.buildActivity(SearchActivity.class);
        SearchActivity activity = mainController.create().start().resume().get();
        EditText input = activity.findViewById(R.id.editText_search);
        input.setText("");
        Intent actual = shadowOf(RuntimeEnvironment.application).getNextStartedActivity();
        assertEquals(SearchActivity.class, actual);
    }

    // test work flow function in listview
    @Test
    public void listviewMainActivity() {
        ActivityController<MainActivity> mainController
                = Robolectric.buildActivity(MainActivity.class);
        MainActivity activity = mainController.create().start().resume().get();
        assertNotNull(activity);
        assertNotNull(activity.getComingMeetingsFragment());
        assertNotNull(activity.getComingMeetingsFragment().meetings_list);
        MeetingsListview listview = activity.findViewById(R.id.scroll_coming_meetingLv);
        int item_idx = 0;
        listview.performItemClick(listview.getChildAt(item_idx), item_idx, item_idx);
        assertEquals(1, activity.getSupportFragmentManager().getFragments().size());
        assertEquals(MeetingListFragment.class,
                activity.getSupportFragmentManager().getFragments().get(0).getClass());
    }

    // test tap and long press in list view
    @Test
    public void controllersFromListViewMainActivity() {
        ActivityController<MainActivity> mainController = Robolectric.buildActivity(MainActivity.class);
        MainActivity activity = mainController.create().start().resume().get();
        int ori_size = activity.getComingMeetingsFragment().meetings_list.size();
        // select list view fragement
        activity.getBotm_navigation().setSelectedItemId(R.id.navigation_meeting_lists);
        FragmentScenario<MeetingListFragment> meetingListViewFragment =
                FragmentScenario.launch(MeetingListFragment.class);
        meetingListViewFragment.onFragment(new FragmentScenario.FragmentAction<MeetingListFragment>() {
            @Override
            public void perform(@NonNull MeetingListFragment fragment) {
                MeetingsListview meeting_item = fragment.getView().findViewById(R.id.scroll_coming_meetingLv);
                assertNotNull(meeting_item);
                assertTrue(meeting_item.isClickable());
                meeting_item.performClick();
            }
        });
        assertEquals(ori_size,
                activity.getComingMeetingsFragment().meetings_list.size());
    }

    // test controller life circle in MeetingInfoFragment
    @Test
    public void controllersFromMeetingInfoFragment() {
        ActivityController<MainActivity> mainController = Robolectric.buildActivity(MainActivity.class);
        MainActivity activity = mainController.create().start().resume().get();
        int ori_size = activity.getComingMeetingsFragment().meetings_list.size();
        // select list view fragement
        activity.getBotm_navigation().setSelectedItemId(R.id.navigation_meeting_lists);
        FragmentScenario<MeetingInfoFragment> meetingInfoFragment =
                FragmentScenario.launch(MeetingInfoFragment.class);
        meetingInfoFragment.onFragment(new FragmentScenario.FragmentAction<MeetingInfoFragment>() {
            @Override
            public void perform(@NonNull MeetingInfoFragment fragment) {
                //fragment.getView().findViewById(R.id.scroll_courses_list_names).performClick();
                Button exit_infoBtn = fragment.getView().findViewById(R.id.button_exit_info);
                assertNotNull(exit_infoBtn);
                assertTrue(exit_infoBtn.isClickable());
                assertEquals("Exit", exit_infoBtn.getText());
            }
        });
        assertEquals(ori_size,
                activity.getComingMeetingsFragment().meetings_list.size());
    }

    @Test
    public void fromWelcome2mainActivity() {
        WelcomeActivity activity = Robolectric.setupActivity(WelcomeActivity.class);

        Intent expectedIntent = new Intent(activity, MainActivity.class);
        Intent actual = shadowOf(RuntimeEnvironment.application).getNextStartedActivity();
        if (actual != null)
            assertEquals(expectedIntent.getComponent(), actual.getComponent());
    }

    @Test
    public void addition_isCorrect() { //base test for environment
        Log.i(TAG, "======== test start ======== ");
        assertEquals(4, 2 + 2);
    }
}