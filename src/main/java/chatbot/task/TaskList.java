package chatbot.task;

import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import chatbot.parser.Parser;
import chatbot.storage.Storage;

/**
 * A <code>TaskList</code> object represents an <code>ArrayList</code> of tasks.
 * This <code>ArrayList</code> is read from a <code>Storage</code> object.
 *
 * @author hongxun03
 */
public class TaskList {
    private final ArrayList<Task> tasks;
    private Storage storage;

    /**
     * Constructor to create a list of <code>Task</code>s.
     *
     * @param tasks An <code>ArrayList</code> of <code>Task</code>s.
     * @param storage The <code>Storage</code> of previous <code>Task</code>s.
     */
    public TaskList(ArrayList<Task> tasks, Storage storage) {
        this.tasks = tasks;
        this.storage = storage;
    }

    /**
     * Performs an operation based on the input of the user.
     *
     * <p>
     *     If command is list: List out all current <code>Task</code>s.<br>
     *     If command is mark: Mark the specified <code>Task</code> as completed.<br>
     *     If command is unmark: Mark the specified <code>Task</code> as uncompleted.<br>
     *     If command is delete: Delete the specified <code>Task</code>.<br>
     *     Otherwise, command must be an add operation. {@link #addTask(String, String)}
     *     </p>
     *
     * @param message The input from the user.
     * @return The output message after performing operation.
     */
    public String op(String message) {
        String[] parts = message.split(" ", 2);
        String command = parts[0];
        String arg = (parts.length > 1) ? parts[1] : "";

        switch (command) {
        case "list":
            return listTasks();
        case "mark":
            return markTask(arg);
        case "unmark":
            return unMarkTask(arg);
        case "delete":
            return deleteTask(arg);
        case "find":
            return findTasks(arg);
        default:
            try {
                return addTask(command, arg);
            } catch (TaskException e) {
                return e.toString();
            }
        }
    }

    /**
     * Adds a new <code>Task</code> and stores it in the <code>Storage</code>.
     *
     * <p>
     *     If command is <code>ToDo</code>: Create new <code>ToDo</code> object.<br>
     *     If command is <code>Deadline</code>: Create new <code>Deadline</code> object.<br>
     *     If command is <code>Event</code>: Create new <code>Event</code> object.<br>
     *     Else: chatbot cannot recognise command.
     * </p>
     *
     * @param command The <code>Task</code> to be added.
     * @param arg The description of the <code>Task</code>.
     * @return The message of the operation.
     * @throws TaskException If input is in wrong format or not recognised.
     */
    public String addTask(String command, String arg) throws TaskException {
        switch (command) {
        case "todo":
            if (arg.isEmpty()) {
                throw new TaskException("Enter the description of the todo.");
            }
            tasks.add(new ToDo(arg));
            break;

        case "deadline":
            if (arg.isEmpty()) {
                throw new TaskException("Enter the description of the deadline.");
            }
            String[] split1 = arg.split(" /by ");
            if (split1.length == 1) {
                throw new TaskException("Enter the time of the deadline. Usage: deadline study /by 03/08 1800");
            }

            try {
                tasks.add(new Deadline(split1[0], Parser.formatDate(split1[1])));
                break;
            } catch (DateTimeParseException e) {
                return "Enter a valid date and time, format: DD/MM HHMM";
            }

        case "event":
            if (arg.isEmpty()) {
                throw new TaskException("Enter the description of the event.");
            }
            String[] fromSplit = arg.split(" /from ");
            if (fromSplit.length == 1) {
                throw new TaskException(
                        "Enter the start time of the event. Usage: event meeting /from 03/08 1300 /to 03/08 1400");
            }
            String[] bySplit = fromSplit[1].split(" /to ");
            if (bySplit.length == 1) {
                throw new TaskException(
                        "Enter the end time of the event. Usage: event meeting /from 03/08 1300 /to 03/08 1400");
            }

            try {
                tasks.add(new Event(fromSplit[0],
                        Parser.formatDate(bySplit[0]),
                        Parser.formatDate(bySplit[1])));
                break;
            } catch (DateTimeParseException e) {
                return "Enter a valid date and time, format: DD/MM HHMM";
            }

        default:
            throw new TaskException("I don't understand that command.");
        }
        storage.save(tasks);
        int listSize = tasks.size();
        return "Got it. I've added this task:\n\t"
                + tasks.get(listSize - 1)
                + "\nNow you have "
                + listSize
                + (listSize == 1 ? "task" : " tasks")
                + " in the list.";
    }

