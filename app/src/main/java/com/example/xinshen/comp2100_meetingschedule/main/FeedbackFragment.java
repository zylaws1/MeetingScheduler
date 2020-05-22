package com.example.xinshen.comp2100_meetingschedule.main;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xinshen.comp2100_meetingschedule.R;
import com.example.xinshen.comp2100_meetingschedule.adapter.FeedbackAdapter;
import com.example.xinshen.comp2100_meetingschedule.data.model.Feedback;
import com.example.xinshen.comp2100_meetingschedule.databinding.ActivityFeedbackBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hjq.bar.TitleBar;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FeedbackFragment extends Fragment {

    ActivityFeedbackBinding mBinding;
    TextView mTvRight;
    TextView mTvTitle;
    EditText mEtFeedback;
    ImageView ivBack;
    List<Feedback> list=new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_feedback, null);
        mBinding = DataBindingUtil.setContentView(getActivity(), R.layout.activity_feedback);
        mTvRight = view.findViewById(R.id.tv_right);
        mTvTitle = view.findViewById(R.id.top_title);
        ivBack = view.findViewById(R.id.iv_back);
        mEtFeedback = view.findViewById(R.id.et_feedback);
        mTvRight.setVisibility(View.VISIBLE);
        mTvTitle.setText(R.string.feedback);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });
        mTvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Feedback feedback = new Feedback("ah", mEtFeedback.getText().toString());
                list.add(feedback);
            }
        });
        initView();

        return view;
    }

    // Init the view by adding some demo feedback
    private void initView() {
        RecyclerView.LayoutManager manager=new LinearLayoutManager(getContext());
        mBinding.rvFeedback.setLayoutManager(manager);
        for (int i = 0; i < 10; i++) {
            Feedback feedback=new Feedback("xiaoming"+i,"This app is awesome!");
            list.add(feedback);
        }
        FeedbackAdapter adapter=new FeedbackAdapter(list);
        mBinding.rvFeedback.setAdapter(adapter);

    }
}
