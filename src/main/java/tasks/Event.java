package tasks;

/** Represents a task that occurs between a start and end time. */
public class Event extends Task {
    private final String from;
    private final String to;

    /** Creates an event task. */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /** Returns the Duke code for event tasks. */
    @Override
    protected String getTaskType() {
        return "E";
    }

    /** Returns the event's time range as list details. */
    @Override
    protected String getAdditionalDetails() {
        return " (from: " + from + " to: " + to + ")";
    }
}
