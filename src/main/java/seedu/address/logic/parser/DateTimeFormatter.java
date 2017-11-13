package seedu.address.logic.parser;

import java.text.SimpleDateFormat;
import java.util.Date;

//@@author raisa2010
/**
 * Contains methods to format task dates and times into the display format.
 */
public class DateTimeFormatter {

    public static final String DISPLAY_DATE_FORMAT = "EEE, MMM d, ''yy";
    public static final String DISPLAY_TIME_FORMAT = "HH:mm";

    /**
     * Formats the last date of a given {@code Date} object into a String in the display format.
     */
    public static String formatDate(Date date) {
        assert date != null;
        SimpleDateFormat sdf = new SimpleDateFormat(DISPLAY_DATE_FORMAT);
        return sdf.format(date);
    }

    /**
     * Formats the last time of a given {@code time} object into a String in the display format.
     */
    public static String formatTime(Date time) {
        assert time != null;
        SimpleDateFormat sdf = new SimpleDateFormat(DISPLAY_TIME_FORMAT);
        return sdf.format(time);
    }
}
