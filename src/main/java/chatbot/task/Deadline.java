package chatbot.task;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class Deadline extends Task {
    protected LocalDateTime dueDate;

    public Deadline(String taskName, LocalDateTime dueDate) {
        super(taskName);
        this.dueDate = dueDate;
    }

    protected String dateToString() {
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Singapore"));
        LocalDate monday = today.with(java.time.temporal.TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate sunday = today.with(java.time.temporal.TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        LocalDate date = this.dueDate.toLocalDate();

        return (date.isEqual(today)
                ? this.dueDate.format(DateTimeFormatter.ofPattern("h:mma"))
                : date.isEqual(today.plusDays(1))
                ? "tomorrow " + this.dueDate.format(DateTimeFormatter.ofPattern("h:mma"))
                : (!date.isBefore(monday) && !date.isAfter(sunday))
                ? this.dueDate.format(DateTimeFormatter.ofPattern("EEE h:mma"))
                : this.dueDate.format(DateTimeFormatter.ofPattern("d MMM h:mma")));
    }

    @Override
    public String saveString() {
        return "D | " + (this.isCompleted ? "✓" : "X")
                + " | " + this.taskName + " | " + this.dueDate;
    }

    @Override
    public String toString() {
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Singapore"));
        LocalDate monday = today.with(java.time.temporal.TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate sunday = today.with(java.time.temporal.TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        return "[D]" + super.toString() + " (" + dateToString() + ")";
    }
}
