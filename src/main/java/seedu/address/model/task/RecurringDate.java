package seedu.address.model.task;

import static java.util.Objects.requireNonNull;
import java.util.Date;

import seedu.address.commons.exceptions.IllegalValueException;

public class RecurringDate extends TaskDates {

    public final String date;

    /**
     * Validates given starting date.
     *
     * @throws IllegalValueException if given date string is invalid.
     */
    public RecurringDate(String date) throws IllegalValueException {
        requireNonNull(date);
        String trimmedDate = date.trim();
        if (!trimmedDate.isEmpty() && !TaskDates.isDateValid(date)) {
            throw new IllegalValueException(MESSAGE_DATE_CONSTRAINTS);
        }
        this.date = trimmedDate;
    }

    @Override
    public String toString() {
        return date;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof StartDate // instanceof handles nulls
                && this.date.equals(((StartDate) other).date)); // state check
    }

    @Override
    public int hashCode() {
        return date.hashCode();
    }

}
