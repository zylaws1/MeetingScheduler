package com.example.xinshen.comp2100_meetingschedule.data.model;


import androidx.databinding.ObservableField;

/**
 * User feedback class
 *
 * @author Xin Shen, Shaocong Lang
 */
public class Feedback {
    ObservableField<String> userName = new ObservableField<>();
    ObservableField<String> feedback = new ObservableField<>();

    public Feedback(String userName, String feedback) {
        this.userName.set(userName);
        this.feedback.set(feedback);
    }

    public ObservableField<String> getUserName() {
        return userName;
    }

    public void setUserName(ObservableField<String> userName) {
        this.userName = userName;
    }

    public ObservableField<String> getFeedback() {
        return feedback;
    }

    public void setFeedback(ObservableField<String> feedback) {
        this.feedback = feedback;
    }

}
