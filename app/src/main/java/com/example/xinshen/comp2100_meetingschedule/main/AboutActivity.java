
package com.example.xinshen.comp2100_meetingschedule.main;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xinshen.comp2100_meetingschedule.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


/**
 * Introduce the basic information about the meeting scheduler app.
 * Also provide a quick help
 *
 * @author Xin Shen, Shaocong Lang
 */
public class AboutActivity extends AppCompatActivity {
    TextView tvTopTitle;
    TextView tvTopRight;
    ImageView ivBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // bond the views and controls for the first time initialization
        setContentView(R.layout.activity_about);
        tvTopTitle = findViewById(R.id.top_title);
        tvTopRight = findViewById(R.id.tv_right);
        ivBack = findViewById(R.id.iv_back);
        tvTopRight.setVisibility(View.GONE);
        tvTopTitle.setText(getApplicationContext().getString(R.string.quick_help));
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {   //  do nothing for exit help
                finish();
            }
        });
    }
}