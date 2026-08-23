package commands.impl;

import java.util.List;

import commands.Command;
import commands.ParsedToken;
import session.Session;

/** Handles input that does not match a supported command. */
public final class UnknownCommand extends Command {
    /** Creates a command that displays guidance for unsupported input. */
    private UnknownCommand(Session session) { super(session); }

    /**
     * Builds the fallback command for an unsupported command name.
     *
     * @param tokens values supplied after the unsupported command name
     * @param session current application session
     * @return executable unknown-command fallback
     */
    public static Command build(List<ParsedToken> tokens, Session session) {
        return new UnknownCommand(session);
    }

    /**
     * Returns guidance for unsupported input.
     *
     * @return unknown-command guidance
     */
    public static String hint() { return " Unknown command. Use 'help' to see available commands."; }

    /** {@inheritDoc} */
    @Override public void execute() {
        System.out.println(hint());
    }
}
