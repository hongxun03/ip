package chatbot.task;

/**
 * The <code>ToDo</code> class is a subclass of the <code>Task</code> class.
 * The <code>ToDo</code> object only has a description and completion status.
 *
 * @author hongxun03
 */
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
