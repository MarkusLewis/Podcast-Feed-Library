package com.icosillion.podengine.utils;

import com.icosillion.podengine.exceptions.DateFormatException;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    private static int getMonthFromCode(String code) throws DateFormatException {

        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

        for (int i = 0; i < months.length; i++) {
            String month = months[i];

            if (month.equalsIgnoreCase(code)) {
                return i;
            }
        }

        throw new DateFormatException("Invalid month: " + code);
    }

    public static Date stringToDate(String dt) throws DateFormatException {
        Calendar calendar = Calendar.getInstance();
        String[] parts = dt.split(" ");
        if (parts.length != 5) {
            throw new DateFormatException("Invalid date format.");
        }

        //Parse Day of Month
        try {
            calendar.set(Calendar.DAY_OF_MONTH, Integer.valueOf(parts[1]));
        } catch (NumberFormatException e) {
            throw new DateFormatException("Error parsing day of month.");
        }

        //Parse Month
        calendar.set(Calendar.MONTH, getMonthFromCode(parts[2]));

        //Parse Year
        try {
            if (parts[3].length() == 4) {
                calendar.set(Calendar.YEAR, Integer.valueOf(parts[3]));
            } else {
                int year = Integer.valueOf(parts[3]);
                if (year < 80) {
                    year = 2000 + year;
                } else {
                    year = 1900 + year;
                }

                calendar.set(Calendar.YEAR, year);
            }
        } catch (NumberFormatException e) {
            throw new DateFormatException("Error parsing year.");
        }

        //Parse Time
        String[] time = parts[4].split(":");

        try {
            calendar.set(Calendar.HOUR, Integer.valueOf(time[0]));
            calendar.set(Calendar.MINUTE, Integer.valueOf(time[1]));
            calendar.set(Calendar.SECOND, Integer.valueOf(time[2]));
        } catch (NumberFormatException e) {
            throw new DateFormatException("Error parsing time.");
        }

        //Parse Timezone
        int offset = 0;

        if ("EST".equals(parts[5])) {
            offset = -5 * 3600000;
        } else if ("EDT".equals(parts[5])) {
            offset = -4 * 3600000;
        } else if ("CST".equals(parts[5])) {
            offset = -6 * 3600000;
        } else if ("CDT".equals(parts[5])) {
            offset = -5 * 3600000;
        } else if ("MST".equals(parts[5])) {
            offset = -7 * 3600000;
        } else if ("MDT".equals(parts[5])) {
            offset = -6 * 3600000;
        } else if ("PST".equals(parts[5])) {
            offset = -8 * 3600000;
        } else if ("PDT".equals(parts[5])) {
            offset = -7 * 3600000;
        } else if (parts[5].startsWith("+")) {
            offset = Integer.valueOf(parts[5].substring(1, 3)) * 3600000;
            offset += Integer.valueOf(parts[5].substring(3, 5)) * 60000;
        }

        calendar.set(Calendar.ZONE_OFFSET, offset);

        return calendar.getTime();
    }
}
