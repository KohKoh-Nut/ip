package commands;

import java.util.List;

import session.Session;

/** Defines a command that can validate and act on one line of user input. */
public abstract class Command implements Validatable {
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
    /** Structured values supplied after the command name. */
    protected final List<ParsedToken> tokens;

    /**
     * Creates a command with the parsed values and session it will operate on.
     *
     * @param tokens structured values supplied after the command name
     * @param session the current session
     */
    protected Command(List<ParsedToken> tokens, Session session) {
        this.tokens = List.copyOf(tokens);
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

    /** Executes this command's effect. */
    public abstract void execute();

    /**
     * Checks whether this command has valid input.
     *
     * @return whether the command can be executed
     */
    public abstract boolean check();

    /**
     * Returns guidance shown when {@link #check()} fails.
     *
     * @return a hint explaining how to correct the input
     */
    public abstract String hint();

    /**
     * Returns whether this command ends the application session.
     *
     * @return whether Zabud should stop processing input after this command
     */
    protected boolean exitsApplication() {
        return false;
    }

    /**
     * Creates, validates, and invokes the command matching the given input.
     *
     * @param input the complete line entered by the user
     * @param session the current session
     * @return whether the application should continue running
     */
    public static boolean invoke(String input, Session session) {
        Command command = Parser.build(input, session);
        if (command.check()) {
            command.execute();
        } else {
            System.out.println(command.hint());
        }
        return !command.exitsApplication();
    }

}
