package com.example.minimalisticcalendar.Startpage;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.example.minimalisticcalendar.More.Birthday;
import com.example.minimalisticcalendar.More.BirthdayRecyclerAdapter;
import com.example.minimalisticcalendar.More.cFiles;
import com.example.minimalisticcalendar.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class SliderWeekAdapter extends PagerAdapter {

    private final int MAX_PAGES = 13;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private Context context;

    @NonNull
    @Override
    public Object instantiateItem(@NonNull final ViewGroup container, int page) {
        View view;
        String weekdate = getWeekdate(page);

        if (page == 0) { // Birthdaylist
            view = layout(R.layout.geburtstage, container);

            initBirthdays(context, view);

        } else if (page == 1) { // Todolist
            view = layout(R.layout.todo, container);

            initHabits(context, view, weekdate);

        } else {
            view = layout(R.layout.design_theme, container);

            new WeekPage(context, weekdate, page, view);
        }
        return view;
    }

    private String getWeekdate(int page) {
        String week = new SimpleDateFormat("w", Locale.ENGLISH).format(new java.util.Date());
        if (page > 2) {
            week = String.valueOf(Integer.parseInt(week) + page - 2);
        }
        if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            week = String.valueOf(Integer.parseInt(week) - 1);
        }
        return week;
    }

    private View layout(int l, ViewGroup container) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = Objects.requireNonNull(layoutInflater).inflate(l, container, false);
        container.addView(view);
        return view;
    }

    private void initBirthdays(final Context context, final View view) {
        final ArrayList<Birthday> Birthdays = loadBirthdays();

        Space space = new Space(view.getContext());
        space.setMinimumHeight(200);
        LinearLayout G_linearLayout = view.findViewById(R.id.G_linearLayout);
        G_linearLayout.addView(space);

        final RecyclerView recyclerView = view.findViewById(R.id.rec);
        final BirthdayRecyclerAdapter adapter = new BirthdayRecyclerAdapter(context, Birthdays);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        //Add
        FloatingActionButton add = view.findViewById(R.id.add_habit);
        add.setVisibility(View.VISIBLE);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog myDialog = new Dialog(view.getContext());
                myDialog.setContentView(R.layout.todo_add_new_dialog);
                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                Button addbtn = myDialog.findViewById(R.id.addbtn);
                addbtn.setText("Next");
                final EditText titletext = myDialog.findViewById(R.id.titletext);
                TextView todotitle = myDialog.findViewById(R.id.todotitle);
                todotitle.setText(R.string.birthday);

                addbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!titletext.getText().toString().equals("")) {
                            myDialog.dismiss();

                            final Calendar c = Calendar.getInstance();
                            final int gday = c.get(Calendar.DAY_OF_MONTH);
                            final int gmonth = c.get(Calendar.MONTH);
                            final int gyear = c.get(Calendar.YEAR);

                            c.set(Calendar.YEAR, gyear);
                            c.set(Calendar.MONTH, gmonth);
                            c.set(Calendar.DAY_OF_MONTH, gday);

                            mDateSetListener = new DatePickerDialog.OnDateSetListener() {
                                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                    String d = String.valueOf(dayOfMonth);
                                    if (d.length() == 1) {
                                        d = "0" + dayOfMonth;
                                    }
                                    month++;
                                    String m = String.valueOf(month);
                                    if (m.length() == 1) {
                                        m = "0" + month;
                                    }
                                    String name = titletext.getText().toString();
                                    //name = name.replace("}", "");
                                    //name = name.replace("{", "");


                                    Birthdays.add(new Birthday(name, d + "." + m));

                                    cFiles.saveBirthdays(context, Birthdays);

                                    Intent intent = new Intent(view.getContext(), StartpageBackground.class);
                                    intent.putExtra("goGeburtstage", true);
                                    view.getContext().startActivity(intent);
                                    ((Activity) context).overridePendingTransition(0, 0);
                                }
                            };

                            DatePickerDialog datePickerDialog = new DatePickerDialog(context, R.style.Theme_AppCompat_Light_Dialog_MinWidth, mDateSetListener, gyear, gmonth, gday);
                            datePickerDialog.show();
                        }
                    }
                });
                myDialog.show();
            }
        });
    }

    private ArrayList<Birthday> loadBirthdays() {
        ArrayList<Birthday> Birthdays = new ArrayList<>();

        Gson gson = new Gson();

        String fulltext = cFiles.loadBirthdays(context);

        if (!fulltext.isEmpty()) {
            Type BirthdayType = new TypeToken<ArrayList<Birthday>>() {
            }.getType();
            Birthdays = gson.fromJson(fulltext, BirthdayType);
            ArrayList<String> BirthdaysString = new ArrayList<>();
            for (Birthday i : Birthdays) {
                String[] split = i.time().split("\\.");
                BirthdaysString.add(split[1] + "." + split[0] + ":" + i.title());
            }

            Collections.sort(BirthdaysString);

            Birthdays.clear();
            for (String i : BirthdaysString) {
                String[] split = i.split(":");
                String[] split2 = split[0].split("\\.");
                Birthdays.add(new Birthday(split[1], split2[1] + "." + split2[0]));
            }

            cFiles.saveBirthdays(context, Birthdays);

            Birthdays.add(new Birthday("asd", "12.00"));
        }
        return Birthdays;
    }

    private void initHabits(final Context context, final View view, final String weekdatestr) {
        Calendar currentTime = Calendar.getInstance();
        final SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(view.getContext());

        int width = DPtoPixel(view, 165);
        int height = DPtoPixel(view, 58);
        int thirtysix = DPtoPixel(view, 36);

        //get curent day
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        final String current_day = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.getTime());

        final ArrayList<String> Titles = loadTitles(view, current_day, weekdatestr);
        final ArrayList<String> list = new ArrayList<>();

        //add all existing entries
        for (int i = 0; !mPreferences.getString("t" + i, "").equals(""); i++) {
            if (!mPreferences.getString("t" + i, "").equals(mPreferences.getString("deleted_Todo", ""))) {
                if (!Titles.contains(mPreferences.getString("t" + i, ""))) {
                    list.add(mPreferences.getString("t" + i, "None"));
                }
            }
        }
        //add new entries
        list.addAll(Titles);


        //save entries
        for (int i = 0; i < list.size(); i++) {
            mPreferences.edit().putString("t" + i, list.get(i)).apply();
        }
        mPreferences.edit().putString("t" + list.size(), "").apply();

        LinearLayout linearLayout = view.findViewById(R.id.linear_todo);
        int counter = 0;

        for (int i = 0; !mPreferences.getString("t" + i, "").equals(""); i++) {
            CheckBox c = new CheckBox(view.getContext());
            c.setLayoutParams(new ViewGroup.LayoutParams(width, height));
            c.setButtonDrawable(R.drawable.checkbox);
            c.setId(i);
            c.setGravity(Gravity.CENTER);
            c.setMaxLines(2);

            if (Titles.contains(mPreferences.getString("t" + i, ""))) {
                TextView time = new TextView(context);
                time.setTextColor(context.getResources().getColor(R.color.light_grey));
                time.setWidth(thirtysix);

                String[] strings1 = Titles.get(counter).split(";");
                char[] digits1 = strings1[0].toCharArray();
                String time_text;
                if (digits1.length == 3) {
                    time_text = digits1[0] + ":" + digits1[1] + digits1[2];
                } else if (digits1.length == 2) {
                    time_text = String.valueOf(digits1[0]) + digits1[1];
                } else {
                    time_text = String.valueOf(digits1[0]) + digits1[1] + ":" + digits1[2] + digits1[3];
                }
                time.setText(time_text);
                counter++;

                c.setText(strings1[1]);

                //correct Position
                LinearLayout li = new LinearLayout(context);
                li.setOrientation(LinearLayout.HORIZONTAL);
                li.addView(c);
                li.addView(time);
                li.setGravity(Gravity.CENTER);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMarginStart(thirtysix / 2);
                li.setLayoutParams(layoutParams);
                linearLayout.addView(li);
            } else {
                c.setText(mPreferences.getString("t" + i, ""));
                linearLayout.addView(c);
            }

            if (mPreferences.getInt("lastopened", 0) != currentTime.get(Calendar.DAY_OF_MONTH)) {
                mPreferences.edit().putString("t" + i, "").apply();
                c.setChecked(true);
            }

            c.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox a = (CheckBox) v;
                    a.setChecked(true);

                    if (Titles.contains(mPreferences.getString("t" + a.getId(), ""))) {
                        String name = mPreferences.getString("t" + a.getId(), "None");
                        list.remove(name);

                        //save
                        for (int i = 0; i < list.size(); i++) {
                            mPreferences.edit().putString("t" + i, list.get(i)).apply();
                        }
                        mPreferences.edit().putString("t" + list.size(), "").apply();

                        mPreferences.edit().putString("deleted_Todo", name).apply();
                        deleteTitle(Titles.indexOf(name), current_day, weekdatestr);
                    } else {
                        list.remove(mPreferences.getString("t" + a.getId(), "None"));

                        for (int i = 0; i < list.size(); i++) {
                            mPreferences.edit().putString("t" + i, list.get(i)).apply();
                        }
                        mPreferences.edit().putString("t" + list.size(), "").apply();

                        Intent intent = new Intent(view.getContext(), StartpageBackground.class);
                        view.getContext().startActivity(intent);
                        ((Activity) context).overridePendingTransition(0, 0);
                    }
                }
            });
        }

        if (mPreferences.getInt("lastopened", 0) != currentTime.get(Calendar.DAY_OF_MONTH)) {
            mPreferences.edit().putInt("lastopened", currentTime.get(Calendar.DAY_OF_MONTH)).apply();
            Intent intent = new Intent(view.getContext(), StartpageBackground.class);
            view.getContext().startActivity(intent);
            ((Activity) context).overridePendingTransition(0, 0);
        }

        Space space = new Space(view.getContext());
        space.setMinimumHeight(200);
        linearLayout.addView(space);


        FloatingActionButton add_button = view.findViewById(R.id.todo_add_button);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog myDialog = new Dialog(view.getContext());
                myDialog.setContentView(R.layout.todo_add_new_dialog);
                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                Button addbtn = myDialog.findViewById(R.id.addbtn);
                final EditText titletext = myDialog.findViewById(R.id.titletext);

                addbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!titletext.getText().toString().equals("")) {
                            list.add(titletext.getText().toString());
                            for (int i = 0; i < list.size(); i++) {
                                mPreferences.edit().putString("t" + i, list.get(i)).apply();
                            }
                            mPreferences.edit().putString("t" + list.size(), "").apply();

                            Intent intent = new Intent(view.getContext(), StartpageBackground.class);
                            view.getContext().startActivity(intent);
                            ((Activity) context).overridePendingTransition(0, 0);
                            myDialog.dismiss();
                        }
                    }
                });
                myDialog.show();
            }
        });

    }

    private ArrayList<String> loadTitles(View v, String current_day, String weekdate) {

        final String FILE_NAME = weekdate + ".txt";
        FileInputStream fis = null;
        ArrayList<String> Title = new ArrayList<>();
        final SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(v.getContext());

        try {
            fis = context.openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String text;

            while ((text = br.readLine()) != null) {
                String[] newtext = text.split(",");
                //Wenn der Tag der gewünschte ist / 11.11.2011, Hallo, 10:10, #FFFFF
                if (newtext[0].equals(current_day)) {
                    if (!(newtext[2] + ";" + newtext[1]).equals(mPreferences.getString("deleted_Todo", ""))) {
                        Title.add(newtext[2] + ";" + newtext[1]);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                    mPreferences.edit().putString("deleted_Todo", "").apply();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return Title;
    }

    private void deleteTitle(int position, String current_day, String weekdate) {
        Intent intent = new Intent(context, StartpageBackground.class);
        intent.putExtra("delpos", position);
        int week = Integer.parseInt(weekdate);
        //week -=1;
        intent.putExtra("week", String.valueOf(week));
        intent.putExtra("day", current_day);
        intent.putExtra("goHabits", true);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(0, 0);
    }

    //Helpers
    private Integer DPtoPixel(View view, Integer dp) {
        final float scale = view.getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    //--------------------------------------------------------------------------------------------------
    SliderWeekAdapter(Context mcontext) {
        context = mcontext;
    }

    @Override
    public int getCount() {
        return MAX_PAGES;
    }

    @Override
    public boolean isViewFromObject(@NonNull final View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
