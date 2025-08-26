package chatbot.task;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

/**
 * The <code>Event</code> class is a subclass of the <code>Task</code> class.
 * The <code>Event</code> object has a description, completion status, start dateTime and end dateTime.
 *
 * @author hongxun03
 */
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
