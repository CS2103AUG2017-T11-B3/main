package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DEADLINE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SINGLE_EVENT_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_START_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Set;
import java.util.Date;
import java.util.List;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.AddTaskCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.Tag;
import seedu.address.model.task.Deadline;
import seedu.address.model.task.Description;
import seedu.address.model.task.ReadOnlyTask;
import seedu.address.model.task.SingleEventDate;
import seedu.address.model.task.StartDate;
import seedu.address.model.task.Task;

/**
 * Parses input arguments and creates a new AddCommand object
 */
public class AddTaskCommandParser implements Parser<AddTaskCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddTaskCommand
     * and returns an AddTaskCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddTaskCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_START_DATE, PREFIX_DEADLINE, PREFIX_SINGLE_EVENT_DATE,
                        PREFIX_TAG);

        if (!isDescriptionPresent(argMultimap)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddTaskCommand.MESSAGE_USAGE));
        }

        try {
            Description description = ParserUtil.parseDescription(argMultimap.getPreamble());
            StartDate startDate = ParserUtil.parseStartDate(argMultimap.getValue(PREFIX_START_DATE));
            Deadline deadline = ParserUtil.parseDeadline(argMultimap.getValue(PREFIX_DEADLINE));
            SingleEventDate singleEventDate =
                    ParserUtil.parseSingleEventDate(argMultimap.getValue(PREFIX_SINGLE_EVENT_DATE));
            Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));
            List<Date> dates = ParserUtil.parseNaturalLanguage(args);
            System.out.println(dates + " ");
            
            ReadOnlyTask task = new Task(description, startDate, deadline, singleEventDate, tagList);

            return new AddTaskCommand(task);
        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage(), ive);
        }
    }

    /**
     * Returns true if the start date prefix does not contain empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean isDescriptionPresent(ArgumentMultimap argumentMultimap) {
        return !argumentMultimap.getPreamble().isEmpty();
    }
}
