
package com.example.xinshen.comp2100_meetingschedule.main;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xinshen.comp2100_meetingschedule.R;

import java.util.regex.Pattern;

/**
 * Set preference time slot fragment for quick add meeting
 *
 * @author Xin Shen, Shaocong Lang
 */
public class SetPreferTimeslotFragment extends Fragment {

    private EditText edtTxt_pref1;
    private EditText edtTxt_pref2;
    private EditText edtTxt_pref3;
    Button btnSavePref;
    View rootview;
    Context mContext;

    @Override
    public void onAttach(@NonNull Context context) {
        // save the activity when first time attached
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // bond the views and controls for the first time initialization
        if (rootview == null) {
            rootview = inflater.inflate(R.layout.fragment_set_own_timeslot_preference, null);
            edtTxt_pref1 = (EditText) rootview.findViewById(R.id.editText_pref_1);
            edtTxt_pref2 = (EditText) rootview.findViewById(R.id.editText_pref_2);
            edtTxt_pref3 = (EditText) rootview.findViewById(R.id.editText_pref_3);
            btnSavePref = (Button) rootview.findViewById(R.id.btn_save_pref);
            btnSavePref.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // use regular expression to check if the input preference time is valid
                    Pattern p = Pattern.compile("(\\s)*\\d{1,2}(\\s)*[:|：](\\s)*\\d{1,2}(\\s)*");
                    String str[] = new String[3];
                    str[0] = edtTxt_pref1.getText().toString();
                    str[1] = edtTxt_pref2.getText().toString();
                    str[2] = edtTxt_pref3.getText().toString();
                    // check three preference time validation
                    boolean allValid = true;
                    for (int i = 0; i < 3; i++) {
//                        Log.i("shenxin", "set pref str:" + str[i]);
                        if (p.matcher(str[i]).matches()) {
                            String hour_str = str[i].substring(0, str[i].indexOf(":"));
                            String minute_str = str[i].substring(str[i].indexOf(":") + 1);
                            int hour = Integer.parseInt(hour_str.trim().replaceAll(" ", ""));
                            int minute = Integer.parseInt(minute_str.trim().replaceAll(" ", ""));
                            Log.i("shenxin", "set pref hour and min:" + hour + " || " + minute);
                            // Set the preference time slot in adding fragment if valid
                            if (0 <= hour && hour < 25 && 0 <= minute && minute < 61) {
                                AddNewMeetingFragment.spinnerItems[i] = hour + ":" + minute;
                            } else {     // do not add and toast if input time value not valid
                                allValid = false;
                                Toast.makeText(mContext, "bad time!", Toast.LENGTH_LONG);
                            }
                        } else {     // do not add and toast if input expression not valid
                            allValid = false;
                            Toast.makeText(mContext, "invalid time preference", Toast.LENGTH_LONG);
                        }
                    }
                    // Toast if all times are valid
                    if (allValid){
                        Toast.makeText(mContext, "preference time changed!", Toast.LENGTH_LONG);
                        FragmentTransaction transaction = MainActivity.mFraManager.beginTransaction();
                        transaction.replace(R.id.main_linear, MainActivity.instance.getOwnProfileFragment());
                        transaction.commit();
                    }
                }
            });
        }
        return rootview;
    }
//   local test
//    public static void main(String[] args) {
//        String regex = "\\d{1,2}[:]\\d{1,2}";
//        System.out.println(regex.matches("12:00"));
//        System.out.println(regex.matches("24:60"));
//        System.out.println(regex.matches("25:66"));
//        System.out.println(regex.matches("666:666"));
//        System.out.println(regex.matches("6:666"));
//    }
}
