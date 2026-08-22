/** Marks a task as done. */
public class MarkCommand extends Command {
    /** The user input that invokes this command. */
    public static final String COMMAND = "mark";
    /** Tokens required after the command name. */
    private static final String[] REQUIRED_TOKENS = {};

    /** Creates a command that marks a task as done. */
    public MarkCommand(String input, TaskList taskList) { super(input, taskList); }

    /** {@inheritDoc} */
    @Override public void execute() {
        Task task = taskList.get(taskNumber());
        task.markAsDone();
        System.out.println(" Nice! I've marked this task as done:");
        System.out.println("   " + task);
    }
    /** {@inheritDoc} */
    @Override public boolean check() {
        return taskNumber() > 0 && taskList.get(taskNumber()) != null && REQUIRED_TOKENS.length == 0;
    }
    /** {@inheritDoc} */
    @Override public String hint() { return " Please specify a valid task number."; }

    /** Parses the one-based task number following the command name. */
    private int taskNumber() {
        try {
            return Integer.parseInt(input.substring(COMMAND.length()).trim());
        } catch (NumberFormatException exception) {
            return -1;
        }
    }
}
