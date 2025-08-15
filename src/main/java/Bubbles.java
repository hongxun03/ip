import task.*;

import java.util.ArrayList;
import java.util.Scanner;

public class Bubbles {
    private static final String LINE = "\t____________________________________________________________";
    private final String name;
    private ArrayList<Task> taskList;

    public Bubbles(String name) {
        this.name = name;
        this.taskList = new ArrayList<>();
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
                    markTask(command, Integer.parseInt(arg));
                    break;
                case "unmark":
                    unMarkTask(command, Integer.parseInt(arg));
                    break;
                case "bye":
                    bye();
                    return;
                default:
                    addTask(command, arg);
            }
        }
    }

    public void addTask(String command, String arg) {
        switch (command) {
            case "todo":
                taskList.add(new ToDo(arg));
                break;
            case "deadline":
                String[] split1 = arg.split("/by ");
                taskList.add(new Deadline(split1[0], split1[1]));
                break;
            case "event":
                String[] fromSplit = arg.split("/from ");
                String[] bySplit = fromSplit[1].split("/to ");
                taskList.add(new Event(fromSplit[0], bySplit[0], bySplit[1]));
                break;
        }
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

    public void markTask(String command, int arg) {
        taskList.get(arg - 1).setCompleted();
        System.out.println(LINE);
        System.out.println("\t Nice! I've marked this task as done:\n\t\t" + taskList.get(arg - 1).toString());
        System.out.println(LINE);
    }

    public void unMarkTask(String command, int arg) {
        taskList.get(arg - 1).unComplete();
        System.out.println(LINE);
        System.out.println("\t OK, I've marked this task as not done yet:\n\t\t" + taskList.get(arg - 1).toString());
        System.out.println(LINE);
    }

    public static void main(String[] args) {
        Bubbles bubbles = new Bubbles("Bubbles");
        bubbles.greetings();
        bubbles.echo();
    }
}
