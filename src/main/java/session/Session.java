package session;

import java.io.IOException;
import java.nio.file.Path;
import tasks.Task;
import java.util.ArrayList;
import java.util.List;

/**
 * Stores all mutable information that belongs to one running Zabud session.
 * Future session-level state, such as storage settings, can be added here.
 */
public class Session {
    /** Maximum number of commands retained for interactive recall. */
    public static final int COMMAND_HISTORY_LIMIT = 1000;
    /** Relative path used for persistent task data. */
    public static final Path DEFAULT_DATA_PATH = Path.of("data", "zabud.txt");
    /** The task list managed during this session. */
    private final TaskList taskList;

    /** File used to persist this session. */
    private final Path dataPath;

    private final TaskStorage storage = new TaskStorage();
    private final List<String> commandHistory = new ArrayList<>();
    private int historyCursor;

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

    /** Records a command, retaining only the most recent 1000 entries. */
    public void recordCommand(String command) {
        if (command.isBlank()) return;
        commandHistory.add(command);
        if (commandHistory.size() > COMMAND_HISTORY_LIMIT) commandHistory.remove(0);
        historyCursor = commandHistory.size();
    }

    /** Moves to and returns the previous command, or an empty string at the beginning. */
    public String previousCommand() {
        if (historyCursor > 0) historyCursor--;
        return historyCursor < commandHistory.size() ? commandHistory.get(historyCursor) : "";
    }

    /** Moves to and returns the next command, or an empty string after the newest command. */
    public String nextCommand() {
        if (historyCursor < commandHistory.size()) historyCursor++;
        return historyCursor < commandHistory.size() ? commandHistory.get(historyCursor) : "";
    }

    /** Resets navigation so the next previous request starts at the newest command. */
    public void resetHistoryNavigation() { historyCursor = commandHistory.size(); }

    /** Saves current session data and reports a user-friendly error if it fails. */
    public void save() {
        try {
            storage.save(dataPath, taskList);
        } catch (IOException exception) {
            System.out.println(" I could not save your tasks: " + exception.getMessage());
        }
    }
}
