package chatbot.task;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/* All tests are compared with lowercase due to different format in different platforms.
   AM/PM case sensitivity doesn't actually matter for user experience, and this is only for testing.
 */
public class EventTest {
    @Test
    public void dateToString_dateAndTime_success() {
        LocalDateTime start = LocalDateTime.of(2025, 5, 5, 9, 30);
        LocalDateTime end = LocalDateTime.of(2025, 12, 13, 22, 5);

        Event event = new Event("triathlon", start, end);

        Assertions.assertEquals("5 May 9:30am".toLowerCase(), event.dateToString(event.fromDateTime).toLowerCase());
        Assertions.assertEquals("13 Dec 10:05pm".toLowerCase(), event.dateToString(event.dueDateTime).toLowerCase());
    }

    // saveString indirectly tested in StorageTest

    @Test
    public void toString_correctFormat() {
        Event e = new Event("training",
                LocalDateTime.of(2025, 3, 1, 8, 0),
                LocalDateTime.of(2025, 11, 12, 23, 59));

        Assertions.assertEquals("[E][X] training (1 Mar 8:00am - 12 Nov 11:59pm)".toLowerCase(),
                e.toString().toLowerCase());

        // Placing both assertions as class:To-Do already tested both separately
        e.setCompleted();

        Assertions.assertEquals("[E][✓] training (1 Mar 8:00am - 12 Nov 11:59pm)".toLowerCase(),
                e.toString().toLowerCase());
    }
}
