package com.example.minimalisticcalendar.Startpage;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.minimalisticcalendar.More.cFiles;
import com.example.minimalisticcalendar.Notifications.Alert;
import com.example.minimalisticcalendar.Notifications.AlertReceiver;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import static android.content.Context.MODE_PRIVATE;

public class CWeek {
    private ArrayList<CDate> mMonday;
    private ArrayList<CDate> mTuesday;
    private ArrayList<CDate> mWednesday;
    private ArrayList<CDate> mThursday;
    private ArrayList<CDate> mFriday;
    private ArrayList<CDate> mSaturday;
    private ArrayList<CDate> mSunday;

    public CWeek(ArrayList<CDate> m,ArrayList<CDate> t,ArrayList<CDate> w,ArrayList<CDate> th,ArrayList<CDate> f,ArrayList<CDate> sa,ArrayList<CDate> s){
        mMonday = m;
        mTuesday = t;
        mWednesday = w;
        mThursday = th;
        mFriday = f;
        mSaturday = sa;
        mSunday = s;

    }

    public void sortdays() {
        mMonday = sort(mMonday);
        mTuesday = sort(mTuesday);
        mWednesday = sort(mWednesday);
        mThursday = sort(mThursday);
        mFriday = sort(mFriday);
        mSaturday = sort(mSaturday);
        mSunday = sort(mSunday);
    }

    private ArrayList<CDate> sort(ArrayList<CDate> Dates) {
        int i;
        int k;
        ArrayList<CDate> finalDates = new ArrayList<>();

        if (!Dates.isEmpty()) {
            final ArrayList<Integer> newTime = new ArrayList<>();
            for (i = 0; i < Dates.size(); i++) {
                newTime.add(Dates.get(i).time());
            }

            Collections.sort(newTime);

            for (k = 0; k < Dates.size(); k++) {
                //Wenn der Wert anders is
                //if (!Dates.get(i).time().equals(newTime.get(i))) {
                for (i = 0; i < Dates.size(); i++) {
                    //Dann gucke wo der Wert gleich dem anderen entspricht
                    if (Dates.get(i).time().equals(newTime.get(k))) {
                        finalDates.add(new CDate(Dates.get(i).title(), Dates.get(i).desc(), newTime.get(k), Dates.get(i).color()));
                        Dates.set(i, new CDate("", "",-1, "")); // allows two identical entries
                        break;
                    }
                }
                //}
            }
        }
        return finalDates;
    }

