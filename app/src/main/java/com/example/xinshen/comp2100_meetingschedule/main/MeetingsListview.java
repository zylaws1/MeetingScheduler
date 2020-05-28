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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.LinearLayout;

import androidx.fragment.app.FragmentTransaction;

import com.example.xinshen.comp2100_meetingschedule.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Meeting ListView for homepage display
 * Implements OnGestureListener so tha can detect gesture such as fling to delete, long press to multi delete.
 *
 * @author Xin Shen, Shaocong Lang
 */
public class MeetingsListview extends ListView implements OnGestureListener, View.OnTouchListener {

    private static final String TAG = "shenxin";
    private GestureDetector gestureDetector;
    private OnEditListener mEditListener;      // edit listener
    private LongOnClickCallback callback;
    private View deleteButton;     // delete  button view
    private View multiDeleteCbs;     // multi delete checkboxes view
    private ViewGroup itemLayout;   // item to be changed, ViewGroup object
    public ArrayList<ViewGroup> items_view_ary = new ArrayList<>();
    private int selectedId;   //chosen item
    private boolean isDeleteShown;   // is delete button shown flag
    public boolean isMultiDeleteShown;   // is multi delete button shown flag
    public HashMap<ViewGroup, View> all_meeting_items = new HashMap<>(); //all meeting items list
    public ArrayList<Integer> selecting_cbs = new ArrayList<>(); //all meeting items list

    public interface LongOnClickCallback {
        public void onLongOnClick();

        public void onClick();
    }

    public interface OnEditListener {       // callback to activity for deleting
        void onDeletePressed(int index);

        void onMultiDeleted(int[] indexs);

        void show_delete_all_btn();

        void hide_delete_all_btn();
    }

    public void setCallback(LongOnClickCallback callback) {
        this.callback = callback;
    }

    public void setOnDeleteListener(OnEditListener l) {
        this.mEditListener = l;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        return false;
    }

    public MeetingsListview(Context context) {
        super(context);
        Log.i(TAG, "MeetingsListview:0 ");
        init();
    }

    public void bondAdapter(ListAdapter adapter) {
//        Log.i(TAG, "bondAdapter: cnt" + adapter.getCount());
        for (int i = 0; i < adapter.getCount(); i++) {
            items_view_ary.add((ViewGroup) getChildAt(i));
        }
    }

    public MeetingsListview(Context context, AttributeSet attrs) {
        super(context, attrs);
//        Log.i(TAG, "MeetingsListview:1 ");
        init();
    }

//    public MeetingsListview(Context context, AttributeSet attrs, int defStyle) {
//        super(context, attrs, defStyle);
//        Log.i(TAG, "MeetingsListview: 3 params ");
//        init();
//    }

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
        if (!isDeleteShown)
            selectedId = pointToPosition((int) e.getX(), (int) e.getY());
        return false;
    }

    //set listener when single fling gesture
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (getChildCount() == 0) return false;
        // show single delete button on right for fling gesture
        if (!isMultiDeleteShown && !isDeleteShown && Math.abs(velocityX) > Math.abs(velocityY)) {
            deleteButton = LayoutInflater.from(getContext()).inflate(R.layout.meetings_listview_delete_btn, null);
            deleteButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemLayout.removeView(deleteButton);
                    deleteButton = null;
                    isDeleteShown = false;
                    mEditListener.onDeletePressed(selectedId);
                }
            });
            itemLayout = (ViewGroup) getChildAt(selectedId - getFirstVisiblePosition());
            // create delete button view
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.MATCH_PARENT
            );
            params.setMargins(20, 45, 3, 45);
            // add button view at selected line
            itemLayout.addView(deleteButton, params);
            isDeleteShown = true;
        } else {
            // remove delete button if touch other place
//            Log.i("shenxin", "onFling: else");
            if (isDeleteShown && itemLayout != null)
                itemLayout.removeView(deleteButton);
            isDeleteShown = false;
        }
        return false;
    }

    // show multi deleting checkboxes when long pressed meeting item
    @Override
    public void onLongPress(MotionEvent event) {
        // return if no meeting items in list
        if (getChildCount() == 0) return;
        // create a add multi delete checkboxes for all meeting items on normal state
        if (!isMultiDeleteShown && !isDeleteShown) {
            selecting_cbs.clear();
            int child_cnt = getChildCount();
            Log.i("shenxin", "onLongPress:child count " + getChildCount() + " " + isMultiDeleteShown + " " + isDeleteShown);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.MATCH_PARENT
            );
            params.setMargins(20, 45, 3, 45);
            // loop for init and displaying all the checkboxes
            for (int i = 0; i < child_cnt; i++) {
                itemLayout = (ViewGroup) getChildAt(i);
                View multiDeleteCbs = LayoutInflater.from(getContext()).inflate(R.layout.meetings_listview_multi_delete_cb, null);
                selectedId = i;
                multiDeleteCbs.setOnClickListener(new OnClickListener() {
                    //save the index for all selected meeting item for deleting later
                    @Override
                    public void onClick(View view) {
                        if (!selecting_cbs.contains(selectedId))
                            selecting_cbs.add(selectedId);
                    }
                });
                itemLayout.addView(multiDeleteCbs, params);
                all_meeting_items.put(itemLayout, multiDeleteCbs);
            }
            // change the main bottom button to deleting mode
            mEditListener.show_delete_all_btn();
            isMultiDeleteShown = true;
        } else {
            // remove all the deleting checkboxes if already in deleting mode
//            Log.i("shenxin", "onLongPress: else");
            for (Map.Entry<ViewGroup, View> e : all_meeting_items.entrySet()) {
                e.getKey().removeView(e.getValue());
            }
            mEditListener.hide_delete_all_btn();
            isMultiDeleteShown = false;
        }
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//        Log.i("shenxin", "onScroll");
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    //
    @Override
    public boolean onSingleTapUp(MotionEvent e) {
//        Log.i("shenxin", "onSingleTapUp x y: " + e.getX() + " " + e.getY());
//        Log.i("shenxin", "onSingleTapUp: " + MainActivity.SCREEN_WIDTH);
        int touched_id = pointToPosition((int) e.getX(), (int) e.getY());
        if (getChildCount() == 0) return false;
        // remove the single delete button when single tap on single deleting mode
        if (isDeleteShown && selectedId != touched_id) {
            if (itemLayout != null)
                itemLayout.removeView(deleteButton);
            isDeleteShown = false;
        } else if (isMultiDeleteShown && e.getX() < MainActivity.SCREEN_WIDTH * 0.75) {
            // remove all delete checkbox views when single tap on multi deleting mode
            for (Map.Entry<ViewGroup, View> ent : all_meeting_items.entrySet()) {
                ent.getKey().removeView(ent.getValue());
            }
            mEditListener.hide_delete_all_btn();
            isMultiDeleteShown = false;
        } else if (!isMultiDeleteShown && !isDeleteShown) {
            Log.i(TAG, "apply onTouch ");
            // normal mode: transact to meeting detailed fragment for selected meeting item
            MainActivity.setmTitleBarInactive();
            MainActivity.meetingInfoFragment.setTouched_id(touched_id);
            FragmentTransaction transaction = MainActivity.mFraManager.beginTransaction();
            transaction.replace(R.id.main_linear, MainActivity.meetingInfoFragment);
            transaction.commit();
        }
        return false;
    }


    public void setMultiDeleteShown(boolean multiDeleteShown) {
        isMultiDeleteShown = multiDeleteShown;
    }

    public void setDeleteShown(boolean deleteShown) {
        isDeleteShown = deleteShown;
    }
}
