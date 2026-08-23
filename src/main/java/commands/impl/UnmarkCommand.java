package commands.impl;

import java.util.List;

import commands.Command;
import commands.ParsedToken;
import session.Session;
import tasks.Task;

/** Marks a task as not done. */
public final class UnmarkCommand extends Command {
    /** The user input that invokes this command. */
    public static final String COMMAND = "unmark";
    /** Parsed one-based task number, or {@code -1} for invalid input. */
    private final int taskNumber;

    /**
     * Creates a command that marks a task as not done.
     *
     * @param taskNumber validated one-based task number
     * @param session the current session
     */
    private UnmarkCommand(int taskNumber, Session session) {
        super(session);
        this.taskNumber = taskNumber;
    }

    /**
     * Builds an unmark command from parser-produced values.
     *
     * @param tokens structured values supplied after the command name
     * @param session current application session
     * @return unmark command containing the parsed task number
     */
    public static Command build(List<ParsedToken> tokens, Session session) {
        return new UnmarkCommand(taskNumber(tokens), session);
    }

    /**
     * Checks that the default value identifies an existing task.
     *
     * @param tokens parsed values supplied after the command name
     * @param session current application session
     * @return whether the unmark input identifies an existing task
     */
    public static boolean check(List<ParsedToken> tokens, Session session) {
        return hasExistingTaskNumber(tokens, session);
    }

    /**
     * Returns guidance for invalid unmark input.
     *
     * @return valid unmark syntax and requirements
     */
    public static String hint() {
        return formatHint(COMMAND + " " + TASK_NUMBER, TASK_NUMBER_REQUIREMENT);
    }

    /** {@inheritDoc} */
    @Override public void execute() {
        Task task = session.getTaskList().get(taskNumber);
        task.markAsNotDone();
        session.save();
        System.out.println(" OK, I've marked this task as not done yet:");
        System.out.println("   " + task);
    }
}
