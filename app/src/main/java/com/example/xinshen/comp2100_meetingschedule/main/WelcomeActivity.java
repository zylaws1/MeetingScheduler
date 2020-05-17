package com.example.xinshen.comp2100_meetingschedule.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.xinshen.comp2100_meetingschedule.R;

import java.util.Timer;
import java.util.TimerTask;

public class WelcomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        //init and display welcome page for meetings app
        ImageView img=findViewById(R.id.welcome_img);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        startMainActivity();  // set time counted auto jump
    }

    // Delay and auto jump to main activity
    private void startMainActivity(){
        TimerTask delayTask = new TimerTask() {
            @Override
            public void run() {
                // jump to main activity
                Intent mainIntent = new Intent(WelcomeActivity.this,MainActivity.class);
                startActivity(mainIntent);
                WelcomeActivity.this.finish();
            }
        };
        //delay for 2500ms
        Timer timer = new Timer();
        //todo(shenxin : fix to 2500
        timer.schedule(delayTask,2500);
    }
}
