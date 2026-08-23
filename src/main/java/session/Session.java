package session;

/**
 * Stores all mutable information that belongs to one running Zabud session.
 * Future session-level state, such as storage settings, can be added here.
 */
public class Session {
    /** The task list managed during this session. */
    private final TaskList taskList;

    /** Creates a new session with an empty task list. */
    public Session() {
        taskList = new TaskList();
    }

    /**
     * Returns the task list managed by this session.
     *
     * @return the current session's task list
     */
    public TaskList getTaskList() {
        return taskList;
    }
}
