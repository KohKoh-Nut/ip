package commands;

import java.util.List;

import session.Session;

/** Defines an executable action built from validated command input. */
public abstract class Command {
    /** Placeholder used for a required task description. */
    protected static final String DESCRIPTION = "DESCRIPTION";
    /** Placeholder used for a required one-based task number. */
    protected static final String TASK_NUMBER = "TASK_NUMBER";
    /** Requirement shown for task descriptions. */
    protected static final String DESCRIPTION_REQUIREMENT =
            " - DESCRIPTION: enter a description containing at least one non-space character.";
    /** Requirement shown for task numbers. */
    protected static final String TASK_NUMBER_REQUIREMENT =
            " - TASK_NUMBER: enter the number of an existing task.";
    /** The session this command may read or update. */
    protected final Session session;

    /**
     * Creates a command with the session it will operate on.
     *
     * @param session the current session
     */
    protected Command(Session session) {
        this.session = session;
    }

    /**
     * Formats a complete invalid-input hint from syntax and requirement lines.
     *
     * @param syntax command syntax containing named placeholders
     * @param requirements requirement lines for those placeholders
     * @return formatted command hint
     */
    protected static String formatHint(String syntax, String... requirements) {
        return " Use '" + syntax + "'.\n\n" + String.join("\n", requirements);
    }

    /**
     * Checks that parsed values have exactly the expected names and order.
     *
     * @param tokens parsed values to inspect
     * @param names expected names in command syntax order
     * @return whether the parsed values match the command's syntax
     */
    protected static boolean hasTokenNames(List<ParsedToken> tokens, String... names) {
        if (tokens.size() != names.length) return false;
        for (int index = 0; index < names.length; index++) {
            if (!tokens.get(index).name().equals(names[index])) return false;
        }
        return true;
    }

    /**
     * Parses the default value as a one-based task number.
     *
     * @param tokens parsed values supplied after the command name
     * @return parsed task number, or {@code -1} when it is not an integer
     */
    protected static int taskNumber(List<ParsedToken> tokens) {
        try {
            return tokens.isEmpty() ? -1 : Integer.parseInt(tokens.getFirst().value());
        } catch (NumberFormatException exception) {
            return -1;
        }
    }

    /**
     * Checks input containing one existing task number.
     *
     * @param tokens parsed values supplied after the command name
     * @param session current application session
     * @return whether the input identifies an existing task
     */
    protected static boolean hasExistingTaskNumber(List<ParsedToken> tokens, Session session) {
        int taskNumber = taskNumber(tokens);
        return hasTokenNames(tokens, Parser.DEFAULT_TOKEN)
                && taskNumber > 0 && session.getTaskList().get(taskNumber) != null;
    }

    /** Executes this command's effect. */
    public abstract void execute();

    /**
     * Returns whether this command ends the application session.
     *
     * @return whether Zabud should stop processing input after this command
     */
    public boolean exitsApplication() {
        return false;
    }
}
