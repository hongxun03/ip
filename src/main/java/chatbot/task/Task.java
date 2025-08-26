package chatbot.task;

public abstract class Task {
    protected final String taskName;
    protected boolean isCompleted;

    public Task(String taskName) {
        this.taskName  = taskName;
        this.isCompleted = false;
    }

    public void setCompleted() {
        this.isCompleted = true;
    }

    public void unComplete() {
        this.isCompleted = false;
    }

    public abstract String saveString();

    public String toString() {
        return (this.isCompleted ? "[✓]" : "[X]") + " " + this.taskName;
    }
}
