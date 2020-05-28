package com.example.xinshen.comp2100_meetingschedule;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import androidx.test.core.app.ApplicationProvider;

/**
 * Test helper class, to inflate the listView content
 * create an mock adapter.
 * @author Xin Shen, Shaocong Lang
 */
class ShadowCountingAdapter extends BaseAdapter {
    private int itemCount;

    public ShadowCountingAdapter(int itemCount) {
        this.itemCount = itemCount;
    }

    public void setCount(int itemCount) {
        this.itemCount = itemCount;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return itemCount;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView = new TextView(ApplicationProvider.getApplicationContext());
        textView.setText("Item " + position);
        return textView;
    }
}
