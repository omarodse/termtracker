package com.sld.termtracker.UI;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    private static final String TAG = "DateUtils";

    // Convert a date string to a timestamp representing midnight of that date
    public static long convertToTimestamp(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.getDefault());
        try {
            Date date = sdf.parse(dateStr);
            if (date != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                return calendar.getTimeInMillis();
            }
        } catch (ParseException e) {
            Log.e(TAG, "Error parsing date: " + dateStr, e);
        }
        return 0;
    }

    // Schedule a notification for a specific date without considering the exact time
    public static void scheduleDateNotification(Context context, String dateStr, String message) {
        long timestamp = convertToTimestamp(dateStr);
        if (timestamp > 0) {
            scheduleNotification(context, timestamp, message);
            Log.d(TAG, "Date is: " + new Date(timestamp));
        } else {
            Log.e(TAG, "Invalid timestamp for date: " + dateStr);
        }
    }

    // Schedule a notification at a specific timestamp
    private static void scheduleNotification(Context context, long timestamp, String message) {
        Intent intent = new Intent(context, NotificationBroadcastReceiver.class);
        intent.putExtra("message", message);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                (int) timestamp,  // Use the timestamp as a unique request code
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, timestamp, pendingIntent);
        } else {
            Log.e(TAG, "AlarmManager is null");
        }
    }

    public static boolean areDatesValid(String startDateStr, String endDateStr) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.getDefault());
        try {
            Date startDate = sdf.parse(startDateStr);
            Date endDate = sdf.parse(endDateStr);

            if (startDate != null && endDate != null) {
                return !endDate.before(startDate);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }
}
