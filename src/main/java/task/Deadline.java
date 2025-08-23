package task;

public class Deadline extends Task {
    protected String dueDate;

    public Deadline(String taskName, String dueDate) {
        super(taskName);
        this.dueDate = dueDate;
    }

    @Override
    public String saveString() {
        return "D | " + (this.isCompleted ? "✓" : "X")
                + " | " + this.taskName + " | " + this.dueDate;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + this.dueDate + ")";
    }
}
