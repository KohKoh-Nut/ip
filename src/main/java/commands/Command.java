package commands;

import tasks.TaskList;

/** Defines a command that can validate and act on one line of user input. */
public abstract class Command {
    /** The complete line entered by the user. */
    protected final String input;

    /** The task list this command may read or update. */
    protected final TaskList taskList;

    /**
     * Creates a command with the input and task list it will operate on.
     *
     * @param input the complete line entered by the user
     * @param taskList the task list for the current session
     */
    protected Command(String input, TaskList taskList) {
        this.input = input;
        this.taskList = taskList;
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
     * @param taskList the task list for the current session
     * @return whether the application should continue running
     */
    public static boolean invoke(String input, TaskList taskList) {
        Command command = createCommand(input, taskList);
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
     * @param taskList the task list for the current session
     * @return the command responsible for the input
     */
    private static Command createCommand(String input, TaskList taskList) {
        if (input.equals(ByeCommand.COMMAND)) return new ByeCommand(input, taskList);
        if (input.equals(ListCommand.COMMAND)) return new ListCommand(input, taskList);
        if (input.equals(MarkCommand.COMMAND) || input.startsWith(MarkCommand.COMMAND + " ")) {
            return new MarkCommand(input, taskList);
        }
        if (input.equals(UnmarkCommand.COMMAND) || input.startsWith(UnmarkCommand.COMMAND + " ")) {
            return new UnmarkCommand(input, taskList);
        }
        if (input.equals(TodoCommand.COMMAND) || input.startsWith(TodoCommand.COMMAND + " ")) {
            return new TodoCommand(input, taskList);
        }
        if (input.equals(DeadlineCommand.COMMAND) || input.startsWith(DeadlineCommand.COMMAND + " ")) {
            return new DeadlineCommand(input, taskList);
        }
        if (input.equals(EventCommand.COMMAND) || input.startsWith(EventCommand.COMMAND + " ")) {
            return new EventCommand(input, taskList);
        }
        return new UnknownCommand(input, taskList);
    }
}
