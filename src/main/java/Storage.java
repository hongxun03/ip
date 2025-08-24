import task.*;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Storage {
    private String filePath;

    public Storage(String filePath) {
        this.filePath = filePath;
    }

    public ArrayList<Task> load() {
        ArrayList<Task> tasks = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("Error creating file: " + e.getMessage());
            }
        } else {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null)  {
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
        return tasks;
    }

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
        String type = lineSplit[0];
        boolean isCompleted = lineSplit[1].equals("✓");
        String desc =  lineSplit[2];

        return switch (type) {
            case "T" -> new ToDo(desc);
            case "D" -> new Deadline(desc, Parser.parseDate(lineSplit[3]));
            case "E" -> {
                String[] dateSplit = lineSplit[3].split(" - ");
                yield new Event(desc,
                        LocalDateTime.parse(dateSplit[0]),
                        LocalDateTime.parse(dateSplit[1]));
            }
            default -> null;
        };
    }
}
