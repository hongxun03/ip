package chatbot;

import chatbot.storage.Storage;
import chatbot.task.TaskList;

/**
 * The <code>Bubbles</code> program is the main implementation of the chatbot.
 *
 * <p>
 * It makes use of its <code>UI</code>, <code>TaskList</code> and <code>Storage</code> to properly handle and
 * store user input.
 * </p>
 *
 * @author hongxun03
 *
 */
public class Bubbles {
    private Ui ui;
    private TaskList tasks;
    private Storage storage;

    public Bubbles(String filePath) {
        storage = new Storage(filePath);
        tasks = new TaskList(storage.load(), storage);
        ui = new Ui(tasks);
    }

    /**
     * Runs the chatbot.
     */
    public void run() {
        ui.start();
        ui.getInput();
    }

    public static void main(String[] args) {
        new Bubbles("./data/Bubbles.txt").run();
    }
}
