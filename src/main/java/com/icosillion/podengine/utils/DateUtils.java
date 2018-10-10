package com.icosillion.podengine.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    public static Date stringToDate(String dt) {
        SimpleDateFormat[] dateFormats = {
                new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US),
                new SimpleDateFormat("dd MMM yyyy HH:mm:ss Z", Locale.US),
                new SimpleDateFormat("EEE, dd MMM yyyy HH:mm Z", Locale.US),
                new SimpleDateFormat("dd MMM yyyy HH:mm Z", Locale.US)
        };

        String normalizedDt = normalize(dt);

        Date date = null;

        for (SimpleDateFormat dateFormat : dateFormats) {
            try {
                date = dateFormat.parse(normalizedDt);
                break;
            } catch (ParseException e) {
                //This format didn't work, keep going
            }
        }

        return date;
    }

    private static String normalize(String dt) {
        return dt.replace("Tues,", "Tue,")
                 .replace("Thurs,", "Thu,")
                 .replace("Wednes,", "Wed,");
    }
}
