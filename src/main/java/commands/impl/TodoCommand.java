package commands.impl;

import java.util.List;

import commands.Command;
import commands.ParsedToken;
import commands.Parser;
import commands.TaskCommand;
import session.Session;
import tasks.Todo;

/** Adds a to-do task. */
public final class TodoCommand extends TaskCommand {
    /** The user input that invokes this command. */
    public static final String COMMAND = "todo";
    /** Parsed task description. */
    private final String description;

    /**
     * Creates a command that adds a to-do task.
     *
     * @param description validated task description
     * @param session the current session
     */
    private TodoCommand(String description, Session session) {
        super(session);
        this.description = description;
    }

    /**
     * Builds a to-do command from parser-produced values.
     *
     * @param tokens structured values supplied after the command name
     * @param session current application session
     * @return to-do command containing the parsed description
     */
    public static Command build(List<ParsedToken> tokens, Session session) {
        return new TodoCommand(tokens.getFirst().value(), session);
    }

    /**
     * Checks that the input contains only a nonblank default description.
     *
     * @param tokens parsed values supplied after the command name
     * @param session current application session
     * @return whether the to-do input is valid
     */
    public static boolean check(List<ParsedToken> tokens, Session session) {
        return hasTokenNames(tokens, Parser.DEFAULT_TOKEN) && !tokens.getFirst().value().isBlank();
    }

    /**
     * Returns guidance for invalid to-do input.
     *
     * @return valid to-do syntax and requirements
     */
    public static String hint() {
        return formatHint(COMMAND + " " + DESCRIPTION, DESCRIPTION_REQUIREMENT);
    }

    /** {@inheritDoc} */
    @Override public void execute() { addTask(new Todo(description)); }
}
