package chatbot.task;

public class TaskException extends Exception {
    private Task task;

    public TaskException(String message) {
        super(message);
    }

    public TaskException(String message, Task task) {
        super(message);
        this.task = task;
    }

    @Override
    public String toString() {
        return "\t Oops! " + super.getMessage();
    }
}
