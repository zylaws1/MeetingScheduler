package com.example.xinshen.comp2100_meetingschedule.data.model;

/**
 * User feedback class for upload feedback data to server
 *
 * @author Xin Shen, Shaocong Lang
 */
public class FeedbackBean {
    private String name;
    private String feedback;

    public FeedbackBean() {
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}
