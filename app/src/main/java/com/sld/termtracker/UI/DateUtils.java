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

    public static boolean areDatesValid(String startDateStr, String endDateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.getDefault());
        sdf.setLenient(false); // Ensure strict parsing
        try {
            Date startDate = sdf.parse(startDateStr);
            Date endDate = sdf.parse(endDateStr);

            if (startDate == null || endDate == null) {
                return false;
            }

            // Split the date strings to validate month, day, and year
            if (!isValidDateComponent(startDateStr) || !isValidDateComponent(endDateStr)) {
                return false;
            }

            // Ensure end date is not before start date
            return !endDate.before(startDate);

        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean isValidDateComponent(String dateStr) {
        String[] dateParts = dateStr.split("/");
        if (dateParts.length != 3) {
            return false;
        }

        try {
            int month = Integer.parseInt(dateParts[0]);
            int day = Integer.parseInt(dateParts[1]);
            int year = Integer.parseInt(dateParts[2]);

            // Validate month
            if (month < 1 || month > 12) {
                return false;
            }

            // Validate day based on the month and year
            if (!isValidDayForMonth(month, day, year)) {
                return false;
            }

            // Validate year (assuming a reasonable range for 'yy')
            if (year < 0 || year > 99) {
                return false;
            }

        } catch (NumberFormatException e) {
            return false; // If any component is not a valid number
        }

        return true;
    }

    private static boolean isValidDayForMonth(int month, int day, int year) {
        // Days in each month (non-leap year)
        int[] daysInMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

        // Check for leap year
        if (month == 2 && isLeapYear(year)) {
            return day >= 1 && day <= 29;
        }

        // Validate day based on the month
        return day >= 1 && day <= daysInMonth[month - 1];
    }

    private static boolean isLeapYear(int year) {
        // Convert 'yy' to 'yyyy' assuming 20th or 21st century
        year += (year < 50 ? 2000 : 1900);
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

}
