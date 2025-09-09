package chatbot.storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import chatbot.parser.Parser;
import chatbot.task.Deadline;
import chatbot.task.Event;
import chatbot.task.Task;
import chatbot.task.TaskException;
import chatbot.task.ToDo;

/**
 * A <code>Storage</code> object helps to store the tasks inputted by the user. The <code>Storage</code> object
 * can load and save the list of tasks.
 *
 * @author hongxun03
 */
public class Storage {
    private String filePath;

    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads the list of tasks stored by the text file.
     *
     * @return An <code>ArrayList</code> of tasks
     */
    public ArrayList<Task> load() {
        ArrayList<Task> tasks = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) {
            createFile(file);
        } else {
            readFile(file, tasks);
        }
        return tasks;
    }

    /**
     * Saves the current lists of tasks into a text file.
     *
     * @param tasks An <code>ArrayList</code> of tasks
     */
    public void save(ArrayList<Task> tasks) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (Task task : tasks) {
                bw.write(task.saveString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }

    private Task parseTask(String line) throws TaskException {
        String[] lineSplit = line.split(" \\| ");
        assert lineSplit.length >= 3;

        String type = lineSplit[0];
        boolean isCompleted = !lineSplit[1].equals("X");
        String desc = lineSplit[2];

        Task task = switch (type) {
        case "T" -> new ToDo(desc);
        case "D" -> new Deadline(desc, Parser.parseDate(lineSplit[3]));
        case "E" -> {
            String[] dateSplit = lineSplit[3].split(" - ");
            assert dateSplit.length == 2;

            yield new Event(desc,
                    LocalDateTime.parse(dateSplit[0]),
                    LocalDateTime.parse(dateSplit[1]));
        }
        default -> null;
        };

        if (task != null && isCompleted) {
            task.setCompleted();
        }

        return task;
    }

    /**
     * Create a storage file if it doesn't already exist.
     *
     * @param file The specified file to create.
     */
    private void createFile(File file) {
        try {
            file.getParentFile().mkdirs();
            file.createNewFile();
        } catch (IOException e) {
            System.out.println("Error creating file: " + e.getMessage());
        }
    }

    /**
     * Reads the tasks in the existing file and places it into the current <code>TaskList</code>.
     *
     * @param file The storage file.
     * @param tasks An <code>ArrayList</code> of tasks.
     */
    private void readFile(File file, ArrayList<Task> tasks) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                Task task = parseTask(line);
                if (task != null) {
                    tasks.add(task);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading file: " + e.getMessage());
        } catch (TaskException e) {
            System.out.println(e.getMessage());
        }
    }
}
