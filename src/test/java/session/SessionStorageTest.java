package session;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;

import tasks.Deadline;
import tasks.Event;
import tasks.Todo;

/** Lightweight regression checks for session persistence. Run with assertions enabled. */
public final class SessionStorageTest {
    private SessionStorageTest() {
    }

    /** Verifies task/history round trips, status preservation, and missing-file handling. */
    public static void main(String[] args) throws Exception {
        Path directory = Files.createTempDirectory("zabud-storage-test");
        Path path = directory.resolve("tasks.txt");
        Session first = new Session(path);
        first.getTaskList().add(new Todo("read | book"));
        first.getTaskList().add(new Deadline("return book", LocalDate.of(2019, 12, 2), null));
        first.getTaskList().add(new Event("project meeting", LocalDate.of(2025, 8, 6), LocalTime.of(14, 0),
                LocalDate.of(2025, 8, 6), LocalTime.of(16, 0)));
        first.getTaskList().get(2).markAsDone();
        first.recordCommand("todo read book");
        first.recordCommand("list");
        first.save();

        Session second = new Session(path);
        assert second.getTaskList().size() == 3;
        assert second.getTaskList().get(1).getDescription().equals("read | book");
        assert second.getTaskList().get(2).isDone();
        assert second.getTaskList().get(3).toString().contains("from: 2025-08-06 14:00");
        assert second.previousCommand().equals("list");
        assert second.previousCommand().equals("todo read book");

        Session missing = new Session(directory.resolve("missing.txt"));
        assert missing.getTaskList().size() == 0;
    }
}
