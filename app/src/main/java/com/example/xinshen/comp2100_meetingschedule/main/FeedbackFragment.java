package com.example.xinshen.comp2100_meetingschedule.main;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.xinshen.comp2100_meetingschedule.R;
import com.example.xinshen.comp2100_meetingschedule.adapter.FeedbackAdapter;
import com.example.xinshen.comp2100_meetingschedule.data.model.Feedback;
import com.example.xinshen.comp2100_meetingschedule.data.model.FeedbackBean;
import com.example.xinshen.comp2100_meetingschedule.database.SpManager;
import com.example.xinshen.comp2100_meetingschedule.databinding.ActivityFeedbackBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    private static final String TAG = "shenxin";
    private static final String FBFD_NAME = "feedback_list";
    private static final String FB_SIZE = "feedback_size";
    private static final String TEST_NAME = "test_fb";
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child(FBFD_NAME);
    ActivityFeedbackBinding mBinding;
    List<Feedback> list = new ArrayList<>();
    List<FeedbackBean> beanList = new ArrayList<>();
    FeedbackAdapter mAdapter;
    String userName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = ActivityFeedbackBinding.inflate(inflater);
        mBinding.fbRight.setVisibility(View.VISIBLE);
        mBinding.fbTitle.setText(R.string.feedback);
        MainActivity.setHideTitleBar();
        userName = SpManager.getInstance(getActivity().getApplicationContext()).getUserName();
        mBinding.fbBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
                MainActivity.instance.setShowTitleBar();
            }
        });
        mBinding.fbRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String feedbackValue = mBinding.etFeedback.getText().toString();
                if (feedbackValue.trim().equals("")) {
                    showToast(getActivity().getString(R.string.feedback_null));
                } else {
                    mBinding.etFeedback.setText("");
                    showToast(getActivity().getString(R.string.feedback_success));
                    Feedback feedback = new Feedback(userName + ":", feedbackValue);
                    list.add(feedback);
                    FeedbackBean bean = new FeedbackBean();
                    bean.setName(userName);
                    bean.setFeedback(feedbackValue);
                    mDatabase.child(bean.getName()).setValue(bean);
                }
            }
        });
        loadFeedbackDataFromFirebase();
        initView();
        return mBinding.getRoot();
    }

    // Randomly update a node to trigger the callback to get data from server
    private void loadFeedbackDataFromFirebase() {
        mDatabase.orderByChild("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();
                beanList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (!FB_SIZE.equals(ds.getKey()) && !TEST_NAME.equals(ds.getKey())) {
                        FeedbackBean bean = ds.getValue(FeedbackBean.class);
                        Feedback feedback = new Feedback(bean.getName(), bean.getFeedback());
                        list.add(feedback);
                        beanList.add(bean);
                    }
                }
                if (list.size() > 0) {
                    mAdapter.setList(list);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    // Init the view by adding some demo feedback
    private void initView() {
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
        mBinding.rvFeedback.setLayoutManager(manager);
        mAdapter = new FeedbackAdapter();
        mAdapter.setList(list);
        mBinding.rvFeedback.setAdapter(mAdapter);
    }

    private void showToast(String info) {
        Toast.makeText(getActivity().getApplicationContext(), info, Toast.LENGTH_SHORT).show();
    }
}
