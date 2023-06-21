package com.example.spree;

import android.os.Build;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtils {
    public static String[] computeStartAndEndDate(String duration) {
        LocalDate endDate = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            endDate = LocalDate.now();
        }
        LocalDate startDate = null;

        if (endDate == null) {
            return new String[]{"2023-06-17", "2023-01-01",};
        }

        // Parse the duration to determine the start date
        if (duration.equalsIgnoreCase("1 Day")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startDate = endDate.minusDays(1);
            }
        } else if (duration.equalsIgnoreCase("3 Days")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startDate = endDate.minusDays(3);
            }
        } else if (duration.equalsIgnoreCase("15 Days")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startDate = endDate.minusDays(15);
            }
        } else if (duration.equalsIgnoreCase("1 Month")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startDate = endDate.minusMonths(1);
            }
        } else if (duration.equalsIgnoreCase("6 Months")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startDate = endDate.minusMonths(6);
            }
        } else if (duration.equalsIgnoreCase("1 Year")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startDate = endDate.minusYears(1);
            }
        } else {
            // Invalid duration, return null
            return null;
        }

        if (startDate == null) {
            return new String[]{"2023-06-17", "2023-01-01",};
        }

        // Format the dates as strings in the format "yyyy-MM-dd"
        DateTimeFormatter formatter = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        }
        String startDateString = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startDateString = startDate.format(formatter);
        }
        String endDateString = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            endDateString = endDate.format(formatter);
        }

        if (startDateString == null || endDateString == null) {
            return new String[]{"2023-06-17", "2023-01-01",};
        }

        return new String[]{startDateString, endDateString};
    }
}
