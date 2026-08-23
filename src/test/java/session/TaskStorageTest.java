package session;

import java.nio.file.Files;
import java.nio.file.Path;
import tasks.Deadline;
import tasks.Event;
import tasks.Todo;
import java.time.LocalDate;
import java.time.LocalDateTime;

/** Lightweight regression checks for task persistence. Run with assertions enabled. */
public final class TaskStorageTest {
    private TaskStorageTest() {
    }

    /** Verifies round-trip persistence, status preservation, and missing-file handling. */
    public static void main(String[] args) throws Exception {
        Path directory = Files.createTempDirectory("zabud-storage-test");
        Path path = directory.resolve("tasks.txt");
        Session first = new Session(path);
        first.getTaskList().add(new Todo("read | book"));
        first.getTaskList().add(new Deadline("return book", LocalDate.of(2019, 12, 2), null));
        first.getTaskList().add(new Event("project meeting", LocalDate.of(2025, 8, 6), java.time.LocalTime.of(14, 0),
                LocalDate.of(2025, 8, 6), java.time.LocalTime.of(16, 0)));
        first.getTaskList().get(2).markAsDone();
        first.save();

        Session second = new Session(path);
        assert second.getTaskList().size() == 3;
        assert second.getTaskList().get(1).getDescription().equals("read | book");
        assert second.getTaskList().get(2).isDone();
        assert second.getTaskList().get(3).toString().contains("from: 2025-08-06 14:00");

        Session missing = new Session(directory.resolve("missing.txt"));
        assert missing.getTaskList().size() == 0;
    }
}
