import java.util.ArrayList;
import java.util.Scanner;

public class Bubbles {
    private static final String LINE = "\t____________________________________________________________";
    private final String name;
    private ArrayList<String> tasks = new ArrayList<>();

    public Bubbles(String name) {
        this.name = name;
    }

    public void greetings() {
        System.out.println(LINE + "\n\tHello! I'm " + name + " \uD83E\uDEE7\uD83E\uDEE7");
        System.out.println("\tWhat can I do for you?\n" + LINE);
    }

    public void bye() {
        System.out.println(LINE + "\n\tBye. Hope to see you again soon!\n" + LINE);
    }

    public void addTask() {
        Scanner scanner = new Scanner(System.in);
        String message;
        while (true) {
            message = scanner.nextLine().trim();
            if (message.equals("list")) {
                listTasks();
            } else if (message.equals("bye")) {
                scanner.close();
                break;
            } else {
                tasks.add(message);
                System.out.println(LINE + "\n\t added: " + message + "\n" + LINE);
            }
        }
    }

    public void listTasks() {
        System.out.println(LINE);
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println("\t" + (i + 1) + ". " + tasks.get(i));
        }
        System.out.println(LINE);
    }

    public static void main(String[] args) {
        Bubbles bubbles = new Bubbles("Bubbles");
        bubbles.greetings();
        bubbles.addTask();
        bubbles.bye();
    }
}
