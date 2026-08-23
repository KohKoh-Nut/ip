package commands.impl;

import java.util.List;

import commands.Command;
import commands.ParsedToken;
import commands.Parser;
import session.Session;

/** Displays all tasks in the current session. */
public class ListCommand extends Command {
    /** The user input that invokes this command. */
    public static final String COMMAND = "list";
    /** Creates a command that displays all tasks. */
    private ListCommand(Session session) { super(session); }

    /** Builds a list command from validated input. */
    public static Command build(List<ParsedToken> tokens, Session session) {
        return new ListCommand(session);
    }

    /** Checks that no value follows the command name. */
    public static boolean check(List<ParsedToken> tokens, Session session) {
        return hasTokenNames(tokens, Parser.DEFAULT_TOKEN) && tokens.getFirst().value().isBlank();
    }

    /** Returns guidance for invalid list input. */
    public static String hint() { return " Use 'list'."; }

    /** {@inheritDoc} */
    @Override public void execute() { session.getTaskList().printTasks(); }
}
