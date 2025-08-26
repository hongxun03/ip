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
    public void dateToString_today_onlyTime() {
        Deadline dToday = new Deadline("due today", LocalDateTime.of(today, LocalTime.of(12, 35)));
        Assertions.assertEquals("12:35pm", dToday.dateToString());
    }

    @Test
    public void dateToString_tomorrow_tomAndTime() {
        Deadline dTom = new Deadline("due tomorrow", LocalDateTime.of(today.plusDays(1),
                LocalTime.of(0, 35)));
        Assertions.assertEquals("tomorrow 12:35am", dTom.dateToString());
    }

    @Test
    public void dateToString_week_dayAndTime() {
        Deadline dThurs = new Deadline("due this week", LocalDateTime.of(monday.plusDays(3),
                LocalTime.of(9, 40)));
        Assertions.assertEquals("Thu 9:40am", dThurs.dateToString());

        Deadline dSun = new Deadline("due this week", LocalDateTime.of(monday.plusDays(6),
                LocalTime.of(23, 5)));
        Assertions.assertEquals("Sun 11:05pm", dSun.dateToString());
    }

    @Test
    public void dateToString_future_dateAndTime() {
        Deadline dl = new Deadline("future", LocalDateTime.of(2025,
                12, 12, 8, 0));
        Assertions.assertEquals("12 Dec 8:00am", dl.dateToString());
    }

    // saveString indirectly tested in StorageTest

    @Test
    public void toString_correctFormat() {
        Deadline dl = new Deadline("assignment", LocalDateTime.of(2025,
                12, 12, 8, 0));

        Assertions.assertEquals("[D][X] assignment (12 Dec 8:00am)", dl.toString());

        // Placing both assertions as class:To-Do already tested both separately
        dl.setCompleted();

        Assertions.assertEquals("[D][✓] assignment (12 Dec 8:00am)", dl.toString());
    }
}
