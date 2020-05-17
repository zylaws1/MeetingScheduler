package com.example.xinshen.comp2100_meetingschedule.main;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.xinshen.comp2100_meetingschedule.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MeetingsListview extends ListView implements OnGestureListener, View.OnTouchListener {

    public interface LongOnClickCallback {
        public void onLongOnClick();

        public void onClick();
    }

    public interface OnEditListener {       // callback to activity for edit
        void onEdit(int index);

        void onMutilEdit(int[] indexs);

        void show_delete_all_btn();

        void hide_delete_all_btn();
    }

    private GestureDetector gestureDetector;

    private OnEditListener mEditListener;      // edit listener

    private LongOnClickCallback callback;

    private View deleteButton;     // delete  button view

    private View mutilDeleteCbs;     // mutil delete checkboxes view

    private ViewGroup itemLayout;   // item to be changed, ViewGroup object

    private int selectedId;   //chosen item

    private boolean isDeleteShown;   // is delete button shown flag

    private boolean isMutilDeleteShown;   // is mutil delete button shown flag

    public HashMap<ViewGroup, View> all_meeting_items = new HashMap<>(); //all meeting items list

    public ArrayList<Integer> selecting_cbs = new ArrayList<>(); //all meeting items list

    public void setCallback(LongOnClickCallback callback) {
        this.callback = callback;
    }

    public void setOnDeleteListener(OnEditListener l) {
        this.mEditListener = l;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
//        Log.i("shenxin", "onTouch: " + isDeleteShown);
//        if (isDeleteShown) {
//            itemLayout.removeView(deleteButton);
//            deleteButton = null;
//            isDeleteShown = false;
//            return true;
//        } else {
//            // 如果在空白地方继续滑动  ,  禁止非法位置出现  删除按钮
//            if (AdapterView.INVALID_POSITION == pointToPosition((int) event.getX(), (int) event.getY())) {
//                return false;
//            }
//            selectedId = pointToPosition((int) event.getX(), (int) event.getY());
//            return gestureDetector.onTouchEvent(event);
//        }
        return false;
    }

    public MeetingsListview(Context context) {
        super(context);
        init();
    }

    public MeetingsListview(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MeetingsListview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        detector = new GestureDetector(getContext(), this);
    }

    private GestureDetector detector;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        detector.onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        if (!isDeleteShown) {
            selectedId = pointToPosition((int) e.getX(), (int) e.getY());
        }
        return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (!isMutilDeleteShown && !isDeleteShown && Math.abs(velocityX) > Math.abs(velocityY)) {
            deleteButton = LayoutInflater.from(getContext()).inflate(R.layout.meetings_listview_delete_btn, null);
            deleteButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemLayout.removeView(deleteButton);
                    deleteButton = null;
                    isDeleteShown = false;
                    mEditListener.onEdit(selectedId);
                }
            });
            itemLayout = (ViewGroup) getChildAt(selectedId - getFirstVisiblePosition());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.MATCH_PARENT
            );
            params.setMargins(20, 45, 3, 45);
            itemLayout.addView(deleteButton, params);
            isDeleteShown = true;
        } else {
            Log.i("shenxin", "onFling: else");
            if (isDeleteShown)
                itemLayout.removeView(deleteButton);
            isDeleteShown = false;
        }
        return false;
    }

    @Override
    public void onLongPress(MotionEvent event) {
        if (!isMutilDeleteShown && !isDeleteShown) {
            selecting_cbs.clear();
            int child_cnt = getChildCount();
            Log.i("shenxin", "onLongPress:child cuont " + getChildCount());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.MATCH_PARENT
            );
            params.setMargins(20, 45, 3, 45);
            for (int i = 0; i < child_cnt; i++) {
                itemLayout = (ViewGroup) getChildAt(i - getFirstVisiblePosition());
                View mutilDeleteCbs = LayoutInflater.from(getContext()).inflate(R.layout.meetings_listview_mutil_delete_cb, null);
                selectedId = i;
                mutilDeleteCbs.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!selecting_cbs.contains(selectedId))
                            selecting_cbs.add(selectedId);
                    }
                });
                itemLayout.addView(mutilDeleteCbs, params);
                all_meeting_items.put(itemLayout, mutilDeleteCbs);
            }
            mEditListener.show_delete_all_btn();
            isMutilDeleteShown = true;
        } else {
            Log.i("shenxin", "onFling: else");
            //itemLayout.removeView(mutilDeleteCbs);
            for (Map.Entry<ViewGroup, View> e : all_meeting_items.entrySet()) {
                e.getKey().removeView(e.getValue());
            }
            mEditListener.hide_delete_all_btn();
            isMutilDeleteShown = false;
        }
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        Log.i("shenxin", "onScroll");
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
//        Log.i("shenxin", "onSingleTapUp: " + e.getX() + " " + e.getY());
        if (isDeleteShown && selectedId != pointToPosition((int) e.getX(), (int) e.getY())) {
            itemLayout.removeView(deleteButton);
            isDeleteShown = false;
        } else if (isMutilDeleteShown && e.getX() < 1190) {
            for (Map.Entry<ViewGroup, View> ent : all_meeting_items.entrySet()) {
                ent.getKey().removeView(ent.getValue());
            }
            mEditListener.hide_delete_all_btn();
            isMutilDeleteShown = false;
        } else {

        }
        return false;
    }

}
