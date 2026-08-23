package commands.impl;

import java.util.List;

import commands.Command;
import commands.ParsedToken;
import session.Session;
import tasks.Task;

/** Marks a task as done. */
public final class MarkCommand extends Command {
    /** The user input that invokes this command. */
    public static final String COMMAND = "mark";
    /** Parsed one-based task number, or {@code -1} for invalid input. */
    private final int taskNumber;

    /**
     * Creates a command that marks a task as done.
     *
     * @param taskNumber validated one-based task number
     * @param session the current session
     */
    private MarkCommand(int taskNumber, Session session) {
        super(session);
        this.taskNumber = taskNumber;
    }

    /**
     * Builds a mark command from parser-produced values.
     *
     * @param tokens structured values supplied after the command name
     * @param session current application session
     * @return mark command containing the parsed task number
     */
    public static Command build(List<ParsedToken> tokens, Session session) {
        return new MarkCommand(taskNumber(tokens), session);
    }

    /**
     * Checks that the default value identifies an existing task.
     *
     * @param tokens parsed values supplied after the command name
     * @param session current application session
     * @return whether the mark input identifies an existing task
     */
    public static boolean check(List<ParsedToken> tokens, Session session) {
        return hasExistingTaskNumber(tokens, session);
    }

    /**
     * Returns guidance for invalid mark input.
     *
     * @return valid mark syntax and requirements
     */
    public static String hint() {
        return formatHint(COMMAND + " " + TASK_NUMBER, TASK_NUMBER_REQUIREMENT);
    }

    /** {@inheritDoc} */
    @Override public void execute() {
        Task task = session.getTaskList().get(taskNumber);
        task.markAsDone();
        session.save();
        System.out.println(" Nice! I've marked this task as done:");
        System.out.println("   " + task);
    }
}
