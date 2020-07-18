package com.example.minimalisticcalendar.Startpage;

public class CDate {
    String mTitle;
    Integer mTime;
    String mColor;

    public CDate(String title, Integer time, String color){
        mTitle = title;
        mTime = time;
        mColor = color;
    }

    public String title(){
        return mTitle;
    }

    public Integer time(){
        return mTime;
    }

    public String color(){
        return mColor;
    }
}
