package commands.impl;

import java.util.List;

import commands.Command;
import commands.ParsedToken;
import commands.Parser;
import session.Session;

/** Ends the current Zabud session. */
public final class ByeCommand extends Command {
    /** The user input that invokes this command. */
    public static final String COMMAND = "bye";

    /** Creates a command that ends the current session. */
    private ByeCommand(Session session) { super(session); }

    /**
     * Builds a bye command from validated input.
     *
     * @param tokens validated values supplied after the command name
     * @param session current application session
     * @return executable bye command
     */
    public static Command build(List<ParsedToken> tokens, Session session) {
        return new ByeCommand(session);
    }

    /**
     * Checks that no value follows the command name.
     *
     * @param tokens parsed values supplied after the command name
     * @param session current application session
     * @return whether the input contains no arguments
     */
    public static boolean check(List<ParsedToken> tokens, Session session) {
        return hasTokenNames(tokens, Parser.DEFAULT_TOKEN) && tokens.getFirst().value().isBlank();
    }

    /**
     * Returns guidance for invalid bye input.
     *
     * @return valid bye syntax
     */
    public static String hint() { return " Use 'bye'."; }

    /** {@inheritDoc} */
    @Override public void execute() {
        System.out.println("Bye, King Solomon. Hope to see you again soon!");
        System.out.println("____________________________________________________________");
    }
    /** {@inheritDoc} */
    @Override protected boolean exitsApplication() { return true; }
}
