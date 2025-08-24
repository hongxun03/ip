import task.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Scanner;

public class Bubbles {
    private static final String LINE = "\t____________________________________________________________";
    private final String name;
    private ArrayList<Task> taskList;
    private Storage storage;

    public Bubbles(String name) {
        this.name = name;
        storage = new Storage("./data/Bubbles.txt");
        taskList = storage.load();
    }

    public void greetings() {
        System.out.println(LINE + "\n\tHello! I'm " + name);
        System.out.println("\tWhat can I do for you?\n" + LINE);
    }

    public void bye() {
        System.out.println(LINE + "\n\tBye. Hope to see you again soon!\n" + LINE);
    }

    public void echo() {
        Scanner scanner = new Scanner(System.in);
        String message;
        while (true) {
            message = scanner.nextLine().trim();
            if (message.isEmpty()) continue;

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
            case "bye":
                bye();
                return;
            case "delete":
                deleteTask(arg);
                break;
            default:
                try {
                    addTask(command, arg);
                } catch (TaskException todoE) {
                    System.out.println(LINE + "\n" + todoE.toString() + "\n" + LINE);
                }
            }
        }
    }

    public void addTask(String command, String arg) throws TaskException{
        switch (command) {
        case "todo":
            if (arg.isEmpty()) {
                throw new TaskException("Enter the description of the todo.");
            }
            taskList.add(new ToDo(arg));
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
                taskList.add(new Deadline(split1[0], LocalDateTime.parse(split1[1], formatter)));
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
                taskList.add(new Event(fromSplit[0],
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
        storage.save(taskList);
        int listSize = taskList.size();
        System.out.println(LINE + "\n\t Got it. I've added this task:");
        System.out.println("\t\t" + taskList.get(listSize - 1).toString());
        System.out.println("\t Now you have " + listSize + (listSize == 1 ? " task" : " tasks")
                + " in the list.\n" + LINE);
    }

    public void listTasks() {
        System.out.println(LINE);
        System.out.println("\t Here are the tasks in your list:");
        for (int i = 0; i < taskList.size(); i++) {
            System.out.println("\t " + (i + 1) + ". " + taskList.get(i).toString());
        }
        System.out.println(LINE);
    }

    public void markTask(String arg) {
        System.out.println(LINE);
        try {
            Task task = taskList.get(Integer.parseInt(arg) - 1);
            task.setCompleted();
            storage.save(taskList);
            System.out.println("\t Nice! I've marked this task as done:\n\t\t" + task.toString());
        } catch (NumberFormatException e) {
            System.out.println("\tWhoops! Indicate the task number to be marked as completed. For example, mark 2.");
        } catch (IndexOutOfBoundsException e) {
            if (taskList.isEmpty()) {
                System.out.println("\tWhoops! You need to add a task first.");
            } else {
                System.out.println("\tWhoops! Enter a number between 1 and " + taskList.size() + ".");
            }
        }
        System.out.println(LINE);
    }

    public void unMarkTask(String arg) {
        System.out.println(LINE);
        try {
            Task task = taskList.get(Integer.parseInt(arg) - 1);
            task.unComplete();
            storage.save(taskList);
            System.out.println("\t OK, I've marked this task as not done yet:\n\t\t" + task.toString());
        } catch (NumberFormatException e) {
            System.out.println("\tWhoops! Indicate the task number to be marked as incomplete. For example, unmark 2.");
        } catch (IndexOutOfBoundsException e) {
            if (taskList.isEmpty()) {
                System.out.println("\tWhoops! You need to add a task first.");
            } else {
                System.out.println("\tWhoops! Enter a number between 1 and " + taskList.size() + ".");
            }
        }
        System.out.println(LINE);
    }

    public void deleteTask(String arg) {
        System.out.println(LINE);
        try {
            Task task =  taskList.get(Integer.parseInt(arg) - 1);
            taskList.remove(task);
            storage.save(taskList);
            System.out.println("\t Noted. I've deleted this task from your list:\n\t\t" + task.toString());
            int listSize = taskList.size();
            System.out.println("\t Now you have " + listSize + (listSize == 1 ? " task" : " tasks")
                    + " remaining.");
        } catch (NumberFormatException e) {
            System.out.println("\tWhoops! Indicate the task number to be deleted. For example, delete 2.");
        } catch (IndexOutOfBoundsException e) {
            if (taskList.isEmpty()) {
                System.out.println("\tWhoops! You need to add a task first.");
            } else {
                System.out.println("\tWhoops! Enter a number between 1 and " + taskList.size() + ".");
            }
        }
        System.out.println(LINE);
    }

    public static void main(String[] args) {
        Bubbles bubbles = new Bubbles("Bubbles");
        bubbles.greetings();
        bubbles.listTasks();
        bubbles.echo();
    }
}
