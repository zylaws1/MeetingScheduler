package com.example.xinshen.comp2100_meetingschedule.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xinshen.comp2100_meetingschedule.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hjq.bar.TitleBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * Introduce the basic information about the meeting scheduler app.
 *
 * @author Xin Shen, Shaocong Lang
 */
public class AboutFragment extends Fragment {
    TextView tvTopTitle;
    TextView tvTopRight;
    ImageView ivBack;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_about,null);
        tvTopTitle = view.findViewById(R.id.top_title);
        tvTopRight = view.findViewById(R.id.tv_right);
        ivBack = view.findViewById(R.id.iv_back);
        tvTopRight.setVisibility(View.GONE);
        tvTopTitle.setText(getResources().getString(R.string.about_meeting));
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
