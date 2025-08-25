package chatbot.parser;

import chatbot.task.TaskException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;

public class Parser {

    private static final DateTimeFormatter DATE_FORMAT = new DateTimeFormatterBuilder()
            .parseDefaulting(ChronoField.YEAR, LocalDate.now().getYear())
            .appendPattern("dd/MM HHmm")
            .toFormatter();

    private static final DateTimeFormatter SAVE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public static LocalDateTime formatDate(String date) throws DateTimeParseException {
        return LocalDateTime.parse(date, DATE_FORMAT);
    }

    public static int parseTaskIndex(String arg, int listSize) throws TaskException {
        try {
            int index = Integer.parseInt(arg) - 1;
            if (index < 0 || index >= listSize) {
                throw new TaskException("Enter a number between 1 and " + listSize + ".");
            }
            return index;
        } catch (NumberFormatException e) {
            throw new TaskException("Indicate the task number. For example, mark 2.");
        }
    }

    public static LocalDateTime parseDate(String input) throws TaskException {
        try {
            return LocalDateTime.parse(input.trim(), SAVE_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new TaskException("Corrupted save file: invalid date format -> " + input);
        }
    }

}
