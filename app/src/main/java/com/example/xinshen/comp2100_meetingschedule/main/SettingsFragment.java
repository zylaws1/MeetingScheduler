package com.example.xinshen.comp2100_meetingschedule.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.xinshen.comp2100_meetingschedule.R;
import com.example.xinshen.comp2100_meetingschedule.database.SpManager;

/**
 * The main fragment of settings, through which you can jump to other setting fragment
 *
 * @author Xin Shen, Shaocong Lang
 */
public class SettingsFragment extends Fragment implements View.OnClickListener {
    RelativeLayout mFeedback;
    RelativeLayout mQuickHelp;
    RelativeLayout mAboutMeeting;
    RelativeLayout mSignOut;
    private QuickHelpFragment quickHelpFragment;
    private AboutFragment aboutFragment;
    private FeedbackFragment feedbackFragment;
    public static String userName = null;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // bond the views and controls for initialization
        view = inflater.inflate(R.layout.activity_setting, null);
        mFeedback = view.findViewById(R.id.layout_feedback);
        mQuickHelp = view.findViewById(R.id.layout_quick_help);
        mAboutMeeting = view.findViewById(R.id.layout_about_meeting);
        mSignOut = view.findViewById(R.id.layout_sign_out);
        mFeedback.setOnClickListener(this);
        mQuickHelp.setOnClickListener(this);
        mAboutMeeting.setOnClickListener(this);
        mSignOut.setOnClickListener(this);
        quickHelpFragment = new QuickHelpFragment();
        aboutFragment = new AboutFragment();
        feedbackFragment = new FeedbackFragment();
        userName = SpManager.getInstance(getActivity().getApplicationContext()).getUserName();
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_about_meeting:
                FragmentManager fraManager = getFragmentManager();
                FragmentTransaction transaction = fraManager.beginTransaction();
                transaction.addToBackStack(null);
                transaction.replace(R.id.main_linear, aboutFragment);
                transaction.commit();
                break;
            case R.id.layout_feedback:
                if (userName != null) {
                    FragmentManager fraManager1 = getFragmentManager();
                    FragmentTransaction transaction1 = fraManager1.beginTransaction();
                    transaction1.addToBackStack(null);
                    transaction1.replace(R.id.main_linear, feedbackFragment);
                    transaction1.commit();
                } else {
                    showToast(getString(R.string.no_login));
                }
                break;
            case R.id.layout_quick_help:
                FragmentManager fraManager2 = getFragmentManager();
                FragmentTransaction transaction2 = fraManager2.beginTransaction();
                transaction2.addToBackStack(null);
                transaction2.replace(R.id.main_linear, quickHelpFragment);
                transaction2.commit();
                break;
            case R.id.layout_sign_out:
                if (userName != null) {
                    SpManager.getInstance(getActivity().getApplicationContext()).setUserName(null, true);
                    showToast(getString(R.string.sign_out_success));
                } else {
                    showToast(getString(R.string.has_signed_out));
                }
                break;
            default:
                break;
        }
    }

//    private class Linerlistener implements View.OnClickListener{
//        @Override
//        public void onClick(View view) {
//            if(view.getId()==R.id.check){
//                Intent intent=new Intent();
//                intent.setClass(getActivity(),CheckActivity.class);
//                startActivity(intent);
//            }
//            if(view.getId()==R.id.payment){
//                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                builder.setTitle("Select a payment method");
//                final String key="payment";
//                final String[] payments = {"Paypal", "Credit card", "Gift card", "Others"};
//                builder.setItems(payments, new DialogInterface.OnClickListener()
//                {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which)
//                    {
//                        final String text = payments[which];
//                        editor.putString(key,text);
//                        editor.commit();
//                        Toast.makeText(getActivity(), "Your payment method is:"+ payments[which], Toast.LENGTH_SHORT).show();
//                    }
//                });
//                builder.show();
//            }
//            if(view.getId()==R.id.privacy){
//                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                final AlertDialog dialog = builder.create();
//                View dialogView = View.inflate(getActivity(), R.layout.set_dialog, null);
//                dialog.setView(dialogView);
//                dialog.show();
//                final EditText set = (EditText) dialogView.findViewById(R.id.et_name);
//                TextView title=dialogView.findViewById(R.id.subtitle);
//                title.setText("Set Introduction");
//                set.setHint("Introduce yourself!");
//                final String key="privacy";
//                Button btnLogin = (Button) dialogView.findViewById(R.id.btn_confirm);
//                Button btnCancel = (Button) dialogView.findViewById(R.id.btn_cancel);
//                btnLogin.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        final String text = set.getText().toString();
//                        editor.putString(key,text);
//                        editor.commit();
//                        if (editor.commit()){
//                            Toast.makeText(getActivity(),"Success", Toast.LENGTH_SHORT).show();
//                        }
//                        dialog.dismiss();
//                    }
//                });
//                btnCancel.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                    }
//                });
//            }
//            if(view.getId()==R.id.profile){
//                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                final AlertDialog dialog = builder.create();
//                View dialogView = View.inflate(getActivity(), R.layout.set_dialog, null);
//                dialog.setView(dialogView);
//                dialog.show();
//                final EditText set = (EditText) dialogView.findViewById(R.id.et_name);
//                TextView title=dialogView.findViewById(R.id.subtitle);
//                title.setText("Set profile name");
//                set.setHint("Type your profile name");
//                final String key="profile";
//                Button btnLogin = (Button) dialogView.findViewById(R.id.btn_confirm);
//                Button btnCancel = (Button) dialogView.findViewById(R.id.btn_cancel);
//                btnLogin.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        final String text = set.getText().toString();
//                        editor.putString(key,text);
//                        editor.commit();
//                        if (editor.commit()){
//                            Toast.makeText(getActivity(),"Success", Toast.LENGTH_SHORT).show();
//                        }
//                        dialog.dismiss();
//                    }
//                });
//                btnCancel.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                    }
//                });
//            }
//            if(view.getId()==R.id.location){
//                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                builder.setTitle("Select a city");
//                final String key="city";
//                final String[] cities = {"Canberra", "Sydney", "Melbourne", "Queensland", "Adelaide","Perth"};
//                builder.setItems(cities, new DialogInterface.OnClickListener()
//                {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which)
//                    {
//                        final String text = cities[which];
//                        editor.putString(key,text);
//                        editor.commit();
//                        Toast.makeText(getActivity(), "Your city is:"+ cities[which], Toast.LENGTH_SHORT).show();
//                    }
//                });
//                builder.show();
//
//
//        }
//        }
//    }

    private void showToast(String info) {
        Toast.makeText(getActivity().getApplicationContext(), info, Toast.LENGTH_SHORT).show();
    }
}
