package commands;

import commands.impl.DeadlineCommand;
import commands.impl.EventCommand;
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
        assert command.hint().equals(" Use 'deadline DESCRIPTION /by xxxxxx'.");
        assert command.check();
        command.execute();
        assert session.getTaskList().get(1) instanceof Deadline;
        Deadline deadline = (Deadline) session.getTaskList().get(1);
        assert deadline.getDate().equals(java.time.LocalDate.of(2019, 12, 2));
        assert deadline.getTime().equals(java.time.LocalTime.of(18, 0));

        EventCommand event = new EventCommand(
                "event meeting /from 2/12/2019 1800 /to 2/12/2019 1900", session);
        assert event.hint().equals(" Use 'event DESCRIPTION /from xxxxxx /to xxxxxx'.");
        assert event.splitInput().description().equals("meeting");
        assert event.splitInput().tokens().size() == 2;
        assert event.check();
    }
}
