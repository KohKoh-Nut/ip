package session;

import tasks.Task;

/** Stores the tasks entered during one Zabud session. */
public class TaskList {
    /** The maximum number of tasks in one session. */
    private static final int MAX_TASKS = 100;

    /** Tasks stored in entry order. */
    private final Task[] tasks = new Task[MAX_TASKS];

    /** Number of occupied elements in {@link #tasks}. */
    private int taskCount;

    /** Creates an empty task list. */
    public TaskList() {
    }

    /**
     * Adds a task when there is remaining capacity.
     *
     * @param task the task to add
     * @return whether the task was added
     */
    public boolean add(Task task) {
        if (taskCount == MAX_TASKS) {
            return false;
        }
        tasks[taskCount++] = task;
        return true;
    }

    /**
     * Returns the task at a one-based number, or {@code null} when invalid.
     *
     * @param taskNumber the one-based task number
     * @return the requested task, or {@code null} if it does not exist
     */
    public Task get(int taskNumber) {
        if (taskNumber < 1 || taskNumber > taskCount) {
            return null;
        }
        return tasks[taskNumber - 1];
    }

    /**
     * Removes and returns the task at a one-based number.
     * Tasks after the removed task are shifted forward to preserve their order.
     *
     * @param taskNumber the one-based task number
     * @return the removed task, or {@code null} if the number is invalid
     */
    public Task remove(int taskNumber) {
        Task task = get(taskNumber);
        if (task == null) {
            return null;
        }

        int taskIndex = taskNumber - 1;
        for (int i = taskIndex; i < taskCount - 1; i++) {
            tasks[i] = tasks[i + 1];
        }
        tasks[--taskCount] = null;
        return task;
    }

    /** Prints all tasks in their entry order. */
    public void printTasks() {
        System.out.println(" Here are the tasks in your list:");
        for (int i = 0; i < taskCount; i++) {
            System.out.println(" " + (i + 1) + "." + tasks[i]);
        }
    }
}
