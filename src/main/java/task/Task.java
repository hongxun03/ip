package task;

public class Task {
    private static final String LINE = "\t____________________________________________________________";
    public String taskName;
    public boolean completed;

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