    /**
     * Returns all the tasks that have a matching keyword in its description.
     * The arg argument specifies the specific keyword to match.
     *
     * @param arg The keyword.
     * @return A list of tasks with matching keyword.
     */
    public String findTasks(String arg) {
        if (arg.isEmpty()) {
            return "Whoops! Specify a keyword for me to find the tasks.";
        }

        ArrayList<Task> matchingTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.taskName.contains(arg)) {
                matchingTasks.add(task);
            }
        }

        if (matchingTasks.isEmpty()) {
            return "There are no matching tasks in your list.";
        } else {
            int listSize = matchingTasks.size();
            StringBuilder output = new StringBuilder();
            output.append("There are ")
                    .append(listSize)
                    .append(" matching tasks in your list.");
            for (int i = 0; i < listSize; i++) {
                output.append("\n\t ")
                        .append(i + 1)
                        .append(". ")
                        .append(matchingTasks.get(i).toString());
            }
            return output.toString();
        }
    }

    /**
     * Returns a list of all current <code>Task</code>s.
     *
     * @return A list of all current tasks.
     */
    public String listTasks() {
        // No assert statement for empty task list needed as it is alright for user to
        // request a display of empty task list.

        StringBuilder output = new StringBuilder("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            output.append("\n\t ")
                    .append(i + 1)
                    .append(". ")
                    .append(tasks.get(i).toString());
        }
        return output.toString();
    }

    /**
     * Marks the current <code>Task</code> as completed.
     * The arg argument specifies the index in the <code>TaskList</code>.
     *
     * @param arg The string representation of the index.
     * @return The output message of marking the task.
     */
    public String markTask(String arg) {

        if (tasks.isEmpty()) {
            return "Whoops! You need to add a task first.";
        }

        try {
            int index = Parser.parseTaskIndex(arg, tasks.size()); // index bounds checks handled in parserTaskIndex

            Task task = tasks.get(index);
            assert task != null;

            task.setCompleted();
            storage.save(tasks);
            return "Nice! I've marked this task as done:\n\t" + task.toString();
        } catch (TaskException e) {
            return "Whoops! " + e.getMessage();
        }
    }

    /**
     * Marks the current <code>Task</code> as uncompleted.
     * The arg argument specifies the index in the <code>TaskList</code>.
     *
     * @param arg The string representation of the index.
     * @return The output message of unmarking the task.
     */
    public String unMarkTask(String arg) {

        if (tasks.isEmpty()) {
            return "Whoops! You need to add a task first.";
        }

        try {
            int index = Parser.parseTaskIndex(arg, tasks.size()); // index bounds check handled in parseTaskIndex

            Task task = tasks.get(index);
            assert task != null;

            task.unComplete();
            storage.save(tasks);
            return "OK, I've marked this task as not done yet:\n\t" + task.toString();
        } catch (TaskException e) {
            return "Whoops! " + e.getMessage();
        }
    }

    /**
     * Deletes the current <code>Task</code>.
     * The arg argument specifies the index in the <code>TaskList</code>.
     *
     * @param arg The string representation of the index.
     * @return The task that is deleted.
     */
    public String deleteTask(String arg) {

        if (tasks.isEmpty()) {
            return "Whoops! You need to add a task first.";
        }

        try {
            int size = tasks.size();
            int index = Parser.parseTaskIndex(arg, size); // index bounds checked in parseTaskIndex

            Task task = tasks.get(index);
            assert task != null;

            tasks.remove(task);
            storage.save(tasks);

            size = tasks.size();
            return new StringBuilder("Noted. I've deleted this task from your list:\n\t")
                    .append(task.toString())
                    .append("\n Now you have ")
                    .append(size)
                    .append(size == 1 ? "task remaining." : " tasks remaining.")
                    .toString();
        } catch (TaskException e) {
            return "Whoops! " + e.getMessage();
        }
    }
}
