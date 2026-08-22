import java.util.Scanner;

/**
 * Runs Zabud, a command-line assistant that stores tasks for the current session.
 */
public class Zabud {
    /** The maximum number of tasks that can be stored in one session. */
    private static final int MAX_TASKS = 100;

    /** The line printed between sections of the command-line interface. */
    private static final String SEPARATOR = "____________________________________________________________";

    /** Prevents instantiation of this application entry-point class. */
    private Zabud() {
    }

    /** Commands understood by Zabud and the action performed by each command. */
    private enum Command {
        /** Ends the current session. */
        BYE {
            @Override
            boolean execute(Session session, String input) {
                System.out.println("Bye, King Solomon. Hope to see you again soon!");
                System.out.println(SEPARATOR);
                return false;
            }
        },

        /** Displays all tasks entered in the current session. */
        LIST {
            @Override
            boolean execute(Session session, String input) {
                printTasks(session.tasks, session.taskCount);
                return true;
            }
        },

        /** Adds the input as a new task. */
        ADD {
            @Override
            boolean execute(Session session, String input) {
                if (session.taskCount < MAX_TASKS) {
                    session.tasks[session.taskCount] = input;
                    session.taskCount++;
                    System.out.println(" added: " + input);
                } else {
                    System.out.println(" Sorry, your task list is full.");
                }
                return true;
            }
        };

        /**
         * Performs this command's task.
         *
         * @param session the session whose state may be read or updated
         * @param input the complete line entered by the user
         * @return {@code true} when the application should continue running
         */
        abstract boolean execute(Session session, String input);

        /**
         * Converts user input into a command and dispatches it through a switch.
         * Any input that is not a built-in command is treated as a task to add.
         *
         * @param input the complete line entered by the user
         * @param session the current session
         * @return {@code true} when the application should continue running
         */
        static boolean executeCommand(String input, Session session) {
            Command command = switch (input) {
                case "bye" -> BYE;
                case "list" -> LIST;
                default -> ADD;
            };
            return command.execute(session, input);
        }
    }

    /** Stores the mutable state shared by command actions during one session. */
    private static final class Session {
        /** Tasks in the order in which they were entered. */
        private final String[] tasks = new String[MAX_TASKS];

        /** Number of occupied elements in {@link #tasks}. */
        private int taskCount;

        /** Creates an empty task session. */
        private Session() {
        }
    }

    /**
     * Starts the Zabud command-line application and processes input until EOF
     * or the {@code bye} command is received.
     *
     * @param args command-line arguments; not used by this application
     */
    public static void main(String[] args) {
        String banner =
            "███████╗  █████╗  ██████╗  ██╗   ██╗ ██████╗\n" +
            "╚══███╔╝ ██╔══██╗ ██╔══██╗ ██║   ██║ ██╔══██╗\n" +
            "  ███╔╝  ███████║ ██████╔╝ ██║   ██║ ██║  ██║\n" +
            " ███╔╝   ██╔══██║ ██╔══██╗ ██║   ██║ ██║  ██║\n" +
            "███████╗ ██║  ██║ ██████╔╝ ╚██████╔╝ ██████╔╝\n" +
            "╚══════╝ ╚═╝  ╚═╝ ╚═════╝   ╚═════╝  ╚═════╝";

        Session session = new Session();

        System.out.println(SEPARATOR);
        System.out.println(banner);
        System.out.println("Hello, King Solomon! I'm Zabud, your royal assistant.");
        System.out.println("What can I do for you?");
        System.out.println(SEPARATOR);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            System.out.println(SEPARATOR);

            if (!Command.executeCommand(command, session)) {
                break;
            }
        }
    }

    /**
     * Prints all tasks in the order in which they were entered.
     *
     * @param tasks the task storage to read
     * @param taskCount the number of tasks currently stored
     */
    private static void printTasks(String[] tasks, int taskCount) {
        for (int i = 0; i < taskCount; i++) {
            System.out.println(" " + (i + 1) + ". " + tasks[i]);
        }
    }
}
