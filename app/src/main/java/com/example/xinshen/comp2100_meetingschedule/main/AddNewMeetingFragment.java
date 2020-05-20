package com.example.xinshen.comp2100_meetingschedule.main;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.example.xinshen.comp2100_meetingschedule.R;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddNewMeetingFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "shenxin";
    private TextView txtDate;
    private TextView txtTime;
    private Button btnDate;
    private Button btnTime;
    private Button btnAddMeeting;
    public static int date;
    public static int hour;
    public static int minute;
    public static String date_str;
    public static String time_str;
    public EditText name;
    public EditText room;
    public EditText venue;
    public EditText description;

    Calendar my_calendar = Calendar.getInstance(Locale.ENGLISH);

    AddNewMeetingFragment() {
        super();
//        Log.i(TAG, "init AddNewMeetingFragment");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_new_meeting, null);
        btnDate = (Button) view.findViewById(R.id.btn_Date);
        btnTime = (Button) view.findViewById(R.id.btn_Time);
        txtDate = (TextView) view.findViewById(R.id.txtDate);
        txtTime = (TextView) view.findViewById(R.id.txtTime);
        name = (EditText) view.findViewById(R.id.edit_txt_name);
        room = (EditText) view.findViewById(R.id.edit_txt_room);
        venue = (EditText) view.findViewById(R.id.edit_txt_venue);
        description = (EditText) view.findViewById(R.id.edit_txt_description);
//        btnAddMeeting =  (TextView) view.findViewById(R.id.btn_submit_add);
        btnDate.setOnClickListener(this);
        btnTime.setOnClickListener(this);
        return view;
    }

    public static void showDatePickerDialog(Activity activity, int themeResId, final TextView tv, Calendar calendar) {
       // calendar.set(2020, 05, 20);
        // 直接创建一个DatePickerDialog对话框实例，并将它显示出来
        new DatePickerDialog(activity, themeResId, new DatePickerDialog.OnDateSetListener() {
            // 绑定监听器(How the parent is notified that the date is set.)
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Log.i(TAG, "monthOfYear: " + monthOfYear);
                monthOfYear++;
                date_str = "" + year + "/" + monthOfYear + "/" + dayOfMonth;
                tv.setText("Date:" + date_str);
                if (monthOfYear < 10) {
                    date = dateToWeek(year + "-0" + monthOfYear + "-" + dayOfMonth);
                } else {
                    date = dateToWeek(year + "-" + monthOfYear + "-" + dayOfMonth);
                }
            }
        }
                // 设置初始日期
                , calendar.get(Calendar.YEAR)
                , calendar.get(Calendar.MONTH)
                , calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    public static int dateToWeek(String datetime) {
        Log.i(TAG, "dateToWeek: " + datetime);
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance(); // 获得一个日历
        Date datet = null;
        try {
            datet = f.parse(datetime);
            cal.setTime(datet);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int w = cal.get(Calendar.DAY_OF_WEEK); // 指示一个星期中的某天。
        if (w < 0)
            w = 0;
        return w;
    }

    /**
     * 时间选择
     *
     * @param activity
     * @param themeResId
     * @param tv
     * @param calendar
     */
    public static void showTimePickerDialog(Activity activity, int themeResId, final TextView tv, Calendar calendar) {
        // Calendar c = Calendar.getInstance();
        // 创建一个TimePickerDialog实例，并把它显示出来
        // 解释一哈，Activity是context的子类
        TimePickerDialog time_dialog = new TimePickerDialog(activity, themeResId,
                // 绑定监听器
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minuteC) {
                        time_str = hourOfDay + ":" + minuteC;
                        tv.setText("Time:" + hourOfDay + ":" + minuteC);
                        hour = hourOfDay;
                        minute = minuteC;
                    }
                }
                // 设置初始时间
                , calendar.get(Calendar.HOUR_OF_DAY)
                , calendar.get(Calendar.MINUTE)
                // true表示采用24小时制
                , true);
        time_dialog.show();
    }

    @Override
    public void onClick(View view) {
        int themeResid = 4;
//        Log.i(TAG, "add Frag onClick: " + view.getId());
        switch (view.getId()) {
            case R.id.btn_Date:
                showDatePickerDialog(getActivity(), themeResid, txtDate, my_calendar);
                break;
            case R.id.btn_Time:
                showTimePickerDialog(getActivity(), themeResid, txtTime, my_calendar);
                break;
//            case R.id.btn_submit_add:
//                showTimePickerDialog(getActivity(), themeResid, txtTime, my_calendar);
//                break;
            default:
                break;
        }
    }
}
