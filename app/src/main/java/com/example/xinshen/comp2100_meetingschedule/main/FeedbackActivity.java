package com.example.xinshen.comp2100_meetingschedule.main;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xinshen.comp2100_meetingschedule.R;
import com.example.xinshen.comp2100_meetingschedule.adapter.FeedbackAdapter;
import com.example.xinshen.comp2100_meetingschedule.data.model.Feedback;
import com.example.xinshen.comp2100_meetingschedule.databinding.ActivityFeedbackBinding;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FeedbackActivity extends AppCompatActivity {

    ActivityFeedbackBinding mBinding;
    TextView mTvRight;
    TextView mTvTitle;
    EditText mEtFeedback;
    ImageView ivBack;
    List<Feedback> list=new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_feedback);
        mTvRight = findViewById(R.id.tv_right);
        mTvTitle = findViewById(R.id.top_title);
        ivBack = findViewById(R.id.iv_back);
        mEtFeedback = findViewById(R.id.et_feedback);
        mTvRight.setVisibility(View.VISIBLE);
        mTvTitle.setText(R.string.feedback);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Feedback feedback=new Feedback("ah",mEtFeedback.getText().toString());
                list.add(feedback);
            }
        });
        initView();
    }

    private void initView() {
        RecyclerView.LayoutManager manager=new LinearLayoutManager(FeedbackActivity.this);
        mBinding.rvFeedback.setLayoutManager(manager);
        for (int i = 0; i < 10; i++) {
            Feedback feedback=new Feedback("xiaoming"+i,"这个软件太好用了");
            list.add(feedback);
        }
        FeedbackAdapter adapter=new FeedbackAdapter(list);
        mBinding.rvFeedback.setAdapter(adapter);

    }
}
