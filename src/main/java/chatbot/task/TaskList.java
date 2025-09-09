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
     * Returns a string based on the operation performed.
     *
     * @param message The input from the user.
     * @return The output message after performing operation.
     */
    public String op(String message) {
        Command command = parseCommand(message);
        return executeCommand(command);
    }

    private static class Command {
        private final String name;
        private final String argument;

        public Command(String name, String argument) {
            this.name = name;
            this.argument = argument;
        }

        public String getName() {
            return name;
        }

        public String getArgument() {
            return argument;
        }
    }

    // Helper methods for Command operations.
    private Command parseCommand(String message) {
        String[] parts = message.split(" ", 2);
        String commandName = parts[0];
        String argument = (parts.length > 1) ? parts[1] : "";
        return new Command(commandName, argument);
    }

    private String executeCommand(Command command) {
        String arg = command.getArgument();

        return switch (command.getName()) {
        case "list" -> listTasks();
        case "mark" -> markTask(arg);
        case "unmark" -> unMarkTask(arg);
        case "delete" -> deleteTask(arg);
        case "find" -> findTasks(arg);
        default -> handleTaskCreation(command);
        };
    }

    private String handleTaskCreation(Command command) {
        try {
            return addTask(command.getName(), command.getArgument());
        } catch (TaskException e) {
            return e.toString();
        }
    }

    /**
     * Adds a new <code>Task</code> and stores it in the <code>Storage</code>.
     *
     * @param command The <code>Task</code> to be added.
     * @param arg The description of the <code>Task</code>.
     * @return The message of the operation.
     * @throws TaskException If input is in wrong format or not recognised.
     */
    public String addTask(String command, String arg) throws TaskException {
        Task task = createTask(command, arg);
        addTaskToList(task);
        return successAddMessage(task);
    }

    private Task createTask(String command, String arg) throws TaskException {
        return switch (command) {
        case "todo" -> createTodoTask(arg);
        case "deadline" -> createDeadlineTask(arg);
        case "event" -> createEventTask(arg);
        default -> throw new TaskException("I don't understand that command.");
        };
    }

    private Task createTodoTask(String arg) throws TaskException {
        validateNotEmpty(arg, "Enter the description of the todo.");
        return new ToDo(arg);
    }

    private Task createDeadlineTask(String arg) throws TaskException {
        validateNotEmpty(arg, "Enter the description of the deadline.");

        String[] parts = parseDeadlineArguments(arg);
        String description = parts[0];
        String dateString = parts[1];

        try {
            return new Deadline(description, Parser.formatDate(dateString));
        } catch (DateTimeParseException e) {
            return handleDateParseError();
        }
    }

    private Task createEventTask(String arg) throws TaskException {
        validateNotEmpty(arg, "Enter the description of the event.");

        String[] parts = parseEventArguments(arg);
        String description = parts[0];
        String startDateString = parts[1];
        String endDateString = parts[2];

        try {
            return new Event(description,
                    Parser.formatDate(startDateString),
                    Parser.formatDate(endDateString));
        } catch (DateTimeParseException e) {
            return handleDateParseError();
        }
    }

    private void validateNotEmpty(String arg, String errorMessage) throws TaskException {
        if (arg.isEmpty()) {
            throw new TaskException(errorMessage);
        }
    }

    private String[] parseDeadlineArguments(String arg) throws TaskException {
        String[] split = arg.split(" /by ");
        if (split.length == 1) {
            throw new TaskException("Enter the time of the deadline. Usage: deadline study /by 03/08 1800");
        }
        return split;
    }

    private String[] parseEventArguments(String arg) throws TaskException {
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

        return new String[]{fromSplit[0], bySplit[0], bySplit[1]};
    }

    private Task handleDateParseError() throws TaskException {
        throw new TaskException("Enter a valid date and time, format: DD/MM HHMM");
    }

    private void addTaskToList(Task task) {
        tasks.add(task);
        storage.save(tasks);
    }

    private String successAddMessage(Task task) {
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
     * The keyword argument specifies the specific keyword to match.
     *
     * @param keyword The keyword.
     * @return A list of tasks with matching keyword.
     */
    public String findTasks(String keyword) {
        if (keyword.isEmpty()) {
            return "Whoops! Specify a keyword for me to find the tasks.";
        }

        ArrayList<Task> matchingTasks = searchTasksByKeyword(keyword);
        return searchResultsMessage(matchingTasks).toString();
    }

    /**
     * Returns a list of all current <code>Task</code>s.
     *
     * @return A list of all current tasks.
     */
    public String listTasks() {
        return buildTaskList(tasks, new StringBuilder("Here are the tasks in your list:"))
                .toString();
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
            Task task = changeCompletionStatus(arg, true);
            return buildTaskMarkedMessage(task, true);
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
            Task task = changeCompletionStatus(arg, false);
            return buildTaskMarkedMessage(task, false);
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
            Task task = getTask(arg);
            removeTask(task);
            return taskDeleteMessage(task);
        } catch (TaskException e) {
            return "Whoops! " + e.getMessage();
        }
    }

    // Helper methods for task operations.
    private ArrayList<Task> searchTasksByKeyword(String keyword) {
        ArrayList<Task> matchingTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.taskName.contains(keyword)) {
                matchingTasks.add(task);
            }
        }
        return matchingTasks;
    }

    private Task getTask(String arg) throws TaskException {
        int index = Parser.parseTaskIndex(arg, tasks.size());
        return tasks.get(index);
    }

    private Task changeCompletionStatus(String arg, boolean mark) throws TaskException {
        Task task = getTask(arg);
        if (mark) {
            task.setCompleted();
        } else {
            task.unComplete();
        }
        storage.save(tasks);
        return task;
    }

    private void removeTask(Task task) {
        tasks.remove(task);
        storage.save(tasks);
    }

    // Helper methods for message building
    private StringBuilder searchResultsMessage(ArrayList<Task> matchingTasks) {
        if (matchingTasks.isEmpty()) {
            return new StringBuilder("There are no matching tasks in your list.");
        }

        StringBuilder output = new StringBuilder("There are ");
        output.append(matchingTasks.size())
                .append(" matching tasks in your list.");
        return buildTaskList(matchingTasks, output);
    }

    private StringBuilder buildTaskList(ArrayList<Task> taskList, StringBuilder header) {
        for (int i = 0; i < taskList.size(); i++) {
            header.append("\n\t ")
                    .append(i + 1)
                    .append(". ")
                    .append(taskList.get(i).toString());
        }
        return header;
    }

    private String buildTaskMarkedMessage(Task task, boolean mark) {
        if (mark) {
            return "Nice! I've marked this task as done:\n\t" + task.toString();
        } else {
            return "OK, I've marked this task as not done yet:\n\t" + task.toString();
        }
    }

    private String taskDeleteMessage(Task task) {
        int remainingTasks = tasks.size();
        return new StringBuilder("Noted. I've deleted this task from your list:\n\t")
                .append(task.toString())
                .append("\n Now you have ")
                .append(remainingTasks)
                .append(remainingTasks == 1 ? " task remaining." : " tasks remaining.")
                .toString();
    }
}
