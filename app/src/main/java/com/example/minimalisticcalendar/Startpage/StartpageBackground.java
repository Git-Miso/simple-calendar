package com.example.minimalisticcalendar.Startpage;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.minimalisticcalendar.More.cFiles;
import com.example.minimalisticcalendar.Notifications.Alert;
import com.example.minimalisticcalendar.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


public class StartpageBackground extends AppCompatActivity {

    public SliderWeekAdapter SliderWeekAdapter;
    ViewPager mSlideViewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        deleteOldData();
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slide_background);
        mSlideViewPager = findViewById(R.id.slideviewpager);

        SliderWeekAdapter = new SliderWeekAdapter(StartpageBackground.this);
        findViewById(R.id.loadingPanel).setVisibility(View.INVISIBLE);

        mSlideViewPager.setAdapter(SliderWeekAdapter);
        mSlideViewPager.setCurrentItem(1);
        mSlideViewPager.setOffscreenPageLimit(2);

        //scroll to the week after creating something
        Bundle extras = this.getIntent().getExtras();
        if (extras != null) {
            if (extras.getString("week") != null) {
                Calendar calendar = Calendar.getInstance();
                int current = calendar.get(Calendar.WEEK_OF_YEAR);
                int setposition = Integer.parseInt(extras.getString("week")) - current;
                mSlideViewPager.setCurrentItem(setposition);
            }
            if (extras.getBoolean("goHabits", false)) {
                mSlideViewPager.setCurrentItem(1);
            }
            if (extras.getBoolean("goGeburtstage", false)) {
                mSlideViewPager.setCurrentItem(0);
            }
        }

        /*mSlideViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position==4){
                    mSlideViewPager.setOffscreenPageLimit(5);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });*/
    }

    @Override
    public void onBackPressed() {
        mSlideViewPager.setCurrentItem(1);
    }


    private void deleteOldData() {
        Context context = this.getApplicationContext();
        String weekdate = new SimpleDateFormat("w", Locale.ENGLISH).format(new java.util.Date());
        FileInputStream fis = null;
        String FILE_NAME = Integer.parseInt(weekdate) - 2 + ".txt";
        try {
            fis = context.openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String text;

            while ((text = br.readLine()) != null) {
                String[] newtext = text.split(",");
                cFiles.removeAlarm(context ,newtext[1]);
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

        ArrayList<Alert> alertslist = cFiles.loadAlerts(context);
        ArrayList<Alert> finalAlerts = new ArrayList<>();

        for (Alert alert : alertslist) {
            if (Integer.parseInt(alert.time().substring(0, 9)) > Integer.parseInt(String.valueOf(Calendar.getInstance().getTimeInMillis()).substring(0, 9))) {
                finalAlerts.add(alert);
            }
        }

        cFiles.saveAlerts(context, finalAlerts);
    }
}
