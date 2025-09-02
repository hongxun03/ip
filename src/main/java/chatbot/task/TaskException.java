package chatbot.task;

/**
 * The class <code>TaskException</code> is a subclass of <code>Exception</code>.
 * It is thrown whenever a command for a <code>Task</code> and its subclasses is unknown or has wrong usage.
 */
public class TaskException extends Exception {
    private Task task;

    public TaskException(String message) {
        super(message);
    }

    /**
     * Constructor for a <code>TaskException</code>.
     * The message parameter is the message alerted to the user when the <code>TaskException</code> is thrown.
     * The message task refers to the type of <code>Task</code>.
     *
     * @param message The message provided to the output.
     * @param task The type of task.
     */
    public TaskException(String message, Task task) {
        super(message);
        this.task = task;
    }

    @Override
    public String toString() {
        return "Oops! " + super.getMessage();
    }
}
