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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

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
    private Spinner mSpinner;
    private String[] spinnerItems = {"10:00", "14:00", "18:00"};
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
        mSpinner = (Spinner) view.findViewById(R.id.spinner_add);
        btnDate.setOnClickListener(this);
        btnTime.setOnClickListener(this);
        ChangeSpinner(view);
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

    public void ChangeSpinner(View v) {
        mSpinner.setDropDownWidth(400); //下拉宽度
        mSpinner.setDropDownHorizontalOffset(100); //下拉的横向偏移
        mSpinner.setDropDownVerticalOffset(100); //下拉的纵向偏移
        //mSpinner.setBackgroundColor(AppUtil.getColor(instance,R.color.wx_bg_gray)); //下拉的背景色
        //spinner mode ： dropdown or dialog , just edit in layout xml
        //mSpinner.setPrompt("Spinner Title"); //弹出框标题，在dialog下有效

        //自定义选择填充后的字体样式
        //只能是textview样式，否则报错：ArrayAdapter requires the resource ID to be a TextView
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(),
                R.layout.spinner_select, spinnerItems);
        //自定义下拉的字体样式
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_drop);
        //这个在不同的Theme下，显示的效果是不同的
        //spinnerAdapter.setDropDownViewTheme(Theme.LIGHT);
        mSpinner.setAdapter(spinnerAdapter);

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //parent就是父控件spinner
            //view就是spinner内填充的textview,id=@android:id/text1
            //position是值所在数组的位置
            //id是值所在行的位置，一般来说与positin一致
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                time_str = spinnerItems[(int) id];
                String hour_str = time_str.substring(0, time_str.indexOf(":"));
                hour = Integer.parseInt(hour_str);
                txtTime.setText("Time:" + time_str);
//                Log.i("shenxin","onItemSelected : parent.id=" + parent.getId() +
//                        ",isSpinnerI,d=" + parent.getId() +
//                        ",viewid=" + view.getId() + ",pos=" + pos + ",id=" + id);

                //设置spinner内的填充文字居中
                //((TextView)view).setGravity(Gravity.CENTER);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
    }
}
