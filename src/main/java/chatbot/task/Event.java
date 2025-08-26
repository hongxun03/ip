package chatbot.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Event extends Task {
    protected LocalDateTime fromDateTime;
    protected LocalDateTime dueDateTime;

    public Event(String taskName, LocalDateTime fromDateTime, LocalDateTime dueDateTime) {
        super(taskName);
        this.fromDateTime = fromDateTime;
        this.dueDateTime = dueDateTime;
    }

    protected String dateToString(LocalDateTime date) {
        return date.format(DateTimeFormatter.ofPattern("d MMM h:mma"));
    }

    @Override
    public String saveString() {
        return "E | " + (this.isCompleted ? "✓" : "X")
                + " | " + this.taskName
                + " | " + this.fromDateTime + " - " + this.dueDateTime;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (" + dateToString(fromDateTime)
                + " - " + dateToString(dueDateTime) + ")";
    }
}
