package commands.impl;

import java.util.List;

import commands.ParsedToken;
import commands.Parser;
import commands.TaskCommand;
import session.Session;
import tasks.Todo;

/** Adds a to-do task. */
public class TodoCommand extends TaskCommand {
    /** The user input that invokes this command. */
    public static final String COMMAND = "todo";
    /** Parsed task description. */
    private final String description;

    /**
     * Creates a command that adds a to-do task.
     *
     * @param tokens structured values supplied after the command name
     * @param session the current session
     */
    private TodoCommand(List<ParsedToken> tokens, Session session) {
        super(tokens, session);
        description = tokens.isEmpty() ? "" : tokens.getFirst().value();
    }

    /**
     * Builds a to-do command from parser-produced values.
     *
     * @param tokens structured values supplied after the command name
     * @param session current application session
     * @return to-do command containing the parsed description
     */
    public static TodoCommand build(List<ParsedToken> tokens, Session session) {
        return new TodoCommand(tokens, session);
    }

    /** {@inheritDoc} */
    @Override public void execute() { addTask(new Todo(description)); }
    /** {@inheritDoc} */
    @Override public boolean check() {
        return hasTokenNames(tokens, Parser.DEFAULT_TOKEN) && !description.isBlank();
    }
    /** {@inheritDoc} */
    @Override public String hint() {
        return formatHint(COMMAND + " " + DESCRIPTION, DESCRIPTION_REQUIREMENT);
    }
}