    public void addDate(Context context, String day, String weekdate, String createdtitle, String createdDesc, Integer createdtime, String createdcolor, String notification){
        boolean isExisting = false;
        Calendar c = Calendar.getInstance();
        c.set(Calendar.WEEK_OF_YEAR, Integer.parseInt(weekdate));

        switch (day) {
            case "Monday":
                isExisting = checkDuplicate(context, mMonday, createdtitle);
                if (!isExisting) {
                    mMonday.add(new CDate(createdtitle, createdDesc, createdtime, createdcolor));
                    c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                }
                break;
            case "Tuesday":
                isExisting = checkDuplicate(context, mTuesday, createdtitle);
                if (!isExisting) {
                    mTuesday.add(new CDate(createdtitle, createdDesc, createdtime, createdcolor));
                    c.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
                }
                break;
            case "Wednesday":
                isExisting = checkDuplicate(context, mWednesday, createdtitle);
                if (!isExisting) {
                    mWednesday.add(new CDate(createdtitle, createdDesc, createdtime, createdcolor));
                    c.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
                }
                break;
            case "Thursday":
                isExisting = checkDuplicate(context, mThursday, createdtitle);
                if (!isExisting) {
                    mThursday.add(new CDate(createdtitle, createdDesc, createdtime, createdcolor));
                    c.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
                }
                break;
            case "Friday":
                isExisting = checkDuplicate(context, mFriday, createdtitle);
                if (!isExisting) {
                    mFriday.add(new CDate(createdtitle, createdDesc, createdtime, createdcolor));
                    c.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
                }
                break;
            case "Saturday":
                isExisting = checkDuplicate(context, mSaturday, createdtitle);
                if (!isExisting) {
                    mSaturday.add(new CDate(createdtitle, createdDesc, createdtime, createdcolor));
                    c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                }
                break;
            case "Sunday":
                isExisting = checkDuplicate(context, mSunday, createdtitle);
                if (!isExisting) {
                    mSunday.add(new CDate(createdtitle, createdDesc, createdtime, createdcolor));
                    c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                }
                break;
        }

        //format time
        char[] timechars = createdtime.toString().toCharArray();

        if (timechars.length == 4) {
            c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timechars[0] + String.valueOf(timechars[1])));
            c.set(Calendar.MINUTE, Integer.parseInt(timechars[2] + String.valueOf(timechars[3])));
        } else {
            c.set(Calendar.HOUR_OF_DAY, timechars[0]);
            c.set(Calendar.MINUTE, Integer.parseInt(timechars[1] + String.valueOf(timechars[2])));
        }
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        Calendar c2 = Calendar.getInstance();
        long currenttime = c2.getTimeInMillis();
        c2.setTime(c.getTime());


        if (!isExisting && !notification.equals("no notification") && c.getTimeInMillis() > currenttime) {
            Toast toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
            TextView v;
            switch (notification) {
                case "10 minutes before start":
                    toast = Toast.makeText(context, "You will be reminded 10 minutes before your appointment starts", Toast.LENGTH_LONG);
                    c.add(Calendar.MINUTE, -10);
                    break;
                case "30 minutes before start":
                    toast = Toast.makeText(context, "You will be reminded 30 minutes before your appointment starts", Toast.LENGTH_LONG);
                    c.add(Calendar.MINUTE, -30);
                    break;
                case "1 hour before start":
                    toast = Toast.makeText(context, "You will be reminded 1 hour before your appointment starts", Toast.LENGTH_LONG);
                    c.add(Calendar.HOUR_OF_DAY, -1);
                    break;
                case "1 day before start":
                    toast = Toast.makeText(context, "You will be reminded 1 day before your appointment starts", Toast.LENGTH_LONG);
                    c.add(Calendar.DAY_OF_WEEK, -1);
                    c.set(Calendar.HOUR_OF_DAY, 16);
                    c.set(Calendar.MINUTE, 30);
                    if (day.equals("Monday")) {
                        c.set(Calendar.WEEK_OF_YEAR, Integer.parseInt(weekdate) - 1);
                    }
                    break;
                case "1 week before start":
                    toast = Toast.makeText(context, "You will be reminded 1 week before your appointment starts", Toast.LENGTH_LONG);
                    c.add(Calendar.DAY_OF_WEEK, -7);
                    c.set(Calendar.HOUR_OF_DAY, 16);
                    c.set(Calendar.MINUTE, 30);
                    if (day.equals("Monday")) {
                        c.set(Calendar.WEEK_OF_YEAR, Integer.parseInt(weekdate) - 1);
                    }
                    break;
            }
            v = toast.getView().findViewById(android.R.id.message);
            if (v != null) v.setGravity(Gravity.CENTER);
            toast.show();

            setAlarm(context, c, createdtitle, notification);
        }

        if (!isExisting && c2.getTimeInMillis() > currenttime) {
            setAlarm(context, c2, createdtitle, "no notification");
        }
    }

    public void deleteDate(Context context, String day, int delpos, View view, String weekdate) {
        final SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(view.getContext());
        ArrayList<String> list_todos = new ArrayList<>();

        switch (day) {
            case "Monday":
                for (int i = 0; !mPreferences.getString("t" + i, "").equals(""); i++) {
                    if (!mPreferences.getString("t" + i, "").contains(mMonday.get(delpos).title())) {
                        list_todos.add(mPreferences.getString("t" + i, ""));
                    }
                }
                cFiles.removeAlarm(context, mMonday.get(delpos).title());
                mMonday.remove(delpos);
                break;
            case "Tuesday":
                for (int i = 0; !mPreferences.getString("t" + i, "").equals(""); i++) {
                    if (!mPreferences.getString("t" + i, "").contains(mTuesday.get(delpos).time() + ";" + mTuesday.get(delpos).title())) {
                        list_todos.add(mPreferences.getString("t" + i, ""));
                    }
                }
                cFiles.removeAlarm(context, mTuesday.get(delpos).title());
                mTuesday.remove(delpos);
                break;
            case "Wednesday":
                //if its not the deleted -> add it to the list
                for (int i = 0; !mPreferences.getString("t" + i, "").equals(""); i++) {
                    if (!mPreferences.getString("t" + i, "").contains(mWednesday.get(delpos).title() + ";" + mWednesday.get(delpos).title())) {
                        list_todos.add(mPreferences.getString("t" + i, ""));
                    }
                }
                cFiles.removeAlarm(context, mWednesday.get(delpos).title());
                mWednesday.remove(delpos);
                break;
            case "Thursday":
                for (int i = 0; !mPreferences.getString("t" + i, "").equals(""); i++) {
                    if (!mPreferences.getString("t" + i, "").contains(mThursday.get(delpos).time() + ";" + mThursday.get(delpos).title())) {
                        list_todos.add(mPreferences.getString("t" + i, ""));
                    }
                }
                cFiles.removeAlarm(context, mThursday.get(delpos).title());
                mThursday.remove(delpos);
                break;
            case "Friday":
                for (int i = 0; !mPreferences.getString("t" + i, "").equals(""); i++) {
                    if (!mPreferences.getString("t" + i, "").contains(mFriday.get(delpos).time() + ";" + mFriday.get(delpos).title())) {
                        list_todos.add(mPreferences.getString("t" + i, ""));
                    }
                }
                cFiles.removeAlarm(context, mFriday.get(delpos).title());
                mFriday.remove(delpos);
                break;
            case "Saturday":
                for (int i = 0; !mPreferences.getString("t" + i, "").equals(""); i++) {
                    if (!mPreferences.getString("t" + i, "").contains(mSaturday.get(delpos).time() + ";" + mSaturday.get(delpos).title())) {
                        list_todos.add(mPreferences.getString("t" + i, ""));
                    }
                }
                cFiles.removeAlarm(context, mSaturday.get(delpos).title());
                mSaturday.remove(delpos);
                break;
            case "Sunday":
                for (int i = 0; !mPreferences.getString("t" + i, "").equals(""); i++) {
                    if (!mPreferences.getString("t" + i, "").contains(mSunday.get(delpos).time() + ";" + mSunday.get(delpos).title())) {
                        list_todos.add(mPreferences.getString("t" + i, ""));
                    }
                }
                cFiles.removeAlarm(context, mSunday.get(delpos).title());
                mSunday.remove(delpos);
                break;
        }
        //save
        for (int i = 0; i < list_todos.size(); i++) {
            mPreferences.edit().putString("t" + i, list_todos.get(i)).apply();
        }
        mPreferences.edit().putString("t" + list_todos.size(), "").apply();

        cFiles.saveWeek(context, weekdate, this);
    }

    public ArrayList<CDate> getMonday(){
        return mMonday;
    }

    public ArrayList<CDate> getTuesday(){
        return mTuesday;
    }

    public ArrayList<CDate> getmWednesday(){
        return mWednesday;
    }

    public ArrayList<CDate> getThursday(){
        return mThursday;
    }

    public ArrayList<CDate> getFriday(){
        return mFriday;
    }

    public ArrayList<CDate> getSaturday(){
        return mSaturday;
    }

    public ArrayList<CDate> getSunday(){
        return mSunday;
    }

    //Notifications
    private void setAlarm(Context context, Calendar c, String createdtitle, String notification) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlertReceiver.class);
        final int id = (int) System.currentTimeMillis();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent, 0);

        // schreibe es in die Alert List
        context.getSharedPreferences("createdtitle", MODE_PRIVATE).edit().putString("createdtitle", createdtitle).apply();

        //new File(context.getFilesDir(), "alerts.txt").delete();
        ArrayList<Alert> alerts = cFiles.loadAlerts(context);

        switch (notification) {
            case "10 minutes before start":
                alerts.add(new Alert(createdtitle, c.getTimeInMillis() + "a"));
                break;
            case "30 minutes before start":
                alerts.add(new Alert(createdtitle, c.getTimeInMillis() + "b"));
                break;
            case "1 hour before start":
                alerts.add(new Alert(createdtitle, c.getTimeInMillis() + "c"));
                break;
            case "1 day before start":
                alerts.add(new Alert(createdtitle, c.getTimeInMillis() + "d"));
                break;
            case "1 week before start":
                alerts.add(new Alert(createdtitle, c.getTimeInMillis() + "e"));
                break;
            default:
                alerts.add(new Alert(createdtitle, c.getTimeInMillis() + "x"));
                break;
        }
        alerts = sortAlerts(alerts);

        cFiles.saveAlerts(context, alerts);

        assert alarmManager != null;
        (alarmManager).setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }

    //Helpers
    private Boolean checkDuplicate(Context context, ArrayList<CDate> Dates, String createdtitle) {
        for (int i = 0; i < Dates.size(); i++) {
            if (Dates.get(i).title().equals(createdtitle)) {
                Toast toast = Toast.makeText(context, "Title already exists", Toast.LENGTH_LONG);
                TextView v = toast.getView().findViewById(android.R.id.message);
                if (v != null) v.setGravity(Gravity.CENTER);
                toast.show();
                return true;
            }
        }
        return false;

    }

    private ArrayList<Alert> sortAlerts(ArrayList<Alert> alerts) {
        int i;
        int k;
        ArrayList<Alert> finalAlerts = new ArrayList<>();

        if (!alerts.isEmpty()) {
            ArrayList<String> newTime = new ArrayList<>();
            for (Alert alert : alerts) {
                newTime.add(alert.time().replace(alert.time().charAt(alert.time().length() - 1), '0'));
            }

            Collections.sort(newTime);

            for (k = 0; k < alerts.size(); k++) {
                //Wenn der Wert anders is
                //if (!Dates.get(i).time().equals(newTime.get(i))) {
                for (i = 0; i < alerts.size(); i++) {
                    //Dann gucke wo der Wert gleich dem anderen entspricht
                    if (alerts.get(i).time().replace(alerts.get(i).time().charAt(alerts.get(i).time().length() - 1), '0').equals(newTime.get(k))) {
                        finalAlerts.add(new Alert(alerts.get(i).title(), alerts.get(i).time()));
                        alerts.set(i, new Alert("", "-1")); //erlaubt Dopplungen
                        break;
                    }
                }
                //}
            }
        }
        return finalAlerts;
    }
}
