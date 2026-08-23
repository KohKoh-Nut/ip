package commands;

import commands.impl.ByeCommand;
import commands.impl.DeadlineCommand;
import commands.impl.DeleteCommand;
import commands.impl.EventCommand;
import commands.impl.HelpCommand;
import commands.impl.ListCommand;
import commands.impl.MarkCommand;
import commands.impl.TodoCommand;
import commands.impl.UnknownCommand;
import commands.impl.UnmarkCommand;
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
    /** The complete line entered by the user. */
    protected final String input;

    /** The session this command may read or update. */
    protected final Session session;

    /**
     * Creates a command with the input and session it will operate on.
     *
     * @param input the complete line entered by the user
     * @param session the current session
     */
    protected Command(String input, Session session) {
        this.input = input;
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
        Command command = createCommand(input, session);
        if (command.check()) {
            command.execute();
        } else {
            System.out.println(command.hint());
        }
        return !command.exitsApplication();
    }

    /**
     * Selects the command subclass that handles the given input.
     *
     * @param input the complete line entered by the user
     * @param session the current session
     * @return the command responsible for the input
     */
    private static Command createCommand(String input, Session session) {
        if (input.equals(ByeCommand.COMMAND)) return new ByeCommand(input, session);
        if (input.equals(ListCommand.COMMAND)) return new ListCommand(input, session);
        if (input.equals(HelpCommand.COMMAND)) return new HelpCommand(input, session);
        if (input.equals(MarkCommand.COMMAND) || input.startsWith(MarkCommand.COMMAND + " ")) {
            return new MarkCommand(input, session);
        }
        if (input.equals(UnmarkCommand.COMMAND) || input.startsWith(UnmarkCommand.COMMAND + " ")) {
            return new UnmarkCommand(input, session);
        }
        if (input.equals(DeleteCommand.COMMAND) || input.startsWith(DeleteCommand.COMMAND + " ")) {
            return new DeleteCommand(input, session);
        }
        if (input.equals(TodoCommand.COMMAND) || input.startsWith(TodoCommand.COMMAND + " ")) {
            return new TodoCommand(input, session);
        }
        if (input.equals(DeadlineCommand.COMMAND) || input.startsWith(DeadlineCommand.COMMAND + " ")) {
            return new DeadlineCommand(input, session);
        }
        if (input.equals(EventCommand.COMMAND) || input.startsWith(EventCommand.COMMAND + " ")) {
            return new EventCommand(input, session);
        }
        return new UnknownCommand(input, session);
    }
}
