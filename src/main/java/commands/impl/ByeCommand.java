package commands.impl;

import java.util.List;

import commands.Command;
import commands.ParsedToken;
import commands.Parser;
import session.Session;

/** Ends the current Zabud session. */
public class ByeCommand extends Command {
    /** The user input that invokes this command. */
    public static final String COMMAND = "bye";

    /**
     * Creates a command that ends the current session.
     *
     * @param tokens structured values supplied after the command name
     * @param session the current session
     */
    public ByeCommand(List<ParsedToken> tokens, Session session) { super(tokens, session); }

    /** {@inheritDoc} */
    @Override public void execute() {
        System.out.println("Bye, King Solomon. Hope to see you again soon!");
        System.out.println("____________________________________________________________");
    }
    /** {@inheritDoc} */
    @Override public boolean check() {
        return hasTokenNames(tokens, Parser.DEFAULT_TOKEN) && tokens.getFirst().value().isBlank();
    }
    /** {@inheritDoc} */
    @Override public String hint() { return ""; }
    /** {@inheritDoc} */
    @Override protected boolean exitsApplication() { return true; }
}
