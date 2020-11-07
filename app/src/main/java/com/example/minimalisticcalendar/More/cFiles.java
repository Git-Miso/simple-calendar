package com.example.minimalisticcalendar.More;

import android.content.Context;

import com.example.minimalisticcalendar.Notifications.Alert;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class cFiles {

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
        Gson gson = new Gson();

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

        Gson gson = new Gson();
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
        Gson gson = new Gson();

        try {
            fos = context.openFileOutput(FILE_NAME, MODE_PRIVATE);
            fos.write(gson.toJson(Alerts).getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
