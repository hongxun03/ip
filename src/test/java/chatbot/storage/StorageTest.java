package chatbot.storage;

import java.util.ArrayList;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import chatbot.task.Task;
import chatbot.task.ToDo;

public class StorageTest {
    @Test
    public void testStorage_loadEmptyFile_emptyList() {
        String filePath = "./data/EmptyBubbles.txt";

        Storage storage = new Storage(filePath);
        ArrayList<Task> tasks = storage.load();

        Assertions.assertTrue(tasks.isEmpty());
    }

    @Test
    public void testStorage_loadSampleFile_listOfSize3() {
        String filePath = "./data/SampleBubbles.txt";

        Storage storage = new Storage(filePath);
        ArrayList<Task> tasks = storage.load();

        Assertions.assertEquals(3, tasks.size());
    }

    @Test
    public void testStorage_saveTaskToEmptyFile_listOfSize1() {
        String filePath = "./data/EmptyBubbles.txt";

        Storage storage = new Storage(filePath);
        ArrayList<Task> tasks = storage.load();

        tasks.add(new ToDo("study"));
        storage.save(tasks);
        tasks = storage.load();

        Assertions.assertEquals(1, tasks.size());

        tasks.remove(0);
        storage.save(tasks);
    }
}
