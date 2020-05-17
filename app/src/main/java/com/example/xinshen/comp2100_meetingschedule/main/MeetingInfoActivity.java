package com.example.xinshen.comp2100_meetingschedule.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.xinshen.comp2100_meetingschedule.R;

public class MeetingInfoActivity extends AppCompatActivity {

    private Button button;
    private TextView classname;
    private TextView classnum;
    private TextView classdes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_info);
        initial();
    }

    private void initial(){

    Intent intent = getIntent();
    String str = intent.getStringExtra("label");
    classname = findViewById(R.id.textViewClassname);
    classname.setText(str);

    button = findViewById(R.id.button3);
    button.setOnClickListener(new Buttonlistener3());
    }


    private class Buttonlistener3 implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent2 = new Intent(MeetingInfoActivity.this,MainActivity.class);
            startActivity(intent2);
        }
    }
}
