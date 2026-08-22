/** Provides shared behavior for commands that add a task. */
public abstract class TaskCommand extends Command {
    /**
     * Creates a command that adds a task.
     *
     * @param input the complete line entered by the user
     * @param taskList the task list for the current session
     */
    protected TaskCommand(String input, TaskList taskList) {
        super(input, taskList);
    }

    /**
     * Adds a task and prints the common confirmation.
     *
     * @param task the validated task to add
     */
    protected void addTask(Task task) {
        if (taskList.add(task)) {
            System.out.println(" Got it. I've added this task:");
            System.out.println("   " + task);
        } else {
            System.out.println(" Sorry, your task list is full.");
        }
    }
}
