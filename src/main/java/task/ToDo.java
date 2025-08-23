package task;

public class ToDo extends Task {

    public ToDo(String taskName) {
        super(taskName);
    }

    @Override
    public String saveString() {
        return "T | " + (this.isCompleted ? "✓" : "X")
                + " | " + this.taskName;
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
