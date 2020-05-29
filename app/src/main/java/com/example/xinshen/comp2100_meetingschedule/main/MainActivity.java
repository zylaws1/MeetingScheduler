package com.example.xinshen.comp2100_meetingschedule.main;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import android.app.AlertDialog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import androidx.multidex.MultiDex;

import com.example.xinshen.comp2100_meetingschedule.R;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.hjq.bar.style.TitleBarLightStyle;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Main activity for meeting scheduler, providing
 * context for most functional fragment.
 *
 * @author Xin Shen, Shaocong Lang
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "shenxin";
    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;
    private static final String MEETING_TABLE_NAME = "meeting_info";

    protected static AddNewMeetingFragment addNewMeetingFragment;
    protected static MeetingInfoFragment meetingInfoFragment;
    protected static SetPreferTimeslotFragment setPreferTimeslotFragment;
    protected static FragmentManager mFraManager;
    protected static MeetingModel param_model = new MeetingModel();
    public static MainActivity instance = null;
    protected static Context mContext;
    protected MeetingListFragment comingMeetingsFragment;
    private static TitleBar mTitleBar;
    private static int titile_bar_color;
    private MeetingListFragment pastMeetingsFragment;
    private SettingsFragment settingsFragment;
    private OwnProfileFragment ownProfileFragment;
    private NoteListFragment noteListFragment;
    private NoteEditFragment noteEditFragment;
    private FragmentTransaction transaction;
    private MeetingSchedulerFragment mScheduleFragment;
    private boolean chosen_coming_meetings = true;
    private OnTitleBarListener mTitleListener;
    private BottomNavigationView botm_navigation;
    private MeetingDeadlineNotification m_notification = new MeetingDeadlineNotification();
    private DatabaseReference mDatabase = null;
    private ArrayList<MeetingModel> coming_meetings_data;
    private ArrayList<MeetingModel> past_meetings_data;
    private boolean first_loading = true;

    public ArrayList<MeetingModel> getComing_meetings_data() {
        return coming_meetings_data;
    }

    public ArrayList<MeetingModel> getPast_meetings_data() {
        return past_meetings_data;
    }

    public MeetingListFragment getComingMeetingsFragment() {
        return comingMeetingsFragment;
    }

    // add listener for bottom fragment navigation bar
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id = item.getItemId();
            // only show coming and past tag when in meeting list view
            if (id == R.id.navigation_meeting_lists) {
                setmTitleBarStyle(true);
                replaceFragment(comingMeetingsFragment);
                return true;
            } else {
                setmTitleBarStyle(false);
                if (id == R.id.navigation_category) {
                    replaceFragment(mScheduleFragment);
                    return true;
                } else if (id == R.id.navigation_profile) {
                    replaceFragment(ownProfileFragment);
                    return true;
                } else if (id == R.id.navigation_settings) {
                    replaceFragment(settingsFragment);
                    return true;
                } else if (id == R.id.navigation_search) {
                    Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                    startActivity(intent);
                    return true;
                }
            }
            return false;
        }
    };

    public static Context getContext() {
        return mContext;
    }

    // Matching for different screen sizes and solutions
    private void getDevicedInfo() {
        DisplayMetrics dMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dMetrics);
        SCREEN_WIDTH = dMetrics.widthPixels;
        SCREEN_HEIGHT = dMetrics.heightPixels;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getDevicedInfo();
        mContext = getApplicationContext();
        instance = this;
        FirebaseApp.initializeApp(this);
        mDatabase = FirebaseDatabase.getInstance().getReference().child(MEETING_TABLE_NAME);
        initViews();
        ValueEventListener dataListener = new ValueEventListener() {
            // Add callback listener to get meetings data from google firebase.
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the meetings data
//                Log.i(TAG, "server data str:" + str);
                if (first_loading) {
                    Object size_coming = dataSnapshot.child("size_coming").getValue();
                    if (size_coming == null) return;
                    int size_come = Integer.parseInt(size_coming.toString());
                    Object size_past = dataSnapshot.child("size_past").getValue();
                    if (size_past == null) return;
                    int size_past_int = Integer.parseInt(size_past.toString());
                    coming_meetings_data = new ArrayList<>();
                    past_meetings_data = new ArrayList<>();
                    MeetingModel meet;
                    // add data into ListView arrays for coming meetings
                    for (int i = 0; i < size_come; i++) {
                        meet = dataSnapshot.child("coming_" + i).getValue(MeetingModel.class);
                        coming_meetings_data.add(meet);
                    }
                    // add data into ListView arrays for past meetings
                    for (int i = 0; i < size_past_int; i++) {
                        meet = dataSnapshot.child("past_" + i).getValue(MeetingModel.class);
                        past_meetings_data.add(meet);
                    }
                    Log.i(TAG, "onDataChange coming data.size():" + coming_meetings_data.size());
                    Log.i(TAG, "onDataChange past data.size():" + past_meetings_data.size());
                    Log.i(TAG, "onDataChange: recieve data at:" + System.currentTimeMillis());
                    comingMeetingsFragment.setMeetings_list(coming_meetings_data);
                    pastMeetingsFragment.setMeetings_list(past_meetings_data);
                    comingMeetingsFragment.getMeetings_list_adapter().notifyDataSetChanged();
                    // perform click to synchronize the view content;
                    //mTitleBar.getRightView().performClick();
                    first_loading = false;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        mDatabase.addValueEventListener(dataListener);
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // allow for big apk package
        MultiDex.install(base);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Check if the clipboard content is a sharing token
        TimerTask delayTask = new TimerTask() {
            @Override
            public void run() {
                Looper.prepare();
                getClipTxt();
                Looper.loop();
            }
        };
        // delay 2S for views and controllers  loading
        Timer timer = new Timer();
        timer.schedule(delayTask, 2000);
        String info_intent = getIntent().getStringExtra("Info");
        if (info_intent != null && info_intent.length() > 0) {
            Log.i(TAG, "onResume from search info:" + info_intent);
            MeetingModel m = comingMeetingsFragment.getModelByName(info_intent);
            if (info_intent != null && m == null)
                m = param_model;
            MainActivity.setmTitleBarInactive();
            MainActivity.meetingInfoFragment.setMeetingModel(m);
            FragmentTransaction transaction = MainActivity.mFraManager.beginTransaction();
            transaction.replace(R.id.main_linear, MainActivity.meetingInfoFragment);
            transaction.commit();
        }
        Log.i(TAG, "onResume: at" + System.currentTimeMillis());
        comingMeetingsFragment.getMeetings_list_adapter().notifyDataSetChanged();
    }

    // Tool method for scaling icon
//    public static Drawable zoomDrawable(Drawable drawable, int w, int h) {
//        int width = drawable.getIntrinsicWidth();
//        int height = drawable.getIntrinsicHeight();
//        Bitmap oldbmp = drawableToBitmap(drawable);
//        Matrix matrix = new Matrix();
//        float scaleWidth = ((float) w / width);
//        float scaleHeight = ((float) h / height);
//        matrix.postScale(scaleWidth, scaleHeight);
//        Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height,
//                matrix, true);
//        return new BitmapDrawable(null, newbmp);
//    }

    // Tool for transforming drawable to bitmap image
//    private static Bitmap drawableToBitmap(Drawable drawable) {
//        int width = drawable.getIntrinsicWidth();
//        int height = drawable.getIntrinsicHeight();
//        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
//                : Bitmap.Config.RGB_565;
//        Bitmap bitmap = Bitmap.createBitmap(width, height, config);
//        Canvas canvas = new Canvas(bitmap);
//        drawable.setBounds(0, 0, width, height);
//        drawable.draw(canvas);
//        return bitmap;
//    }

    // Init the homepage meeting ListView view
    private void initViews() {
        botm_navigation = (BottomNavigationView) findViewById(R.id.navigation);
        botm_navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        botm_navigation.setAlpha(1f);
        botm_navigation.setBackgroundColor(getResources().getColor(R.color.colorBottomNavigation));
        botm_navigation.setItemIconSize(90);

        // set title bar style for homepage as left and right two part
        TitleBar.initStyle(new TitleBarLightStyle());
        mTitleBar = findViewById(R.id.title_bar);
        mTitleListener = new OnTitleBarListener() {
            @Override
            public void onLeftClick(View v) {   // left part for coming ListView fragment
                setActiveCategory(true);
                replaceMeetingsFragment(true);
                chosen_coming_meetings = true;
            }

            @Override
            public void onTitleClick(View v) {  // no content in middle
                // Toast.makeText(getApplicationContext(),"shenxin",Toast.LENGTH_SHORT ).show();
            }

            //add listener to right login icon in titlebar
            @Override
            public void onRightClick(View view) {   // right part for past ListView fragment
                chosen_coming_meetings = false;
                replaceMeetingsFragment(false);
                setActiveCategory(false);
            }
        };
        initFragment();
        //mCategoryTable = (TableLayout) findViewById(R.id.selector_category_table);
        setmTitleBarStyle(true);
        titile_bar_color = getResources().getColor(R.color.colorBottomNavigation);
        // set default selected as homepage
        botm_navigation.setSelectedItemId(R.id.navigation_meeting_lists);

    }

    // Change the "Coming" and "Past" title style by mode
    void setmTitleBarStyle(boolean isHomepage) {
        mTitleBar.setVisibility(View.VISIBLE);
        if (isHomepage) {
            mTitleBar.setAlpha(0.94f);
            String space_str = "           ";
            mTitleBar.setLeftTitle(space_str + "Coming");
            mTitleBar.setLeftSize(1, 25);
            mTitleBar.setLeftColor(Color.LTGRAY);
            mTitleBar.setRightTitle("Past" + space_str + "   ");
            mTitleBar.setRightSize(1, 17);
            mTitleBar.setRightColor(Color.GRAY);
            mTitleBar.setBackgroundColor(titile_bar_color);
            mTitleBar.setOnTitleBarListener(mTitleListener);
            mTitleBar.setTitle("");
        } else {
            mTitleBar.setAlpha(1f);
            mTitleBar.setLeftTitle("");
            mTitleBar.setRightTitle("");
            mTitleBar.setTitle("    My meetings scheduler");
            mTitleBar.setTitleSize(2, 25);
            mTitleBar.setTitleColor(Color.MAGENTA);
        }
    }

    // Tool method for other activities to deactivate title bar
    public static void setmTitleBarInactive() {
        mTitleBar.setVisibility(View.VISIBLE);
        mTitleBar.setAlpha(1f);
        mTitleBar.setLeftTitle("");
        mTitleBar.setRightTitle("");
        mTitleBar.setTitle("    My meetings scheduler");
        mTitleBar.setTitleSize(2, 25);
        mTitleBar.setTitleColor(Color.MAGENTA);
    }

    protected void setmTitleBarActive() {
        mTitleBar.setAlpha(0.94f);
        String space_str = "           ";
        mTitleBar.setLeftTitle(space_str + "Coming");
        mTitleBar.setLeftSize(1, 25);
        mTitleBar.setLeftColor(Color.LTGRAY);
        mTitleBar.setRightTitle("Past" + space_str + "   ");
        mTitleBar.setRightSize(1, 17);
        mTitleBar.setRightColor(Color.GRAY);
        mTitleBar.setBackgroundColor(titile_bar_color);
        mTitleBar.setOnTitleBarListener(mTitleListener);
        mTitleBar.setTitle("");
    }

    // Tool method for other activities to deactivate title bar
    public static void setHideTitleBar() {
        mTitleBar.setVisibility(View.GONE);
    }

    // Tool method for other activities to deactivate title bar
    public void setShowTitleBar() {
        mTitleBar.setVisibility(View.VISIBLE);
        setmTitleBarStyle(false);
    }

    // change tag style with active category
    public void setActiveCategory(Boolean isLeft) {
        if (isLeft) {
            mTitleBar.setLeftSize(1, 25);
            mTitleBar.setLeftColor(Color.LTGRAY);
            mTitleBar.setRightSize(1, 17);
            mTitleBar.setRightColor(Color.GRAY);
        } else {
            mTitleBar.setLeftSize(1, 17);
            mTitleBar.setLeftColor(Color.GRAY);
            mTitleBar.setRightSize(1, 25);
            mTitleBar.setRightColor(Color.LTGRAY);
        }
    }

    // Transfer to add new meeting fragment when adding button clicked
    public void startNewMeetings(View v) {
        setmTitleBarStyle(true);
        replaceFragment(addNewMeetingFragment);
    }

    // Delete selected meetings data when delete all button clicked
    public void deleteSelectedMeetings(View v) {
        //delete coming or past meetings with mode
        if (chosen_coming_meetings) {
            Integer del_ids[] = comingMeetingsFragment.deleteSelectedMeetings(v);
            Log.i(TAG, "mList size:" + mScheduleFragment.mList.size());
            // Loop to delete all selected meeting items in reverse order
            for (int i = del_ids.length - 1; i >= 0; i--) {
                Log.i(TAG, "deleteSelectedMeetings: id:" + del_ids[i]);
                if (del_ids[i] < mScheduleFragment.mList.size())
                    mScheduleFragment.mList.remove((int) del_ids[i]);
            }
            // Call to refresh timetable view if under scheduler fragment
            if (mScheduleFragment.mTimaTableView != null)
                mScheduleFragment.mTimaTableView.refreshTable();
            else Log.i(TAG, "mTimaTableView null");
        } else
            pastMeetingsFragment.deleteSelectedMeetings(v);
    }

    // Back to homepage if cancel adding
    public void cancelAdd(View v) {
        setmTitleBarStyle(true);
        botm_navigation.setSelectedItemId(R.id.navigation_meeting_lists);
        replaceFragment(comingMeetingsFragment);
    }

    // Submit adding when add button confirmed
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void addMeeting(View v) {
        Toast.makeText(MainActivity.this, "meeting added!", Toast.LENGTH_LONG).show();
        // Get the data from input
        int date = addNewMeetingFragment.date - 1;
        if (date == 0) date = 7;
        int hour = addNewMeetingFragment.hour;
        String name = addNewMeetingFragment.name.getText().toString();
        String room = addNewMeetingFragment.room.getText().toString();
        String description = addNewMeetingFragment.description.getText().toString();
        String venue = addNewMeetingFragment.venue.getText().toString();
        String date_str = addNewMeetingFragment.date_str;
        String time_str = addNewMeetingFragment.time_str;

        // create a meeting model with input data
        param_model = new MeetingModel(
                name, room, venue, description, date, date_str, time_str, hour, addNewMeetingFragment.minute
        );
        // calculate the remain time to meeting deadline in ms
        long res_time = param_model.getTimeRemain() - 60 * 1000 * addNewMeetingFragment.remind_time_advance;
        Log.i(TAG, "addMeeting remain time: " + res_time);
        // add meeting in data array if the meeting time is valid
        if (res_time > 0) {
            comingMeetingsFragment.meetings_list.add(param_model);
            m_notification.startNoti(res_time, "at "
                    + param_model.getStart_time_str() + " have meeting:" + param_model.getName(), param_model.getName());
        } else {    // Toast if the time is invalid
            Toast.makeText(getApplicationContext(), "Bad meeting time", Toast.LENGTH_LONG);
        }
        // back to homepage fragment
        setmTitleBarStyle(true);
        replaceFragment(comingMeetingsFragment);
    }

    // Back to homepage ListView fragment
    public void exitMeetInfo(View v) {
        botm_navigation.setSelectedItemId(R.id.navigation_meeting_lists);
        setmTitleBarStyle(true);
        replaceFragment(comingMeetingsFragment);
    }

    // Share the meeting by clipboard token when share button clicked
    public void shareMeeting(View v) {
        Toast.makeText(MainActivity.this, "meeting info copied to clipboard!", Toast.LENGTH_LONG).show();
        // Get data from sharing meeting
        String name = param_model.getName();
        String room = param_model.getRoom();
        String description = param_model.getDescription();
        String venue = param_model.getVenue();
        String date_str = param_model.getDate_str();
        String time_str = param_model.getStart_time_str();
        int date = param_model.getDay();
        if (date == 0) date = 7;
        int hour = param_model.getStar_hour();
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        //  Create ClipData object as sharing token
        ClipData mClipData = ClipData.newPlainText("token", "&_meetS:" + name + "," + room + ","
                + description + "," + venue + "," + date_str + "," + time_str + "," + date + "," + hour);
        // Put ClipData content in the system clipboard
        cm.setPrimaryClip(mClipData);
        setmTitleBarStyle(true);
        replaceFragment(comingMeetingsFragment);
    }

    // Get the content from system clipboard
    public void getClipTxt() {
        ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(CLIPBOARD_SERVICE);
        // Return when there is no data
        if (!clipboard.hasPrimaryClip()) {
            return;
        }
        // If content is plain text which maybe a sharing token
        if (clipboard.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
            ClipData cdText = clipboard.getPrimaryClip();
            ClipData.Item item = cdText.getItemAt(0);
            if (item.getText() != null) {
                // get the content of plain text
                final String str = item.getText().toString();
//                Log.i(TAG, "getClipTxt: " + str);
                if (str.length() > 8 && str.substring(0, 8).equals("&_meetS:")) {
                    // Alert whether to add a meeting if the content is a sharing token.
                    final AlertDialog alert_add = new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Add a new meeting with shared token?")
                            .setPositiveButton("Yes & Add!", new DialogInterface.OnClickListener() {
                                // add for "Yes & Add!" button
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    String data = str.substring(8);
                                    Log.i(TAG, "add share bundle data" + data);
                                    FragmentTransaction transaction = MainActivity.mFraManager.beginTransaction();
                                    transaction.replace(R.id.main_linear, MainActivity.addNewMeetingFragment);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("item", data);
                                    // use bundle to init input meeting in adding fragment
                                    addNewMeetingFragment.setArguments(bundle);
                                    transaction.commit();
                                }
                            })

                            .setNegativeButton("later", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // cancel if do not want to add
                                    return;
                                }
                            })
                            .create();
                    alert_add.show();
                }
            }
        }
    }

    @Override
    protected void onStop() {
        // send the meetings data array to server for saving if the life circle is in danger
        saveMeetingsOnServer();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    // Save the meetings data on server
    public void saveMeetingsOnServer() {

        int size_coming_meets = comingMeetingsFragment.meetings_list.size();
        int size_past_meets;
        if (pastMeetingsFragment.meetings_list != null)
            size_past_meets = pastMeetingsFragment.meetings_list.size();
        else
            size_past_meets = 0;
        Log.i(TAG, "saveMeetingsOnServer: " + size_coming_meets);
        mDatabase.child("size_coming").setValue(size_coming_meets);
        mDatabase.child("size_past").setValue(size_past_meets);
        for (int i = 0; i < size_coming_meets; i++) {
            mDatabase.child("coming_" + i).setValue(comingMeetingsFragment.meetings_list.get(i));
        }
        for (int i = 0; i < size_past_meets; i++) {
            mDatabase.child("past_" + i).setValue(pastMeetingsFragment.meetings_list.get(i));
        }
        mDatabase.child("get_data").setValue("");
        Log.i(TAG, "saved Meetings OnServer");
    }

    // Randomly update a node to trigger the callback to get data from server
    private ArrayList<MeetingModel> load_data_from_firebase() {
        ArrayList<MeetingModel> data = new ArrayList<>();
        mDatabase.child("get_data").setValue(Math.random());
        return data;
    }

    // Initialize the fragments in main activity
    private void initFragment() {
        load_data_from_firebase();
        comingMeetingsFragment = new MeetingListFragment();
        pastMeetingsFragment = new MeetingListFragment(true);
        settingsFragment = new SettingsFragment();
        ownProfileFragment = new OwnProfileFragment();
        noteListFragment = new NoteListFragment();
        noteEditFragment = new NoteEditFragment();
        addNewMeetingFragment = new AddNewMeetingFragment();
        mScheduleFragment = new MeetingSchedulerFragment();
        mScheduleFragment.setMeetings_list(comingMeetingsFragment.meetings_list);
        meetingInfoFragment = new MeetingInfoFragment();
        setPreferTimeslotFragment = new SetPreferTimeslotFragment();
        setDefaultFragment();
    }

    // set default fragment as meeting ListView
    private void setDefaultFragment() {
        mFraManager = getSupportFragmentManager();
        transaction = mFraManager.beginTransaction();
        transaction.replace(R.id.main_linear, comingMeetingsFragment);
        transaction.commit();
    }

    // Tool method for transfer between fragments
    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = mFraManager.beginTransaction();
        transaction.replace(R.id.main_linear, fragment);
        transaction.commit();
    }

    // Tool method for transfer between coming and past meeting ListView
    public void replaceMeetingsFragment(Boolean ChooseComing) {
        FragmentTransaction transaction = mFraManager.beginTransaction();
        if (ChooseComing)
            transaction.replace(R.id.main_linear, comingMeetingsFragment);
        else
            transaction.replace(R.id.main_linear, pastMeetingsFragment);
        transaction.commit();
    }

    // private variables getters and setters:
    public OwnProfileFragment getOwnProfileFragment() {
        return ownProfileFragment;
    }

    public BottomNavigationView getBotm_navigation() {
        return botm_navigation;
    }

    public NoteListFragment getNoteListFragment() {
        return noteListFragment;
    }

    public NoteEditFragment getNoteEditFragment() {
        return noteEditFragment;
    }

    public static TitleBar getmTitleBar() {
        return mTitleBar;
    }

    //for test
    public static void setMeetingModel(MeetingModel model) {
        param_model = model;
    }


}