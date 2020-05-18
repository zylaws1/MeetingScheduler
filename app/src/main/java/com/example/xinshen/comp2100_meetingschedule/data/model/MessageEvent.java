package com.example.xinshen.comp2100_meetingschedule.data.model;

public class MessageEvent {
    public MessageEvent(){

    }
    public int getLoginState() {
        return loginState;
    }

    public void setLoginState(int loginState) {
        this.loginState = loginState;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private int loginState;
    private String message;
}
