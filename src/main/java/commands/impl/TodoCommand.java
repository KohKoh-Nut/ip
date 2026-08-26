package commands.impl;

import java.util.List;

import commands.Buildable;
import commands.Command;
import commands.ParsedToken;
import commands.Parser;
import commands.TaskCommand;
import commands.Validatable;
import session.Session;
import tasks.Todo;

/**
 * Validates and builds commands that add to-do tasks.
 */
public final class TodoCommand implements Validatable, Buildable {
    /**
     * The user input that selects this command type.
     */
    public static final String COMMAND = "todo";

    /**
     * Creates a to-do command handler.
     */
    public TodoCommand() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean check(List<ParsedToken> tokens, Session session) {
        return Validatable.hasTokenNames(tokens, Parser.DEFAULT_TOKEN)
                && !tokens.getFirst().value().isBlank();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String hint() {
        return Validatable.formatHint(COMMAND + " " + DESCRIPTION, DESCRIPTION_REQUIREMENT);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Command build(List<ParsedToken> tokens, Session session) {
        String description = tokens.getFirst().value();
        return new TaskCommand(session) {
            @Override
            public void execute() {
                addTask(new Todo(description));
            }
        };
    }
}
