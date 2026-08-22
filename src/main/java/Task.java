/**
 * Represents a task and whether it has been completed.
 */
public abstract class Task {
    /** The text describing this task. */
    protected String description;

    /** Whether this task has been completed. */
    protected boolean isDone;

    /**
     * Creates an incomplete task with the given description.
     *
     * @param description the text describing the task
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /** Marks this task as completed. */
    public void markAsDone() {
        isDone = true;
    }

    /** Marks this task as incomplete. */
    public void markAsNotDone() {
        isDone = false;
    }

    /**
     * Returns the task description.
     *
     * @return the task description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the status icon used when displaying this task.
     *
     * @return {@code "X"} for a completed task or a space otherwise
     */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /** Returns the single-letter code that identifies this task type. */
    protected abstract String getTaskType();

    /** Returns the task-specific information shown after its description. */
    protected String getAdditionalDetails() {
        return "";
    }

    /** Returns this task in the Duke list format. */
    @Override
    public String toString() {
        return "[" + getTaskType() + "][" + getStatusIcon() + "] "
                + description + getAdditionalDetails();
    }
}
