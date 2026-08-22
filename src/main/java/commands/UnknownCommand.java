package commands;

import tasks.TaskList;

/** Handles input that does not match a supported command. */
public class UnknownCommand extends Command {
    /** The input marker for an otherwise unsupported command. */
    public static final String COMMAND = "";
    /** Tokens required after the command name. */
    private static final String[] REQUIRED_TOKENS = {};

    /**
     * Creates a command that displays guidance for unsupported input.
     *
     * @param input the complete line entered by the user
     * @param taskList the task list for the current session
     */
    public UnknownCommand(String input, TaskList taskList) { super(input, taskList); }

    /** {@inheritDoc} */
    @Override public void execute() {
        // This command never executes because its input is always invalid.
    }
    /** {@inheritDoc} */
    @Override public boolean check() { return REQUIRED_TOKENS.length != 0; }
    /** {@inheritDoc} */
    @Override public String hint() { return " Please use todo, deadline, or event to add a task."; }
}
