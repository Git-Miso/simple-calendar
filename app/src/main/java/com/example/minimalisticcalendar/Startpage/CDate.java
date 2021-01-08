package com.example.minimalisticcalendar.Startpage;

public class CDate {
    String mTitle;
    String mDesc;
    Integer mTime;
    String mColor;

    public CDate(String title, String desc, Integer time, String color){
        mTitle = title;
        mDesc = desc;
        mTime = time;
        mColor = color;
    }

    public String title(){
        return mTitle;
    }

    public String desc(){
        return mDesc;
    }

    public Integer time(){
        return mTime;
    }

    public String color(){
        return mColor;
    }
}
