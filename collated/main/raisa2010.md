# raisa2010
###### /java/seedu/address/logic/commands/CommandUtil.java
``` java
/**
 * Contains common methods for commands.
 */
public class CommandUtil {

    /**
     * Filters the valid indices in a given array of indices.
     */
    public static Index[] filterValidIndices(int lastShownListSize, Index[] indices) {
        assert indices != null;
        assert lastShownListSize != 0;
        return Arrays.stream(indices)
                .filter(currentIndex -> currentIndex.getZeroBased() < lastShownListSize)
                .toArray(Index[]::new);
    }
}
```
###### /java/seedu/address/logic/commands/persons/TagCommand.java
``` java
/**
 * Tags multiple people in the address book.
 */
public class TagCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "tag";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Tags multiple people using the same tag(s) "
            + "by the index number used in the last person listing. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDICES (must be positive integers and may be one or more) "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " 1, 2, 3 "
            + PREFIX_TAG + "friend";

    public static final String MESSAGE_TAG_PERSONS_SUCCESS = "New tag added.";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the address book.";

    private final Index[] indices;
    private final Set<Tag> newTags;

    private Logger logger = LogsCenter.getLogger(this.getClass());

    /**
     * @param indices of the people in the filtered person list to tag
     * @param tagList list of tags to tag the people with
     */
    public TagCommand(Index[] indices, Set<Tag> tagList) {
        requireNonNull(indices);
        requireNonNull(tagList);

        this.indices = indices;
        newTags = tagList;
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        List<ReadOnlyPerson> lastShownList = model.getFilteredPersonList();

        Index[] validIndices = CommandUtil.filterValidIndices(lastShownList.size(), indices);

        if (validIndices.length == 0) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        int numInvalidValues = indices.length - validIndices.length;
        logger.info("Number of invalid indices skipped: " + numInvalidValues);

        for (Index currentIndex : validIndices) {
            assert currentIndex != null;
            assert currentIndex.getZeroBased() >= 0;
            ReadOnlyPerson personToEdit = lastShownList.get(currentIndex.getZeroBased());

            try {
                model.updatePersonTags(personToEdit, newTags);
            } catch (DuplicatePersonException dpe) {
                throw new CommandException(MESSAGE_DUPLICATE_PERSON);
            } catch (PersonNotFoundException pnfe) {
                throw new AssertionError("The target person cannot be missing");
            }
        }
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(MESSAGE_TAG_PERSONS_SUCCESS);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof TagCommand)) {
            return false;
        }

        //state check
        for (int i = 0; i < indices.length; i++) {
            assert(indices.length == ((TagCommand) other).indices.length);
            if (!indices[i].equals(((TagCommand) other).indices[i])) {
                return false;
            }
        }
        return newTags.toString().equals(((TagCommand) other).newTags.toString());
    }
}
```
###### /java/seedu/address/logic/commands/tasks/AddTaskCommand.java
``` java
/**
 * Adds a task to the task manager
 */
public class AddTaskCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "add";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a task to the task manager. "
            + "Parameters: "
            + "DESCRIPTION "
            + PREFIX_DEADLINE_BY + "/" + PREFIX_DEADLINE_ON  + "/" + PREFIX_DEADLINE_FROM + " DEADLINE DATE "
            + PREFIX_TIME_AT + " START TIME " + PREFIX_ENDTIME_TO + " END TIME "
            + PREFIX_TAG + "TAG.\n"
            + "Task Descriptions containing deadline or time prefixes must be in double quotes [\"\"].";

    public static final String MESSAGE_SUCCESS = "Task has been added: %1$s";
    public static final String MESSAGE_DUPLICATE_TASK = "This task already exists in the task manager";

    private final Task toAdd;

    /**
     * Creates an AddTaskCommand to add the specified {@code ReadOnlyTask}
     */
    public AddTaskCommand(ReadOnlyTask task) {
        toAdd = new Task(task);
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        requireNonNull(model);
        try {
            model.addTask(toAdd);
            return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
        } catch (IllegalValueException e) {
            throw new CommandException(MESSAGE_DUPLICATE_TASK);
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddTaskCommand // instanceof handles nulls
                && toAdd.equals(((AddTaskCommand) other).toAdd));
    }
}
```
###### /java/seedu/address/logic/commands/tasks/TagTaskCommand.java
``` java
/**
 * Tags multiple people in the address book.
 */
public class TagTaskCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "tag";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Tags multiple tasks using the same tag(s) "
            + "by the index number used in the last task listing. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDICES (must be positive integers and may be one or more) "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " 1, 2, 3 "
            + PREFIX_TAG + "urgent";

    public static final String MESSAGE_TAG_TASKS_SUCCESS = "New tag added.";
    public static final String MESSAGE_DUPLICATE_TASK = "This task already exists in the address book.";

    private final Index[] indices;
    private final Set<Tag> newTags;

    private Logger logger = LogsCenter.getLogger(this.getClass());

    /**
     * @param indices of the tasks in the filtered task list to tag
     * @param tagList list of tags to tag the task with
     */
    public TagTaskCommand(Index[] indices, Set<Tag> tagList) {
        requireNonNull(indices);
        requireNonNull(tagList);

        this.indices = indices;
        newTags = tagList;
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        List<ReadOnlyTask> lastShownList = model.getFilteredTaskList();

        Index[] validIndices = CommandUtil.filterValidIndices(lastShownList.size(), indices);

        if (validIndices.length == 0) {
            throw new CommandException(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }

        int numInvalidValues = indices.length - validIndices.length;
        logger.info("Number of invalid indices skipped: " + numInvalidValues);

        for (Index currentIndex : validIndices) {
            assert currentIndex != null;
            assert currentIndex.getZeroBased() >= 0;
            ReadOnlyTask taskToEdit = lastShownList.get(currentIndex.getZeroBased());

            try {
                model.updateTaskTags(taskToEdit, newTags);
            } catch (DuplicateTaskException dpe) {
                throw new CommandException(MESSAGE_DUPLICATE_TASK);
            } catch (TaskNotFoundException pnfe) {
                throw new AssertionError("The target task cannot be missing");
            }
        }
        model.updateFilteredTaskList(PREDICATE_SHOW_ALL_TASKS);
        return new CommandResult(MESSAGE_TAG_TASKS_SUCCESS);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof TagTaskCommand)) {
            return false;
        }

        //state check
        for (int i = 0; i < indices.length; i++) {
            assert(indices.length == ((TagTaskCommand) other).indices.length);
            if (!indices[i].equals(((TagTaskCommand) other).indices[i])) {
                return false;
            }
        }
        return newTags.toString().equals(((TagTaskCommand) other).newTags.toString());
    }
}
```
###### /java/seedu/address/logic/Logic.java
``` java
    /** Returns an unmodifiable view of the filtered list of tasks */
    ObservableList<ReadOnlyTask> getFilteredTaskList();

```
###### /java/seedu/address/logic/parser/AddTaskCommandParser.java
``` java
/**
 * Parses input arguments and creates a new AddTaskCommand object
 */
public class AddTaskCommandParser implements Parser<AddTaskCommand> {

    private static final int INDEX_START_TIME = 0;
    private static final int INDEX_END_TIME = 1;

    private Logger logger = LogsCenter.getLogger(this.getClass());

    /**
     * Parses the given {@code String} of arguments in the context of the AddTaskCommand
     * and returns an AddTaskCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddTaskCommand parse(String args) throws ParseException {
        requireNonNull(args);

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_DEADLINE_ON, PREFIX_DEADLINE_BY,
                PREFIX_DEADLINE_FROM, PREFIX_TIME_AT, PREFIX_TAG);

        if (!isDescriptionPresent(argMultimap) | !isSinglePrefixPresent(argMultimap)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddTaskCommand.MESSAGE_USAGE));
        }

        try {
            Description description = ParserUtil.parseDescription(argMultimap.getPreamble()).get();
            Deadline deadline = ParserUtil.parseDeadline(argMultimap.getValue(PREFIX_DEADLINE_BY,
                    PREFIX_DEADLINE_FROM, PREFIX_DEADLINE_ON))
                    .orElse(new Deadline(""));
            EventTime[] eventTimes = ParserUtil.parseEventTimes(argMultimap.getValue(PREFIX_TIME_AT))
                    .orElse(new EventTime[]{new EventTime(""), new EventTime("")});
            Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));

            if (!DateTimeValidator.isStartTimeBeforeEndTime(eventTimes[INDEX_START_TIME], eventTimes[INDEX_END_TIME])) {
                throw new IllegalValueException(DateTimeValidator.MESSAGE_TIME_CONSTRAINTS);
            }

            if (deadline.isEmpty() && eventTimes[INDEX_END_TIME].isPresent()) {
                deadline = ParserUtil.parseDeadline(Optional.of(new Date().toString())).get();
                logger.fine("Time(s) specified without deadline. Setting deadline to " + deadline);
            }

            ReadOnlyTask task = new Task(description, deadline, eventTimes[INDEX_START_TIME],
                    eventTimes[INDEX_END_TIME], tagList);

            return new AddTaskCommand(task);
        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage(), ive);
        }
    }

    /**
     * Returns true if the preamble (string before first valid prefix) is not empty in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean isDescriptionPresent(ArgumentMultimap argumentMultimap) {
        return !argumentMultimap.getPreamble().isEmpty();
    }

    /**
     * Returns true if a single deadline prefix has been used less than two times in an unquoted string in the given
     * {@code ArgumentMultimap}
     */
    public static boolean isSinglePrefixPresent(ArgumentMultimap argumentMultimap) {
        int prefixCounter = 0;
        if (argumentMultimap.getValue(PREFIX_DEADLINE_BY).isPresent()) {
            prefixCounter++;
        }
        if (argumentMultimap.getValue(PREFIX_DEADLINE_ON).isPresent()) {
            prefixCounter++;
        }
        if (argumentMultimap.getValue(PREFIX_DEADLINE_FROM).isPresent()) {
            prefixCounter++;
        }
        return prefixCounter < 2;
    }
}
```
###### /java/seedu/address/logic/parser/ArgumentTokenizer.java
``` java
    /**
     * Returns the part of the {@code argsString} that is unquoted to prevent tokenization of prefixes intended to be
     * in the description. If the argsString contains no quotes then the entire string is returned.
     * @param argsString Arguments string of the form: {@code "preamble" <prefix>value <prefix>value ...} or
     *                   Arguments string of the form: {@code preamble <prefix>value <prefix>value ...}
     * @return           The part of the {@code argsString} that is unquoted.
     */
    private static String extractUnquotedArgsString(String argsString) {
        assert argsString != null;
        if (argsString.indexOf(QUOTE_REGEX) == argsString.lastIndexOf(QUOTE_REGEX)) {
            return argsString;
        }
        String[] unquotedArgsString = argsString.split(QUOTE_REGEX);
        return (unquotedArgsString.length == 2) ? "" : unquotedArgsString[2];
    }

```
###### /java/seedu/address/logic/parser/DateTimeFormatter.java
``` java
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
```
###### /java/seedu/address/logic/parser/DateTimeValidator.java
``` java
/**
 * Contains methods to validate dates and times input by users into the task manager.
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

    public static final String MESSAGE_DATE_CONSTRAINTS = "Date is invalid! Invalid values include values such as 32 "
            + "Jan, 32-01-2019.\n"
            + "Invalid formats include \"next thursday\", \"th\", \"the 25th\" (without specifying the month), "
            + "and any date not using the (M)M(d)d(YY)YY format.";
    public static final String MESSAGE_TIME_CONSTRAINTS = "Time is invalid! Invalid values include values such as "
            + "29:00, 29pm or 2900.\n"
            + "Invalid formats include 100 (instead of 1:00), 1900 (instead of 19:00) and 11 (instead of 11 am or pm)\n"
            + "Start times cannot be after End times.";

    /**
     * Validates a given {@code inputDate} given in an MDY format.
     * Works around some limitations of the Pretty Time NLP.
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
            Date parsedStartTime =
                    new SimpleDateFormat(DateTimeFormatter.DISPLAY_TIME_FORMAT).parse(startTime.toString());
            Date parsedEndTime = new SimpleDateFormat(DateTimeFormatter.DISPLAY_TIME_FORMAT).parse(endTime.toString());
            return parsedStartTime.before(parsedEndTime);
        } catch (ParseException p) {
            assert !startTime.isPresent() | !endTime.isPresent();
            return true;
        }
    }

    /**
     * Determines the specific dotted date format used by the {@code String} inputDate.
     * Returns an empty string if it doesn not match the dotted date format.
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
     * Checks if the {@code String inputDate} matches the given {@code String validDateFormat}.
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
```
###### /java/seedu/address/logic/parser/EditTaskCommandParser.java
``` java
/**
 * Parses input arguments and creates a new EditTaskCommand object.
 */
public class EditTaskCommandParser implements Parser<EditTaskCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditTaskCommand
     * and returns an EditTaskCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditTaskCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_DEADLINE_BY, PREFIX_DEADLINE_ON,
                PREFIX_DEADLINE_FROM, PREFIX_TIME_AT, PREFIX_TAG);

        Index index;

        try {
            index = ParserUtil.parseIndex(getIndexForEdit(argMultimap.getPreamble()));
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditTaskCommand.MESSAGE_USAGE));
        }

        if (!AddTaskCommandParser.isSinglePrefixPresent(argMultimap)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditTaskCommand.MESSAGE_USAGE));
        }

        EditTaskDescriptor editTaskDescriptor = new EditTaskCommand.EditTaskDescriptor();
        try {
            parseDescriptionForEdit(argMultimap.getPreamble()).ifPresent(editTaskDescriptor::setDescription);
            parseDeadlineForEdit(argMultimap.getAllValues(PREFIX_DEADLINE_BY, PREFIX_DEADLINE_FROM, PREFIX_DEADLINE_ON))
                    .ifPresent(editTaskDescriptor::setDeadline);
            parseEventTimesForEdit(argMultimap.getAllValues(PREFIX_TIME_AT))
                    .ifPresent(editTaskDescriptor::setEventTimes);
            ParserUtil.parseTagsForEdit(argMultimap.getAllValues(PREFIX_TAG)).ifPresent(editTaskDescriptor::setTags);

            if (editTaskDescriptor.getStartTime().isPresent() && editTaskDescriptor.getEndTime().isPresent()
                    && !DateTimeValidator.isStartTimeBeforeEndTime(editTaskDescriptor.getStartTime().get(),
                    editTaskDescriptor.getEndTime().get())) {
                throw new IllegalValueException(DateTimeValidator.MESSAGE_TIME_CONSTRAINTS);
            }

        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage(), ive);
        }

        if (!editTaskDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditTaskCommand.MESSAGE_NOT_EDITED);
        }

        return new EditTaskCommand(index, editTaskDescriptor);
    }

    /**
     * Parses {@code List<String> dates} into a {@code Optional<Deadline>} containing the last date in the list,
     * if {@code dates} is non-empty.
     * If {@code dates} contain only one element which is an empty string, it will be parsed into a
     * {@code Optional<Deadline>} containing an empty date.
     */
    public Optional<Deadline> parseDeadlineForEdit(List<String> dates) throws IllegalValueException {
        assert dates != null;

        if (dates.isEmpty()) {
            return Optional.empty();
        }
        return dates.size() == 1 && dates.contains("")
                ? Optional.of(new Deadline(""))
                : ParserUtil.parseDeadline(Optional.of(dates.get(dates.size() - 1)));

    }

    /**
     * Parses {@code List<String> times} into a {@code Optional<EventTime[]>} containing the last two times in
     * the list if the {@code times} is non-empty. All single times are counted as end times.
     *  If {@code times} contain only one element which is an empty string, it will be parsed into a
     * {@code Optional<EventTime[]>} containing empty times.
     */
    public Optional<EventTime[]> parseEventTimesForEdit(List<String> times) throws IllegalValueException {
        assert times != null;

        if (times.isEmpty()) {
            return Optional.empty();
        }

        return times.size() == 1 && times.contains("")
                ? Optional.of(new EventTime[]{new EventTime(""), new EventTime("")})
                : ParserUtil.parseEventTimes(Optional.of(times.get(times.size() - 1)));
    }

    /**
     * Separates the index from the description in the preamble.
     * @param preamble (string before any valid prefix) received from the argument multimap of the tokenized edit
     * task command.
     * @return the index of the task to be edited.
     */
    public String getIndexForEdit(String preamble) {
        assert preamble != null;
        String trimmedPreamble = preamble.trim();
        return (trimmedPreamble.indexOf(' ') == -1) ? trimmedPreamble
                : trimmedPreamble.substring(0, trimmedPreamble.indexOf(' '));
    }

    /**
     * Separates the description from the index in the preamble.
     * @param preamble (string before any valid prefix) received from the argument multimap of the tokenized edit
     * task command.
     * @return {@code Optional<Description>} (parsed) of the task to be edited if it is present in the preamble,
     * otherwise an empty Optional is returned.
     */
    public Optional<Description> parseDescriptionForEdit(String preamble)
            throws IllegalValueException {
        assert preamble != null;
        int indexLength = getIndexForEdit(preamble).length();
        String description = (indexLength == preamble.length()) ? ""
                : preamble.substring(indexLength, preamble.length());
        return description.isEmpty() ? Optional.empty() : ParserUtil.parseDescription(description);
    }
}
```
###### /java/seedu/address/logic/parser/ParserUtil.java
``` java
    /**
     * Parses a {@code Optional<String> description} into an {@code Optional<Description>} if {@code description}
     * is present.
     */
    public static Optional<Description> parseDescription(String description) throws IllegalValueException {
        requireNonNull(description);
        return description.isEmpty() ? Optional.empty()
                : Optional.of(new Description(description.replace("\"", "")));
    }

    /**
     * Parses a {@code Optional<String> date} into an {@code Deadline} if {@code date} is present.
     */
    public static Optional<Deadline> parseDeadline(Optional<String> date)
            throws IllegalValueException {
        requireNonNull(date);
        if (date.isPresent() && !DateTimeValidator.getDottedFormat(date.get()).isEmpty()) {
            return Optional.of(new Deadline(DateTimeFormatter.formatDate(parseDottedDate(date.get()))));
        }
        if ((date.isPresent() && !date.get().isEmpty())) {
            Date parsedDate = parseDate(date.get());
            return Optional.of(new Deadline(DateTimeFormatter.formatDate(parsedDate)));
        }
        return Optional.empty();
    }

    /**
     * Parses a {@code Optional<String> time} into a {@code EventTime} is the {@code time} is present.
     */
    public static Optional<EventTime[]> parseEventTimes(Optional<String> dateTime) throws IllegalValueException {
        requireNonNull(dateTime);
        if (dateTime.isPresent()) {
            List<DateGroup> dateGroup = new PrettyTimeParser().parseSyntax(dateTime.get().trim());
            if (dateGroup.isEmpty()) {
                throw new IllegalValueException(DateTimeValidator.MESSAGE_TIME_CONSTRAINTS);
            }
            List<Date> dates = dateGroup.get(dateGroup.size() - 1).getDates();

            String endTime = DateTimeFormatter.formatTime(dates.get(dates.size() - 1));
            String startTime = dates.size() > 1 ? DateTimeFormatter.formatTime(dates.get(dates.size() - 2)) : "";

            return Optional.of(new EventTime[]{new EventTime(startTime), new EventTime(endTime)});
        } else {
            return Optional.empty();
        }
    }

    /**
     * Parses a {@code String naturalLanguageInput} using PrettyTime NLP, into a {@code Date}.
     * @throws IllegalValueException if the date cannot be parsed from the phrase or if the given date is invalid.
     */
    public static Date parseDate(String naturalLanguageInput) throws IllegalValueException {
        List<DateGroup> dateGroup = new PrettyTimeParser().parseSyntax(naturalLanguageInput.trim());
        if (dateGroup.isEmpty() | !DateTimeValidator.isDateValid(naturalLanguageInput)) {
            throw new IllegalValueException(DateTimeValidator.MESSAGE_DATE_CONSTRAINTS);
        }
        List<Date> dates = dateGroup.get(dateGroup.size() - 1).getDates();
        return dates.get(dates.size() - 1);
    }

    /**
     * Parses the {@code String inputDate} into a {@code Date} if the input is given in (M)M.(d)d.(yy)yy format,
     * which cannot be parsed by the PrettyTime NLP.
     */
    public static Date parseDottedDate(String inputDate) throws IllegalValueException {
        try {
            return new SimpleDateFormat(DateTimeValidator.getDottedFormat(inputDate)).parse(inputDate);
        } catch (ParseException p) {
            throw new IllegalValueException(DateTimeValidator.MESSAGE_DATE_CONSTRAINTS);
        }
    }
}
```
###### /java/seedu/address/logic/parser/TagCommandParser.java
``` java
/**
 * Parses input arguments and creates a new TagCommand object.
 */
public class TagCommandParser implements Parser<TagCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the TagCommand
     * and returns a TagCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format.
     */
    public TagCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_TAG);

        Index[] parsedIndices;
        Set<Tag> tagList;

        try {
            parsedIndices =  ParserUtil.parseIndices(argMultimap.getPreamble().split(","));
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagCommand.MESSAGE_USAGE));
        }

        try {
            tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));
        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage());
        }
        return new TagCommand(parsedIndices, tagList);
    }
}
```
###### /java/seedu/address/logic/parser/TagTaskCommandParser.java
``` java
/**
 * Parses input arguments and creates a new TagTaskCommand object.
 */
public class TagTaskCommandParser implements Parser<TagTaskCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the TagTaskCommand
     * and returns a TagTaskCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format.
     */
    public TagTaskCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_TAG);

        Index[] parsedIndices;
        Set<Tag> tagList;

        try {
            parsedIndices =  ParserUtil.parseIndices(argMultimap.getPreamble().split(","));
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagTaskCommand.MESSAGE_USAGE));
        }

        try {
            tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));
        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage());
        }

        return new TagTaskCommand(parsedIndices, tagList);
    }

}
```
###### /java/seedu/address/model/AddressBook.java
``` java
    /**
     * Updates the tags of an existing {@code person} in the addressbook by adding the
     * {@code newTags} to the person's existing tags.
     * @throws PersonNotFoundException if the person index provided is invalid.
     */
    public void updatePersonTags(ReadOnlyPerson person, Set<Tag> newTags)
            throws PersonNotFoundException, DuplicatePersonException {

        ReadOnlyPerson oldPerson = getPersonList().stream()
                .filter(personInList -> person.equals(personInList))
                .findAny()
                .get();

        Set<Tag> allTags = new HashSet<>(person.getTags());
        allTags.addAll(newTags);
        Person newPerson = new Person(oldPerson);
        newPerson.setTags(allTags);

        updatePerson(oldPerson, newPerson);
    }

    /**
     * Updates the tags of an existing {@code task} in the task manager by adding the
     * {@code newTags} to the task's existing tags.
     * @throws TaskNotFoundException if the task index provided is invalid.
     */
    public void updateTaskTags(ReadOnlyTask task, Set<Tag> newTags)
            throws TaskNotFoundException, DuplicateTaskException {

        ReadOnlyTask oldTask = getTaskList().stream()
                .filter(taskInList -> task.equals(taskInList))
                .findAny()
                .get();

        Set<Tag> allTags = new HashSet<>(task.getTags());
        allTags.addAll(newTags);
        Task newTask = new Task(oldTask);
        newTask.setTags(allTags);

        updateTask(oldTask, newTask);
    }

    ////task-level operations

    /**
     * Adds a task to the task manager.
     *
     * @throws DuplicateTaskException if an equivalent task already exists.
     */
    public void addTask(ReadOnlyTask t) throws IllegalValueException {
        Task newTask = new Task(t);
        if (t.getDeadline().isEmpty() && t.getEndTime().isPresent()) {
            newTask.setDeadline(new Deadline(DateTimeFormatter.formatDate(new Date())));
        }
        if (t.getStartTime().isPresent() && !t.getEndTime().isPresent()) {
            newTask.setEndTime(t.getStartTime());
            newTask.setStartTime(new EventTime(""));
        }
        syncMasterTagListWith(newTask);
        tasks.add(newTask);
    }

    /**
     * Replaces the given task {@code target} in the list with {@code editedReadOnlyTask}.
     * {@code AddressBook}'s tag list will be updated with the tags of {@code editedReadOnlyTask}.
     *
     * @throws DuplicateTaskException if updating the task's details causes the task to be equivalent to
     *      another existing task in the list.
     * @throws TaskNotFoundException if {@code target} could not be found in the list.
     *
     * @see #syncMasterTagListWith(Task)
     */
    public void updateTask(ReadOnlyTask target, ReadOnlyTask editedReadOnlyTask)
            throws DuplicateTaskException, TaskNotFoundException {
        requireNonNull(editedReadOnlyTask);

        Task editedTask = new Task(editedReadOnlyTask);
        syncMasterTagListWith(editedTask);
        tasks.setTask(target, editedTask);
    }

```
###### /java/seedu/address/model/Model.java
``` java

    /**
     * Updates the tags of an existing {@code person} in the addressbook by adding the
     * {@code newTags} to the person's existing tags.
     * @throws PersonNotFoundException if the person index provided is invalid.
     */
    void updatePersonTags(ReadOnlyPerson person, Set<Tag> newTags)
            throws PersonNotFoundException, DuplicatePersonException;

```
###### /java/seedu/address/model/Model.java
``` java
    /**
     * Replaces the given task {@code target} with {@code editedTask}.
     *
     * @throws DuplicateTaskException if updating the task's details causes the task to be equivalent to
     *      another existing task in the list.
     * @throws TaskNotFoundException if {@code target} could not be found in the list.
     */
    void updateTask(ReadOnlyTask target, ReadOnlyTask editedTask)
            throws DuplicateTaskException, TaskNotFoundException;

    /**
     * Updates the tags of an existing {@code task} in the task manager by adding the
     * {@code newTags} to the task's existing tags.
     * @throws TaskNotFoundException if the task index provided is invalid.
     */
    void updateTaskTags(ReadOnlyTask task, Set<Tag> newTags)
        throws DuplicateTaskException, TaskNotFoundException;

    /** Returns an unmodifiable view of the filtered task list */
    ObservableList<ReadOnlyTask> getFilteredTaskList();

    /**
     * Updates the filter of the filtered task list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredTaskList(Predicate<ReadOnlyTask> predicate);
```
###### /java/seedu/address/model/ModelManager.java
``` java
    @Override
    public void updatePersonTags(ReadOnlyPerson person, Set<Tag> newTags)
            throws PersonNotFoundException, DuplicatePersonException {
        requireAllNonNull(newTags);

        addressBook.updatePersonTags(person, newTags);
        indicateAddressBookChanged();
    }
```
###### /java/seedu/address/model/ModelManager.java
``` java
    @Override
    public synchronized void addTask(ReadOnlyTask task) throws IllegalValueException {
        addressBook.addTask(task);
        updateFilteredTaskList(PREDICATE_SHOW_ALL_TASKS);
        indicateAddressBookChanged();
        if (!task.getDeadline().isEmpty()) {
            indicateCalendarChanged();
        }
    }

    @Override
    public void updateTask(ReadOnlyTask target, ReadOnlyTask editedTask)
            throws DuplicateTaskException, TaskNotFoundException {
        requireAllNonNull(target, editedTask);

        addressBook.updateTask(target, editedTask);
        indicateAddressBookChanged();
        if (!target.getDeadline().equals(editedTask.getDeadline())) {
            indicateCalendarChanged();
        }
    }

    @Override
    public void updateTaskTags(ReadOnlyTask task, Set<Tag> newTags)
            throws TaskNotFoundException, DuplicateTaskException {
        requireAllNonNull(newTags);

        addressBook.updateTaskTags(task, newTags);
        indicateAddressBookChanged();
    }

```
###### /java/seedu/address/model/ModelManager.java
``` java
    /**
     * Returns an unmodifiable view of the list of {@code ReadOnlyTask} backed by the internal list of
     * {@code addressBook}
     */
    @Override
    public ObservableList<ReadOnlyTask> getFilteredTaskList() {
        return FXCollections.unmodifiableObservableList(filteredTasks);
    }

    @Override
    public void updateFilteredTaskList(Predicate<ReadOnlyTask> predicate) {
        requireNonNull(predicate);
        filteredTasks.setPredicate(predicate);
    }
```
###### /java/seedu/address/model/task/Deadline.java
``` java
/**
 * Represents the deadline of a task in the task manager.
 * Guarantees: immutable.
 */
public class Deadline {

    public final String date;

    public Deadline(String date) {
        requireNonNull(date);
        String trimmedDate = date.trim();
        this.date = trimmedDate;
    }

    /**
     * Returns a boolean specifying whether the given deadline date is empty.
     */
    public boolean isEmpty() {
        return date.isEmpty();
    }

    @Override
    public String toString() {
        return date;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Deadline // instanceof handles nulls
                && this.date.equals(((Deadline) other).date)); // state check
    }

    @Override
    public int hashCode() {
        return date.hashCode();
    }

}
```
###### /java/seedu/address/model/task/Description.java
``` java
/**
 * Represents a person's task in the application.
 * Guarantees: immutable; is valid as declared in {@link #isValidDescription(String)}
 */
public class Description {

    public static final String MESSAGE_DESCRIPTION_CONSTRAINTS =
            "Task descriptions should not be blank";

    /*
     * The first character of the description must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String DESCRIPTION_VALIDATION_REGEX = "[\\p{Alnum}][\\p{Alnum} ]*";

    public final String taskDescription;

    /**
     * Validates given task description.
     *
     * @throws IllegalValueException if given name string is invalid.
     */
    public Description(String description) throws IllegalValueException {
        requireNonNull(description);
        String trimmedDescription = description.trim();
        if (!isValidDescription(trimmedDescription)) {
            throw new IllegalValueException(MESSAGE_DESCRIPTION_CONSTRAINTS);
        }
        this.taskDescription = trimmedDescription;
    }

    /**
     * Returns true if a given string is a valid task description.
     */
    public static boolean isValidDescription(String test) {
        return test.matches(DESCRIPTION_VALIDATION_REGEX);
    }


    @Override
    public String toString() {
        return taskDescription;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Description // instanceof handles nulls
                && this.taskDescription.equals(((Description) other).taskDescription)); // state check
    }

    @Override
    public int hashCode() {
        return taskDescription.hashCode();
    }
}
```
###### /java/seedu/address/model/task/exceptions/DuplicateTaskException.java
``` java
/**
 * Signals that the operation will result in duplicate Task objects.
 */
public class DuplicateTaskException extends DuplicateDataException {
    public DuplicateTaskException() {
        super("Operation would result in duplicate tasks");
    }
}
```
###### /java/seedu/address/model/task/exceptions/TaskNotFoundException.java
``` java
/**
 * Signals that the operation is unable to find the specified task.
 */
public class TaskNotFoundException extends Exception {}
```
###### /java/seedu/address/model/task/ReadOnlyTask.java
``` java
/**
 * A read-only immutable interface for a Task in the task manager.
 * Implementations should guarantee: details are present and not null, field values are validated.
 */
public interface ReadOnlyTask {

    ObjectProperty<Description> descriptionProperty();
    Description getDescription();
    ObjectProperty<Deadline> deadlineProperty();
    Deadline getDeadline();
    ObjectProperty<EventTime> startTimeProperty();
    EventTime getStartTime();
    ObjectProperty<EventTime> endTimeProperty();
    EventTime getEndTime();
    ObjectProperty<UniqueTagList> tagProperty();
    Set<Tag> getTags();

    /**
     * Returns true if both have the same state. (interfaces cannot override .equals)
     */
    default boolean isSameStateAs(ReadOnlyTask other) {
        return other == this // short circuit if same object
                || (other != null // this is first to avoid NPE below
                && other.getDescription().equals(this.getDescription()) // state checks here onwards
                && other.getDeadline().equals(this.getDeadline()))
                && other.getStartTime().equals(this.getStartTime())
                && other.getEndTime().equals(this.getEndTime());
    }

    /**
     * Formats the task as text, showing all non-empty task details.
     */
    default String getAsText() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getDescription());
        if (!getDeadline().isEmpty()) {
            builder.append(" Deadline: ").append(getDeadline());
        }
        if (getEndTime().isPresent() && !getStartTime().isPresent()) {
            builder.append(" At: ").append(getEndTime());
        }
        if (getStartTime().isPresent()) {
            builder.append(" At: ").append(getStartTime());
        }
        if (getEndTime().isPresent() && getStartTime().isPresent()) {
            builder.append(" - ").append(getEndTime());
        }
        if (!getTags().isEmpty()) {
            builder.append(" Tags: ");
            getTags().forEach(builder::append);
        }
        return builder.toString();
    }
}
```
###### /java/seedu/address/model/task/Task.java
``` java
/**
 * Represents a Task in the task manager.
 * Guarantees: details are present and not null, field values are validated.
 */
public class Task implements ReadOnlyTask {

    private ObjectProperty<Description> description;
    private ObjectProperty<Deadline> deadline;
    private ObjectProperty<EventTime> startTime;
    private ObjectProperty<EventTime> endTime;
    private ObjectProperty<UniqueTagList> taskTags;

    /**
     * Description must be present and not null.
     */
    public Task(Description description, Deadline deadline, EventTime startTime,
                EventTime endTime, Set<Tag> taskTags) {
        requireAllNonNull(description, startTime, endTime, deadline);
        this.description = new SimpleObjectProperty<>(description);
        this.deadline = new SimpleObjectProperty<>(deadline);
        this.startTime = new SimpleObjectProperty<>(startTime);
        this.endTime = new SimpleObjectProperty<>(endTime);
        // protect internal tags from changes in the arg list
        this.taskTags = new SimpleObjectProperty<>(new UniqueTagList(taskTags));
    }

    /**
     * Creates a copy of the given ReadOnlyTask.
     */
    public Task(ReadOnlyTask source) {
        this(source.getDescription(), source.getDeadline(), source.getStartTime(),
                source.getEndTime(), source.getTags());
    }

    public void setDescription(Description description) {
        this.description.set(requireNonNull(description));
    }

    @Override
    public ObjectProperty<Description> descriptionProperty() {
        return description;
    }

    @Override
    public Description getDescription() {
        return description.get();
    }

    public void setDeadline(Deadline deadline) {
        this.deadline.set(requireNonNull(deadline));
    }

    @Override
    public ObjectProperty<Deadline> deadlineProperty() {
        return deadline;
    }

    @Override
    public Deadline getDeadline() {
        return deadline.get();
    }

    @Override
    public ObjectProperty<EventTime> startTimeProperty() {
        return startTime;
    }

    @Override
    public EventTime getStartTime() {
        return startTime.get();
    }

    public void setStartTime(EventTime startTime) {
        this.startTime.set(requireNonNull(startTime));
    }

    @Override
    public ObjectProperty<EventTime> endTimeProperty() {
        return endTime;
    }

    @Override
    public EventTime getEndTime() {
        return endTime.get();
    }

    public void setEndTime(EventTime endTime) {
        this.endTime.set(requireNonNull(endTime));
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    @Override
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(taskTags.get().toSet());
    }

    public ObjectProperty<UniqueTagList> tagProperty() {
        return taskTags;
    }

    /**
     * Replaces this task's tags with the tags in the argument tag set.
     */
    public void setTags(Set<Tag> replacement) {
        taskTags.set(new UniqueTagList(replacement));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ReadOnlyTask // instanceof handles nulls
                && this.isSameStateAs((ReadOnlyTask) other));
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, deadline, startTime, endTime);
    }

    @Override
    public String toString() {
        return getAsText();
    }
}
```
###### /java/seedu/address/model/task/UniqueTaskList.java
``` java
/**
 * A list of tasks that enforces uniqueness between its elements and does not allow nulls.
 *
 * Supports a minimal set of list operations.
 *
 * @see Task#equals(Object)
 * @see CollectionUtil#elementsAreUnique(Collection)
 */

public class UniqueTaskList implements Iterable<Task> {

    private final ObservableList<Task> internalList = FXCollections.observableArrayList();
    // used by asObservableList()
    private final ObservableList<ReadOnlyTask> mappedList = EasyBind.map(internalList, (task) -> task);

    /**
     * Returns true if the list contains an equivalent task as the given argument.
     */
    public boolean contains(ReadOnlyTask toCheck) {
        requireNonNull(toCheck);
        return internalList.contains(toCheck);
    }

    /**
     * Adds a task to the list.
     *
     * @throws DuplicateTaskException if the task to add is a duplicate of an existing task in the list.
     */
    public void add(ReadOnlyTask toAdd) throws DuplicateTaskException {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateTaskException();
        }
        internalList.add(new Task(toAdd));
    }

    /**
     * Replaces the task {@code target} in the list with {@code editedTask}.
     *
     * @throws DuplicateTaskException if the replacement is equivalent to another existing task in the list.
     * @throws TaskNotFoundException if {@code target} could not be found in the list.
     */
    public void setTask(ReadOnlyTask target, ReadOnlyTask editedTask)
            throws DuplicateTaskException, TaskNotFoundException {
        requireNonNull(editedTask);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new TaskNotFoundException();
        }

        if (!target.equals(editedTask) && internalList.contains(editedTask)) {
            throw new DuplicateTaskException();
        }

        internalList.set(index, new Task(editedTask));
    }

    /**
     * Removes the equivalent task from the list.
     *
     * @throws TaskNotFoundException if no such task could be found in the list.
     */
    public boolean remove(ReadOnlyTask toRemove) throws TaskNotFoundException {
        requireNonNull(toRemove);
        final boolean taskFoundAndDeleted = internalList.remove(toRemove);
        if (!taskFoundAndDeleted) {
            throw new TaskNotFoundException();
        }
        return taskFoundAndDeleted;
    }

    public void setTasks(UniqueTaskList replacement) {
        this.internalList.setAll(replacement.internalList);
    }

    public void setTasks(List<? extends ReadOnlyTask> tasks) throws DuplicateTaskException {
        final UniqueTaskList replacement = new UniqueTaskList();
        for (final ReadOnlyTask task : tasks) {
            replacement.add(new Task(task));
        }
        setTasks(replacement);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<ReadOnlyTask> asObservableList() {
        sortTasks(internalList);
        return FXCollections.unmodifiableObservableList(mappedList);
    }

```
###### /java/seedu/address/storage/XmlAdaptedTask.java
``` java
/**
 * JAXB-friendly version of the Task.
 */
public class XmlAdaptedTask {
    @XmlElement(required = true)
    private String description;
    @XmlElement
    private String deadline;
    @XmlElement
    private String startTime;
    @XmlElement
    private String endTime;

    @XmlElement
    private List<XmlAdaptedTag> tagged = new ArrayList<>();


    /**
     * Constructs an XmlAdaptedTask.
     * This is the no-arg constructor that is required by JAXB.
     */
    public XmlAdaptedTask() {}


    /**
     * Converts a given Task into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created XmlAdaptedTask
     */
    public XmlAdaptedTask(ReadOnlyTask source) {
        description = source.getDescription().taskDescription;
        deadline = source.getDeadline().date;
        startTime = source.getStartTime().time;
        endTime = source.getEndTime().time;
        tagged = new ArrayList<>();
        for (Tag tag : source.getTags()) {
            tagged.add(new XmlAdaptedTag(tag));
        }
    }

    /**
     * Converts this jaxb-friendly adapted task object into the model's Task object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted task
     */
    public Task toModelType() throws IllegalValueException {
        final List<Tag> taskTags = new ArrayList<>();
        for (XmlAdaptedTag tag : tagged) {
            taskTags.add(tag.toModelType());
        }
        final Description description = new Description(this.description);
        final Deadline deadline = new Deadline(this.deadline);
        final EventTime startTime = new EventTime(this.startTime);
        final EventTime endTime = new EventTime(this.endTime);
        final Set<Tag> tags = new HashSet<>(taskTags);
        return new Task(description, deadline, startTime, endTime, tags);
    }
}
```
###### /java/seedu/address/ui/ResultDisplay.java
``` java
    @Subscribe
    private void handleNewResultAvailableEvent(NewResultAvailableEvent event) {
        if (event.isInvalid) {
            setStyleToIndicateCommandFailure();
        } else {
            setStyleToDefault();
        }
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        Platform.runLater(() -> displayed.setValue(event.message));
    }
```
###### /java/seedu/address/ui/TaskCard.java
``` java
        logger.info("Binding UI elements.");
```
###### /java/seedu/address/ui/TaskCard.java
``` java
        if (task.getStartTime().isPresent() && task.getEndTime().isPresent()) {
            StringBinding binding = Bindings.createStringBinding(() -> MessageFormat.format("{0} - {1}",
                task.getStartTime(), task.getEndTime(), task.startTimeProperty()), task.endTimeProperty());
            time.textProperty().bind(binding);
        } else if (task.getStartTime().isPresent()) {
            time.textProperty().bind(Bindings.convert(task.startTimeProperty()));
        } else {
            time.textProperty().bind(Bindings.convert(task.endTimeProperty()));
        }
```
