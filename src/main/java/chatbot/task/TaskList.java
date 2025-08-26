package chatbot.task;

import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import chatbot.parser.Parser;
import chatbot.storage.Storage;

public class TaskList {
    private static final String LINE = "\t____________________________________________________________";
    private final ArrayList<Task> tasks;
    private Storage storage;

    public TaskList(ArrayList<Task> tasks, Storage storage) {
        this.tasks = tasks;
        this.storage = storage;
    }

    public void op(String message) {
        String[] parts =  message.split(" ", 2);
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
        default:
            try {
                addTask(command, arg);
            } catch (TaskException e) {
                System.out.println(LINE + "\n" + e.toString() + "\n" + LINE);
            }
        }
    }

    public void addTask(String command, String arg) throws TaskException{
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
            } catch(DateTimeParseException e) {
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

    public void listTasks() {
        System.out.println(LINE);
        System.out.println("\t Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println("\t " + (i + 1) + ". " + tasks.get(i).toString());
        }
        System.out.println(LINE);
    }

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
            Task task =  tasks.get(index);
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
