/** Adds a task with a start and end time. */
public class EventCommand extends TaskCommand {
    /** The user input that invokes this command. */
    public static final String COMMAND = "event";
    /** Tokens required after the command name. */
    private static final String[] REQUIRED_TOKENS = {"/from", "/to"};

    /** Creates a command that adds an event task. */
    public EventCommand(String input, TaskList taskList) { super(input, taskList); }

    /** {@inheritDoc} */
    @Override public void execute() {
        String[] details = details();
        addTask(new Event(details[0], details[1], details[2]));
    }
    /** {@inheritDoc} */
    @Override public boolean check() {
        String[] details = details();
        return details != null && !details[0].isBlank() && !details[1].isBlank() && !details[2].isBlank();
    }
    /** {@inheritDoc} */
    @Override public String hint() {
        return " Use '" + COMMAND + " DESCRIPTION " + REQUIRED_TOKENS[0]
                + " START " + REQUIRED_TOKENS[1] + " END'.";
    }

    /** Splits the description, start time, and end time around the required tokens. */
    private String[] details() {
        String[] descriptionAndTimes = input.substring(COMMAND.length()).trim()
                .split(" " + REQUIRED_TOKENS[0] + " ", 2);
        if (descriptionAndTimes.length != 2) return null;
        String[] times = descriptionAndTimes[1].split(" " + REQUIRED_TOKENS[1] + " ", 2);
        if (times.length != 2) return null;
        return new String[] {descriptionAndTimes[0].trim(), times[0].trim(), times[1].trim()};
    }
}
