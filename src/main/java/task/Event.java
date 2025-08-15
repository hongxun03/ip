package task;

public class Event extends Task {
    protected String fromDate;
    protected String dueDate;

    public Event(String taskName, String fromDate, String dueDate) {
        super(taskName);
        this.fromDate = fromDate;
        this.dueDate = dueDate;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + this.fromDate + "to: " + this.dueDate + ")";
    }
}
