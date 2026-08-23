package commands;

import commands.task.DeadlineCommand;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.nio.file.Path;
import session.Session;
import tasks.Deadline;

/** Regression checks for task command validation and typed task creation. */
public final class TaskCommandTest {
    private TaskCommandTest() { }

    /** Verifies that a deadline command creates a LocalDateTime-backed task. */
    public static void main(String[] args) throws Exception {
        Path path = Files.createTempDirectory("zabud-command-test").resolve("tasks.txt");
        Session session = new Session(path);
        DeadlineCommand command = new DeadlineCommand("deadline return book /by 2/12/2019 1800", session);
        assert command.check();
        command.execute();
        assert session.getTaskList().get(1) instanceof Deadline;
        Deadline deadline = (Deadline) session.getTaskList().get(1);
        assert deadline.getDate().equals(java.time.LocalDate.of(2019, 12, 2));
        assert deadline.getTime().equals(java.time.LocalTime.of(18, 0));
    }
}
