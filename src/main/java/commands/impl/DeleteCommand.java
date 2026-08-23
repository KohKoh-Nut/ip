package commands.impl;

import java.util.List;

import commands.Command;
import commands.ParsedToken;
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
     * @param taskNumber validated one-based task number
     * @param session the current session
     */
    private DeleteCommand(int taskNumber, Session session) {
        super(session);
        this.taskNumber = taskNumber;
    }

    /**
     * Builds a delete command from parser-produced values.
     *
     * @param tokens structured values supplied after the command name
     * @param session current application session
     * @return delete command containing the parsed task number
     */
    public static Command build(List<ParsedToken> tokens, Session session) {
        return new DeleteCommand(taskNumber(tokens), session);
    }

    /** Checks that the default value identifies an existing task. */
    public static boolean check(List<ParsedToken> tokens, Session session) {
        return hasExistingTaskNumber(tokens, session);
    }

    /** Returns guidance for invalid delete input. */
    public static String hint() {
        return formatHint(COMMAND + " " + TASK_NUMBER, TASK_NUMBER_REQUIREMENT);
    }

    /** {@inheritDoc} */
    @Override
    public void execute() {
        Task task = session.getTaskList().remove(taskNumber);
        System.out.println(" Noted. I've removed this task:");
        System.out.println("   " + task);
    }

}
