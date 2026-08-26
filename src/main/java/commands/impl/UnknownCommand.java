package commands.impl;

import java.util.List;

import commands.Buildable;
import commands.Command;
import commands.ParsedToken;
import commands.Validatable;
import session.Session;

/**
 * Validates and builds the fallback for unsupported command names.
 */
public final class UnknownCommand implements Validatable, Buildable {
    /**
     * Creates an unknown-command handler.
     */
    public UnknownCommand() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean check(List<ParsedToken> tokens, Session session) {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String hint() {
        return " Unknown command. Use 'help' to see available commands.";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Command build(List<ParsedToken> tokens, Session session) {
        throw new IllegalStateException("Unsupported commands cannot be built");
    }
}
