package chatbot.task;

import java.time.LocalDate;

/**
 * The <code>Task</code> class defines the implementation and methods of its subclasses.
 *
 * @author hongxun03
 */
public abstract class Task {
    protected final String taskName;
    protected boolean isCompleted;

    /**
     * Constructor to create a <code>Task</code>.
     *
     * @param taskName description of task.
     */
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

    // ChatGPT helped me improve polymorphism by encouraging me to move time conflict logic
    // checks into Task subclasses instead.
    // In particular, tasks that don't override this method won't conflict with the queried time.
    public boolean conflictsWith(LocalDate date) {
        return false;
    }

    public boolean conflictsWithin(LocalDate start, LocalDate end) {
        return false;
    }

    /**
     * Returns the <code>Task</code> as a formatted string displaying the description and completion status.
     * @return A string displaying its fields.
     */
    public String toString() {
        return (this.isCompleted ? "[✓]" : "[X]") + " " + this.taskName;
    }
}
