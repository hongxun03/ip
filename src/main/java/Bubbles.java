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
        System.out.println(LINE + "\n\tHello! I'm " + name + " \uD83E\uDEE7\uD83E\uDEE7");
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

            String[] parts = message.split(" ");
            String command = parts[0];
            String arg = (parts.length > 1) ? parts[1] : "";

            switch (command) {
                case "list":
                    listTasks();
                    break;
                case "mark":
                    taskList.get(Integer.parseInt(arg) - 1).setCompleted();
                    break;
                case "unmark":
                    taskList.get(Integer.parseInt(arg) - 1).unComplete();
                    break;
                case "bye":
                    bye();
                    return;
                default:
                    addTask(message);
            }
        }
    }

    public void addTask(String message) {
        taskList.add(new Task(message));
        System.out.println(LINE + "\n\t added: " + message + "\n" + LINE);
    }

    public void listTasks() {
        System.out.println(LINE);
        System.out.println("\t Here are the tasks in your list:");
        for (int i = 0; i < taskList.size(); i++) {
            System.out.println("\t " + (i + 1) + ". " + taskList.get(i).toString());
        }
        System.out.println(LINE);
    }

    public static void main(String[] args) {
        Bubbles bubbles = new Bubbles("Bubbles");
        bubbles.greetings();
        bubbles.echo();
    }
}
