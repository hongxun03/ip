package chatbot;

import chatbot.storage.Storage;
import chatbot.task.TaskList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class UiTest {
    @Test
    public void testUi_emptyFile_noTasks() {
        String filePath = "./data/EmptyBubbles.txt";

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Storage storage = new Storage(filePath);
        Ui ui = new Ui(new TaskList(storage.load(), storage));
        ui.start();

        String output = outContent.toString();
        String[] expectedLines = {
                "Hello! I'm Bubbles",
                "Here are the tasks in your list:",
                "What can I do for you?"
        };

        for (String expectedLine : expectedLines) {
            Assertions.assertTrue(output.contains(expectedLine),
                    "Output should contain: " + expectedLine + "\nActual output:\n" + output);
        }
    }

    @Test
    public void testUi_sampleFile_3TasksShown() {
        String filePath = "./data/SampleBubbles.txt";

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Storage storage = new Storage(filePath);
        Ui ui = new Ui(new TaskList(storage.load(), storage));
        ui.start();

        String output = outContent.toString();
        String[] expectedLines = {
                "Hello! I'm Bubbles",
                "Here are the tasks in your list:",
                "1. [T][X] read",
                "2. [D][✓] homework (5 Oct 2:00pm)",
                "3. [E][X] hackathon (10 Oct 12:00pm - 15 Oct 12:00pm)",
                "What can I do for you?"
        };

        for (String expectedLine : expectedLines) {
            Assertions.assertTrue(output.contains(expectedLine),
                    "Output should contain: " + expectedLine + "\nActual output:\n" + output);
        }
    }
}
