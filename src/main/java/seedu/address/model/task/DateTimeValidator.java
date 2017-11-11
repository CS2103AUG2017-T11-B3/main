package seedu.address.model.task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.IllegalValueException;

//@@author raisa2010
/**
 * Represents a date for a task in the task manager.
 */
public abstract class DateTimeValidator {

    public static final String[] DOTTED_DATE_FORMATS = new String[]{"MM.dd.yyyy", "MM.d.yyyy", "M.d.yyyy",
        "M.d.yy", "M.dd.yy", "MM.d.yy", "MM.dd.yy", "M.dd.yyyy"};
    public static final String[] VALID_DATE_FORMATS = new String[]{"MM-dd-yyyy", "M-dd-yyyy", "M-d-yyyy",
        "MM-d-yyyy", "MM-dd-yy", "M-dd-yy", "MM-d-yy", "M-d-yy", "MM.dd.yyyy", "MM.d.yyyy", "M.d.yyyy",
        "M.d.yy", "M.dd.yy", "MM.d.yy", "MM.dd.yy", "M.dd.yyyy", "MM/dd/yyyy", "M/dd/yyyy", "M/d/yyyy",
        "MM/d/yyyy", "MM/dd/yy", "M/dd/yy", "MM/d/yy", "M/d/yy"};
    public static final int NUMBER_MONTHS = 12;
    public static final int FEBRUARY = 2;
    public static final int MAX_DAYS_IN_MONTH = 31;
    public static final int MAX_DAYS_IN_FEB = 29;

    public static final String MESSAGE_DATE_CONSTRAINTS = "Date is invalid";
    public static final String MESSAGE_TIME_CONSTRAINTS = "Time is invalid";

    /**
     * Validates a given {@code inputDate} given in an MDY format.
     */
    public static boolean isDateValid(String inputDate) {
        String trimmedDate = inputDate.trim();
        for (String format : VALID_DATE_FORMATS) {
            if (doesDateMatchValidFormat(trimmedDate, format)) {
                int firstSeparatorIndex = Math.max(trimmedDate.indexOf('-'), trimmedDate.indexOf('/'));
                firstSeparatorIndex = Math.max(firstSeparatorIndex, trimmedDate.indexOf('.'));
                int secondSeparatorIndex = Math.max(trimmedDate.lastIndexOf('-'),
                        trimmedDate.lastIndexOf('/'));
                secondSeparatorIndex = Math.max(secondSeparatorIndex, trimmedDate.lastIndexOf('.'));
                int month = Integer.parseInt(trimmedDate.substring(0, firstSeparatorIndex));
                int day = Integer.parseInt(trimmedDate.substring(firstSeparatorIndex + 1, secondSeparatorIndex));
                if (month > NUMBER_MONTHS | day > MAX_DAYS_IN_MONTH | (month == FEBRUARY && day > MAX_DAYS_IN_FEB)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Validates a given {@code startTime} to check if it is before the {@code endTime}.
     */
    public static boolean isStartTimeBeforeEndTime(EventTime startTime, EventTime endTime) {
        try {
            Date parsedStartTime = new SimpleDateFormat("HH:mm").parse(startTime.toString());
            Date parsedEndTime = new SimpleDateFormat("HH:mm").parse(endTime.toString());
            return parsedStartTime.before(parsedEndTime);
        } catch (ParseException p) {
            assert(!startTime.isPresent() | !endTime.isPresent());
            return true;
        }
    }

    /**
     * Determines the specific dotted date format used by the {@code String} inputDate.
     */
    public static String getDottedFormat(String inputDate) {
        for (String format : DOTTED_DATE_FORMATS) {
            if (isDateInDottedFormat(inputDate, format) && isDateValid(inputDate)) {
                return format;
            }
        }
        return "";
    }

    /**
     * Checks if a given {@code String inputDate} is in (M)M.(d)d.(yy)yy format.
     */
    public static boolean isDateInDottedFormat(String inputDate, String dateFormat) {
        try {
            new SimpleDateFormat(dateFormat).parse(inputDate);
            return true;
        } catch (ParseException p) {
            return false;
        }
    }

    /**
     * Checks if the {@code String inputDate} matches the given {@code String validDateFormat}
     */
    public static boolean doesDateMatchValidFormat(String inputDate, String validDateFormat) {
        try {
            new SimpleDateFormat(validDateFormat).parse(inputDate);
            return true;
        } catch (ParseException p) {
            return false;
        }
    }

}
