package com.example.xinshen.comp2100_meetingschedule.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
//import android.support.design.widget.BottomNavigationView;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentTransaction;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import androidx.multidex.MultiDex;

import com.example.xinshen.comp2100_meetingschedule.R;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.hjq.bar.style.TitleBarLightStyle;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "shenxin";
    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;
    private TitleBar mTitleBar;
    //    private ImageView bgImgView;
//  private Cursor cursor;
//    private DatabaseHelper mDbHelper;
//    public ContextManager mCtxManager;
//    private TableLayout mCategoryTable;
    private MeetingListFragmentActivity ComingMeetingsFragment;
    private MeetingListFragmentActivity PastMeetingsFragment;
    private SettingsFragment settingsFragment;
    private PostStatusFragment postStatusFragement;
    private OtherProfileFragment otherProfileFragment;
    private AddNewMeetingFragment addNewMeetingFragment;
    private OwnProfileFragment ownProfileFragment;
    private EditOwnMeetingProfileFragment editOwnMeetingProfileFragment;
    private EditOwnUserProfileFragment editOwnUserProfileFragment;

    private RadioGroup main_radiogroup;
    private FragmentTransaction transaction;
    private FragmentManager mFraManager;

    private WeekScheduleFragment weekScheduleFragment;
    private MeetingSchedulerFragment mScheduleFragment;
    private boolean chosen_coming_meetings = true;
    private OnTitleBarListener mTitleListener;
