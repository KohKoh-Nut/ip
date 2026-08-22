package tasks;

/** Represents a task that must be completed by a specified time. */
public class Deadline extends Task {
    private final String by;

    /** Creates a deadline task. */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    /** Returns the Duke code for deadline tasks. */
    @Override
    protected String getTaskType() {
        return "D";
    }

    /** Returns the deadline time as list details. */
    @Override
    protected String getAdditionalDetails() {
        return " (by: " + by + ")";
    }
}
