package com.example.minimalisticcalendar.Notifications;

public class Alert {
    private String mTitle;
    private String mTime;

    public Alert(String title, String time){
        mTitle = title;
        mTime = time;
    }

    public String title(){
        return mTitle;
    }

    public String time(){
        return mTime;
    }
}
