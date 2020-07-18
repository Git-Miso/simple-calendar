package com.example.minimalisticcalendar.Startpage;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.minimalisticcalendar.R;

import java.util.Calendar;


public class StartpageBackground extends AppCompatActivity {

    public SliderWeekAdapter SliderWeekAdapter;
    ViewPager mSlideViewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slide_background);
        mSlideViewPager = findViewById(R.id.slideviewpager);

        SliderWeekAdapter = new SliderWeekAdapter(StartpageBackground.this);
        findViewById(R.id.loadingPanel).setVisibility(View.INVISIBLE);

        mSlideViewPager.setAdapter(SliderWeekAdapter);
        mSlideViewPager.setCurrentItem(1);
        mSlideViewPager.setOffscreenPageLimit(2);

        Bundle extras = this.getIntent().getExtras();
        if (extras != null) {
            if (extras.getString("week") != null) {
                Calendar calendar = Calendar.getInstance();
                int current = calendar.get(Calendar.WEEK_OF_YEAR);
                int setposition = Integer.parseInt(extras.getString("week")) - current + 2;
                mSlideViewPager.setCurrentItem(setposition);
            }
            if(extras.getBoolean("goHabits", false)){
                mSlideViewPager.setCurrentItem(1);
            }
            if(extras.getBoolean("goGeburtstage", false)){
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
}
