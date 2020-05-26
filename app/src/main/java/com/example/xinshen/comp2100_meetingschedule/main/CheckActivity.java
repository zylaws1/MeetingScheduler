package com.example.xinshen.comp2100_meetingschedule.main;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.xinshen.comp2100_meetingschedule.R;

public class CheckActivity extends Activity {
    TextView profile, payment, location, privacy;
    Button back;
    SharedPreferences preferences;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_information);
        // bond the views and controls for the first time initialization
        back = findViewById(R.id.backtoset);
        back.setOnClickListener(new Buttonlistener());
        preferences = this.getSharedPreferences("Setting", 0);
        payment = findViewById(R.id.showpayment);
        profile = findViewById(R.id.showprofile);
        privacy = findViewById(R.id.showprivacy);
        location = findViewById(R.id.showlocation);
        payment.setText("Payment Method:" + preferences.getString("payment", ""));
        profile.setText("Profile Name:" + preferences.getString("profile", ""));
        privacy.setText("Introduction:" + preferences.getString("privacy", ""));
        location.setText("Location:" + preferences.getString("city", ""));

    }

    // bond check button listener
    private class Buttonlistener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(CheckActivity.this, MainActivity.class);
            intent.putExtra("set_flag", 1);
            startActivity(intent);
        }
    }
}


