package commands;

import java.nio.file.Files;
import java.util.List;

import commands.impl.ByeCommand;
import commands.impl.DeadlineCommand;
import commands.impl.DeleteCommand;
import commands.impl.EventCommand;
import commands.impl.HelpCommand;
import commands.impl.ListCommand;
import commands.impl.MarkCommand;
import commands.impl.TodoCommand;
import commands.impl.UnknownCommand;
import commands.impl.UnmarkCommand;
import session.Session;

/** Regression checks for parsing raw input into ordered named values. */
public final class ParserTest {
    private ParserTest() {
    }

    /** Verifies default, named, empty, and multiple token values. */
    public static void main(String[] args) throws Exception {
        assert Parser.parse("deadline test /by 1200").equals(List.of(
                new ParsedToken("command", "deadline"),
                new ParsedToken("default", "test"),
                new ParsedToken("by", "1200")));

        assert Parser.parse("event meeting /from 2/12/2019 1800 /to 2/12/2019 1900").equals(List.of(
                new ParsedToken("command", "event"),
                new ParsedToken("default", "meeting"),
                new ParsedToken("from", "2/12/2019 1800"),
                new ParsedToken("to", "2/12/2019 1900")));

        assert Parser.parse("list").equals(List.of(
                new ParsedToken("command", "list"),
                new ParsedToken("default", "")));
        assert Parser.parse("deadline test /by").getLast().equals(new ParsedToken("by", ""));

        Session session = new Session(Files.createTempDirectory("zabud-parser").resolve("session.txt"));
        List<ParsedToken> deadline = Parser.parse("deadline test /by 1200");
        assert Parser.check(deadline, session);
        assert Parser.build(deadline, session) != null;
        List<ParsedToken> unknown = Parser.parse("unsupported value");
        assert !Parser.check(unknown, session);
        assert Parser.hint(unknown).contains("Unknown command");
        List<Object> handlers = List.of(
                new ByeCommand(), new DeadlineCommand(), new DeleteCommand(),
                new EventCommand(), new HelpCommand(), new ListCommand(),
                new MarkCommand(), new TodoCommand(), new UnknownCommand(),
                new UnmarkCommand());
        assert handlers.stream().allMatch(handler -> handler instanceof Validatable);
        assert handlers.stream().allMatch(handler -> handler instanceof Buildable);
    }
}
