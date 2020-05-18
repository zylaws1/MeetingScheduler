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

import com.example.xinshen.comp2100_meetingschedule.R;
import com.hjq.bar.TitleBar;
import com.hjq.bar.style.TitleBarLightStyle;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    private TitleBar mTitleBar;
    private ListView search_result_lv;
    public EditText search;
    private Button button;
    private Button button2;
    private String[] data = {
            "Meeting01", "Video01", "Post03", "Meeting04", "Meeting05",
            "Meeting06", "Post07", "Meeting08", "Meeting09", "Meeting10",
            "Meeting11", "Video22", "Meeting33", "Meeting44", "Meeting55"
    };
    ArrayList<String> list = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        for (int i = 0; i < data.length; ++i) {
            list.add(data[i]);
        }

        initViews();
    }

    private void initViews() {
        search = findViewById(R.id.editText2);
        mTitleBar = findViewById(R.id.title_bar);

        button = findViewById(R.id.button);
        button.setOnClickListener(new Buttonlistener());

        button2 = findViewById(R.id.button_go);
        button2.setOnClickListener(new Buttonlistener2());

     //   ArrayAdapter<String> adapter;
     //   adapter = new ArrayAdapter<String>(this,R.layout.activity_search,R.id.search_listview_textview,data);
     //   adapter=new ArrayAdapter<String>(this,R.layout.activity_search,R.id.item_text,data);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, list);

    //    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.search_listview_textview, data);
        search_result_lv = findViewById(R.id.search_result_lv);
        search_result_lv.setAdapter(adapter);
        search_result_lv.setClickable(true);
        search_result_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("itemClick", String.valueOf(position));
                if (!list.contains("Results not found"))
                { Intent intent = new Intent(SearchActivity.this, MeetingInfoActivity.class);
                intent.putExtra("label", data[position]);
                startActivity(intent);}
            }
        });


        TitleBar.initStyle(new TitleBarLightStyle());
        mTitleBar = findViewById(R.id.title_bar);
        mTitleBar.setAlpha(0.6f);
        //Drawable title_bar_left_ic = getResources().getDrawable(R.drawable.titlebar_logo_vivid);
        //mTitleBar.setLeftIcon(MainActivity.zoomDrawable(title_bar_left_ic, 820, 330));
        mTitleBar.setBackgroundColor(getResources().getColor(R.color.colorBottomNavigation));
    }


    private class Buttonlistener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent2 = new Intent(SearchActivity.this,MainActivity.class);
            startActivity(intent2);
        }
    }

    private class Buttonlistener2 implements View.OnClickListener {
        @Override
        public void onClick(View v) {




            list.clear();
            Log.d("shuru",search.getText().toString());
            if (search.getText().toString() != "") {
                int i = search.getText().length();
                Log.d("jinru", Integer.toString(i));
                String str = search.getText().toString();
                for (String string : data)
                    if (string.length() >= i) {
                        Log.d("zhiwei",string.substring(0, i));
                        if (str.equalsIgnoreCase(string.substring(0, i))) {
                            list.add(string);
                        }
                    }
            }
            else
            {   Log.d("shiyan","111111111111");
                Toast.makeText(SearchActivity.this,"serach can't be null", Toast.LENGTH_SHORT);

            }
            if (list.isEmpty())
                list.add("Results not found");
            initViews();
        }
    }

}
