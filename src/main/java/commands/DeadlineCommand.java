package commands;

import tasks.Deadline;
import tasks.TaskList;

/** Adds a task with a deadline. */
public class DeadlineCommand extends TaskCommand {
    /** The user input that invokes this command. */
    public static final String COMMAND = "deadline";
    /** Tokens required after the command name. */
    private static final String[] REQUIRED_TOKENS = {"/by"};

    /** Creates a command that adds a deadline task. */
    public DeadlineCommand(String input, TaskList taskList) { super(input, taskList); }

    /** {@inheritDoc} */
    @Override public void execute() {
        String[] parts = details();
        addTask(new Deadline(parts[0].trim(), parts[1].trim()));
    }
    /** {@inheritDoc} */
    @Override public boolean check() {
        String[] parts = details();
        return parts.length == 2 && !parts[0].isBlank() && !parts[1].isBlank();
    }
    /** {@inheritDoc} */
    @Override public String hint() {
        return " Use '" + COMMAND + " DESCRIPTION " + REQUIRED_TOKENS[0] + " WHEN'.";
    }

    /** Splits the description and deadline details around the required token. */
    private String[] details() {
        return input.substring(COMMAND.length()).trim().split(" " + REQUIRED_TOKENS[0] + " ", 2);
    }
}
