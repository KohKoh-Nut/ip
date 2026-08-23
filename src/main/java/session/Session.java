package session;

import java.io.IOException;
import java.nio.file.Path;
import tasks.Task;

/**
 * Stores all mutable information that belongs to one running Zabud session.
 * Future session-level state, such as storage settings, can be added here.
 */
public class Session {
    /** Relative path used for persistent task data. */
    public static final Path DEFAULT_DATA_PATH = Path.of("data", "zabud.txt");
    /** The task list managed during this session. */
    private final TaskList taskList;

    /** File used to persist this session. */
    private final Path dataPath;

    private final TaskStorage storage = new TaskStorage();

    /** Creates a new session with an empty task list. */
    public Session() {
        this(DEFAULT_DATA_PATH);
    }

    /**
     * Creates a session and loads its tasks from the supplied path.
     *
     * @param dataPath relative or absolute path containing persisted tasks
     */
    public Session(Path dataPath) {
        this.dataPath = dataPath;
        taskList = new TaskList();
        for (Task task : storage.load(dataPath)) taskList.addLoaded(task);
        taskList.setChangeListener(this::save);
    }

    /**
     * Returns the task list managed by this session.
     *
     * @return the current session's task list
     */
    public TaskList getTaskList() {
        return taskList;
    }

    /** Saves current session data and reports a user-friendly error if it fails. */
    public void save() {
        try {
            storage.save(dataPath, taskList);
        } catch (IOException exception) {
            System.out.println(" I could not save your tasks: " + exception.getMessage());
        }
    }
}
