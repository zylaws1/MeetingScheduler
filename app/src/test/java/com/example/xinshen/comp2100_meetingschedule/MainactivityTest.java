package com.example.xinshen.comp2100_meetingschedule;

import android.app.Instrumentation;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.InstrumentationRegistry;

import com.example.xinshen.comp2100_meetingschedule.data.model.Feedback;
import com.example.xinshen.comp2100_meetingschedule.data.model.FeedbackBean;
import com.example.xinshen.comp2100_meetingschedule.data.model.UserInfo;
import com.example.xinshen.comp2100_meetingschedule.database.MeetingSQLiteOpenHelper;
import com.example.xinshen.comp2100_meetingschedule.database.NoteDBManager;
import com.example.xinshen.comp2100_meetingschedule.main.MainActivity;
import com.example.xinshen.comp2100_meetingschedule.main.MeetingDeadlineNotification;
import com.example.xinshen.comp2100_meetingschedule.main.MeetingInfoFragment;
import com.example.xinshen.comp2100_meetingschedule.main.MeetingListFragment;
import com.example.xinshen.comp2100_meetingschedule.main.MeetingModel;
import com.example.xinshen.comp2100_meetingschedule.main.MeetingSchedulerFragment;
import com.example.xinshen.comp2100_meetingschedule.main.MeetingsListview;
import com.example.xinshen.comp2100_meetingschedule.main.NoteEditFragment;
import com.example.xinshen.comp2100_meetingschedule.main.NoteListFragment;
import com.example.xinshen.comp2100_meetingschedule.main.OwnProfileFragment;
import com.example.xinshen.comp2100_meetingschedule.main.QuickHelpFragment;
import com.example.xinshen.comp2100_meetingschedule.main.ScrolledMeetingAdapter;
import com.example.xinshen.comp2100_meetingschedule.main.SearchActivity;
import com.example.xinshen.comp2100_meetingschedule.main.SetPreferTimeslotFragment;
import com.example.xinshen.comp2100_meetingschedule.main.WelcomeActivity;
import com.example.xinshen.comp2100_meetingschedule.main.AboutFragment;
import com.example.xinshen.comp2100_meetingschedule.main.FeedbackFragment;
import com.example.xinshen.comp2100_meetingschedule.main.MeetingSchedulerView;
import com.example.xinshen.comp2100_meetingschedule.main.SettingsFragment;
import com.example.xinshen.comp2100_meetingschedule.ui.login.LoginFragment;
import com.example.xinshen.comp2100_meetingschedule.ui.login.RegisterFragment;
import com.example.xinshen.comp2100_meetingschedule.ui.login.RegisterViewModel;
import com.google.firebase.FirebaseApp;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.robolectric.Shadows.shadowOf;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@RunWith(RobolectricTestRunner.class)
public class MainactivityTest {
    public static final String TAG = "sx test";

