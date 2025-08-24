import task.*;

import java.util.Scanner;

public class Bubbles {
    private Ui ui;
    private TaskList tasks;
    private Storage storage;

    public Bubbles(String filePath) {
        storage = new Storage(filePath);
        tasks = new TaskList(storage.load(), storage);
        ui = new Ui(tasks);
    }

    public void run() {
        ui.start();
        ui.getInput();
    }

    public static void main(String[] args) {
        new Bubbles("./data/Bubbles.txt").run();
    }
}
