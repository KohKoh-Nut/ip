package session;

import java.nio.file.Files;
import java.nio.file.Path;
import tasks.Deadline;
import tasks.Event;
import tasks.Todo;

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
        first.getTaskList().add(new Deadline("return book", "June 6th"));
        first.getTaskList().add(new Event("project meeting", "Aug 6th 2pm", "Aug 6th 4pm"));
        first.getTaskList().get(2).markAsDone();
        first.save();

        Session second = new Session(path);
        assert second.getTaskList().size() == 3;
        assert second.getTaskList().get(1).getDescription().equals("read | book");
        assert second.getTaskList().get(2).isDone();
        assert second.getTaskList().get(3).toString().contains("from: Aug 6th 2pm");

        Session missing = new Session(directory.resolve("missing.txt"));
        assert missing.getTaskList().size() == 0;
    }
}
