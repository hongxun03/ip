package chatbot;

import java.util.Scanner;

import chatbot.task.TaskList;

public class Ui {
    private static final String LINE = "\t____________________________________________________________";
    private static final String NAME = "Bubbles";
    private TaskList tasks;

    public Ui(TaskList tasks) {
        this.tasks = tasks;
    }

    public void start() {
        System.out.println(LINE + "\n\tHello! I'm " + NAME);
        tasks.listTasks();
        System.out.println("\tWhat can I do for you?\n" + LINE);
    }

    public void getInput() {
        Scanner scanner = new Scanner(System.in);
        String message;

        while (true) {
            message = scanner.nextLine().trim();
            if (message.isEmpty()) {
                continue;
            } else if (message.equalsIgnoreCase("bye")) {
                bye();
                scanner.close();
                break;
            }

            tasks.op(message);
        }
    }

    private void bye() {
        System.out.println(LINE + "\n\tBye. Hope to see you again soon!\n" + LINE);
    }
}
