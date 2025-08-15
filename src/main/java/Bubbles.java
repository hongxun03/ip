import java.util.Scanner;

public class Bubbles {
    private static final String LINE = "\t____________________________________________________________";
    private final String name;

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

    public void echo() {
        Scanner scanner = new Scanner(System.in);
        String message;
        while (true) {
            message = scanner.nextLine().trim();
            if (message.equals("bye")) {
                scanner.close();
                break;
            }
            System.out.println(LINE + "\n\t" + message + "\n" + LINE);
        }
    }

    public static void main(String[] args) {
        Bubbles bubbles = new Bubbles("Bubbles");
        bubbles.greetings();
        bubbles.echo();
        bubbles.bye();
    }
}
