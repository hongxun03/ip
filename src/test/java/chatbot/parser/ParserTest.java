package chatbot.parser;

import chatbot.task.TaskException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.format.DateTimeParseException;

public class ParserTest {

    @Test
    public void formatDate_correctFormat_LocalDateTime() {
        Assertions.assertDoesNotThrow(() -> {Parser.formatDate("13/05 1300");});
    }

    @Test
    public void formatDate_wrongFormat_exceptionThrown() {
        Assertions.assertThrows(DateTimeParseException.class, () -> {Parser.formatDate("abc");});
    }

    @Test
    public void formatDate_outOfBounds_exceptionThrown() {
        Assertions.assertThrows(DateTimeParseException.class, () -> {Parser.formatDate("50/03 1500");});
        Assertions.assertThrows(DateTimeParseException.class, () -> {Parser.formatDate("15/13 1500");});
        Assertions.assertThrows(DateTimeParseException.class, () -> {Parser.formatDate("05/20 2500");});
    }

    @Test
    public void parseTaskIndex_correct_success() {
        Assertions.assertDoesNotThrow(() -> {
            Assertions.assertEquals(4, Parser.parseTaskIndex("5", 5));
        });
    }

    // parseTaskIndex when listSize = 0 throws Exception in TaskList. Test there instead.

    @Test
    public void parseTaskIndex_inputZero_exceptionThrown() {
        // Throws TaskException("Enter a number between 1 and 0."
        Assertions.assertThrows(TaskException.class, () -> {Parser.parseTaskIndex("0", 1);});
    }

    @Test
    public void parseTaskIndex_exceedSize_exceptionThrown() {
        Assertions.assertThrows(TaskException.class, () -> {Parser.parseTaskIndex("6", 5);});
    }

    @Test
    public void parseTaskIndex_NaN_exceptionThrown() {
        Assertions.assertThrows(TaskException.class, () -> {Parser.parseTaskIndex("string", 5);});
    }
}
