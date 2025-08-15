package task;

public class Task {
    protected final String taskName;
    protected boolean completed;

    public Task(String taskName) {
        this.taskName  = taskName;
        this.completed = false;
    }

    public void setCompleted() {
        this.completed = true;
    }

    public void unComplete() {
        this.completed = false;
    }

    public String toString() {
        return (this.completed ? "[X]" : "[ ]") + " " + this.taskName;
    }
}
