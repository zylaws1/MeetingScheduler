package com.example.xinshen.comp2100_meetingschedule.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xinshen.comp2100_meetingschedule.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * Helper activity, provide some basic guide
 * description for app users.
 *
 * @author Xin Shen, Shaocong Lang
 */
public class QuickHelpFragment extends Fragment {
    TextView tvTopTitle;
    TextView tvTopRight;
    ImageView ivBack;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // bond views and controls and display guide description.
        View view = inflater.inflate(R.layout.activity_quick_help, null);
        tvTopTitle = view.findViewById(R.id.top_title);
        tvTopRight = view.findViewById(R.id.tv_right);
        ivBack = view.findViewById(R.id.iv_back);
        tvTopRight.setVisibility(View.GONE);
        tvTopTitle.setText(getResources().getString(R.string.quick_help));
        MainActivity.setHideTitleBar();
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
                MainActivity.instance.setShowTitleBar();
            }
        });
        return view;
    }
}
