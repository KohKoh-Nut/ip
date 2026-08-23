package commands.impl;

import java.util.List;

import commands.Command;
import commands.ParsedToken;
import session.Session;

/** Handles input that does not match a supported command. */
public class UnknownCommand extends Command {
    /** The input marker for an otherwise unsupported command. */
    public static final String COMMAND = "";
    /**
     * Creates a command that displays guidance for unsupported input.
     *
     * @param tokens structured values supplied after the unrecognized command name
     * @param session the current session
     */
    public UnknownCommand(List<ParsedToken> tokens, Session session) { super(tokens, session); }

    /** {@inheritDoc} */
    @Override public void execute() {
        // This command never executes because its input is always invalid.
    }
    /** {@inheritDoc} */
    @Override public boolean check() { return false; }
    /** {@inheritDoc} */
    @Override public String hint() { return " Unknown command. Use 'help' to see available commands."; }
}
