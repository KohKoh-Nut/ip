package commands.impl;

import java.util.List;

import commands.Buildable;
import commands.Command;
import commands.ParsedToken;
import commands.Parser;
import commands.Validatable;
import session.Session;

/**
 * Validates and builds commands that display all tasks.
 */
public final class ListCommand implements Validatable, Buildable {
    /**
     * The user input that selects this command type.
     */
    public static final String COMMAND = "list";

    /**
     * Creates a list command handler.
     */
    public ListCommand() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean check(List<ParsedToken> tokens, Session session) {
        return Validatable.hasTokenNames(tokens, Parser.DEFAULT_TOKEN)
                && tokens.getFirst().value().isBlank();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String hint() {
        return " Use 'list'.";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Command build(List<ParsedToken> tokens, Session session) {
        return new Command(session) {
            @Override
            public void execute() {
                session.getTaskList().printTasks();
            }
        };
    }
}
