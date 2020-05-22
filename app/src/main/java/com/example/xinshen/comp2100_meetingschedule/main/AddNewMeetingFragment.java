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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Add new meeting fragment
 *
 * @author Xin Shen, Shaocong Lang
 */
public class AddNewMeetingFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "shenxin";
    private TextView txtDate;
    private TextView txtTime;
    private TextView txtRemindTime;
    private Button btnDate;
    private Button btnTime;
    public static int date;
    public static int hour;
    public static int minute;
    public static String date_str;
    public static String time_str;
    public static long remind_time_advance = 1;
    public EditText name;
    public EditText room;
    public EditText venue;
    public EditText description;
    private Spinner mSpinner_pref_time;
    private Spinner mSpinner_remind_time;
    public static String[] spinnerItems = {"10:00", "14:00", "18:00"};
    Calendar my_calendar = Calendar.getInstance(Locale.ENGLISH);
    View rootView;

    AddNewMeetingFragment() {
        super();
//        Log.i(TAG, "init AddNewMeetingFragment");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // bond the views and controls for the first time initialization
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_add_new_meeting, null);
            btnDate = (Button) rootView.findViewById(R.id.btn_Date);
            btnTime = (Button) rootView.findViewById(R.id.btn_Time);
            txtDate = (TextView) rootView.findViewById(R.id.txtDate);
            txtTime = (TextView) rootView.findViewById(R.id.txtTime);
            txtRemindTime = (TextView) rootView.findViewById(R.id.txt_remind_time);
            name = (EditText) rootView.findViewById(R.id.edit_txt_name);
            room = (EditText) rootView.findViewById(R.id.edit_txt_room);
            venue = (EditText) rootView.findViewById(R.id.edit_txt_venue);
            description = (EditText) rootView.findViewById(R.id.edit_txt_description);
            mSpinner_pref_time = (Spinner) rootView.findViewById(R.id.spinner_add);
            mSpinner_remind_time = (Spinner) rootView.findViewById(R.id.spinner_remind_time);
            btnDate.setOnClickListener(this);
            btnTime.setOnClickListener(this);
            name.setSaveEnabled(false);
            room.setSaveEnabled(false);
            venue.setSaveEnabled(false);
            description.setSaveEnabled(false);
        }
        // init Spinner style and content
        ChangeSpinner(rootView);

        // Init the content if find bundle information from shared token.
        Bundle bundle = getArguments();
        if (bundle != null) {
            String data = bundle.getString("item");
            Log.i(TAG, "onCreateView get bundle:" + data);
            if (data != null) {
                String datas[] = data.split(",");
                if (datas.length == 8) {
                    Log.i(TAG, "onCreateView: bundle info good");
                    name.setText(datas[0]);
                    room.setText(datas[1]);
                    description.setText(datas[2]);
                    venue.setText(datas[3]);
                    txtDate.setText(datas[4]);
                    date_str = datas[4];
                    time_str = datas[5];
                    date = Integer.parseInt(datas[6]);
                    txtTime.setText(datas[5]);
                    hour = Integer.parseInt(datas[7]);
                }
            }
            bundle.clear();
        }
        return rootView;
    }

    public static void showDatePickerDialog(Activity activity, int themeResId, final TextView tv, Calendar calendar) {
        // calendar.set(2020, 05, 20);
        // create an instance of DatePickerDialog and show
        new DatePickerDialog(activity, themeResId, new DatePickerDialog.OnDateSetListener() {
            // bond the listener (How the parent is notified that the date is set.)
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // set the date params for meeting according to chosen result.
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
                // set init date
                , calendar.get(Calendar.YEAR)
                , calendar.get(Calendar.MONTH)
                , calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    // Transfer the "yyyy-MM-dd" date to weekday
    // return: int: range: 1-7 as Monday to Sunday
    public static int dateToWeek(String datetime) {
        Log.i(TAG, "dateToWeek: " + datetime);
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance(); // get the calendar
        Date datet = null;
        try {
            datet = f.parse(datetime);
            cal.setTime(datet);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int w = cal.get(Calendar.DAY_OF_WEEK); // transfer to the day in a week.
        if (w < 0)
            w = 0;
        return w;
    }

    /**
     * Time Picker
     *
     * @param activity
     * @param themeResId
     * @param tv
     * @param calendar
     */
    public static void showTimePickerDialog(Activity activity, int themeResId, final TextView tv, Calendar calendar) {
        TimePickerDialog time_dialog = new TimePickerDialog(activity, themeResId,
                // bond listener
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minuteC) {
                        //  set time as chosen result.
                        time_str = hourOfDay + ":" + minuteC;
                        tv.setText("Time:" + hourOfDay + ":" + minuteC);
                        hour = hourOfDay;
                        minute = minuteC;
                    }
                }
                // set init time
                , calendar.get(Calendar.HOUR_OF_DAY)
                , calendar.get(Calendar.MINUTE)
                // 24 hours format
                , true);
        time_dialog.show();
    }

    @Override
    public void onClick(View view) {    // respond the click action to open picker
        int themeResid = 4; // android dark style 4
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

    // init the spinner style and parameters
    public void ChangeSpinner(View v) {
        // init spinner style
        mSpinner_pref_time.setDropDownWidth(400); // drop Width
        mSpinner_pref_time.setDropDownHorizontalOffset(100); // drop horizontal offset
        mSpinner_pref_time.setDropDownVerticalOffset(100); // drop vertical offset

        // set adapter as content
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(),
                R.layout.spinner_select, spinnerItems);
        // set the drop style
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_drop);
        mSpinner_pref_time.setAdapter(spinnerAdapter);

        mSpinner_pref_time.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            boolean isSpinnerFirst = true;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Log.i(TAG, "onItemSelected: " + id);
                // first time loaded do not make default select
                if (isSpinnerFirst) {
                    isSpinnerFirst = false;
                } else {  // set meeting time according to selected item
                    time_str = spinnerItems[(int) id];
                    String hour_str = time_str.substring(0, time_str.indexOf(":"));
                    hour = Integer.parseInt(hour_str);
                    txtTime.setText("Time: " + time_str);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

        mSpinner_remind_time.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                // set meeting reminding time according to selected item
                txtRemindTime.setText(mSpinner_remind_time.getAdapter().getItem(pos).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
    }
}
