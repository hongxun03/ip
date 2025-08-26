package chatbot.parser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;

import chatbot.task.TaskException;

/**
 * The <code>Parser</code> class provides a useful way to handle user input.
 *
 * <p>
 * A <code>Parser</code> only has static methods.
 * </p>
 *
 * @author hongxun03
 */
public class Parser {

    private static final DateTimeFormatter DATE_FORMAT = new DateTimeFormatterBuilder()
            .parseDefaulting(ChronoField.YEAR, LocalDate.now().getYear())
            .appendPattern("dd/MM HHmm")
            .toFormatter();

    private static final DateTimeFormatter SAVE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    /**
     * Returns a <code>LocalDateTime</code> which can be used to create a new
     * <code>Deadline</code> or <code>Event</code> object.
     * The date argument must be in the exact form of DD/MM HHmm
     *
     * @param date The start and end date of the task.
     * @return The <code>LocalDateTime</code> of the specified date.
     * @throws DateTimeParseException If date is of an incorrect format.
     */
    public static LocalDateTime formatDate(String date) throws DateTimeParseException {
        return LocalDateTime.parse(date, DATE_FORMAT);
    }

    /**
     * Returns an integer to access the index to delete, mark or unmark the specified task.
     * The arg argument must specify the particular index. The listSize argument must specify the current size
     * of the <code>ArrayList</code>
     *
     * @param arg The index to perform the operation on.
     * @param listSize The size of the ArrayList
     * @return An integer to index to.
     * @throws TaskException if index is <=0 or >listSize
     */
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

    /**
     * Returns a <code>LocalDateTime</code> so that the <code>Storage</code> object can create a new
     * <code>Deadline</code> or <code>Event</code> task from the text file.
     * The input argument is read from the text file.
     *
     * @param input The <code>LocalDateTime</code> stored as a string.
     * @return A <code>LocalDateTime</code>
     * @throws TaskException if text file is corrupted.
     */
    public static LocalDateTime parseDate(String input) throws TaskException {
        try {
            return LocalDateTime.parse(input.trim(), SAVE_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new TaskException("Corrupted save file: invalid date format -> " + input);
        }
    }

}
