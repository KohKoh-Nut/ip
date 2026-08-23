package commands.impl;

import commands.Command;
import session.Session;
import tasks.Task;

/** Removes a task from the current session. */
public class DeleteCommand extends Command {
    /** The user input that invokes this command. */
    public static final String COMMAND = "delete";

    /** Tokens required after the command name. */
    private static final String[] REQUIRED_TOKENS = {};

    /**
     * Creates a command that removes a task.
     *
     * @param input the complete line entered by the user
     * @param session the current session
     */
    public DeleteCommand(String input, Session session) {
        super(input, session);
    }

    /** {@inheritDoc} */
    @Override
    public void execute() {
        Task task = session.getTaskList().remove(taskNumber());
        System.out.println(" Noted. I've removed this task:");
        System.out.println("   " + task);
    }

    /** {@inheritDoc} */
    @Override
    public boolean check() {
        return taskNumber() > 0 && session.getTaskList().get(taskNumber()) != null
                && REQUIRED_TOKENS.length == 0;
    }

    /** {@inheritDoc} */
    @Override
    public String hint() {
        return formatHint(COMMAND + " " + TASK_NUMBER, TASK_NUMBER_REQUIREMENT);
    }

    /**
     * Parses the one-based task number following the command name.
     *
     * @return the task number, or {@code -1} if the input is not a number
     */
    private int taskNumber() {
        try {
            return Integer.parseInt(input.substring(COMMAND.length()).trim());
        } catch (NumberFormatException exception) {
            return -1;
        }
    }
}
