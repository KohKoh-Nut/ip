package commands.impl;

import commands.Command;
import session.Session;

/** Displays the commands available in Zabud. */
public class HelpCommand extends Command {
    /** The user input that invokes this command. */
    public static final String COMMAND = "help";

    /** Tokens required after the command name. */
    private static final String[] REQUIRED_TOKENS = {};

    /**
     * Creates a command that displays available command syntax.
     *
     * @param input the complete line entered by the user
     * @param session the current session
     */
    public HelpCommand(String input, Session session) {
        super(input, session);
    }

    /** {@inheritDoc} */
    @Override
    public void execute() {
        System.out.println(" Available commands:");
        System.out.println("   todo DESCRIPTION");
        System.out.println("   deadline DESCRIPTION /by DD/MM/YYYY, HHMM, or DD/MM/YYYY HHMM");
        System.out.println("   event DESCRIPTION /from DATE_OR_TIME /to DATE_OR_TIME");
        System.out.println("   list");
        System.out.println("   mark TASK_NUMBER");
        System.out.println("   unmark TASK_NUMBER");
        System.out.println("   delete TASK_NUMBER");
        System.out.println("   help");
        System.out.println("   bye");
    }

    /** {@inheritDoc} */
    @Override
    public boolean check() {
        return REQUIRED_TOKENS.length == 0;
    }

    /** {@inheritDoc} */
    @Override
    public String hint() {
        return "";
    }
}
