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
    /**
     * Creates a command that displays all tasks.
     *
     * @param tokens structured values supplied after the command name
     * @param session the current session
     */
    public ListCommand(List<ParsedToken> tokens, Session session) { super(tokens, session); }

    /** {@inheritDoc} */
    @Override public void execute() { session.getTaskList().printTasks(); }
    /** {@inheritDoc} */
    @Override public boolean check() {
        return hasTokenNames(tokens, Parser.DEFAULT_TOKEN) && tokens.getFirst().value().isBlank();
    }
    /** {@inheritDoc} */
    @Override public String hint() { return ""; }
}
