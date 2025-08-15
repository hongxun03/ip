package task;

public class TaskException extends Exception {
    Task task;

    public TaskException(String message) {
        super(message);
    }

    public TaskException(String message, Task task) {
        super(message);
        this.task = task;
    }

    @Override
    public String toString() {
        return "\tOops! " + super.getMessage();
    }
}
