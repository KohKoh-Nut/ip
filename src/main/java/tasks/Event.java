package tasks;

import java.time.LocalDate;
import java.time.LocalTime;

/** Represents a task that occurs between a start and end time. */
public class Event extends Task {
    /** The event start time. */
    private final LocalDate fromDate;
    private final LocalTime fromTime;

    /** The event end time. */
    private final LocalDate toDate;
    private final LocalTime toTime;

    /** {@inheritDoc} */
    @Override
    public String[] getStorageDetails() {
        return new String[] {text(fromDate), text(fromTime), text(toDate), text(toTime)};
    }

    /** @return the event start date or time */
    public LocalDate getFromDate() { return fromDate; }
    /** @return the event start time */
    public LocalTime getFromTime() { return fromTime; }

    /** @return the event end date or time */
    public LocalDate getToDate() { return toDate; }
    /** @return the event end time */
    public LocalTime getToTime() { return toTime; }

    /**
     * Creates an event task.
     *
     * @param description the text describing the event
     * @param from the event start time
     * @param to the event end time
     */
    public Event(String description, LocalDate fromDate, LocalTime fromTime, LocalDate toDate, LocalTime toTime) {
        super(description);
        this.fromDate = fromDate; this.fromTime = fromTime;
        this.toDate = toDate; this.toTime = toTime;
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
        return " (from: " + combined(fromDate, fromTime) + " to: " + combined(toDate, toTime) + ")";
    }

    private static String text(Object value) { return value == null ? "" : value.toString(); }
    private static String combined(LocalDate date, LocalTime time) {
        return date == null ? text(time) : time == null ? text(date) : date + " " + time;
    }
}
