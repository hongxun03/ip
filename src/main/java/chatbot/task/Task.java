package chatbot.task;

/**
 * The <code>Task</code> class defines the implementation and methods of its subclasses.
 *
 * @author hongxun03
 */
public abstract class Task {
    protected final String taskName;
    protected boolean isCompleted;

    public Task(String taskName) {
        this.taskName = taskName;
        this.isCompleted = false;
    }

    /**
     * Sets the field isCompleted to true.
     */
    public void setCompleted() {
        this.isCompleted = true;
    }

    /**
     * Sets the field isCompleted to false.
     */
    public void unComplete() {
        this.isCompleted = false;
    }

    public abstract String saveString();

    /**
     * Returns the <code>Task</code> as a formatted string displaying the description and completion status.
     * @return A string displaying its fields.
     */
    public String toString() {
        return (this.isCompleted ? "[✓]" : "[X]") + " " + this.taskName;
    }
}
