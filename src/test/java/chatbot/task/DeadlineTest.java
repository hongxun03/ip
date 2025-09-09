package chatbot.task;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/* All tests are compared with lowercase due to different format in different platforms.
   AM/PM case sensitivity doesn't actually matter for user experience, and this is only for testing.
 */
public class DeadlineTest {

    private LocalDate today = LocalDate.now(ZoneId.of("Asia/Singapore"));
    private LocalDate monday = today.with(java.time.temporal.TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

    @Test
    public void dateToString_today_onlyTime() {
        Deadline dToday = new Deadline("due today", LocalDateTime.of(today, LocalTime.of(12, 35)));
        Assertions.assertEquals("12:35pm".toLowerCase(), dToday.dateToString().toLowerCase());
    }

    @Test
    public void dateToString_tomorrow_tomAndTime() {
        Deadline dTom = new Deadline("due tomorrow", LocalDateTime.of(today.plusDays(1),
                LocalTime.of(0, 35)));
        Assertions.assertEquals("tomorrow 12:35am".toLowerCase(), dTom.dateToString().toLowerCase());
    }

    @Test
    public void dateToString_week_dayAndTime() {
        Deadline dThurs = new Deadline("due this week", LocalDateTime.of(monday.plusDays(3),
                LocalTime.of(9, 40)));
        Assertions.assertEquals("Thu 9:40am".toLowerCase(), dThurs.dateToString().toLowerCase());

        Deadline dSun = new Deadline("due this week", LocalDateTime.of(monday.plusDays(6),
                LocalTime.of(23, 5)));
        Assertions.assertEquals("Sun 11:05pm".toLowerCase(), dSun.dateToString().toLowerCase());
    }

    @Test
    public void dateToString_future_dateAndTime() {
        Deadline dl = new Deadline("future", LocalDateTime.of(2025,
                12, 12, 8, 0));
        Assertions.assertEquals("12 Dec 8:00am".toLowerCase(), dl.dateToString().toLowerCase());
    }

    // saveString indirectly tested in StorageTest

    @Test
    public void toString_correctFormat() {
        Deadline dl = new Deadline("assignment", LocalDateTime.of(2025,
                12, 12, 8, 0));

        Assertions.assertEquals("[D][X] assignment (12 Dec 8:00am)".toLowerCase(), dl.toString().toLowerCase());

        // Placing both assertions as class:To-Do already tested both separately
        dl.setCompleted();

        Assertions.assertEquals("[D][✓] assignment (12 Dec 8:00am)".toLowerCase(), dl.toString().toLowerCase());
    }
}
