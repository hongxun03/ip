package chatbot;

import chatbot.task.TaskList;

/**
 * The <code>UI</code> represents the interactive portion of the chatbot to receive commands from user.
 *
 * @author hongxun03
 */
public class Ui {
    private static final String NAME = "Bubbles";
    private TaskList tasks;

    public Ui(TaskList tasks) {
        this.tasks = tasks;
    }

    /**
     * Introduces chatbot and lists out tasks stored.
     */
    public String start() {
        return "Hello! I'm " + NAME + "!\n" + tasks.listTasks() + "\nWhat can I do for you?";
    }

    /**
     * Ends the chatbot with a parting message.
     */
    public String bye() {
        return "Bye. Hope to see you again soon!";
    }
}
