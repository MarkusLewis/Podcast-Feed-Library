package com.icosillion.podengine.utils;

import com.icosillion.podengine.exceptions.DateFormatException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    public static Date stringToDate(String dt) throws DateFormatException {
        SimpleDateFormat[] dateFormats = {
                new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US),
                new SimpleDateFormat("dd MMM yyyy HH:mm:ss Z", Locale.US),
                new SimpleDateFormat("EEE, dd MMM yyyy HH:mm Z", Locale.US),
                new SimpleDateFormat("dd MMM yyyy HH:mm Z", Locale.US)
        };

        Date date = null;

        for (SimpleDateFormat dateFormat : dateFormats) {
            try {
                date = dateFormat.parse(dt);
                break;
            } catch (ParseException e) {
                //This format didn't work, keep going
            }
        }

        return date;
    }
}
