package commands.impl;

import commands.Command;
import session.Session;

/** Displays all tasks in the current session. */
public class ListCommand extends Command {
    /** The user input that invokes this command. */
    public static final String COMMAND = "list";
    /** Tokens required after the command name. */
    private static final String[] REQUIRED_TOKENS = {};

    /**
     * Creates a command that displays all tasks.
     *
     * @param input the complete line entered by the user
     * @param session the current session
     */
    public ListCommand(String input, Session session) { super(input, session); }

    /** {@inheritDoc} */
    @Override public void execute() { session.getTaskList().printTasks(); }
    /** {@inheritDoc} */
    @Override public boolean check() { return REQUIRED_TOKENS.length == 0; }
    /** {@inheritDoc} */
    @Override public String hint() { return ""; }
}
