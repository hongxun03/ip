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
    private static final String LINE = "\t____________________________________________________________";
    private final ArrayList<Task> tasks;
    private Storage storage;

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
     */
    public void op(String message) {
        String[] parts = message.split(" ", 2);
        String command = parts[0];
        String arg = (parts.length > 1) ? parts[1] : "";

        switch (command) {
        case "list":
            listTasks();
            break;
        case "mark":
            markTask(arg);
            break;
        case "unmark":
            unMarkTask(arg);
            break;
        case "delete":
            deleteTask(arg);
            break;
        case "find":
            findTasks(arg);
            break;
        default:
            try {
                addTask(command, arg);
            } catch (TaskException e) {
                System.out.println(LINE + "\n" + e.toString() + "\n" + LINE);
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
     * @throws TaskException if input is in wrong format or not recognised.
     */
    public void addTask(String command, String arg) throws TaskException {
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
                System.out.println(LINE);
                System.out.println("\t Enter a valid date and time, format: DD/MM HHMM");
                System.out.println(LINE);
                return;
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
                System.out.println(LINE);
                System.out.println("\t Enter a valid date and time, format: DD/MM HHMM");
                System.out.println(LINE);
                return;
            }

        default:
            throw new TaskException("I don't understand that command.");
        }
        storage.save(tasks);
        int listSize = tasks.size();
        System.out.println(LINE + "\n\t Got it. I've added this task:");
        System.out.println("\t\t" + tasks.get(listSize - 1).toString());
        System.out.println("\t Now you have " + listSize + (listSize == 1 ? "chatbot/task" : " tasks")
                + " in the list.\n" + LINE);
    }

    /**
     * Prints out all the tasks that have a matching keyword in its description.
     * The arg argument specifies the specific keyword to match.
     *
     * @param arg The keyword
     */
    public void findTasks(String arg) {
        System.out.println(LINE);

        if (arg.isEmpty()) {
            System.out.println("\t Whoops! Specify a keyword for us to find the tasks.");
            System.out.println(LINE);
            return;
        }

        ArrayList<Task> matchingTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.taskName.contains(arg)) {
                matchingTasks.add(task);
            }
        }

        if (matchingTasks.isEmpty()) {
            System.out.println("\t There are no matching tasks in your list.");
        } else {
            int listSize = matchingTasks.size();
            System.out.println("\t There are " + listSize + " matching tasks in your list.");
            for (int i = 0; i < listSize; i++) {
                System.out.println("\t " + (i + 1) + ". " + matchingTasks.get(i).toString());
            }
        }

        System.out.println(LINE);
    }

    /**
     * Prints out all current <code>Task</code>s.
     */
    public void listTasks() {
        System.out.println(LINE);
        System.out.println("\t Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println("\t " + (i + 1) + ". " + tasks.get(i).toString());
        }
        System.out.println(LINE);
    }

    /**
     * Marks the current <code>Task</code> as completed.
     * The arg argument specifies the index in the <code>TaskList</code>.
     *
     * @param arg The string representation of the index.
     */
    public void markTask(String arg) {
        System.out.println(LINE);

        if (tasks.isEmpty()) {
            System.out.println("\t Whoops! You need to add a task first.");
            System.out.println(LINE);
            return;
        }

        try {
            int index = Parser.parseTaskIndex(arg, tasks.size());
            Task task = tasks.get(index);
            task.setCompleted();
            storage.save(tasks);
            System.out.println("\t Nice! I've marked this task as done:\n\t\t" + task.toString());
        } catch (TaskException e) {
            System.out.println("\t Whoops! " + e.getMessage());
        }
        System.out.println(LINE);
    }

    /**
     * Marks the current <code>Task</code> as uncompleted.
     * The arg argument specifies the index in the <code>TaskList</code>.
     *
     * @param arg The string representation of the index.
     */
    public void unMarkTask(String arg) {
        System.out.println(LINE);

        if (tasks.isEmpty()) {
            System.out.println("\t Whoops! You need to add a task first.");
            System.out.println(LINE);
            return;
        }

        try {
            int index = Parser.parseTaskIndex(arg, tasks.size());
            Task task = tasks.get(index);
            task.unComplete();
            storage.save(tasks);
            System.out.println("\t OK, I've marked this task as not done yet:\n\t\t" + task.toString());
        } catch (TaskException e) {
            System.out.println("\t Whoops! " + e.getMessage());
        }
        System.out.println(LINE);
    }

    /**
     * Deletes the current <code>Task</code>.
     * The arg argument specifies the index in the <code>TaskList</code>.
     *
     * @param arg The string representation of the index.
     */
    public void deleteTask(String arg) {
        System.out.println(LINE);

        if (tasks.isEmpty()) {
            System.out.println("\t Whoops! You need to add a task first.");
            System.out.println(LINE);
            return;
        }

        try {
            int size = tasks.size();
            int index = Parser.parseTaskIndex(arg, size);
            Task task = tasks.get(index);
            tasks.remove(task);
            storage.save(tasks);

            size = tasks.size();
            System.out.println("\t Noted. I've deleted this task from your list:\n\t\t" + task.toString());
            System.out.println("\t Now you have " + size + (size == 1 ? "chatbot/task" : " tasks")
                    + " remaining.");
        } catch (TaskException e) {
            System.out.println("\t Whoops! " + e.getMessage());
        }
        System.out.println(LINE);
    }
}
