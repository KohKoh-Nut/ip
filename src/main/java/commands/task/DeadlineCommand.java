package commands.task;

import commands.TaskCommand;
import commands.tokens.DateTimeToken;
import tasks.Deadline;
import session.Session;

/** Adds a task with a deadline. */
public class DeadlineCommand extends TaskCommand {
    /** The user input that invokes this command. */
    public static final String COMMAND = "deadline";
    /** Tokens required after the command name. */
    private static final String[] REQUIRED_TOKENS = {"/by"};

    /**
     * Creates a command that adds a deadline task.
     *
     * @param input the complete line entered by the user
     * @param session the current session
     */
    public DeadlineCommand(String input, Session session) { super(input, session); }

    /** {@inheritDoc} */
    @Override public void execute() {
        String[] parts = details();
        DateTimeToken token = token(parts[1]);
        addTask(new Deadline(parts[0].trim(), token.date(), token.time()));
    }
    /** {@inheritDoc} */
    @Override public boolean check() {
        String[] parts = details();
        return parts.length == 2 && !parts[0].isBlank() && token(parts[1]).check();
    }
    /** {@inheritDoc} */
    @Override public String hint() {
        return " Use '" + COMMAND + " DESCRIPTION " + token("").hint() + "'.";
    }

    /**
     * Splits the description and deadline details around the required token.
     *
     * @return the description and deadline details
     */
    private String[] details() {
        return input.substring(COMMAND.length()).trim().split(" " + REQUIRED_TOKENS[0] + " ", 2);
    }

    private DateTimeToken token(String value) { return new DateTimeToken(REQUIRED_TOKENS[0], value); }
}
