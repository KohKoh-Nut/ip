package commands.impl;

import java.util.List;

import commands.Command;
import commands.ParsedToken;
import commands.Parser;
import session.Session;
import tasks.Task;

/** Marks a task as done. */
public class MarkCommand extends Command {
    /** The user input that invokes this command. */
    public static final String COMMAND = "mark";
    /** Parsed one-based task number, or {@code -1} for invalid input. */
    private final int taskNumber;

    /**
     * Creates a command that marks a task as done.
     *
     * @param tokens structured values supplied after the command name
     * @param session the current session
     */
    private MarkCommand(List<ParsedToken> tokens, Session session) {
        super(tokens, session);
        taskNumber = parseTaskNumber(tokens);
    }

    /**
     * Builds a mark command from parser-produced values.
     *
     * @param tokens structured values supplied after the command name
     * @param session current application session
     * @return mark command containing the parsed task number
     */
    public static MarkCommand build(List<ParsedToken> tokens, Session session) {
        return new MarkCommand(tokens, session);
    }

    /** {@inheritDoc} */
    @Override public void execute() {
        Task task = session.getTaskList().get(taskNumber);
        task.markAsDone();
        session.save();
        System.out.println(" Nice! I've marked this task as done:");
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