    // Simulate a single click event
    private void simulateClick(View view, float x, float y) {
        long time = SystemClock.uptimeMillis();

        MotionEvent downEvent = MotionEvent.obtain(time, time, MotionEvent.ACTION_DOWN, x, y, 0);
        time += 500;
        MotionEvent upEvent = MotionEvent.obtain(time, time, MotionEvent.ACTION_UP, x, y, 0);
        view.onTouchEvent(downEvent);
        view.onTouchEvent(upEvent);
    }

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
        activity.findViewById(R.id.btn_Date).performClick();
        activity.findViewById(R.id.btn_Time).performClick();
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
        Intent expected = new Intent(activity, SearchActivity.class);
        assertEquals(expected.getClass(), actual.getClass());
    }

    // test search activity
    @Test
    public void searchActivity() {
        ActivityController<SearchActivity> mainController
                = Robolectric.buildActivity(SearchActivity.class);
        SearchActivity activity = mainController.create().start().resume().get();
        EditText input = activity.findViewById(R.id.editText_search);
        input.setText("");
        Button goBtn = activity.findViewById(R.id.button_go);

        Intent actual = shadowOf(RuntimeEnvironment.application).getNextStartedActivity();
        assertEquals(0, activity.getList().size());
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
                ScrolledMeetingAdapter meetings_list_adapter =
                        new ScrolledMeetingAdapter(fragment.getContext(),
                                R.layout.scrolled_meetings_listview, MeetingListFragment.get_mock_data());
                fragment.getLv_meetins().setAdapter(meetings_list_adapter);
                fragment.getLv_meetins().bondAdapter(meetings_list_adapter);
                MeetingsListview meetingsListview = fragment.getLv_meetins();
                assertNotNull(meetingsListview);
                meetingsListview.setAdapter(meetings_list_adapter);
                meetingsListview.bondAdapter(meetings_list_adapter);
//                meetingsListview.addView(new 0,);
                meetingsListview.performClick();
                meetingsListview.performLongClick();
                meetingsListview.dispatchNestedFling(20, 0, true);
                assertEquals(0, meetingsListview.getChildCount());
                simulateClick(fragment.getView(), 50, 20);
                long time = SystemClock.uptimeMillis();
                meetingsListview.onSingleTapUp(MotionEvent.obtain(time, time + 50,
                        MotionEvent.ACTION_DOWN, 50, 0, 0));
                meetingsListview.onFling(
                        MotionEvent.obtain(time, time + 50,
                                MotionEvent.ACTION_DOWN, 50, 0, 0),
                        MotionEvent.obtain(time, time + 50,
                                MotionEvent.ACTION_UP, 80, 0, 0),
                        50, 0
                );
                meetingsListview.onLongPress(MotionEvent.obtain(time, time + 500,
                        MotionEvent.ACTION_DOWN, 50, 0, 0));
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

    // test controller life circle in AboutFragment
    @Test
    public void controllersFromAboutFragment() {
        ActivityController<MainActivity> mainController = Robolectric.buildActivity(MainActivity.class);
        final MainActivity activity = mainController.create().start().resume().get();
        activity.getBotm_navigation().setSelectedItemId(R.id.navigation_meeting_lists);
        FragmentScenario<AboutFragment> fragment =
                FragmentScenario.launch(AboutFragment.class);
        fragment.onFragment(new FragmentScenario.FragmentAction<AboutFragment>() {
            @Override
            public void perform(@NonNull AboutFragment fragment) {
                //fragment.getView().findViewById(R.id.scroll_courses_list_names).performClick();
                Object back_obj = fragment.getView().findViewById(R.id.iv_back);
                assertEquals(ImageView.class, back_obj.getClass());
                ImageView back_img = (ImageView) back_obj;
                back_img.performClick();
            }
        });
    }

    // test controller life circle in Feedback fragment
    @Test
    public void controllersFromFeedbackFragment() {
        Context context= InstrumentationRegistry.getContext();
        FirebaseApp.initializeApp(context);
        ActivityController<MainActivity> mainController = Robolectric.buildActivity(MainActivity.class);
        final MainActivity activity = mainController.create().start().resume().get();
        activity.getBotm_navigation().setSelectedItemId(R.id.navigation_meeting_lists);
        FragmentScenario<FeedbackFragment> fragment =
                FragmentScenario.launch(FeedbackFragment.class);
        fragment.onFragment(new FragmentScenario.FragmentAction<FeedbackFragment>() {
            @Override
            public void perform(@NonNull FeedbackFragment fragment) {
                //fragment.getView().findViewById(R.id.scroll_courses_list_names).performClick();
                FeedbackFragment.beanList.clear();
                FeedbackFragment.userName = "1111";
                assertEquals(ImageView.class, fragment.getView().findViewById(R.id.fb_back).getClass());
                ImageView back_obj = fragment.getView().findViewById(R.id.fb_back);
                assertTrue(back_obj.isClickable());
                EditText editText = fragment.getView().findViewById(R.id.et_feedback);
                editText.setText("哈哈");
                TextView textView = fragment.getView().findViewById(R.id.fb_right);
                assertEquals("Submit", textView.getText().toString());
                textView.performClick();
                FeedbackBean bean = FeedbackFragment.beanList.get(0);
                assertEquals("1111", bean.getName());
                assertEquals("哈哈", bean.getFeedback());
            }
        });
    }

    // test controller life circle in MeetingSchedulerView
    @Test
    public void controllersFromMeetingSchedulerView() {
        ActivityController<MainActivity> mainController = Robolectric.buildActivity(MainActivity.class);
        final MainActivity activity = mainController.create().start().resume().get();
        activity.getBotm_navigation().setSelectedItemId(R.id.navigation_meeting_lists);
        FragmentScenario<MeetingSchedulerFragment> fragment =
                FragmentScenario.launch(MeetingSchedulerFragment.class);
        fragment.onFragment(new FragmentScenario.FragmentAction<MeetingSchedulerFragment>() {
            @Override
            public void perform(@NonNull MeetingSchedulerFragment fragment) {
                ScrollView scrollview = fragment.getView().findViewById(R.id.s_scrollview);
                assertTrue(scrollview.isFocusable());
                assertEquals(View.VISIBLE, scrollview.getVisibility());
                assertEquals(MeetingSchedulerView.class, scrollview.getChildAt(0).getClass());
                MeetingSchedulerView meetingSchedulerView = (MeetingSchedulerView) scrollview.getChildAt(0);
                assertEquals(View.VISIBLE, meetingSchedulerView.getVisibility());
                meetingSchedulerView.setTimeTable(null);
                meetingSchedulerView.refreshTable();
                List<MeetingModel> mListTimeTable = new ArrayList<>();
                mListTimeTable.addAll(MeetingListFragment.get_mock_data());
                meetingSchedulerView.setTimeTable(mListTimeTable);
                meetingSchedulerView.refreshTable();
            }
        });
    }

    // test controller life circle in SettingFragment
    @Test
    public void controllersFromSettingFragmentView() {
        ActivityController<MainActivity> mainController = Robolectric.buildActivity(MainActivity.class);
        final MainActivity activity = mainController.create().start().resume().get();
        activity.getBotm_navigation().setSelectedItemId(R.id.navigation_meeting_lists);
        FragmentScenario<SettingsFragment> fragment =
                FragmentScenario.launch(SettingsFragment.class);
        fragment.onFragment(new FragmentScenario.FragmentAction<SettingsFragment>() {
            @Override
            public void perform(@NonNull SettingsFragment fragment) {
                //fragment.getView().findViewById(R.id.scroll_courses_list_names).performClick();
                RelativeLayout feedback = fragment.getView().findViewById(R.id.layout_feedback);
                RelativeLayout qucik_help = fragment.getView().findViewById(R.id.layout_quick_help);
                RelativeLayout about_meeting = fragment.getView().findViewById(R.id.layout_about_meeting);
                RelativeLayout layout_sign_out = fragment.getView().findViewById(R.id.layout_sign_out);
                assertTrue(feedback.isClickable());
                assertTrue(qucik_help.isClickable());
                assertTrue(about_meeting.isClickable());
                assertTrue(layout_sign_out.isClickable());
                feedback.performClick();
//                qucik_help.performClick();
//                about_meeting.performClick();
                layout_sign_out.performClick();
            }
        });
    }

    // test controller life circle in QuickHelpFragment
    @Test
    public void controllersQuickHelpFragmentView() {
        ActivityController<MainActivity> mainController = Robolectric.buildActivity(MainActivity.class);
        final MainActivity activity = mainController.create().start().resume().get();
        activity.getBotm_navigation().setSelectedItemId(R.id.navigation_meeting_lists);
        FragmentScenario<QuickHelpFragment> fragment =
                FragmentScenario.launch(QuickHelpFragment.class);
        fragment.onFragment(new FragmentScenario.FragmentAction<QuickHelpFragment>() {
            @Override
            public void perform(@NonNull QuickHelpFragment fragment) {
                Object back_obj = fragment.getView().findViewById(R.id.iv_back);
                assertEquals(ImageView.class, back_obj.getClass());
                ImageView back_img = (ImageView) back_obj;
                back_img.performClick();
            }
        });
    }

    // test controller life circle in OwnProfileFragment
    @Test
    public void controllersOwnProfileFragmentView() {
        ActivityController<MainActivity> mainController = Robolectric.buildActivity(MainActivity.class);
        final MainActivity activity = mainController.create().start().resume().get();
        activity.getBotm_navigation().setSelectedItemId(R.id.navigation_meeting_lists);
        FragmentScenario<OwnProfileFragment> fragment =
                FragmentScenario.launch(OwnProfileFragment.class);
        fragment.onFragment(new FragmentScenario.FragmentAction<OwnProfileFragment>() {
            @Override
            public void perform(@NonNull OwnProfileFragment fragment) {
                Object obj_login = fragment.getView().findViewById(R.id.tv_login);
                assertEquals(TextView.class, obj_login.getClass());
                TextView tv_login = (TextView) obj_login;
                assertTrue(tv_login.isClickable());
            }
        });
    }

    // test controller life circle in SetPreferTimeslotFragment
    @Test
    public void controllersSetPreferTimeslotFragmentView() {
        ActivityController<MainActivity> mainController = Robolectric.buildActivity(MainActivity.class);
        final MainActivity activity = mainController.create().start().resume().get();
        FragmentScenario<SetPreferTimeslotFragment> fragment =
                FragmentScenario.launch(SetPreferTimeslotFragment.class);
        fragment.onFragment(new FragmentScenario.FragmentAction<SetPreferTimeslotFragment>() {
            @Override
            public void perform(@NonNull SetPreferTimeslotFragment fragment) {
                Button btn_save_pref = fragment.getView().findViewById(R.id.btn_save_pref);
                assertTrue(btn_save_pref.isClickable());
                btn_save_pref.performClick();
            }
        });
    }

    // test controller life circle in NoteListFragment
    @Test
    public void controllersFromNoteListFragmentView() {
        ActivityController<MainActivity> mainController = Robolectric.buildActivity(MainActivity.class);
        final MainActivity activity = mainController.create().start().resume().get();
        NoteDBManager.DB_PATH=new Random().nextInt(1000) + ".db";
        FragmentScenario<NoteListFragment> fragment =
                FragmentScenario.launch(NoteListFragment.class);
        fragment.onFragment(new FragmentScenario.FragmentAction<NoteListFragment>() {
            @Override
            public void perform(@NonNull NoteListFragment fragment) {
                NoteDBManager.getInstance(RuntimeEnvironment.application);
                Button add_noteBtn = fragment.getView().findViewById(R.id.addNote);
                assertTrue(add_noteBtn.isClickable());
                add_noteBtn.performClick();
            }
        });
    }

    // test controller life circle in NoteEditFragment
    @Test
    public void controllersFromNoteEditFragmentView() {
        ActivityController<MainActivity> mainController = Robolectric.buildActivity(MainActivity.class);
        final MainActivity activity = mainController.create().start().resume().get();
        FragmentScenario<NoteEditFragment> fragment =
                FragmentScenario.launch(NoteEditFragment.class);
        fragment.onFragment(new FragmentScenario.FragmentAction<NoteEditFragment>() {
            @Override
            public void perform(@NonNull NoteEditFragment fragment) {
                Button save_noteBtn = fragment.getView().findViewById(R.id.save);
                assertTrue(save_noteBtn.isClickable());
                save_noteBtn.performClick();
                Button cancel_noteBtn = fragment.getView().findViewById(R.id.cancel);
                assertTrue(cancel_noteBtn.isClickable());
                cancel_noteBtn.performClick();
            }
        });
    }

    // test controller life circle in SetPreferTimeslotFragment
    @Test
    public void meetingDeadLineNotification() {
        ActivityController<MainActivity> mainController = Robolectric.buildActivity(MainActivity.class);
        final MainActivity activity = mainController.create().start().resume().get();
        MeetingDeadlineNotification meetingDNotification = new MeetingDeadlineNotification();
        meetingDNotification.addNotification(200,
                "test ticket", "test title", "test ticket");
        meetingDNotification.startNoti(6000, "test content", "test title");
        NotificationManager notificationManager = (NotificationManager)
                activity.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        meetingDNotification.cleanAllNotification();
        assertEquals(0, notificationManager.getActiveNotifications().length);
    }

    // test controller life circle in LoginFragment
    @Test
    public void controllersFromLoginFragmentView() {
        ActivityController<MainActivity> mainController = Robolectric.buildActivity(MainActivity.class);
        final MainActivity activity = mainController.create().start().resume().get();
        activity.getBotm_navigation().setSelectedItemId(R.id.navigation_meeting_lists);
        FragmentScenario<LoginFragment> fragment =
                FragmentScenario.launch(LoginFragment.class);
        fragment.onFragment(new FragmentScenario.FragmentAction<LoginFragment>() {
            @Override
            public void perform(@NonNull LoginFragment fragment) {
                //fragment.getView().findViewById(R.id.scroll_courses_list_names).performClick();
                final EditText usernameEditText = fragment.getView().findViewById(R.id.username);
                final EditText passwordEditText = fragment.getView().findViewById(R.id.password);
                final TextView registerTextView = fragment.getView().findViewById(R.id.tv_register);
                final Button loginButton = fragment.getView().findViewById(R.id.login);
                assertTrue(usernameEditText.isEnabled());
                assertTrue(passwordEditText.isEnabled());
                assertTrue(registerTextView.isClickable());
                assertTrue(loginButton.isClickable());
                usernameEditText.setText("1111");
                passwordEditText.setText("11");
//                registerTextView.performClick();
                loginButton.performClick();
                CharSequence error = passwordEditText.getError();
                assertEquals("Password must be >5 characters", error);

                usernameEditText.setText("");
                passwordEditText.setText("111111");
                CharSequence userNameError = usernameEditText.getError();
                assertEquals("Not a valid username", userNameError);

                usernameEditText.setText("e@1212");
                passwordEditText.setText("111111");
                CharSequence userNameError1 = usernameEditText.getError();
                assertEquals("Not a valid username", userNameError1);

                Object back_obj = fragment.getView().findViewById(R.id.iv_back);
                assertEquals(ImageView.class, back_obj.getClass());
                ImageView back_img = (ImageView) back_obj;
                back_img.performClick();

            }
        });
    }

    // test controller life circle in RegisterFragment
    @Test
    public void controllersFromRegisterFragmentView() {
        ActivityController<MainActivity> mainController = Robolectric.buildActivity(MainActivity.class);
        final MainActivity activity = mainController.create().start().resume().get();
        activity.getBotm_navigation().setSelectedItemId(R.id.navigation_meeting_lists);
        FragmentScenario<RegisterFragment> fragment =
                FragmentScenario.launch(RegisterFragment.class);
        fragment.onFragment(new FragmentScenario.FragmentAction<RegisterFragment>() {
            @Override
            public void perform(@NonNull RegisterFragment fragment) {
                //fragment.getView().findViewById(R.id.scroll_courses_list_names).performClick();
                EditText etUserName=fragment.getView().findViewById(R.id.et_register_username);
                EditText etPassword=fragment.getView().findViewById(R.id.et_register_password);
                EditText etConfirmPassword=fragment.getView().findViewById(R.id.et_confirm_password);
                RadioGroup rgGender=fragment.getView().findViewById(R.id.rg_gender);
                EditText etAge=fragment.getView().findViewById(R.id.et_age);
                EditText etPhone=fragment.getView().findViewById(R.id.et_phone);
                EditText etEmail=fragment.getView().findViewById(R.id.et_email);
                Button btRegister=fragment.getView().findViewById(R.id.btn_register);
                assertTrue(etUserName.isEnabled());
                assertTrue(etPassword.isEnabled());
                assertTrue(etConfirmPassword.isEnabled());
                assertTrue(rgGender.isEnabled());
                assertTrue(etAge.isEnabled());
                assertTrue(etPhone.isEnabled());
                assertTrue(etEmail.isEnabled());
                assertTrue(btRegister.isClickable());
                assertEquals("Register", btRegister.getText().toString());

                etUserName.setText("");
                CharSequence userNameError = etUserName.getError();
                assertEquals("Not a valid username", userNameError);

                etUserName.setText("e@1212");
                CharSequence userNameError1 = etUserName.getError();
                assertEquals("Not a valid username", userNameError1);

                etUserName.setText("1111");
                etPassword.setText("11");
                CharSequence passwordError = etPassword.getError();
                assertEquals("Password must be >5 characters", passwordError);

                etPassword.setText("1111111");
                etConfirmPassword.setText("111");
                CharSequence confirmPasswordError = etConfirmPassword.getError();
                assertEquals("Confirm password is not equal with password", confirmPasswordError);

                etConfirmPassword.setText("1111111");
                etAge.setText("300");
                CharSequence ageError = etAge.getError();
                assertEquals("age range or format error", ageError);

                etAge.setText("19");
                etPhone.setText("10001");
                CharSequence phoneError = etPhone.getError();
                assertEquals("Phone number format error", phoneError);

                etPhone.setText("13297463212");
                etEmail.setText("111002");
                CharSequence emailError = etEmail.getError();
                assertEquals("Not a valid email", emailError);

                etEmail.setText("3412455432@gmail.com");
                assertTrue(btRegister.isClickable());
                btRegister.performClick();

                UserInfo userInfo=new UserInfo();
                userInfo.setDisplayName("1111");
                userInfo.setPassword("1111111");
                userInfo.setAge(19);
                userInfo.setPhone("13297463212");
                userInfo.setEmail("3412455432@gmail.com");
                fragment.setEditTextEnable(userInfo,true);

                Object back_obj = fragment.getView().findViewById(R.id.iv_back);
                assertEquals(ImageView.class, back_obj.getClass());
                ImageView back_img = (ImageView) back_obj;
                back_img.performClick();

            }
        });

        FragmentScenario<RegisterFragment> fragment1 =
                FragmentScenario.launch(RegisterFragment.class);
        fragment.onFragment(new FragmentScenario.FragmentAction<RegisterFragment>() {
            @Override
            public void perform(@NonNull RegisterFragment fragment) {
                fragment.userName = "1111";
            }
        });
    }

    @Test
    public void addition_isCorrect() { //base test for environment
        Log.i(TAG, "======== test start ======== ");
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testPhoneValid() { //base test for phone valid
        RegisterViewModel viewModel = new RegisterViewModel();
        int result = viewModel.checkPhoneValid("13297463212");
        assertEquals(0, result);
    }
}