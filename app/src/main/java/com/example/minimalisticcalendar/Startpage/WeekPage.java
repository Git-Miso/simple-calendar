package com.example.minimalisticcalendar.Startpage;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.minimalisticcalendar.More.WeekRecyclerAdapter;
import com.example.minimalisticcalendar.More.cFiles;
import com.example.minimalisticcalendar.Notifications.Alert;
import com.example.minimalisticcalendar.Notifications.AlertReceiver;
import com.example.minimalisticcalendar.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class WeekPage {
    Context context;
    private ArrayList<CDate> mMonday;
    private ArrayList<CDate> mTuesday;
    private ArrayList<CDate> mWednesday;
    private ArrayList<CDate> mThursday;
    private ArrayList<CDate> mFriday;
    private ArrayList<CDate> mSaturday;
    private ArrayList<CDate> mSunday;

    private final int KEEP_DATE = -1;

    public WeekPage(Context c, String weekdate, int page, View view) {
        context = c;
        mMonday = new ArrayList<>();
        mTuesday = new ArrayList<>();
        mWednesday = new ArrayList<>();
        mThursday = new ArrayList<>();
        mFriday = new ArrayList<>();
        mSaturday = new ArrayList<>();
        mSunday = new ArrayList<>();

        if (page == 2) { // current week
            deleteOldData(weekdate);
        }

        load(context, weekdate);
        checkForActions(view, weekdate, page);
        initWeekPage(view, page, weekdate);
        setDates(view, weekdate);
        showActionButton(view);
    }

    private void checkForActions(View view, final String weekdate, int page) {
        Bundle extras = ((Activity) context).getIntent().getExtras();
        if (extras != null) {
            String createdTitle = extras.getString("title");
            String createdColor = extras.getString("color");
            String createdNotification = extras.getString("notification");
            String day = extras.getString("day");
            String week = extras.getString("week");
            int delpos = extras.getInt("delpos");

            if (weekdate.equals(week)) {
                if (createdTitle != null) {
                    Integer createdTime = Integer.parseInt(Objects.requireNonNull(extras.getString("time")));
                    createDate(createdTitle, createdTime, createdColor, day, weekdate, createdNotification);
                }

                if (day != null && delpos != KEEP_DATE) {
                    deleteDate(day, delpos, view, weekdate, page);
                }
            }
        }
    }

    private void createDate(String createdtitle, Integer createdtime, String createdcolor, String day, String weekdate, String notification) {
        boolean isExisting = false;
        Calendar c = Calendar.getInstance();
        c.set(Calendar.WEEK_OF_YEAR, Integer.parseInt(weekdate));

        switch (day) {
            case "Monday":
                isExisting = checkDuplicate(mMonday, createdtitle);
                if (!isExisting) {
                    mMonday.add(new CDate(createdtitle, createdtime, createdcolor));
                    c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                }
                break;
            case "Tuesday":
                isExisting = checkDuplicate(mTuesday, createdtitle);
                if (!isExisting) {
                    mTuesday.add(new CDate(createdtitle, createdtime, createdcolor));
                    c.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
                }
                break;
            case "Wednesday":
                isExisting = checkDuplicate(mWednesday, createdtitle);
                if (!isExisting) {
                    mWednesday.add(new CDate(createdtitle, createdtime, createdcolor));
                    c.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
                }
                break;
            case "Thursday":
                isExisting = checkDuplicate(mThursday, createdtitle);
                if (!isExisting) {
                    mThursday.add(new CDate(createdtitle, createdtime, createdcolor));
                    c.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
                }
                break;
            case "Friday":
                isExisting = checkDuplicate(mFriday, createdtitle);
                if (!isExisting) {
                    mFriday.add(new CDate(createdtitle, createdtime, createdcolor));
                    c.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
                }
                break;
            case "Saturday":
                isExisting = checkDuplicate(mSaturday, createdtitle);
                if (!isExisting) {
                    mSaturday.add(new CDate(createdtitle, createdtime, createdcolor));
                    c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                }
                break;
            case "Sunday":
                isExisting = checkDuplicate(mSunday, createdtitle);
                if (!isExisting) {
                    mSunday.add(new CDate(createdtitle, createdtime, createdcolor));
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

            setAlarm(c, createdtitle, createdcolor);
        }

        if (!isExisting && c2.getTimeInMillis() > currenttime) {
            setAlarm(c2, createdtitle, createdcolor);
        }

        ((Activity) context).getIntent().removeExtra("title");
        save(context, weekdate);
    }

    private void deleteDate(String day, int delpos, View view, String weekdate, Integer page) {
        final SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(view.getContext());
        ArrayList<String> list_todos = new ArrayList<>();

        switch (day) {
            case "Monday":
                for (int i = 0; !mPreferences.getString("t" + i, "").equals(""); i++) {
                    if (!mPreferences.getString("t" + i, "").contains(mMonday.get(delpos).title())) {
                        list_todos.add(mPreferences.getString("t" + i, ""));
                    }
                }
                removeAlert(mMonday.get(delpos).title());
                mMonday.remove(delpos);
                break;
            case "Tuesday":
                for (int i = 0; !mPreferences.getString("t" + i, "").equals(""); i++) {
                    if (!mPreferences.getString("t" + i, "").contains(mTuesday.get(delpos).time() + ";" + mTuesday.get(delpos).title())) {
                        list_todos.add(mPreferences.getString("t" + i, ""));
                    }
                }
                removeAlert(mTuesday.get(delpos).title());
                mTuesday.remove(delpos);
                break;
            case "Wednesday":
                //if its not the deleted -> add it to the list
                for (int i = 0; !mPreferences.getString("t" + i, "").equals(""); i++) {
                    if (!mPreferences.getString("t" + i, "").contains(mWednesday.get(delpos).title() + ";" + mWednesday.get(delpos).title())) {
                        list_todos.add(mPreferences.getString("t" + i, ""));
                    }
                }
                removeAlert(mWednesday.get(delpos).title());
                mWednesday.remove(delpos);
                break;
            case "Thursday":
                for (int i = 0; !mPreferences.getString("t" + i, "").equals(""); i++) {
                    if (!mPreferences.getString("t" + i, "").contains(mThursday.get(delpos).time() + ";" + mThursday.get(delpos).title())) {
                        list_todos.add(mPreferences.getString("t" + i, ""));
                    }
                }
                removeAlert(mThursday.get(delpos).title());
                mThursday.remove(delpos);
                break;
            case "Friday":
                for (int i = 0; !mPreferences.getString("t" + i, "").equals(""); i++) {
                    if (!mPreferences.getString("t" + i, "").contains(mFriday.get(delpos).time() + ";" + mFriday.get(delpos).title())) {
                        list_todos.add(mPreferences.getString("t" + i, ""));
                    }
                }
                removeAlert(mFriday.get(delpos).title());
                mFriday.remove(delpos);
                break;
            case "Saturday":
                for (int i = 0; !mPreferences.getString("t" + i, "").equals(""); i++) {
                    if (!mPreferences.getString("t" + i, "").contains(mSaturday.get(delpos).time() + ";" + mSaturday.get(delpos).title())) {
                        list_todos.add(mPreferences.getString("t" + i, ""));
                    }
                }
                removeAlert(mSaturday.get(delpos).title());
                mSaturday.remove(delpos);
                break;
            case "Sunday":
                for (int i = 0; !mPreferences.getString("t" + i, "").equals(""); i++) {
                    if (!mPreferences.getString("t" + i, "").contains(mSunday.get(delpos).time() + ";" + mSunday.get(delpos).title())) {
                        list_todos.add(mPreferences.getString("t" + i, ""));
                    }
                }
                removeAlert(mSunday.get(delpos).title());
                mSunday.remove(delpos);
                break;
        }
        //save
        for (int i = 0; i < list_todos.size(); i++) {
            mPreferences.edit().putString("t" + i, list_todos.get(i)).apply();
        }
        mPreferences.edit().putString("t" + list_todos.size(), "").apply();

        save(context, weekdate);
        initWeekPage(view, page, weekdate);
        ((Activity) context).getIntent().removeExtra("day");
    }


    private void initWeekPage(final View view, int page, final String weekdate) {
        initWeekTitle(view, page);

        ArrayList<RecyclerView> Days = new ArrayList<>();
        Days.add((RecyclerView) view.findViewById(R.id.monday_rec));
        Days.add((RecyclerView) view.findViewById(R.id.tuesday_rec));
        Days.add((RecyclerView) view.findViewById(R.id.wednesday_rec));
        Days.add((RecyclerView) view.findViewById(R.id.thursday_rec));
        Days.add((RecyclerView) view.findViewById(R.id.friday_rec));
        Days.add((RecyclerView) view.findViewById(R.id.saturday_rec));
        Days.add((RecyclerView) view.findViewById(R.id.sunday_rec));

        for (RecyclerView day : Days) {
            final WeekRecyclerAdapter adapter;
            switch (Days.indexOf(day)) {
                default:
                    adapter = new WeekRecyclerAdapter(context, mMonday, weekdate, "Monday");
                    break;
                case 1:
                    adapter = new WeekRecyclerAdapter(context, mTuesday, weekdate, "Tuesday");
                    break;
                case 2:
                    adapter = new WeekRecyclerAdapter(context, mWednesday, weekdate, "Wednesday");
                    break;
                case 3:
                    adapter = new WeekRecyclerAdapter(context, mThursday, weekdate, "Thursday");
                    break;
                case 4:
                    adapter = new WeekRecyclerAdapter(context, mFriday, weekdate, "Friday");
                    break;
                case 5:
                    adapter = new WeekRecyclerAdapter(context, mSaturday, weekdate, "Saturday");
                    break;
                case 6:
                    adapter = new WeekRecyclerAdapter(context, mSunday, weekdate, "Sunday");
                    break;
            }
            day.setAdapter(adapter);
            day.setLayoutManager(new LinearLayoutManager(context));
        }

        initRecyclingHeight(Days, view);

        initAddButtons(view, weekdate);
    }

    private void initWeekTitle(View view, Integer page) {
        TextView title = view.findViewById(R.id.textView6);
        String title_string;

        switch (page) {
            default:
                title_string = page - 2 + "th week";
                break;
            case 2:
                title_string = "Current week";
                break;
            case 3:
                title_string = "Next week";
                break;
            case 4:
                title_string = "2nd week";
                break;
            case 5:
                title_string = "3rd week";
                break;
        }
        title.setText(title_string);
    }

    //Sets the Height if you habe no dates. Design only
    private void initRecyclingHeight(ArrayList<RecyclerView> days, View view) {
        int fortytwo = DPtoPixel(view, 42);

        for (RecyclerView day : days) {
            switch (days.indexOf(day)) {
                case 0:
                    if (mMonday.size() > 0) {
                        day.setMinimumHeight(mMonday.size() * fortytwo);
                    }
                    break;
                case 1:
                    if (mTuesday.size() > 0) {
                        day.setMinimumHeight(mTuesday.size() * fortytwo);
                    }
                    break;
                case 2:
                    if (mWednesday.size() > 0) {
                        day.setMinimumHeight(mWednesday.size() * fortytwo);
                    }
                    break;
                case 3:
                    if (mThursday.size() > 0) {
                        day.setMinimumHeight(mThursday.size() * fortytwo);
                    }
                    break;
                case 4:
                    if (mFriday.size() > 0) {
                        day.setMinimumHeight(mFriday.size() * fortytwo);
                    }
                    break;
                case 5:
                    if (mSaturday.size() > 0) {
                        day.setMinimumHeight(mSaturday.size() * fortytwo);
                    }
                    break;
                case 6:
                    if (mSunday.size() > 0) {
                        day.setMinimumHeight(mSunday.size() * fortytwo);
                    }
                    break;
            }
        }
    }

    private void initAddButtons(View view, final String weekdate) {
        FloatingActionButton add1 = view.findViewById(R.id.design_add);
        add1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Create_New(context, "Monday", weekdate);
            }
        });
        FloatingActionButton add2 = view.findViewById(R.id.design_add2);
        add2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Create_New(context, "Tuesday", weekdate);
            }
        });
        FloatingActionButton add3 = view.findViewById(R.id.design_add3);
        add3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Create_New(context, "Wednesday", weekdate);
            }
        });
        FloatingActionButton add4 = view.findViewById(R.id.design_add4);
        add4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Create_New(context, "Thursday", weekdate);
            }
        });
        FloatingActionButton add5 = view.findViewById(R.id.design_add5);
        add5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Create_New(context, "Friday", weekdate);
            }
        });
        FloatingActionButton add6 = view.findViewById(R.id.design_add6);
        add6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Create_New(context, "Saturday", weekdate);
            }
        });
        FloatingActionButton add7 = view.findViewById(R.id.design_add7);
        add7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Create_New(context, "Sunday", weekdate);
            }
        });
    }


    private void setDates(View view, final String weekdate) {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        int weekdateint = Integer.parseInt(weekdate);
        TextView textmon = view.findViewById(R.id.montext);
        TextView texttues = view.findViewById(R.id.tuestext);
        TextView textwed = view.findViewById(R.id.wedtext);
        TextView textthurs = view.findViewById(R.id.thurstext);
        TextView textfri = view.findViewById(R.id.fritext);
        TextView textsat = view.findViewById(R.id.sattext);
        TextView textsun = view.findViewById(R.id.suntext);

        int currentweekdate = Integer.parseInt(new SimpleDateFormat("w", Locale.ENGLISH).format(new Date()));
        if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            currentweekdate -= 1;
        }

        if (weekdateint == currentweekdate) {
            //is marking the current day with a red background
            switch (day) {
                case Calendar.MONDAY:
                    textmon.setBackgroundResource(R.drawable.design_roundedbutton_red);
                    break;
                case Calendar.TUESDAY:
                    texttues.setBackgroundResource(R.drawable.design_roundedbutton_red);
                    break;
                case Calendar.WEDNESDAY:
                    textwed.setBackgroundResource(R.drawable.design_roundedbutton_red);
                    break;
                case Calendar.THURSDAY:
                    textthurs.setBackgroundResource(R.drawable.design_roundedbutton_red);
                    break;
                case Calendar.FRIDAY:
                    textfri.setBackgroundResource(R.drawable.design_roundedbutton_red);
                    break;
                case Calendar.SATURDAY:
                    textsat.setBackgroundResource(R.drawable.design_roundedbutton_red);
                    break;
                case Calendar.SUNDAY:
                    textsun.setBackgroundResource(R.drawable.design_roundedbutton_red);
                    break;
            }
        }

        //The rest will set the date to each day
        //find the correct week
        while (weekdateint != currentweekdate) {
            weekdateint -= 1;
            calendar.add(Calendar.DAY_OF_MONTH, 7);
        }

        switch (day) {
            case Calendar.TUESDAY:
                calendar.add(Calendar.DAY_OF_MONTH, -1); //get Monday as Day
                break;
            case Calendar.WEDNESDAY:
                calendar.add(Calendar.DAY_OF_MONTH, -2); //get Monday as Day
                break;
            case Calendar.THURSDAY:
                calendar.add(Calendar.DAY_OF_MONTH, -3); //get Monday as Day
                break;
            case Calendar.FRIDAY:
                calendar.add(Calendar.DAY_OF_MONTH, -4); //get Monday as Day
                break;
            case Calendar.SATURDAY:
                calendar.add(Calendar.DAY_OF_MONTH, -5); //get Monday as Day
                break;
            case Calendar.SUNDAY:
                calendar.add(Calendar.DAY_OF_MONTH, -6); //get Monday as Day
                break;
        }


        //Set the correct date
        textmon.setText(context.getString(R.string.setdate, "Monday", calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1));
        calendar.add(Calendar.DAY_OF_WEEK, 1);
        texttues.setText(context.getString(R.string.setdate, "Tuesday", calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1));
        calendar.add(Calendar.DAY_OF_WEEK, 1);
        textwed.setText(context.getString(R.string.setdate, "Wednesday", calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1));
        calendar.add(Calendar.DAY_OF_WEEK, 1);
        textthurs.setText(context.getString(R.string.setdate, "Thursday", calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1));
        calendar.add(Calendar.DAY_OF_WEEK, 1);
        textfri.setText(context.getString(R.string.setdate, "Friday", calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1));
        calendar.add(Calendar.DAY_OF_WEEK, 1);
        textsat.setText(context.getString(R.string.setdate, "Saturday", calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1));
        calendar.add(Calendar.DAY_OF_WEEK, 1);
        textsun.setText(context.getString(R.string.setdate, "Sunday", calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1));
    }

    private void showActionButton(View view) {
        FloatingActionButton add_specific = view.findViewById(R.id.add_specific_date);
        add_specific.setVisibility(View.VISIBLE);
        add_specific.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToSpecificDay();
            }
        });
    }


    private void addToSpecificDay() {

        final Calendar c = Calendar.getInstance();
        final int gday = c.get(Calendar.DAY_OF_MONTH);
        final int gmonth = c.get(Calendar.MONTH);
        final int gyear = c.get(Calendar.YEAR);

        DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                c.set(Calendar.YEAR, year);
                c.set(Calendar.MONTH, month);
                c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                c.set(Calendar.HOUR_OF_DAY, 23);
                c.set(Calendar.MINUTE, 59);

                if (!c.getTime().before(Calendar.getInstance().getTime())) {
                    addspecday_dialog(c);
                } else {
                    Toast.makeText(context, "Select a date in the future", Toast.LENGTH_SHORT).show();
                }
            }
        };

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, R.style.Theme_AppCompat_Light_Dialog_MinWidth, mDateSetListener, gyear, gmonth, gday);
        datePickerDialog.show();


    }

    private void Create_New(final Context mContext, final String day, final String weekdate) {
        final Dialog myDialog = new Dialog(mContext);
        myDialog.setContentView(R.layout.add_new_dialog);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button addbtn = myDialog.findViewById(R.id.addbtn);
        final EditText titletext = myDialog.findViewById(R.id.titletext);
        final EditText timetext = myDialog.findViewById(R.id.timetext);
        final EditText timetext2 = myDialog.findViewById(R.id.timetext2);
        timetext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    if (s.length() > 1 || Integer.parseInt(s.toString()) > 2) {
                        timetext2.requestFocus();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //Button colorbtn = myDialog.findViewById(R.id.colorbtn);
        Button colorbtn = myDialog.findViewById(R.id.textView);
        myDialog.findViewById(R.id.textView).setTag("blue"); //Standartfarbe blau
        myDialog.findViewById(R.id.textView).setTag(R.id.slider, "no notification");

        colorbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog myColorDialog = new Dialog(mContext);
                myColorDialog.setContentView(R.layout.colorspinner);
                myColorDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                myColorDialog.show();
                setColorAtOpening(myDialog.findViewById(R.id.textView).getTag().toString(), myColorDialog);

                FloatingActionButton close = myColorDialog.findViewById(R.id.closeColorDialog);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myColorDialog.dismiss();
                    }
                });

                SeekBar slider = myColorDialog.findViewById(R.id.slider);
                final TextView sliderText = myColorDialog.findViewById(R.id.sliderText);
                slider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        String notification = "no notification";
                        switch (progress) {
                            case 0:
                                notification = "no notification";
                                break;
                            case 1:
                                notification = "10 minutes before start";
                                break;
                            case 2:
                                notification = "30 minutes before start";
                                break;
                            case 3:
                                notification = "1 hour before start";
                                break;
                            case 4:
                                notification = "1 day before start";
                                break;
                            case 5:
                                notification = "1 week before start";
                                break;
                        }
                        sliderText.setText(notification);
                        myDialog.findViewById(R.id.textView).setTag(R.id.slider, notification);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
                myColorDialog.findViewById(R.id.red).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialog.findViewById(R.id.textView).setBackgroundResource(R.drawable.roundedbutton_red);
                        resetColorSelection(myColorDialog);
                        myDialog.findViewById(R.id.textView).setTag("red");
                        myColorDialog.findViewById(R.id.red).setAlpha(0.5F);
                        myColorDialog.findViewById(R.id.red).setForeground(context.getDrawable(R.drawable.ic_check_black_24dp));
                    }
                });
                myColorDialog.findViewById(R.id.blue).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialog.findViewById(R.id.textView).setBackgroundResource(R.drawable.roundedbutton_blue);
                        myDialog.findViewById(R.id.textView).setTag("blue");
                        resetColorSelection(myColorDialog);
                        myColorDialog.findViewById(R.id.blue).setAlpha(0.5F);
                        myColorDialog.findViewById(R.id.blue).setForeground(context.getDrawable(R.drawable.ic_check_black_24dp));
                    }
                });
                myColorDialog.findViewById(R.id.green).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialog.findViewById(R.id.textView).setBackgroundResource(R.drawable.roundedbutton_green);
                        myDialog.findViewById(R.id.textView).setTag("green");
                        resetColorSelection(myColorDialog);
                        myColorDialog.findViewById(R.id.green).setAlpha(0.5F);
                        myColorDialog.findViewById(R.id.green).setForeground(context.getDrawable(R.drawable.ic_check_black_24dp));
                    }
                });
                myColorDialog.findViewById(R.id.dark).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialog.findViewById(R.id.textView).setBackgroundResource(R.drawable.roundedbutton_dark);
                        myDialog.findViewById(R.id.textView).setTag("dark");
                        resetColorSelection(myColorDialog);
                        myColorDialog.findViewById(R.id.dark).setAlpha(0.5F);
                        myColorDialog.findViewById(R.id.dark).setForeground(context.getDrawable(R.drawable.ic_check_black_24dp));
                    }
                });
                myColorDialog.findViewById(R.id.orange).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialog.findViewById(R.id.textView).setBackgroundResource(R.drawable.roundedbutton_orange);
                        myDialog.findViewById(R.id.textView).setTag("orange");
                        resetColorSelection(myColorDialog);
                        myColorDialog.findViewById(R.id.orange).setAlpha(0.5F);
                        myColorDialog.findViewById(R.id.orange).setForeground(context.getDrawable(R.drawable.ic_check_black_24dp));
                    }
                });
                myColorDialog.findViewById(R.id.yellow).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialog.findViewById(R.id.textView).setBackgroundResource(R.drawable.roundedbutton_yellow);
                        myDialog.findViewById(R.id.textView).setTag("yellow");
                    }
                });
            }
        });

        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titletext.getText().toString();
                String time1 = timetext.getText().toString();
                String time2 = timetext2.getText().toString();

                time1 = checkTimeInput(time1, "10");
                time2 = checkTimeInput(time2, "00");

                if (!title.isEmpty() && Integer.parseInt(time1) < 24 && Integer.parseInt(time1) > 0 && Integer.parseInt(time2) < 61) {
                    Intent intent = new Intent(mContext, StartpageBackground.class);
                    title = title.replace(",", "");
                    title = title.replace(";", "");
                    if (title.endsWith("+") || title.endsWith("-")) {
                        title = title.replace("+", "");
                        title = title.replace("-", "");
                    }
                    String time = (time1 + time2);

                    //create(title, Integer.parseInt(time), myDialog.findViewById(R.id.textView).getTag().toString(), day, adapter, weekdate);
                    myDialog.dismiss();
                    intent.putExtra("title", title);
                    intent.putExtra("time", time);
                    intent.putExtra("day", day);
                    intent.putExtra("notification", myDialog.findViewById(R.id.textView).getTag(R.id.slider).toString());
                    intent.putExtra("color", myDialog.findViewById(R.id.textView).getTag().toString());
                    intent.putExtra("delpos", KEEP_DATE);
                    intent.putExtra("week", weekdate);
                    mContext.startActivity(intent);
                    ((Activity) context).overridePendingTransition(0, 0);
                }
            }
        });
        myDialog.show();
    }

    private void addspecday_dialog(final Calendar c) {

        final Dialog myDialog = new Dialog(context);
        myDialog.setContentView(R.layout.add_new_dialog);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();

        final EditText titletext = myDialog.findViewById(R.id.titletext);
        final EditText timetext = myDialog.findViewById(R.id.timetext);
        final EditText timetext2 = myDialog.findViewById(R.id.timetext2);
        Button colorbtn = myDialog.findViewById(R.id.textView);
        Button addbtn = myDialog.findViewById(R.id.addbtn);

        //keep writing
        timetext.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    if (s.length() > 1 || Integer.parseInt(s.toString()) > 2) {
                        timetext2.requestFocus();
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        myDialog.findViewById(R.id.textView).setTag("blue"); //Default color is blue
        myDialog.findViewById(R.id.textView).setTag(R.id.slider, "no notification");

        colorbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog myColorDialog = new Dialog(context);
                myColorDialog.setContentView(R.layout.colorspinner);
                myColorDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                myColorDialog.show();
                setColorAtOpening(myDialog.findViewById(R.id.textView).getTag().toString(), myColorDialog);

                FloatingActionButton close = myColorDialog.findViewById(R.id.closeColorDialog);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myColorDialog.dismiss();
                    }
                });

                SeekBar slider = myColorDialog.findViewById(R.id.slider);
                final TextView sliderText = myColorDialog.findViewById(R.id.sliderText);
                slider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        String notification = "no notification";
                        switch (progress) {
                            case 0:
                                notification = "no notification";
                                break;
                            case 1:
                                notification = "10 minutes before start";
                                break;
                            case 2:
                                notification = "30 minutes before start";
                                break;
                            case 3:
                                notification = "1 hour before start";
                                break;
                            case 4:
                                notification = "1 day before start";
                                break;
                            case 5:
                                notification = "1 week before start";
                                break;
                        }
                        sliderText.setText(notification);
                        myDialog.findViewById(R.id.textView).setTag(R.id.slider, notification);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
                myColorDialog.findViewById(R.id.red).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialog.findViewById(R.id.textView).setBackgroundResource(R.drawable.roundedbutton_red);
                        resetColorSelection(myColorDialog);
                        myDialog.findViewById(R.id.textView).setTag("red");
                        myColorDialog.findViewById(R.id.red).setAlpha(0.5F);
                        myColorDialog.findViewById(R.id.red).setForeground(context.getDrawable(R.drawable.ic_check_black_24dp));
                    }
                });
                myColorDialog.findViewById(R.id.blue).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialog.findViewById(R.id.textView).setBackgroundResource(R.drawable.roundedbutton_blue);
                        myDialog.findViewById(R.id.textView).setTag("blue");
                        resetColorSelection(myColorDialog);
                        myColorDialog.findViewById(R.id.blue).setAlpha(0.5F);
                        myColorDialog.findViewById(R.id.blue).setForeground(context.getDrawable(R.drawable.ic_check_black_24dp));
                    }
                });
                myColorDialog.findViewById(R.id.green).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialog.findViewById(R.id.textView).setBackgroundResource(R.drawable.roundedbutton_green);
                        myDialog.findViewById(R.id.textView).setTag("green");
                        resetColorSelection(myColorDialog);
                        myColorDialog.findViewById(R.id.green).setAlpha(0.5F);
                        myColorDialog.findViewById(R.id.green).setForeground(context.getDrawable(R.drawable.ic_check_black_24dp));
                    }
                });
                myColorDialog.findViewById(R.id.dark).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialog.findViewById(R.id.textView).setBackgroundResource(R.drawable.roundedbutton_dark);
                        myDialog.findViewById(R.id.textView).setTag("dark");
                        resetColorSelection(myColorDialog);
                        myColorDialog.findViewById(R.id.dark).setAlpha(0.5F);
                        myColorDialog.findViewById(R.id.dark).setForeground(context.getDrawable(R.drawable.ic_check_black_24dp));
                    }
                });
                myColorDialog.findViewById(R.id.orange).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialog.findViewById(R.id.textView).setBackgroundResource(R.drawable.roundedbutton_orange);
                        myDialog.findViewById(R.id.textView).setTag("orange");
                        resetColorSelection(myColorDialog);
                        myColorDialog.findViewById(R.id.orange).setAlpha(0.5F);
                        myColorDialog.findViewById(R.id.orange).setForeground(context.getDrawable(R.drawable.ic_check_black_24dp));
                    }
                });
                myColorDialog.findViewById(R.id.yellow).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialog.findViewById(R.id.textView).setBackgroundResource(R.drawable.roundedbutton_yellow);
                        myDialog.findViewById(R.id.textView).setTag("yellow");
                    }
                });
            }
        });

        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titletext.getText().toString();
                String time1 = timetext.getText().toString();
                String time2 = timetext2.getText().toString();

                time1 = checkTimeInput(time1, "10");
                time2 = checkTimeInput(time2, "00");
                if (!title.isEmpty() && Integer.parseInt(time1) < 24 && (Integer.parseInt(time1) + Integer.parseInt(time2)) > 0 && Integer.parseInt(time2) < 61) {
                    Intent intent = new Intent(context, StartpageBackground.class);
                    //catch errors
                    title = title.replace(";", "");
                    if (title.endsWith("+") || title.endsWith("-")) {
                        title = title.replace("+", "");
                        title = title.replace("-", "");
                    }
                    String complete_time = (time1 + time2);

                    switch (c.get(Calendar.DAY_OF_WEEK)) {
                        case 2:
                            intent.putExtra("day", "Monday");
                            break;
                        case 3:
                            intent.putExtra("day", "Tuesday");
                            break;
                        case 4:
                            intent.putExtra("day", "Wednesday");
                            break;
                        case 5:
                            intent.putExtra("day", "Thursday");
                            break;
                        case 6:
                            intent.putExtra("day", "Friday");
                            break;
                        case 7:
                            intent.putExtra("day", "Saturday");
                            break;
                        case 1:
                            intent.putExtra("day", "Sunday");
                            break;
                    }

                    intent.putExtra("title", title);
                    intent.putExtra("time", complete_time);
                    intent.putExtra("color", myDialog.findViewById(R.id.textView).getTag().toString());
                    intent.putExtra("notification", myDialog.findViewById(R.id.textView).getTag(R.id.slider).toString());
                    intent.putExtra("delpos", KEEP_DATE);
                    intent.putExtra("week", String.valueOf(c.get(Calendar.WEEK_OF_YEAR)));
                    context.startActivity(intent);
                    myDialog.dismiss();
                }
            }
        });
        myDialog.show();
    }

    private void deleteOldData(String weekdate) {
        FileInputStream fis = null;
        String FILE_NAME = Integer.parseInt(weekdate) - 2 + ".txt";
        try {
            fis = context.openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String text;

            while ((text = br.readLine()) != null) {
                String[] newtext = text.split(",");
                removeAlert(newtext[1]);
            }
        } catch (
                IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        new File(context.getFilesDir(), (Integer.parseInt(weekdate) - 2) + ".txt").delete();
    }

    private void setColorAtOpening(String color, Dialog myColorDialog) {
        switch (color) {
            case "red":
                myColorDialog.findViewById(R.id.red).setAlpha(0.5F);
                myColorDialog.findViewById(R.id.red).setForeground(context.getDrawable(R.drawable.ic_check_black_24dp));
                break;
            case "blue":
                myColorDialog.findViewById(R.id.blue).setAlpha(0.5F);
                myColorDialog.findViewById(R.id.blue).setForeground(context.getDrawable(R.drawable.ic_check_black_24dp));
                break;
            case "green":
                myColorDialog.findViewById(R.id.green).setAlpha(0.5F);
                myColorDialog.findViewById(R.id.green).setForeground(context.getDrawable(R.drawable.ic_check_black_24dp));
                break;
            case "orange":
                myColorDialog.findViewById(R.id.orange).setAlpha(0.5F);
                myColorDialog.findViewById(R.id.orange).setForeground(context.getDrawable(R.drawable.ic_check_black_24dp));
                break;
            case "dark":
                myColorDialog.findViewById(R.id.dark).setAlpha(0.5F);
                myColorDialog.findViewById(R.id.dark).setForeground(context.getDrawable(R.drawable.ic_check_black_24dp));
                break;
        }
    }

    //FileManager
    private void save(Context context, String weekdate) {
        String FILE_NAME = weekdate + ".txt";
        ArrayList<String> text = new ArrayList<>();

        sortevery();

        for (int i = 0; i < mMonday.size(); i++) {
            text.add("Monday" + "," + mMonday.get(i).title() + "," + mMonday.get(i).time() + "," + mMonday.get(i).color() + "\n");
        }
        for (int i = 0; i < mTuesday.size(); i++) {
            text.add("Tuesday" + "," + mTuesday.get(i).title() + "," + mTuesday.get(i).time() + "," + mTuesday.get(i).color() + "\n");
        }
        for (int i = 0; i < mWednesday.size(); i++) {
            text.add("Wednesday" + "," + mWednesday.get(i).title() + "," + mWednesday.get(i).time() + "," + mWednesday.get(i).color() + "\n");
        }
        for (int i = 0; i < mThursday.size(); i++) {
            text.add("Thursday" + "," + mThursday.get(i).title() + "," + mThursday.get(i).time() + "," + mThursday.get(i).color() + "\n");
        }
        for (int i = 0; i < mFriday.size(); i++) {
            text.add("Friday" + "," + mFriday.get(i).title() + "," + mFriday.get(i).time() + "," + mFriday.get(i).color() + "\n");
        }
        for (int i = 0; i < mSaturday.size(); i++) {
            text.add("Saturday" + "," + mSaturday.get(i).title() + "," + mSaturday.get(i).time() + "," + mSaturday.get(i).color() + "\n");
        }
        for (int i = 0; i < mSunday.size(); i++) {
            text.add("Sunday" + "," + mSunday.get(i).title() + "," + mSunday.get(i).time() + "," + mSunday.get(i).color() + "\n");
        }
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(FILE_NAME, MODE_PRIVATE);
            for (int i = 0; i < mMonday.size() + mTuesday.size() + mWednesday.size() + mThursday.size() + mFriday.size() + mSaturday.size() + mSunday.size(); i++) {
                fos.write(text.get(i).getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void load(Context context, String weekdate) {
        FileInputStream fis = null;
        String FILE_NAME = weekdate + ".txt";

        try {
            fis = context.openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String text;

            while ((text = br.readLine()) != null) {
                String[] newtext = text.split(",");
                switch (newtext[0]) {
                    case "Monday":
                        mMonday.add(new CDate(newtext[1], Integer.parseInt(newtext[2]), newtext[3]));
                        break;
                    case "Tuesday":
                        mTuesday.add(new CDate(newtext[1], Integer.parseInt(newtext[2]), newtext[3]));
                        break;
                    case "Wednesday":
                        mWednesday.add(new CDate(newtext[1], Integer.parseInt(newtext[2]), newtext[3]));
                        break;
                    case "Thursday":
                        mThursday.add(new CDate(newtext[1], Integer.parseInt(newtext[2]), newtext[3]));
                        break;
                    case "Friday":
                        mFriday.add(new CDate(newtext[1], Integer.parseInt(newtext[2]), newtext[3]));
                        break;
                    case "Saturday":
                        mSaturday.add(new CDate(newtext[1], Integer.parseInt(newtext[2]), newtext[3]));
                        break;
                    case "Sunday":
                        mSunday.add(new CDate(newtext[1], Integer.parseInt(newtext[2]), newtext[3]));
                        break;
                }
            }
        } catch (
                IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //Notifications
    private void setAlarm(Calendar c, String createdtitle, String notification) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlertReceiver.class);
        final int id = (int) System.currentTimeMillis();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent, 0);

        // schreibe es in die Alert List
        context.getSharedPreferences("createdtitle", MODE_PRIVATE).edit().putString("createdtitle", createdtitle).apply();

        //new File(context.getFilesDir(), "alerts.txt").delete();
        ArrayList<Alert> alerts = cFiles.loadAlerts(context);

        // add + for one hour, "" for on beginn and - for one day
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

        ArrayList<Alert> finalAlerts = new ArrayList<>();

        for (Alert i : alerts) {
            if (!i.title().equals("--deleted--")) {
                finalAlerts.add(i);
            }
        }

        cFiles.saveAlerts(context, finalAlerts);

        (alarmManager).setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }

    private void removeAlert(String title) {
        ArrayList<Alert> alertslist = cFiles.loadAlerts(context);
        ArrayList<Alert> finalAlerts = new ArrayList<>();

        for (Alert alert : alertslist) {
            if (!alert.title().equals(title)) {
                finalAlerts.add(alert);
            }
        }

        cFiles.saveAlerts(context, finalAlerts);
    }

    //Helpers
    private Integer DPtoPixel(View view, Integer dp) {
        final float scale = view.getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    private void resetColorSelection(Dialog myColorDialog) {
        myColorDialog.findViewById(R.id.red).setAlpha(1F);
        myColorDialog.findViewById(R.id.blue).setAlpha(1F);
        myColorDialog.findViewById(R.id.green).setAlpha(1F);
        myColorDialog.findViewById(R.id.dark).setAlpha(1F);
        myColorDialog.findViewById(R.id.orange).setAlpha(1F);
        myColorDialog.findViewById(R.id.red).setForeground(null);
        myColorDialog.findViewById(R.id.blue).setForeground(null);
        myColorDialog.findViewById(R.id.green).setForeground(null);
        myColorDialog.findViewById(R.id.dark).setForeground(null);
        myColorDialog.findViewById(R.id.orange).setForeground(null);
    }

    private String checkTimeInput(String time, String standard) {
        time = time.length() == 0 ? standard : time.length() == 1 ? "0" + time : time;
        return time;
    }

    // Sorting and Lists
    private void sortevery() {
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
            ArrayList<Integer> newTime = new ArrayList<>();
            for (i = 0; i < Dates.size(); i++) {
                newTime.add(Dates.get(i).time());
            }
            ArrayList<String> newTitle = new ArrayList<>();
            for (i = 0; i < Dates.size(); i++) {
                newTitle.add(Dates.get(i).title());
            }
            ArrayList<String> newColor = new ArrayList<>();
            for (i = 0; i < Dates.size(); i++) {
                newColor.add(Dates.get(i).color());
            }

            Collections.sort(newTime);

            for (k = 0; k < Dates.size(); k++) {
                //Wenn der Wert anders is
                //if (!Dates.get(i).time().equals(newTime.get(i))) {
                for (i = 0; i < Dates.size(); i++) {
                    //Dann gucke wo der Wert gleich dem anderen entspricht
                    if (Dates.get(i).time().equals(newTime.get(k))) {
                        finalDates.add(new CDate(newTitle.get(i), newTime.get(k), newColor.get(i)));
                        Dates.set(i, new CDate("", -1, "")); //erlaubt Dopplungen
                        break;
                    }
                }
                //}
            }
        }
        return finalDates;
    }

    private ArrayList<Alert> sortAlerts(ArrayList<Alert> alerts) {
        int i;
        int k;
        ArrayList<Alert> finalAlerts = new ArrayList<>();

        if (!alerts.isEmpty()) {
            ArrayList<String> newTime = new ArrayList<>();
            ArrayList<String> newTitle = new ArrayList<>();
            for (Alert alert : alerts) {
                newTime.add(alert.time().replace(alert.time().charAt(alert.time().length() - 1), '0'));
                newTitle.add(alert.title());
            }

            Collections.sort(newTime);

            for (k = 0; k < alerts.size(); k++) {
                //Wenn der Wert anders is
                //if (!Dates.get(i).time().equals(newTime.get(i))) {
                for (i = 0; i < alerts.size(); i++) {
                    //Dann gucke wo der Wert gleich dem anderen entspricht
                    if (alerts.get(i).time().replace(alerts.get(i).time().charAt(alerts.get(i).time().length() - 1), '0').equals(newTime.get(k))) {
                        finalAlerts.add(new Alert(newTitle.get(i), newTime.get(k)));
                        alerts.set(i, new Alert("", "-1")); //erlaubt Dopplungen
                        break;
                    }
                }
                //}
            }
        }
        return finalAlerts;
    }

    private Boolean checkDuplicate(ArrayList<CDate> Dates, String createdtitle) {
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

}