//    private boolean first_loaded = true;
//    private int postTarget = 0;
//    private static final int SELECT_PIC_BY_PICK_PHOTO = 2;
//    private Button postStatusSaveBtn;
//    private AlertDialog selfdialog;
//    private String usernamestr;
//    private String passwordstr;
//    private ProgressDialog progressdialog;
//    public static boolean hasLogged = false;
//    private Thread mDbReadInThread;
//    private Thread mLoginStatusThread;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id = item.getItemId();


            if (id == R.id.navigation_meeting_lists) {
                setmTitleBarStyle(true);
                replaceFragment(ComingMeetingsFragment);
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
//
//    private void showPostDialog() {
//        AlertDialog.Builder postBuilder = new AlertDialog.Builder(this);
//        postBuilder.setTitle(R.string.new_post_question);
//        postBuilder.setIcon(android.R.drawable.ic_media_play);
//        postBuilder.setSingleChoiceItems(
//                new String[]{"Image", "Video", "Status", "Album"}, 0,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        postTarget = which;
//                        //System.out.println("target = " + postTarget);
//                    }
//                });
//        postBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int i) {
//                dialog.dismiss();
//            }
//        });
//        postBuilder.setPositiveButton("Post", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int i) {
//                //System.out.println("zyl postTarget = " + postTarget);
//                if (postTarget == 0 || postTarget == 1 || postTarget == 3) {
//                    pickPhoto();
//                } else {
//                    postStatus();
//                }
//            }
//        });
//        AlertDialog dialog = postBuilder.create();
//        dialog.show();
//    }
//

    private void postStatus() {
        replaceFragment(postStatusFragement);
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
//        mCtxManager = ContextManager.getInstance();
//        //Create public class single instance in context and save them in ContextManager
//        mCtxManager.createInstances(getApplicationContext());
//        //mCtxManager.initInstances(getApplicationContext());
//        mDbHelper = mCtxManager.getDbHelper();
//        mDbReadInThread = new Thread(new Runnable() {is
//            @Override
//            public void run() {
//                // Power.get_zysj_gpio_value(4);
//                mCtxManager.readRegisteredDatas();
//            }
//        });
//
//        mLoginStatusThread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(2100);
//                    if (!hasLogged) {
//                        Log.d(TAG, "ready to loop uiThread");
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                logOnEvent(getCurrentFocus());
//                            }
//                        });
//                    }
//                } catch (Exception e) {
//                    e.getStackTrace();
//                }
//            }
//        });
//        mDbReadInThread.start();
//        mLoginStatusThread.start();
        initViews();
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    @Override
    protected void onResume() {
        super.onResume();
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
        initFragment();
        BottomNavigationView botm_navigation = (BottomNavigationView) findViewById(R.id.navigation);
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
        //mCategoryTable = (TableLayout) findViewById(R.id.selector_category_table);
        initFragment();
        botm_navigation.setSelectedItemId(R.id.navigation_meeting_lists);
        //postStatusSaveBtn=new Button(new );

        setmTitleBarStyle(true);
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
            mTitleBar.setBackgroundColor(getResources().getColor(R.color.colorBottomNavigation));
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

    private void logOnEvent(View view) {
//        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
//        view = inflater.inflate(R.layout.login, null);
//
//        final EditText username = (EditText) view.findViewById(R.id.txt_username);
//        final EditText password = (EditText) view.findViewById(R.id.txt_password);
//        username.setText("username");
//        password.setText("password");
//        AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this);
//        ad.setView(view);
//        ad.setTitle("Lon in");
//        selfdialog = ad.create();
//        selfdialog.setButton(DialogInterface.BUTTON_POSITIVE, "Log in", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                usernamestr = username.getText().toString();
//                passwordstr = password.getText().toString();
//                //mock logging in
//                progressdialog = ProgressDialog.show(MainActivity.this, "please waiting...", "Logging in...");
//                refreshHandler.sleep(999);
//                //dialog.cancel();
//            }
//        });
//        selfdialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                selfdialog.cancel();
//            }
//        });
//        selfdialog.show();
    }

    private RefreshHandler refreshHandler = new RefreshHandler();

    //handler
    class RefreshHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
//            Log.d(TAG, "handleMessage: " + msg.what);
//            try {
//                // String host ="/anndroiduser.do?user=login";
//
//                // Bundle flag = UserDataServiceHelper.login(uri, usernamestr, passwordstr);
//
//                if (usernamestr.equals("username") && passwordstr.equals("password")) {
//                    Toast.makeText(MainActivity.this, "Log in successful", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
//                    // intent.putExtras(flag);
//                    MainActivity.this.startActivity(intent);
//                    hasLogged = true;
//                } else {
//                    Toast.makeText(MainActivity.this, "Log in fails: wrong password", Toast.LENGTH_SHORT).show();
//                    //view.findViewById(R.id.txt_loginerror).setVisibility(View.VISIBLE);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            } finally {
//                progressdialog.dismiss();
//            }
        }

        public void sleep(long delayMillis) {
            this.removeMessages(0);
            sendMessageDelayed(obtainMessage(0), delayMillis);
        }

    }

    public void savePostStatus(View v) {
        //first_loaded = true;
        Toast.makeText(MainActivity.this, "Status saved!", Toast.LENGTH_SHORT).show();
        setmTitleBarStyle(false);
        replaceFragment(ComingMeetingsFragment);
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
            Integer del_ids[] = ComingMeetingsFragment.deleteSelectedMeetings(v);
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
            PastMeetingsFragment.deleteSelectedMeetings(v);
    }

    public void editClassProfileDescription(View v) {
        setmTitleBarStyle(false);
        replaceFragment(editOwnMeetingProfileFragment);
    }

    public void editUserProfileDescription(View v) {

        setmTitleBarStyle(false);
        replaceFragment(editOwnUserProfileFragment);
    }

    public void cancelAdd(View v) {
        setmTitleBarStyle(true);
        replaceFragment(ComingMeetingsFragment);
    }

    public void addMeeting(View v) {
        Toast.makeText(MainActivity.this, "meeting added!", Toast.LENGTH_LONG).show();
        String name = addNewMeetingFragment.name.getText().toString();
        String room = addNewMeetingFragment.room.getText().toString();
        String description = addNewMeetingFragment.description.getText().toString();
        String venue = addNewMeetingFragment.venue.getText().toString();
        int date = addNewMeetingFragment.date - 1;
        if (date == 0) date = 7;
        int hour = addNewMeetingFragment.hour;
        Log.i(TAG, "addMeeting: date:" + date + " " + hour);
        ComingMeetingsFragment.meetings_list.add(new MeetingModel(name, room,
                venue, description, date, hour, addNewMeetingFragment.minute));
        mScheduleFragment.mList.add(new MeetingModel(0, hour - 8, hour - 8 + 1, date,
                hour + ":00", (hour + 1) + ":00", name,
                description, room, venue));
        if (mScheduleFragment.mTimaTableView != null)
            mScheduleFragment.mTimaTableView.refreshTable();
        setmTitleBarStyle(true);
        replaceFragment(ComingMeetingsFragment);
    }

//    public void chooseDate(View v) {
//        addNewMeetingFragment.showPicker(this, v);
//    }
//
//    public void chooseTime(View v) {
//        addNewMeetingFragment.showPicker(this, v);
//    }


    //
    private void initFragment() {

        ComingMeetingsFragment = new MeetingListFragmentActivity();
        PastMeetingsFragment = new MeetingListFragmentActivity(new ArrayList<MeetingModel>());
        settingsFragment = new SettingsFragment();
        postStatusFragement = new PostStatusFragment();
        otherProfileFragment = new OtherProfileFragment();
        ownProfileFragment = new OwnProfileFragment();
        addNewMeetingFragment = new AddNewMeetingFragment();
        addNewMeetingFragment = new AddNewMeetingFragment();
        weekScheduleFragment = new WeekScheduleFragment();
        mScheduleFragment = new MeetingSchedulerFragment();
        setDefaultFragment();
    }

    private void setDefaultFragment() {
        mFraManager = getSupportFragmentManager();
        transaction = mFraManager.beginTransaction();
        transaction.replace(R.id.main_linear, ComingMeetingsFragment);
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
            transaction.replace(R.id.main_linear, ComingMeetingsFragment);
        } else {
            transaction.replace(R.id.main_linear, PastMeetingsFragment);
        }
        transaction.commit();
    }

}