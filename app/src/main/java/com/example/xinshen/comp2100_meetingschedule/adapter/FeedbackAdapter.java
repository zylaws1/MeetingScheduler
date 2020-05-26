package com.example.xinshen.comp2100_meetingschedule.adapter;


import androidx.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.xinshen.comp2100_meetingschedule.R;
import com.example.xinshen.comp2100_meetingschedule.data.model.Feedback;
import com.example.xinshen.comp2100_meetingschedule.databinding.ItemFeedbackBinding;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Feedback adapter class
 *
 * @author Xin Shen, Shaocong Lang
 */
public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.FeedbackHolder> {
    List<Feedback> mList = null;

    public FeedbackAdapter() {
    }

    public void setList(List<Feedback> list){
        this.mList=list;
    }

    @NonNull
    @Override
    public FeedbackHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFeedbackBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_feedback, parent, false);
        return new FeedbackHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedbackHolder holder, int position) {
        if (mList != null) {
            holder.getBinding().setUserFeedback(mList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class FeedbackHolder extends RecyclerView.ViewHolder{
        ItemFeedbackBinding mBinding;
        public FeedbackHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            mBinding= (ItemFeedbackBinding) binding;
        }

        public ItemFeedbackBinding getBinding(){
            return mBinding;
        }

    }
}
