package session;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import tasks.Deadline;
import tasks.Event;
import tasks.Task;
import tasks.Todo;

/** Reads and writes the task list using a small, escaped text format. */
public class TaskStorage {
    /** Creates a storage helper. */
    public TaskStorage() {
    }

    /**
     * Loads tasks from a file, treating a missing file as an empty list.
     *
     * @param path file to read
     * @return tasks decoded from the file
     */
    public List<Task> load(Path path) {
        try {
            List<Task> tasks = new ArrayList<>();
            for (String line : Files.readAllLines(path)) {
                Task task = parse(line);
                if (task != null) tasks.add(task);
            }
            return tasks;
        } catch (IOException exception) {
            return new ArrayList<>();
        }
    }

    /**
     * Saves all tasks, creating the parent directory when necessary.
     *
     * @param path file to write
     * @param taskList tasks to encode
     * @throws IOException if the file cannot be written
     */
    public void save(Path path, TaskList taskList) throws IOException {
        if (path.getParent() != null) Files.createDirectories(path.getParent());
        List<String> lines = new ArrayList<>();
        for (int i = 1; i <= taskList.size(); i++) lines.add(format(taskList.get(i)));
        Files.write(path, lines, StandardCharsets.UTF_8);
    }

    private String format(Task task) {
        StringBuilder line = new StringBuilder(task.storageType()).append('|').append(task.isDone() ? '1' : '0');
        line.append('|').append(encode(task.getDescription()));
        for (String detail : task.getStorageDetails()) line.append('|').append(encode(detail));
        return line.toString();
    }

    private Task parse(String line) {
        String[] parts = line.split("\\|", -1);
        if (parts.length < 3) return null;
        try {
            String description = decode(parts[2]);
            Task task = switch (parts[0]) {
                case "T" -> new Todo(description);
                case "D" -> parts.length == 4 ? new Deadline(description, decode(parts[3])) : null;
                case "E" -> parts.length == 5 ? new Event(description, decode(parts[3]), decode(parts[4])) : null;
                default -> null;
            };
            if (task != null && parts[1].equals("1")) task.markAsDone();
            return task;
        } catch (IllegalArgumentException exception) {
            return null;
        }
    }

    private String encode(String value) {
        return Base64.getUrlEncoder().encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }

    private String decode(String value) {
        return new String(Base64.getUrlDecoder().decode(value), StandardCharsets.UTF_8);
    }
}
