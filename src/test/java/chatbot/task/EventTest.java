package chatbot.task;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class EventTest {
    @Test
    public void testEvent_dateToString() {
        LocalDateTime start = LocalDateTime.of(2025, 5, 5, 9, 30);
        LocalDateTime end = LocalDateTime.of(2025, 12, 13, 22, 5);

        Event event = new Event("triathlon", start, end);

        Assertions.assertEquals("5 May 9:30am", event.dateToString(event.fromDateTime));
        Assertions.assertEquals("13 Dec 10:05pm", event.dateToString(event.dueDateTime));
    }

    @Test
    public void testDeadline_toString() {
        Deadline dl = new Deadline("assignment", LocalDateTime.of(2025,
                12, 12, 8, 0));

        Assertions.assertEquals("[D][X] assignment (12 Dec 8:00am)", dl.toString());

        dl.setCompleted();

        Assertions.assertEquals("[D][✓] assignment (12 Dec 8:00am)", dl.toString());
    }
}
