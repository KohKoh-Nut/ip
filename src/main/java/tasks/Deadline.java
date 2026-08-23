package tasks;

/** Represents a task that must be completed by a specified time. */
public class Deadline extends Task {
    /** The time by which this task must be completed. */
    private final String by;

    /**
     * Returns the deadline value used when saving this task.
     *
     * @return the deadline value
     */
    public String getBy() {
        return by;
    }

    /** {@inheritDoc} */
    @Override
    public String[] getStorageDetails() {
        return new String[] {by};
    }

    /**
     * Creates a deadline task.
     *
     * @param description the text describing the task
     * @param by the time by which the task must be completed
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    /**
     * Returns the Duke code for deadline tasks.
     *
     * @return {@code "D"}
     */
    @Override
    protected String getTaskType() {
        return "D";
    }

    /**
     * Returns the deadline time as list details.
     *
     * @return the deadline details formatted for the task list
     */
    @Override
    protected String getAdditionalDetails() {
        return " (by: " + by + ")";
    }
}
