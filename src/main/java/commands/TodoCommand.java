/** Adds a to-do task. */
public class TodoCommand extends TaskCommand {
    /** The user input that invokes this command. */
    public static final String COMMAND = "todo";
    /** Tokens required after the command name. */
    private static final String[] REQUIRED_TOKENS = {};

    /** Creates a command that adds a to-do task. */
    public TodoCommand(String input, TaskList taskList) { super(input, taskList); }

    /** {@inheritDoc} */
    @Override public void execute() { addTask(new Todo(description())); }
    /** {@inheritDoc} */
    @Override public boolean check() { return !description().isEmpty() && REQUIRED_TOKENS.length == 0; }
    /** {@inheritDoc} */
    @Override public String hint() { return " Please provide a description after '" + COMMAND + "'."; }

    /** Returns the task description following the command name. */
    private String description() { return input.substring(COMMAND.length()).trim(); }
}
