package commands.impl;

import java.util.List;

import commands.Command;
import commands.ParsedToken;
import commands.Parser;
import session.Session;
import tasks.Task;

/** Marks a task as not done. */
public class UnmarkCommand extends Command {
    /** The user input that invokes this command. */
    public static final String COMMAND = "unmark";
    /** Parsed one-based task number, or {@code -1} for invalid input. */
    private final int taskNumber;

    /**
     * Creates a command that marks a task as not done.
     *
     * @param tokens structured values supplied after the command name
     * @param session the current session
     */
    private UnmarkCommand(List<ParsedToken> tokens, Session session) {
        super(tokens, session);
        taskNumber = parseTaskNumber(tokens);
    }

    /**
     * Builds an unmark command from parser-produced values.
     *
     * @param tokens structured values supplied after the command name
     * @param session current application session
     * @return unmark command containing the parsed task number
     */
    public static UnmarkCommand build(List<ParsedToken> tokens, Session session) {
        return new UnmarkCommand(tokens, session);
    }

    /** {@inheritDoc} */
    @Override public void execute() {
        Task task = session.getTaskList().get(taskNumber);
        task.markAsNotDone();
        session.save();
        System.out.println(" OK, I've marked this task as not done yet:");
        System.out.println("   " + task);
    }
    /** {@inheritDoc} */
    @Override public boolean check() {
        return hasTokenNames(tokens, Parser.DEFAULT_TOKEN)
                && taskNumber > 0 && session.getTaskList().get(taskNumber) != null;
    }
    /** {@inheritDoc} */
    @Override public String hint() {
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
