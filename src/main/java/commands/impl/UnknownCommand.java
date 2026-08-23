package commands.impl;

import java.util.List;

import commands.Command;
import commands.ParsedToken;
import session.Session;

/** Handles input that does not match a supported command. */
public class UnknownCommand extends Command {
    /** The input marker for an otherwise unsupported command. */
    public static final String COMMAND = "";
    /** Creates a command that displays guidance for unsupported input. */
    private UnknownCommand(Session session) { super(session); }

    /** Builds the fallback command for an unsupported command name. */
    public static Command build(List<ParsedToken> tokens, Session session) {
        return new UnknownCommand(session);
    }

    /** Returns guidance for unsupported input. */
    public static String hint() { return " Unknown command. Use 'help' to see available commands."; }

    /** {@inheritDoc} */
    @Override public void execute() {
        System.out.println(hint());
    }
}
