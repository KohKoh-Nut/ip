import java.util.Scanner;

/** A small command-line assistant that stores tasks for the current session. */
public class Zabud {
    private static final int MAX_TASKS = 100;
    private static final String SEPARATOR = "____________________________________________________________";

    public static void main(String[] args) {
        String banner =
            "███████╗  █████╗  ██████╗  ██╗   ██╗ ██████╗\n" +
            "╚══███╔╝ ██╔══██╗ ██╔══██╗ ██║   ██║ ██╔══██╗\n" +
            "  ███╔╝  ███████║ ██████╔╝ ██║   ██║ ██║  ██║\n" +
            " ███╔╝   ██╔══██║ ██╔══██╗ ██║   ██║ ██║  ██║\n" +
            "███████╗ ██║  ██║ ██████╔╝ ╚██████╔╝ ██████╔╝\n" +
            "╚══════╝ ╚═╝  ╚═╝ ╚═════╝   ╚═════╝  ╚═════╝";

        String[] tasks = new String[MAX_TASKS];
        int taskCount = 0;

        System.out.println(SEPARATOR);
        System.out.println(banner);
        System.out.println("Hello, King Solomon! I'm Zabud, your royal assistant.");
        System.out.println("What can I do for you?");
        System.out.println(SEPARATOR);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            System.out.println(SEPARATOR);

            if (command.equals("bye")) {
                System.out.println("Bye, King Solomon. Hope to see you again soon!");
                System.out.println(SEPARATOR);
                break;
            } else if (command.equals("list")) {
                printTasks(tasks, taskCount);
            } else if (taskCount < MAX_TASKS) {
                tasks[taskCount] = command;
                taskCount++;
                System.out.println(" added: " + command);
            } else {
                System.out.println(" Sorry, your task list is full.");
            }
        }
    }

    /** Prints all tasks in the order in which they were entered. */
    private static void printTasks(String[] tasks, int taskCount) {
        for (int i = 0; i < taskCount; i++) {
            System.out.println(" " + (i + 1) + ". " + tasks[i]);
        }
    }
}
