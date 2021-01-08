package com.example.minimalisticcalendar.Startpage;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.example.minimalisticcalendar.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class WeekPage {
    Context context;
    private CWeek mWeek;

    private final int KEEP_DATE = -1;

    public WeekPage(Context c, String weekdate, int page, View view) {
        context = c;

        mWeek = cFiles.loadWeek(context, weekdate);
        checkForActions(view, weekdate, page);
        initWeekPage(view, page, weekdate);
        setDates(view, weekdate);
        showActionButton(view);
    }

    private void checkForActions(View view, final String weekdate, int page) {
        Bundle extras = ((Activity) context).getIntent().getExtras();
        if (extras != null) {
            String createdTitle = extras.getString("title");
            String createdDesc = extras.getString("desc");
            String createdColor = extras.getString("color");
            String createdNotification = extras.getString("notification");
            String day = extras.getString("day");
            String week = extras.getString("week");
            int delpos = extras.getInt("delpos");

            if (weekdate.equals(week)) {
                if (createdTitle != null) {
                    Integer createdTime = Integer.parseInt(Objects.requireNonNull(extras.getString("time")));
                    mWeek.addDate(context, day, weekdate, createdTitle, createdDesc, createdTime, createdColor, createdNotification);
                    ((Activity) context).getIntent().removeExtra("title");
                    cFiles.saveWeek(context, weekdate, mWeek);
                }

                if (day != null && delpos != KEEP_DATE) {
                    mWeek.deleteDate(context, day, delpos, view, weekdate);
                    initWeekPage(view, page, weekdate);
                    ((Activity) context).getIntent().removeExtra("day");
                }
            }
        }
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
                    adapter = new WeekRecyclerAdapter(context, mWeek.getMonday(), weekdate, "Monday");
                    break;
                case 1:
                    adapter = new WeekRecyclerAdapter(context, mWeek.getTuesday(), weekdate, "Tuesday");
                    break;
                case 2:
                    adapter = new WeekRecyclerAdapter(context, mWeek.getmWednesday(), weekdate, "Wednesday");
                    break;
                case 3:
                    adapter = new WeekRecyclerAdapter(context, mWeek.getThursday(), weekdate, "Thursday");
                    break;
                case 4:
                    adapter = new WeekRecyclerAdapter(context, mWeek.getFriday(), weekdate, "Friday");
                    break;
                case 5:
                    adapter = new WeekRecyclerAdapter(context, mWeek.getSaturday(), weekdate, "Saturday");
                    break;
                case 6:
                    adapter = new WeekRecyclerAdapter(context, mWeek.getSunday(), weekdate, "Sunday");
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
                    if (mWeek.getMonday().size() > 0) {
                        day.setMinimumHeight(mWeek.getMonday().size() * fortytwo);
                    }
                    break;
                case 1:
                    if (mWeek.getTuesday().size() > 0) {
                        day.setMinimumHeight(mWeek.getTuesday().size() * fortytwo);
                    }
                    break;
                case 2:
                    if (mWeek.getmWednesday().size() > 0) {
                        day.setMinimumHeight(mWeek.getmWednesday().size() * fortytwo);
                    }
                    break;
                case 3:
                    if (mWeek.getThursday().size() > 0) {
                        day.setMinimumHeight(mWeek.getThursday().size() * fortytwo);
                    }
                    break;
                case 4:
                    if (mWeek.getFriday().size() > 0) {
                        day.setMinimumHeight(mWeek.getFriday().size() * fortytwo);
                    }
                    break;
                case 5:
                    if (mWeek.getSaturday().size() > 0) {
                        day.setMinimumHeight(mWeek.getSaturday().size() * fortytwo);
                    }
                    break;
                case 6:
                    if (mWeek.getSunday().size() > 0) {
                        day.setMinimumHeight(mWeek.getSunday().size() * fortytwo);
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
        final EditText desctext = myDialog.findViewById(R.id.desctext);
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
        Button settings = myDialog.findViewById(R.id.settings);
        myDialog.findViewById(R.id.textView).setTag("blue"); //Standartfarbe blau
        myDialog.findViewById(R.id.textView).setTag(R.id.slider, "no notification");

        settings.setOnClickListener(new View.OnClickListener() {
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
                String desc = desctext.getText().toString();
                String time1 = timetext.getText().toString();
                String time2 = timetext2.getText().toString();

                time1 = checkTimeInput(time1, "10");
                time2 = checkTimeInput(time2, "00");

                if (!title.isEmpty() && Integer.parseInt(time1) < 24 && Integer.parseInt(time1) > 0 && Integer.parseInt(time2) < 61) {
                    Intent intent = new Intent(mContext, StartpageBackground.class);
                    String time = (time1 + time2);

                    //create(title, Integer.parseInt(time), myDialog.findViewById(R.id.textView).getTag().toString(), day, adapter, weekdate);
                    myDialog.dismiss();
                    intent.putExtra("title", title);
                    intent.putExtra("desc", desc);
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
        final EditText desctext = myDialog.findViewById(R.id.desctext);
        final EditText timetext = myDialog.findViewById(R.id.timetext);
        final EditText timetext2 = myDialog.findViewById(R.id.timetext2);
        Button settings = myDialog.findViewById(R.id.settings);
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

        settings.setOnClickListener(new View.OnClickListener() {
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
                String desc = desctext.getText().toString();
                String time1 = timetext.getText().toString();
                String time2 = timetext2.getText().toString();

                time1 = checkTimeInput(time1, "10");
                time2 = checkTimeInput(time2, "00");
                if (!title.isEmpty() && Integer.parseInt(time1) < 24 && (Integer.parseInt(time1) + Integer.parseInt(time2)) > 0 && Integer.parseInt(time2) < 61) {
                    Intent intent = new Intent(context, StartpageBackground.class);
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
                    intent.putExtra("desc", desc);
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

}
