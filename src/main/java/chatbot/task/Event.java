package chatbot.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * The <code>Event</code> class is a subclass of the <code>Task</code> class.
 * The <code>Event</code> object has a description, completion status, start dateTime and end dateTime.
 *
 * @author hongxun03
 */
public class Event extends Task {
    protected LocalDateTime fromDateTime;
    protected LocalDateTime dueDateTime;

    /**
     * Constructor to create a <code>Event</code> task
     *
     * @param taskName description of task.
     * @param fromDateTime starting date and time of task.
     * @param dueDateTime ending date and time of task.
     */
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
