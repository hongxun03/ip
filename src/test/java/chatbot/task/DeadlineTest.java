package chatbot.task;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

public class DeadlineTest {

    LocalDate today = LocalDate.now(ZoneId.of("Asia/Singapore"));
    LocalDate monday = today.with(java.time.temporal.TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

    @Test
    public void testDeadline_dateToStringToday_onlyTime() {
        Deadline dToday = new Deadline("due today", LocalDateTime.of(today, LocalTime.of(12, 35)));
        Assertions.assertEquals("12:35pm", dToday.dateToString());
    }

    @Test
    public void testDeadline_dateToStringTomorrow_tomAndTime() {
        Deadline dTom = new Deadline("due tomorrow", LocalDateTime.of(today.plusDays(1),
                LocalTime.of(0, 35)));
        Assertions.assertEquals("tomorrow 12:35am", dTom.dateToString());
    }

    @Test
    public void testDeadline_dateToStringWeek_dayAndTime() {
        Deadline dThurs = new Deadline("due this week", LocalDateTime.of(monday.plusDays(3),
                LocalTime.of(9, 40)));
        Assertions.assertEquals("Thu 9:40am", dThurs.dateToString());
    }

    @Test
    public void testDeadline_dateToStringFuture_dateAndTime() {
        Deadline dl = new Deadline("future", LocalDateTime.of(2025,
                12, 12, 8, 0));
        Assertions.assertEquals("12 Dec 8:00am", dl.dateToString());
    }

    // saveString indirectly tested in StorageTest

    @Test
    public void testDeadline_toString_correctFormat() {
        Deadline dl = new Deadline("assignment", LocalDateTime.of(2025,
                12, 12, 8, 0));

        Assertions.assertEquals("[D][X] assignment (12 Dec 8:00am)", dl.toString());

        dl.setCompleted();

        Assertions.assertEquals("[D][✓] assignment (12 Dec 8:00am)", dl.toString());
    }
}
