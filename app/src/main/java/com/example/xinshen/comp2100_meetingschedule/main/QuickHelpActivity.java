package com.example.xinshen.comp2100_meetingschedule.main;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xinshen.comp2100_meetingschedule.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class QuickHelpActivity extends AppCompatActivity {
    TextView tvTopTitle;
    TextView tvTopRight;
    ImageView ivBack;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_help);
        tvTopTitle = findViewById(R.id.top_title);
        tvTopRight = findViewById(R.id.tv_right);
        ivBack = findViewById(R.id.iv_back);
        tvTopRight.setVisibility(View.GONE);
        tvTopTitle.setText(getApplicationContext().getString(R.string.quick_help));
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
