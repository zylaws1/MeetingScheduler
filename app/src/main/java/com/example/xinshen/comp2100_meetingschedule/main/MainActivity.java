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
import android.widget.RadioGroup;
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
import com.google.gson.Gson;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.hjq.bar.style.TitleBarLightStyle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "shenxin";
    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;
    private static TitleBar mTitleBar;
    protected MeetingListFragment comingMeetingsFragment;
    private MeetingListFragment pastMeetingsFragment;
    private SettingsFragment settingsFragment;
    protected static AddNewMeetingFragment addNewMeetingFragment;
    protected static MeetingInfoFragment meetingInfoFragment;
    protected static SetPreferTimeslotFragment setPreferTimeslotFragment;
    private OwnProfileFragment ownProfileFragment;
    private FragmentTransaction transaction;
    private MeetingSchedulerFragment mScheduleFragment;
    private boolean chosen_coming_meetings = false;
    private OnTitleBarListener mTitleListener;
    private BottomNavigationView botm_navigation;
    private static int titile_bar_color;
    protected static FragmentManager mFraManager;
    protected static MeetingModel param_model = new MeetingModel();
    protected static MainActivity instance = null;
    protected static Context mContext;

    private MeetingDeadlineNotification m_notification = new MeetingDeadlineNotification();
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private ArrayList<MeetingModel> coming_meetings_data;
    private ArrayList<MeetingModel> past_meetings_data;


    public ArrayList<MeetingModel> getComing_meetings_data() {
        return coming_meetings_data;
    }

    public ArrayList<MeetingModel> getPast_meetings_data() {
        return past_meetings_data;
    }

    public MeetingListFragment getComingMeetingsFragment() {
        return comingMeetingsFragment;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id = item.getItemId();


            if (id == R.id.navigation_meeting_lists) {
                setmTitleBarStyle(true);
                replaceFragment(comingMeetingsFragment);
                return true;
            } else {
                setmTitleBarStyle(false);
                if (id == R.id.navigation_category) {
                    //replaceFragment(ownMeetingFragment);
                    //replaceFragment(weekScheduleFragment);
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
        initViews();
//        FirebaseApp.initializeApp(this);
        ValueEventListener dataListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the meetings data
//                Log.i(TAG, "server data str:" + str);
                Object size_coming = dataSnapshot.child("size_coming").getValue();
                if (size_coming == null) return;
                int size_come = Integer.parseInt(size_coming.toString());
                Object size_past = dataSnapshot.child("size_past").getValue();
                if (size_past == null) return;
                int size_past_int = Integer.parseInt(size_past.toString());
                coming_meetings_data = new ArrayList<>();
                past_meetings_data = new ArrayList<>();
                MeetingModel meet;
                for (int i = 0; i < size_come; i++) {
                    meet = dataSnapshot.child("coming_" + i).getValue(MeetingModel.class);
                    coming_meetings_data.add(meet);
                }
                for (int i = 0; i < size_past_int; i++) {
                    meet = dataSnapshot.child("past_" + i).getValue(MeetingModel.class);
                    past_meetings_data.add(meet);
                }
                Log.i(TAG, "onDataChange past_meetings_data.size():" + past_meetings_data.size());
                Log.i(TAG, "onDataChange: recieve data at:" + System.currentTimeMillis());
                comingMeetingsFragment.setMeetings_list(coming_meetings_data);
                pastMeetingsFragment.setMeetings_list(past_meetings_data);
                mTitleBar.getRightView().performClick();
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
        MultiDex.install(base);
    }

    @Override
    protected void onResume() {
        super.onResume();
        TimerTask delayTask = new TimerTask() {
            @Override
            public void run() {
                Looper.prepare();
                getClipTxt();
                Looper.loop();
            }
        };
        Timer timer = new Timer();
        timer.schedule(delayTask, 2000);
        String info_intent = getIntent().getStringExtra("Info");
        if (info_intent != null && info_intent.length() > 0) {
            Log.i(TAG, "onResume got search info:" + info_intent);
            MeetingModel m = comingMeetingsFragment.getModelByName(info_intent);
            MainActivity.setmTitleBarInactive();
            MainActivity.meetingInfoFragment.setMeetingModel(m);
            FragmentTransaction transaction = MainActivity.mFraManager.beginTransaction();
            transaction.replace(R.id.main_linear, MainActivity.meetingInfoFragment);
            transaction.commit();
        }
    }

    public static Drawable zoomDrawable(Drawable drawable, int w, int h) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap oldbmp = drawableToBitmap(drawable);
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) w / width);
        float scaleHeight = ((float) h / height);
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height,
                matrix, true);
        return new BitmapDrawable(null, newbmp);
    }

    private static Bitmap drawableToBitmap(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(width, height, config);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;
    }

    private void initViews() {
        botm_navigation = (BottomNavigationView) findViewById(R.id.navigation);
        botm_navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        botm_navigation.setAlpha(1f);
        botm_navigation.setBackgroundColor(getResources().getColor(R.color.colorBottomNavigation));
        botm_navigation.setItemIconSize(90);

        TitleBar.initStyle(new TitleBarLightStyle());
        mTitleBar = findViewById(R.id.title_bar);
        mTitleListener = new OnTitleBarListener() {
            @Override
            public void onLeftClick(View v) {
                setActiveCategory(true);
                replaceMeetingsFragment(true);
                chosen_coming_meetings = true;
            }

            @Override
            public void onTitleClick(View v) {
                // Toast.makeText(getApplicationContext(),"shenxin",Toast.LENGTH_SHORT ).show();
            }

            //add listener to right login icon in titlebar
            @Override
            public void onRightClick(View view) {
                chosen_coming_meetings = false;
                replaceMeetingsFragment(false);
                setActiveCategory(false);
            }
        };
        initFragment();
        //mCategoryTable = (TableLayout) findViewById(R.id.selector_category_table);
        setmTitleBarStyle(true);
        titile_bar_color = getResources().getColor(R.color.colorBottomNavigation);
        botm_navigation.setSelectedItemId(R.id.navigation_meeting_lists);

    }

    void setmTitleBarStyle(boolean isHomepage) {
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

    static void setmTitleBarInactive() {
        mTitleBar.setAlpha(1f);
        mTitleBar.setLeftTitle("");
        mTitleBar.setRightTitle("");
        mTitleBar.setTitle("    My meetings scheduler");
        mTitleBar.setTitleSize(2, 25);
        mTitleBar.setTitleColor(Color.MAGENTA);
    }


    // change tag style with active category
    void setActiveCategory(Boolean isLeft) {
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

    public void saveEditOwnUserDescription(View v) {
        Toast.makeText(MainActivity.this, "Description saved!", Toast.LENGTH_SHORT).show();

        setmTitleBarStyle(false);
        replaceFragment(ownProfileFragment);
    }

    public void setUpNewMeetings(View v) {
        replaceFragment(addNewMeetingFragment);
    }


    public void startNewMeetings(View v) {
        setmTitleBarStyle(true);
        replaceFragment(addNewMeetingFragment);
    }

    public void deleteSelectedMeetings(View v) {

        if (chosen_coming_meetings) {
            Integer del_ids[] = comingMeetingsFragment.deleteSelectedMeetings(v);
            Log.i(TAG, "mList size:" + mScheduleFragment.mList.size());
            for (int i = del_ids.length - 1; i >= 0; i--) {
                Log.i(TAG, "deleteSelectedMeetings: id:" + del_ids[i]);
                if (del_ids[i] < mScheduleFragment.mList.size())
                    mScheduleFragment.mList.remove((int) del_ids[i]);
            }
            if (mScheduleFragment.mTimaTableView != null)
                mScheduleFragment.mTimaTableView.refreshTable();
            else Log.i(TAG, "mTimaTableView null");
        } else
            pastMeetingsFragment.deleteSelectedMeetings(v);
    }

    public void editClassProfileDescription(View v) {
        setmTitleBarStyle(false);
        replaceFragment(setPreferTimeslotFragment);
    }

    public void cancelAdd(View v) {
        setmTitleBarStyle(true);
        botm_navigation.setSelectedItemId(R.id.navigation_meeting_lists);
        replaceFragment(comingMeetingsFragment);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void addMeeting(View v) {
        Toast.makeText(MainActivity.this, "meeting added!", Toast.LENGTH_LONG).show();
        String name = addNewMeetingFragment.name.getText().toString();
        String room = addNewMeetingFragment.room.getText().toString();
        String description = addNewMeetingFragment.description.getText().toString();
        String venue = addNewMeetingFragment.venue.getText().toString();
        String date_str = addNewMeetingFragment.date_str;
        String time_str = addNewMeetingFragment.time_str;
        int date = addNewMeetingFragment.date - 1;
        if (date == 0) date = 7;
        int hour = addNewMeetingFragment.hour;
        Log.i(TAG, "addMeeting: date:" + date + " " + hour);
        param_model = new MeetingModel(
                name, room, venue, description, date, date_str, time_str, hour, addNewMeetingFragment.minute
        );
        long res_time = param_model.getTimeRemain() - 60 * 1000 * addNewMeetingFragment.remind_time_advance;
        Log.i(TAG, "addMeeting remain time: " + res_time);
        if (res_time > 0) {
            comingMeetingsFragment.meetings_list.add(param_model);
            m_notification.startNoti(res_time, "at "
                    + param_model.getStart_time_str() + " have meeting:" + param_model.getName(), param_model.getName());
        } else {
            Toast.makeText(getApplicationContext(), "Bad meeting time", Toast.LENGTH_LONG);
        }
        setmTitleBarStyle(true);
        replaceFragment(comingMeetingsFragment);
    }

    public void exitMeetInfo(View v) {
        botm_navigation.setSelectedItemId(R.id.navigation_meeting_lists);
        setmTitleBarStyle(true);
        replaceFragment(comingMeetingsFragment);
    }

    public void shareMeeting(View v) {
        Toast.makeText(MainActivity.this, "meeting info copied to clipboard!", Toast.LENGTH_LONG).show();
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
        // 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("token", "&_meetS:" + name + "," + room + ","
                + description + "," + venue + "," + date_str + "," + time_str + "," + date + "," + hour);
        // 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(mClipData);
        setmTitleBarStyle(true);
        replaceFragment(comingMeetingsFragment);
    }

    public void getClipTxt() {
        ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(CLIPBOARD_SERVICE);
        //无数据时直接返回
        if (!clipboard.hasPrimaryClip()) {
            return;
        }
        //如果是文本信息
        if (clipboard.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
            ClipData cdText = clipboard.getPrimaryClip();
            ClipData.Item item = cdText.getItemAt(0);
            //此处是TEXT文本信息
            if (item.getText() != null) {
                final String str = item.getText().toString();
//                Log.i(TAG, "getClipTxt: " + str);
                if (str.length() > 8 && str.substring(0, 8).equals("&_meetS:")) {
                    final AlertDialog alert_add = new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Add a new meeting with shared token?")
                            .setPositiveButton("Yes & Add!", new DialogInterface.OnClickListener() {//添加"Yes"按钮
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    String data = str.substring(8);
                                    Log.i(TAG, "add share bundle data" + data);
                                    FragmentTransaction transaction = MainActivity.mFraManager.beginTransaction();
                                    transaction.replace(R.id.main_linear, MainActivity.addNewMeetingFragment);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("item", data);
                                    addNewMeetingFragment.setArguments(bundle);
                                    transaction.commit();
                                }
                            })

                            .setNegativeButton("later", new DialogInterface.OnClickListener() {//添加取消
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
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
        super.onStop();
        saveMeetingsOnServer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void saveMeetingsOnServer() {
        int size_coming_meets = comingMeetingsFragment.meetings_list.size();
        int size_past_meets = pastMeetingsFragment.meetings_list.size();
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
    }

    private ArrayList<MeetingModel> load_data_from_firebase() {
        ArrayList<MeetingModel> data = new ArrayList<>();
        mDatabase.child("get_data").setValue(Math.random());
        return data;
    }

    private void initFragment() {
        load_data_from_firebase();
        comingMeetingsFragment = new MeetingListFragment();
        pastMeetingsFragment = new MeetingListFragment(true);
        settingsFragment = new SettingsFragment();
        ownProfileFragment = new OwnProfileFragment();
        addNewMeetingFragment = new AddNewMeetingFragment();
        mScheduleFragment = new MeetingSchedulerFragment(comingMeetingsFragment.meetings_list);
        meetingInfoFragment = new MeetingInfoFragment();
        setPreferTimeslotFragment = new SetPreferTimeslotFragment();
        setDefaultFragment();
    }

    private void setDefaultFragment() {
        mFraManager = getSupportFragmentManager();
        transaction = mFraManager.beginTransaction();
        transaction.replace(R.id.main_linear, comingMeetingsFragment);
        transaction.commit();
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = mFraManager.beginTransaction();
        transaction.replace(R.id.main_linear, fragment);
        transaction.commit();
    }

    private void replaceMeetingsFragment(Boolean ChooseComing) {
        FragmentTransaction transaction = mFraManager.beginTransaction();
        if (ChooseComing) {
            transaction.replace(R.id.main_linear, comingMeetingsFragment);
        } else {
            transaction.replace(R.id.main_linear, pastMeetingsFragment);
        }
        transaction.commit();
    }

}