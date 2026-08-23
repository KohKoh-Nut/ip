package commands;

import session.Session;

/** Ends the current Zabud session. */
public class ByeCommand extends Command {
    /** The user input that invokes this command. */
    public static final String COMMAND = "bye";

    /** Tokens required after the command name. */
    private static final String[] REQUIRED_TOKENS = {};

    /**
     * Creates a command that ends the current session.
     *
     * @param input the complete line entered by the user
     * @param session the current session
     */
    public ByeCommand(String input, Session session) { super(input, session); }

    /** {@inheritDoc} */
    @Override public void execute() {
        System.out.println("Bye, King Solomon. Hope to see you again soon!");
        System.out.println("____________________________________________________________");
    }
    /** {@inheritDoc} */
    @Override public boolean check() { return REQUIRED_TOKENS.length == 0; }
    /** {@inheritDoc} */
    @Override public String hint() { return ""; }
    /** {@inheritDoc} */
    @Override protected boolean exitsApplication() { return true; }
}
