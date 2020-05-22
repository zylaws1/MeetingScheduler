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
        TitleBar.initStyle(new TitleBarLightStyle());
        mTitleBar = findViewById(R.id.title_bar);
        mTitleBar.setAlpha(0.92f);
        mTitleBar.setBackgroundColor(getResources().getColor(R.color.colorBottomNavigation));

        for (MeetingModel m : meeting_model_list)
            list.add(m.getName());
        initViews();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.no_slide, R.anim.slide_out_right);
    }

    private void initViews() {

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, list);
        //    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.search_listview_textview, data);
        search_result_lv = findViewById(R.id.search_result_lv);
        search_result_lv.setAdapter(adapter);
        search_result_lv.setClickable(true);
        search_result_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("shenxin itemClick", String.valueOf(position));
                if (!list.contains("Results not found")) {
                    Log.i("shenxin", "onItemClick: " + meeting_model_list.get(position).getName());
                    Intent intent = new Intent(SearchActivity.this, MainActivity.class);
                    intent.putExtra("Info", meeting_model_list.get(position).getName());
                    startActivity(intent);
                }
            }
        });
    }


    private class Buttonlistener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            finish();
        }
    }

    private class Buttonlistenner_go implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            list.clear();
            meeting_model_list = MainActivity.instance.getComingMeetingsFragment().meetings_list;
            ArrayList<MeetingModel> tmp_ary = new ArrayList<>();
            String input = search.getText().toString();
            Log.i("shenxin input:", input);
            if (input != "" && input.length() > 0) {
                for (MeetingModel m : meeting_model_list) {
                    String mod_name = m.getName();
                    if (mod_name.toLowerCase().contains(input.toLowerCase())) {
                        list.add(mod_name);
                        tmp_ary.add(m);
                    }
                }
            } else {
                Log.i("shenxin", " search input null");
                Toast.makeText(SearchActivity.this, "serach can't be null", Toast.LENGTH_SHORT);
                for (MeetingModel m : meeting_model_list)
                    list.add(m.getName());
                tmp_ary = meeting_model_list;
            }
            if (list.isEmpty())
                list.add("Results not found");
            meeting_model_list = tmp_ary;
            initViews();
        }
    }

}
