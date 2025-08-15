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
        System.out.println(LINE);
        System.out.println("\t Nice! I've marked this task as done:\n\t\t" + this);
        System.out.println(LINE);
    }

    public void unComplete() {
        this.completed = false;
        System.out.println(LINE);
        System.out.println("\t OK, I've marked this task as not done yet:\n\t\t" + this);
        System.out.println(LINE);
    }

    public String toString() {
        return (this.completed ? "[X]" : "[ ]") + " " + this.taskName;
    }
}
