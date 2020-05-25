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
import com.example.xinshen.comp2100_meetingschedule.database.SpManager;
import com.example.xinshen.comp2100_meetingschedule.databinding.ActivityFeedbackBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hjq.bar.TitleBar;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Provide users with fragment that can write feedback
 *
 * @author Xin Shen, Shaocong Lang
 */
public class FeedbackFragment extends Fragment {

    ActivityFeedbackBinding mBinding;
    TextView mTvRight;
    TextView mTvTitle;
    EditText mEtFeedback;
    ImageView ivBack;
    List<Feedback> list = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = ActivityFeedbackBinding.inflate(inflater);
//        mBinding =  DataBindingUtil.inflate(inflater, R.layout.activity_feedback,container,false);
        mBinding.fbRight.setVisibility(View.VISIBLE);
        mBinding.fbTitle.setText(R.string.feedback);
        MainActivity.setHideTitleBar();
        mBinding.fbBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
                MainActivity.setShowTitleBar();
            }
        });
        mBinding.fbRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = SpManager.getInstance(getActivity().getApplicationContext()).getUserName();
                Feedback feedback = new Feedback(userName + ":", mBinding.etFeedback.getText().toString());
                list.add(feedback);
            }
        });
        initView();
        return mBinding.getRoot();
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
