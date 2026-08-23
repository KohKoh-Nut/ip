package tasks;

import java.time.LocalDate;
import java.time.LocalTime;

/** Represents a task that must be completed by a specified time. */
public class Deadline extends Task {
    /** The time by which this task must be completed. */
    private final LocalDate date;
    private final LocalTime time;

    /**
     * Returns the deadline value used when saving this task.
     *
     * @return the deadline value
     */
    public LocalDate getDate() { return date; }
    /** @return the deadline time, or {@code null} when date-only */
    public LocalTime getTime() { return time; }

    /** {@inheritDoc} */
    @Override
    public String[] getStorageDetails() {
        return new String[] {date == null ? "" : date.toString(), time == null ? "" : time.toString()};
    }

    /**
     * Creates a deadline task.
     *
     * @param description the text describing the task
     * @param by the time by which the task must be completed
     */
    public Deadline(String description, LocalDate date, LocalTime time) {
        super(description);
        this.date = date;
        this.time = time;
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
        return " (by: " + (date == null ? time : time == null ? date : date + " " + time) + ")";
    }
}
