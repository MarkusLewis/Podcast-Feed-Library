package com.icosillion.podengine.utils;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DateUtilsTest {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US);

    @Test
    public void parsesStandardDateFormats() {
        String dateTime = "Tue, 03 March 2009 15:00:00 -0000";

        Date date = DateUtils.stringToDate(dateTime);

        assertNotNull(date);
        assertEquals("Tue, 03 Mar 2009 16:00:00 +0100", sdf.format(date));
    }

    @Test
    public void parsesNonStandardDateFormats() {
        assertEquals("Tue, 03 Mar 2009 16:00:00 +0100",
                     sdf.format(DateUtils.stringToDate("Tues, 03 March 2009 15:00:00 -0000")));

        assertEquals("Wed, 04 Mar 2009 16:00:00 +0100",
                     sdf.format(DateUtils.stringToDate("Wednes, 04 March 2009 15:00:00 -0000")));

        assertEquals("Thu, 05 Mar 2009 16:00:00 +0100",
                     sdf.format(DateUtils.stringToDate("Thurs, 05 March 2009 15:00:00 -0000")));
    }

}
