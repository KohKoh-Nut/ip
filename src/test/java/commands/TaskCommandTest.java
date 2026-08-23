package commands;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;

import commands.impl.DeadlineCommand;
import commands.impl.EventCommand;
import session.Session;
import tasks.Deadline;

/** Regression checks for task command validation and typed task creation. */
public final class TaskCommandTest {
    private TaskCommandTest() { }

    /** Verifies tokenized hints, parsing, validation, and concrete date/time task fields. */
    public static void main(String[] args) throws Exception {
        Path path = Files.createTempDirectory("zabud-command-test").resolve("tasks.txt");
        Session session = new Session(path);
        DeadlineCommand command = (DeadlineCommand) Parser.build(
                "deadline return book /by 2/12/2019 1800", session);
        assert command.hint().equals(" Use 'deadline DESCRIPTION /by DATE_OR_TIME'.\n\n"
                + " - DESCRIPTION: enter a description containing at least one non-space character.\n"
                + " - DATE_OR_TIME: enter a date as DD/MM/YYYY, a 24-hour time as HHMM, "
                + "or both as DD/MM/YYYY HHMM.");
        assert command.check();
        command.execute();
        assert session.getTaskList().get(1) instanceof Deadline;
        Deadline deadline = (Deadline) session.getTaskList().get(1);
        assert deadline.getDate().equals(LocalDate.of(2019, 12, 2));
        assert deadline.getTime().equals(LocalTime.of(18, 0));

        EventCommand event = (EventCommand) Parser.build(
                "event meeting /from 2/12/2019 1800 /to 2/12/2019 1900", session);
        assert event.hint().equals(" Use 'event DESCRIPTION /from DATE_OR_TIME /to DATE_OR_TIME'.\n\n"
                + " - DESCRIPTION: enter a description containing at least one non-space character.\n"
                + " - DATE_OR_TIME: enter a date as DD/MM/YYYY, a 24-hour time as HHMM, "
                + "or both as DD/MM/YYYY HHMM.");
        assert event.check();
        assert !Parser.build("event meeting /to 1900 /from 1800", session).check();
        assert !Parser.build("deadline test /when 1200", session).check();
    }
}
