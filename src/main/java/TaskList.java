import task.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.util.ArrayList;

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
                throw new TaskException("Enter the time of the deadline. For example, deadline study /by 03/08 1800");
            }

            try {
                DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                        .parseDefaulting(ChronoField.YEAR, LocalDate.now().getYear())
                        .appendPattern("dd/MM HHmm")
                        .toFormatter();
                tasks.add(new Deadline(split1[0], LocalDateTime.parse(split1[1], formatter)));
                break;
            } catch (DateTimeParseException e) {
                System.out.println("Enter a valid date and time, format: DD/MM HHMM");
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
                DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                        .parseDefaulting(ChronoField.YEAR, LocalDate.now().getYear())
                        .appendPattern("dd/MM HHmm")
                        .toFormatter();
                tasks.add(new Event(fromSplit[0],
                        LocalDateTime.parse(bySplit[0], formatter),
                        LocalDateTime.parse(bySplit[1], formatter)));
                break;
            } catch(DateTimeParseException e) {
                System.out.println("Enter a valid date and time, format: DD/MM HHMM");
                return;
            }

        default:
            throw new TaskException("I don't understand that command.");
        }
        storage.save(tasks);
        int listSize = tasks.size();
        System.out.println(LINE + "\n\t Got it. I've added this task:");
        System.out.println("\t\t" + tasks.get(listSize - 1).toString());
        System.out.println("\t Now you have " + listSize + (listSize == 1 ? " task" : " tasks")
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
        try {
            Task task = tasks.get(Integer.parseInt(arg) - 1);
            task.setCompleted();
            storage.save(tasks);
            System.out.println("\t Nice! I've marked this task as done:\n\t\t" + task.toString());
        } catch (NumberFormatException e) {
            System.out.println("\tWhoops! Indicate the task number to be marked as completed. For example, mark 2.");
        } catch (IndexOutOfBoundsException e) {
            if (tasks.isEmpty()) {
                System.out.println("\tWhoops! You need to add a task first.");
            } else {
                System.out.println("\tWhoops! Enter a number between 1 and " + tasks.size() + ".");
            }
        }
        System.out.println(LINE);
    }

    public void unMarkTask(String arg) {
        System.out.println(LINE);
        try {
            Task task = tasks.get(Integer.parseInt(arg) - 1);
            task.unComplete();
            storage.save(tasks);
            System.out.println("\t OK, I've marked this task as not done yet:\n\t\t" + task.toString());
        } catch (NumberFormatException e) {
            System.out.println("\tWhoops! Indicate the task number to be marked as incomplete. For example, unmark 2.");
        } catch (IndexOutOfBoundsException e) {
            if (tasks.isEmpty()) {
                System.out.println("\tWhoops! You need to add a task first.");
            } else {
                System.out.println("\tWhoops! Enter a number between 1 and " + tasks.size() + ".");
            }
        }
        System.out.println(LINE);
    }

    public void deleteTask(String arg) {
        System.out.println(LINE);
        try {
            Task task =  tasks.get(Integer.parseInt(arg) - 1);
            tasks.remove(task);
            storage.save(tasks);
            System.out.println("\t Noted. I've deleted this task from your list:\n\t\t" + task.toString());
            int listSize = tasks.size();
            System.out.println("\t Now you have " + listSize + (listSize == 1 ? " task" : " tasks")
                    + " remaining.");
        } catch (NumberFormatException e) {
            System.out.println("\tWhoops! Indicate the task number to be deleted. For example, delete 2.");
        } catch (IndexOutOfBoundsException e) {
            if (tasks.isEmpty()) {
                System.out.println("\tWhoops! You need to add a task first.");
            } else {
                System.out.println("\tWhoops! Enter a number between 1 and " + tasks.size() + ".");
            }
        }
        System.out.println(LINE);
    }
}
