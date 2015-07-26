package com.icosillion.podengine.utils;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    private static int getMonthFromCode(String code) {
        if("Jan".equalsIgnoreCase(code))
            return 0;
        else if("Feb".equalsIgnoreCase(code))
            return 1;
        else if("Mar".equalsIgnoreCase(code))
            return 2;
        else if("Apr".equalsIgnoreCase(code))
            return 3;
        else if("May".equalsIgnoreCase(code))
            return 4;
        else if("Jun".equalsIgnoreCase(code))
            return 5;
        else if("Jul".equalsIgnoreCase(code))
            return 6;
        else if("Aug".equalsIgnoreCase(code))
            return 7;
        else if("Sep".equalsIgnoreCase(code))
            return 8;
        else if("Oct".equalsIgnoreCase(code))
            return 9;
        else if("Nov".equalsIgnoreCase(code))
            return 10;
        else if("Dec".equalsIgnoreCase(code))
            return 11;

        return -1;
    }

    public static Date stringToDate(String dt) {
        Calendar calendar = Calendar.getInstance();
        String[] parts = dt.split(" ");
        calendar.set(Calendar.DAY_OF_MONTH, Integer.valueOf(parts[1]));
        calendar.set(Calendar.MONTH, getMonthFromCode(parts[2]));
        if(parts[3].length() == 4)
            calendar.set(Calendar.YEAR, Integer.valueOf(parts[3]));
        else {
            int year = Integer.valueOf(parts[3]);
            if(year < 80)
                year = 2000 + year;
            else
                year = 1900 + year;
            calendar.set(Calendar.YEAR, year);
        }
        String[] time = parts[4].split(":");
        calendar.set(Calendar.HOUR, Integer.valueOf(time[0]));
        calendar.set(Calendar.MINUTE, Integer.valueOf(time[1]));
        calendar.set(Calendar.SECOND, Integer.valueOf(time[2]));
        int offset = 0;
        if("EST".equals(parts[5]))
            offset = -5 * 3600000;
        else if("EDT".equals(parts[5]))
            offset = -4 * 3600000;
        else if("CST".equals(parts[5]))
            offset = -6 * 3600000;
        else if("CDT".equals(parts[5]))
            offset = -5 * 3600000;
        else if("MST".equals(parts[5]))
            offset = -7 * 3600000;
        else if("MDT".equals(parts[5]))
            offset = -6 * 3600000;
        else if("PST".equals(parts[5]))
            offset = -8 * 3600000;
        else if("PDT".equals(parts[5]))
            offset = -7 * 3600000;
        else if(parts[5].startsWith("+")) {
            offset = Integer.valueOf(parts[5].substring(1, 3)) * 3600000;
            offset += Integer.valueOf(parts[5].substring(3, 5)) * 60000;
        }
        calendar.set(Calendar.ZONE_OFFSET, offset);
        return calendar.getTime();
    }
}
