package commands;

import session.Session;
import tasks.Task;

/** Marks a task as not done. */
public class UnmarkCommand extends Command {
    /** The user input that invokes this command. */
    public static final String COMMAND = "unmark";
    /** Tokens required after the command name. */
    private static final String[] REQUIRED_TOKENS = {};

    /**
     * Creates a command that marks a task as not done.
     *
     * @param input the complete line entered by the user
     * @param session the current session
     */
    public UnmarkCommand(String input, Session session) { super(input, session); }

    /** {@inheritDoc} */
    @Override public void execute() {
        Task task = session.getTaskList().get(taskNumber());
        task.markAsNotDone();
        System.out.println(" OK, I've marked this task as not done yet:");
        System.out.println("   " + task);
    }
    /** {@inheritDoc} */
    @Override public boolean check() {
        return taskNumber() > 0 && session.getTaskList().get(taskNumber()) != null
                && REQUIRED_TOKENS.length == 0;
    }
    /** {@inheritDoc} */
    @Override public String hint() { return " Please specify a valid task number."; }

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
