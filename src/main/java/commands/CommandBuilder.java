package commands;

import java.util.List;

import session.Session;

/** Builds an executable command from parser-validated values. */
@FunctionalInterface
public interface CommandBuilder {
    /**
     * Builds a command from values that have already passed validation.
     *
     * @param tokens parsed values supplied after the command name
     * @param session current application session
     * @return executable command containing the validated input
     */
    Command build(List<ParsedToken> tokens, Session session);
}
