package com.example.xinshen.comp2100_meetingschedule;

import android.content.Intent;

import com.example.xinshen.comp2100_meetingschedule.main.MainActivity;
import com.example.xinshen.comp2100_meetingschedule.main.MeetingSchedulerFragment;
import com.example.xinshen.comp2100_meetingschedule.main.WelcomeActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static org.junit.Assert.*;
import static org.robolectric.Shadows.shadowOf;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@RunWith(RobolectricTestRunner.class)
public class MainactivityTest {

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

    @Test
    public void fromWelcome2mainActivity() {
        WelcomeActivity activity = Robolectric.setupActivity(WelcomeActivity.class);

        Intent expectedIntent = new Intent(activity, MainActivity.class);
        Intent actual = shadowOf(RuntimeEnvironment.application).getNextStartedActivity();
        if (actual != null)
            assertEquals(expectedIntent.getComponent(), actual.getComponent());
    }

    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }
}