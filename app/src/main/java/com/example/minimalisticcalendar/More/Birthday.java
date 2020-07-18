package com.example.minimalisticcalendar.More;

public class Birthday {
    private String mTitle;
    private String mTime;

    public Birthday(String title, String time){
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
