package com.example.minimalisticcalendar.More;

import android.content.Context;

import com.example.minimalisticcalendar.Notifications.Alert;
import com.example.minimalisticcalendar.Startpage.CDate;
import com.example.minimalisticcalendar.Startpage.CWeek;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class cFiles {
    private final static Gson gson = new Gson();

    public static String loadBirthdays(Context c) {
        String text, fulltext = "";
        FileInputStream fis;
        String FILE_NAME = "birthdays.txt";

        try {
            fis = c.openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);

            while ((text = br.readLine()) != null) {
                fulltext += text;
            }
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fulltext;
    }

    public static void saveBirthdays(Context context, ArrayList<Birthday> Birthdays) {
        FileOutputStream fos;
        String FILE_NAME = "birthdays.txt";

        try {
            fos = context.openFileOutput(FILE_NAME, MODE_PRIVATE);
            fos.write(gson.toJson(Birthdays).getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Alert> loadAlerts(Context c) {
        ArrayList<Alert> alertslist;
        FileInputStream fis;
        String FILE_NAME = "alerts.txt";
        String text;
        StringBuilder fulltext = new StringBuilder();

        try {
            fis = c.openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);

            while ((text = br.readLine()) != null) {
                fulltext.append(text);
            }
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Type AlertType = new TypeToken<ArrayList<Alert>>() {
        }.getType();
        alertslist = gson.fromJson(String.valueOf(fulltext), AlertType);
        if (alertslist == null) {
            alertslist = new ArrayList<>();
        }

        return alertslist;
    }

    public static void saveAlerts(Context context, ArrayList<Alert> Alerts) {
        FileOutputStream fos;
        String FILE_NAME = "alerts.txt";

        try {
            fos = context.openFileOutput(FILE_NAME, MODE_PRIVATE);
            fos.write(gson.toJson(Alerts).getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveWeek(Context context, String weekdate, CWeek mWeek) {
        FileOutputStream fos;
        String FILE_NAME = weekdate + ".txt";

        mWeek.sortdays();

        try {
            fos = context.openFileOutput(FILE_NAME, MODE_PRIVATE);
            fos.write(gson.toJson(mWeek).getBytes(StandardCharsets.UTF_8));
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static CWeek loadWeek(Context context, String weekdate) {
        CWeek mWeek;
        FileInputStream fis;
        String FILE_NAME = weekdate + ".txt";
        StringBuilder fulltext = new StringBuilder();
        String text;

        try {
            fis = context.openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);

            while ((text = br.readLine()) != null) {
                fulltext.append(text);
            }
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Type WeekType = new TypeToken<CWeek>() {
        }.getType();

        mWeek = gson.fromJson(String.valueOf(fulltext), WeekType);
        if (mWeek == null) {
            mWeek = new CWeek(new ArrayList<CDate>(),new ArrayList<CDate>(),new ArrayList<CDate>(),new ArrayList<CDate>(),new ArrayList<CDate>(),new ArrayList<CDate>(),new ArrayList<CDate>());
        }

        return mWeek;
    }

    public static void removeAlarm(Context context, String title) {
        ArrayList<Alert> alertslist = cFiles.loadAlerts(context);
        ArrayList<Alert> finalAlerts = new ArrayList<>();

        for (Alert alert : alertslist) {
            if (!alert.title().equals(title)) {
                finalAlerts.add(alert);
            }
        }
        cFiles.saveAlerts(context, finalAlerts);
    }
}
