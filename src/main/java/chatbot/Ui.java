package chatbot;

import java.util.Scanner;

import chatbot.task.TaskList;

/**
 * The <code>UI</code> represents the interactive portion of the chatbot to receive commands from user.
 *
 * @author hongxun03
 */
public class Ui {
    private static final String LINE = "\t____________________________________________________________";
    private static final String NAME = "Bubbles";
    private TaskList tasks;

    public Ui(TaskList tasks) {
        this.tasks = tasks;
    }

    /**
     * Introduces chatbot and lists out tasks stored.
     */
    public void start() {
        System.out.println(LINE + "\n\tHello! I'm " + NAME);
        tasks.listTasks();
        System.out.println("\tWhat can I do for you?\n" + LINE);
    }

    /**
     * Receives input from user and sends to TaskList to perform the operation.
     * If input is bye, chatbot is stopped.
     */
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

    /**
     * Ends the chatbot with a parting message.
     */
    private void bye() {
        System.out.println(LINE + "\n\tBye. Hope to see you again soon!\n" + LINE);
    }
}
