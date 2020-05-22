package com.example.xinshen.comp2100_meetingschedule.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.xinshen.comp2100_meetingschedule.R;
import com.hjq.bar.TitleBar;
import com.hjq.bar.style.TitleBarLightStyle;

import java.util.ArrayList;

/**
 * Search activity for quick finding
 *  a specific meeting by keywords.
 *
 * @author Xin Shen, Shaocong Lang
 */
public class SearchActivity extends AppCompatActivity {

    private TitleBar mTitleBar;
    private ListView search_result_lv;
    public EditText search;
    private Button button;
    private Button button_go;
    ArrayList<String> list = new ArrayList<>();
    ArrayList<MeetingModel> meeting_model_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // init the views and bond controllers
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_right, R.anim.no_slide);
        setContentView(R.layout.activity_search);
        meeting_model_list = MainActivity.instance.getComingMeetingsFragment().meetings_list;
        search = findViewById(R.id.editText2);
        mTitleBar = findViewById(R.id.title_bar);
        button = findViewById(R.id.button);
        button.setOnClickListener(new Buttonlistener());
        button_go = findViewById(R.id.button_go);
        button_go.setOnClickListener(new Buttonlistenner_go());

        // Set title bar as searching input view
        TitleBar.initStyle(new TitleBarLightStyle());
        mTitleBar = findViewById(R.id.title_bar);
        mTitleBar.setAlpha(0.92f);
        mTitleBar.setBackgroundColor(getResources().getColor(R.color.colorBottomNavigation));

        //init the searching data array list
        for (MeetingModel m : meeting_model_list)
            list.add(m.getName());
        initViews();
    }

    @Override
    public void finish() {
        super.finish();
        // add slide out animation
        overridePendingTransition(R.anim.no_slide, R.anim.slide_out_right);
    }


    private void initViews() {
        // Init view with unfiltered meetings.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, list);
        //    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.search_listview_textview, data);
        search_result_lv = findViewById(R.id.search_result_lv);
        search_result_lv.setAdapter(adapter);
        search_result_lv.setClickable(true);
        // set a click listener to transfer to meeting detailed information when touched
        search_result_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("shenxin itemClick", String.valueOf(position));
                if (!list.contains("Results not found")) {
                    // Switch to main activity with intent data to open information fragment.
//                    Log.i("shenxin", "onItemClick: " + meeting_model_list.get(position).getName());
                    Intent intent = new Intent(SearchActivity.this, MainActivity.class);
                    intent.putExtra("Info", meeting_model_list.get(position).getName());
                    startActivity(intent);
                }
            }
        });
    }

    // Add exit on back button
    private class Buttonlistener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            finish();
        }
    }

    // Add filter function on searching button
    private class Buttonlistenner_go implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            list.clear();   // clean all data first
            meeting_model_list = MainActivity.instance.getComingMeetingsFragment().meetings_list;
            ArrayList<MeetingModel> tmp_ary = new ArrayList<>();
            String input = search.getText().toString();
            Log.i("shenxin input:", input);
            if (input != "" && input.length() > 0) {    // check if input keyword valid
                for (MeetingModel m : meeting_model_list) {  // go through all meeting data
                    String mod_name = m.getName();
                    if (mod_name.toLowerCase().contains(input.toLowerCase())) {
                        list.add(mod_name);     // add into result list if match keyword no matter upper or lower letter
                        tmp_ary.add(m);
                    }
                }
            } else {    // do not filter any meeting if the keyword is invalid
                Log.i("shenxin", " search input null");
                Toast.makeText(SearchActivity.this, "serach can't be null", Toast.LENGTH_SHORT);
                for (MeetingModel m : meeting_model_list)
                    list.add(m.getName());
                tmp_ary = meeting_model_list;
            }
            // add a tip if not any matching result
            if (list.isEmpty())
                list.add("Results not found");
            meeting_model_list = tmp_ary;
            initViews();
        }
    }

}
