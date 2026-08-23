package commands;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

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
        List<ParsedToken> deadlineInput = Parser.parse(
                "deadline return book /by 2/12/2019 1800");
        assert Parser.hint(deadlineInput).equals(" Use 'deadline DESCRIPTION /by DATE_OR_TIME'.\n\n"
                + " - DESCRIPTION: enter a description containing at least one non-space character.\n"
                + " - DATE_OR_TIME: enter a date as DD/MM/YYYY, a 24-hour time as HHMM, "
                + "or both as DD/MM/YYYY HHMM.");
        assert Parser.check(deadlineInput, session);
        DeadlineCommand command = (DeadlineCommand) Parser.build(deadlineInput, session);
        command.execute();
        assert session.getTaskList().get(1) instanceof Deadline;
        Deadline deadline = (Deadline) session.getTaskList().get(1);
        assert deadline.getDate().equals(LocalDate.of(2019, 12, 2));
        assert deadline.getTime().equals(LocalTime.of(18, 0));

        List<ParsedToken> eventInput = Parser.parse(
                "event meeting /from 2/12/2019 1800 /to 2/12/2019 1900");
        assert Parser.hint(eventInput).equals(" Use 'event DESCRIPTION /from DATE_OR_TIME /to DATE_OR_TIME'.\n\n"
                + " - DESCRIPTION: enter a description containing at least one non-space character.\n"
                + " - DATE_OR_TIME: enter a date as DD/MM/YYYY, a 24-hour time as HHMM, "
                + "or both as DD/MM/YYYY HHMM.");
        assert Parser.check(eventInput, session);
        assert Parser.build(eventInput, session) instanceof EventCommand;
        assert !Parser.check(Parser.parse("event meeting /to 1900 /from 1800"), session);
        assert !Parser.check(Parser.parse("deadline test /when 1200"), session);
    }
}
