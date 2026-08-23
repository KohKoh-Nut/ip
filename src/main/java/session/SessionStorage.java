package session;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import tasks.Deadline;
import tasks.Event;
import tasks.Task;
import tasks.Todo;

/** Reads and writes all persistent session data using a small, escaped text format. */
public final class SessionStorage {
    /** Record type used for command-history entries. */
    private static final String COMMAND_RECORD = "C";

    /**
     * Loads session data, treating a missing or unreadable file as an empty session.
     *
     * @param path file to read
     * @return tasks and command history decoded from the file
     */
    public SessionData load(Path path) {
        try {
            List<Task> tasks = new ArrayList<>();
            List<String> commandHistory = new ArrayList<>();
            for (String line : Files.readAllLines(path)) {
                if (line.startsWith(COMMAND_RECORD + "|")) {
                    String command = parseCommand(line);
                    if (command != null) commandHistory.add(command);
                } else {
                    Task task = parseTask(line);
                    if (task != null) tasks.add(task);
                }
            }
            return new SessionData(tasks, commandHistory);
        } catch (IOException exception) {
            return new SessionData(List.of(), List.of());
        }
    }

    /**
     * Saves all tasks and command history, creating the parent directory when necessary.
     *
     * @param path file to write
     * @param taskList tasks to encode
     * @param commandHistory commands to encode in chronological order
     * @throws IOException if the file cannot be written
     */
    public void save(Path path, TaskList taskList, List<String> commandHistory) throws IOException {
        if (path.getParent() != null) Files.createDirectories(path.getParent());
        List<String> lines = new ArrayList<>();
        for (int i = 1; i <= taskList.size(); i++) lines.add(formatTask(taskList.get(i)));
        for (String command : commandHistory) lines.add(COMMAND_RECORD + "|" + encode(command));
        Files.write(path, lines, StandardCharsets.UTF_8);
    }

    private String formatTask(Task task) {
        StringBuilder line = new StringBuilder(task.storageType()).append('|').append(task.isDone() ? '1' : '0');
        line.append('|').append(encode(task.getDescription()));
        for (String detail : task.getStorageDetails()) line.append('|').append(encode(detail));
        return line.toString();
    }

    private Task parseTask(String line) {
        String[] parts = line.split("\\|", -1);
        if (parts.length < 3) return null;
        try {
            String description = decode(parts[2]);
            Task task = switch (parts[0]) {
                case "T" -> new Todo(description);
                case "D" -> parts.length == 5 ? new Deadline(description, date(decode(parts[3])), time(decode(parts[4]))) : null;
                case "E" -> parts.length == 7 ? new Event(description, date(decode(parts[3])), time(decode(parts[4])),
                        date(decode(parts[5])), time(decode(parts[6]))) : null;
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

    private String parseCommand(String line) {
        String[] parts = line.split("\\|", -1);
        try {
            return parts.length == 2 ? decode(parts[1]) : null;
        } catch (IllegalArgumentException exception) {
            return null;
        }
    }

    private LocalDate date(String value) { return value.isBlank() ? null : LocalDate.parse(value); }
    private LocalTime time(String value) { return value.isBlank() ? null : LocalTime.parse(value); }

    /**
     * Immutable session data returned by {@link #load(Path)}.
     *
     * @param tasks tasks restored for the session
     * @param commandHistory commands restored in chronological order
     */
    public record SessionData(List<Task> tasks, List<String> commandHistory) {
        /** Copies loaded collections to prevent callers from modifying the stored result. */
        public SessionData {
            tasks = List.copyOf(tasks);
            commandHistory = List.copyOf(commandHistory);
        }
    }
}
