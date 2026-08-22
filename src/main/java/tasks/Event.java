package tasks;

/** Represents a task that occurs between a start and end time. */
public class Event extends Task {
    /** The event start time. */
    private final String from;

    /** The event end time. */
    private final String to;

    /**
     * Creates an event task.
     *
     * @param description the text describing the event
     * @param from the event start time
     * @param to the event end time
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns the Duke code for event tasks.
     *
     * @return {@code "E"}
     */
    @Override
    protected String getTaskType() {
        return "E";
    }

    /**
     * Returns the event's time range as list details.
     *
     * @return the event time range formatted for the task list
     */
    @Override
    protected String getAdditionalDetails() {
        return " (from: " + from + " to: " + to + ")";
    }
}
