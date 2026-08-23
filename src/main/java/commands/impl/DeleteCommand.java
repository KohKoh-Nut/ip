package commands.impl;

import java.util.List;

import commands.Command;
import commands.ParsedToken;
import commands.Parser;
import session.Session;
import tasks.Task;

/** Removes a task from the current session. */
public class DeleteCommand extends Command {
    /** The user input that invokes this command. */
    public static final String COMMAND = "delete";

    /** Parsed one-based task number, or {@code -1} for invalid input. */
    private final int taskNumber;

    /**
     * Creates a command that removes a task.
     *
     * @param tokens structured values supplied after the command name
     * @param session the current session
     */
    private DeleteCommand(List<ParsedToken> tokens, Session session) {
        super(tokens, session);
        taskNumber = parseTaskNumber(tokens);
    }

    /**
     * Builds a delete command from parser-produced values.
     *
     * @param tokens structured values supplied after the command name
     * @param session current application session
     * @return delete command containing the parsed task number
     */
    public static DeleteCommand build(List<ParsedToken> tokens, Session session) {
        return new DeleteCommand(tokens, session);
    }

    /** {@inheritDoc} */
    @Override
    public void execute() {
        Task task = session.getTaskList().remove(taskNumber);
        System.out.println(" Noted. I've removed this task:");
        System.out.println("   " + task);
    }

    /** {@inheritDoc} */
    @Override
    public boolean check() {
        return hasTokenNames(tokens, Parser.DEFAULT_TOKEN)
                && taskNumber > 0 && session.getTaskList().get(taskNumber) != null;
    }

    /** {@inheritDoc} */
    @Override
    public String hint() {
        return formatHint(COMMAND + " " + TASK_NUMBER, TASK_NUMBER_REQUIREMENT);
    }

    /**
     * Parses a one-based task number from the default value.
     *
     * @param tokens parser-produced values
     * @return the task number, or {@code -1} if the input is not a number
     */
    private static int parseTaskNumber(List<ParsedToken> tokens) {
        try {
            return tokens.isEmpty() ? -1 : Integer.parseInt(tokens.getFirst().value());
        } catch (NumberFormatException exception) {
            return -1;
        }
    }
}
