package com.example.minimalisticcalendar.Notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.example.minimalisticcalendar.More.cFiles;

import java.util.ArrayList;
import java.util.Calendar;

public class AlertReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationHelper notificationHelper = new NotificationHelper(context);

        ArrayList<Alert> allAlerts = cFiles.loadAlerts(context);

        Long currentTime = Calendar.getInstance().getTimeInMillis();

        int i = 0;
        while (!allAlerts.get(i).time().substring(0, 9).equals(String.valueOf(currentTime).substring(0, 9)) && i < allAlerts.size() - 1) {
            i++;
        }

        if (allAlerts.get(i).time().substring(0, 9).equals(String.valueOf(currentTime).substring(0, 9))) {
            String desc;
            switch (allAlerts.get(i).time().charAt(allAlerts.size() - 1)) {
                case 'a':
                    desc = "starts in 10 minutes";
                    break;
                case 'b':
                    desc = "starts in 30 minutes";
                    break;
                case 'c':
                    desc = "starts in 1 hour";
                    break;
                case 'd':
                    desc = "starts in one day";
                    break;
                case 'e':
                    desc = "starts in one week";
                    break;
                default:
                    desc = "starts now";
                    break;
            }


            if (!allAlerts.get(i).title().equals("--deleted--")) {
                NotificationCompat.Builder nb = notificationHelper.getChannelNotification(allAlerts.get(i).title(), desc);
                notificationHelper.getManager().notify(currentTime.intValue(), nb.build());
            }
            allAlerts.remove(i);
            cFiles.saveAlerts(context, allAlerts);
        }
    }
}
